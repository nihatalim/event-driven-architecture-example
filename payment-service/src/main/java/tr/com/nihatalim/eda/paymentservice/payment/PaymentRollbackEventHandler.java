package tr.com.nihatalim.eda.paymentservice.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.NotifyEvent;
import tr.com.nihatalim.eda.core.event.PaymentRollbackEvent;

@Service
@RequiredArgsConstructor
public class PaymentRollbackEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.PAYMENT_ROLLBACK)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PaymentRollbackEvent paymentRollbackEvent = objectMapper.readValue(message.value(), PaymentRollbackEvent.class);

        final String formattedMessage =
            String.format("Payment has been failed. %s", objectMapper.writeValueAsString(paymentRollbackEvent));

        NotifyEvent notifyEvent = NotifyEvent.builder()
            .message(formattedMessage)
            .type(EventType.NOTIFICATION)
            .build();

        eventPublisher.send(Topic.NOTIFY, notifyEvent);
    }
}
