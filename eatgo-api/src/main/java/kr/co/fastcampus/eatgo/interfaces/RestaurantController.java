package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.RestaurantService;
import kr.co.fastcampus.eatgo.domain.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

    @Autowired //Spring이 알아서 Repository를 생성해서 넣어주게 됨
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<Restaurant> list() {

        List<Restaurant> restaurants = restaurantService.getRestaurants();

        return restaurants;
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant detail(@PathVariable("id") Long id) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        // 레스토랑의 기본 정보와 메뉴 정보를 한번에 돌려주는 새로운 기능

        return restaurant;
    }
}
