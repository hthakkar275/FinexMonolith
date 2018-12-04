package org.hemant.thakkar.stockexchange;

public interface Product {
	public long getId();
	public void setId(long id);
	public String getSymbol();
	public void setSymbol(String symbol);
	public String getDescription();
	public void setDescription(String description);
}
