package iu.frontenders.restaurantappbackend.data;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class MealResponse {

    String title;
    String description;
    Integer calories;
    Integer price;

    String imageName;
    String image;
}
