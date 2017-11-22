package com.ifd.menu.gateways.feign;

import com.ifd.menu.config.FeignConfiguration;
import com.ifd.menu.domains.promotion.IfdContext;
import com.ifd.menu.domains.promotion.Promotion;
import com.ifd.menu.gateways.PromotionGateway;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PromotionGatewayImplTest {

    private MockWebServer server;
    private PromotionClient promotionClient;
    private PromotionGateway promotionGateway;

    @Before
    public void setup() {
        server = new MockWebServer();
        promotionClient =
            FeignConfiguration
                .createClient(PromotionClient.class, server.url("/").uri().toString(), new PromotionFallback());
        promotionGateway = new PromotionGatewayImpl(promotionClient);
    }

    @Test
    public void testFallbackDefaultPromoWhenPromotionServiceReturns500() {
        server.enqueue(new MockResponse().setResponseCode(500));
        Observable<Promotion> promotionObservable = promotionGateway.findPromotion("teste", new IfdContext());

        TestObserver<Promotion> promotionTestObserver = promotionObservable.test()
            .assertComplete()
            .assertNoErrors();
        final Promotion promotion = promotionTestObserver.values().get(0);
        assertTrue(promotion.getDiscount() == 0);
        assertTrue(promotion.getId().equals("default_promo"));
    }

    @Test
    public void testFallbackDefaultPromoWhenPromotionServiceReturns404() {
        server.enqueue(new MockResponse().setResponseCode(404));
        Observable<Promotion> promotionObservable = promotionGateway.findPromotion("teste", new IfdContext());

        TestObserver<Promotion> promotionTestObserver = promotionObservable.test()
            .assertComplete()
            .assertNoErrors();
        final Promotion promotion = promotionTestObserver.values().get(0);
        assertTrue(promotion.getDiscount() == 0);
        assertTrue(promotion.getId().equals("default_promo"));
    }

    @Test
    public void testValidPromotionResponse() {
        server.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\":\"special_customer\",\"discount\":20}"));
        IfdContext ifdContext = new IfdContext();
        ifdContext.setUid("john@doe.com");
        Observable<Promotion> promotionObservable = promotionGateway.findPromotion("teste", ifdContext);

        TestObserver<Promotion> promotionTestObserver = promotionObservable.test()
            .assertComplete()
            .assertNoErrors();
        final Promotion promotion = promotionTestObserver.values().get(0);
        assertTrue(promotion.getDiscount() == 20);
        assertTrue(promotion.getId().equals("special_customer"));
    }


}
