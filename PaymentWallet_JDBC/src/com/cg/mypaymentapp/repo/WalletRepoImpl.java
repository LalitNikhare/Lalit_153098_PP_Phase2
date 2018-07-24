
package com.cg.mypaymentapp.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo {

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "corp123");

		return con;

	}

	public boolean save(Customer customer) {
		try (Connection con = WalletRepoImpl.getConnection()) {
			PreparedStatement pstm = con.prepareStatement("insert into Customer values(?,?,?)");

			pstm.setString(1, customer.getMobileNo());
			pstm.setString(2, customer.getName());
			pstm.setBigDecimal(3, customer.getWallet().getBalance());
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Customer findOne(String mobileNo) {
		Customer customer = null;
		Wallet wallet = null;
		try (Connection con = WalletRepoImpl.getConnection()) {
			PreparedStatement pstm = con.prepareStatement("select * from Customer where mobile_number = ?");
			pstm.setString(1, mobileNo);
			ResultSet res = pstm.executeQuery();
			if (res.next() == false) {
				throw new InvalidInputException("Mobile Number does not exist");
			}
			customer = new Customer();
			wallet = new Wallet();
			customer.setMobileNo(res.getString(1));
			customer.setName(res.getString(2));
			// customer.setWallet(new Wallet(res.getBigDecimal(3)));
			wallet.setBalance(res.getBigDecimal(3));
			customer.setWallet(wallet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return customer;
	}

	@Override
	public Boolean update(Customer customer) {
		try (Connection con = WalletRepoImpl.getConnection()) {
			PreparedStatement pstm = con.prepareStatement("update Customer set balance = ? where mobile_number = ?");

			pstm.setBigDecimal(1, customer.getWallet().getBalance());
			pstm.setString(2, customer.getMobileNo());
			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean truncate() {
		try (Connection con = WalletRepoImpl.getConnection()) {
			String truncateQuery = "truncate table customer";
			Statement pstmt = con.createStatement();
			pstmt.executeUpdate(truncateQuery);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
