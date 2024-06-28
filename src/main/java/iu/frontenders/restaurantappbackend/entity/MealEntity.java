package iu.frontenders.restaurantappbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meal")
public class MealEntity {

    @Id
    private String title;

    private String description;
    private Integer calories;
    private Integer price;
    private String imageName;
}
