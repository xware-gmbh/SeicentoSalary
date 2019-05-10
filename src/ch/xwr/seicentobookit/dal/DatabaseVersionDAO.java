
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.DatabaseVersion;

/**
 * Home object for domain model class DatabaseVersion.
 * 
 * @see DatabaseVersion
 */
public class DatabaseVersionDAO extends JPADAO<DatabaseVersion, Long> {
	public DatabaseVersionDAO() {
		super(DatabaseVersion.class);
	}
}