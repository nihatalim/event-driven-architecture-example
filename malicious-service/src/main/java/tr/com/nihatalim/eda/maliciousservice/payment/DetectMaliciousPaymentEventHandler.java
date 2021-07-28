package tr.com.nihatalim.eda.maliciousservice.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.event.ApproveStartEvent;
import tr.com.nihatalim.eda.core.event.DetectMaliciousPaymentEvent;
import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.NotifyEvent;

@Service
@RequiredArgsConstructor
public class DetectMaliciousPaymentEventHandler {
    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.DETECT_MALICIOUS_PAYMENT)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        DetectMaliciousPaymentEvent maliciousPaymentEvent =
            objectMapper.readValue(message.value(), DetectMaliciousPaymentEvent.class);

        boolean incorrectCvv = maliciousPaymentEvent.getCard().getCvv().equals("incorrect");

        if (incorrectCvv) {
            fail();
        } else {
            success(maliciousPaymentEvent);
        }
    }

    private void fail() {
        NotifyEvent notifyEvent = NotifyEvent.builder()
            .message("This payment is malicious because cvv in the card is abnormal.")
            .type(EventType.NOTIFICATION)
            .build();

        eventPublisher.send(Topic.NOTIFY, notifyEvent);
    }

    private void success(DetectMaliciousPaymentEvent event) {
        ApproveStartEvent approveStartEvent = ApproveStartEvent.builder()
            .order(event.getOrder())
            .card(event.getCard())
            .type(EventType.APPROVE)
            .build();

        eventPublisher.send(Topic.APPROVE_START, approveStartEvent);
    }
}
