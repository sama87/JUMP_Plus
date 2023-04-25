package com.movieRater.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.movieRater.utility.RandomGenerator;

public class PopulateVoters {
	
	public void populate() {
		RandomGenerator rnd = new RandomGenerator();
		double rating;
		Connection conn;
		try {
			conn =	DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
			//ratings_id, user_id, movie_id, rating
			PreparedStatement stmt = conn.prepareStatement("insert into ratings values(null, ?, ?, ?)");
			for(int users = 1; users <=5; users++) {
				stmt.setInt(1, users);
				for (int movies = 1; movies <=10; movies++) {
					
					stmt.setInt(2, movies);
					
					if(movies > 6) {
						rating = (double) rnd.randNum(5);
						if (rating < 4.0) rating = 4.0;
					}
					else if (movies > 3) rating = (double) rnd.randNum(5);
					else rating = (double) rnd.randNum(3)-1;
					
					stmt.setDouble(3, rating);
					stmt.executeUpdate();
				}
			}
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
