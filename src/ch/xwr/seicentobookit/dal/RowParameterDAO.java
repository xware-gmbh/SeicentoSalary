
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowParameter;

/**
 * Home object for domain model class RowParameter.
 * 
 * @see RowParameter
 */
public class RowParameterDAO extends JPADAO<RowParameter, Long> {
	public RowParameterDAO() {
		super(RowParameter.class);
	}
}