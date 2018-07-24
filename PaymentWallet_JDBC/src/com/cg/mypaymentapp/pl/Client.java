package com.cg.mypaymentapp.pl;

import java.math.BigDecimal;
import java.util.Scanner;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.service.WalletService;
import com.cg.mypaymentapp.service.WalletServiceImpl;

public class Client {
	private WalletService walletsrv;
	private Customer customer;

	public Client() {

		walletsrv = new WalletServiceImpl();
	}

	public void menu() {
		System.out.println("1. Create Payment Wallet Account ");
		System.out.println("2. Show Balance in Account ");
		System.out.println("3. Deposit Money into Account ");
		System.out.println("4. Withdraw Money from Account ");
		System.out.println("5. Transfer Money to another wallet ");
		System.out.println("0. Exit Application");

		Scanner console = new Scanner(System.in);

		System.out.println("Please select an option");
		int choice = console.nextInt();

		switch (choice) {
		case 1:
			System.out.println("1. Enter Name: ");
			String name = console.next();

			System.out.println("2. Enter Mobile no: ");
			String mobile = console.next();

			System.out.println("3. Enter initial balance: ");
			BigDecimal balance = console.nextBigDecimal();

			try {
				customer = walletsrv.createAccount(name, mobile, balance);
				System.out.println(" Wallet successfully created with following details " + "\n" + customer);
			} catch (InvalidInputException e) {
				System.out.println("Wrong Input");
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case 2:
			System.out.println("Enter mobile number to view balance");
			String mobileNum = console.next();

			Customer customer;
			try {
				customer = walletsrv.showBalance(mobileNum);
				System.out.println("Account : " + customer.getMobileNo());
				System.out.println("Balance : " + customer.getWallet().getBalance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case 3:
			System.out.println("Enter mobile number");
			mobileNum = console.next();
			System.out.println("Enter Amount to deposit");
			BigDecimal amount = console.nextBigDecimal();

			try {
				customer = walletsrv.depositAmount(mobileNum, amount);
				System.out.println("Account No: " + customer.getMobileNo());
				System.out.println("Balance : " + customer.getWallet().getBalance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 4:
			System.out.println("Enter mobile number");
			mobileNum = console.next();
			System.out.println("Enter Amount to withdraw");
			amount = console.nextBigDecimal();

			try {
				customer = walletsrv.withdrawAmount(mobileNum, amount);
				System.out.println("Account No: " + customer.getMobileNo());
				System.out.println("Balance : " + customer.getWallet().getBalance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case 5:
			System.out.println("Enter your mobile number");
			String mobileNumSrc = console.next();
			System.out.println("Enter target mobile number");
			String mobileNumTgt = console.next();
			System.out.println("Enter Amount to Transfer");
			amount = console.nextBigDecimal();

			try {
				customer = walletsrv.fundTransfer(mobileNumSrc, mobileNumTgt, amount);
				System.out.println("Account No: " + customer.getMobileNo());
				System.out.println("Balance : " + customer.getWallet().getBalance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case 9:

			break;
		case 0:
			System.out.println("Goodbye");
			System.exit(0);
			break;

		default:
			System.out.println("Invalid Option");
			break;
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		// make the application runs forever unless you close it
		while (true)
			client.menu();
	}
}
