package PaperTrade.paper_trade;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Stock2 {
	String ticker;
	String name;
	double avgPrice;
	int quantity;
	public Stock2(String t, String n, double p, int q) {
		ticker = t.toUpperCase();
		name = n;
		avgPrice = p;
		quantity = q;
	}
	public void buyMore(double p, int q) {
		quantity += q;
		avgPrice = (avgPrice + (p * q)) / quantity;
	}
	public double percentChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal change = s.getQuote().getPrice();
		change = change.round(new MathContext(2));
		BigDecimal avg = new BigDecimal(avgPrice);
		avg = avg.round(new MathContext(2));
		change = change.subtract(avg);
		change = change.divide(avg);
		change = change.round(new MathContext(2));
		double theChange = change.doubleValue();
		return theChange;
	}
	public double dollarChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal change = s.getQuote().getPrice();
		BigDecimal avg = new BigDecimal(avgPrice);
		change = change.subtract(avg);
		change = change.round(new MathContext(2));
		double theChange = change.doubleValue();
		return theChange;
	}
}
