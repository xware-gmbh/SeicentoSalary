package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.VatDAO;

/**
 * Vat
 */
@DAO(daoClass = VatDAO.class)
@Caption("{%vatName}")
@Entity
@Table(name = "Vat", schema = "dbo")
public class Vat implements java.io.Serializable {

	private long vatId;
	private String vatName;
	private Double vatRate;
	private String vatSign;
	private Boolean vatInclude;
	private Short vatState;

	public Vat() {
	}

	public Vat(long vatId) {
		this.vatId = vatId;
	}

	public Vat(long vatId, String vatName, Double vatRate, String vatSign, Boolean vatInclude, Short vatState) {
		this.vatId = vatId;
		this.vatName = vatName;
		this.vatRate = vatRate;
		this.vatSign = vatSign;
		this.vatInclude = vatInclude;
		this.vatState = vatState;
	}

	@Caption("VatId")
	@Id

	@Column(name = "vatId", unique = true, nullable = false)
	public long getVatId() {
		return this.vatId;
	}

	public void setVatId(long vatId) {
		this.vatId = vatId;
	}

	@Caption("VatName")
	@Column(name = "vatName", columnDefinition = "nvarchar")
	public String getVatName() {
		return this.vatName;
	}

	public void setVatName(String vatName) {
		this.vatName = vatName;
	}

	@Caption("VatRate")
	@Column(name = "vatRate", precision = 6, scale = 4)
	public Double getVatRate() {
		return this.vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	@Caption("VatSign")
	@Column(name = "vatSign", columnDefinition = "nvarchar")
	public String getVatSign() {
		return this.vatSign;
	}

	public void setVatSign(String vatSign) {
		this.vatSign = vatSign;
	}

	@Caption("VatInclude")
	@Column(name = "vatInclude")
	public Boolean getVatInclude() {
		return this.vatInclude;
	}

	public void setVatInclude(Boolean vatInclude) {
		this.vatInclude = vatInclude;
	}

	@Caption("VatState")
	@Column(name = "vatState")
	public Short getVatState() {
		return this.vatState;
	}

	public void setVatState(Short vatState) {
		this.vatState = vatState;
	}

}
