package iu.frontenders.restaurantappbackend.controller;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import iu.frontenders.restaurantappbackend.data.MealRequest;
import iu.frontenders.restaurantappbackend.data.MealResponse;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/meal")
@RestController
public class MealController {

    private final MealService mealService;

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> createMeal(@RequestBody MealResponse mealResponse) throws ServerException, InsufficientDataException, ErrorResponseException, MealAlreadyExistException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        mealService.createMeal(MealRequest.builder()
                .title(mealResponse.getTitle())
                .description(mealResponse.getDescription())
                .calories(mealResponse.getCalories())
                .price(mealResponse.getPrice())
                .imageName(mealResponse.getImageName())
                .image(Base64.getDecoder().decode(mealResponse.getImage()))
                .build()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{title}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<MealRequest> getMeal(@PathVariable String title) throws NoSuchMealException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.ok(mealService.getMeal(title));
    }

    @DeleteMapping("/{title}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<MealRequest> deleteMeal(@PathVariable String title) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.ok(mealService.deleteMeal(title));
    }

    @PatchMapping("/{title}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> updateMeal(@PathVariable String title, @RequestBody MealResponse mealResponse) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MealAlreadyExistException {

        mealService.updateMeal(title, MealRequest.builder()
                .title(mealResponse.getTitle())
                .description(mealResponse.getDescription())
                .calories(mealResponse.getCalories())
                .price(mealResponse.getPrice())
                .imageName(mealResponse.getImageName())
                .image(Base64.getDecoder().decode(mealResponse.getImage()))
                .build()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<MealRequest>> getAllMeals() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return ResponseEntity.ok(mealService.getAllMeals());
    }
}
