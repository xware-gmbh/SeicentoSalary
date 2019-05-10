
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.WorkRole;

/**
 * Home object for domain model class WorkRole.
 * 
 * @see WorkRole
 */
public class WorkRoleDAO extends JPADAO<WorkRole, Long> {
	public WorkRoleDAO() {
		super(WorkRole.class);
	}
}