package com.ifd.menu.http;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Restaurant;
import com.ifd.menu.http.error.NotFoundException;
import com.ifd.menu.usecases.FindRestaurant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Api(value = "restaurants", tags = "rest api for restaurant", produces = APPLICATION_JSON_UTF8_VALUE)
public class RestaurantController {
    private final FindRestaurant findRestaurant;

    @ApiOperation("find restaurant by id")
    @RequestMapping(value = "/{restaurantId}", method = RequestMethod.GET)
    public DeferredResult<Restaurant> findById(
        @PathVariable final String restaurantId,
        @RequestHeader(value = "uid", required = false) final String uid,
        @RequestHeader(value = "source", required = false) final String source,
        @RequestHeader(value = "voucher", required = false) final String voucher
    ) {
        final DeferredResult<Restaurant> deferredResult = new DeferredResult<>();
        final IfdContext ifdContext = new IfdContext();
        ifdContext.setUid(uid);
        ifdContext.setSource(source);
        ifdContext.setVoucher(voucher);
        final Restaurant[] result = new Restaurant[1];
        findRestaurant.execute(restaurantId, ifdContext)
            .subscribe(
                restaurant -> result[0] = restaurant,
                error -> deferredResult.setErrorResult(error),
                () -> {
                    final Restaurant restaurant = result[0];
                    if (restaurant != null) {
                        deferredResult.setResult(restaurant);
                    } else {
                        deferredResult.setErrorResult(new NotFoundException());
                    }
                }
            );
        return deferredResult;
    }

}
