package com.dollarsbank.utility;


public class RandomGenerator {
	
	public int randNum(int maxNum) {
		
		//Get time in millisecs, perfom mod to get random number. 
		//Add 1 at end to ensure no 0. max num - 1 to make sure we get max specified (ie 100 instead of 101)
		int randNum = (int) (System.currentTimeMillis() % (maxNum - 1) ) + 1;
		return randNum;
	}
}
