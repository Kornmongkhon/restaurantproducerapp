package th.co.priorsolution.traning.restaurantapp.model;

import lombok.Data;

@Data
public class RestaurantOrderListModel {
    private int ordlistId;

    private int ordId;

    private int foodId;

    private String foodName;

    private String foodType;

    private int foodQuantity;

    private String foodStatus;

    private int foodBychef;
}
