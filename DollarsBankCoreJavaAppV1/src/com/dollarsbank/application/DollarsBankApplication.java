package com.dollarsbank.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DollarsBankApplication {

	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dollars_bank","root","root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Couldn't connect to database");
		}

		MainMenu menu = new MainMenu();
		menu.printMenu(conn);

		
		System.out.println("Thank you for using Dollars Bank!");
	}

}
