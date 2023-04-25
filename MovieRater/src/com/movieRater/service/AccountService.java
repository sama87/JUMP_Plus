package com.movieRater.service;

import java.util.Scanner;
import java.util.regex.Pattern;

import com.movieRater.repository.RatingRepository;
import com.movieRater.utility.Color;

public class AccountService {
	Color col = new Color();
	Scanner readIn = null;
	RatingRepository repo = new RatingRepository();
	
	public AccountService(Scanner readIn) {
		this.readIn = readIn;
	}
	
	public void login() {
		
		int id = -1;
		String email = "";
		String password = "";
		
		do {
		
			col.title();
			System.out.println("+----------------------+");
			System.out.println("|        Log In        |");
			System.out.println("+----------------------+");
			col.text();
			System.out.println();
			System.out.println("Please enter your email and password");
			System.out.println("or enter exit to exit");
			System.out.println();
			
			System.out.println("Email: ");
			col.input();
			email = readIn.nextLine();
			if(email.equals("exit") ) {
				col.clearScreen();
				return;
			}
			col.text();
			System.out.println("Password: ");
			col.input();
			password = readIn.nextLine();
			
			id = repo.getUserId(email, password);
			

			col.clearScreen();
			if(id < 0) {
				col.red();
				System.out.println("Invalid email or password!");
			}
			System.out.println();
			
		}while(id < 0);
		
		menu(id);
			
		return;
	}
	
	
	public void register() {
		Boolean valid = false;
		String email = "";
		String password = "";
		

		
		col.title();
		System.out.println("+------------------------+");
		System.out.println("|        Register        |");
		System.out.println("+------------------------+");
		col.text();
		System.out.println();
		System.out.println("Please enter your email and password");
		System.out.println("or enter exit to exit");
		System.out.println();
			
		do {
			System.out.println("Email: ");
			col.input();
			email = readIn.nextLine();
			if(email.equals("exit") ) {
				col.clearScreen();
				return;
			}
			valid = validateEmail(email);
		}while(!valid);
		
		valid = false;
		
		do {
			col.text();
			System.out.println("Password: ");
			col.input();
			password = readIn.nextLine();
			System.out.println("Confirm Password: ");
			if(password.equals(readIn.nextLine() ) ) valid = true;
			else System.out.println(Color.RED + "Passwords didn't match!" + Color.BLUE);
		}while(!valid);
			
		repo.createUser(email, password);

		col.clearScreen();
		return;
	}
	
	
	public void menu(int id) {
		
		boolean exit = false;
		String menuInput = "";
		
		do{
			
			col.title();
			System.out.println("+---------------------+");
			System.out.println("|     Movie Rater     |");
			System.out.println("+---------------------+");
			System.out.println();
			col.text();
			
			repo.getAllRatings();
			
			System.out.println("1. Add a Rating");
			System.out.println("2. View Your Ratings");
			System.out.println("3. Exit");
			
			System.out.println("Enter choice (1, 2, or 3) :");
			col.input();
			menuInput = readIn.nextLine();
			col.text();
			
			
			switch(menuInput){
			
			case "1": 
				col.clearScreen();
				addRating(id);
				break;
			case "2":
				col.clearScreen();
				repo.getUserRatings(id);
				System.out.println("Press ENTER to continue...");
				readIn.nextLine();
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
		}while(!exit);
		
		return;
	}
	
	
	private void addRating(int id) {
		
		int movieId = -1;
		double rating = -1.0;
		
		col.title();
		System.out.println("+---------------------+");
		System.out.println("|     Movie Rater     |");
		System.out.println("+---------------------+");
		System.out.println();
		col.text();
		
		repo.getAllRatings();
		
		System.out.println("Enter number of movie to rate");
		System.out.println("Movie Number: ");
		movieId = readIn.nextInt();
		readIn.nextLine();
		while(rating < 0.0 || rating > 5.0) {
			System.out.println("Movies are rated from 0 to 5");
			System.out.println("Rating: ");
			rating = (double) readIn.nextInt();
		}
		
		repo.createRating(id, movieId, rating);
		
		return;
	}

	public Boolean validateEmail(String email) {
		if(!Pattern.matches(".*@.*[.]com", email) ) {
			System.out.println(Color.RED + "Invalid email! Format is email@somedomain.com" + Color.BLUE);
			return false;
		}
		if (repo.checkUserExists(email) ) {
			System.out.println(Color.RED + "There is already an account under " + email + ". Please try a different one, or login" + Color.BLUE);
			return false;
		}
		
		return true;
	}
	
}
