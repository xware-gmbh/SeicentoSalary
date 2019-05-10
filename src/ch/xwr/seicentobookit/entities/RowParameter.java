package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.RowParameterDAO;

/**
 * RowParameter
 */
@DAO(daoClass = RowParameterDAO.class)
@Caption("{%prmGroup}")
@Entity
@Table(name = "RowParameter", schema = "dbo", uniqueConstraints = @UniqueConstraint(columnNames = {
		"prmobjId", "prmGroup", "prmSubGroup", "prmKey" }))
public class RowParameter implements java.io.Serializable {

	private long prmId;
	private RowObject rowObject;
	private String prmGroup;
	private String prmSubGroup;
	private String prmKey;
	private String prmValue;
	private Short prmValueType;
	private Short prmState;
	private Short prmParamType;
	private String prmLookupTable;
	private Boolean prmVisible;

	public RowParameter() {
	}

	public RowParameter(long prmId, RowObject rowObject) {
		this.prmId = prmId;
		this.rowObject = rowObject;
	}

	public RowParameter(long prmId, RowObject rowObject, String prmGroup, String prmSubGroup, String prmKey,
			String prmValue, Short prmValueType, Short prmState, Short prmParamType, String prmLookupTable,
			Boolean prmVisible) {
		this.prmId = prmId;
		this.rowObject = rowObject;
		this.prmGroup = prmGroup;
		this.prmSubGroup = prmSubGroup;
		this.prmKey = prmKey;
		this.prmValue = prmValue;
		this.prmValueType = prmValueType;
		this.prmState = prmState;
		this.prmParamType = prmParamType;
		this.prmLookupTable = prmLookupTable;
		this.prmVisible = prmVisible;
	}

	@Caption("PrmId")
	@Id

	@Column(name = "prmId", unique = true, nullable = false)
	public long getPrmId() {
		return this.prmId;
	}

	public void setPrmId(long prmId) {
		this.prmId = prmId;
	}

	@Caption("RowObject")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prmobjId", nullable = false)
	public RowObject getRowObject() {
		return this.rowObject;
	}

	public void setRowObject(RowObject rowObject) {
		this.rowObject = rowObject;
	}

	@Caption("PrmGroup")
	@Column(name = "prmGroup", columnDefinition = "nvarchar")
	public String getPrmGroup() {
		return this.prmGroup;
	}

	public void setPrmGroup(String prmGroup) {
		this.prmGroup = prmGroup;
	}

	@Caption("PrmSubGroup")
	@Column(name = "prmSubGroup", columnDefinition = "nvarchar")
	public String getPrmSubGroup() {
		return this.prmSubGroup;
	}

	public void setPrmSubGroup(String prmSubGroup) {
		this.prmSubGroup = prmSubGroup;
	}

	@Caption("PrmKey")
	@Column(name = "prmKey", columnDefinition = "nvarchar")
	public String getPrmKey() {
		return this.prmKey;
	}

	public void setPrmKey(String prmKey) {
		this.prmKey = prmKey;
	}

	@Caption("PrmValue")
	@Column(name = "prmValue", columnDefinition = "nvarchar")
	public String getPrmValue() {
		return this.prmValue;
	}

	public void setPrmValue(String prmValue) {
		this.prmValue = prmValue;
	}

	@Caption("PrmValueType")
	@Column(name = "prmValueType")
	public Short getPrmValueType() {
		return this.prmValueType;
	}

	public void setPrmValueType(Short prmValueType) {
		this.prmValueType = prmValueType;
	}

	@Caption("PrmState")
	@Column(name = "prmState")
	public Short getPrmState() {
		return this.prmState;
	}

	public void setPrmState(Short prmState) {
		this.prmState = prmState;
	}

	@Caption("PrmParamType")
	@Column(name = "prmParamType")
	public Short getPrmParamType() {
		return this.prmParamType;
	}

	public void setPrmParamType(Short prmParamType) {
		this.prmParamType = prmParamType;
	}

	@Caption("PrmLookupTable")
	@Column(name = "prmLookupTable", columnDefinition = "nvarchar")
	public String getPrmLookupTable() {
		return this.prmLookupTable;
	}

	public void setPrmLookupTable(String prmLookupTable) {
		this.prmLookupTable = prmLookupTable;
	}

	@Caption("PrmVisible")
	@Column(name = "prmVisible")
	public Boolean getPrmVisible() {
		return this.prmVisible;
	}

	public void setPrmVisible(Boolean prmVisible) {
		this.prmVisible = prmVisible;
	}

}
