package com.dollarsbank.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dollarsbank.utility.RandomGenerator;

public class Account {
	
	RandomGenerator rdm = new RandomGenerator();
	

	
	private long accountNum;
	private double balance;
	private int custId;
	private List<String> transactions = new ArrayList<String>();
	
	public Account() {
		super();
	}

	public Account(long idCounter) {
		idCounter = idCounter + rdm.randNum(100);
		this.accountNum = idCounter;
	}

	public long getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(long accountNum) {
		this.accountNum = accountNum;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public List<String> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}
	
	public void addTransaction(String transaction) {
		LocalDateTime timestamp = LocalDateTime.now();
		this.transactions.add(transaction + "\nBalance: " + this.balance + " as of: " + timestamp);
	}

	@Override
	public String toString() {
		return "Account [accountNum=" + accountNum + ", balance=" + balance + ", custId=" + custId + "]";
	}
	
	
	
	

}
