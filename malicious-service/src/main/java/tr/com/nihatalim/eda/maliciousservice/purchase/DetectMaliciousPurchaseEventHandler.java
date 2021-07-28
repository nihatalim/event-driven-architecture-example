package tr.com.nihatalim.eda.maliciousservice.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.event.*;

@Service
@RequiredArgsConstructor
public class DetectMaliciousPurchaseEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.DETECT_MALICIOUS_PURCHASE)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        DetectMaliciousPurchaseEvent event =
            objectMapper.readValue(message.value(), DetectMaliciousPurchaseEvent.class);

        if (event.getOrder().getItems().size() < 1) {
            fail();
        } else {
            success(event);
        }
    }

    private void fail() {
        NotifyEvent notifyEvent = NotifyEvent.builder()
            .message("This purchase is malicious purchase because there is no item in order.")
            .type(EventType.NOTIFICATION)
            .build();

        eventPublisher.send(Topic.NOTIFY, notifyEvent);
    }

    private void success(DetectMaliciousPurchaseEvent event) {
        PaymentStartEvent paymentStartEvent = PaymentStartEvent.builder()
            .card(event.getCard())
            .order(event.getOrder())
            .type(EventType.PAYMENT)
            .build();

        eventPublisher.send(Topic.PAYMENT_START, paymentStartEvent);
    }
}
