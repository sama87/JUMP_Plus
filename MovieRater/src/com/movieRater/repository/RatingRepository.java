package com.movieRater.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class RatingRepository {
	
	public void getAllRatings() {
		int id = -1;
		double avg;
		DecimalFormat df = new DecimalFormat("#.#");
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
			PreparedStatement stmt = conn.prepareStatement("select movies.movie_id, title, count(rating), avg(rating) from movies \r\n"
					+ "inner join ratings on movies.movie_id = ratings.movie_id\r\n"
					+ "group by ratings.movie_id;"); 
			ResultSet rs = stmt.executeQuery();
				){
			
			while(rs.next() ) {
				id = rs.getInt("movie_id");
				avg = rs.getDouble("avg(rating)");
				
				if (id < 10) System.out.println("   0" + id + "   " + rs.getString("title") + "\n           " 
				+ df.format(avg) + " / 5" + "     " + rs.getInt("count(rating)") + " votes\n" );
				else System.out.println("   " + id + "   " + rs.getString("title") + "\n           " 
						+ df.format(avg) + " / 5" + "     " + rs.getInt("count(rating)") + " votes\n" );
			}
			
		} 
		catch (SQLException e) {

			System.err.println("Couldn't connect to database");
		}
	}
	
	
	public int getUserId(String email, String password) {
		int id = -1;
	
		try( Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
			PreparedStatement stmt = conn.prepareStatement("select user_id from users where email = ? and password = ?");
				){
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() ) id = rs.getInt("user_id");
		}
		catch (SQLException e){
			System.err.println("Couldn't connect to database\n");
			return -1;
		}
		return id;
	}


	public void createUser(String email, String password) {
	
		try( Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
				PreparedStatement stmt = conn.prepareStatement("insert into users values(null, ?, ?)");//id, email, password
					){
				stmt.setString(1, email);
				stmt.setString(2, password);
				stmt.executeUpdate();
			}
			catch (SQLException e) {
				System.err.println("Couldn't connect to database\n");			
			}
		
		return;
	}


	public boolean checkUserExists(String email) {
		
		try( Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
				PreparedStatement stmt = conn.prepareStatement("select user_id from users where email = ?");
					){
				stmt.setString(1, email);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() ) return true;
			}
			catch (SQLException e) {
				System.err.println("Couldn't connect to database\n");			
			}

		
		return false;
	}


	public void getUserRatings(int id) {

		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
			PreparedStatement stmt = conn.prepareStatement("select movies.movie_id, title, rating from movies \r\n"
					+ "inner join ratings on movies.movie_id = ratings.movie_id where user_id = ?");
				){
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next() ) {
				id = rs.getInt("movie_id");
				if (id < 10) System.out.println("   0" + id + "   " + rs.getString("title") + "\n           " + rs.getDouble("rating") + " / 5\n");
				else System.out.println("   " + id + "   " + rs.getString("title") + "\n           " + rs.getDouble("rating") + " / 5\n");
			}
			
		} 
		catch (SQLException e) {

			System.err.println("Couldn't connect to database");
		}
		
	}


	public void createRating(int userId, int movieId, double rating) {
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/movie_rater","root","root");
				PreparedStatement stmt = conn.prepareStatement("select * from ratings where user_id = ? and movie_id = ?");
				PreparedStatement stmt2 = conn.prepareStatement("insert into ratings values(null, ?, ?, ?)");//rating_id, user_id, movie_id, rating
				PreparedStatement stmt3 = conn.prepareStatement("update ratings set rating = ? where user_id = ? and movie_id = ?");
				){
		
			stmt.setInt(1, userId);
			stmt.setInt(2, movieId);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				stmt3.setDouble(1, rating);
				stmt3.setInt(2, userId);
				stmt3.setInt(3, movieId);
				stmt3.executeUpdate();
			}
			else {
				stmt2.setInt(1, userId);
				stmt2.setInt(2, movieId);
				stmt2.setDouble(3, rating);
				stmt2.executeUpdate();
			}
			
			System.out.println("Rating Submitted \n");
		}
		catch (SQLException e) {

			System.err.println("Couldn't connect to database");
		}
	}

}
