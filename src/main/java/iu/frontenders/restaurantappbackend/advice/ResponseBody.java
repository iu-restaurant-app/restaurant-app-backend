package iu.frontenders.restaurantappbackend.advice;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ResponseBody {
    String exceptionName;
    String detail;
    String stackTrace;
}
