package PaperTrade.paper_trade;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Stock2 implements Serializable {
	String ticker;
	String name;
	double avgPrice;
	int quantity;
	public Stock2() {
		
	}
	public Stock2(String t, String n, double p, int q) {
		ticker = t.toUpperCase();
		name = n;
		avgPrice = p;
		quantity = q;
	}
	public void buyMore(double p, int q) {
		double d = avgPrice * quantity;
		quantity += q;
		avgPrice = (d + (p * q)) / quantity;
		BigDecimal bd = new BigDecimal(avgPrice);
		int digits = bd.precision() - bd.scale();
		bd = bd.round(new MathContext(digits + 2));
		avgPrice = bd.doubleValue();
	}
	public boolean sell(int q) {
		if(q > quantity) {
			System.out.println("You do not own that many shares.");
			return false;
		}
		else {
			return true;
		}
	}
	public double percentChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal change = s.getQuote().getPrice();
		int digits = change.precision() - change.scale();
		change = change.round(new MathContext(digits + 2));
		BigDecimal avg = new BigDecimal(avgPrice);
		digits = avg.precision() - avg.scale();
		avg = avg.round(new MathContext(digits + 2));
		change = change.subtract(avg);
		try {
			MathContext round = new MathContext(2);
			change = change.divide(avg, round);
			double theChange = change.doubleValue();
			return theChange;
		}
		catch(ArithmeticException e) {
			return 0;
		}
	}
	public double dollarChange() throws IOException {
		Stock s = YahooFinance.get(ticker);
		BigDecimal change = s.getQuote().getPrice();
		BigDecimal avg = new BigDecimal(avgPrice);
		change = change.subtract(avg);
		int digits = change.precision() - change.scale();
		change = change.round(new MathContext(digits + 2));
		double theChange = change.doubleValue();
		return theChange;
	}
}
