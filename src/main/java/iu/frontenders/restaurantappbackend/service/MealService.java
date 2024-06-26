package iu.frontenders.restaurantappbackend.service;

import iu.frontenders.restaurantappbackend.entity.MealEntity;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.repository.MealRepository;
import iu.frontenders.restaurantappbackend.request.MealCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;

    @Transactional
    public MealEntity getMeal(String title) throws NoSuchMealException {
        Optional<MealEntity> mealEntityOptional = mealRepository.getByTitle(title);

        if (mealEntityOptional.isEmpty()) {
            throw new NoSuchMealException();
        }

        return mealEntityOptional.get();
    }

    @Transactional
    public void saveMeal(MultipartFile multipartFile, MealCreateRequest mealCreateRequest) throws IOException, MealAlreadyExistException {
        if (mealRepository.getByTitle(mealCreateRequest.getTitle()).isPresent()) {
            throw new MealAlreadyExistException();
        }

        MealEntity mealEntity = MealEntity.builder()
                .title(mealCreateRequest.getTitle())
                .description(mealCreateRequest.getDescription())
                .calories(mealCreateRequest.getCalories())
                .price(mealCreateRequest.getPrice())
                .image(multipartFile.getBytes())
                .build();

        mealRepository.save(mealEntity);
    }

    @Transactional
    public void deleteMeal(String title) throws NoSuchMealException {
        Optional<MealEntity> mealEntityOptional = mealRepository.getByTitle(title);

        if (mealEntityOptional.isEmpty()) {
            throw new NoSuchMealException();
        }

        mealRepository.deleteByTitle(title);
    }

    @Transactional
    public void updateMeal(String title, MultipartFile multipartFile, MealCreateRequest mealCreateRequest) throws NoSuchMealException, IOException, MealAlreadyExistException {
        Optional<MealEntity> mealEntityOptional = mealRepository.getByTitle(title);

        if (mealEntityOptional.isEmpty()) {
            throw new NoSuchMealException();
        }

        mealRepository.delete(mealEntityOptional.get());
        saveMeal(multipartFile, mealCreateRequest);
    }
}