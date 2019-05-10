package ch.xwr.seicentobookit.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.xwr.seicentobookit.dal.EmployeeDAO;
import ch.xwr.seicentobookit.dal.SalaryDAO;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Salary;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;

/*
 * Create new Salary Records from Employee data
 */
public class CreateSalary {
	private Date refDate;
	private Salary dto;
	private CalculationHelper helper;

	/**
	 * Konstruktor
	 * @param refDate Lohndatum per
	 */
	public CreateSalary(Date refDate) {
		this.refDate = refDate;
		helper = new CalculationHelper();
	}

	public Salary getDto() {
		return this.dto;
	}

	public void createRecords(boolean cpLastValue) {

		// loop over active Employees
		EmployeeDAO empDao = new EmployeeDAO();
		List<Employee> slrs = empDao.findAll();

		for (Iterator<Employee> iterator = slrs.iterator(); iterator.hasNext();) {
			Employee emp = iterator.next();
			//check Status und MA noch im Datumsrange
			if (emp.getEmpState().equals(LovState.State.active)) {
				if (refDate.after(emp.getEmpStartwork())) {
					if (emp.getEmpEndWork() == null || refDate.before(emp.getEmpEndWork())) {						
						createSingleSalary(emp, cpLastValue);
					}					
				}
			}
		}
	}

	private void createSingleSalary(Employee emp, boolean cpLastValue) {
		Salary lastSal = getLastSalary(emp, cpLastValue);
		Salary sal = new Salary();

		sal.setEmployee(emp);
		sal.setSlrDate(refDate);
		sal.setSlrSalaryBase(emp.getEmpBaseSalary());
		if (cpLastValue &&  lastSal != null) {
			sal.setSlrSalaryBase(lastSal.getSlrSalaryBase());
		}
		sal.setSlrKidsAdditon(emp.getEmpKidsAddition());
		sal.setSlrState(LovState.State.active);
		sal.setSlrType(Salary.SalaryType.Normal);

		//set % as default
		SalaryCalculationLine clc = helper.getCalculationBase(sal, emp);
		sal.setSlrFactorAdmin(clc.getSlxFactorAdmin());
		sal.setSlrFactorAhv(clc.getSlxFactorAhv());
		sal.setSlrFactorAlv(clc.getSlxFactorAlv());
		sal.setSlrFactorSol(clc.getSlxFactorSol());
		sal.setSlrFactorFak(clc.getSlxFactorFak());

		sal.setSlrBaseBvg(getBaseBvg(emp));
		sal.setSlrBaseAlv(new Double(0));
		sal.setSlrBaseSol(new Double(0));
		sal.setSlrAmountSourceTax(new Double(0));
		sal.setSlrBirthAddon(new Double(0));
		
		CalculateSalary calc = new CalculateSalary(sal, emp);
		calc.calculateSalary();
		dto = calc.getDto();

		SalaryDAO dao = new SalaryDAO();
		dao.save(dto);

		//PersistenceUtils.getEntityManager(Salary.class).persist(sal);
		
		//create Objectroot
		RowObjectManager man = new RowObjectManager();
		man.updateObject(dto.getSlrId(), dto.getClass().getSimpleName());		
	}

	private Salary getLastSalary(Employee emp, boolean cpLastValue) {
		if (! cpLastValue) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(refDate);
		int year = cal.get(Calendar.YEAR);
		
		int month = cal.get(Calendar.MONTH);
		if (month == 0) year--;
		
		SalaryDAO dao = new SalaryDAO();
		Salary sal = null;
		List<Salary> slrs = dao.getEmployeeSalaries(emp, year, refDate);
		//Salary sal =
		for (Iterator<Salary> iterator = slrs.iterator(); iterator.hasNext();) {
			sal = iterator.next();
			break;
		}

		
		return sal;
	}

	private Double getBaseBvg(Employee emp) {
        //if (comboBoxType.getConvertedValue().equals(Salary.SalaryType.Normal)) return new Double(0);
        //return computePercentage(newEmp.getSalarybvgbase().getSbesalarydefamt(), 12., new BigDecimal(1));    	
		return computePercentage(emp.getSalaryBvgBase().getSbeSalarydefAmt(), 12.);
	}

    private Double computePercentage(Double input, double d1) {
    	//Double output = input.divide(new Double(d1), RoundingMode.HALF_DOWN);
    	Double output = input/d1;
        return swissCommercialRound(new BigDecimal(output));    	
    }
    
    private Double swissCommercialRound(BigDecimal input)
    {
    	double ans = java.lang.Math.round((input.doubleValue() / 0.05)) * 0.05;
    	BigDecimal value = new BigDecimal(ans).setScale(2, RoundingMode.HALF_DOWN);

        return value.doubleValue();
    }

}
