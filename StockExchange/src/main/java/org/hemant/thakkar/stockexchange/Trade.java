package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Trade {

	public long getId();
	public void setId(long id);
	public LocalDateTime getTradeTime();
	public void setTradeTime(LocalDateTime tradeTime);
	public Tradable getBuyer();
	public void setBuyer(Tradable buyTradable);
	public Tradable getSeller();
	public void setSeller(Tradable sellTradable);
	public BigDecimal getPrice();
	public void setPrice(BigDecimal price);
	public int getQuantity();
	public void setQuantity(int quantity);
	
}
