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
import iu.frontenders.restaurantappbackend.data.MealRequest;
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

    public void createMeal(MealRequest mealRequest) throws ServerException, InsufficientDataException, ErrorResponseException, MealAlreadyExistException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        if (mealRepository.getByTitle(mealRequest.getTitle()).isPresent()) {
            throw new MealAlreadyExistException();
        }

        MealEntity mealEntity = MealEntity.builder()
                .title(mealRequest.getTitle())
                .description(mealRequest.getDescription())
                .calories(mealRequest.getCalories())
                .price(mealRequest.getPrice())
                .imageName(minioService.addImage(
                        mealRequest.getImageName(),
                        mealRequest.getImage()
                ))
                .build();

        mealRepository.save(mealEntity);
    }

    public MealRequest getMeal(String title) throws NoSuchMealException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        Optional<MealEntity> mealEntityOptional = mealRepository.getByTitle(title);
        if (mealEntityOptional.isEmpty()) {
            throw new NoSuchMealException();
        }
        MealEntity mealEntity = mealEntityOptional.get();

        return MealRequest.builder()
                .title(mealEntity.getTitle())
                .description(mealEntity.getDescription())
                .calories(mealEntity.getCalories())
                .price(mealEntity.getPrice())
                .imageName(mealEntity.getImageName())
                .image(minioService.getImage(mealEntity.getImageName()))
                .build();
    }

    public MealRequest deleteMeal(String title) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        MealRequest mealRequest = getMeal(title);
        mealRepository.delete(MealEntity.builder()
                .title(mealRequest.getTitle())
                .description(mealRequest.getDescription())
                .calories(mealRequest.getCalories())
                .price(mealRequest.getPrice())
                .imageName(mealRequest.getImageName())
                .build()
        );
        minioService.deleteImage(mealRequest.getImageName());

        return mealRequest;
    }

    public void updateMeal(String title, MealRequest mealRequest) throws ServerException, NoSuchMealException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, MealAlreadyExistException {

        deleteMeal(title);
        createMeal(mealRequest);
    }

    public List<MealRequest> getAllMeals() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        List<MealEntity> mealEntities = mealRepository.findAll();
        List<MealRequest> mealRequests = new ArrayList<>();

        for (MealEntity mealEntity : mealEntities) {
            mealRequests.add(MealRequest.builder()
                    .title(mealEntity.getTitle())
                    .description(mealEntity.getDescription())
                    .calories(mealEntity.getCalories())
                    .price(mealEntity.getPrice())
                    .imageName(mealEntity.getImageName())
                    .image(minioService.getImage(mealEntity.getImageName()))
                    .build()
            );
        }

        return mealRequests;
    }
}
