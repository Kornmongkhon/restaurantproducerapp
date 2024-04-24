package th.co.priorsolution.traning.restaurantapp.component.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaComponent {

    private KafkaTemplate<String,String> kafkaTemplate;

    public KafkaComponent(@Qualifier("restaurantKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendData(String topic, String message){
        this.kafkaTemplate.send(topic,message)
                .whenComplete((result, throwable) -> {
                    if (null == throwable){
                        log.info("kafka send to {} done {}"
                                ,result.getRecordMetadata().topic()
                                ,result.getProducerRecord().value());
                    }else{
                        log.info("kafka send exception {}",throwable.getMessage());
                    }
                });
    }
}
