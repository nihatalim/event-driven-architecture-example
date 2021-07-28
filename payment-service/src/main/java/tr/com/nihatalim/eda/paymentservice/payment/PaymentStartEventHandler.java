package tr.com.nihatalim.eda.paymentservice.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.event.DetectMaliciousPaymentEvent;
import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.PaymentStartEvent;

@Service
@RequiredArgsConstructor
public class PaymentStartEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.PAYMENT_START)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PaymentStartEvent event = objectMapper.readValue(message.value(), PaymentStartEvent.class);

        DetectMaliciousPaymentEvent maliciousPaymentEvent = DetectMaliciousPaymentEvent.builder()
            .card(event.getCard())
            .order(event.getOrder())
            .type(EventType.MALICIOUS)
            .build();

        eventPublisher.send(Topic.DETECT_MALICIOUS_PAYMENT, maliciousPaymentEvent);
    }
}
