package com.dollarsbank.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.utility.Clear;
import com.dollarsbank.utility.Color;

public class LoginService {
	
	Color col = new Color();
	public Clear clear = new Clear();

	public void login(Scanner readIn, Connection conn){
		
		AccountService service = new AccountService();
		
		String pass = "";
		String userName = "";
		int customerIndex = -1;
		boolean validLogin = false;
		
		
		while (!validLogin) {
			col.title();
			System.out.println("+----------------------+");
			System.out.println("|     DOLLARS BANK     |");
			System.out.println("+----------------------+");
			col.text();
			System.out.println();
			System.out.println("Please enter your credentials");
			//System.out.println("or enter exit ") implement later
			System.out.println();
			
			System.out.println("Username: ");
			col.input();
			userName = readIn.nextLine();
			col.text();
			System.out.println("Password: ");
			col.input();
			pass = readIn.nextLine();
			
			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement("select * from customers where userName = ? and user_password = ?");
				stmt.setString(1, userName);
				stmt.setString(2, pass);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() ) {
					customerIndex = rs.getInt("cust_id");
					validLogin = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			clear.clearScreen();
			col.red();
			if(!validLogin) System.out.println("Invalid username or password!");
			System.out.println();
		
		}//while(!validLogin)
		
		clear.clearScreen();
		service.accountMenu(readIn, customerIndex, conn);
	
		return;
		
	}//login
	
	
}//class
