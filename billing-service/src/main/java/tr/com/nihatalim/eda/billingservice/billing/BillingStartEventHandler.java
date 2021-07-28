package tr.com.nihatalim.eda.billingservice.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.event.BillingStartEvent;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.NotifyEvent;

@Service
@RequiredArgsConstructor
public class BillingStartEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.BILLING_START)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        BillingStartEvent billingStartEvent = objectMapper.readValue(message.value(), BillingStartEvent.class);

        final String formattedMessage =
            String.format("All operations has been completed. %s", objectMapper.writeValueAsString(billingStartEvent));

        NotifyEvent notifyEvent = NotifyEvent.builder()
            .message(formattedMessage)
            .type(EventType.NOTIFICATION)
            .build();

        eventPublisher.send(Topic.NOTIFY, notifyEvent);
    }
}
