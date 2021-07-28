package tr.com.nihatalim.eda.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import tr.com.nihatalim.eda.core.card.Card;
import tr.com.nihatalim.eda.core.order.Order;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DetectMaliciousPaymentEvent extends EventMessage {
    private Order order;
    private Card card;
}
