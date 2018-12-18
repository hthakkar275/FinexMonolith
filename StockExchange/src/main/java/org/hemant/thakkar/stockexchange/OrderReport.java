package org.hemant.thakkar.stockexchange;

import java.util.ArrayList;

public class OrderReport {
	private ArrayList<Trade> trades = new ArrayList<Trade>();
	private boolean orderInBook = false;
	private Order order;
	
	public OrderReport(ArrayList<Trade> trades, 
					   boolean orderInBook) {
		this.trades = trades;
		this.orderInBook = orderInBook;
	}

	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}

	public ArrayList<Trade> getTrades() {
		return trades;
	}

	public boolean isOrderInBook() {
		return orderInBook;
	}
	
	public String toString() {
		String retString = "--- Order Report ---:\nTrades:\n";
		for (Trade t : trades) {
			retString += ("\n" + t.toString());
		}
		retString += ("order in book? " + orderInBook + "\n");
		retString+= ("\nOrders:\n");
		retString += (order.toString());
		return  retString + "\n--------------------------";
	}

}
