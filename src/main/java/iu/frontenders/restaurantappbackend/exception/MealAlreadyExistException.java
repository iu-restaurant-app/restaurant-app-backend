package iu.frontenders.restaurantappbackend.exception;

public class MealAlreadyExistException extends BaseException {

    @Override
    public String getMessage() {
        return "Such meal already exists";
    }
}
