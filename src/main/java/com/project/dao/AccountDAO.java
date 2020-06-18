package com.project.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.models.Account;
import com.project.models.AccountStatus;
import com.project.models.AccountType;
import com.project.models.SimpAccount;
import com.project.util.ConnectionUtil;
public class AccountDAO implements IAccountDAO{
	@Override
	public int insert(Account a) { //re
		try (Connection conn = ConnectionUtil.getConnection()) { //obtains conneciton from utility class
			String sql = "INSERT INTO ACCOUNTS (balance, status_id, type_id) VALUES (?,?,?)";
			// the ? are placeholders for input values, they work for PreparedStatements, and are designed to protect from SQL Injection
			PreparedStatement stmt = conn.prepareStatement(sql); //using prepared statement to prevent sql injection
			//inserting below values into the ?'s above
			double balance = a.getBalance(); //initial object creation of balance
			AccountType accounttype = a.getType(); //initial account type 
			AccountStatus accountstatus = a.getStatus();
		//	stmt.setDouble(1, double balance = a.getBalance(); //initial object creation of balance);
			//			stmt.setInt(6, u.getRole().getId());
			stmt.setDouble(1, balance);
			stmt.setInt(2, accountstatus.getId());
			stmt.setInt(3, accounttype.getId());
			return stmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public List<Account> findAll() { // Return all users
		List<Account> allAccount = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM account ORDER BY accountid" ;

			Statement stmnt = conn.createStatement();
			ResultSet resultSet = stmnt.executeQuery(sql); 
			
			while(resultSet.next()) { // For each entry in the result set
				
				int id = resultSet.getInt("accountid"); // Grab the account id
				double balance = resultSet.getDouble("balance");
				int statID = resultSet.getInt("status_id");
				int typeID = resultSet.getInt("type_id");
				
				
				Account a = new Account(id, balance, new AccountStatus(statID, ""), new AccountType(typeID,""));
				allAccount.add(a);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<Account>(); 
		}
		return allAccount;
	}
	@Override
	public Account findById(int accountId) { 
		Account result = null;
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM ACCOUNTS " + "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id " + "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id " + "WHERE ACCOUNTS.ID = ?";
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, accountId); // Defines the WHERE ID = ?
			ResultSet rs = stmnt.executeQuery(); // grabs result set of the query
			while(rs.next()) { // While there are results:
				int accountID = rs.getInt("id");
				double balance = rs.getDouble("balance");
				int statusId = rs.getInt("status_id");
				String statusName = rs.getString("status");
				int typeId = rs.getInt("type_id");
				String typeName = rs.getString("type");
				AccountStatus accountStatus = new AccountStatus(statusId,statusName);
				AccountType accountType = new AccountType(typeId,typeName);
				result = new SimpAccount(accountID,balance,accountStatus,accountType);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return result; 
			}
		return null;
	}
	@Override
	public List<Account> findByStatus(int statusId) {
		//CONFIRMED WORKS
		List<Account> everyAccount = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT *" + "FROM ACCOUNTS " + "INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id " + "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id " + "WHERE ACCOUNT_STATUS.id = ?"; // gets all accounts that match the specific account status ID
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, statusId);
			ResultSet resultSet = stmnt.executeQuery(); // Right as this is executed, the query runs to the database and grabs the info
			while(resultSet.next()) { 
				double balance = resultSet.getDouble("BALANCE");
				int asID = resultSet.getInt("status_id");
				String asStatus = resultSet.getString("status");
				int atID = resultSet.getInt("type_id");
				String atType = resultSet.getString("type");
				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				Account a = new SimpAccount(atID,balance,as,at);
				everyAccount.add(a); 
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<Account>();
		}
		return everyAccount;
	}
	public List<Account> findByType(int typeId){
		
		List<Account> everyAccount = new ArrayList<>();
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "SELECT * " + "FROM ACCOUNTS INNER JOIN ACCOUNT_STATUS ON ACCOUNTS.status_id = ACCOUNT_STATUS.id " + "INNER JOIN ACCOUNT_TYPE ON ACCOUNTS.type_id = ACCOUNT_TYPE.id " + "WHERE ACCOUNT_TYPE.id = ?";
			PreparedStatement stmnt = conn.prepareStatement(sql);
			stmnt.setInt(1, typeId);
			ResultSet rs = stmnt.executeQuery();
			while(rs.next()) { 
				int id = rs.getInt("id"); 
				double balance = rs.getDouble("balance");
				int asID = rs.getInt("status_id");
				String asStatus = rs.getString("status");
				int atID = rs.getInt("type_id");
				String atType = rs.getString("type");
				AccountStatus as = new AccountStatus(asID,asStatus);
				AccountType at = new AccountType(atID, atType);
				Account a = new SimpAccount(id,balance,as,at);
				everyAccount.add(a); 
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return new ArrayList<Account>(); 
		}
		return everyAccount;
	}
}