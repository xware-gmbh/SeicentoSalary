package ch.xwr.seicentobookit.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.RowSecurityDAO;

/**
 * RowSecurity
 */
@DAO(daoClass = RowSecurityDAO.class)
@Caption("{%secValidto}")
@Entity
@Table(name = "RowSecurity", schema = "dbo")
public class RowSecurity implements java.io.Serializable {

	private long secId;
	private RowObject rowObject;
	private int secType;
	private Date secValidfrom;
	private String secValidto;
	private short secState;
	private String secPermissionKey;

	public RowSecurity() {
	}

	public RowSecurity(long secId, RowObject rowObject, int secType, short secState) {
		this.secId = secId;
		this.rowObject = rowObject;
		this.secType = secType;
		this.secState = secState;
	}

	public RowSecurity(long secId, RowObject rowObject, int secType, Date secValidfrom, String secValidto,
			short secState, String secPermissionKey) {
		this.secId = secId;
		this.rowObject = rowObject;
		this.secType = secType;
		this.secValidfrom = secValidfrom;
		this.secValidto = secValidto;
		this.secState = secState;
		this.secPermissionKey = secPermissionKey;
	}

	@Caption("SecId")
	@Id

	@Column(name = "secId", unique = true, nullable = false)
	public long getSecId() {
		return this.secId;
	}

	public void setSecId(long secId) {
		this.secId = secId;
	}

	@Caption("RowObject")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "secobjId", nullable = false)
	public RowObject getRowObject() {
		return this.rowObject;
	}

	public void setRowObject(RowObject rowObject) {
		this.rowObject = rowObject;
	}

	@Caption("SecType")
	@Column(name = "secType", nullable = false)
	public int getSecType() {
		return this.secType;
	}

	public void setSecType(int secType) {
		this.secType = secType;
	}

	@Caption("SecValidfrom")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "secValidfrom", length = 23)
	public Date getSecValidfrom() {
		return this.secValidfrom;
	}

	public void setSecValidfrom(Date secValidfrom) {
		this.secValidfrom = secValidfrom;
	}

	@Caption("SecValidto")
	@Column(name = "secValidto", columnDefinition = "nvarchar")
	public String getSecValidto() {
		return this.secValidto;
	}

	public void setSecValidto(String secValidto) {
		this.secValidto = secValidto;
	}

	@Caption("SecState")
	@Column(name = "secState", nullable = false)
	public short getSecState() {
		return this.secState;
	}

	public void setSecState(short secState) {
		this.secState = secState;
	}

	@Caption("SecPermissionKey")
	@Column(name = "secPermissionKey", columnDefinition = "nvarchar")
	public String getSecPermissionKey() {
		return this.secPermissionKey;
	}

	public void setSecPermissionKey(String secPermissionKey) {
		this.secPermissionKey = secPermissionKey;
	}

}
