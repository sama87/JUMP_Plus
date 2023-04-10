package com.dollarsbank.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.*;

import com.dollarsbank.model.Customer;
import com.dollarsbank.utility.Clear;
import com.dollarsbank.utility.Color;


public class CustomerService {

	Clear clear = new Clear();
	Color col = new Color();
	
	public Customer newCustomer(Scanner readIn, Connection conn){
		
		Customer newCustomer = new Customer();
		String pass = "";
		
		col.title();
		System.out.println("+-----------------------+");
		System.out.println("|   Account Creation    |");
		System.out.println("+-----------------------+\n");
		
		
		col.text();
		System.out.println("Customer Name: ");
		col.input();
		newCustomer.setName(readIn.nextLine() );
		
		col.text();
		System.out.println("Customer Address: ");
		col.input();
		newCustomer.setAddress(readIn.nextLine() );
		
		col.text();
		System.out.println("Customer Phone Number: ");
		col.input();
		newCustomer.setPhone(readIn.nextLine() );
		
		col.text();
		System.out.println("User Name: ");
		col.input();
		newCustomer.setUserName(readIn.nextLine() );
		
		col.text();
		System.out.println("Password (8 characters w/ at least one each capital, lower, & special): ");
		col.input();
		pass = readIn.nextLine();
		while(!validatePass(pass)) {
			col.red();
			System.out.println("\n\nInvalid password!");
			System.out.println("Password (8 characters w/ at least one each capital, lower, & special: ");
			col.input();
			pass = readIn.nextLine();
		}
		newCustomer.setPassword(pass);
		
		//Write the new customer to the DB. Get id of that customer, write it to the Java entity, so that it can be used 
		// when making the account
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into customers values(null, ?,?, ?, ?, ?)",
					+ Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, newCustomer.getName() );
			stmt.setString(2, newCustomer.getAddress() );
			stmt.setString(3, newCustomer.getPhone() );
			stmt.setString(4, newCustomer.getUserName() );
			stmt.setString(5, newCustomer.getPassword() );
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next() ) newCustomer.setId(keys.getInt(1) );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		return newCustomer;
	}
	
	private boolean validatePass(String pass) {
		
		Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[#?!@$%^&*-]).{8,}$");
		Matcher matcher = pattern.matcher(pass);
		
		if (matcher.matches()) return true;
		
		return false;
	}
}
