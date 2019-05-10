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

import ch.xwr.seicentobookit.dal.RowRelationDAO;

/**
 * RowRelation
 */
@DAO(daoClass = RowRelationDAO.class)
@Caption("{%relName}")
@Entity
@Table(name = "RowRelation", schema = "dbo")
public class RowRelation implements java.io.Serializable {

	private long relId;
	private RowObject rowObjectByRelobjIdTarget;
	private RowObject rowObjectByRelobjIdSource;
	private String relName;
	private Integer relOrder;
	private String relDescription;
	private Short relState;

	public RowRelation() {
	}

	public RowRelation(long relId, RowObject rowObjectByRelobjIdTarget, RowObject rowObjectByRelobjIdSource,
			String relName) {
		this.relId = relId;
		this.rowObjectByRelobjIdTarget = rowObjectByRelobjIdTarget;
		this.rowObjectByRelobjIdSource = rowObjectByRelobjIdSource;
		this.relName = relName;
	}

	public RowRelation(long relId, RowObject rowObjectByRelobjIdTarget, RowObject rowObjectByRelobjIdSource,
			String relName, Integer relOrder, String relDescription, Short relState) {
		this.relId = relId;
		this.rowObjectByRelobjIdTarget = rowObjectByRelobjIdTarget;
		this.rowObjectByRelobjIdSource = rowObjectByRelobjIdSource;
		this.relName = relName;
		this.relOrder = relOrder;
		this.relDescription = relDescription;
		this.relState = relState;
	}

	@Caption("RelId")
	@Id

	@Column(name = "relId", unique = true, nullable = false)
	public long getRelId() {
		return this.relId;
	}

	public void setRelId(long relId) {
		this.relId = relId;
	}

	@Caption("RowObjectByRelobjIdTarget")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "relobjId_Target", nullable = false)
	public RowObject getRowObjectByRelobjIdTarget() {
		return this.rowObjectByRelobjIdTarget;
	}

	public void setRowObjectByRelobjIdTarget(RowObject rowObjectByRelobjIdTarget) {
		this.rowObjectByRelobjIdTarget = rowObjectByRelobjIdTarget;
	}

	@Caption("RowObjectByRelobjIdSource")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "relobjId_Source", nullable = false)
	public RowObject getRowObjectByRelobjIdSource() {
		return this.rowObjectByRelobjIdSource;
	}

	public void setRowObjectByRelobjIdSource(RowObject rowObjectByRelobjIdSource) {
		this.rowObjectByRelobjIdSource = rowObjectByRelobjIdSource;
	}

	@Caption("RelName")
	@Column(name = "relName", nullable = false, columnDefinition = "nvarchar")
	public String getRelName() {
		return this.relName;
	}

	public void setRelName(String relName) {
		this.relName = relName;
	}

	@Caption("RelOrder")
	@Column(name = "relOrder")
	public Integer getRelOrder() {
		return this.relOrder;
	}

	public void setRelOrder(Integer relOrder) {
		this.relOrder = relOrder;
	}

	@Caption("RelDescription")
	@Column(name = "relDescription", columnDefinition = "nvarchar")
	public String getRelDescription() {
		return this.relDescription;
	}

	public void setRelDescription(String relDescription) {
		this.relDescription = relDescription;
	}

	@Caption("RelState")
	@Column(name = "relState")
	public Short getRelState() {
		return this.relState;
	}

	public void setRelState(Short relState) {
		this.relState = relState;
	}

}
