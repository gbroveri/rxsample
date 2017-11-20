package com.ifd.menu.gateways.mongo.converters;

import com.ifd.menu.domains.*;
import com.ifd.menu.gateways.mongo.documents.*;
import io.reactivex.Maybe;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestaurantConverterTest {

    @Test
    public void testOptionConverter() {
        final OptionMongo optionMongo = buildOptionMongo();
        Option option = RestaurantConverter.optionConverter().convert(optionMongo);
        Assert.assertSame(option.getPrice(), 100l);
        Assert.assertEquals(option.getId(), "bacon");
        Assert.assertEquals(option.getName(), "Bacon");
    }

    @Test
    public void testOptionGroupConverter() {
        final OptionGroupMongo optionGroupMongo = buildOptionGroupMongo();
        OptionGroup optionGroup = RestaurantConverter.optionGroupConverter().convert(optionGroupMongo);
        Assert.assertSame(optionGroup.getPick(), 1);
        Assert.assertEquals(optionGroup.getId(), "chips_optionals");
        Assert.assertEquals(optionGroup.getName(), "Chips Optional");
        Assert.assertTrue(optionGroup.getOptions().size() == 1);
    }

    @Test
    public void testItemGroupConverter() {
        final ItemGroupMongo itemGroupMongo = buildItemGroupMongo();
        ItemGroup itemGroup = RestaurantConverter.itemGroupConverter().convert(itemGroupMongo);
        Assert.assertSame(itemGroup.getPick(), 2);
        Assert.assertEquals(itemGroup.getId(), "side_dish_group");
        Assert.assertEquals(itemGroup.getName(), "Side dish");
        Assert.assertTrue(itemGroup.getItems().size() == 1);
        Assert.assertTrue(itemGroup.getItems().iterator().next().getPickOptionals().getPick() == 1);
    }

    @Test
    public void testRegularItemConverter() {
        RegularItem regularItem = RestaurantConverter.regularItemConverter().convert(buildRegularItemMongo());
        Assert.assertSame(regularItem.getPrice(), 0l);
        Assert.assertEquals(regularItem.getId(), "chips");
        Assert.assertEquals(regularItem.getName(), "Chips");
        Assert.assertTrue(regularItem.getPickOptionals().getOptions().size() == 1);
    }

    @Test
    public void testComboConverter() {
        final ComboMongo comboMongo = buildComboMongo();
        Combo combo = RestaurantConverter.comboConverter().convert(comboMongo);
        Assert.assertEquals(combo.getPrice(), Long.valueOf(1700l));
        Assert.assertEquals(combo.getId(), "combo5");
        Assert.assertEquals(combo.getName(), "Combo 5");
        Assert.assertTrue(combo.getItems().size() == 1);
    }

    @Test
    public void testMenuItemConverterWithCombo() {
        final ComboMongo comboMongo = buildComboMongo();
        final MenuItem menuItem = RestaurantConverter.menuItemConverter().convert(comboMongo);
        Assert.assertTrue(menuItem instanceof Combo);
    }

    @Test
    public void testMenuItemConverterWithRegularItem() {
        RegularItemMongo regularItemMongo = buildRegularItemMongo();
        final MenuItem menuItem = RestaurantConverter.menuItemConverter().convert(regularItemMongo);
        Assert.assertTrue(menuItem instanceof RegularItem);
    }

    @Test
    public void testMenuConverter() {
        final MenuMongo menuMongo = buildMenuMongo();
        final Menu menu = RestaurantConverter.menuConverter().convert(menuMongo);
        Assert.assertEquals(menu.getId(), "fast_food_menu");
        Assert.assertEquals(menu.getName(), "Fast Food Menu");
        Assert.assertTrue(menu.getItems().size() == 1);
    }

    @Test
    public void testChainConverter() {
        ChainMongo chainMongo = new ChainMongo();
        chainMongo.setId("chips_n_burgers");
        chainMongo.setName("chips n burgers");
        Chain chain = RestaurantConverter.chainConverter().convert(chainMongo);
        Assert.assertEquals(chain.getId(), "chips_n_burgers");
        Assert.assertEquals(chain.getName(), "chips n burgers");
    }

    @Test
    public void testRestaurantConverter() {
        RestaurantMongo restaurantMongo = new RestaurantMongo();
        restaurantMongo.setTags(Arrays.asList("fast-food", "burger", "bacon", "american"));
        restaurantMongo.setId("burger_n_chips");
        restaurantMongo.setName("burger n chips");
        restaurantMongo.setChain(null);
        restaurantMongo.setMenus(Arrays.asList(buildMenuMongo()));
        Restaurant restaurant =
            RestaurantConverter.newConverter().convert(Maybe.just(restaurantMongo)).test().values().get(0);
        Assert.assertEquals(restaurant.getId(), "burger_n_chips");
        Assert.assertEquals(restaurant.getName(), "burger n chips");
        Assert.assertTrue(restaurant.getMenus().size() == 1);
    }


    private MenuMongo buildMenuMongo() {
        final MenuMongo menuMongo = new MenuMongo();
        menuMongo.setId("fast_food_menu");
        menuMongo.setName("Fast Food Menu");
        menuMongo.setItems(Arrays.asList(buildComboMongo()));
        return menuMongo;
    }

    private ComboMongo buildComboMongo() {
        final ComboMongo comboMongo = new ComboMongo();
        comboMongo.setId("combo5");
        comboMongo.setName("Combo 5");
        comboMongo.setPrice(1700l);
        comboMongo.setItems(Arrays.asList(buildRegularItemMongo()));
        return comboMongo;
    }

    private ItemGroupMongo buildItemGroupMongo() {
        final ItemGroupMongo itemGroupMongo = new ItemGroupMongo();
        itemGroupMongo.setId("side_dish_group");
        itemGroupMongo.setName("Side dish");
        itemGroupMongo.setPick(2);
        itemGroupMongo.setItems(Arrays.asList(buildRegularItemMongo()));
        return itemGroupMongo;
    }

    private RegularItemMongo buildRegularItemMongo() {
        final RegularItemMongo regularItemMongo = new RegularItemMongo();
        regularItemMongo.setId("chips");
        regularItemMongo.setName("Chips");
        regularItemMongo.setPrice(0l);
        regularItemMongo.setPickOptionals(buildOptionGroupMongo());
        return regularItemMongo;
    }

    private OptionGroupMongo buildOptionGroupMongo() {
        final OptionGroupMongo optionGroupMongo = new OptionGroupMongo();
        optionGroupMongo.setId("chips_optionals");
        optionGroupMongo.setName("Chips Optional");
        optionGroupMongo.setPick(1);
        optionGroupMongo.setOptions(Arrays.asList(buildOptionMongo()));
        return optionGroupMongo;
    }

    private OptionMongo buildOptionMongo() {
        final OptionMongo optionMongo = new OptionMongo();
        optionMongo.setId("bacon");
        optionMongo.setName("Bacon");
        optionMongo.setPrice(100l);
        return optionMongo;
    }
}
