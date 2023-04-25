package com.movieRater.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.movieRater.model.PopulateVoters;
import com.movieRater.repository.RatingRepository;
import com.movieRater.service.AccountService;
import com.movieRater.utility.Color;

public class MovieRaterApplication {

	public static void main(String[] args) {
		Connection conn = null;
		Color col = new Color();
		Boolean exit = false;
		String menuInput = "";
		Scanner readIn = new Scanner(System.in);
		RatingRepository repo = new RatingRepository();
		AccountService acctSrv = new AccountService(readIn);
		
		//Used once to generate very non-biased initial ratings!
//		PopulateVoters populateVoters = new PopulateVoters();
//		populateVoters.populate();
		
		
		while(!exit) {
			
			col.title();
			System.out.println("+---------------------+");
			System.out.println("|     Movie Rater     |");
			System.out.println("+---------------------+");
			System.out.println();
			col.text();
			
			repo.getAllRatings();
			
			System.out.println("1. Create new account");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			
			System.out.println("Enter choice (1, 2, or 3) :");
			col.input();
			menuInput = readIn.nextLine();
			col.text();
			
			
			switch(menuInput){
			
			case "1": 
				col.clearScreen();
				acctSrv.register();
				break;
			case "2":
				col.clearScreen();
				acctSrv.login();
				break;
			case "3":
				col.clearScreen();
				exit = true;
				break;
			default: 
				col.clearScreen();
				col.red();
				System.out.println("Invalid Input!");
			}
			
		
		}
		
		
		

		//MainMenu menu = new MainMenu();
		//menu.printMenu(conn);

		readIn.close();
		System.out.println("Thank you for using Movie Rater!");
	}

}
