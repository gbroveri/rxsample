package com.ifd.menu.gateways;

import com.ifd.menu.domains.Restaurant;
import io.reactivex.Maybe;

public interface RestaurantGateway {
    Maybe<Restaurant> findById(String restaurantId);
}
