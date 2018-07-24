package com.cg.mypaymentapp.service;

import java.math.BigDecimal;
import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {

	private WalletRepo repo;

	public WalletServiceImpl() {
		repo = new WalletRepoImpl();
	}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount) {
		Wallet wallet = new Wallet(amount);
		Customer customer = new Customer(name, mobileNo, wallet);

		if (!isValid(customer)) {
			throw new InvalidInputException("Invalid Information,Please try again.Exiting");
		}
		repo.save(customer);

		return customer;
	}

	public Customer showBalance(String mobileNo) {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null)
			return customer;
		else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) {
		if (sourceMobileNo == targetMobileNo)
			throw new InvalidInputException("Source and Target mobile number cannot be equal");
		Customer customerSrc = withdrawAmount(sourceMobileNo, amount);
		Customer customerTgt = depositAmount(targetMobileNo, amount);
		if (customerSrc != null && customerTgt != null)
			return customerSrc;
		else
			return null;
	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {
			BigDecimal currBalance = customer.getWallet().getBalance();
			BigDecimal finalBalance = currBalance.add(amount);
			Wallet wallet = customer.getWallet();
			wallet.setBalance(finalBalance);
			customer.setWallet(wallet);
			repo.update(customer);
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");

	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {
			BigDecimal currBalance = customer.getWallet().getBalance();
			int compare = currBalance.compareTo(amount);
			if (compare == -1) {
				throw new InsufficientBalanceException("Insufficient Balance to complete the transaction");
			} else {
				BigDecimal finalBalance = currBalance.subtract(amount);
				Wallet wallet = customer.getWallet();
				wallet.setBalance(finalBalance);
				customer.setWallet(wallet);
				repo.update(customer);
			}
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");
	}

	public boolean isValid(Customer customer) {
		String regexName = "[a-zA-Z]*";
		String regexNum = "[1-9][0-9]{9}";
		if (customer.getName().equals(null) || customer.getMobileNo().equals(null)
				|| customer.getWallet().getBalance().equals(null))
			throw new NullPointerException();
		if (customer.getName().matches(regexName) && customer.getMobileNo().matches(regexNum))
			return true;
		return false;
	}

	public boolean truncate() {
		return repo.truncate();
	}
}