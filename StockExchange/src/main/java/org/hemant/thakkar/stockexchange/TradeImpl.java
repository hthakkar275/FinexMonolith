package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TradeImpl implements Trade {

	private long id;
	private LocalDateTime tradeTime;
	private Tradable buyer;
	private Tradable seller;
	private int quantity;
	private BigDecimal price;
	
//	public TradeImpl(LocalDateTime time, BigDecimal price, int qtyTraded, long id2, long takerId, long buyer,
//			long seller, long id3) {
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalDateTime getTradeTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTradeTime(LocalDateTime tradeTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public Tradable getBuyer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBuyer(Tradable buyTradable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tradable getSeller() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSeller(Tradable sellTradable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BigDecimal getPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrice(BigDecimal price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getQuantity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setQuantity(int quantity) {
		// TODO Auto-generated method stub
		
	}

}
