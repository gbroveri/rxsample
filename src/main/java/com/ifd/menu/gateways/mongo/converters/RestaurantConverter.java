package com.ifd.menu.gateways.mongo.converters;

import com.ifd.menu.domains.*;
import com.ifd.menu.gateways.Converter;
import com.ifd.menu.gateways.mongo.documents.*;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;

public class RestaurantConverter {

    public static final String MENUS_PROP = "menus";
    public static final String ITEMS_PROP = "items";

    private RestaurantConverter() {
        //
    }

    public static Converter<Maybe<RestaurantMongo>, Maybe<Restaurant>> newConverter() {
        return restaurantMongoMaybe -> restaurantMongoMaybe.map(restaurantMongo -> {
            final Restaurant restaurant = new Restaurant();
            final Collection<Menu> menus = new ArrayList<>();
            restaurant.setMenus(menus);
            BeanUtils.copyProperties(restaurantMongo, restaurant, MENUS_PROP);
            Observable.fromIterable(restaurantMongo.getMenus())
                .subscribe(menuMongo -> menus.add(menuConverter().convert(menuMongo)));
            if (restaurantMongo.getChain() != null) {
                restaurant.setChain(chainConverter().convert(restaurantMongo.getChain()));
            }
            return restaurant;
        });
    }

    static Converter<ChainMongo, Chain> chainConverter() {
        return chainMongo -> {
            final Chain chain = new Chain();
            final Collection<Menu> menus = new ArrayList<>();
            chain.setMenus(menus);
            BeanUtils.copyProperties(chainMongo, chain, MENUS_PROP);
            if (chainMongo.getMenus() != null) {
                Observable.fromIterable(chainMongo.getMenus())
                    .subscribe(menuMongo -> menus.add(menuConverter().convert(menuMongo)));
            }
            return chain;
        };
    }

    static Converter<MenuMongo, Menu> menuConverter() {
        return menuMongo -> {
            final Menu menu = new Menu();
            final Collection<MenuItem> menuItems = new ArrayList<>();
            menu.setItems(menuItems);
            BeanUtils.copyProperties(menuMongo, menu, ITEMS_PROP);
            Observable.fromIterable(menuMongo.getItems())
                .subscribe(itemMongo -> menuItems.add(menuItemConverter().convert(itemMongo)));
            return menu;
        };
    }

    static Converter<MenuItemMongo, MenuItem> menuItemConverter() {
        return menuItemMongo -> {
            if (menuItemMongo instanceof ComboMongo) {
                return comboConverter().convert((ComboMongo) menuItemMongo);
            } else {
                return regularItemConverter().convert((RegularItemMongo) menuItemMongo);
            }
        };
    }

    static Converter<ComboMongo, Combo> comboConverter() {
        return comboMongo -> {
            final Combo combo = new Combo();
            final Collection<RegularItem> regularItems = new ArrayList<>();
            combo.setItems(regularItems);
            BeanUtils.copyProperties(comboMongo, combo, ITEMS_PROP);
            Observable.fromIterable(comboMongo.getItems())
                .subscribe(regularItemMongo -> regularItems.add(regularItemConverter().convert(regularItemMongo)));
            return combo;
        };
    }

    static Converter<RegularItemMongo, RegularItem> regularItemConverter() {
        return regularItemMongo -> {
            final RegularItem regularItem = new RegularItem();
            BeanUtils.copyProperties(regularItemMongo, regularItem, "pickOptionals", "pickItems");
            if (regularItemMongo.getPickItems() != null) {
                Observable.just(regularItemMongo.getPickItems())
                    .subscribe(
                        itemGroupMongo -> regularItem.setPickItems(itemGroupConverter().convert(itemGroupMongo)));
            }
            if (regularItemMongo.getPickOptionals() != null) {
                Observable.just(regularItemMongo.getPickOptionals())
                    .subscribe(
                        optionGroupMongo -> regularItem
                            .setPickOptionals(optionGroupConverter().convert(optionGroupMongo)));
            }
            return regularItem;
        };
    }

    static Converter<ItemGroupMongo, ItemGroup> itemGroupConverter() {
        return itemGroupMongo -> {
            final ItemGroup itemGroup = new ItemGroup();
            final Collection<RegularItem> regularItems = new ArrayList<>();
            itemGroup.setItems(regularItems);
            BeanUtils.copyProperties(itemGroupMongo, itemGroup, ITEMS_PROP);
            Observable.fromIterable(itemGroupMongo.getItems())
                .subscribe(regularItemMongo -> regularItems.add(regularItemConverter().convert(regularItemMongo)));
            return itemGroup;
        };
    }

    static Converter<OptionGroupMongo, OptionGroup> optionGroupConverter() {
        return optionGroupMongo -> {
            final OptionGroup optionGroup = new OptionGroup();
            final Collection<Option> options = new ArrayList<>();
            optionGroup.setOptions(options);
            BeanUtils.copyProperties(optionGroupMongo, optionGroup, "options");
            Observable.fromIterable(optionGroupMongo.getOptions())
                .subscribe(optionMongo -> options.add(optionConverter().convert(optionMongo)));
            return optionGroup;
        };
    }

    static Converter<OptionMongo, Option> optionConverter() {
        return optionMongo -> {
            final Option option = new Option();
            BeanUtils.copyProperties(optionMongo, option);
            return option;
        };
    }

}
