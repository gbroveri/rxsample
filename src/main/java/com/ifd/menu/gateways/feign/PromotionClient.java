package com.ifd.menu.gateways.feign;

import com.ifd.menu.domains.promotion.Promotion;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface PromotionClient {
    @RequestLine("GET /promotions")
    Promotion findPromotion(@QueryMap Map<String, Object> queryMap);
}
