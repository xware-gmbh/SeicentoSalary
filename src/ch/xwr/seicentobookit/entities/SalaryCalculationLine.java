package ch.xwr.seicentobookit.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.SalaryCalculationLineDAO;

/**
 * SalaryCalculation
 */
@DAO(daoClass = SalaryCalculationLineDAO.class)
@Caption("{%slxName}")
@Entity
@Table(name = "SalaryCalculationLine", schema = "dbo")
public class SalaryCalculationLine implements java.io.Serializable {

	private long slxId;
	private Date slxValidFrom;
	private double slxFactorAhv;
	private double slxFactorAlv;
	private double slxFactorKtg;
	private double slxFactorFak;
	private double slxFactorAdmin;
	private double slxFactorSol;
	private double slxCoordinationAlv;
	private double slxSldLowerBoundry;
	private double slxSldUpperBoundry;
	private LovState.State slxState;
	private SalaryCalculation salaryCalculation;
	public SalaryCalculationLine() {
	}

	public SalaryCalculationLine(long slxId, double slxFactorAhv, double slxFactorAlv, double slxFactorKtg,
			double slxFactorFak, double slxFactorAdmin, double slxFactorSol, double slxCoordinationAlv,
			double slxSldLowerBoundry, double slxSldUpperBoundry, LovState.State slxState) {
		this.slxId = slxId;
		this.slxFactorAhv = slxFactorAhv;
		this.slxFactorAlv = slxFactorAlv;
		this.slxFactorKtg = slxFactorKtg;
		this.slxFactorFak = slxFactorFak;
		this.slxFactorAdmin = slxFactorAdmin;
		this.slxFactorSol = slxFactorSol;
		this.slxCoordinationAlv = slxCoordinationAlv;
		this.slxSldLowerBoundry = slxSldLowerBoundry;
		this.slxSldUpperBoundry = slxSldUpperBoundry;
		this.slxState = slxState;
	}

	public SalaryCalculationLine(long slxId, Date slxValidFrom, String slxDescription, double slxFactorAhv,
			double slxFactorAlv, double slxFactorKtg, double slxFactorFak, double slxFactorAdmin, double slxFactorSol,
			double slxCoordinationAlv, double slxSldLowerBoundry, double slxSldUpperBoundry, LovState.State slxState,
			Set<Employee> employees) {
		this.slxId = slxId;
		this.slxValidFrom = slxValidFrom;
		this.slxFactorAhv = slxFactorAhv;
		this.slxFactorAlv = slxFactorAlv;
		this.slxFactorKtg = slxFactorKtg;
		this.slxFactorFak = slxFactorFak;
		this.slxFactorAdmin = slxFactorAdmin;
		this.slxFactorSol = slxFactorSol;
		this.slxCoordinationAlv = slxCoordinationAlv;
		this.slxSldLowerBoundry = slxSldLowerBoundry;
		this.slxSldUpperBoundry = slxSldUpperBoundry;
		this.slxState = slxState;
	}

	@Caption("slxId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "slxId", unique = true, nullable = false)
	public long getSlxId() {
		return this.slxId;
	}

	public void setSlxId(long slxId) {
		this.slxId = slxId;
	}

	@Caption("Gültigab")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "slxValidFrom", columnDefinition = "datetime")
	public Date getSlxValidFrom() {
		return this.slxValidFrom;
	}

	public void setSlxValidFrom(Date slxValidFrom) {
		this.slxValidFrom = slxValidFrom;
	}

	@Caption("slxFactorAhv")
	@Column(name = "slxFactorAhv", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorAhv() {
		return this.slxFactorAhv;
	}

	public void setSlxFactorAhv(double slxFactorAhv) {
		this.slxFactorAhv = slxFactorAhv;
	}

	@Caption("slxFactorAlv")
	@Column(name = "slxFactorAlv", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorAlv() {
		return this.slxFactorAlv;
	}

	public void setSlxFactorAlv(double slxFactorAlv) {
		this.slxFactorAlv = slxFactorAlv;
	}

	@Caption("slxFactorKtg")
	@Column(name = "slxFactorKtg", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorKtg() {
		return this.slxFactorKtg;
	}

	public void setSlxFactorKtg(double slxFactorKtg) {
		this.slxFactorKtg = slxFactorKtg;
	}

	@Caption("slxFactorFak")
	@Column(name = "slxFactorFak", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorFak() {
		return this.slxFactorFak;
	}

	public void setSlxFactorFak(double slxFactorFak) {
		this.slxFactorFak = slxFactorFak;
	}

	@Caption("slxFactorAdmin")
	@Column(name = "slxFactorAdmin", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorAdmin() {
		return this.slxFactorAdmin;
	}

	public void setSlxFactorAdmin(double slxFactorAdmin) {
		this.slxFactorAdmin = slxFactorAdmin;
	}

	@Caption("slxFactorSol")
	@Column(name = "slxFactorSol", nullable = false, precision = 8, scale = 4)
	public double getSlxFactorSol() {
		return this.slxFactorSol;
	}

	public void setSlxFactorSol(double slxFactorSol) {
		this.slxFactorSol = slxFactorSol;
	}

	@Caption("slxCoordinationAlv")
	@Column(name = "slxCoordinationAlv", nullable = false, precision = 10)
	public double getSlxCoordinationAlv() {
		return this.slxCoordinationAlv;
	}

	public void setSlxCoordinationAlv(double slxCoordinationAlv) {
		this.slxCoordinationAlv = slxCoordinationAlv;
	}

	@Caption("slxSldLowerBoundry")
	@Column(name = "slxSldLowerBoundry", nullable = false, precision = 10)
	public double getSlxSldLowerBoundry() {
		return this.slxSldLowerBoundry;
	}

	public void setSlxSldLowerBoundry(double slxSldLowerBoundry) {
		this.slxSldLowerBoundry = slxSldLowerBoundry;
	}

	@Caption("slxSldUpperBoundry")
	@Column(name = "slxSldUpperBoundry", nullable = false, precision = 10)
	public double getSlxSldUpperBoundry() {
		return this.slxSldUpperBoundry;
	}

	public void setSlxSldUpperBoundry(double slxSldUpperBoundry) {
		this.slxSldUpperBoundry = slxSldUpperBoundry;
	}

	@Caption("Status")
	@Column(name = "slxState", nullable = false)
	public LovState.State getSlxState() {
		return this.slxState;
	}

	public void setSlxState(LovState.State slxState) {
		this.slxState = slxState;
	}

	@Caption("Header")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slxsleId")
	public SalaryCalculation getSalaryCalculation() {
		return salaryCalculation;
	}

	public void setSalaryCalculation(SalaryCalculation salaryCalculation) {
		this.salaryCalculation = salaryCalculation;
	}

}
