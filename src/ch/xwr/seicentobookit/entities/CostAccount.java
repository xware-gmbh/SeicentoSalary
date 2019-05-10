package ch.xwr.seicentobookit.entities;

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

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.dal.CostAccountDAO;

/**
 * CostAccount
 */
@DAO(daoClass = CostAccountDAO.class)
@Caption("{%csaCode}")
@Entity
@Table(name = "CostAccount", schema = "dbo")
public class CostAccount implements java.io.Serializable {

	private long csaId;
	private CostAccount costAccount;
	private String csaCode;
	private String csaName;
	private Short csaState;
	private Set<CostAccount> costAccounts = new HashSet<CostAccount>(0);

	public CostAccount() {
	}

	public CostAccount(long csaId) {
		this.csaId = csaId;
	}

	public CostAccount(long csaId, CostAccount costAccount, String csaCode, String csaName, Short csaState,
			Set<CostAccount> costAccounts) {
		this.csaId = csaId;
		this.costAccount = costAccount;
		this.csaCode = csaCode;
		this.csaName = csaName;
		this.csaState = csaState;
		this.costAccounts = costAccounts;
	}

	@Caption("CsaId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "csaId", unique = true, nullable = false)
	public long getCsaId() {
		return this.csaId;
	}

	public void setCsaId(long csaId) {
		this.csaId = csaId;
	}

	@Caption("CostAccount")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "csacsaId")
	public CostAccount getCostAccount() {
		return this.costAccount;
	}

	public void setCostAccount(CostAccount costAccount) {
		this.costAccount = costAccount;
	}

	@Caption("CsaCode")
	@Column(name = "csaCode", columnDefinition = "nvarchar")
	public String getCsaCode() {
		return this.csaCode;
	}

	public void setCsaCode(String csaCode) {
		this.csaCode = csaCode;
	}

	@Caption("CsaName")
	@Column(name = "csaName", columnDefinition = "nvarchar")
	public String getCsaName() {
		return this.csaName;
	}

	public void setCsaName(String csaName) {
		this.csaName = csaName;
	}

	@Caption("CsaState")
	@Column(name = "csaState")
	public Short getCsaState() {
		return this.csaState;
	}

	public void setCsaState(Short csaState) {
		this.csaState = csaState;
	}

	@Caption("CostAccounts")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "costAccount")
	public Set<CostAccount> getCostAccounts() {
		return this.costAccounts;
	}

	public void setCostAccounts(Set<CostAccount> costAccounts) {
		this.costAccounts = costAccounts;
	}

}
