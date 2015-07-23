package com.gw.application.dao;

import java.util.List;

import com.gw.application.Account;

public interface IAccountDAO {
	public enum ACCOUNTFILTER{NAME, SECTOR, TYPE, MANAGER, STATUS}
	
	public Account loadAccount(String id);
	public List<Account> getAllAccounts();
	public List<Account> getAccountsByKey(ACCOUNTFILTER filter, List<String> key);
	public boolean saveAccount(Account account);
	public boolean deleteAccount(Account account);
	
	
}
