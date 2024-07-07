package iu.frontenders.restaurantappbackend.service;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import iu.frontenders.restaurantappbackend.entity.MealEntity;
import iu.frontenders.restaurantappbackend.exception.MealAlreadyExistException;
import iu.frontenders.restaurantappbackend.exception.NoSuchMealException;
import iu.frontenders.restaurantappbackend.repository.MealRepository;
import iu.frontenders.restaurantappbackend.request.MealRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MinioService minioService;

    public void createMeal(MealRequestResponse mealRequestResponse) throws ServerException, InsufficientDataException, ErrorResponseException, MealAlreadyExistException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        if (mealRepository.getByTitle(mealRequestResponse.getTitle()).isPresent()) {
            throw new MealAlreadyExistException();
        }

        MealEntity mealEntity = MealEntity.builder()
                .title(mealRequestResponse.getTitle())
                .description(mealRequestResponse.getDescription())
                .calories(mealRequestResponse.getCalories())
                .price(mealRequestResponse.getPrice())
                .imageName(minioService.addImage(
                        mealRequestResponse.getImageName(),
                        mealRequestResponse.getImage()
                ))
                .build();

        mealRepository.save(mealEntity);
    }

    public MealRequestResponse getMeal(String title) throws NoSuchMealException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        Optional<MealEntity> mealEntityOptional = mealRepository.getByTitle(title);
        if (mealEntityOptional.isEmpty()) {
            throw new NoSuchMealException();
        }
        MealEntity mealEntity = mealEntityOptional.get();

        return MealRequestResponse.builder()
                .title(mealEntity.getTitle())
                .description(mealEntity.getDescription())
                .calories(mealEntity.getCalories())
                .price(mealEntity.getPrice())
                .imageName(mealEntity.getImageName())
                .image(minioService.getImage(mealEntity.getImageName()))
                .build();
    }

    public MealRequestResponse deleteMeal(String title) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        MealRequestResponse mealRequestResponse = getMeal(title);
        mealRepository.delete(MealEntity.builder()
                .title(mealRequestResponse.getTitle())
                .description(mealRequestResponse.getDescription())
                .calories(mealRequestResponse.getCalories())
                .price(mealRequestResponse.getPrice())
                .imageName(mealRequestResponse.getImageName())
                .build()
        );
        minioService.deleteImage(mealRequestResponse.getImageName());

        return mealRequestResponse;
    }

    public void updateMeal(String title, MealRequestResponse mealRequestResponse) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MealAlreadyExistException {

        deleteMeal(title);
        createMeal(mealRequestResponse);
    }

    public List<MealRequestResponse> getAllMeals() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        List<MealEntity> mealEntities = mealRepository.findAll();
        List<MealRequestResponse> mealRequestResponses = new ArrayList<>();

        for (MealEntity mealEntity : mealEntities) {
            mealRequestResponses.add(MealRequestResponse.builder()
                    .title(mealEntity.getTitle())
                    .description(mealEntity.getDescription())
                    .calories(mealEntity.getCalories())
                    .price(mealEntity.getPrice())
                    .imageName(mealEntity.getImageName())
                    .image(minioService.getImage(mealEntity.getImageName()))
                    .build()
            );
        }

        return mealRequestResponses;
    }
}
