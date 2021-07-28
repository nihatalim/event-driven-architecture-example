package tr.com.nihatalim.eda.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum EventType {
    PURCHASE("PURCHASE"),
    PAYMENT("PAYMENT"),
    APPROVE("APPROVE"),
    MALICIOUS("MALICIOUS"),
    BILLING("BILLING"),
    NOTIFICATION("NOTIFICATION");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static EventType forValue(String value) {
        return Arrays.stream(EventType.values())
            .filter(item -> item.value.equals(value))
            .findFirst()
            .orElse(null);
    }
}
