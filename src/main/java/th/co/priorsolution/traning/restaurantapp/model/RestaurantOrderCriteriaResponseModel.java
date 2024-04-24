package th.co.priorsolution.traning.restaurantapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RestaurantOrderCriteriaResponseModel {
    private int ordId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ordDate;

    private String ordStatus;

}
