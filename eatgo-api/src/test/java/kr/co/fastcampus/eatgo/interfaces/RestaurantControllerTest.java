package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.RestaurantService;
import kr.co.fastcampus.eatgo.domain.MenuItem;
import kr.co.fastcampus.eatgo.domain.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean //서비스가 가짜로 바뀜, 실제로 서비스가 어떻게 되있는지 별개로 가짜 처리를 통해서 작동
    private RestaurantService restaurantService;

//    @SpyBean(RestaurantService.class) //컨트롤러에 원하는 객체 주입 가능, 컨트롤러가 직접적으로 의존
//    private RestaurantService restaurantService;

//    @SpyBean(RestaurantRepositoryImpl.class) //컨트롤러에 원하는 객체 주입 가능
//    private RestaurantRepository restaurantRepository;
//
//    @SpyBean(MenuItemRepositoryImpl.class)
//    private MenuItemRepository menuItemRepository;

    @Test
    public void list() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant(1004L, "Bob zip", "Seoul"));

        given(restaurantService.getRestaurants()).willReturn(restaurants); //실행하면 리턴할 것이다.

        mvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004") //json 형태 확인
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Bob zip\"") //json 형태 확인
                ));

    }

    @Test
    public void detail() throws Exception {
        Restaurant restaurant1 = new Restaurant(1004L, "JOKER House", "Seoul");
        restaurant1.addMenuItem(new MenuItem("Kimchi"));

        Restaurant restaurant2 = new Restaurant(2020L, "Cyber food", "Seoul");

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant1); //정보들을 얻는 것에 대해서 Mock 가짜 객체 넣어주어 확인
        given(restaurantService.getRestaurant(2020L)).willReturn(restaurant2);


        mvc.perform(get("/restaurants/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004") //json 형태 확인
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"JOKER House\"") //json 형태 확인
                ))
                .andExpect(content().string(
                        containsString("Kimchi")
                ));

        mvc.perform(get("/restaurants/2020"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":2020") //json 형태 확인
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Cyber food\"") //json 형태 확인
                ));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Busan\", \"address\":\"Busan\"}"))
                .andExpect(status().isCreated()) //기존에는 isOk()
                .andExpect(header().string("location", "/restaurants/1234"))
                .andExpect(content().string("{}"));

        verify(restaurantService).addRestaurant(any()); //리턴이 중심이 아니라 실행이 중심, mockito에서는 무엇을 넣던지 작동하도록 any()
    }
}