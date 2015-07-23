/**
 * 
 */
package com.gw.application.dao;

import java.util.List;

import com.gw.application.Account;
import com.gw.dao.BaseDominoDAO;

/**
 * @author Tom Van Aken
 *
 */
public class AccountDominoDAO extends BaseDominoDAO implements IAccountDAO {

	/* (non-Javadoc)
	 * @see com.gw.application.dao.IAccountDAO#deleteAccount(com.gw.application.Account)
	 */
	public boolean deleteAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gw.application.dao.IAccountDAO#getAccountsByKey(com.gw.application.dao.IAccountDAO.ACCOUNTFILTER, java.util.List)
	 */
	public List<Account> getAccountsByKey(ACCOUNTFILTER filter, List<String> key) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gw.application.dao.IAccountDAO#getAllAccounts()
	 */
	public List<Account> getAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gw.application.dao.IAccountDAO#loadAccount(java.lang.String)
	 */
	public Account loadAccount(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gw.application.dao.IAccountDAO#saveAccount(com.gw.application.Account)
	 */
	public boolean saveAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

}
