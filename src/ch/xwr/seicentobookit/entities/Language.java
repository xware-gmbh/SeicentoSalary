package ch.xwr.seicentobookit.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.LanguageDAO;

/**
 * Language
 */
@DAO(daoClass = LanguageDAO.class)
@Caption("{%lngName}")
@Entity
@Table(name = "Language", schema = "dbo", uniqueConstraints = @UniqueConstraint(columnNames = "lngCode"))
public class Language implements java.io.Serializable {

	private long lngId;
	private int lngCode;
	private String lngName;
	private String lngIsocode;
	private String lngKeyboard;
	private Boolean lngDefault;
	private Short lngState;
	private Set<RowText> rowTexts = new HashSet<RowText>(0);
	private Set<RowLabel> rowLabels = new HashSet<RowLabel>(0);

	public Language() {
	}

	public Language(long lngId, int lngCode, String lngName) {
		this.lngId = lngId;
		this.lngCode = lngCode;
		this.lngName = lngName;
	}

	public Language(long lngId, int lngCode, String lngName, String lngIsocode, String lngKeyboard, Boolean lngDefault,
			Short lngState, Set<RowText> rowTexts, Set<RowLabel> rowLabels) {
		this.lngId = lngId;
		this.lngCode = lngCode;
		this.lngName = lngName;
		this.lngIsocode = lngIsocode;
		this.lngKeyboard = lngKeyboard;
		this.lngDefault = lngDefault;
		this.lngState = lngState;
		this.rowTexts = rowTexts;
		this.rowLabels = rowLabels;
	}

	@Caption("LngId")
	@Id

	@Column(name = "lngId", unique = true, nullable = false)
	public long getLngId() {
		return this.lngId;
	}

	public void setLngId(long lngId) {
		this.lngId = lngId;
	}

	@Caption("LngCode")
	@Column(name = "lngCode", unique = true, nullable = false)
	public int getLngCode() {
		return this.lngCode;
	}

	public void setLngCode(int lngCode) {
		this.lngCode = lngCode;
	}

	@Caption("LngName")
	@Column(name = "lngName", nullable = false, columnDefinition = "nvarchar")
	public String getLngName() {
		return this.lngName;
	}

	public void setLngName(String lngName) {
		this.lngName = lngName;
	}

	@Caption("LngIsocode")
	@Column(name = "lngIsocode", columnDefinition = "nvarchar")
	public String getLngIsocode() {
		return this.lngIsocode;
	}

	public void setLngIsocode(String lngIsocode) {
		this.lngIsocode = lngIsocode;
	}

	@Caption("LngKeyboard")
	@Column(name = "lngKeyboard", columnDefinition = "nvarchar")
	public String getLngKeyboard() {
		return this.lngKeyboard;
	}

	public void setLngKeyboard(String lngKeyboard) {
		this.lngKeyboard = lngKeyboard;
	}

	@Caption("LngDefault")
	@Column(name = "lngDefault")
	public Boolean getLngDefault() {
		return this.lngDefault;
	}

	public void setLngDefault(Boolean lngDefault) {
		this.lngDefault = lngDefault;
	}

	@Caption("LngState")
	@Column(name = "lngState")
	public Short getLngState() {
		return this.lngState;
	}

	public void setLngState(Short lngState) {
		this.lngState = lngState;
	}

	@Caption("RowTexts")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "language")
	public Set<RowText> getRowTexts() {
		return this.rowTexts;
	}

	public void setRowTexts(Set<RowText> rowTexts) {
		this.rowTexts = rowTexts;
	}

	@Caption("RowLabels")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "language")
	public Set<RowLabel> getRowLabels() {
		return this.rowLabels;
	}

	public void setRowLabels(Set<RowLabel> rowLabels) {
		this.rowLabels = rowLabels;
	}

}
