
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.Bank;

/**
 * Home object for domain model class Bank.
 * 
 * @see Bank
 */
public class BankDAO extends JPADAO<Bank, Long> {
	public BankDAO() {
		super(Bank.class);
	}
}