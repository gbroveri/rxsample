package com.ifd.menu.usecases;

import com.ifd.menu.domains.*;
import com.ifd.menu.domains.promotion.IfdContext;
import com.ifd.menu.domains.promotion.Promotion;
import com.ifd.menu.gateways.RestaurantGateway;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindRestaurant {

    private final RestaurantGateway restaurantGateway;
    private final FindPromotions findPromotions;

    public Observable<Restaurant> execute(final String restaurantId, final IfdContext context) {
        return Observable.create(e -> {
            Restaurant[] restaurants = new Restaurant[1];
            restaurantGateway.findById(restaurantId).subscribe(
                restaurant -> {
                    restaurants[0] = restaurant;
                    final Promotion promotion = findPromotions.execute(restaurantId, context).blockingSingle();
                    Observable.fromIterable(restaurant.getMenus())
                        .flatMapIterable(menu -> menu.getItems())
                        .flatMap(menuItem -> getMenuItems(menuItem))
                        .forEach(menuItem -> applyDiscount(menuItem, promotion));
                    e.onNext(restaurant);
                    e.onComplete();
                },
                throwable -> e.onError(throwable),
                () -> {
                    if (restaurants[0] == null) {
                        e.onComplete();
                    }
                }
            );
        });
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
