package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return this.getId() == order.getId();
    }

    @Override
    public int hashCode() {
        if (getId() > Integer.MAX_VALUE) {
        	return (int) (getId() % Integer.MAX_VALUE);
        } else {
        	return (int) getId();
        }
    }
    
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append(" id = ").append(this.getId()).append(";");
		message.append(" time = ").append(formatter.format(LocalDateTime.now())).append(";");
		message.append(" side = ").append(this.getSide()).append(";");
		message.append(" qty = ").append(this.getQuantity()).append(";");
		message.append(" price = ").append(this.getPrice()).append(";");
		message.append(" participant: ").append(this.getParticipant());
		return message.toString();

	}
}
