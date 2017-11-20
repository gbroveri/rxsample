package com.ifd.menu.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifd.menu.gateways.feign.PromotionClient;
import com.ifd.menu.gateways.feign.PromotionFallback;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Value("${feign.promotion.url:}")
    private String promotionUrl;

    @Bean
    public PromotionClient promotionClient() {
        return createClient(PromotionClient.class, promotionUrl, new PromotionFallback());
    }

    public static <T> T createClient(Class<T> type, String uri, FallbackFactory<? extends T> fallbackFactory) {
        final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return HystrixFeign.builder()
            .client(new ApacheHttpClient())
            .encoder(new JacksonEncoder(mapper))
            .decoder(new JacksonDecoder(mapper))
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.FULL)
            .target(type, uri, fallbackFactory);
    }

}
