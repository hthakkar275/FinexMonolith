package org.hemant.thakkar.stockexchange;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderBookImplTest {

	@Test
	void testProcessOrder() {
		Order order = new OrderImpl();
		Broker broker = new Broker();
		broker.setName("Hemant");
		order.setParticipant(broker);
		order.setType(OrderType.MARKET);
		order.setLongevity(OrderLongevity.DAY);
		order.setQuantity(300);
		order.setPrice(new BigDecimal("10.45"));
		OrderBook orderBook = new OrderBookImpl();
		
	}

	@Test
	void testCancelOrder() {
		fail("Not yet implemented");
	}

	@Test
	void testModifyOrder() {
		fail("Not yet implemented");
	}

}
