package ch.xwr.seicentobookit.entities;

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
import ch.xwr.seicentobookit.dal.SalaryBvgBaseLineDAO;

/**
 * SalaryBvgBase
 */
@DAO(daoClass = SalaryBvgBaseLineDAO.class)
@Caption("{%sbxName}")
@Entity
@Table(name = "SalaryBvgBaseLine", schema = "dbo")
public class SalaryBvgBaseLine implements java.io.Serializable {

	private long sbxId;
	private Date sbxValidFrom;
	private int sbxAgeStartYear;
	private double sbxCompany;
	private double sbxWorker;
	private LovState.State sbxState;
	private SalaryBvgBase salaryBvgBasis;
	
	
	public SalaryBvgBaseLine() {
	}

	public SalaryBvgBaseLine(long sbxId, int sbxAgeStartYear, double sbxCoordinationAmt,
			double sbxCompany, double sbxWorker, LovState.State sbxState) {
		this.sbxId = sbxId;
		this.sbxValidFrom = sbxValidFrom;
		this.sbxAgeStartYear = sbxAgeStartYear;
		this.sbxCompany = sbxCompany;
		this.sbxWorker = sbxWorker;
		this.sbxState = sbxState;
	}

	@Caption("Id")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "sbxId", unique = true, nullable = false)
	public long getSbxId() {
		return this.sbxId;
	}

	public void setSbxId(long sbxId) {
		this.sbxId = sbxId;
	}

	@Caption("Gültig ab")
	@Temporal(TemporalType.TIMESTAMP)	
	@Column(name = "sbxValidFrom", nullable = false, columnDefinition = "datetime")
	public Date getSbxValidFrom() {
		return this.sbxValidFrom;
	}

	public void setSbxValidFrom(Date sbxName) {
		this.sbxValidFrom = sbxName;
	}

	@Caption("Alter ab")
	@Column(name = "sbxAgeStartYear", nullable = false, precision = 10)
	public int getSbxAgeStartYear() {
		return this.sbxAgeStartYear;
	}

	public void setSbxAgeStartYear(int sbxCoordinationAmt) {
		this.sbxAgeStartYear = sbxCoordinationAmt;
	}

	@Caption("Arbeitgeber")
	@Column(name = "sbxCompany", nullable = false, precision = 6, scale = 4)
	public double getSbxCompany() {
		return this.sbxCompany;
	}

	public void setSbxCompany(double sbxCompany18) {
		this.sbxCompany = sbxCompany18;
	}

	@Caption("Arbeitnehmer")
	@Column(name = "sbxWorker", nullable = false, precision = 6, scale = 4)
	public double getSbxWorker() {
		return this.sbxWorker;
	}

	public void setSbxWorker(double sbxWorker18) {
		this.sbxWorker = sbxWorker18;
	}

	@Caption("Status")
	@Column(name = "sbxState", nullable = false)
	public LovState.State getSbxState() {
		return this.sbxState;
	}

	public void setSbxState(LovState.State sbxState) {
		this.sbxState = sbxState;
	}

	@Caption("Kopf")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sbxsbeId")
	public SalaryBvgBase getSalaryBvgBasis() {
		return salaryBvgBasis;
	}

	public void setSalaryBvgBasis(SalaryBvgBase salaryBvgBasis) {
		this.salaryBvgBasis = salaryBvgBasis;
	}

}
