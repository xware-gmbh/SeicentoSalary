package ch.xwr.seicentobookit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xdev.dal.DAO;
import com.xdev.util.Caption;

import ch.xwr.seicentobookit.business.LovState;
import ch.xwr.seicentobookit.dal.CompanyDAO;

/**
 * Company
 */
@DAO(daoClass = CompanyDAO.class)
@Caption("{%cmpName}")
@Entity
@Table(name = "Company", schema = "dbo")
public class Company implements java.io.Serializable {

	private long cmpId;
	private String cmpName;
	private String cmpAddress;
	private Integer cmpZip;
	private String cmpPlace;
	private String cmpVatcode;
	private String cmpCurrency;
	private String cmpUid;
	private String cmpPhone;
	private String cmpMail;
	private String cmpComm1;
	private String cmpBusiness;
	private String cmpRemark;
	private String cmpJapsperServer;
	private String cmpJasperUri;
	private String cmpReportUsr;
	private String cmpReportPwd;
	private String cmpAhvnumber;
	private String cmpNameBookKepper;
	private Short cmpSalaryDay;
	private Integer cmpLastEmpNbr;
	private LovState.State cmpState;

	public Company() {
	}

	public Company(long cmpId, LovState.State cmpState) {
		this.cmpId = cmpId;
		this.cmpState = cmpState;
	}

	public Company(long cmpId, String cmpName, String cmpAddress, Integer cmpZip, String cmpPlace, String cmpVatcode,
			String cmpCurrency, String cmpUid, String cmpPhone, String cmpMail, String cmpComm1, String cmpBusiness,
			String cmpRemark, String cmpJapsperServer, String cmpJasperUri, String cmpReportUsr, String cmpReportPwd,
			String cmpAhvnumber, String cmpNameBookKepper, Short cmpSalaryDay, Integer cmpLastEmpNbr, LovState.State cmpState) {
		this.cmpId = cmpId;
		this.cmpName = cmpName;
		this.cmpAddress = cmpAddress;
		this.cmpZip = cmpZip;
		this.cmpPlace = cmpPlace;
		this.cmpVatcode = cmpVatcode;
		this.cmpCurrency = cmpCurrency;
		this.cmpUid = cmpUid;
		this.cmpPhone = cmpPhone;
		this.cmpMail = cmpMail;
		this.cmpComm1 = cmpComm1;
		this.cmpBusiness = cmpBusiness;
		this.cmpRemark = cmpRemark;
		this.cmpJapsperServer = cmpJapsperServer;
		this.cmpJasperUri = cmpJasperUri;
		this.cmpReportUsr = cmpReportUsr;
		this.cmpReportPwd = cmpReportPwd;
		this.cmpAhvnumber = cmpAhvnumber;
		this.cmpNameBookKepper = cmpNameBookKepper;
		this.cmpSalaryDay = cmpSalaryDay;
		this.cmpLastEmpNbr = cmpLastEmpNbr;
		this.cmpState = cmpState;
	}

	@Caption("CmpId")
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "cmpId", unique = true, nullable = false)
	public long getCmpId() {
		return this.cmpId;
	}

	public void setCmpId(long cmpId) {
		this.cmpId = cmpId;
	}

	@Caption("CmpName")
	@Column(name = "cmpName", columnDefinition = "nvarchar")
	public String getCmpName() {
		return this.cmpName;
	}

	public void setCmpName(String cmpName) {
		this.cmpName = cmpName;
	}

	@Caption("CmpAddress")
	@Column(name = "cmpAddress", columnDefinition = "nvarchar")
	public String getCmpAddress() {
		return this.cmpAddress;
	}

	public void setCmpAddress(String cmpAddress) {
		this.cmpAddress = cmpAddress;
	}

	@Caption("CmpZip")
	@Column(name = "cmpZip")
	public Integer getCmpZip() {
		return this.cmpZip;
	}

	public void setCmpZip(Integer cmpZip) {
		this.cmpZip = cmpZip;
	}

	@Caption("CmpPlace")
	@Column(name = "cmpPlace", columnDefinition = "nvarchar")
	public String getCmpPlace() {
		return this.cmpPlace;
	}

	public void setCmpPlace(String cmpPlace) {
		this.cmpPlace = cmpPlace;
	}

	@Caption("CmpVatcode")
	@Column(name = "cmpVatcode", columnDefinition = "nvarchar")
	public String getCmpVatcode() {
		return this.cmpVatcode;
	}

	public void setCmpVatcode(String cmpVatcode) {
		this.cmpVatcode = cmpVatcode;
	}

	@Caption("CmpCurrency")
	@Column(name = "cmpCurrency", columnDefinition = "nvarchar")
	public String getCmpCurrency() {
		return this.cmpCurrency;
	}

	public void setCmpCurrency(String cmpCurrency) {
		this.cmpCurrency = cmpCurrency;
	}

	@Caption("CmpUid")
	@Column(name = "cmpUid", columnDefinition = "nvarchar")
	public String getCmpUid() {
		return this.cmpUid;
	}

	public void setCmpUid(String cmpUid) {
		this.cmpUid = cmpUid;
	}

	@Caption("CmpPhone")
	@Column(name = "cmpPhone", columnDefinition = "nvarchar")
	public String getCmpPhone() {
		return this.cmpPhone;
	}

	public void setCmpPhone(String cmpPhone) {
		this.cmpPhone = cmpPhone;
	}

	@Caption("CmpMail")
	@Column(name = "cmpMail", columnDefinition = "nvarchar")
	public String getCmpMail() {
		return this.cmpMail;
	}

	public void setCmpMail(String cmpMail) {
		this.cmpMail = cmpMail;
	}

	@Caption("CmpComm1")
	@Column(name = "cmpComm1", columnDefinition = "nvarchar")
	public String getCmpComm1() {
		return this.cmpComm1;
	}

	public void setCmpComm1(String cmpComm1) {
		this.cmpComm1 = cmpComm1;
	}

	@Caption("CmpBusiness")
	@Column(name = "cmpBusiness", columnDefinition = "nvarchar")
	public String getCmpBusiness() {
		return this.cmpBusiness;
	}

	public void setCmpBusiness(String cmpBusiness) {
		this.cmpBusiness = cmpBusiness;
	}

	@Caption("CmpRemark")
	@Column(name = "cmpRemark", columnDefinition = "nvarchar")
	public String getCmpRemark() {
		return this.cmpRemark;
	}

	public void setCmpRemark(String cmpRemark) {
		this.cmpRemark = cmpRemark;
	}

	@Caption("CmpJapsperServer")
	@Column(name = "cmpJapsperServer", columnDefinition = "nvarchar")
	public String getCmpJapsperServer() {
		return this.cmpJapsperServer;
	}

	public void setCmpJapsperServer(String cmpJapsperServer) {
		this.cmpJapsperServer = cmpJapsperServer;
	}

	@Caption("CmpJasperUri")
	@Column(name = "cmpJasperUri", columnDefinition = "nchar")
	public String getCmpJasperUri() {
		return this.cmpJasperUri;
	}

	public void setCmpJasperUri(String cmpJasperUri) {
		this.cmpJasperUri = cmpJasperUri;
	}

	@Caption("CmpReportUsr")
	@Column(name = "cmpReportUsr", columnDefinition = "nvarchar")
	public String getCmpReportUsr() {
		return this.cmpReportUsr;
	}

	public void setCmpReportUsr(String cmpReportUsr) {
		this.cmpReportUsr = cmpReportUsr;
	}

	@Caption("CmpReportPwd")
	@Column(name = "cmpReportPwd", columnDefinition = "nvarchar")
	public String getCmpReportPwd() {
		return this.cmpReportPwd;
	}

	public void setCmpReportPwd(String cmpReportPwd) {
		this.cmpReportPwd = cmpReportPwd;
	}

	@Caption("CmpAhvnumber")
	@Column(name = "cmpAhvnumber", columnDefinition = "nvarchar")
	public String getCmpAhvnumber() {
		return this.cmpAhvnumber;
	}

	public void setCmpAhvnumber(String cmpAhvnumber) {
		this.cmpAhvnumber = cmpAhvnumber;
	}

	@Caption("CmpNameBookKepper")
	@Column(name = "cmpNameBookKepper", columnDefinition = "nvarchar")
	public String getCmpNameBookKepper() {
		return this.cmpNameBookKepper;
	}

	public void setCmpNameBookKepper(String cmpNameBookKepper) {
		this.cmpNameBookKepper = cmpNameBookKepper;
	}

	@Caption("CmpSalaryDay")
	@Column(name = "cmpSalaryDay")
	public Short getCmpSalaryDay() {
		return this.cmpSalaryDay;
	}

	public void setCmpSalaryDay(Short cmpSalaryDay) {
		this.cmpSalaryDay = cmpSalaryDay;
	}

	@Caption("CmpLastEmpNbr")
	@Column(name = "cmpLastEmpNbr")
	public Integer getCmpLastEmpNbr() {
		return this.cmpLastEmpNbr;
	}

	public void setCmpLastEmpNbr(Integer cmpLastEmpNbr) {
		this.cmpLastEmpNbr = cmpLastEmpNbr;
	}

	@Caption("CmpState")
	@Column(name = "cmpState", nullable = false)
	public LovState.State getCmpState() {
		return this.cmpState;
	}

	public void setCmpState(LovState.State cmpState) {
		this.cmpState = cmpState;
	}

}
