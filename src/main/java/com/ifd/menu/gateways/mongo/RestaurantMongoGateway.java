package com.ifd.menu.gateways.mongo;

import com.ifd.menu.domains.Restaurant;
import com.ifd.menu.gateways.RestaurantGateway;
import com.ifd.menu.gateways.mongo.converters.RestaurantConverter;
import io.reactivex.Maybe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantMongoGateway implements RestaurantGateway {

    private final RestaurantRepository repository;

    @Override
    public Maybe<Restaurant> findById(final String restaurantId) {
        return RestaurantConverter.newConverter().convert(repository.findById(restaurantId));
    }

}
