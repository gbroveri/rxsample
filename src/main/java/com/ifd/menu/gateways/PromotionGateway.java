package com.ifd.menu.gateways;

import com.ifd.menu.domains.promotion.IfdContext;
import com.ifd.menu.domains.promotion.Promotion;
import io.reactivex.Observable;

public interface PromotionGateway {
    Observable<Promotion> findPromotion(final String restaurantId, final IfdContext context);
}
