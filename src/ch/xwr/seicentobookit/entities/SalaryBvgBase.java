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
import ch.xwr.seicentobookit.dal.SalaryBvgBaseDAO;

/**
 * SalaryBvgBase
 */
@DAO(daoClass = SalaryBvgBaseDAO.class)
@Caption("{%sbeName}")
@Entity
@Table(name = "SalaryBvgBase", schema = "dbo")
public class SalaryBvgBase implements java.io.Serializable {

	private long sbeId;
	private String sbeName;
	private double sbeCoordinationAmt;
	private double sbeSalarydefAmt;
	private LovState.State sbeState;
	private Set<Employee> employees = new HashSet<Employee>(0);
	private List<SalaryBvgBaseLine> salaryBvgBaseLines = new ArrayList<>();

	public SalaryBvgBase() {
	}

	public SalaryBvgBase(long sbeId, String sbeName, double sbeCoordinationAmt, double sbeSalarydefAmt,
			 LovState.State sbeState) {
		this.sbeId = sbeId;
		this.sbeName = sbeName;
		this.sbeCoordinationAmt = sbeCoordinationAmt;
		this.sbeSalarydefAmt = sbeSalarydefAmt;
		this.sbeState = sbeState;
	}

	public SalaryBvgBase(long sbeId, String sbeName, double sbeCoordinationAmt, double sbeSalarydefAmt,
		 LovState.State sbeState, Set<Employee> employees) {
		this.sbeId = sbeId;
		this.sbeName = sbeName;
		this.sbeCoordinationAmt = sbeCoordinationAmt;
		this.sbeSalarydefAmt = sbeSalarydefAmt;
		this.sbeState = sbeState;
		this.employees = employees;
	}

	@Caption("SbeId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "sbeId", unique = true, nullable = false)
	public long getSbeId() {
		return this.sbeId;
	}

	public void setSbeId(long sbeId) {
		this.sbeId = sbeId;
	}

	@Caption("Name BVG")
	@Column(name = "sbeName", nullable = false, columnDefinition = "nvarchar")
	public String getSbeName() {
		return this.sbeName;
	}

	public void setSbeName(String sbeName) {
		this.sbeName = sbeName;
	}

	@Caption("SbeCoordinationAmt")
	@Column(name = "sbeCoordinationAmt", nullable = false, precision = 10)
	public double getSbeCoordinationAmt() {
		return this.sbeCoordinationAmt;
	}

	public void setSbeCoordinationAmt(double sbeCoordinationAmt) {
		this.sbeCoordinationAmt = sbeCoordinationAmt;
	}

	@Caption("SbeSalarydefAmt")
	@Column(name = "sbeSalarydefAmt", nullable = false, precision = 10)
	public double getSbeSalarydefAmt() {
		return this.sbeSalarydefAmt;
	}

	public void setSbeSalarydefAmt(double sbeSalarydefAmt) {
		this.sbeSalarydefAmt = sbeSalarydefAmt;
	}

	@Caption("Status")
	@Column(name = "sbeState", nullable = false)
	public LovState.State getSbeState() {
		return this.sbeState;
	}

	public void setSbeState(LovState.State sbeState) {
		this.sbeState = sbeState;
	}

	@Caption("Employees")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salaryBvgBase")
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Caption("Zeilen")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salaryBvgBasis")
	public List<SalaryBvgBaseLine> getSalaryBvgBaseLines() {
		return salaryBvgBaseLines;
	}

	public void setSalaryBvgBaseLines(List<SalaryBvgBaseLine> salaryBvgBaseLines) {
		this.salaryBvgBaseLines = salaryBvgBaseLines;
	}

	public SalaryBvgBaseLine addSalaryBvgBaseLine(SalaryBvgBaseLine salaryBvgBaseLine) {
		getSalaryBvgBaseLines().add(salaryBvgBaseLine);
		salaryBvgBaseLine.setSalaryBvgBasis(this);
		return salaryBvgBaseLine;
	}

	public SalaryBvgBaseLine removeSalaryBvgBaseLine(SalaryBvgBaseLine salaryBvgBaseLine) {
		getSalaryBvgBaseLines().remove(salaryBvgBaseLine);
		salaryBvgBaseLine.setSalaryBvgBasis(null);
		return salaryBvgBaseLine;
	}

}
