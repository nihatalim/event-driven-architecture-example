package tr.com.nihatalim.eda.approveservice.approve;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import tr.com.nihatalim.eda.core.event.ApproveStartEvent;
import tr.com.nihatalim.eda.core.event.BillingStartEvent;
import tr.com.nihatalim.eda.core.config.EventPublisher;
import tr.com.nihatalim.eda.core.config.Topic;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.PaymentRollbackEvent;

@Service
@RequiredArgsConstructor
public class ApproveStartEventHandler {
    private static final double APPROVAL_RATE = 0.77;

    private final EventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = Topic.APPROVE_START)
    public void handle(ConsumerRecord<String, String> message) throws JsonProcessingException {
        ApproveStartEvent approveStartEvent = objectMapper.readValue(message.value(), ApproveStartEvent.class);

        if (isApproval()) {
            success(approveStartEvent);
        } else {
            fail(approveStartEvent);
        }
    }

    private void fail(ApproveStartEvent event) {
        PaymentRollbackEvent rollbackEvent = PaymentRollbackEvent.builder()
            .card(event.getCard())
            .order(event.getOrder())
            .type(EventType.PAYMENT)
            .build();

        eventPublisher.send(Topic.PAYMENT_ROLLBACK, rollbackEvent);
    }

    private void success(ApproveStartEvent event) {
        BillingStartEvent billingStartEvent = BillingStartEvent.builder()
            .card(event.getCard())
            .order(event.getOrder())
            .type(EventType.PAYMENT)
            .build();

        eventPublisher.send(Topic.BILLING_START, billingStartEvent);
    }

    private boolean isApproval() {
        return Math.random() > APPROVAL_RATE;
    }
}
