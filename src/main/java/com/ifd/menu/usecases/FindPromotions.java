package com.ifd.menu.usecases;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Promotion;
import io.reactivex.Observable;
import org.springframework.stereotype.Component;

@Component
public class FindPromotions {
    public Observable<Promotion> execute(final String restaurantId, IfdContext context) {
        return null;
    }
}
