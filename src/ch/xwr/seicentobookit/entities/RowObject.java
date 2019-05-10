package ch.xwr.seicentobookit.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.RowObjectDAO;

/**
 * RowObject
 */
@DAO(daoClass = RowObjectDAO.class)
@Caption("{%objAddedBy}")
@Entity
@Table(name = "RowObject", schema = "dbo", uniqueConstraints = @UniqueConstraint(columnNames = {
		"objentId", "objRowId" }))
public class RowObject implements java.io.Serializable {

	private long objId;
	private DatabaseVersion databaseVersion;
	private RowEntity entity;
	private long objRowId;
	private Long objChngcnt;
	private LovState.State objState;
	private Date objAdded;
	private String objAddedBy;
	private Date objChanged;
	private String objChangedBy;
	private Date objDeleted;
	private String objDeletedBy;
	private Set<RowLabel> rowLabels = new HashSet<RowLabel>(0);
	private Set<RowSecurity> rowSecurities = new HashSet<RowSecurity>(0);
	private Set<RowRelation> rowRelationsForRelobjIdTarget = new HashSet<RowRelation>(0);
	private Set<RowFile> rowFiles = new HashSet<RowFile>(0);
	private Set<RowParameter> rowParameters = new HashSet<RowParameter>(0);
	private Set<RowText> rowTexts = new HashSet<RowText>(0);
	private Set<RowRelation> rowRelationsForRelobjIdSource = new HashSet<RowRelation>(0);

	public RowObject() {
	}

	public RowObject(long objId, RowEntity entity, long objRowId) {
		this.objId = objId;
		this.entity = entity;
		this.objRowId = objRowId;
	}

	public RowObject(long objId, DatabaseVersion databaseVersion, RowEntity entity, long objRowId, Long objChngcnt,
			LovState.State objState, Date objAdded, String objAddedBy, Date objChanged, String objChangedBy, Date objDeleted,
			String objDeletedBy, Set<RowLabel> rowLabels, Set<RowSecurity> rowSecurities,
			Set<RowRelation> rowRelationsForRelobjIdTarget, Set<RowFile> rowFiles, Set<RowParameter> rowParameters,
			Set<RowText> rowTexts, Set<RowRelation> rowRelationsForRelobjIdSource) {
		this.objId = objId;
		this.databaseVersion = databaseVersion;
		this.entity = entity;
		this.objRowId = objRowId;
		this.objChngcnt = objChngcnt;
		this.objState = objState;
		this.objAdded = objAdded;
		this.objAddedBy = objAddedBy;
		this.objChanged = objChanged;
		this.objChangedBy = objChangedBy;
		this.objDeleted = objDeleted;
		this.objDeletedBy = objDeletedBy;
		this.rowLabels = rowLabels;
		this.rowSecurities = rowSecurities;
		this.rowRelationsForRelobjIdTarget = rowRelationsForRelobjIdTarget;
		this.rowFiles = rowFiles;
		this.rowParameters = rowParameters;
		this.rowTexts = rowTexts;
		this.rowRelationsForRelobjIdSource = rowRelationsForRelobjIdSource;
	}

	@Caption("ObjId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "objId", unique = true, nullable = false)
	public long getObjId() {
		return this.objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	@Caption("DatabaseVersion")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "objdbvId")
	public DatabaseVersion getDatabaseVersion() {
		return this.databaseVersion;
	}

	public void setDatabaseVersion(DatabaseVersion databaseVersion) {
		this.databaseVersion = databaseVersion;
	}

	@Caption("Entity")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "objentId", nullable = false)
	public RowEntity getRowEntity() {
		return this.entity;
	}

	public void setRowEntity(RowEntity entity) {
		this.entity = entity;
	}

	@Caption("ObjRowId")
	@Column(name = "objRowId", nullable = false)
	public long getObjRowId() {
		return this.objRowId;
	}

	public void setObjRowId(long objRowId) {
		this.objRowId = objRowId;
	}

	@Caption("ObjChngcnt")
	@Column(name = "objChngcnt")
	public Long getObjChngcnt() {
		return this.objChngcnt;
	}

	public void setObjChngcnt(Long objChngcnt) {
		this.objChngcnt = objChngcnt;
	}

	@Caption("ObjState")
	@Column(name = "objState")
	public LovState.State getObjState() {
		return this.objState;
	}

	public void setObjState(LovState.State objState) {
		this.objState = objState;
	}

	@Caption("ObjAdded")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "objAdded", length = 23)
	public Date getObjAdded() {
		return this.objAdded;
	}

	public void setObjAdded(Date objAdded) {
		this.objAdded = objAdded;
	}

	@Caption("ObjAddedBy")
	@Column(name = "objAddedBy", columnDefinition = "nvarchar")
	public String getObjAddedBy() {
		return this.objAddedBy;
	}

	public void setObjAddedBy(String objAddedBy) {
		this.objAddedBy = objAddedBy;
	}

	@Caption("ObjChanged")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "objChanged", length = 23)
	public Date getObjChanged() {
		return this.objChanged;
	}

	public void setObjChanged(Date objChanged) {
		this.objChanged = objChanged;
	}

	@Caption("ObjChangedBy")
	@Column(name = "objChangedBy", columnDefinition = "nvarchar")
	public String getObjChangedBy() {
		return this.objChangedBy;
	}

	public void setObjChangedBy(String objChangedBy) {
		this.objChangedBy = objChangedBy;
	}

	@Caption("ObjDeleted")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "objDeleted", length = 23)
	public Date getObjDeleted() {
		return this.objDeleted;
	}

	public void setObjDeleted(Date objDeleted) {
		this.objDeleted = objDeleted;
	}

	@Caption("ObjDeletedBy")
	@Column(name = "objDeletedBy", columnDefinition = "nvarchar")
	public String getObjDeletedBy() {
		return this.objDeletedBy;
	}

	public void setObjDeletedBy(String objDeletedBy) {
		this.objDeletedBy = objDeletedBy;
	}

	@Caption("RowLabels")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObject")
	public Set<RowLabel> getRowLabels() {
		return this.rowLabels;
	}

	public void setRowLabels(Set<RowLabel> rowLabels) {
		this.rowLabels = rowLabels;
	}

	@Caption("RowSecurities")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObject")
	public Set<RowSecurity> getRowSecurities() {
		return this.rowSecurities;
	}

	public void setRowSecurities(Set<RowSecurity> rowSecurities) {
		this.rowSecurities = rowSecurities;
	}

	@Caption("RowRelationsForRelobjIdTarget")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObjectByRelobjIdTarget")
	public Set<RowRelation> getRowRelationsForRelobjIdTarget() {
		return this.rowRelationsForRelobjIdTarget;
	}

	public void setRowRelationsForRelobjIdTarget(Set<RowRelation> rowRelationsForRelobjIdTarget) {
		this.rowRelationsForRelobjIdTarget = rowRelationsForRelobjIdTarget;
	}

	@Caption("RowFiles")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObject")
	public Set<RowFile> getRowFiles() {
		return this.rowFiles;
	}

	public void setRowFiles(Set<RowFile> rowFiles) {
		this.rowFiles = rowFiles;
	}

	@Caption("RowParameters")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObject")
	public Set<RowParameter> getRowParameters() {
		return this.rowParameters;
	}

	public void setRowParameters(Set<RowParameter> rowParameters) {
		this.rowParameters = rowParameters;
	}

	@Caption("RowTexts")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObject")
	public Set<RowText> getRowTexts() {
		return this.rowTexts;
	}

	public void setRowTexts(Set<RowText> rowTexts) {
		this.rowTexts = rowTexts;
	}

	@Caption("RowRelationsForRelobjIdSource")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rowObjectByRelobjIdSource")
	public Set<RowRelation> getRowRelationsForRelobjIdSource() {
		return this.rowRelationsForRelobjIdSource;
	}

	public void setRowRelationsForRelobjIdSource(Set<RowRelation> rowRelationsForRelobjIdSource) {
		this.rowRelationsForRelobjIdSource = rowRelationsForRelobjIdSource;
	}

}
