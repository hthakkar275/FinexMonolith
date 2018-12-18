package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class TradeImpl implements Trade {

	private static AtomicLong idGenerator = new AtomicLong(1);
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS");

	private long id;
	private LocalDateTime tradeTime;
	private Tradable buyer;
	private Tradable seller;
	private int quantity;
	private BigDecimal price;
	
	public TradeImpl() {
		this.setId(idGenerator.getAndIncrement());
		this.setTradeTime(LocalDateTime.now());
	}
	
	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public LocalDateTime getTradeTime() {
		return this.tradeTime;
	}

	@Override
	public void setTradeTime(LocalDateTime tradeTime) {
		this.tradeTime = tradeTime;
	}

	@Override
	public Tradable getBuyer() {
		return this.buyer;
	}

	@Override
	public void setBuyer(Tradable buyer) {
		this.buyer = buyer;
	}

	@Override
	public Tradable getSeller() {
		return this.seller;
	}

	@Override
	public void setSeller(Tradable seller) {
		this.seller = seller;
	}

	@Override
	public BigDecimal getPrice() {
		return this.price;
	}

	@Override
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("-------- Trade ----------\n");
		message.append("ID: ").append(this.getId()).append("\n");
		message.append("Time: " + formatter.format(LocalDateTime.now()) + "\n");
		message.append("Qty: ").append(this.getQuantity()).append("\n");
		message.append("Price: ").append(this.getPrice()).append("\n");
		message.append("Buyer: ").append(this.getBuyer());
		message.append("Seller: ").append(this.getSeller());
		return message.toString();

	}
}
