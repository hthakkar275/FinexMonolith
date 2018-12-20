package org.hemant.thakkar.stockexchange;

import java.time.LocalDateTime;

public interface Tradable {
	public long getId();
	public void setId(long id);
	public Product getProduct();
	public void setProduct(Product product);
	public LocalDateTime getEntryTime();
	public void setEntryTime(LocalDateTime entryTime);
	public Participant getParticipant();
	public void setParticipant(Participant participant);
	public Side getSide();
	public void setSide(Side side);
}
