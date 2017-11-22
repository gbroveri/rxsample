package com.ifd.menu.usecases

import com.ifd.menu.domains.promotion.IfdContext
import com.ifd.menu.domains.promotion.Promotion
import com.ifd.menu.gateways.PromotionGateway
import io.reactivex.Observable
import spock.lang.Specification

class FindPromotionsSpec extends Specification {

    def promotionGateway = Mock(PromotionGateway)
    def findPromotions = new FindPromotions(promotionGateway)

    def "find promotion with success"() {
        given: "exists default_promo with 10% off for restaurant burger_n_chips"
          def defaultPromo = new Promotion(discount: 10, id: "default_promo")
          promotionGateway.findPromotion(_, _) >> Observable.just(defaultPromo)
        when: "find promotions for restaurant burger_n_chips"
          def promo = findPromotions.execute("burger_n_chips", new IfdContext())
        then: "promo default_promo with 10% off is returned"
          def testObserver = promo.test().assertComplete().assertNoErrors()
          testObserver.values()[0].discount == 10
    }

}