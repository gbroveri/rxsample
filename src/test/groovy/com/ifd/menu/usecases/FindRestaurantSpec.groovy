package com.ifd.menu.usecases

import com.ifd.menu.domains.*
import com.ifd.menu.gateways.RestaurantGateway
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import spock.lang.Specification

class FindRestaurantSpec extends Specification {

    def restaurantGateway = Mock(RestaurantGateway)
    def findPromotions = Mock(FindPromotions)
    def findMenu = new FindRestaurant(restaurantGateway, findPromotions)

    def "find restaurant menu with regular and combo items"() {
        given: "there is a menu for restaurant chips_n_burger_1 with 4 items"
          def mock = sampleRestaurant()
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.just(mock)
          findPromotions.execute(_, _) >> Observable.empty()
        when: "a search by chips_n_burger_1 menus is performed"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext())
        then: "the menu with 4 items is returned"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          def menuItems = observer.values()[0].menus[0].items
          menuItems.size() == 4
    }

    def "find restaurant menu from customer with discount"() {
        given: "there is a menu for restaurant chips_n_burger_1 with 4 items"
          def mock = sampleRestaurant()
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.just(mock)
        and: "user john@doe.com has a promotion 'special_customer' that takes 10% off"
          findPromotions.execute(_, _) >> Observable.just(new Promotion(discount: 10))
        when: "a search is performed by user john@doe.com"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext(uid: "john@doe.com"))
        then: "all menu items have the price with 10% off"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          def menuItems = observer.values()[0].menus[0].items
          menuItems.size() == 4
          menuItems[0].price == 360
          menuItems[0].pickOptionals.options[0].price == 90
          menuItems[1].price == 270
          menuItems[2].price == 1080
          menuItems[2].pickOptionals.options[1].price == 270
    }

    def "find restaurant menus from source partner with discount"() {
        given: "there is a menu for restaurant chips_n_burger_1 with 4 items"
          def mock = sampleRestaurant()
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.just(mock)
        and: "source facebook has a promotion 'facebook_specials' that gives 20% off"
          findPromotions.execute(_, _) >> Observable.just(new Promotion(discount: 20))
        when: "a search is performed from source facebook"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext(source: "facebook"))
        then: "all menu items have the price with 20% off"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          def menuItems = observer.values()[0].menus[0].items
          menuItems.size() == 4
          menuItems[0].price == 320
          menuItems[0].pickOptionals.options[0].price == 80
          menuItems[1].price == 240
          menuItems[2].price == 960
          menuItems[2].pickOptionals.options[1].price == 240
    }

    def "find menu with voucher applied"() {
        given: "there is a menu for restaurant chips_n_burger_1 with 4 items"
          def mock = sampleRestaurant()
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.just(mock)
        and: "voucher XPTO has a promotion that gives 20% off"
          findPromotions.execute(_, _) >> Observable.just(new Promotion(discount: 20))
        when: "a search is performed with voucher XPTO in context"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext(voucher: "XPTO"))
        then: "all menu items have the price with 20% off"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          def menuItems = observer.values()[0].menus[0].items
          menuItems.size() == 4
          menuItems[0].price == 320
          menuItems[0].pickOptionals.options[0].price == 80
          menuItems[1].price == 240
          menuItems[2].price == 960
          menuItems[2].pickOptionals.options[1].price == 240
    }

    def "find restaurant without menus"() {
        given: "exists restaurant chips_n_burger_1 with but without items on menu"
          def mock = sampleRestaurant()
          mock.menus = []
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.just(mock)
          findPromotions.execute(_, _) >> Observable.empty()
        when: "a search is performed by restaurant chips_n_burger_1"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext(voucher: "XPTO"))
        then: "restaurant details is returned without menus"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          observer.values()[0].menus.isEmpty()
    }

    def "find menu from invalid restaurant"() {
        given: "restaurant chips_n_burger_1 does not exist"
          restaurantGateway.findById("chips_n_burger_1") >> Maybe.empty()
          findPromotions.execute(_, _) >> Observable.empty()
        when: "a search is performed by restaurant chips_n_burger_1"
          def restaurant = findMenu.execute("chips_n_burger_1", new IfdContext(voucher: "XPTO"))
        then: "no restaurant is returned"
          final TestObserver observer = new TestObserver<>()
          restaurant.subscribe(observer)
          observer.assertComplete()
          observer.assertNoErrors()
          observer.values().isEmpty()
    }

    def sampleRestaurant() {
        return new Restaurant(
            id: "chips_n_burger_1",
            name: "chips n burgers campinas mall 1",
            chain: new Chain(id: "chips_n_burgers", name: "chips n burgers"),
            menus: Arrays.asList(
                new Menu(
                    id: "fast_food_menu",
                    name: "fast food menu",
                    items: Arrays.asList(
                        new RegularItem(
                            id: "chips",
                            name: "Chips",
                            price: 400,
                            pickOptionals: new OptionGroup(
                                id: "chips_optionals",
                                name: "Chips adds",
                                pick: 1,
                                options: Arrays.asList(
                                    new Option(
                                        id: "bacon",
                                        name: "Bacon",
                                        price: 100)))),
                        new RegularItem(
                            id: "cola",
                            name: "Cola",
                            price: 300),
                        new RegularItem(
                            id: "double_cheese_burger",
                            name: "Double Cheese Burger",
                            price: 1200,
                            pickOptionals: new OptionGroup(
                                id: "burger_optionals",
                                name: "Burger optionals",
                                pick: 2,
                                options: Arrays.asList(
                                    new Option(
                                        id: "salad",
                                        name: "Salad",
                                        price: 0),
                                    new Option(
                                        id: "extra_burger",
                                        name: "Extra burger",
                                        price: 300),
                                    new Option(
                                        id: "bacon",
                                        name: "Bacon",
                                        price: 200)))),
                        new Combo(
                            id: "combo_5",
                            name: "Combo 5",
                            price: 1700,
                            items: Arrays.asList(
                                new RegularItem(
                                    id: "double_cheese_burger",
                                    name: "Double Cheese Burger",
                                    price: 1200,
                                    pickOptionals: new OptionGroup(
                                        id: "burger_optionals",
                                        name: "Burger optionals",
                                        pick: 2,
                                        options: Arrays.asList(
                                            new Option(
                                                id: "salad",
                                                name: "Salad",
                                                price: 0),
                                            new Option(
                                                id: "extra_burger",
                                                name: "Extra burger",
                                                price: 300),
                                            new Option(
                                                id: "bacon",
                                                name: "Bacon",
                                                price: 200)))),
                                new RegularItem(
                                    id: "side_dish",
                                    name: "Side dish",
                                    price: 0,
                                    pickItems: new ItemGroup(
                                        id: "side_dish_group",
                                        name: "Side dish",
                                        pick: 1,
                                        items: Arrays.asList(
                                            new RegularItem(
                                                id: "onion_rings",
                                                name: "Onion Rings",
                                                price: 100),
                                            new RegularItem(
                                                id: "chips",
                                                name: "Chips",
                                                price: 0,
                                                pickOptionals: new OptionGroup(
                                                    id: "chips_optionals",
                                                    name: "Chips adds",
                                                    pick: 1,
                                                    options: Arrays.asList(
                                                        new Option(
                                                            id: "bacon",
                                                            name: "Bacon",
                                                            price: 100))))))),
                                new RegularItem(
                                    id: "beverage",
                                    name: "Beverage",
                                    price: 0,
                                    pickItems: new ItemGroup(
                                        id: "beverage_group",
                                        name: "Beverage",
                                        pick: 1,
                                        items: Arrays.asList(
                                            new RegularItem(
                                                id: "soda",
                                                name: "Soda",
                                                price: 0,
                                                pickItems: new ItemGroup(
                                                    id: "soda_group",
                                                    name: "Soda",
                                                    pick: 1,
                                                    items: Arrays.asList(
                                                        new RegularItem(
                                                            id: "cola",
                                                            name: "Cola",
                                                            price: 0),
                                                        new RegularItem(
                                                            id: "dr_pepper",
                                                            name: "Dr Pepper",
                                                            price: 0)))),
                                            new RegularItem(
                                                id: "juice",
                                                name: "Juice",
                                                price: 100,
                                                pickItems: new ItemGroup(
                                                    id: "juice_group",
                                                    name: "Juice",
                                                    pick: 1,
                                                    items: Arrays.asList(
                                                        new RegularItem(
                                                            id: "orange",
                                                            name: "Orange",
                                                            price: 100),
                                                        new RegularItem(
                                                            id: "lemon",
                                                            name: "Lemon",
                                                            price: 100)))))))
                            )
                        )
                    )
                )))
    }
}