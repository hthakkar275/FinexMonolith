package org.hemant.thakkar.stockexchange;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public abstract class TradableImpl implements Tradable {

	private static AtomicLong idGenerator = new AtomicLong(1);
	private long id;
	private Product product;
	private Participant participant;
	private LocalDateTime entryTime;

	public TradableImpl() {
		this.setId(idGenerator.getAndIncrement());
		this.setEntryTime(LocalDateTime.now());
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
	public Product getProduct() {
		return this.product;
	}

	@Override
	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public LocalDateTime getEntryTime() {
		return this.entryTime;
	}

	@Override
	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	@Override
	public Participant getParticipant() {
		return this.participant;
	}

	@Override
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
}
