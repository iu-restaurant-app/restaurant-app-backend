package iu.frontenders.restaurantappbackend.controller;

import iu.frontenders.restaurantappbackend.entity.MealEntity;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.request.MealCreateRequest;
import iu.frontenders.restaurantappbackend.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/meal")
@RestController
public class MealController {

    private final MealService mealService;

    @GetMapping("/{title}")
    public ResponseEntity<MealEntity> getMeal(@PathVariable String title) throws NoSuchMealException {
        return ResponseEntity.ok(mealService.getMeal(title));
    }

    @GetMapping(value = "/image/{title}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> getMealImage(@PathVariable String title) throws NoSuchMealException {
        return ResponseEntity.ok(new ByteArrayResource(mealService.getMeal(title).getImage()));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createMeal(@RequestParam String title,
                                                 @RequestParam(defaultValue = "") String description,
                                                 @RequestParam(defaultValue = "0") Integer calories,
                                                 @RequestParam(defaultValue = "0") Integer price,
                                                 @RequestBody MultipartFile multipartFile)
            throws IOException, MealAlreadyExistException {
        mealService.saveMeal(multipartFile, MealCreateRequest.builder()
                .title(title)
                .description(description)
                .calories(calories)
                .price(price)
                .build()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteMeal(@PathVariable String title) throws NoSuchMealException {
        mealService.deleteMeal(title);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{title}")
    public ResponseEntity<Void> updateMeal(@PathVariable String title,
                                                 @RequestParam String newTitle,
                                                 @RequestParam(defaultValue = "") String description,
                                                 @RequestParam(defaultValue = "0") Integer calories,
                                                 @RequestParam(defaultValue = "0") Integer price,
                                                 @RequestBody MultipartFile multipartFile)
            throws NoSuchMealException, IOException, MealAlreadyExistException {
        mealService.updateMeal(title, multipartFile, MealCreateRequest.builder()
                .title(newTitle)
                .description(description)
                .calories(calories)
                .price(price)
                .build()
        );
        return ResponseEntity.ok().build();
    }
}
