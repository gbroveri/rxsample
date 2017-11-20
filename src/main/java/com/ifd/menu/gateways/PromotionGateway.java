package com.ifd.menu.gateways;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Promotion;
import io.reactivex.Observable;

public interface PromotionGateway {
    Observable<Promotion> findPromotion(final String restaurantId, final IfdContext context);
}
