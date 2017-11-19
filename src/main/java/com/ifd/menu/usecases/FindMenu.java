package com.ifd.menu.usecases;

import com.ifd.menu.domains.*;
import com.ifd.menu.gateways.RestaurantGateway;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindMenu {

    private final RestaurantGateway restaurantGateway;
    private final FindPromotions findPromotions;

    @Autowired
    public FindMenu(
        final RestaurantGateway restaurantGateway,
        final FindPromotions findPromotions) {

        this.restaurantGateway = restaurantGateway;
        this.findPromotions = findPromotions;
    }

    public Observable<Restaurant> execute(final String restaurantId, final IfdContext context) {
        final Promotion promotion =
            findPromotions.execute(restaurantId, context).singleElement().blockingGet(new Promotion());
        final Observable<Restaurant> restaurantObservable = restaurantGateway.findById(restaurantId);
        if (promotion != null) {
            restaurantObservable.flatMapIterable(restaurant -> restaurant.getMenus())
                .flatMapIterable(menu -> menu.getItems())
                .flatMap(menuItem -> getMenuItems(menuItem))
                .forEach(menuItem -> applyDiscount(menuItem, promotion));
        }
        return restaurantObservable;
    }

    private Observable<MenuItem> getMenuItems(final MenuItem item) {
        if (item instanceof Combo) {
            final Combo comboItem = (Combo) item;
            if (comboItem.getItems() != null) {
                return getMenuItemsFromCombo(item, comboItem);
            }
        } else if (item instanceof RegularItem) {
            final RegularItem regularItem = (RegularItem) item;
            if (regularItem.getPickItems() != null) {
                return getMenuItemsFromRegularItem(item, regularItem);
            }
        }
        return Observable.just(item);
    }

    private Observable<MenuItem> getMenuItemsFromRegularItem(final MenuItem item, final RegularItem regularItem) {
        return Observable.merge(
            Observable.just(item),
            Observable.fromIterable(regularItem.getPickItems().getItems())
                .flatMap(this::getMenuItems));
    }

    private Observable<MenuItem> getMenuItemsFromCombo(final MenuItem item, final Combo comboItem) {
        return Observable.merge(
            Observable.just(item),
            Observable.fromIterable(comboItem.getItems())
                .flatMap(this::getMenuItems)
        );
    }

    private void applyDiscount(final MenuItem item, final Promotion promotion) {
        if (item instanceof Combo) {
            final Combo comboItem = (Combo) item;
            comboItem.setPrice(calculatePrice(comboItem.getPrice(), promotion));
        } else if (item instanceof RegularItem) {
            final RegularItem regularItem = (RegularItem) item;
            regularItem.setPrice(calculatePrice(regularItem.getPrice(), promotion));
            final OptionGroup pickOptionals = regularItem.getPickOptionals();
            if (pickOptionals != null) {
                Observable.fromIterable(pickOptionals.getOptions())
                    .forEach(option -> option.setPrice(calculatePrice(option.getPrice(), promotion)));
            }
        }
    }

    private long calculatePrice(final Long basePrice, final Promotion promotion) {
        return (long) (basePrice * ((100f - promotion.getDiscount()) / 100));
    }

}
