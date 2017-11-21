package com.ifd.menu;

import com.ifd.menu.gateways.mongo.RestaurantRepository;
import com.ifd.menu.gateways.mongo.documents.*;
import io.reactivex.Flowable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(RestaurantRepository restaurantRepository) {
        return args -> restaurantRepository.saveAll(Flowable.just(buildRestaurant())).blockingLast();
    }


    private RestaurantMongo buildRestaurant() {
        ChainMongo chain = new ChainMongo();
        chain.setId("chips_n_burgers");
        chain.setName("chips n burgers");

        RestaurantMongo restaurant = new RestaurantMongo();
        restaurant.setId("chips_n_burger_1");
        restaurant.setName("chips n burgers campinas mall 1");
        restaurant.setChain(chain);
        MenuMongo menu = new MenuMongo();
        restaurant.setMenus(Arrays.asList(menu));

        menu.setId("fast_food_menu");
        menu.setName("fast food menu");

        Collection<MenuItemMongo> menuItems = new ArrayList<>();
        menu.setItems(menuItems);

        RegularItemMongo regularItemChips = new RegularItemMongo();
        regularItemChips.setId("chips");
        regularItemChips.setName("Chips");
        regularItemChips.setPrice(400l);

        OptionGroupMongo optionGroupChips = new OptionGroupMongo();
        optionGroupChips.setId("chips_optionals");
        optionGroupChips.setName("Chips adds");
        optionGroupChips.setPick(1);
        OptionMongo optionBacon = new OptionMongo();
        optionBacon.setId("bacon");
        optionBacon.setName("Bacon");
        optionBacon.setPrice(100l);
        optionGroupChips.setOptions(Arrays.asList(optionBacon));

        regularItemChips.setPickOptionals(optionGroupChips);
        menuItems.add(regularItemChips);

        RegularItemMongo regularItemCola = new RegularItemMongo();
        regularItemCola.setId("cola");
        regularItemCola.setName("Cola");
        regularItemCola.setPrice(300l);
        menuItems.add(regularItemCola);

        return restaurant;

    }


}
