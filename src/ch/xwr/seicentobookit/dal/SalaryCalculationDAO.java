
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.SalaryCalculation;

/**
 * Home object for domain model class SalaryCalculation.
 * 
 * @see SalaryCalculation
 */
public class SalaryCalculationDAO extends JPADAO<SalaryCalculation, Long> {
	public SalaryCalculationDAO() {
		super(SalaryCalculation.class);
	}
}