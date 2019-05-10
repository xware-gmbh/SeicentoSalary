package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.RowFileDAO;

/**
 * RowFile
 */
@DAO(daoClass = RowFileDAO.class)
@Caption("{%rimName}")
@Entity
@Table(name = "RowFile", schema = "dbo")
public class RowFile implements java.io.Serializable {

	private long rimId;
	private RowObject rowObject;
	private String rimName;
	private byte[] rimIcon;
	private byte[] rimFile;
	private LovState.State rimState;
	private String rimMimetype;
	private int rimNumber;
	private short rimType;
	private String rimSize;

	public RowFile() {
	}

	public RowFile(long rimId, RowObject rowObject, int rimNumber, short rimType) {
		this.rimId = rimId;
		this.rowObject = rowObject;
		this.rimNumber = rimNumber;
		this.rimType = rimType;
	}

	public RowFile(long rimId, RowObject rowObject, String rimName, byte[] rimIcon, byte[] rimFile, LovState.State rimState,
			String rimMimetype, int rimNumber, short rimType, String rimSize) {
		this.rimId = rimId;
		this.rowObject = rowObject;
		this.rimName = rimName;
		this.rimIcon = rimIcon;
		this.rimFile = rimFile;
		this.rimState = rimState;
		this.rimMimetype = rimMimetype;
		this.rimNumber = rimNumber;
		this.rimType = rimType;
		this.rimSize = rimSize;
	}

	@Caption("RimId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "rimId", unique = true, nullable = false)
	public long getRimId() {
		return this.rimId;
	}

	public void setRimId(long rimId) {
		this.rimId = rimId;
	}

	@Caption("RowObject")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rimobjId", nullable = false)
	public RowObject getRowObject() {
		return this.rowObject;
	}

	public void setRowObject(RowObject rowObject) {
		this.rowObject = rowObject;
	}

	@Caption("RimName")
	@Column(name = "rimName", columnDefinition = "nvarchar")
	public String getRimName() {
		return this.rimName;
	}

	public void setRimName(String rimName) {
		this.rimName = rimName;
	}

	@Caption("RimIcon")
	@Column(name = "rimIcon")
	public byte[] getRimIcon() {
		return this.rimIcon;
	}

	public void setRimIcon(byte[] rimIcon) {
		this.rimIcon = rimIcon;
	}

	@Caption("RimFile")
	@Column(name = "rimFile")
	public byte[] getRimFile() {
		return this.rimFile;
	}

	public void setRimFile(byte[] rimFile) {
		this.rimFile = rimFile;
	}

	@Caption("RimState")
	@Column(name = "rimState")
	public LovState.State getRimState() {
		return this.rimState;
	}

	public void setRimState(LovState.State rimState) {
		this.rimState = rimState;
	}

	@Caption("RimMimetype")
	@Column(name = "rimMimetype", columnDefinition = "nvarchar")
	public String getRimMimetype() {
		return this.rimMimetype;
	}

	public void setRimMimetype(String rimMimetype) {
		this.rimMimetype = rimMimetype;
	}

	@Caption("RimNumber")
	@Column(name = "rimNumber", nullable = false)
	public int getRimNumber() {
		return this.rimNumber;
	}

	public void setRimNumber(int rimNumber) {
		this.rimNumber = rimNumber;
	}

	@Caption("RimType")
	@Column(name = "rimType", nullable = false)
	public short getRimType() {
		return this.rimType;
	}

	public void setRimType(short rimType) {
		this.rimType = rimType;
	}

	@Caption("RimSize")
	@Column(name = "rimSize", columnDefinition = "nvarchar")
	public String getRimSize() {
		return this.rimSize;
	}

	public void setRimSize(String rimSize) {
		this.rimSize = rimSize;
	}

}
