package com.gradebook.application;

import java.util.Scanner;
import com.gradebook.service.TeacherService;
import com.gradebook.utility.Color;

public class GradeBookApplication {
	
	public static void main(String[] args) {
		
		menu();
	
	}
	
	private static void menu() {
		
		Boolean exit = false;
		String menuInput = "";
		Color col = new Color();
		Scanner readIn = new Scanner(System.in);
		TeacherService teachService = new TeacherService(readIn);
		
		 do{
			

					col.title();
					System.out.println("+---------------------+");
					System.out.println("|      Grade Book     |");
					System.out.println("+---------------------+");
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
						col.clearScreen();
						teachService.register();
						break;
					case "2":
						col.clearScreen();
						teachService.login();
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
		
		 readIn.close();
		return;
	}

}
