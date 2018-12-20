package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;

public class OrderImpl extends TradableImpl implements Order {

	private Side side;
	private int quantity;
	private BigDecimal price;
	private OrderStatus status;
	private OrderType type;
	private OrderLongevity longevity;
	
	@Override
	public Side getSide() {
		return this.side;
	}

	@Override
	public void setSide(Side side) {
		this.side = side;
	}

	@Override
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public OrderStatus getStatus() {
		return this.status;
	}

	@Override
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public OrderType getType() {
		return this.type;
	}

	@Override
	public void setType(OrderType type) {
		this.type = type;
	}

	@Override
	public OrderLongevity getLongevity() {
		return this.longevity;
	}

	@Override
	public void setLongevity(OrderLongevity longevity) {
		this.longevity = longevity;
	}
}
