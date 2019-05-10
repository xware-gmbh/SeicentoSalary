package ch.xwr.seicentobookit.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import ch.xwr.seicentobookit.business.model.SolAlvValues;
import ch.xwr.seicentobookit.dal.SalaryBvgBaseLineDAO;
import ch.xwr.seicentobookit.dal.SalaryCalculationLineDAO;
import ch.xwr.seicentobookit.dal.SalaryDAO;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Salary;
import ch.xwr.seicentobookit.entities.SalaryBvgBaseLine;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;

public class CalculationHelper {

	
	/**
	 * @param birthday
	 * @param date Lohnzahlung
	 * @return int Alter gemäss Jahrgang für Bvg
	 */
    public int computeAge(Date birthday, Date date) {
//        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        int d1 = Integer.parseInt(formatter.format(birthday));
//        int d2 = Integer.parseInt(formatter.format(date));
//        int age = (d2-d1)/10000;
//        return age;
		
    	//Berechnung nach Jahrgang
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		int year1 = calendar.get(Calendar.YEAR);
		
		calendar.setTime(birthday);
		int year2 = calendar.get(Calendar.YEAR);
		
		return (year1 - year2); 			        
    }
	
    
    public SalaryCalculationLine getCalculationBase(Salary dto, Employee emp) {
    	try {
        	SalaryCalculationLineDAO dao = new SalaryCalculationLineDAO();
        	List<SalaryCalculationLine> lst = dao.findByHeaderAndDate(emp.getSalaryCalculation(), dto.getSlrDate());
        	
        	if (lst.size() > 0) {
        		return lst.get(0);
        	}
    		
    	} catch (Exception e) {
    		System.out.println("Could not fetch SalaryCalculationLine");
    	}
		return null;
	}

    public SalaryBvgBaseLine getBvgBaseLine(Salary dto, Employee emp) {
    	int ageYear=computeAge(emp.getEmpBirthday(), dto.getSlrDate());
    	
    	SalaryBvgBaseLineDAO dao = new SalaryBvgBaseLineDAO();
    	List<SalaryBvgBaseLine> lst = dao.findByHeaderAndDate(emp.getSalaryBvgBase(), dto.getSlrDate(), ageYear);
    	
    	if (lst.size() > 0) {
    		return lst.get(0);
    	}
		return null;
	}
    
    public SolAlvValues getBaseSolAlvValues(Salary dto, Employee emp, SalaryCalculationLine clcDto)
    {
    	SolAlvValues values = new SolAlvValues();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dto.getSlrDate());
    	
    	SalaryDAO salDao = new SalaryDAO();
    	List<Salary> slrs = salDao.getEmployeeSalaries(emp, cal.get(Calendar.YEAR), cal.getTime());
    	    	
		double currentBrutSalary = dto.getSlrSalaryBase();  //Bruttolohn  	
        double totSalary = currentBrutSalary; //dto.getSlrsalarybase().doubleValue();
        double totSol = 0;
        
        for (Iterator<Salary> iterator = slrs.iterator(); iterator.hasNext();) {
			Salary sal = iterator.next();
			totSalary = totSalary + sal.getSlrSalaryBase();
			totSol = totSol + sal.getSlrBaseSol();
		}
        
        values.setTotalSalary(totSalary);
        values.setTotalSol(totSol);
        
        //Solidaritätbeitrag unter/ober Limit
        double solValue = 0;
        if (totSalary > clcDto.getSlxSldLowerBoundry()) {      	
        	solValue = dto.getSlrSalaryBase();  //Basis für SOL, gesamter Lohn
			if (solValue > currentBrutSalary) {
				solValue = currentBrutSalary;
			}        	
        }
        values.setSolValue(solValue);

        //Koordinationslohn ALV        
        dto.setSlrBaseAlv(dto.getSlrSalaryBase());
		double salaryBase = currentBrutSalary;
        if (totSalary > clcDto.getSlxCoordinationAlv())
        {
            if ((totSalary - salaryBase) < clcDto.getSlxCoordinationAlv())
            {
            	double value = clcDto.getSlxCoordinationAlv() - (totSalary - salaryBase);
        		values.setBaseAlv(value);
            } else {
        		values.setBaseAlv(0.);
            }
        }
        
        return values;
    }
    
    
	public double swissCommercialRound(double input)
    {
    	double ans = java.lang.Math.round((input / 0.05)) * 0.05;
    	BigDecimal value = new BigDecimal(ans).setScale(2, RoundingMode.HALF_DOWN);

        return value.doubleValue();
    }
    
    
    public double computePercentage(double input, double d1, Double m1) {
    	double output = input / d1; //divide(new BigDecimal(d1), RoundingMode.HALF_DOWN);
    	if (m1 == null) return 0.;
        return swissCommercialRound(output * m1);    	
    }
    
}
