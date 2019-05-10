package ch.xwr.seicentobookit.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ch.xwr.seicentobookit.dal.CompanyDAO;
import ch.xwr.seicentobookit.entities.Company;

public class JasperManager {
	private List<String> keys = new ArrayList<String>();
	private List<String> values = new ArrayList<String>();

	public static String SalaryReport1 = "Salary_Slip";
	public static String SalaryReport2 = "Salary_TaxForm";
	public static String SalaryReport3 = "Salary_Summary";
	public static String SalaryReport4 = "Salary_Overview";

	public void addParameter(String name, String value) {
		keys.add(name);
		values.add(value);		
	}
	
	public String getUri(String report) {
		CompanyDAO dao = new CompanyDAO();
		Company cmp = dao.getActiveConfig();
		
		String uri = cmp.getCmpJasperUri().trim();  
		uri = MessageFormat.format(uri, report);
		
		if (keys.size() > 0) {
			for (int i = 0; i < keys.size(); i++) {
				String s1 = keys.get(i);
				String s2 = values.get(i);
				
				uri = uri + "&" + s1 + "=" +s2;				
			}
		}
		
        uri = uri + "&j_username=" + cmp.getCmpReportUsr() + "&j_password=" + cmp.getCmpReportPwd();		
		return uri;
	}
}
