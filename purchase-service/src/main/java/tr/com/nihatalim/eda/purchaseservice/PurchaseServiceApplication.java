package tr.com.nihatalim.eda.purchaseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import tr.com.nihatalim.eda.core.config.KafkaProducerConfig;

@Import(KafkaProducerConfig.class)
@EnableKafka
@SpringBootApplication
public class PurchaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurchaseServiceApplication.class, args);
    }

}
