package ch.xwr.seicentobookit.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.EmployeeDAO;

/**
 * Employee
 */
@DAO(daoClass = EmployeeDAO.class)
@Caption("{%empFirstName}")
@Entity
@Table(name = "Employee", schema = "dbo")
public class Employee implements java.io.Serializable {

	private long empId;
	private Bank bank;
	private SalaryBvgBase salaryBvgBase;
	private SalaryCalculation salaryCalculation;
	private WorkRole workRole;
	private long empNumber;
	private String empFirstName;
	private String empLastName;
	private String empAddress;
	private String empZip;
	private String empPlace;
	private String empAhvnbr;
	private Date empStartwork;
	private Date empEndWork;
	private String empCostAccount;
	private Date empBirthday;
	private CivilState empCivilState;
	private Short empNbrOfKids;
	private double empKidsAddition;
	private Boolean empSourceTax;
	private Double empBaseSalary;
	private String empBankAccount;
	private String empBankIban;
	private String empAhvnbrold;
	private Double empAmtFrmRep;
	private Double empAmtFrmCar;
	private Double empAmtFrmJourney;
	private String empFrmRemark;
	private LovState.State empState;
	private Set<Salary> salaries = new HashSet<Salary>(0);
	@Transient
	private String caption;
	@Transient
	private String age;
	
	public enum CivilState {
		Ledig, Verheiratet, Geschieden, Sonstiges
	}
	

	public Employee() {
	}

	public Employee(long empId, Bank bank, SalaryBvgBase salaryBvgBase, SalaryCalculation salaryCalculation,
			WorkRole workRole, long empNumber, String empLastName, Date empStartwork, String empCostAccount,
			Date empBirthday, double empKidsAddition, LovState.State empState) {
		this.empId = empId;
		this.bank = bank;
		this.salaryBvgBase = salaryBvgBase;
		this.salaryCalculation = salaryCalculation;
		this.workRole = workRole;
		this.empNumber = empNumber;
		this.empLastName = empLastName;
		this.empStartwork = empStartwork;
		this.empCostAccount = empCostAccount;
		this.empBirthday = empBirthday;
		this.empKidsAddition = empKidsAddition;
		this.empState = empState;
	}

	public Employee(long empId, Bank bank, SalaryBvgBase salaryBvgBase, SalaryCalculation salaryCalculation,
			WorkRole workRole, long empNumber, String empFirstName, String empLastName, String empAddress,
			String empZip, String empPlace, String empAhvnbr, Date empStartwork, Date empEndWork, String empCostAccount,
			Date empBirthday, CivilState empCivilState, Short empNbrOfKids, double empKidsAddition, Boolean empSourceTax,
			Double empBaseSalary, String empBankAccount, String empBankIban, String empAhvnbrold, Double empAmtFrmRep,
			Double empAmtFrmCar, Double empAmtFrmJourney, String empFrmRemark, LovState.State empState, Set<Salary> salaries) {
		this.empId = empId;
		this.bank = bank;
		this.salaryBvgBase = salaryBvgBase;
		this.salaryCalculation = salaryCalculation;
		this.workRole = workRole;
		this.empNumber = empNumber;
		this.empFirstName = empFirstName;
		this.empLastName = empLastName;
		this.empAddress = empAddress;
		this.empZip = empZip;
		this.empPlace = empPlace;
		this.empAhvnbr = empAhvnbr;
		this.empStartwork = empStartwork;
		this.empEndWork = empEndWork;
		this.empCostAccount = empCostAccount;
		this.empBirthday = empBirthday;
		this.empCivilState = empCivilState;
		this.empNbrOfKids = empNbrOfKids;
		this.empKidsAddition = empKidsAddition;
		this.empSourceTax = empSourceTax;
		this.empBaseSalary = empBaseSalary;
		this.empBankAccount = empBankAccount;
		this.empBankIban = empBankIban;
		this.empAhvnbrold = empAhvnbrold;
		this.empAmtFrmRep = empAmtFrmRep;
		this.empAmtFrmCar = empAmtFrmCar;
		this.empAmtFrmJourney = empAmtFrmJourney;
		this.empFrmRemark = empFrmRemark;
		this.empState = empState;
		this.salaries = salaries;
	}

	@Caption("EmpId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "empId", unique = true, nullable = false)
	public long getEmpId() {
		return this.empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	@Caption("Bank")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empbnkId", nullable = false)
	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	@Caption("SalaryBvgBase")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empsbeId", nullable = false)
	public SalaryBvgBase getSalaryBvgBase() {
		return this.salaryBvgBase;
	}

	public void setSalaryBvgBase(SalaryBvgBase salaryBvgBase) {
		this.salaryBvgBase = salaryBvgBase;
	}

	@Caption("SalaryCalculation")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empsleId", nullable = false)
	public SalaryCalculation getSalaryCalculation() {
		return this.salaryCalculation;
	}

	public void setSalaryCalculation(SalaryCalculation salaryCalculation) {
		this.salaryCalculation = salaryCalculation;
	}

	@Caption("Funktion MA")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empwrrId", nullable = false)
	public WorkRole getWorkRole() {
		return this.workRole;
	}

	public void setWorkRole(WorkRole workRole) {
		this.workRole = workRole;
	}

	@Caption("Nummer MA")
	@Column(name = "empNumber", nullable = false)
	public long getEmpNumber() {
		return this.empNumber;
	}

	public void setEmpNumber(long empNumber) {
		this.empNumber = empNumber;
	}

	@Caption("Vorname")
	@Column(name = "empFirstName", columnDefinition = "nvarchar")
	public String getEmpFirstName() {
		return this.empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	@Caption("Name MA")
	@Column(name = "empLastName", nullable = false, columnDefinition = "nvarchar")
	public String getEmpLastName() {
		return this.empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	@Caption("Adresse")
	@Column(name = "empAddress", columnDefinition = "nvarchar")
	public String getEmpAddress() {
		return this.empAddress;
	}

	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}

	@Caption("Plz")
	@Column(name = "empZip", columnDefinition = "nvarchar")
	public String getEmpZip() {
		return this.empZip;
	}

	public void setEmpZip(String empZip) {
		this.empZip = empZip;
	}

	@Caption("Ort")
	@Column(name = "empPlace", columnDefinition = "nvarchar")
	public String getEmpPlace() {
		return this.empPlace;
	}

	public void setEmpPlace(String empPlace) {
		this.empPlace = empPlace;
	}

	@Caption("Ahvnummer")
	@Column(name = "empAhvnbr", columnDefinition = "nvarchar")
	public String getEmpAhvnbr() {
		return this.empAhvnbr;
	}

	public void setEmpAhvnbr(String empAhvnbr) {
		this.empAhvnbr = empAhvnbr;
	}

	@Caption("EmpStartwork")
	@Temporal(TemporalType.DATE)
	@Column(name = "empStartwork", nullable = false, length = 10)
	public Date getEmpStartwork() {
		return this.empStartwork;
	}

	public void setEmpStartwork(Date empStartwork) {
		this.empStartwork = empStartwork;
	}

	@Caption("EmpEndWork")
	@Temporal(TemporalType.DATE)
	@Column(name = "empEndWork", length = 10)
	public Date getEmpEndWork() {
		return this.empEndWork;
	}

	public void setEmpEndWork(Date empEndWork) {
		this.empEndWork = empEndWork;
	}

	@Caption("Kostenstelle")
	@Column(name = "empCostAccount", nullable = false, columnDefinition = "nvarchar")
	public String getEmpCostAccount() {
		return this.empCostAccount;
	}

	public void setEmpCostAccount(String empCostAccount) {
		this.empCostAccount = empCostAccount;
	}

	@Caption("EmpBirthday")
	@Temporal(TemporalType.DATE)
	@Column(name = "empBirthday", nullable = false, length = 10)
	public Date getEmpBirthday() {
		return this.empBirthday;
	}

	public void setEmpBirthday(Date empBirthday) {
		this.empBirthday = empBirthday;
	}

	@Caption("Zivilstand")
	@Column(name = "empCivilState")
	public CivilState getEmpCivilState() {
		return this.empCivilState;
	}

	public void setEmpCivilState(CivilState empCivilState) {
		this.empCivilState = empCivilState;
	}

	@Caption("EmpNbrOfKids")
	@Column(name = "empNbrOfKids")
	public Short getEmpNbrOfKids() {
		return this.empNbrOfKids;
	}

	public void setEmpNbrOfKids(Short empNbrOfKids) {
		this.empNbrOfKids = empNbrOfKids;
	}

	@Caption("EmpKidsAddition")
	@Column(name = "empKidsAddition", nullable = false, precision = 8)
	public double getEmpKidsAddition() {
		return this.empKidsAddition;
	}

	public void setEmpKidsAddition(double empKidsAddition) {
		this.empKidsAddition = empKidsAddition;
	}

	@Caption("EmpSourceTax")
	@Column(name = "empSourceTax")
	public Boolean getEmpSourceTax() {
		return this.empSourceTax;
	}

	public void setEmpSourceTax(Boolean empSourceTax) {
		this.empSourceTax = empSourceTax;
	}

	@Caption("EmpBaseSalary")
	@Column(name = "empBaseSalary", precision = 8)
	public Double getEmpBaseSalary() {
		return this.empBaseSalary;
	}

	public void setEmpBaseSalary(Double empBaseSalary) {
		this.empBaseSalary = empBaseSalary;
	}

	@Caption("EmpBankAccount")
	@Column(name = "empBankAccount", columnDefinition = "nvarchar")
	public String getEmpBankAccount() {
		return this.empBankAccount;
	}

	public void setEmpBankAccount(String empBankAccount) {
		this.empBankAccount = empBankAccount;
	}

	@Caption("EmpBankIban")
	@Column(name = "empBankIban", columnDefinition = "nvarchar")
	public String getEmpBankIban() {
		return this.empBankIban;
	}

	public void setEmpBankIban(String empBankIban) {
		this.empBankIban = empBankIban;
	}

	@Caption("EmpAhvnbrold")
	@Column(name = "empAhvnbrold", columnDefinition = "nvarchar")
	public String getEmpAhvnbrold() {
		return this.empAhvnbrold;
	}

	public void setEmpAhvnbrold(String empAhvnbrold) {
		this.empAhvnbrold = empAhvnbrold;
	}

	@Caption("EmpAmtFrmRep")
	@Column(name = "empAmtFrmRep", precision = 8)
	public Double getEmpAmtFrmRep() {
		return this.empAmtFrmRep;
	}

	public void setEmpAmtFrmRep(Double empAmtFrmRep) {
		this.empAmtFrmRep = empAmtFrmRep;
	}

	@Caption("EmpAmtFrmCar")
	@Column(name = "empAmtFrmCar", precision = 8)
	public Double getEmpAmtFrmCar() {
		return this.empAmtFrmCar;
	}

	public void setEmpAmtFrmCar(Double empAmtFrmCar) {
		this.empAmtFrmCar = empAmtFrmCar;
	}

	@Caption("EmpAmtFrmJourney")
	@Column(name = "empAmtFrmJourney", precision = 8)
	public Double getEmpAmtFrmJourney() {
		return this.empAmtFrmJourney;
	}

	public void setEmpAmtFrmJourney(Double empAmtFrmJourney) {
		this.empAmtFrmJourney = empAmtFrmJourney;
	}

	@Caption("EmpFrmRemark")
	@Column(name = "empFrmRemark", columnDefinition = "nvarchar")
	public String getEmpFrmRemark() {
		return this.empFrmRemark;
	}

	public void setEmpFrmRemark(String empFrmRemark) {
		this.empFrmRemark = empFrmRemark;
	}

	@Caption("Status")
	@Column(name = "empState", nullable = false)
	public LovState.State getEmpState() {
		return this.empState;
	}

	public void setEmpState(LovState.State empState) {
		this.empState = empState;
	}

	@Caption("Salaries")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	public Set<Salary> getSalaries() {
		return this.salaries;
	}

	public void setSalaries(Set<Salary> salaries) {
		this.salaries = salaries;
	}

	@Transient
	public String getCaption() {
		this.caption = getEmpLastName() + " " + getEmpFirstName(); 
		return caption;
	}

	
	@Column(name = "AGE", insertable = false, updatable = false)
	@Transient
	public String getAge() {
		String age = "";
		
		if (this.empBirthday != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			int year1 = calendar.get(Calendar.YEAR);
			
			calendar.setTime(this.empBirthday);
			int year2 = calendar.get(Calendar.YEAR);
			
			age = "" + (year1 - year2); 			
		}
		return age;
	}

	public void setAge(final String age) {
		this.age = age;
	}
	
}
