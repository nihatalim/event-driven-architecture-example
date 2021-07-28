package tr.com.nihatalim.eda.purchaseservice.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.event.DetectMaliciousPurchaseEvent;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.PurchaseStartEvent;

@Service
@RequiredArgsConstructor
public class PurchaseStartEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.PURCHASE_START)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PurchaseStartEvent purchaseStartEvent = objectMapper.readValue(message.value(), PurchaseStartEvent.class);

        DetectMaliciousPurchaseEvent detectMaliciousPurchaseEvent = DetectMaliciousPurchaseEvent.builder()
            .card(purchaseStartEvent.getCard())
            .order(purchaseStartEvent.getOrder())
            .type(EventType.MALICIOUS)
            .build();

        eventPublisher.send(Topic.DETECT_MALICIOUS_PURCHASE, detectMaliciousPurchaseEvent);
    }
}
