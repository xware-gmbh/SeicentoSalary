package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.RowLabelDAO;

/**
 * RowLabel
 */
@DAO(daoClass = RowLabelDAO.class)
@Caption("{%lblLabelShort}")
@Entity
@Table(name = "RowLabel", schema = "dbo")
public class RowLabel implements java.io.Serializable {

	private long lblId;
	private Language language;
	private RowObject rowObject;
	private String lblLabelShort;
	private String lblLabelLong;
	private Short lblState;

	public RowLabel() {
	}

	public RowLabel(long lblId, Language language, RowObject rowObject) {
		this.lblId = lblId;
		this.language = language;
		this.rowObject = rowObject;
	}

	public RowLabel(long lblId, Language language, RowObject rowObject, String lblLabelShort, String lblLabelLong,
			Short lblState) {
		this.lblId = lblId;
		this.language = language;
		this.rowObject = rowObject;
		this.lblLabelShort = lblLabelShort;
		this.lblLabelLong = lblLabelLong;
		this.lblState = lblState;
	}

	@Caption("LblId")
	@Id

	@Column(name = "lblId", unique = true, nullable = false)
	public long getLblId() {
		return this.lblId;
	}

	public void setLblId(long lblId) {
		this.lblId = lblId;
	}

	@Caption("Language")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lbllngId", nullable = false)
	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Caption("RowObject")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lblobjId", nullable = false)
	public RowObject getRowObject() {
		return this.rowObject;
	}

	public void setRowObject(RowObject rowObject) {
		this.rowObject = rowObject;
	}

	@Caption("LblLabelShort")
	@Column(name = "lblLabelShort", columnDefinition = "nvarchar")
	public String getLblLabelShort() {
		return this.lblLabelShort;
	}

	public void setLblLabelShort(String lblLabelShort) {
		this.lblLabelShort = lblLabelShort;
	}

	@Caption("LblLabelLong")
	@Column(name = "lblLabelLong", columnDefinition = "nvarchar")
	public String getLblLabelLong() {
		return this.lblLabelLong;
	}

	public void setLblLabelLong(String lblLabelLong) {
		this.lblLabelLong = lblLabelLong;
	}

	@Caption("LblState")
	@Column(name = "lblState")
	public Short getLblState() {
		return this.lblState;
	}

	public void setLblState(Short lblState) {
		this.lblState = lblState;
	}

}
