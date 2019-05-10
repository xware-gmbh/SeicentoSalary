package ch.xwr.seicentobookit.entities;

import java.util.HashSet;
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
import ch.xwr.seicentobookit.dal.BankDAO;

/**
 * Bank
 */
@DAO(daoClass = BankDAO.class)
@Caption("{%bnkName}")
@Entity
@Table(name = "Bank", schema = "dbo")
public class Bank implements java.io.Serializable {

	private long bnkId;
	private String bnkName;
	private String bnkAddress;
	private String bnkZip;
	private String bnkPlace;
	private String bnkSwift;
	private String bnkClearing;
	private LovState.State bnkState;
	private Set<Employee> employees = new HashSet<Employee>(0);

	public Bank() {
	}

	public Bank(long bnkId, String bnkName, String bnkPlace, LovState.State bnkState) {
		this.bnkId = bnkId;
		this.bnkName = bnkName;
		this.bnkPlace = bnkPlace;
		this.bnkState = bnkState;
	}

	public Bank(long bnkId, String bnkName, String bnkAddress, String bnkZip, String bnkPlace, String bnkSwift,
			String bnkClearing, LovState.State bnkState, Set<Employee> employees) {
		this.bnkId = bnkId;
		this.bnkName = bnkName;
		this.bnkAddress = bnkAddress;
		this.bnkZip = bnkZip;
		this.bnkPlace = bnkPlace;
		this.bnkSwift = bnkSwift;
		this.bnkClearing = bnkClearing;
		this.bnkState = bnkState;
		this.employees = employees;
	}

	@Caption("BnkId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "bnkId", unique = true, nullable = false)
	public long getBnkId() {
		return this.bnkId;
	}

	public void setBnkId(long bnkId) {
		this.bnkId = bnkId;
	}

	@Caption("Bank-Name")
	@Column(name = "bnkName", nullable = false, columnDefinition = "nvarchar")
	public String getBnkName() {
		return this.bnkName;
	}

	public void setBnkName(String bnkName) {
		this.bnkName = bnkName;
	}

	@Caption("Bank-Adresse")
	@Column(name = "bnkAddress", columnDefinition = "nvarchar")
	public String getBnkAddress() {
		return this.bnkAddress;
	}

	public void setBnkAddress(String bnkAddress) {
		this.bnkAddress = bnkAddress;
	}

	@Caption("Plz")
	@Column(name = "bnkZip", columnDefinition = "nvarchar")
	public String getBnkZip() {
		return this.bnkZip;
	}

	public void setBnkZip(String bnkZip) {
		this.bnkZip = bnkZip;
	}

	@Caption("Ort")
	@Column(name = "bnkPlace", nullable = false, columnDefinition = "nvarchar")
	public String getBnkPlace() {
		return this.bnkPlace;
	}

	public void setBnkPlace(String bnkPlace) {
		this.bnkPlace = bnkPlace;
	}

	@Caption("SWIFT Nummer")
	@Column(name = "bnkSwift", columnDefinition = "nvarchar")
	public String getBnkSwift() {
		return this.bnkSwift;
	}

	public void setBnkSwift(String bnkSwift) {
		this.bnkSwift = bnkSwift;
	}

	@Caption("Bankenclearing")
	@Column(name = "bnkClearing", columnDefinition = "nvarchar")
	public String getBnkClearing() {
		return this.bnkClearing;
	}

	public void setBnkClearing(String bnkClearing) {
		this.bnkClearing = bnkClearing;
	}

	@Caption("Status")
	@Column(name = "bnkState", nullable = false)
	public LovState.State getBnkState() {
		return this.bnkState;
	}

	public void setBnkState(LovState.State bnkState) {
		this.bnkState = bnkState;
	}

	@Caption("Employees")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

}
