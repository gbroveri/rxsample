package com.ifd.menu.gateways.mongo;

import com.ifd.menu.domains.Restaurant;
import com.ifd.menu.gateways.mongo.documents.RestaurantMongo;
import io.reactivex.Flowable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantMongoGatewayIntegrationTest {

    @Autowired
    private RestaurantMongoGateway gateway;

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private ReactiveMongoOperations operations;

    @Before
    public void setUp() {
        operations.collectionExists(RestaurantMongo.class) //
            .flatMap(exists -> exists ? operations.dropCollection(RestaurantMongo.class) : Mono.just(exists))
            .flatMap(o -> operations.createCollection(RestaurantMongo.class))
            .then()
            .block();
        repository.saveAll(Flowable.just(buildRestaurant())).blockingLast();
    }

    @Test
    public void testFindExistingMenu() throws InterruptedException {
        //when
        Restaurant restaurant = gateway.findById("test_restaurant").blockingGet();

        //then
        Assert.assertTrue(restaurant.getName().equals("Test Restaurant"));
    }

    public RestaurantMongo buildRestaurant() {
        final RestaurantMongo restaurant = new RestaurantMongo();
        restaurant.setId("test_restaurant");
        restaurant.setName("Test Restaurant");
        restaurant.setTags(Arrays.asList("burger", "american food", "bacon"));
        return restaurant;
    }

}
