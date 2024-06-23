package iu.frontenders.restaurantappbackend.exception;

public class NoSuchMealException extends BaseException {

    @Override
    public String getMessage() {
        return "No such meal exists";
    }
}
