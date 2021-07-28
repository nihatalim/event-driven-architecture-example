package tr.com.nihatalim.eda.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import tr.com.nihatalim.eda.core.card.Card;
import tr.com.nihatalim.eda.core.config.EventType;
import tr.com.nihatalim.eda.core.event.PurchaseStartEvent;
import tr.com.nihatalim.eda.core.order.Order;
import tr.com.nihatalim.eda.core.order.OrderItem;

import java.util.Collections;

@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) throws JsonProcessingException {
		OrderItem huawei_p40 = new OrderItem("Huawei P40", 1, 4000);
		Order order = new Order(Collections.singletonList(huawei_p40));
		Card card = new Card("cardNo", "owner", "exp date", "cvv");

		PurchaseStartEvent purchaseStartEvent = PurchaseStartEvent.builder()
			.card(card)
			.order(order)
			.type(EventType.PURCHASE)
			.build();

		String s = new ObjectMapper().writeValueAsString(purchaseStartEvent);
		System.out.println(s);


//		String s = "{\"event\":{\"order\":{\"items\":[{\"name\":\"Huawei P40\",\"quantity\":1,\"price\":4000.0}]},\"card\":{\"cardNo\":\"cardNo\",\"owner\":\"owner\",\"expirationDate\":\"exp date\",\"cvv\":\"cvv\"}},\"type\":\"PURCHASE\"}\n";
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		EventMessage eventMessage = objectMapper.readValue(s, EventMessage.class);
//
//		System.out.println(eventMessage);


//		SpringApplication.run(CoreApplication.class, args);
	}



}
