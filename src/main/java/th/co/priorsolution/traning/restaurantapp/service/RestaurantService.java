package th.co.priorsolution.traning.restaurantapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.priorsolution.traning.restaurantapp.component.RestaurantOrderUtilsComponent;
import th.co.priorsolution.traning.restaurantapp.component.RestaurantTransferComponent;
import th.co.priorsolution.traning.restaurantapp.component.kafka.KafkaComponent;
import th.co.priorsolution.traning.restaurantapp.model.*;
import th.co.priorsolution.traning.restaurantapp.repository.RestaurantNativeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {

    @Value("${kafka.topics.restaurant.name}")
    private String restaurantTopic;

    @Value("${kafka.topics.served.name}")
    private String restaurantServedTopic;

    private KafkaComponent kafkaComponent;
    private RestaurantNativeRepository restaurantNativeRepository;

    private RestaurantOrderUtilsComponent restaurantOrderUtilsComponent;


    private RestaurantTransferComponent restaurantTransferComponent;

    public RestaurantService(KafkaComponent kafkaComponent, RestaurantNativeRepository restaurantNativeRepository, RestaurantOrderUtilsComponent restaurantOrderUtilsComponent, RestaurantTransferComponent restaurantTransferComponent) {
        this.kafkaComponent = kafkaComponent;
        this.restaurantNativeRepository = restaurantNativeRepository;
        this.restaurantOrderUtilsComponent = restaurantOrderUtilsComponent;
        this.restaurantTransferComponent = restaurantTransferComponent;
    }

    public ResponseModel<String> insertOrder(RestaurantOrderModel restaurantOrderModel){
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatusCode(200);
        result.setDescription("OK");
        List<Map<String, Object>> menuInfoList;
        boolean allItemsValid = true;
        try {
            List<RestaurantOrderListModel> orderList = restaurantOrderModel.getOrderList();
            //check orderList empty
            if(orderList == null || orderList.isEmpty()){
                throw new IllegalArgumentException("Order list is empty, please fill order list.");
            }
            List<Integer> foodIds = new ArrayList<>();
            for (RestaurantOrderListModel orderItem : orderList) {
                foodIds.add(orderItem.getFoodId());
            }
            menuInfoList = this.restaurantNativeRepository.getMenuInfo(foodIds);
            //check menu and quantity
            for (RestaurantOrderListModel orderItem : orderList) {
                Integer foodId = orderItem.getFoodId();
                Map<String, Object> menuInfo = menuInfoList.stream()
                        .filter(map -> map.get("menu_id").equals(foodId))
                        .findFirst()
                        .orElse(null);
//                Map<String, Object> menuInfo = menuMap.get(orderItem.getFoodId());
                //check menu
                if(menuInfo == null){
                    throw new IllegalArgumentException("Menu with ID : " + orderItem.getFoodId() + " does not exist in the menu, please order again!");
                }
                //check quantity
                int quantity = (int) menuInfo.get("menu_quantity");
                if (quantity < orderItem.getFoodQuantity()){
                    allItemsValid = false; // if found failed case
                    throw new IllegalArgumentException("There is not enough food in stock left for cooking with food ID : " + orderItem.getFoodId() + " , please order again! ");
                }
            }
            if (allItemsValid) {
                for (RestaurantOrderListModel orderItem : orderList) {
                    int foodId = orderItem.getFoodId();
                    int quantity = (int) menuInfoList.stream()
                            .filter(map -> map.get("menu_id").equals(foodId))
                            .findFirst()
                            .get()
                            .get("menu_quantity");
                    int updateQuantity = quantity - orderItem.getFoodQuantity();
                    this.restaurantNativeRepository.updateStock(updateQuantity, foodId);
                }

//                int orderId = this.restaurantNativeRepository.insertOrder(restaurantOrderModel);
//                OrderMessageModel orderMessageModel = new OrderMessageModel(restaurantOrderModel.getOrdStatus(),orderList);
                String message = this.restaurantTransferComponent.objectToJsonString(restaurantOrderModel);
                this.kafkaComponent.sendData(this.restaurantTopic,message);
                result.setData("Send insert order request success. ");

                // Insert order list
//                this.restaurantNativeRepository.insertOrderList(orderId, orderList);
            }
        } catch (Exception e) {
            result.setStatusCode(400);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<List<RestaurantOrderCriteriaResponseModel>> getStatusOrder(RestaurantOrderCriteriaRequestModel restaurantOrderCriteriaRequestModel){
        ResponseModel<List<RestaurantOrderCriteriaResponseModel>> result = new ResponseModel<>();
        result.setStatusCode(200);
        result.setDescription("OK");
        try{
            List<RestaurantOrderModel> queryResult = this.restaurantNativeRepository.receiveOrderStatus(restaurantOrderCriteriaRequestModel);
//            result.setOrdId(restaurantChefCriteriaModel.getCId());
            List<RestaurantOrderCriteriaResponseModel> dataList = this.restaurantOrderUtilsComponent.transformQueryResultToResponseModel(queryResult);
            result.setData(dataList);
        }catch (Exception e){
            result.setStatusCode(400);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<String> updateServedStatus(RestaurantOrderModel restaurantOrderModel){
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatusCode(200);
        result.setDescription("OK");
        try {
            String message = this.restaurantTransferComponent.objectToJsonString(restaurantOrderModel);
            this.kafkaComponent.sendData(this.restaurantServedTopic,message);
//            int updateRow = this.restaurantNativeRepository.servedOrderStatus(restaurantOrderModel);
            result.setData("Send update status request success.");
        }catch (Exception e){
            result.setStatusCode(400);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
