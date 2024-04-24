package th.co.priorsolution.traning.restaurantapp.component;

import org.springframework.stereotype.Component;
import th.co.priorsolution.traning.restaurantapp.model.RestaurantOrderCriteriaResponseModel;
import th.co.priorsolution.traning.restaurantapp.model.RestaurantOrderModel;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantOrderUtilsComponent {
    public List<RestaurantOrderCriteriaResponseModel> transformQueryResultToResponseModel(List<RestaurantOrderModel> queryResult){
        List<RestaurantOrderCriteriaResponseModel> restaurantOrderCriteriaResponseModels = new ArrayList<>();
        for (RestaurantOrderModel x : queryResult) {
            RestaurantOrderCriteriaResponseModel y = new RestaurantOrderCriteriaResponseModel();
            y.setOrdId(x.getOrdId());
            y.setOrdDate(x.getOrdDate());
            y.setOrdStatus(x.getOrdStatus());
            restaurantOrderCriteriaResponseModels.add(y);
        }
        return  restaurantOrderCriteriaResponseModels;
    }
}
