package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;

public interface Quote extends Tradable {

	public int getBidQuantity();
	public void setBidQuantity(int quantity);
	public BigDecimal getBidPrice();
	public void setBidPrice(BigDecimal price);
	public int getOfferQuantity();
	public void setOfferQuantity(int quantity);
	public BigDecimal getOfferPrice();
	public void setOfferPrice(BigDecimal price);
}
