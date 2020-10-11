package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants;
    }

    public Restaurant getRestaurant(Long id) {
        //들어올 때만 실행해야돼서 ()-> 처리
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantNotFoundException(id));


        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantId(id);
        restaurant.setMenuItems(menuItems);
        return restaurant;
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional //변경들이 범위를 벗어날 때 다같이 적용, 객체 처리를 디비에 반영하기 위해서
    public Restaurant updateRestaurant(Long id, String name, String address) {

        Restaurant restaurant = restaurantRepository.findById(id).orElse(null); //Optional로 나와서 orElse(null) 처리

        restaurant.updateInformation(name, address);

        return restaurant;
    }
}
