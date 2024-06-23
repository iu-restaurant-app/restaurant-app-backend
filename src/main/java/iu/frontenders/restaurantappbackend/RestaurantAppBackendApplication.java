package iu.frontenders.restaurantappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:hidden.properties")
@SpringBootApplication
public class RestaurantAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantAppBackendApplication.class, args);
	}
}
