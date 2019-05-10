package ch.xwr.seicentobookit.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.WorkRoleDAO;

/**
 * WorkRole
 */
@DAO(daoClass = WorkRoleDAO.class)
@Caption("{%wrrName}")
@Entity
@Table(name = "WorkRole", schema = "dbo")
public class WorkRole implements java.io.Serializable {

	private long wrrId;
	private String wrrName;
	private short wrrState;
	private Set<Employee> employees = new HashSet<Employee>(0);

	public WorkRole() {
	}

	public WorkRole(long wrrId, String wrrName, short wrrState) {
		this.wrrId = wrrId;
		this.wrrName = wrrName;
		this.wrrState = wrrState;
	}

	public WorkRole(long wrrId, String wrrName, short wrrState, Set<Employee> employees) {
		this.wrrId = wrrId;
		this.wrrName = wrrName;
		this.wrrState = wrrState;
		this.employees = employees;
	}

	@Caption("WrrId")
	@Id

	@Column(name = "wrrId", unique = true, nullable = false)
	public long getWrrId() {
		return this.wrrId;
	}

	public void setWrrId(long wrrId) {
		this.wrrId = wrrId;
	}

	@Caption("WrrName")
	@Column(name = "wrrName", nullable = false, columnDefinition = "nvarchar")
	public String getWrrName() {
		return this.wrrName;
	}

	public void setWrrName(String wrrName) {
		this.wrrName = wrrName;
	}

	@Caption("WrrState")
	@Column(name = "wrrState", nullable = false)
	public short getWrrState() {
		return this.wrrState;
	}

	public void setWrrState(short wrrState) {
		this.wrrState = wrrState;
	}

	@Caption("Employees")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workRole")
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

}
