package iu.frontenders.restaurantappbackend.repository;

import iu.frontenders.restaurantappbackend.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, String> {
    Optional<MealEntity> getByTitle(String title);
    void deleteByTitle(String title);
}
