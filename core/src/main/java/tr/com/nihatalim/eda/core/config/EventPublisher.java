package tr.com.nihatalim.eda.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.kafka.core.KafkaTemplate;
import tr.com.nihatalim.eda.core.event.EventMessage;

@RequiredArgsConstructor
public class EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void send(String topic, EventMessage message) {
        kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
    }
}
