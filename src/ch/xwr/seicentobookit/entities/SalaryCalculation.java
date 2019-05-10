package ch.xwr.seicentobookit.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.SalaryCalculationDAO;

/**
 * SalaryCalculation
 */
@DAO(daoClass = SalaryCalculationDAO.class)
@Caption("{%sleName}")
@Entity
@Table(name = "SalaryCalculation", schema = "dbo")
public class SalaryCalculation implements java.io.Serializable {

	private long sleId;
	private String sleName;
	private String sleDescription;
	private LovState.State sleState;
	private Set<Employee> employees = new HashSet<Employee>(0);
	private List<SalaryCalculationLine> salaryCalculationLines = new ArrayList<>();

	public SalaryCalculation() {
	}

	public SalaryCalculation(long sleId,  LovState.State sleState) {
		this.sleId = sleId;
		this.sleState = sleState;
	}

	public SalaryCalculation(long sleId, String sleName, String sleDescription,  LovState.State sleState,
			Set<Employee> employees) {
		this.sleId = sleId;
		this.sleName = sleName;
		this.sleDescription = sleDescription;
		this.sleState = sleState;
		this.employees = employees;
	}

	@Caption("SleId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "sleId", unique = true, nullable = false)
	public long getSleId() {
		return this.sleId;
	}

	public void setSleId(long sleId) {
		this.sleId = sleId;
	}

	@Caption("Name Nebenkosten")
	@Column(name = "sleName", columnDefinition = "nvarchar")
	public String getSleName() {
		return this.sleName;
	}

	public void setSleName(String sleName) {
		this.sleName = sleName;
	}

	@Caption("SleDescription")
	@Column(name = "sleDescription", columnDefinition = "nvarchar")
	public String getSleDescription() {
		return this.sleDescription;
	}

	public void setSleDescription(String sleDescription) {
		this.sleDescription = sleDescription;
	}

	@Caption("Status")
	@Column(name = "sleState", nullable = false)
	public LovState.State getSleState() {
		return this.sleState;
	}

	public void setSleState(LovState.State sleState) {
		this.sleState = sleState;
	}

	@Caption("Employees")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salaryCalculation")
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Caption("Zeile")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salaryCalculation")
	public List<SalaryCalculationLine> getSalaryCalculationLines() {
		return salaryCalculationLines;
	}

	public void setSalaryCalculationLines(List<SalaryCalculationLine> salaryCalculationLines) {
		this.salaryCalculationLines = salaryCalculationLines;
	}

	public SalaryCalculationLine addSalaryCalculationLine(SalaryCalculationLine salaryCalculationLine) {
		getSalaryCalculationLines().add(salaryCalculationLine);
		salaryCalculationLine.setSalaryCalculation(this);
		return salaryCalculationLine;
	}

	public SalaryCalculationLine removeSalaryCalculationLine(SalaryCalculationLine salaryCalculationLine) {
		getSalaryCalculationLines().remove(salaryCalculationLine);
		salaryCalculationLine.setSalaryCalculation(null);
		return salaryCalculationLine;
	}

}
