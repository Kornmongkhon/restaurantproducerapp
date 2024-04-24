package th.co.priorsolution.traning.restaurantapp.repository;

import th.co.priorsolution.traning.restaurantapp.model.*;

import java.util.List;
import java.util.Map;

public interface RestaurantNativeRepository {

    public List<RestaurantOrderModel> receiveOrderStatus(RestaurantOrderCriteriaRequestModel restaurantOrderCriteriaRequestModel);

    public List<Map<String, Object>> getMenuInfo(List<Integer> foodIds);

    public void updateStock(int quantity,int menuId);
}