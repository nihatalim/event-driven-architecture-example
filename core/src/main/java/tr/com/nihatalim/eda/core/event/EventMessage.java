package tr.com.nihatalim.eda.core.event;

import lombok.*;
import lombok.experimental.SuperBuilder;
import tr.com.nihatalim.eda.core.config.EventType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class EventMessage {
    protected EventType type;
}
