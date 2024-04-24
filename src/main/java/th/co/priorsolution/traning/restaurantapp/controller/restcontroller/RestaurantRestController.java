package th.co.priorsolution.traning.restaurantapp.controller.restcontroller;

import org.springframework.web.bind.annotation.*;
import th.co.priorsolution.traning.restaurantapp.model.*;
import th.co.priorsolution.traning.restaurantapp.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantRestController {
    private RestaurantService restaurantService;

    public RestaurantRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping("/v1/restaurant/orderMenu")
    public ResponseModel<String> insertOrder(@RequestBody RestaurantOrderModel restaurantOrderModel){
        return this.restaurantService.insertOrder(restaurantOrderModel);
    }

    @GetMapping("/v1/restaurant/getOrderStatus")
    public ResponseModel<List<RestaurantOrderCriteriaResponseModel>> receiveOrder(@RequestBody RestaurantOrderCriteriaRequestModel restaurantOrderCriteriaRequestModel){
        return this.restaurantService.getStatusOrder(restaurantOrderCriteriaRequestModel);
    }

    @PatchMapping("v1/restaurant/servedOrder")
    public ResponseModel<String> servedOrder(@RequestBody RestaurantOrderModel restaurantOrderModel){
        return this.restaurantService.updateServedStatus(restaurantOrderModel);
    }

}
