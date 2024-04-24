package th.co.priorsolution.traning.restaurantapp.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantTransferComponent {
    public static final ObjectMapper mapper = new ObjectMapper();

    public String objectToJsonString(Object model) throws JsonProcessingException {
        return mapper.writeValueAsString(model);
    }
}
