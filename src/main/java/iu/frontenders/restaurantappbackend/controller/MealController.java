package iu.frontenders.restaurantappbackend.controller;

import iu.frontenders.restaurantappbackend.entity.MealEntity;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.request.MealCreateRequest;
import iu.frontenders.restaurantappbackend.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/meal")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/{title}")
    public ResponseEntity<MealEntity> getMeal(@PathVariable String title) {
        try {
            return ResponseEntity.ok(mealService.getMeal(title));
        }
        catch (NoSuchMealException exception) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createMeal(@RequestParam String title, @RequestParam(defaultValue = "") String description, @RequestParam(defaultValue = "") Integer calories, @RequestParam(defaultValue = "") Integer price, @RequestBody MultipartFile multipartFile) {
        try {
            mealService.saveMeal(multipartFile, MealCreateRequest.builder()
                    .title(title)
                    .description(description)
                    .calories(calories)
                    .price(price)
                    .build()
            );
            return ResponseEntity.ok().build();
        }
        catch (MealAlreadyExistException exception) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
        catch (IOException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
