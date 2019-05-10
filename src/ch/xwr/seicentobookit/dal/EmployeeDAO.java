
package ch.xwr.seicentobookit.dal;

import com.xdev.dal.JPADAO;

import ch.xwr.seicentobookit.entities.Employee;

/**
 * Home object for domain model class Employee.
 * 
 * @see Employee
 */
public class EmployeeDAO extends JPADAO<Employee, Long> {
	public EmployeeDAO() {
		super(Employee.class);
	}
}