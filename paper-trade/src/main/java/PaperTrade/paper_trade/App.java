package PaperTrade.paper_trade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

public class App {
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
    	File f = new File("myAccount.dat");
    	Account myAccount = new Account();
		if(f.exists()) {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			myAccount = (Account) in.readObject();
			in.close();
		}
        Scanner in = new Scanner(System.in);
    	while(true) {
    		System.out.println("What would you like to do?\n1. view a stock\n2. view my account");
    		System.out.println("3. buy a stock\n4. sell a stock\n5. exit");
    		String s = in.next();
    		if(s.equals("1")) {
    			System.out.println("Type in the ticker symbol of the stock that you are interested in.");
    			while(true) {
    				s = in.next();
            		try {
            			Stock stock = YahooFinance.get(s);
            			stock.print();
            			break;
            		}
            		catch(Exception NullPointerException) {
            			System.out.println("That is not a valid ticker symbol, please try again.");
            		}
    			}
    		}
    		else if(s.equals("2")) {
    			System.out.println("What would you like to do with your account?\n1. view my stocks");
    			System.out.println("2. view my balance\n3. view my history\n4. deposit to my account");
    			System.out.println("5. withdraw from my account\n6. view my overall profit\n7. reset my account");
    			s = in.next();
    			if(s.equals("1")) {
    				myAccount.viewStocks();
    			}
    			else if(s.equals("2")) {
    				System.out.println(myAccount.balance);
    			}
    			else if(s.equals("3")) {
    				myAccount.viewHistory();
    			}
    			else if(s.equals("4")) {
    				myAccount.depositToAccount();
    			}
    			else if(s.equals("5")) {
    				myAccount.withdrawFromAccount();
    			}
    			else if(s.equals("6")) {
    				System.out.println(myAccount.overallProfit);
    			}
    			else if(s.equals("7")) {
    				myAccount.reset();
    			}
    			else {
    				System.out.println("Please enter in a valid number.");
    			}
    			
    		}
    		else if(s.equals("3")) {
    			System.out.println("Which stock would you like to buy?");
    			String ticker;
    			while(true) {
    				ticker = in.next();
    				ticker = ticker.toUpperCase();
            		try {
            			Stock stock = YahooFinance.get(ticker);
            			break;
            		}
            		catch(Exception NullPointerException) {
            			System.out.println("That is not a valid ticker symbol, please try again.");
            		}
    			}
    			System.out.println("How many shares would you like to buy?");
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
    			Stock stock = YahooFinance.get(ticker);
    			BigDecimal bd = stock.getQuote().getPrice();
    			int digits = bd.precision() - bd.scale();
    			bd = bd.round(new MathContext(digits + 2));
    			double price = bd.doubleValue();
    			if(price * shares > myAccount.balance) {
    				System.out.print("You don't have enough money to complete the purchase,");
    				System.out.println(" please deposit more money into your account.");
    			}
    			else {
    				myAccount.buy(stock, ticker, price, shares);
    			}
			}
    		else if(s.equals("4")) {
    			System.out.println("Which stock would you like to sell?");
    			String ticker;
    			while(true) {
    				ticker = in.next();
    				ticker = ticker.toUpperCase();
            		try {
            			Stock stock = YahooFinance.get(ticker);
            			break;
            		}
            		catch(Exception NullPointerException) {
            			System.out.println("That is not a valid ticker symbol, please try again.");
            		}
    			}
    			myAccount.sell(ticker);
			}
    		else if(s.equals("5")) {
    			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
    			out.writeObject(myAccount);
    			out.close();
    			break;
    		}
    		else {
    			System.out.println("That is not a valid number.");
    		}
    	}
    }
}
