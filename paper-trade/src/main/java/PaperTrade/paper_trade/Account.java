package PaperTrade.paper_trade;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Calendar;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Account implements Serializable{
	ArrayList<Stock2> myStocks;
	ArrayList<String> stockHistory;
	double overallProfit;
	double balance;
	public Account() {
		myStocks = new ArrayList<Stock2>();
		stockHistory = new ArrayList<String>();
		overallProfit = 0;
		balance = 0;
	}
	public void viewStocks() throws IOException {
		if(myStocks.size() == 0) {
			System.out.println("You don't own any stocks.");
		}
		else {
			for(Stock2 s : myStocks) {
				System.out.print("Ticker: " + s.ticker + ", Name: " + s.name + ", shares: " + s.quantity);
				System.out.print(", average cost: " + s.avgPrice + ", % change: " + s.percentChange());
				System.out.println(", $ change: " + (s.dollarChange() * s.quantity));
			}
		}
	}
	/*Can deposit an infinite amount.*/
	public void depositToAccount() {
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.println("How much would you like to deposit to your account?");
			try {
				double amount = in.nextDouble();
				BigDecimal bd = new BigDecimal(amount);
				int digits = bd.precision() - bd.scale();
				bd.round(new MathContext(digits + 2));
				amount = bd.doubleValue();
				balance += amount;
				System.out.println("Current balance: " + balance);
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Please enter in a valid amount.");
			}
		}
	}
	public void withdrawFromAccount() {
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.println("How much would you like to withdraw from your account?");
			try {
				double amount = in.nextDouble();
				BigDecimal bd = new BigDecimal(amount);
				int digits = bd.precision() - bd.scale();
				bd.round(new MathContext(digits + 2));
				amount = bd.doubleValue();
				if(amount > balance) {
					System.out.println("You do not have that much money to withdraw.");
					continue;
				}
				balance -= amount;
				System.out.println("Current balance: " + balance);
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Please enter in a valid amount.");
			}
		}
	}
	public void reset() {
		System.out.println("Are you sure you want to reset your account?\n1. yes\n2. no");
		Scanner in = new Scanner(System.in);
		String s = in.next();
		if(s.equals("1")) {
			myStocks.clear();
			stockHistory.clear();
			balance = 0;
			overallProfit = 0;
			System.out.println("Your account has been reset.");
		}
		else if(!s.equals("2")){
			System.out.println("Please enter a valid number.");
		}
	}
	public void buy(Stock stock, String ticker, double price, int shares) {
		boolean newStock = true;
		for(Stock2 stock2 : myStocks) {
			if(stock2.ticker.equals(ticker)) {
				newStock = false;
				stock2.buyMore(price, shares);
				break;
			}
		}
		if(newStock) {
			Stock2 st = new Stock2(ticker, stock.getName(), price, shares);
			myStocks.add(st);
		}
		balance -= price * shares;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		month = (month + 1) % 12;
		if(month == 0) {
			month = 1;
		}
		String date = String.format("%d/%d/%d", month, day, year);
		float p = (float) price;
		String history = String.format("On %s you bought %d shares of %s at %f", date, shares, ticker, p);
		stockHistory.add(history);
	}
	public void sell(String ticker) throws IOException {
		boolean own = false;
		Stock2 s2 = new Stock2();
		for(Stock2 s : myStocks) {
			if(s.ticker.equals(ticker)) {
				own = true;
				s2 = s;
				break;
			}
		}
		if(own) {
			System.out.println("How many shares would you like to sell?");
			Scanner in = new Scanner(System.in);
			int shares;
			while(true) {
				try {
					shares = in.nextInt();
					break;
				}
				catch(InputMismatchException e) {
					System.out.println("Please enter an integer.");
				}
			}
			if(s2.sell(shares)) {
				/*If all of the shares of that stock are sold, remove that stock from myStocks.*/
				System.out.println("Quantity: " + s2.quantity + " Shares: " + shares);
				if(s2.quantity == shares) { 
					myStocks.remove(myStocks.indexOf(s2));
				}
				Stock stock = YahooFinance.get(ticker);
				BigDecimal priceNow = stock.getQuote().getPrice();
				int digits = priceNow.precision() - priceNow.scale();
				priceNow = priceNow.round(new MathContext(digits + 2));
				float thePrice = priceNow.floatValue();
				String printOut = String.format("You have sold %d shares of %s at %f", shares, ticker, thePrice);
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				month = (month + 1) % 12;
				if(month == 0) {
					month = 1;
				}
				String date = String.format("%d/%d/%d", month, day, year);
				String history = String.format("On %s you sold %d shares of %s at %f", date, shares, ticker, thePrice);
				stockHistory.add(history);
				overallProfit += (thePrice - s2.avgPrice) * shares;
				balance += thePrice * shares;
				s2.quantity -= shares;
				System.out.println(printOut);
			}
		}
		else {
			System.out.println("You do not own that stock.");
		}
	}
	public void viewHistory() {
		for(String s : stockHistory) {
			System.out.println(s);
		}
	}
}
