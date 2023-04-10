package com.dollarsbank.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.utility.Clear;
import com.dollarsbank.utility.Color;

public class AccountService  {
	
	Color col = new Color();
	Clear clear = new Clear();
	
	public Account newAccount(Scanner readIn, Customer cust, Connection conn) {
		
		long counter = 0;
		
		//Get the current account number
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("select counter from counter where id = 0");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) counter = rs.getInt(1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Create account. Also increments the counter with the next account number
		Account newAccount = new Account(counter);
		counter = newAccount.getAccountNum();
		
		//Write counter to the DB
		try {
			stmt = conn.prepareStatement("update counter set counter = " + counter + " where id = 0");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		col.text();
		System.out.println();
		System.out.println("Initial Deposit Amount: ");
		col.input();
		newAccount.setBalance(readIn.nextDouble());
		readIn.nextLine();//Must advance the line reader any time nextInt, nextDouble etc is used
		
		newAccount.setCustId(cust.getId());
		
		
		
		//Write the new account to the DB
		try {
			stmt = conn.prepareStatement("insert into accounts values(?, ?, ?)");
			stmt.setLong(1, newAccount.getAccountNum() );
			stmt.setDouble(2, newAccount.getBalance() );
			stmt.setInt(3, newAccount.getCustId() );
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Failed to create new account");
		}
		
		addTransaction(newAccount, "Initial deposit amount: " + newAccount.getBalance(), conn );
		
		return newAccount;
	}
	
	
	
	public void accountMenu(Scanner readIn, int customerId, Connection conn){
		
		String menu = "";
		boolean exit = false;
		Account account = null;
		
		while(!exit) {
		
			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement("select * from accounts where cust_id = ?");
				stmt.setInt(1, customerId);
				ResultSet rs = stmt.executeQuery();
				
				if(rs.next() ) {
					account = new Account();
					account.setAccountNum(rs.getLong("accountNumber") );
					account.setBalance(rs.getDouble("balance") );
					account.setCustId(rs.getInt("cust_id") );
				}
				else {
					col.red();
					System.err.println("Couldn't connect to account");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			col.title();
			System.out.println("+-----------------------+");
			System.out.println("|   Welcome Customer!   |");
			System.out.println("+-----------------------+");
			
			System.out.println();
			col.text();
			System.out.println("1. Make a Deposit");
			System.out.println("2. Make a Withdrawal");
			System.out.println("3. Transfer Funds");
			System.out.println("4. View Transaction History");
			System.out.println("5. View Customer Profile");
			System.out.println("6. Sign Out");
			
			System.out.println("\nEnter your selection (1-6):");
			col.input();
			menu = readIn.nextLine();
			
			switch(menu) {
			case "1":
				clear.clearScreen();
				deposit(readIn, account, conn);
				break;
			case "2":
				clear.clearScreen();
				withdrawal( readIn, account, conn) ;
				break;
			case "3":
				clear.clearScreen();
				transfer(readIn, account, conn);
				break;
			case "4":
				clear.clearScreen();
				transactions(readIn, account, conn);
				break;
			case "5":
				clear.clearScreen();
				printProfile(readIn, account, conn);
				break;
			case "6":
				exit = true;
				clear.clearScreen();
				break;
			default:
				clear.clearScreen();
				col.red();
				System.out.println("Invalid input! Please enter 1, 2, 3, 4, 5, or 6!");
			}
		
		}
		
		return;
	}
	
	
	
	private void deposit(Scanner readIn, Account account, Connection conn) {
		
		double deposit = -1.0;

		col.title();
		System.out.println("+-------------+");
		System.out.println("|   Deposit   |");
		System.out.println("+-------------+");
		
		while (deposit < 0.0) {
			System.out.println();
			col.text();
			System.out.println("Current Balance: " + account.getBalance() );
			System.out.println("Deposit Amount: ");
			col.input();
			deposit = readIn.nextDouble();
			readIn.nextLine();
			
			if (deposit < 0.0) {
				col.red();
				System.out.println("Invalid Input! Please enter a positive amount or enter 0 to cancel deposit");
			}
		}
		
		col.text();

		double prevBalance = account.getBalance();
		account.setBalance(deposit + account.getBalance() );//update java object

		//Update the sql server data
		try {
			PreparedStatement stmt = conn.prepareStatement("update accounts set balance = ? where accountNumber = ?");
			stmt.setDouble(1, account.getBalance() );
			stmt.setLong(2, account.getAccountNum() );
			stmt.executeUpdate();
		} catch (SQLException e) {
			account.setBalance(prevBalance);
			col.red();
			System.err.println("Deposit Failed");
			col.text();
			System.out.println("\nPress ENTER to Continue...");
			readIn.nextLine();
			return;
		}

		addTransaction(account, "Deposited Amount: " + deposit, conn); 
		System.out.println("\n\n\nPrevious Balance: " + prevBalance );
		System.out.println("Amount Deposited: " + deposit);
		System.out.println("New Balance: " + account.getBalance() );
		System.out.println("\nPress ENTER to Continue...");
		readIn.nextLine();
		
		clear.clearScreen();
		
		return;
	}
	
	
	
	private void withdrawal(Scanner readIn, Account account, Connection conn) {
		
		double withdrawal = -1.0;
		
		col.title();
		System.out.println("+----------------+");
		System.out.println("|   Withdrawal   |");
		System.out.println("+----------------+");
		
		while (withdrawal < 0.0 || withdrawal > account.getBalance()) {
			System.out.println();
			col.text();
			System.out.println("Current Balance: " + account.getBalance() );
			System.out.println("Withdrawal Amount: ");
			col.input();
			withdrawal = readIn.nextDouble();
			readIn.nextLine();
			
			col.red();
			if (withdrawal < 0.0) System.out.println("Invalid Input! Please enter a positive amount or enter 0 to cancel withdrawal");
			else if (withdrawal > account.getBalance() ) System.out.println("Withdrawal cannot exceed your current balance!");
		}
		
		col.text();
		
		double prevBalance = account.getBalance();
		account.setBalance(account.getBalance() - withdrawal);
		
		//Update the sql server data
		try {
			PreparedStatement stmt = conn.prepareStatement("update accounts set balance = ? where accountNumber = ?");
			stmt.setDouble(1, account.getBalance() );
			stmt.setLong(2, account.getAccountNum() );
			stmt.executeUpdate();
		} catch (SQLException e) {
			account.setBalance(prevBalance);
			col.red();
			System.err.println("Deposit Failed");
			col.text();
			System.out.println("\nPress ENTER to Continue...");
			readIn.nextLine();
			return;
		}
		
		
		addTransaction(account, "Withdrawn Amount: " + Color.RED + withdrawal + Color.BLUE, conn);
		
		System.out.println("\n\n\nPrevious Balance: " + prevBalance );
		System.out.println("Amount Withdrawn: " + Color.RED + withdrawal);
		col.text();
		System.out.println("New Balance: " + account.getBalance() );
		System.out.println("\nPress ENTER to Continue...");
		readIn.nextLine();
		
		clear.clearScreen();
		
		return;
	}
	
	
	
	private void transfer(Scanner readIn, Account account, Connection conn) {
		
		Account account2 = new Account();
		long accountNumber2 = -1;
		boolean accountValid = false;
		double transferAmount = -1.0;
		char confirm = ' ';
		PreparedStatement stmt;
		ResultSet rs;

		while(!accountValid) {
		
			col.title();
			System.out.println("+--------------------+");
			System.out.println("|   Transfer Funds   |");
			System.out.println("+--------------------+");
			
			col.text();
			System.out.println();
			System.out.println("Please enter the account number for the account you wish to TRANSFER TO");
			System.out.println("Account Number: ");
			col.input();
			accountNumber2 = readIn.nextLong();
			
			try {
				stmt = conn.prepareStatement("select * from accounts where accountNumber = ?");
				stmt.setLong(1, accountNumber2);
				rs = stmt.executeQuery();
				if(rs.next()) {
					accountValid = true;
					account2.setAccountNum(accountNumber2);
					account2.setBalance(rs.getDouble("balance") );
				}
				else {
					col.red();
					System.out.println("Account not found. Please try again");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				col.red();
				e.printStackTrace();
			}
		
		}//while

		while (transferAmount < 0.0 || transferAmount > account.getBalance()) {
			col.text();
			System.out.println("Current Balance: " + account.getBalance() );
			System.out.println("Transfer Amount: ");
			col.input();
			transferAmount = readIn.nextDouble();
			readIn.nextLine();
			
			col.red();
			if(transferAmount < 0.0) System.out.println("\nPlease enter a positive number!\n");
			else if(transferAmount > account.getBalance() ) System.out.println("\nAmount to transfer can't exceed current balance!\n");
			
		}
			
		while(confirm != 'Y') {
			col.text();
			System.out.println("$" + transferAmount + " will be transfered from Account Number " +
					account.getAccountNum() + " to account number " + accountNumber2);
			System.out.println("Please enter (Y)es to confirm, or (N) to cancel:");
			col.input();
			confirm = readIn.next().toUpperCase().charAt(0);
		
			if (confirm == 'N') {
				col.text();
				System.out.println("Transfer canceled. Press ENTER to continue...");
				readIn.nextLine();
				return;
			}
		}
		
		col.text();
		
		
		
		//Adjust Balances in java objects
		double prevBalance = account.getBalance();
		account.setBalance(account.getBalance() - transferAmount);
		account2.setBalance(account2.getBalance() + transferAmount);
		
		
		//Update the sql server data
		try {
			stmt = conn.prepareStatement("update accounts set balance = ? where accountNumber = ?");
			stmt.setDouble(1, account.getBalance() );
			stmt.setLong(2, account.getAccountNum() );
			
			PreparedStatement stmt2 = conn.prepareStatement("update accounts set balance = ? where accountNumber = ?");
			stmt2.setDouble(1, account2.getBalance() );
			stmt2.setLong(2, account2.getAccountNum() );
			
			//Grouped together to minimize chances of issues connecting to db after first execution
			stmt2.executeUpdate();
			stmt.executeUpdate();
		} catch (SQLException e) {
			//reset java objects in event of failure writing to DB
			account.setBalance(prevBalance); 
			account2.setBalance(account2.getBalance() - transferAmount);
			col.red();
			System.err.println("Transfer Failed");
			col.text();
			System.out.println("\nPress ENTER to Continue...");
			readIn.nextLine();
			return;
		}
		
		
		//Add transactions to history
		addTransaction(account, "Transfer to account # " + account2.getAccountNum()
			+	": " + Color.RED  + transferAmount + Color.BLUE, conn);
		addTransaction(account2, "Transfer from  account # " + account.getAccountNum()
				+	": " + transferAmount, conn);
		
		
		//Output of results
		System.out.println("Previous Balance: " + prevBalance);
		System.out.println("$" + transferAmount + " was transfered from Account Number " +
				account.getAccountNum() + " to account number " + account2.getAccountNum());
		System.out.println("Current Balance: " + account.getBalance());
		System.out.println("Press ENTER to continue...");
		readIn.nextLine();
		
		clear.clearScreen();
		
		return;
	}
	
	
	private void transactions(Scanner readIn, Account account, Connection conn ) {
		
		List<String> transactions = account.getTransactions();
		

		char selection = ' ';
		String header = "Last 5 transactions sorted from most recent to oldest: ";
		int numTransactions = 0; //number of transactions existing for account
		int offset = 0;//Number of transactions to skip for a given query, used with LIMIT
		
		PreparedStatement stmt;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("select count(transaction_id) from transactions where accountNumber = ?");
			stmt.setLong(1, account.getAccountNum() );
			rs = stmt.executeQuery();
			if( rs.next() ) {
				numTransactions = rs.getInt("Count(transaction_id)");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		do {
			col.title();
			System.out.println("+-------------------------+");
			System.out.println("|   Transaction History   |");
			System.out.println("+-------------------------+");
			
			col.text();
			System.out.println();
			System.out.println(header);
			System.out.println();
			
			//Query DB
			try {
				stmt = conn.prepareStatement("select * from transactions where accountNumber = ? order by transaction_id desc limit ?, 5");
				stmt.setLong(1, account.getAccountNum() );
				stmt.setInt(2, offset);
				rs = stmt.executeQuery();
				//Display results
				while(rs.next() ) {
					System.out.println(rs.getString("msg"));
					System.out.println();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			System.out.println("\n\nSelect (N)ext, (P)revious, Show (A)ll, or E(X)it: ");
			selection = readIn.next().toUpperCase().charAt(0);
			readIn.nextLine();
			
			switch(selection) {
			case 'N':
				if (offset + 5 < numTransactions) {
					offset += 5;
					if (offset + 5 < numTransactions) {
						header = "Transactions " + (offset + 1) + " to " + (offset + 5) + " of " + numTransactions + " sorted by most recent transactions";
					}
					else {
						//It is possible for lastIndex to be up to 4 greater than number of transactions, since
						//there is a set page size of 5
						header = "Transactions " + (offset + 1) + " to " + numTransactions + " of " + numTransactions + " sorted by most recent transactions";
					}
				}
				break;
				
			case 'P':
				if(offset > 0) {
					offset -= 5;
					header = "Transactions " + (offset + 1) + " to " + (offset + 5) + " of " + numTransactions + " sorted by most recent transactions";
				}
				break;
				
			case 'A':
				try {
					clear.clearScreen();
					stmt = conn.prepareStatement("select * from transactions where accountNumber = ? order by transaction_id desc");
					stmt.setLong(1, account.getAccountNum() );
					rs = stmt.executeQuery();
					System.out.println("All transactions - Sorted from most recent to oldest\n\n");
					//Display results
					while(rs.next() ) {
						System.out.println(rs.getString("msg"));
						System.out.println();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("\nPress ENTER to continue...");
				readIn.nextLine();
				selection = 'X';
				break;
			case 'X':
				break;
			}
			

			clear.clearScreen();
			
		}while(selection != 'X');
		
		return;
	}



	private void printProfile(Scanner readIn, Account account, Connection conn) {
		

		//int lastFour = (int) account.getAccountNum() % 10000;
		
		Customer cust = new Customer();
		
		try {
			PreparedStatement stmt = conn.prepareStatement("select * from customers where cust_id = ?");
			stmt.setInt(1, account.getCustId() );
			ResultSet rs = stmt.executeQuery();
			if(rs.next() ) {
				cust.setName(rs.getString("cust_name") );
				cust.setAddress(rs.getString("address") );
				cust.setPhone(rs.getString("phone"));
				cust.setUserName(rs.getString("userName") );
			}		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		col.title();
		System.out.println("+----------------------+");
		System.out.println("|   Customer Profile   |");
		System.out.println("+----------------------+");
		
		col.text();
		System.out.println("Name: " + cust.getName() );
		System.out.println("\nUsername: " + cust.getUserName() );
		System.out.println("\nAddress: " + cust.getAddress() );
		System.out.println("\nPhone Number: " + cust.getPhone() );
		//if (lastFour == 0) System.out.println("\nAccount Number: *****0000");
		//else System.out.println("\nAccount Number: *****" + lastFour);
		System.out.println("\nAccount Number: " + account.getAccountNum() );//Need to include for testing
		System.out.println("\nAccount Balance: " + account.getBalance());
		
		System.out.println("\n\nPress ENTER to continue...");
		readIn.nextLine();
		
		return;
	}
	
	
	private void addTransaction(Account account, String msg, Connection conn) { 
		
		LocalDateTime timestamp = LocalDateTime.now();
		msg = msg + "\nBalance: " + account.getBalance() + " as of: " + timestamp;
		
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into transactions values(null, ?, ?)");
			stmt.setLong(1, account.getAccountNum() );
			stmt.setString(2, msg);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Failed to log transaction");
		}
				
		
		return;
	}
	
	
}