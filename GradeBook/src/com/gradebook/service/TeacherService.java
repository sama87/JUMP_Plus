package com.gradebook.service;

import java.util.Scanner;

import com.gradebook.repository.GradebookRepo;
import com.gradebook.utility.Color;

public class TeacherService {
	
	Scanner readIn = null;
	Color col = new Color();
	GradebookRepo repo = new GradebookRepo();
	
	public TeacherService(Scanner readIn){
		this.readIn = readIn;
	}

	public void login() {
		
		int id = -1;
		String username = "";
		String password = "";
		
		do {
		
			col.title();
			System.out.println("+----------------------+");
			System.out.println("|        Log In        |");
			System.out.println("+----------------------+");
			col.text();
			System.out.println();
			System.out.println("Please enter your username and password");
			System.out.println("or enter exit to exit");
			System.out.println();
			
			System.out.println("Username: ");
			col.input();
			username = readIn.nextLine();
			if(username.equals("exit") ) {
				col.clearScreen();
				return;
			}
			col.text();
			System.out.println("Password: ");
			col.input();
			password = readIn.nextLine();
			
			id = repo.getUserId(username, password);
			

			col.clearScreen();
			if(id < 0) {
				col.red();
				System.out.println("Invalid email or password!");
			}
			System.out.println();
			
		}while(id < 0);
		
		classMenu(id);
			
		return;
	}
	
	public void register() {
		String name;
		String username;
		String password;
		System.out.println("Name: ");
		name = readIn.nextLine();
		System.out.println("Username: ");
		username = readIn.nextLine();
		System.out.println("Password: ");
		password = readIn.nextLine();
		repo.addTeacher(name, username, password);
	}
	
	public void classMenu(int teacherId) {
		
		String menuInput = " ";
		Boolean exit = false;
		int classId = -1;
		
		 do{
				

				col.title();
				System.out.println("+--------------------+");
				System.out.println("|       Classes      |");
				System.out.println("+--------------------+");
				System.out.println();
				col.text();
				
				repo.getClasses(teacherId);
				
				System.out.println("\n\n\n1. Select Class");
				System.out.println("2. Add a New Class");
				System.out.println("3. Exit");
				
				System.out.println("Enter choice (1, 2, or 3) :");
				col.input();
				menuInput = readIn.nextLine();
				col.text();
				
				
				switch(menuInput){
				
				case "1": 
					System.out.println("\nEnter the Class id");
					System.out.println("Class id: ");
					classId = readIn.nextInt();
					readIn.nextLine();
					col.clearScreen();
					gradeMenu(classId);
					break;
				case "2":
					col.clearScreen();
					addClass(teacherId);
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
	}
	
	public void addClass(int teacherId) {
		
		String name = "";
		
		System.out.println("Enter Class Name: ");
		name = readIn.nextLine();
		
		repo.addClass(teacherId, name);
		return;
	}
	
	public void gradeMenu(int classId){
		
		String menuInput = "";
		int studentId = -1;
		int grade = 0;
		Boolean exit = false;
		
		do {
			col.title();
			System.out.println("+--------------------+");
			System.out.println("|        Grades      |");
			System.out.println("+--------------------+");
			System.out.println();
			col.text();
			
			repo.getClassGrades(classId, "student_id");
			
			System.out.println("\n\n\n1. View Grades Sorted By Name");
			System.out.println("2. View Grades Sorted By Grade");
			System.out.println("3. Update Grade");
			System.out.println("4. \"Add Student\"");
			System.out.println("5. \"Remove Student\"");
			System.out.println("6. Exit");
			
			System.out.println("Enter choice (1, 2, 3, 4, 5, or 6) :");
			col.input();
			menuInput = readIn.nextLine();
			col.text();
			
			
			switch(menuInput){
			
			case "1": 
				col.clearScreen();
				repo.getClassGrades(classId, "student_name");
				menuInput = readIn.nextLine();
				break;
			case "2":
				col.clearScreen();
				repo.getClassGrades(classId, "grade desc");
				menuInput = readIn.nextLine();
				break;
			case "3":
				System.out.println("Student Id: ");
				studentId = readIn.nextInt();
				readIn.nextLine();
				System.out.println("New Grade: ");
				grade = readIn.nextInt();
				readIn.nextLine();
				repo.updateGrade(classId, studentId, grade);
				col.clearScreen();
				break;
			case "4":
				System.out.println("Student Name: ");
				String name = readIn.nextLine();
				System.out.println("Grade: ");
				grade = readIn.nextInt();
				readIn.nextLine();
				int id = repo.addStudent(name);
				repo.addGrade(id, classId, grade);
				break;
			case "5":
				break;
			case "6":
				col.clearScreen();
				return;
			default: 
				col.clearScreen();
				col.red();
				System.out.println("Invalid Input!");
			}
			
	
		}while(!exit);
		
		return;
	}
	
	

}
