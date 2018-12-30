package org.hemant.thakkar.stockexchange;

import java.util.concurrent.atomic.AtomicLong;

public class Broker implements Participant {

	private static AtomicLong idGenerator = new AtomicLong(1);

	private long id;
	private String name;
	
	public Broker() {
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("id = ").append(getId()).append(";");
		message.append("name = ").append(getName());
		return message.toString();
	}
}
