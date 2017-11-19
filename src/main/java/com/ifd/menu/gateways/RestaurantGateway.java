package com.ifd.menu.gateways;

import com.ifd.menu.domains.Restaurant;
import io.reactivex.Observable;

public interface RestaurantGateway {
    Observable<Restaurant> findById(String restaurantId);
}
