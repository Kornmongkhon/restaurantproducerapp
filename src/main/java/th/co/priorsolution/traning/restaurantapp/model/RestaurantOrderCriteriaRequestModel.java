package th.co.priorsolution.traning.restaurantapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RestaurantOrderCriteriaRequestModel {
    private int ordId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ordDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ordDate2;

    private String ordStatus;

}
