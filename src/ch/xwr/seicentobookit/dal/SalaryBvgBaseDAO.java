
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.SalaryBvgBase;

/**
 * Home object for domain model class SalaryBvgBase.
 * 
 * @see SalaryBvgBase
 */
public class SalaryBvgBaseDAO extends JPADAO<SalaryBvgBase, Long> {
	public SalaryBvgBaseDAO() {
		super(SalaryBvgBase.class);
	}
}