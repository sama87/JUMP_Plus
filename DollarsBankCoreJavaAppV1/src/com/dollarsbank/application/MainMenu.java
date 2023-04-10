package com.dollarsbank.application;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.service.AccountService;
import com.dollarsbank.service.CustomerService;
import com.dollarsbank.service.LoginService;
import com.dollarsbank.utility.Clear;
import com.dollarsbank.utility.Color;

public class MainMenu {
	
	public void printMenu(Connection conn) {
		
		Clear clear = new Clear();
		Color col = new Color();
		boolean exit = false;
		String menuInput = "";
		Scanner readIn = new Scanner(System.in);
		
		CustomerService cService = new CustomerService();
		AccountService aService = new AccountService();
		LoginService login = new LoginService();
		
		Customer newCust = null;
		Account newAcct = null;
		
		List<Customer> customerTB = new ArrayList<Customer>();
		List<Account> accountTB = new ArrayList<Account>();
		
		while(!exit) {
		
			col.title();
			System.out.println("+----------------------+");
			System.out.println("|     DOLLARS BANK     |");
			System.out.println("+----------------------+");
			System.out.println();
			col.text();
			System.out.println("1. Create new account");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			
			System.out.println("Enter choice (1, 2, or 3) :");
			col.input();
			menuInput = readIn.nextLine();
			col.text();
			
			
			switch(menuInput){
			
			case "1": 
				clear.clearScreen();
				newCust = cService.newCustomer(readIn, conn);
				customerTB.add(newCust);
				aService.newAccount(readIn, newCust, conn);
				accountTB.add(newAcct);
				clear.clearScreen();
				break;
			case "2":
				clear.clearScreen();
				login.login(readIn, conn);
				break;
			case "3":
				clear.clearScreen();
				exit = true;
				break;
			default: 
				clear.clearScreen();
				col.red();
				System.out.println("Invalid Input!");
			}
			
		
		}//while
		
		readIn.close();
		
	}//printMenu

}//class
