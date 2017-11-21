package com.ifd.menu.usecases;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Promotion;
import com.ifd.menu.gateways.PromotionGateway;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindPromotions {
    private final PromotionGateway promotionGateway;

    public Observable<Promotion> execute(final String restaurantId, IfdContext context) {
        return promotionGateway.findPromotion(restaurantId, context);
    }
}
