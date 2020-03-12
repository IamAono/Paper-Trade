package PaperTrade.paper_trade;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

public class App {
    public static void main(String[] args) {
        try {
        	File myStocks = new File("myStocks.txt");
        	if(myStocks.createNewFile()) {
        		System.out.println("File created: " + myStocks.getName());
        	}
        	else {
        		System.out.println("File already exists");
        	}
        }
        catch (IOException e) {
        	System.out.println("An error occured.");
        }
        Scanner in = new Scanner(System.in);
    	while(true) {
    		System.out.println("What would you like to do?\n1. view a stock\n2. buy a stock\n3. sell a stock\n4. exit");
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
    			System.out.println("Which stock would you like to buy?");
			}
    		else if(s.equals("3")) {
    			System.out.println("Which stock would you like to sell?");
			}
    		else if(s.equals("4")) {
    			break;
    		}
    		else {
    			System.out.println("That is not a valid number.");
    		}
    	}
    }
}
