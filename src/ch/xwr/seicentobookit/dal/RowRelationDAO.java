
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowRelation;

/**
 * Home object for domain model class RowRelation.
 * 
 * @see RowRelation
 */
public class RowRelationDAO extends JPADAO<RowRelation, Long> {
	public RowRelationDAO() {
		super(RowRelation.class);
	}
}