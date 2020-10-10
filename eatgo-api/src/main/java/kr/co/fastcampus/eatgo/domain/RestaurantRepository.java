package kr.co.fastcampus.eatgo.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    List<Restaurant> findAll();

    Optional<Restaurant> findById(Long id); //Optional은 null을 처리하지 않고 레스토랑이 있냐 없냐로 직접 구분,  널 접근 시 생기는 문제 해결 도움

    Restaurant save(Restaurant restaurant);
}
