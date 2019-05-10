
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.RowFile;

/**
 * Home object for domain model class RowFile.
 * 
 * @see RowFile
 */
public class RowFileDAO extends JPADAO<RowFile, Long> {
	public RowFileDAO() {
		super(RowFile.class);
	}
}