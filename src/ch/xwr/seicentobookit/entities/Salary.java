package ch.xwr.seicentobookit.entities;

import java.util.Calendar;
import java.util.Date;

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
import ch.xwr.seicentobookit.dal.SalaryDAO;

/**
 * Salary
 */
@DAO(daoClass = SalaryDAO.class)
@Caption("{%slrRemark}")
@Entity
@Table(name = "Salary", schema = "dbo")
public class Salary implements java.io.Serializable {

	private long slrId;
	private Employee employee;
	private Integer slrYear;
	private Date slrDate;
	private Date slrPayDate;
	private String slrRemark;
	private double slrSalaryBase;
	private Double slrBaseAlv;
	private Double slrBaseBvg;
	private Double slrBaseSol;
	private double slrKidsAdditon;
	private double slrSalaryNet;
	private double slrAmountAhv;
	private double slrAmountAlv;
	private double slrAmountBvg;
	private double slrAmountFak;
	private double slrAmountSol;
	private double slrAmountAdminfees;
	private Double slrAmountSourceTax;
	private Double slrBirthAddon;
	private Double slrFactorAhv;
	private Double slrFactorAlv;
	private Double slrFactorFak;
	private Double slrFactorSol;
	private Double slrFactorAdmin;
	private Double slrAmountBvgEmp;
	private SalaryType slrType;
	private LovState.State slrState;

	public enum SalaryType {
		Normal, Bonus
	}
	
	public Salary() {
	}

	public Salary(long slrId, Employee employee, double slrSalaryBase, double slrKidsAdditon, double slrSalaryNet,
			double slrAmountAhv, double slrAmountAlv, double slrAmountBvg, double slrAmountFak, double slrAmountSol,
			double slrAmountAdminfees, SalaryType slrType, LovState.State slrState) {
		this.slrId = slrId;
		this.employee = employee;
		this.slrSalaryBase = slrSalaryBase;
		this.slrKidsAdditon = slrKidsAdditon;
		this.slrSalaryNet = slrSalaryNet;
		this.slrAmountAhv = slrAmountAhv;
		this.slrAmountAlv = slrAmountAlv;
		this.slrAmountBvg = slrAmountBvg;
		this.slrAmountFak = slrAmountFak;
		this.slrAmountSol = slrAmountSol;
		this.slrAmountAdminfees = slrAmountAdminfees;
		this.slrType = slrType;
		this.slrState = slrState;
	}

	public Salary(long slrId, Employee employee, Integer slrYear, Date slrDate, Date slrPayDate, String slrRemark,
			double slrSalaryBase, Double slrBaseAlv, Double slrBaseBvg, Double slrBaseSol, double slrKidsAdditon,
			double slrSalaryNet, double slrAmountAhv, double slrAmountAlv, double slrAmountBvg, double slrAmountFak,
			double slrAmountSol, double slrAmountAdminfees, Double slrAmountSourceTax, Double slrBirthAddon,
			Double slrFactorAhv, Double slrFactorAlv, Double slrFactorFak, Double slrFactorSol, Double slrFactorAdmin,
			Double slrAmountBvgEmp, SalaryType slrType, LovState.State slrState) {
		this.slrId = slrId;
		this.employee = employee;
		this.slrYear = slrYear;
		this.slrDate = slrDate;
		this.slrPayDate = slrPayDate;
		this.slrRemark = slrRemark;
		this.slrSalaryBase = slrSalaryBase;
		this.slrBaseAlv = slrBaseAlv;
		this.slrBaseBvg = slrBaseBvg;
		this.slrBaseSol = slrBaseSol;
		this.slrKidsAdditon = slrKidsAdditon;
		this.slrSalaryNet = slrSalaryNet;
		this.slrAmountAhv = slrAmountAhv;
		this.slrAmountAlv = slrAmountAlv;
		this.slrAmountBvg = slrAmountBvg;
		this.slrAmountFak = slrAmountFak;
		this.slrAmountSol = slrAmountSol;
		this.slrAmountAdminfees = slrAmountAdminfees;
		this.slrAmountSourceTax = slrAmountSourceTax;
		this.slrBirthAddon = slrBirthAddon;
		this.slrFactorAhv = slrFactorAhv;
		this.slrFactorAlv = slrFactorAlv;
		this.slrFactorFak = slrFactorFak;
		this.slrFactorSol = slrFactorSol;
		this.slrFactorAdmin = slrFactorAdmin;
		this.slrAmountBvgEmp = slrAmountBvgEmp;
		this.slrType = slrType;
		this.slrState = slrState;
	}

	@Caption("SlrId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "slrId", unique = true, nullable = false)
	public long getSlrId() {
		return this.slrId;
	}

	public void setSlrId(long slrId) {
		this.slrId = slrId;
	}

	@Caption("Mitarbeiter")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "slrempId", nullable = false)
	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Caption("Jahr")
	@Column(name = "slrYear")
	public Integer getSlrYear() {
		return this.slrYear;
	}

	public void setSlrYear(Integer slrYear) {
		this.slrYear = slrYear;
	}

	@Caption("Lohndatum")
	@Temporal(TemporalType.DATE)
	@Column(name = "slrDate", length = 10)
	public Date getSlrDate() {
		return this.slrDate;
	}

	public void setSlrDate(Date slrDate) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(slrDate);
    	this.setSlrYear(cal.get(Calendar.YEAR));
    			
		this.slrDate = slrDate;
	}

	@Caption("Zahlungsdatum")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "slrPayDate", length = 23)
	public Date getSlrPayDate() {
		return this.slrPayDate;
	}

	public void setSlrPayDate(Date slrPayDate) {
		this.slrPayDate = slrPayDate;
	}

	@Caption("Bemerkung")
	@Column(name = "slrRemark", columnDefinition = "nvarchar")
	public String getSlrRemark() {
		return this.slrRemark;
	}

	public void setSlrRemark(String slrRemark) {
		this.slrRemark = slrRemark;
	}

	@Caption("Grundlohn")
	@Column(name = "slrSalaryBase", nullable = false, precision = 8)
	public double getSlrSalaryBase() {
		return this.slrSalaryBase;
	}

	public void setSlrSalaryBase(double slrSalaryBase) {
		this.slrSalaryBase = slrSalaryBase;
	}

	@Caption("Basis ALV")
	@Column(name = "slrBaseAlv", precision = 8)
	public Double getSlrBaseAlv() {
		return this.slrBaseAlv;
	}

	public void setSlrBaseAlv(Double slrBaseAlv) {
		this.slrBaseAlv = slrBaseAlv;
	}

	@Caption("Basis BVG")
	@Column(name = "slrBaseBvg", precision = 8)
	public Double getSlrBaseBvg() {
		return this.slrBaseBvg;
	}

	public void setSlrBaseBvg(Double slrBaseBvg) {
		this.slrBaseBvg = slrBaseBvg;
	}

	@Caption("SlrBaseSol")
	@Column(name = "slrBaseSol", precision = 8)
	public Double getSlrBaseSol() {
		return this.slrBaseSol;
	}

	public void setSlrBaseSol(Double slrBaseSol) {
		this.slrBaseSol = slrBaseSol;
	}

	@Caption("SlrKidsAdditon")
	@Column(name = "slrKidsAdditon", nullable = false, precision = 8)
	public double getSlrKidsAdditon() {
		return this.slrKidsAdditon;
	}

	public void setSlrKidsAdditon(double slrKidsAdditon) {
		this.slrKidsAdditon = slrKidsAdditon;
	}

	@Caption("Nettolohn")
	@Column(name = "slrSalaryNet", nullable = false, precision = 8)
	public double getSlrSalaryNet() {
		return this.slrSalaryNet;
	}

	public void setSlrSalaryNet(double slrSalaryNet) {
		this.slrSalaryNet = slrSalaryNet;
	}

	@Caption("Betrag AHV")
	@Column(name = "slrAmountAhv", nullable = false, precision = 8)
	public double getSlrAmountAhv() {
		return this.slrAmountAhv;
	}

	public void setSlrAmountAhv(double slrAmountAhv) {
		this.slrAmountAhv = slrAmountAhv;
	}

	@Caption("SlrAmountAlv")
	@Column(name = "slrAmountAlv", nullable = false, precision = 8)
	public double getSlrAmountAlv() {
		return this.slrAmountAlv;
	}

	public void setSlrAmountAlv(double slrAmountAlv) {
		this.slrAmountAlv = slrAmountAlv;
	}

	@Caption("SlrAmountBvg")
	@Column(name = "slrAmountBvg", nullable = false, precision = 8)
	public double getSlrAmountBvg() {
		return this.slrAmountBvg;
	}

	public void setSlrAmountBvg(double slrAmountBvg) {
		this.slrAmountBvg = slrAmountBvg;
	}

	@Caption("SlrAmountFak")
	@Column(name = "slrAmountFak", nullable = false, precision = 8)
	public double getSlrAmountFak() {
		return this.slrAmountFak;
	}

	public void setSlrAmountFak(double slrAmountFak) {
		this.slrAmountFak = slrAmountFak;
	}

	@Caption("SlrAmountSol")
	@Column(name = "slrAmountSol", nullable = false, precision = 8)
	public double getSlrAmountSol() {
		return this.slrAmountSol;
	}

	public void setSlrAmountSol(double slrAmountSol) {
		this.slrAmountSol = slrAmountSol;
	}

	@Caption("SlrAmountAdminfees")
	@Column(name = "slrAmountAdminfees", nullable = false, precision = 8)
	public double getSlrAmountAdminfees() {
		return this.slrAmountAdminfees;
	}

	public void setSlrAmountAdminfees(double slrAmountAdminfees) {
		this.slrAmountAdminfees = slrAmountAdminfees;
	}

	@Caption("SlrAmountSourceTax")
	@Column(name = "slrAmountSourceTax", precision = 8)
	public Double getSlrAmountSourceTax() {
		return this.slrAmountSourceTax;
	}

	public void setSlrAmountSourceTax(Double slrAmountSourceTax) {
		this.slrAmountSourceTax = slrAmountSourceTax;
	}

	@Caption("SlrBirthAddon")
	@Column(name = "slrBirthAddon", precision = 8)
	public Double getSlrBirthAddon() {
		return this.slrBirthAddon;
	}

	public void setSlrBirthAddon(Double slrBirthAddon) {
		this.slrBirthAddon = slrBirthAddon;
	}

	@Caption("SlrFactorAhv")
	@Column(name = "slrFactorAhv", precision = 8, scale = 4)
	public Double getSlrFactorAhv() {
		return this.slrFactorAhv;
	}

	public void setSlrFactorAhv(Double slrFactorAhv) {
		this.slrFactorAhv = slrFactorAhv;
	}

	@Caption("SlrFactorAlv")
	@Column(name = "slrFactorAlv", precision = 8, scale = 4)
	public Double getSlrFactorAlv() {
		return this.slrFactorAlv;
	}

	public void setSlrFactorAlv(Double slrFactorAlv) {
		this.slrFactorAlv = slrFactorAlv;
	}

	@Caption("SlrFactorFak")
	@Column(name = "slrFactorFak", precision = 8, scale = 4)
	public Double getSlrFactorFak() {
		return this.slrFactorFak;
	}

	public void setSlrFactorFak(Double slrFactorFak) {
		this.slrFactorFak = slrFactorFak;
	}

	@Caption("SlrFactorSol")
	@Column(name = "slrFactorSol", precision = 8, scale = 4)
	public Double getSlrFactorSol() {
		return this.slrFactorSol;
	}

	public void setSlrFactorSol(Double slrFactorSol) {
		this.slrFactorSol = slrFactorSol;
	}

	@Caption("SlrFactorAdmin")
	@Column(name = "slrFactorAdmin", precision = 8, scale = 4)
	public Double getSlrFactorAdmin() {
		return this.slrFactorAdmin;
	}

	public void setSlrFactorAdmin(Double slrFactorAdmin) {
		this.slrFactorAdmin = slrFactorAdmin;
	}

	@Caption("SlrAmountBvgEmp")
	@Column(name = "slrAmountBvgEmp", precision = 8)
	public Double getSlrAmountBvgEmp() {
		return this.slrAmountBvgEmp;
	}

	public void setSlrAmountBvgEmp(Double slrAmountBvgEmp) {
		this.slrAmountBvgEmp = slrAmountBvgEmp;
	}

	@Caption("Lohnart")
	@Column(name = "slrType", nullable = false)
	public SalaryType getSlrType() {
		return this.slrType;
	}

	public void setSlrType(SalaryType slrType) {
		this.slrType = slrType;
	}

	@Caption("Status")
	@Column(name = "slrState", nullable = false)
	public LovState.State getSlrState() {
		return this.slrState;
	}

	public void setSlrState(LovState.State slrState) {
		this.slrState = slrState;
	}

}
