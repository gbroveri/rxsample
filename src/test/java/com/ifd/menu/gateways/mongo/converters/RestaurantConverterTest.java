package com.ifd.menu.gateways.mongo.converters;

import com.ifd.menu.domains.*;
import com.ifd.menu.gateways.mongo.documents.*;
import io.reactivex.Maybe;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class RestaurantConverterTest {

    @Test
    public void testOptionConverter() {
        final OptionMongo optionMongo = buildOptionMongo();
        Option option = RestaurantConverter.optionConverter().convert(optionMongo);
        Assert.assertSame(100l, option.getPrice());
        Assert.assertEquals("bacon", option.getId());
        Assert.assertEquals("Bacon", option.getName());
    }

    @Test
    public void testOptionGroupConverter() {
        final OptionGroupMongo optionGroupMongo = buildOptionGroupMongo();
        OptionGroup optionGroup = RestaurantConverter.optionGroupConverter().convert(optionGroupMongo);
        Assert.assertSame(1, optionGroup.getPick());
        Assert.assertEquals("chips_optionals", optionGroup.getId());
        Assert.assertEquals("Chips Optional", optionGroup.getName());
        Assert.assertTrue(optionGroup.getOptions().size() == 1);
    }

    @Test
    public void testItemGroupConverter() {
        final ItemGroupMongo itemGroupMongo = buildItemGroupMongo();
        ItemGroup itemGroup = RestaurantConverter.itemGroupConverter().convert(itemGroupMongo);
        Assert.assertSame(2, itemGroup.getPick());
        Assert.assertEquals("side_dish_group", itemGroup.getId());
        Assert.assertEquals("Side dish", itemGroup.getName());
        Assert.assertTrue(itemGroup.getItems().size() == 1);
        Assert.assertTrue(itemGroup.getItems().iterator().next().getPickOptionals().getPick() == 1);
    }

    @Test
    public void testRegularItemConverter() {
        RegularItem regularItem = RestaurantConverter.regularItemConverter().convert(buildRegularItemMongo());
        Assert.assertSame(0l, regularItem.getPrice());
        Assert.assertEquals("chips", regularItem.getId());
        Assert.assertEquals("Chips", regularItem.getName());
        Assert.assertTrue(regularItem.getPickOptionals().getOptions().size() == 1);
    }

    @Test
    public void testComboConverter() {
        final ComboMongo comboMongo = buildComboMongo();
        Combo combo = RestaurantConverter.comboConverter().convert(comboMongo);
        Assert.assertEquals(Long.valueOf(1700l), combo.getPrice());
        Assert.assertEquals("combo5", combo.getId());
        Assert.assertEquals("Combo 5", combo.getName());
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
        Assert.assertEquals("fast_food_menu", menu.getId());
        Assert.assertEquals("Fast Food Menu", menu.getName());
        Assert.assertTrue(menu.getItems().size() == 1);
    }

    @Test
    public void testChainConverter() {
        ChainMongo chainMongo = new ChainMongo();
        chainMongo.setId("chips_n_burgers");
        chainMongo.setName("chips n burgers");
        Chain chain = RestaurantConverter.chainConverter().convert(chainMongo);
        Assert.assertEquals("chips_n_burgers", chain.getId());
        Assert.assertEquals("chips n burgers", chain.getName());
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
        Assert.assertEquals("burger_n_chips", restaurant.getId());
        Assert.assertEquals("burger n chips", restaurant.getName());
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
