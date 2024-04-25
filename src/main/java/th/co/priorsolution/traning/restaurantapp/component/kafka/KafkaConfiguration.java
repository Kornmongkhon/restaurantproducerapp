package th.co.priorsolution.traning.restaurantapp.component.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${kafka.server}")
    private String server;

    @Value("${kafka.topics.restaurant.name}")
    private String restaurantTopic;

    @Value("${kafka.topics.restaurant.partitions}")
    private int restaurantPartitions;

    @Value("${kafka.topics.served.name}")
    private String servedTopic;

    @Value("${kafka.topics.served.partitions}")
    private int servedPartitions;

    @Bean
    public KafkaAdmin settingTopicConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server);

        return new KafkaAdmin(configs);
    }
    @Bean
    public Map<String,Object> settingProducerConfigs(){
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String,String> generateProducerFactory(){
        return new DefaultKafkaProducerFactory<>(settingProducerConfigs());
    }

    @Bean(name = "restaurantKafkaTemplate")
    public KafkaTemplate<String,String> restaurantKafkaTemplate(){
        return new KafkaTemplate<>(generateProducerFactory());
    }

    @Bean
    public NewTopic restaurantTopic() {
        return TopicBuilder.name(restaurantTopic)
                .partitions(restaurantPartitions)
                .replicas((short)1)
                .build();
    }
    @Bean
    public NewTopic servedTopic() {
        return TopicBuilder.name(servedTopic)
                .partitions(servedPartitions)
                .replicas((short)1)
                .build();
    }

}
