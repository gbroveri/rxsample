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

        RegularItemMongo menuItemDoubleCheese = new RegularItemMongo();
        menuItemDoubleCheese.setId("double_cheese_burger");
        menuItemDoubleCheese.setName("Double Cheese Burger");
        menuItemDoubleCheese.setPrice(1200l);

        OptionGroupMongo optionGroupBurger = new OptionGroupMongo();
        optionGroupBurger.setId("burger_optionals");
        optionGroupBurger.setName("Burger optionals");
        optionGroupBurger.setPick(2);

        OptionMongo optionSalad = new OptionMongo();
        optionSalad.setId("salad");
        optionSalad.setName("Salad");
        optionSalad.setPrice(0l);

        OptionMongo optionExtraBurger = new OptionMongo();
        optionExtraBurger.setId("extra_burger");
        optionExtraBurger.setName("Extra burger");
        optionExtraBurger.setPrice(300l);
        optionGroupBurger.setOptions(Arrays.asList(optionSalad, optionExtraBurger, optionBacon));
        menuItemDoubleCheese.setPickOptionals(optionGroupBurger);
        menuItems.add(menuItemDoubleCheese);

        ComboMongo combo = new ComboMongo();
        combo.setId("combo_5");
        combo.setName("Combo 5");
        combo.setPrice(1700l);
        menuItems.add(combo);

        Collection<RegularItemMongo> comboItems = new ArrayList<>();
        combo.setItems(comboItems);
        comboItems.add(menuItemDoubleCheese);

        RegularItemMongo regularItemSideDish = new RegularItemMongo();
        regularItemSideDish.setId("side_dish");
        regularItemSideDish.setName("Side dish");
        regularItemSideDish.setPrice(0l);
        comboItems.add(regularItemSideDish);

        ItemGroupMongo itemGroupSideDish = new ItemGroupMongo();
        itemGroupSideDish.setId("side_dish_group");
        itemGroupSideDish.setName("Side dish");
        itemGroupSideDish.setPick(1);

        RegularItemMongo regularItemOnion = new RegularItemMongo();
        regularItemOnion.setId("onion_rings");
        regularItemOnion.setName("Onion rings");
        regularItemOnion.setPrice(100l);
        itemGroupSideDish.setItems(Arrays.asList(regularItemOnion, regularItemChips));
        regularItemSideDish.setPickItems(itemGroupSideDish);

        RegularItemMongo regularItemBeverage = new RegularItemMongo();
        regularItemBeverage.setId("beverage_group");
        regularItemBeverage.setName("Beverage");
        regularItemBeverage.setPrice(0l);
        comboItems.add(regularItemBeverage);

        ItemGroupMongo itemGroupBeverage = new ItemGroupMongo();
        itemGroupBeverage.setId("beverage_group");
        itemGroupBeverage.setName("Beverage");
        itemGroupBeverage.setPick(1);
        regularItemBeverage.setPickItems(itemGroupBeverage);

        RegularItemMongo regularItemSoda = new RegularItemMongo();
        regularItemSoda.setId("soda");
        regularItemSoda.setName("Soda");
        regularItemSoda.setPrice(0l);
        itemGroupBeverage.setItems(Arrays.asList(regularItemSoda));

        ItemGroupMongo itemGroupSoda = new ItemGroupMongo();
        itemGroupSoda.setId("soda_group");
        itemGroupSoda.setName("Soda");
        itemGroupSoda.setPick(1);
        regularItemSoda.setPickItems(itemGroupSoda);

        RegularItemMongo regularItemCola0 = new RegularItemMongo();
        regularItemCola0.setId("cola");
        regularItemCola0.setName("Cola");
        regularItemCola0.setPrice(0l);

        RegularItemMongo regularItemDrPepper = new RegularItemMongo();
        regularItemDrPepper.setId("dr_pepper");
        regularItemDrPepper.setName("Dr Pepper");
        regularItemDrPepper.setPrice(0l);
        itemGroupSoda.setItems(Arrays.asList(regularItemCola0, regularItemDrPepper));

        RegularItemMongo regularItemJuice = new RegularItemMongo();
        regularItemJuice.setId("juice");
        regularItemJuice.setName("Juice");
        regularItemJuice.setPrice(100l);
        itemGroupBeverage.setItems(Arrays.asList(regularItemSoda, regularItemJuice));

        ItemGroupMongo itemGroupJuice = new ItemGroupMongo();
        itemGroupJuice.setId("juice_group");
        itemGroupJuice.setName("Juice");
        itemGroupJuice.setPick(1);
        regularItemJuice.setPickItems(itemGroupJuice);

        RegularItemMongo regularItemOrange = new RegularItemMongo();
        regularItemOrange.setId("orange");
        regularItemOrange.setName("Orange");
        regularItemOrange.setPrice(100l);

        RegularItemMongo regularItemLemon = new RegularItemMongo();
        regularItemLemon.setId("lemon");
        regularItemLemon.setName("Lemon");
        regularItemLemon.setPrice(100l);
        itemGroupJuice.setItems(Arrays.asList(regularItemOrange, regularItemLemon));

        return restaurant;
    }


}
