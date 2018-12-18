package org.hemant.thakkar.stockexchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


public interface OrderBook {
	void reset();

	OrderReport processOrder(Order quote, boolean verbose);

	void cancelOrder(String side, int qId, int time);

	void modifyOrder(int qId, HashMap<String, String> quote);

	int getVolumeAtPrice(String side, double price);

	double getBestBid();

	double getWorstBid();

	double getBestOffer();

	double getWorstOffer();

	int getLastOrderSign();

	int volumeOnSide(String side);

	BigDecimal getTickSize();

	BigDecimal getSpread();

	double getMid();

	boolean bidsAndAsksExist();

	String toString();

	List<Trade> getTape();

}
