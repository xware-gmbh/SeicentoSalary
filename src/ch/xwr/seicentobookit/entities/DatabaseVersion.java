package ch.xwr.seicentobookit.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.DatabaseVersionDAO;

/**
 * DatabaseVersion
 */
@DAO(daoClass = DatabaseVersionDAO.class)
@Caption("{%dbvMicro}")
@Entity
@Table(name = "DatabaseVersion", schema = "dbo")
public class DatabaseVersion implements java.io.Serializable {

	private long dbvId;
	private Integer dbvMajor;
	private Integer dbvMinor;
	private String dbvMicro;
	private Date dbvReleased;
	private String dbvDescription;
	private Short dbvState;
	private Set<RowObject> rowObjects = new HashSet<RowObject>(0);

	public DatabaseVersion() {
	}

	public DatabaseVersion(long dbvId) {
		this.dbvId = dbvId;
	}

	public DatabaseVersion(long dbvId, Integer dbvMajor, Integer dbvMinor, String dbvMicro, Date dbvReleased,
			String dbvDescription, Short dbvState, Set<RowObject> rowObjects) {
		this.dbvId = dbvId;
		this.dbvMajor = dbvMajor;
		this.dbvMinor = dbvMinor;
		this.dbvMicro = dbvMicro;
		this.dbvReleased = dbvReleased;
		this.dbvDescription = dbvDescription;
		this.dbvState = dbvState;
		this.rowObjects = rowObjects;
	}

	@Caption("DbvId")
	@Id

	@Column(name = "dbvId", unique = true, nullable = false)
	public long getDbvId() {
		return this.dbvId;
	}

	public void setDbvId(long dbvId) {
		this.dbvId = dbvId;
	}

	@Caption("DbvMajor")
	@Column(name = "dbvMajor")
	public Integer getDbvMajor() {
		return this.dbvMajor;
	}

	public void setDbvMajor(Integer dbvMajor) {
		this.dbvMajor = dbvMajor;
	}

	@Caption("DbvMinor")
	@Column(name = "dbvMinor")
	public Integer getDbvMinor() {
		return this.dbvMinor;
	}

	public void setDbvMinor(Integer dbvMinor) {
		this.dbvMinor = dbvMinor;
	}

	@Caption("DbvMicro")
	@Column(name = "dbvMicro", columnDefinition = "nvarchar")
	public String getDbvMicro() {
		return this.dbvMicro;
	}

	public void setDbvMicro(String dbvMicro) {
		this.dbvMicro = dbvMicro;
	}

	@Caption("DbvReleased")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dbvReleased", length = 23)
	public Date getDbvReleased() {
		return this.dbvReleased;
	}

	public void setDbvReleased(Date dbvReleased) {
		this.dbvReleased = dbvReleased;
	}

	@Caption("DbvDescription")
	@Lob
	@Column(name = "dbvDescription", columnDefinition = "ntext")
	public String getDbvDescription() {
		return this.dbvDescription;
	}

	public void setDbvDescription(String dbvDescription) {
		this.dbvDescription = dbvDescription;
	}

	@Caption("DbvState")
	@Column(name = "dbvState")
	public Short getDbvState() {
		return this.dbvState;
	}

	public void setDbvState(Short dbvState) {
		this.dbvState = dbvState;
	}

	@Caption("RowObjects")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "databaseVersion")
	public Set<RowObject> getRowObjects() {
		return this.rowObjects;
	}

	public void setRowObjects(Set<RowObject> rowObjects) {
		this.rowObjects = rowObjects;
	}

}
