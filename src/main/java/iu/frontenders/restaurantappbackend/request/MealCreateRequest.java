package iu.frontenders.restaurantappbackend.request;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class MealCreateRequest {
    String title;
    String description;
    Integer calories;
    Integer price;
}
