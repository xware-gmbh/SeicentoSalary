package ch.xwr.seicentobookit.business;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ch.xwr.seicentobookit.dal.SalaryDAO;
import ch.xwr.seicentobookit.entities.Salary;

public class AdministrationManager {
	
	public void resetBookingDate(Date boDate) {
		SalaryDAO dao = new SalaryDAO();
		
		List<Salary> slrs = dao.findByBookingDate(boDate);

		for (Iterator<Salary> iterator = slrs.iterator(); iterator.hasNext();) {
			Salary sal = iterator.next();
			sal.setSlrPayDate(null);
			
			dao.save(sal);
		}
		
	}

}
