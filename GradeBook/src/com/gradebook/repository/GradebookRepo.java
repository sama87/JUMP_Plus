package com.gradebook.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GradebookRepo {
	
	final private String URI = "jdbc:mysql://localhost:3306/school_db";
	final private String DB_UN = "root";
	final private String DB_PASS = "root";
	
	
	public int getUserId(String username, String password) {


		int id = -1;
		
		try(Connection conn = DriverManager.getConnection(URI,DB_UN,DB_PASS);
				PreparedStatement stmt = conn.prepareStatement("select teacher_id from teachers where username = ?"
						+ " and pass = ?");){
			
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next() ) id = rs.getInt("teacher_id");
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to database");
			return -1;
		}
		
		return id;
	}

	public void getClasses(int teacherId) {
		
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("select * from classes where teacher_id = ?;") ){
			stmt.setInt(1, teacherId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next() ) System.out.println("   " + rs.getInt("class_id") + "  " + rs.getString("class_name"));
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
		
		return;
	}
	
	public void addClass(int teacherId, String name) {
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("insert into classes values (null, ? , ?); ")
			){
			stmt.setInt(1, teacherId);
			stmt.setString(2, name);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
	}
	
	public void getClassGrades(int classId, String sort) {
		
		int count = 0;
		double num1;
		double num2;
		double median = 0.0;
		
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("select students.student_id, student_name, grade from students \r\n"
						+ "inner join grades on students.student_id = grades.student_id\r\n"
						+ "where class_id = ? order by " + sort);
				PreparedStatement avg = conn.prepareStatement("select avg(grade) from students \r\n"
						+ "inner join grades on students.student_id = grades.student_id\r\n"
						+ "where class_id = ?");
				PreparedStatement med = conn.prepareStatement("select students.student_id, student_name, grade from students \r\n"
						+ "inner join grades on students.student_id = grades.student_id\r\n"
						+ "where class_id = ? order by grade");
				){
			stmt.setInt(1, classId);
			//stmt.setString(2, sort);
			avg.setInt(1, classId);
			med.setInt(1, classId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next() ) {
				System.out.println(rs.getInt("student_id") + "   " + rs.getString("student_name") + "   " + rs.getInt("grade") );
				count++;
			}

			rs = avg.executeQuery();
			if (rs.next() ) System.out.println("\n\n Average Grade : " + rs.getDouble("avg(grade)") );
			
			rs = med.executeQuery();
			
			if (count == 0) median = 0.0;
			else if (count % 2 == 0) {
				for(int i = 0; i < count; i++) rs.next();
				num1 = (double) rs.getInt("grade");
				rs.next();
				num2 = (double) rs.getInt("grade");
				median = (num1 + num2)/2;
			}
			else {
				count = count/2 + 1;
				for(int i = 0; i < count; i++) rs.next();
				median = (double) rs.getInt("grade");
			}
			

			System.out.println("\n Median Grade: " + median);
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't connect to database");
		}
		
		return;
	}

	public void updateGrade(int classId, int studentId, int grade) {
		
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("update grades set grade = ? where class_id = ? and "
						+ "student_id = ? ")
			){
			stmt.setInt(1, grade);
			stmt.setInt(2, classId);
			stmt.setInt(3, studentId);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
		
	}

	public void addTeacher(String name, String username, String password) {

		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("insert into teachers values(null, ?, ?, ? )")
			){
			stmt.setString(1, name);
			stmt.setString(2, username);
			stmt.setString(3, password);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
		
	}

	public int addStudent(String name) {
		int id = -1;
		
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("insert into students values(null, ?)" + Statement.RETURN_GENERATED_KEYS)
			){
			stmt.setString(1, name);
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			if(keys.next()) id = keys.getInt(1);
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
		
		return id;
	}

	public void addGrade(int studId, int classId, int grade) {
		
		try(Connection conn = DriverManager.getConnection(URI, DB_UN, DB_UN);
				PreparedStatement stmt = conn.prepareStatement("insert into grades values(?, ?, ?)")
			){
			stmt.setInt(1, studId);
			stmt.setInt(2, classId);
			stmt.setInt(3, grade);
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Couldn't connect to database");
		}
		
	}

}
