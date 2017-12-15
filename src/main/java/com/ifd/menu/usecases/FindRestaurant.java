package com.ifd.menu.usecases;

import com.ifd.menu.domains.*;
import com.ifd.menu.domains.promotion.IfdContext;
import com.ifd.menu.domains.promotion.Promotion;
import com.ifd.menu.gateways.RestaurantGateway;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FindRestaurant {

    private final RestaurantGateway restaurantGateway;
    private final FindPromotions findPromotions;
    private final Map<Class<? extends MenuItem>, GetItems> getItemStrategies;

    @Autowired
    public FindRestaurant(final RestaurantGateway restaurantGateway,
        final FindPromotions findPromotions) {
        this.restaurantGateway = restaurantGateway;
        this.findPromotions = findPromotions;
        getItemStrategies = Stream.of(
            new AbstractMap.SimpleEntry<>(RegularItem.class, new GetRegularItem()),
            new AbstractMap.SimpleEntry<>(Combo.class, new GetComboItem()))
            .collect(
                Collectors.toMap(
                    AbstractMap.SimpleEntry::getKey,
                    AbstractMap.SimpleEntry::getValue
                ));
    }

    public Observable<Restaurant> execute(final String restaurantId, final IfdContext context) {
        return Observable.combineLatest(
            restaurantGateway.findById(restaurantId).toObservable(),
            findPromotions.execute(restaurantId, context),
            this::applyPromotion);
    }

    private Restaurant applyPromotion(final Restaurant restaurant, final Promotion promotion) {
        Observable.fromIterable(restaurant.getMenus())
            .flatMapIterable(Menu::getItems)
            .flatMap(menuItem -> getItemStrategies.get(menuItem.getClass()).getMenuItems(menuItem))
            .forEach(menuItem -> applyDiscount((MenuItem) menuItem, promotion));
        return restaurant;
    }

    private void applyDiscount(final MenuItem item, final Promotion promotion) {
        item.setPrice(calculatePrice(item.getPrice(), promotion));
        if (item instanceof RegularItem) {
            final RegularItem regularItem = (RegularItem) item;
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

    private interface GetItems<T extends MenuItem> {
        Observable<MenuItem> getMenuItems(T item);
    }

    private static class GetRegularItem implements GetItems<RegularItem> {
        public Observable<MenuItem> getMenuItems(final RegularItem item) {
            if (item.getPickItems() != null) {
                return Observable.merge(
                    Observable.just(item),
                    Observable.fromIterable(item.getPickItems().getItems())
                        .flatMap(this::getMenuItems));
            }
            return Observable.just(item);
        }
    }

    private static class GetComboItem implements GetItems<Combo> {
        private GetRegularItem getRegularItem = new GetRegularItem();

        public Observable<MenuItem> getMenuItems(final Combo item) {
            if (item.getItems() != null) {
                return Observable.merge(
                    Observable.just(item),
                    Observable.fromIterable(item.getItems())
                        .flatMap(getRegularItem::getMenuItems)
                );
            }
            return Observable.just(item);
        }
    }


}
