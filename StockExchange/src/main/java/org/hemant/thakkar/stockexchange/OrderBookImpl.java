package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderBookImpl implements OrderBook {
	private static BigDecimal TWO = new BigDecimal("2.0");
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");
	
	private Product product;
	private List<Trade> tape = new ArrayList<Trade>();
	private List<Order> orders = new ArrayList<Order>();
	private BigDecimal tickSize;
	
	public OrderBookImpl(Product product) {
		this(new BigDecimal("0.01"), product);
	}

	public OrderBookImpl(BigDecimal tickSize, Product product) {
		this.tickSize = tickSize;
		this.setProduct(product);
		this.reset();
	}
	
	public void reset() {
		tape.clear();
		orders.clear();
	}
	
	
	/**
	 * Clips price according to tickSize
	 * 
	 * @param price
	 * @return
	 */
	private BigDecimal clipPrice(BigDecimal price) {
		int numDecPlaces = (int) Math.log10(1 / this.tickSize.doubleValue());
		BigDecimal rounded = price.setScale(numDecPlaces, RoundingMode.HALF_UP);
		return rounded;
	}
	
	
	public OrderReport processOrder(Order order, boolean verbose) {
		OrderReport oReport;
		
		if (order.getQuantity() <= 0 ) {
			throw new IllegalArgumentException("processOrder() given qty <= 0");
		}
		
		if (order.getType() == OrderType.LIMIT) {
			BigDecimal clippedPrice = clipPrice(order.getPrice());
			order.setPrice(clippedPrice);
			oReport = processLimitOrder(order, verbose);
		} else {
			oReport = processMarketOrder(order, verbose);
		}
		return oReport;
	}
	
	
	private OrderReport processMarketOrder(Order order, boolean verbose) {
		ArrayList<Trade> trades = new ArrayList<Trade>();
		Side side = order.getSide();
		int qtyRemaining = order.getQuantity();
		if (side == Side.BUY) {
			List<Order> offersByBestPrice = this.orders.stream()
					.filter(o -> o.getSide() == Side.SELL && o.getType() != OrderType.MARKET)
					.sorted(Comparator.comparing(Order::getPrice)
							.thenComparing(Order::getEntryTime))
					.collect(Collectors.toList());
			while ((qtyRemaining > 0) && (offersByBestPrice.size() > 0)) {
				// Find the best offer to match the buy order 
				// That would be to find the sell orders asking for smallest price.
				//List<Order> ordersAtMinPrice = offers.stream().sorted(Comparators.)
				qtyRemaining = processOrderList(trades, offersByBestPrice, qtyRemaining,
												order, verbose);
			}
		} else if (side == Side.SELL) {
			List<Order> bidsByBestPrice = this.orders.stream()
					.filter(o -> o.getSide() == Side.BUY && o.getType() != OrderType.MARKET)
					.sorted(Comparator.comparing(Order::getPrice)
							.reversed()
							.thenComparing(Order::getEntryTime))
					.collect(Collectors.toList());
			while ((qtyRemaining > 0) && (bidsByBestPrice.size() > 0)) {
				// Find the best offer to match the buy order 
				// That would be to find the sell orders asking for smallest price.
				//List<Order> ordersAtMinPrice = offers.stream().sorted(Comparators.)
				qtyRemaining = processOrderList(trades, bidsByBestPrice, qtyRemaining,
												order, verbose);
			}
		} else {
			throw new IllegalArgumentException("order neither market nor limit: " + 
				    						    side);
		}
		
		OrderReport report = new OrderReport(trades, false);

		return  report;
	}
	
	
	private OrderReport processLimitOrder(Order incomingOrder, boolean verbose) {
		boolean orderInBook = false;
		ArrayList<Trade> trades = new ArrayList<Trade>();
		Side incomingOrderSide = incomingOrder.getSide();
		final Side oppositeOrderSide;
		int qtyRemaining = incomingOrder.getQuantity();
		if (incomingOrderSide == Side.BUY) {		
			oppositeOrderSide = Side.SELL;
		} else if (incomingOrderSide == Side.SELL) {
			oppositeOrderSide = Side.BUY;
		} else {
			throw new IllegalArgumentException("order neither market nor limit: " + 
				    incomingOrderSide);
		}
		// Find all offers/sell-orders at or below the buy orders limit price
		// sorted by time entry. We want to give earliest order the highest
		// priority.
//		List<Order> tradableOrders = this.orders.stream()
//				.filter(o -> o.getSide() == oppositeOrderSide && o.getPrice().compareTo(order.getPrice()) <= 0)
//				.sorted(Comparator.comparing(Order::getEntryTime))
//				.collect(Collectors.toList());
//		List<Order> tradableOrders = this.orders.stream()
//				.filter(o -> o.getSide() == oppositeOrderSide && 
//						(o.getPrice().compareTo(order.getPrice()) == 0 || 
//							o.getPrice().compareTo(order.getPrice()) == priceComparisonSign))
//				.sorted(Comparator.comparing(Order::getEntryTime))
//				.collect(Collectors.toList());
	
		List<Order> tradableOrders = this.orders.stream()
				.filter(o -> {
					boolean match = false;
					if (incomingOrder.getSide() == Side.BUY) {
						match = o.getSide() == Side.SELL && incomingOrder.getPrice().compareTo(o.getPrice()) >= 0;
					} else {
						match = o.getSide() == Side.BUY && incomingOrder.getPrice().compareTo(o.getPrice()) <= 0;
					}
					return match;
				 })
				.sorted(Comparator.comparing(Order::getEntryTime))
				.collect(Collectors.toList());

		while ((tradableOrders.size() > 0) && 
				(qtyRemaining > 0)) {
			qtyRemaining = processOrderList(trades, tradableOrders, qtyRemaining,
											incomingOrder, verbose);
		}
		
		Iterator<Order> iterator = orders.iterator();
		while (iterator.hasNext()) {
			Order o = iterator.next();
			if (o.getQuantity() == 0) {
				iterator.remove();
			}
		}

		// If volume remains, add order to book
		if (qtyRemaining > 0) {
			incomingOrder.setQuantity(qtyRemaining);
			this.orders.add(incomingOrder);
			orderInBook = true;
		} else {
			orderInBook = false;
		}
		OrderReport report = new OrderReport(trades, orderInBook);
		if (orderInBook) {
			report.setOrder(incomingOrder);
		}
		return report;
	}
	
	
	private int processOrderList(ArrayList<Trade> trades, List<Order> tradableOrders,
								int qtyRemaining, Order incomingOrder,
								boolean verbose) {
		Iterator<Order> iterator = tradableOrders.iterator();
		
		while ((iterator.hasNext()) && (qtyRemaining > 0)) {
			int qtyTraded = 0;
			Order headOrder = iterator.next();
			if (qtyRemaining < headOrder.getQuantity()) {
				qtyTraded = qtyRemaining;
				headOrder.setQuantity(headOrder.getQuantity() - qtyRemaining);
				headOrder.setEntryTime(LocalDateTime.now());
				qtyRemaining = 0;
			} else {
				qtyTraded = headOrder.getQuantity();
				headOrder.setQuantity(0);
				qtyRemaining -= qtyTraded;
				iterator.remove();
			}
			Trade trade = new TradeImpl();
			if (incomingOrder.getSide() == Side.SELL) {
				trade.setSeller(incomingOrder);
				trade.setBuyer(headOrder);
			} else {
				trade.setSeller(headOrder);
				trade.setBuyer(incomingOrder);
			}
			trade.setPrice(headOrder.getPrice());
			trade.setQuantity(qtyTraded);
			trades.add(trade);
			this.tape.add(trade);
			if (verbose) {
				System.out.println(trade);
			}
		}
		return qtyRemaining;
	}
	
	public void cancelOrder(long orderId) {
		Iterator<Order> iterator = orders.iterator();
		while (iterator.hasNext()) {
			Order o = iterator.next();
			if (o.getId() == orderId) {
				iterator.remove();
			}
		}
	}
	
	
	public void modifyOrder(int qId, HashMap<String, String> quote) {
		// TODO implement modify order
		// Remember if price is changed must check for clearing.
	}
	
	
	public int getVolumeAtPrice(Side side, BigDecimal price) {
	    final BigDecimal clippedPrice = clipPrice(price);
		int volume = orders.stream().filter(o -> o.getSide() == side && o.getPrice().equals(clippedPrice))
				.mapToInt(o -> o.getQuantity()).sum();
		return volume;
		
	}
	
	public BigDecimal getBestBid() {
		BigDecimal bestBid = BigDecimal.ZERO;
		Optional<Order> bestBidOrder = orders.stream()
				.filter(o -> o.getSide() == Side.BUY)
				.sorted(Comparator.comparing(Order::getPrice).reversed()).findFirst();
		if (bestBidOrder.isPresent()) {
			bestBid = bestBidOrder.get().getPrice();
		}
		return bestBid;
	}
	
	public BigDecimal getWorstBid() {
		BigDecimal worstBid = BigDecimal.ZERO;
		Optional<Order> worstBidOrder = orders.stream()
				.filter(o -> o.getSide() == Side.BUY)
				.sorted(Comparator.comparing(Order::getPrice)).findFirst();
		if (worstBidOrder.isPresent()) {
			worstBid = worstBidOrder.get().getPrice();
		}
		return worstBid;
	}
	
	public BigDecimal getBestOffer() {
		BigDecimal bestOffer = BigDecimal.ZERO;
		Optional<Order> bestOfferOrder = orders.stream()
				.filter(o -> o.getSide() == Side.SELL)
				.sorted(Comparator.comparing(Order::getPrice)).findFirst();
		if (bestOfferOrder.isPresent()) {
			bestOffer = bestOfferOrder.get().getPrice();
		}
		return bestOffer;
	}
	
	public BigDecimal getWorstOffer() {
		BigDecimal worstOffer = BigDecimal.ZERO;
		Optional<Order> worstOfferOrder = orders.stream()
				.filter(o -> o.getSide() == Side.SELL)
				.sorted(Comparator.comparing(Order::getPrice).reversed()).findFirst();
		if (worstOfferOrder.isPresent()) {
			worstOffer = worstOfferOrder.get().getPrice();
		}
		return worstOffer;
	}
	
	public int volumeOnSide(Side side) {
		if (side != Side.BUY && side != Side.SELL) {
			throw new IllegalArgumentException("order neither market nor limit: " + 
					side);
		}
		int volume = orders.stream()
						.filter(o -> o.getSide() == side)
						.mapToInt(o -> o.getQuantity())
						.sum();
		return volume;
	}
	
	public BigDecimal getTickSize() {
		return tickSize;
	}
	
	public BigDecimal getSpread() {
		BigDecimal minOfferPrice = BigDecimal.ZERO;
		Optional<BigDecimal> minOfferPriceOpt = orders.stream()
							  						.filter(o -> o.getSide() == Side.SELL)
							  						.min(Comparator.comparing(Order::getPrice))
							  						.map(o -> o.getPrice());
		if (minOfferPriceOpt.isPresent()) {
			minOfferPrice = minOfferPriceOpt.get();
		}
			
		BigDecimal maxBidPrice = BigDecimal.ZERO;
		Optional<BigDecimal> maxBidPriceOpt = orders.stream()
							  						.filter(o -> o.getSide() == Side.BUY)
							  						.min(Comparator.comparing(Order::getPrice))
							  						.map(o -> o.getPrice());
		if (maxBidPriceOpt.isPresent()) {
			maxBidPrice = maxBidPriceOpt.get();
		}

		return minOfferPrice.subtract(maxBidPrice);
	}
	
	public BigDecimal getMid() {
		return this.getBestBid().add(this.getSpread().divide(TWO));
	}
	
	public boolean bidsAndAsksExist() {
		long bidCount = orders.stream().filter(o -> o.getSide() == Side.BUY).count();
		long offerCount = orders.stream().filter(o -> o.getSide() == Side.SELL).count();
		return bidCount > 0 && offerCount > 0;
	}
	
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("Time: " + formatter.format(LocalDateTime.now()) + "\n");
		message.append("-------- The Order Book --------\n");
		message.append("  ").append(this.product).append("\n");
		message.append("   ------- Bid  Book --------   \n");
		String bids = this.orders.stream().filter(o -> o.getSide() == Side.BUY)
				.map(o -> o.toString())
				.reduce("", (a, b) -> a + "\n    " + b);
		message.append(bids + "\n");
		message.append("   ------- Offer  Book --------   \n");
		String offers = this.orders.stream().filter(o -> o.getSide() == Side.SELL)
				.map(o -> o.toString())
				.reduce("", (a, b) -> a + "\n    " + b);
		message.append(offers + "\n");
		message.append("   ----------- Trades ----------   \n");
		this.tape.stream().forEach(o -> message.append(tape.toString() + "\n"));
		message.append("---------------------------------\n");
		return message.toString();
	}


	public List<Trade> getTape() {
		return tape;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}