package org.hemant.thakkar.stockexchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class OrderBookImplTest {

	@Test
	void testMarketDayOrderUnmatched() {
		Broker broker = new Broker();
		broker.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");

		Order buyOrder = new OrderImpl();
		buyOrder.setParticipant(broker);
		buyOrder.setType(OrderType.MARKET);
		buyOrder.setLongevity(OrderLongevity.DAY);
		buyOrder.setSide(Side.BUY);
		buyOrder.setQuantity(300);
		buyOrder.setProduct(equity);
		
		OrderBook orderBook = new OrderBookImpl(equity);
		OrderReport orderReport = orderBook.processOrder(buyOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(0, orderReport.getTrades().size());
		
		// Confirm that the order is not in the order book
		assertEquals(0, orderBook.volumeOnSide(Side.BUY));
		assertEquals(0, orderBook.volumeOnSide(Side.SELL));
	}

	@Test
	void testMarketDayOrderFullyMatchedToSingleOrder() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		Order buyMarketOrder= createMarketDayOrder(equity, "Hemant", Side.BUY);
		buyMarketOrder.setPrice(new BigDecimal("13.00"));
		buyMarketOrder.setQuantity(130);
		
		OrderReport orderReport = orderBook.processOrder(buyMarketOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(1, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(0).getQuantity());
		
		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(13).compareTo(orderBook.getBestOffer()) == 0);
		assertEquals(130, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 + 140 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(130, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));

	}

	@Test
	void testMarketDayOrderFullyMatchedToMultipleOrders() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		Order buyMarketOrder= createMarketDayOrder(equity, "Hemant", Side.BUY);
		buyMarketOrder.setPrice(new BigDecimal("13.00"));
		buyMarketOrder.setQuantity(160);
		
		OrderReport orderReport = orderBook.processOrder(buyMarketOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(2, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(1).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(0).getQuantity());
		assertEquals(30, orderReport.getTrades().get(1).getQuantity());

		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(13).compareTo(orderBook.getBestOffer()) == 0);
		assertEquals(100, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(100 + 140 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(100, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));

	}

	@Test
	void testMarketDayOrderFullyMatchedWithPartialOrderOnBook() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		Order buyMarketOrder= createMarketDayOrder(equity, "Hemant", Side.BUY);
		buyMarketOrder.setPrice(new BigDecimal("13.00"));
		buyMarketOrder.setQuantity(90);
		
		OrderReport orderReport = orderBook.processOrder(buyMarketOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(1, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(90, orderReport.getTrades().get(0).getQuantity());
		
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 +  40 + 140 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(170, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));

	}
	
	@Test
	void testMarketDayOrderMatchedAtMultiplePrices() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		Order buyMarketOrder= createMarketDayOrder(equity, "Hemant", Side.BUY);
		buyMarketOrder.setPrice(new BigDecimal("13.00"));
		buyMarketOrder.setQuantity(300);
		
		OrderReport orderReport = orderBook.processOrder(buyMarketOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(3, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(0).getQuantity());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(1).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(1).getQuantity());
		assertTrue(new BigDecimal(14).compareTo(orderReport.getTrades().get(2).getPrice()) == 0);
		assertEquals(40, orderReport.getTrades().get(2).getQuantity());
		
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(100 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(100, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));

	}

	@Test
	void testLimitOrderUnmatched() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		
		// limit buy order for 9.00 will not match because the best
		// offer/sell side is at 13.00. So this order should get booked
		Order buyLimitOrder= createLimitDayOrder(equity, "Hemant", Side.BUY);
		buyLimitOrder.setPrice(new BigDecimal("9.00"));
		buyLimitOrder.setQuantity(90);
		
		OrderReport orderReport = orderBook.processOrder(buyLimitOrder, true);
		assertNotNull(orderReport);
		assertTrue(orderReport.isOrderInBook());
		assertEquals(0, orderReport.getTrades().size());
		
		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(9).compareTo(orderBook.getWorstBid()) == 0);
		assertEquals(90, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(9)));
		
		// Confirm the order book status
		assertEquals(90 + 100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 + 140 + 150 + 130 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(90, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(9)));
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(260, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));
	}

	@Test
	void testLimitOrderOrderFullyMatchedToOrderPrice() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		
		// limit buy order for 9.00 will not match because the best
		// offer/sell side is at 13.00. So this order should get booked
		Order buyLimitOrder= createLimitDayOrder(equity, "Hemant", Side.BUY);
		buyLimitOrder.setPrice(new BigDecimal("13.00"));
		buyLimitOrder.setQuantity(130);
		
		OrderReport orderReport = orderBook.processOrder(buyLimitOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(1, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(0).getQuantity());
		
		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(13).compareTo(orderBook.getBestOffer()) == 0);
		assertEquals(130, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 + 140 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(130, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));
	}

	@Test
	void testLimitDayOrderPartiallyMatchedWithUnmatchedBooked() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		
		// limit buy order for 9.00 will not match because the best
		// offer/sell side is at 13.00. So this order should get booked
		Order buyLimitOrder= createLimitDayOrder(equity, "Hemant", Side.BUY);
		buyLimitOrder.setPrice(new BigDecimal("13.00"));
		buyLimitOrder.setQuantity(300);
		
		OrderReport orderReport = orderBook.processOrder(buyLimitOrder, true);
		assertNotNull(orderReport);
		assertTrue(orderReport.isOrderInBook());
		assertEquals(2, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(0).getQuantity());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(1).getPrice()) == 0);
		assertEquals(130, orderReport.getTrades().get(1).getQuantity());
		
		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(14).compareTo(orderBook.getBestOffer()) == 0);
		
		// Confirm the order book status
		assertEquals(100 + 110 + 100 + 120 + 40, orderBook.volumeOnSide(Side.BUY));
		assertEquals(140 + 150 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		assertEquals(40, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(13)));

		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));

	}

	@Test
	public void testLimitDayOrderFullyMatchedWithPartialOrderOnBook() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		
		// limit buy order for 9.00 will not match because the best
		// offer/sell side is at 13.00. So this order should get booked
		Order buyLimitOrder= createLimitDayOrder(equity, "Hemant", Side.BUY);
		buyLimitOrder.setPrice(new BigDecimal("13.00"));
		buyLimitOrder.setQuantity(90);
		
		OrderReport orderReport = orderBook.processOrder(buyLimitOrder, true);
		assertNotNull(orderReport);
		assertFalse(orderReport.isOrderInBook());
		assertEquals(1, orderReport.getTrades().size());
		assertTrue(new BigDecimal(13).compareTo(orderReport.getTrades().get(0).getPrice()) == 0);
		assertEquals(90, orderReport.getTrades().get(0).getQuantity());
		
		// Need to probe trade for expected opposite order's details
		// The best sell side should still have one more order at 13.00 with quantity of 130, 
		// because only one of two such ordres at 13.00 is fully traded
		assertTrue(new BigDecimal(13).compareTo(orderBook.getBestOffer()) == 0);
		
		// Confirm the order book status
		assertEquals(40 + 140 + 150 + 130 + 160, orderBook.volumeOnSide(Side.SELL));
		
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(170, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));
	}
	
	@Test
	void testCancelOrder() {
		Broker broker1 = new Broker();
		broker1.setName("Hemant");
		
		Product equity = new Equity();
		equity.setDescription("IBM Stock");
		equity.setSymbol("IBM");
		
		OrderBook orderBook = createOrderBook();
		Order buyLimitOrder= createLimitDayOrder(equity, "Hemant", Side.BUY);
		buyLimitOrder.setPrice(new BigDecimal("9.00"));
		buyLimitOrder.setQuantity(90);
		orderBook.processOrder(buyLimitOrder, false);
		
		// Confirm the order book status after new order is added
		assertEquals(90 + 100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 + 140 + 150 + 130 + 160, orderBook.volumeOnSide(Side.SELL));

		assertEquals(90, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(9)));
		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(260, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));
		
		orderBook.cancelOrder(buyLimitOrder.getId());
		
		// Confirm the order book status after new order is canceled
		assertEquals(100 + 110 + 100 + 120, orderBook.volumeOnSide(Side.BUY));
		assertEquals(130 + 140 + 150 + 130 + 160, orderBook.volumeOnSide(Side.SELL));

		assertEquals(200, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(10)));
		assertEquals(110, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(11)));
		assertEquals(120, orderBook.getVolumeAtPrice(Side.BUY, new BigDecimal(12)));
		
		assertEquals(260, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(13)));
		assertEquals(140, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(14)));
		assertEquals(150, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(15)));
		assertEquals(160, orderBook.getVolumeAtPrice(Side.SELL, new BigDecimal(16)));


	}

//	@Test
//	void testModifyOrder() {
//		fail("Not yet implemented");
//	}
//
	private OrderBook createOrderBook() {
		
		Equity equity = new Equity();
		equity.setSymbol("IBM");
		
		OrderBook orderBook = new OrderBookImpl(equity);
				
		// Add 3 buy orders of price two of 10.00, 11.00, and 12.00 in varying price order
		// Add an artificial 20 ms delay between each order for time priority test
		IntStream.of(11, 10, 12, 10).mapToObj(i -> {
			boolean slept = sleep(i);
			Order buyOrder = null;
			if (slept) {
				buyOrder = createLimitDayOrder(equity, "broker" + i, Side.BUY);
				buyOrder.setQuantity(i * 10);
				buyOrder.setPrice(new BigDecimal(i));
			}
			return buyOrder;
		}).forEach(o -> orderBook.processOrder(o, false));

		// Add 4 sell orders of price two of 13.00, 14.00, 15.00, and 16.00 in varying price order
		// Add an artificial 20 ms delay between each order for time priority test
		IntStream.of(16, 14, 13, 15, 13).mapToObj(i -> {
			boolean slept = sleep(i);
			Order sellOrder = null;
			if (slept) {
				sellOrder = createLimitDayOrder(equity, "broker" + i, Side.SELL);
				sellOrder.setQuantity(i * 10);
				sellOrder.setPrice(new BigDecimal(i));
			}
			return sellOrder;
		}).forEach(o -> orderBook.processOrder(o, false));

		return orderBook;
		
	}
	
	private Order createLimitDayOrder(Product product, String brokerName, Side side) {
		Order order = new OrderImpl();
		order.setProduct(product);
		Broker broker = new Broker();
		broker.setName(brokerName);
		order.setParticipant(broker);
		order.setType(OrderType.LIMIT);
		order.setLongevity(OrderLongevity.DAY);
		order.setSide(side);
		return order;
	}
	
	private Order createMarketDayOrder(Product product, String brokerName, Side side) {
		Order order = new OrderImpl();
		order.setProduct(product);
		Broker broker = new Broker();
		broker.setName(brokerName);
		order.setParticipant(broker);
		order.setType(OrderType.MARKET);
		order.setLongevity(OrderLongevity.DAY);
		order.setSide(side);
		return order;
	}

	private boolean sleep(int sleepTime) {
		boolean result = true;
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
