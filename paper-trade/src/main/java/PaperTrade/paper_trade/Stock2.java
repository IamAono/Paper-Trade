package PaperTrade.paper_trade;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Stock2 implements Serializable {
	private String ticker;
	private String name;
	private double avgPrice;
	private int quantity;
	public Stock2() {
		
	}
	public Stock2(String t, String n, double p, int q) {
		ticker = t.toUpperCase();
		name = n;
		avgPrice = p;
		quantity = q;
	}
	public String getTicker() {
		return ticker;
	}
	public String getName() {
		return name;
	}
	public double getAvgPrice() {
		return avgPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setAvgPrice(double d) {
		avgPrice = d;
	}
	public void setQuantity(int i) {
		quantity = i;
	}
	public void buyMore(double p, int q) {
		double d = avgPrice * quantity;
		quantity += q;
		avgPrice = (d + (p * q)) / quantity;
		String avgPriceStr = Double.toString(avgPrice);
		String[] split = avgPriceStr.split("\\.");
		while(split[1].length() < 2) {
			split[1] += '0';
		}
		avgPrice = Double.parseDouble(split[0] + '.' + split[1].substring(0, 2));
	}
	public double percentChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal price = s.getQuote().getPrice();
		String priceStr = price.toString();
		String[] split = priceStr.split("\\.");
		while(split[1].length() < 2) {
			split[1] += '0';
		}
		double d = Double.parseDouble(split[0] + '.' + split[1].substring(0, 2));
		double change = (d - avgPrice) / avgPrice;
		String changeStr = Double.toString(change);
		String[] split2 = changeStr.split("\\.");
		while(split2[1].length() < 2) {
			split2[1] += '0';
		}
		return Double.parseDouble(split2[0] + '.' + split2[1].substring(0, 2));
	}
	public double dollarChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal change = s.getQuote().getPrice();
		BigDecimal avg = new BigDecimal(avgPrice);
		change = change.subtract(avg);
		if(change.compareTo(new BigDecimal(0.01)) == -1) {
			return 0;
		}
		String changeStr = change.toString();
		String[] split = changeStr.split("\\.");
		while(split[1].length() < 2) {
			split[1] += '0';
		}
		double theChange = Double.parseDouble(split[0] + '.' + split[1].substring(0, 2));
		return theChange;
	}
	public void split() {
		
	}
	public void merge() {
		
	}
}
