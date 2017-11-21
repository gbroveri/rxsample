package com.ifd.menu.http;

import com.ifd.menu.domains.IfdContext;
import com.ifd.menu.domains.Restaurant;
import com.ifd.menu.http.error.RestControllerAdvice;
import com.ifd.menu.usecases.FindRestaurant;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestaurantControllerTests {
    private MockMvc mvc;
    private RestaurantController controller;

    @Mock
    private FindRestaurant findRestaurant;

    @Before
    public void setup() {
        controller = new RestaurantController(findRestaurant);
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestControllerAdvice()).build();
    }

    @Test
    public void testNotFound() throws Exception {
        //setup
        final Observable mock = Maybe.empty().toObservable();
        when(findRestaurant.execute(anyString(), any(IfdContext.class))).thenReturn(mock);

        //when
        MvcResult result = this.mvc.perform(get("/restaurants/teste"))
            .andReturn();

        //then
        mvc
            .perform(asyncDispatch(result))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testInternalServerError() throws Exception {
        //setup
        final Observable mock = Maybe.error(new RuntimeException()).toObservable();
        when(findRestaurant.execute(anyString(), any(IfdContext.class))).thenReturn(mock);

        //when
        MvcResult result = this.mvc.perform(get("/restaurants/teste"))
            .andReturn();

        //then
        mvc
            .perform(asyncDispatch(result))
            .andExpect(status().is5xxServerError());
    }

    @Test
    public void testSuccess() throws Exception {
        //setup
        final Restaurant restaurant = new Restaurant();
        restaurant.setId("test_restaurant");
        restaurant.setName("Test Restaurant");
        final Observable mock = Maybe.just(restaurant).toObservable();
        when(findRestaurant.execute(anyString(), any(IfdContext.class))).thenReturn(mock);

        //when
        MvcResult result = this.mvc.perform(get("/restaurants/test_restaurant"))
            .andReturn();

        //then
        mvc
            .perform(asyncDispatch(result))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("id").value("test_restaurant"));
    }

}



