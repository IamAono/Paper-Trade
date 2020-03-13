package PaperTrade.paper_trade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Account {
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
			System.out.print("Ticker: " + s.ticker + ", Name: " + s.name + ", % change: " + s.percentChange());
			System.out.println(", $ change: " + s.dollarChange());
		}
	}
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
}
