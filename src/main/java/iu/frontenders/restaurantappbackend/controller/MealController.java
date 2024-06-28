package iu.frontenders.restaurantappbackend.controller;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import iu.frontenders.restaurantappbackend.entity.MealEntity;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.request.MealRequestResponse;
import iu.frontenders.restaurantappbackend.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/meal")
@RestController
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<Void> createMeal(@RequestBody MealRequestResponse mealRequestResponse) throws ServerException, InsufficientDataException, ErrorResponseException, MealAlreadyExistException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        mealService.createMeal(mealRequestResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{title}")
    public ResponseEntity<MealRequestResponse> getMeal(@PathVariable String title) throws NoSuchMealException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.ok(mealService.getMeal(title));
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<MealRequestResponse> deleteMeal(@PathVariable String title) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.ok(mealService.deleteMeal(title));
    }

    @PatchMapping("/{title}")
    public ResponseEntity<Void> updateMeal(@PathVariable String title, @RequestBody MealRequestResponse mealRequestResponse) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MealAlreadyExistException {

        mealService.updateMeal(title, mealRequestResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMeals() {

        List<MealEntity> allMeals = mealService.getAllMeals();
        Map<String, Object> result = new HashMap<>();
        result.put("total", allMeals.size());
        result.put("meals", allMeals);

        return ResponseEntity.ok(result);
    }
}
