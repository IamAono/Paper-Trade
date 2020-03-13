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
		ArrayList<Stock2> myStocks = myAccount.myStocks;
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
    			System.out.println(" 2.view my balance\n3. deposit to my account\n4. withdraw from my account");
    			System.out.println("5.view my overall profit\n6. reset my account");
    			s = in.next();
    			if(s.equals("1")) {
    				myAccount.viewStocks();
    			}
    			else if(s.equals("2")) {
    				System.out.println(myAccount.balance);
    			}
    			else if(s.equals("3")) {
    				myAccount.depositToAccount();
    			}
    			else if(s.equals("4")) {
    				myAccount.withdrawFromAccount();
    			}
    			else if(s.equals("5")) {
    				System.out.println(myAccount.balance);
    			}
    			else if(s.equals("6")) {
    				System.out.println(myAccount.balance);
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
    				ticker.toUpperCase();
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
    			bd = bd.round(new MathContext(2));
    			double price = bd.doubleValue();
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
			}
    		else if(s.equals("4")) {
    			System.out.println("Which stock would you like to sell?");
			}
    		else if(s.equals("5")) {
    			break;
    		}
    		else {
    			System.out.println("That is not a valid number.");
    		}
    	}
    }
}
