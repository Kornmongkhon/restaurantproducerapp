package th.co.priorsolution.traning.restaurantapp.model;

import lombok.Data;

@Data
public class ResponseModel<T> {
    private int statusCode;

    private String description;

    private T data;
}
