package org.hemant.thakkar.stockexchange;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Equity implements Product {

	private static AtomicLong idGenerator = new AtomicLong(1);

	private long id;
	private String symbol;
	private String description;

	public Equity() {
		this.id = idGenerator.getAndIncrement();
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
	public String getSymbol() {
		return this.symbol;
	}

	@Override
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append(" id = ").append(this.getId()).append(";");
		message.append(" symbol = ").append(getSymbol());
		return message.toString();

	}
}
