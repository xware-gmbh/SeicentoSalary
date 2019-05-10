
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.CostAccount;

/**
 * Home object for domain model class CostAccount.
 * 
 * @see CostAccount
 */
public class CostAccountDAO extends JPADAO<CostAccount, Long> {
	public CostAccountDAO() {
		super(CostAccount.class);
	}
}