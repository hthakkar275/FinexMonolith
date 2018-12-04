package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;

public interface Order extends Tradable {

	public int getQuantity();
	public void setQuantity(int quantity);
	public BigDecimal getPrice();
	public void setPrice(BigDecimal price);
	public OrderStatus getStatus();
	public void setStatus(OrderStatus orderStatus);
	public OrderType getType();
	public void setType(OrderType type);
	public OrderLongevity getLongevity();
	public void setLongevity(OrderLongevity longevity);
	 
}
