
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowSecurity;

/**
 * Home object for domain model class RowSecurity.
 * 
 * @see RowSecurity
 */
public class RowSecurityDAO extends JPADAO<RowSecurity, Long> {
	public RowSecurityDAO() {
		super(RowSecurity.class);
	}
}