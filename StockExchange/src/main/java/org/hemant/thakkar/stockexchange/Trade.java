package org.hemant.thakkar.stockexchange;

import java.time.LocalDateTime;
import java.util.List;

public interface Trade {

	public long getId();
	public void setId(long id);
	public LocalDateTime getTradeTime();
	public void setTradeTime(LocalDateTime tradeTime);
	public List<Tradable> getTradables();
	public void addTradable(Tradable tradable);
	
}
