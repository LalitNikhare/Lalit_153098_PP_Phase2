package com.cg.mypaymentapp.repo;

import com.cg.mypaymentapp.beans.Customer;

public interface WalletRepo {

	public boolean save(Customer customer);

	public Customer findOne(String mobileNo);

	public Boolean update(Customer customer);
	
	public boolean truncate();
}
