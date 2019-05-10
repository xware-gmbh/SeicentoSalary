package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.RowTextDAO;

/**
 * RowText
 */
@DAO(daoClass = RowTextDAO.class)
@Caption("{%txtFreetext}")
@Entity
@Table(name = "RowText", schema = "dbo", uniqueConstraints = @UniqueConstraint(columnNames = {
		"txtobjId", "txtlngId", "txtNumber" }))
public class RowText implements java.io.Serializable {

	private long txtId;
	private Language language;
	private RowObject rowObject;
	private Integer txtNumber;
	private String txtFreetext;
	private LovState.State txtState;

	public RowText() {
	}

	public RowText(long txtId, Language language, RowObject rowObject) {
		this.txtId = txtId;
		this.language = language;
		this.rowObject = rowObject;
	}

	public RowText(long txtId, Language language, RowObject rowObject, Integer txtNumber, String txtFreetext,
			LovState.State txtState) {
		this.txtId = txtId;
		this.language = language;
		this.rowObject = rowObject;
		this.txtNumber = txtNumber;
		this.txtFreetext = txtFreetext;
		this.txtState = txtState;
	}

	@Caption("TxtId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "txtId", unique = true, nullable = false)
	public long getTxtId() {
		return this.txtId;
	}

	public void setTxtId(long txtId) {
		this.txtId = txtId;
	}

	@Caption("Language")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "txtlngId", nullable = false)
	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Caption("RowObject")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "txtobjId", nullable = false)
	public RowObject getRowObject() {
		return this.rowObject;
	}

	public void setRowObject(RowObject rowObject) {
		this.rowObject = rowObject;
	}

	@Caption("TxtNumber")
	@Column(name = "txtNumber")
	public Integer getTxtNumber() {
		return this.txtNumber;
	}

	public void setTxtNumber(Integer txtNumber) {
		this.txtNumber = txtNumber;
	}

	@Caption("TxtFreetext")
	@Lob
	@Column(name = "txtFreetext", columnDefinition = "ntext")
	public String getTxtFreetext() {
		return this.txtFreetext;
	}

	public void setTxtFreetext(String txtFreetext) {
		this.txtFreetext = txtFreetext;
	}

	@Caption("TxtState")
	@Column(name = "txtState")
	public LovState.State getTxtState() {
		return this.txtState;
	}

	public void setTxtState(LovState.State txtState) {
		this.txtState = txtState;
	}

}
