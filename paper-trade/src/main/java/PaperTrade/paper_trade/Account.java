package PaperTrade.paper_trade;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Account implements Serializable{
	ArrayList<Stock2> myStocks;
	double overallProfit;
	double balance;
	public Account() {
		myStocks = new ArrayList<Stock2>();
		overallProfit = 0;
		balance = 0;
	}
	public void viewStocks() throws IOException {
		for(Stock2 s : myStocks) {
			System.out.print("Ticker: " + s.ticker + ", Name: " + s.name + ", average cost: " + s.avgPrice);
			System.out.println(", % change: " + s.percentChange() + ", $ change: " + s.dollarChange());
		}
	}
	/*Can deposit an infinite amount.*/
	public void depositToAccount() {
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.println("How much would you like to deposit to your account?");
			try {
				double amount = in.nextDouble();
				balance += amount;
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
				if(amount > balance) {
					System.out.println("You do not have that much money to withdraw.");
					continue;
				}
				balance -= amount;
				break;
			}
			catch(InputMismatchException e) {
				System.out.println("Please enter in a valid amount.");
			}
		}
	}
	public void reset() {
		System.out.println("Are you sure you want to reset your account?");
		Scanner in = new Scanner(System.in);
		String s = in.next();
		if(s.equals("1")) {
			myStocks.clear();
			balance = 0;
			overallProfit = 0;
			System.out.println("Your account has been reset.");
		}
		else if(!s.equals("2")){
			System.out.println("Please enter a valid number.");
		}
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
				if(s2.quantity == shares) { 
					myStocks.remove(myStocks.indexOf(s2));
				}
				Stock stock = YahooFinance.get(ticker);
				BigDecimal priceNow = stock.getQuote().getPrice();
				priceNow = priceNow.round(new MathContext(2));
				float thePrice = priceNow.floatValue();
				String printOut = String.format("You have sold %d shares of %s at %f", shares, ticker, thePrice);
				overallProfit += (thePrice - s2.avgPrice) * shares;
				balance += thePrice * shares;
				System.out.println(printOut);
			}
		}
		else {
			System.out.println("You do not own that stock.");
		}
	}
}
