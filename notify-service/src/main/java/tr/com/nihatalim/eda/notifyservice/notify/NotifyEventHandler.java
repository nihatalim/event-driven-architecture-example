package tr.com.nihatalim.eda.notifyservice.notify;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.config.Topic;

@Service
public class NotifyEventHandler {

    @KafkaListener(topics = Topic.NOTIFY)
    public void handle(ConsumerRecord<String, String> event) {
        System.out.println(event.value());
    }
}
