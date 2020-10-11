package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class RestaurantServiceTest {

    //@Autowired 사용 못함(스프링에서 지원하지 않기 때문)
    private RestaurantService restaurantService;
//    private RestaurantRepository restaurantRepository;
//    private MenuItemRepository menuItemRepository;

    @Mock //모키토에 있는 목 객체
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Before //모든 test가 돌기 전에 실행됨
    public void setUp() { //RestaurantRepository 주입
//        restaurantRepository = new RestaurantRepositoryImpl();
//        menuItemRepository = new MenuItemRepositoryImpl();

        MockitoAnnotations.initMocks(this); // Mock 어노테이션이 붙어있는 것들을 초기화해줌

        mockRestaurantRepository();
        mockMenuItemRepository();

        restaurantService = new RestaurantService(restaurantRepository, menuItemRepository);
    }

    private void mockMenuItemRepository() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(MenuItem.builder()
                .name("Kimchi")
                .build());

        given(menuItemRepository.findAllByRestaurantId(1004L)).willReturn(menuItems);
    }

    private void mockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
//        Restaurant restaurant = new Restaurant(1004L, "Bob zip", "Seoul");
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("Seoul")
                .menuItems(new ArrayList<MenuItem>())
                .build();
        restaurants.add(restaurant);

        given(restaurantRepository.findAll()).willReturn(restaurants);
        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant)); //Optional로 wrap
    }

    @Test
    public void getRestaurantWithExisted() {
        Restaurant restaurant = restaurantService.getRestaurant(1004L);

        MenuItem menuItem = restaurant.getMenuItems().get(0);

        assertThat(restaurant.getId(), is(1004L));
        assertThat(menuItem.getName(), is("Kimchi"));
    }

    @Test(expected = RestaurantNotFoundException.class)
    public void getRestaurantWithNotExisted() {
        restaurantService.getRestaurant(404L);
    }

    @Test
    public void getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId(), is(1004L));
    }

    @Test
    public void addRestaurant() {
        given(restaurantRepository.save(any())).will(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            restaurant.setId(1234L);
            return restaurant;
        });

        Restaurant restaurant = Restaurant.builder()
                .name("BeRyong")
                .address("Busan")
                .build();

        /*Restaurant saved = Restaurant.builder()
                .id(1234L)
                .name("BeRyong")
                .address("Busan")
                .build();
        given(restaurantRepository.save(any())).willReturn(saved);*/


        Restaurant created =  restaurantService.addRestaurant(
                Restaurant.builder()
                .name("BeRyong")
                .address("Busan")
                .build());

        assertThat(created.getId(), is(1234L));
    }

    @Test
    public void updateRestaurant() {

        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("seoul")
                .build();

        given(restaurantRepository.findById(1004L)).willReturn(Optional.of(restaurant));

        restaurantService.updateRestaurant(1004L, "Sool zip", "Busan");

        assertThat(restaurant.getName(), is("Sool zip"));
        assertThat(restaurant.getAddress(), is("Busan"));
    }
}