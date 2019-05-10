
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowLabel;

/**
 * Home object for domain model class RowLabel.
 * 
 * @see RowLabel
 */
public class RowLabelDAO extends JPADAO<RowLabel, Long> {
	public RowLabelDAO() {
		super(RowLabel.class);
	}
}