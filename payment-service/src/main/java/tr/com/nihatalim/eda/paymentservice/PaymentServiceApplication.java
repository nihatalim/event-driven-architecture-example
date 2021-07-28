package tr.com.nihatalim.eda.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import tr.com.nihatalim.eda.core.config.KafkaProducerConfig;

@Import(KafkaProducerConfig.class)
@EnableKafka
@SpringBootApplication
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
