package ch.xwr.seicentobookit.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xdev.dal.DAO;

import ch.xwr.seicentobookit.dal.RowEntityDAO;

/**
 * Entity
 */
@DAO(daoClass = RowEntityDAO.class)
@Entity
@Table(name = "Entity", schema = "dbo")
public class RowEntity implements java.io.Serializable {

	private long entId;
	private String entName;
	private String entAbbreviation;
	private String entDataclass;
	private Boolean entHasrowobject;
	private Boolean entReadonly;
	private Boolean entExport2sdf;
	private Short entSdfOrdinal;
	private Short entAuditHistory;
	private Integer entType;
	private Short entState;
	public RowEntity() {
	}

	public RowEntity(long entId) {
		this.entId = entId;
	}

	public RowEntity(long entId, String entName, String entAbbreviation, String entDataclass, Boolean entHasrowobject,
			Boolean entReadonly, Boolean entExport2sdf, Short entSdfOrdinal, Short entAuditHistory, Integer entType,
			Short entState, Set<RowObject> rowObjects) {
		this.entId = entId;
		this.entName = entName;
		this.entAbbreviation = entAbbreviation;
		this.entDataclass = entDataclass;
		this.entHasrowobject = entHasrowobject;
		this.entReadonly = entReadonly;
		this.entExport2sdf = entExport2sdf;
		this.entSdfOrdinal = entSdfOrdinal;
		this.entAuditHistory = entAuditHistory;
		this.entType = entType;
		this.entState = entState;
	}

	@Id

	@Column(name = "entId", unique = true, nullable = false)
	public long getEntId() {
		return this.entId;
	}

	public void setEntId(long entId) {
		this.entId = entId;
	}

	@Column(name = "entName", columnDefinition = "nvarchar")
	public String getEntName() {
		return this.entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	@Column(name = "entAbbreviation", columnDefinition = "nvarchar")
	public String getEntAbbreviation() {
		return this.entAbbreviation;
	}

	public void setEntAbbreviation(String entAbbreviation) {
		this.entAbbreviation = entAbbreviation;
	}

	@Column(name = "entDataclass", columnDefinition = "nvarchar")
	public String getEntDataclass() {
		return this.entDataclass;
	}

	public void setEntDataclass(String entDataclass) {
		this.entDataclass = entDataclass;
	}

	@Column(name = "entHasrowobject")
	public Boolean getEntHasrowobject() {
		return this.entHasrowobject;
	}

	public void setEntHasrowobject(Boolean entHasrowobject) {
		this.entHasrowobject = entHasrowobject;
	}

	@Column(name = "entReadonly")
	public Boolean getEntReadonly() {
		return this.entReadonly;
	}

	public void setEntReadonly(Boolean entReadonly) {
		this.entReadonly = entReadonly;
	}

	@Column(name = "entExport2sdf")
	public Boolean getEntExport2sdf() {
		return this.entExport2sdf;
	}

	public void setEntExport2sdf(Boolean entExport2sdf) {
		this.entExport2sdf = entExport2sdf;
	}

	@Column(name = "entSdfOrdinal")
	public Short getEntSdfOrdinal() {
		return this.entSdfOrdinal;
	}

	public void setEntSdfOrdinal(Short entSdfOrdinal) {
		this.entSdfOrdinal = entSdfOrdinal;
	}

	@Column(name = "entAuditHistory")
	public Short getEntAuditHistory() {
		return this.entAuditHistory;
	}

	public void setEntAuditHistory(Short entAuditHistory) {
		this.entAuditHistory = entAuditHistory;
	}

	@Column(name = "entType")
	public Integer getEntType() {
		return this.entType;
	}

	public void setEntType(Integer entType) {
		this.entType = entType;
	}

	@Column(name = "entState")
	public Short getEntState() {
		return this.entState;
	}

	public void setEntState(Short entState) {
		this.entState = entState;
	}

}
