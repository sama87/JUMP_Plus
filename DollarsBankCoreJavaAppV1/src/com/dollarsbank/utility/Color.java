package com.dollarsbank.utility;

public class Color {
	
	public static final String RESET = "\u001B[0m";
	public static final String YELLOW = "\033[1;33m";
	public static final String RED = "\033[0;91m";
	public static final String BLUE = "\033[0;96m";
	public static final String GREEN = "\033[0;92m";
	


	
	public void title() {
		System.out.print(RESET + YELLOW);
		return;
	}
	
	
	public void input() {
		System.out.print(RESET + GREEN);
		return;
	}
	
	public void red() {
		System.out.print(RESET + RED);
		return;
	}
	
	public void text() {
		System.out.print(RESET + BLUE);
		return;
	}
	
}
