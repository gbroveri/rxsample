package com.ifd.menu.gateways.feign;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Promotion;
import com.ifd.menu.gateways.PromotionGateway;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PromotionGatewayImpl implements PromotionGateway {

    private final PromotionClient promotionClient;

    @Override
    public Observable<Promotion> findPromotion(final String restaurantId, final IfdContext context) {
        final Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("restaurantId", restaurantId);
        queryMap.put("source", context.getSource());
        queryMap.put("uid", context.getUid());
        queryMap.put("voucher", context.getVoucher());
        return Observable.just(promotionClient.findPromotion(queryMap));
    }

}
