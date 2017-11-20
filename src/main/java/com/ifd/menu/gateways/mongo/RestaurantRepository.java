package com.ifd.menu.gateways.mongo;

import com.ifd.menu.gateways.mongo.documents.RestaurantMongo;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;

public interface RestaurantRepository extends RxJava2CrudRepository<RestaurantMongo, String> {
}
