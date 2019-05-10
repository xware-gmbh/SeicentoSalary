
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowText;

/**
 * Home object for domain model class RowText.
 * 
 * @see RowText
 */
public class RowTextDAO extends JPADAO<RowText, Long> {
	public RowTextDAO() {
		super(RowText.class);
	}
}