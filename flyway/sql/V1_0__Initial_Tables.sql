/****
 *   Inital Setup for Salary (SeicentoBookit)
 */

/****** Object:  Table [dbo].[Bank]    Script Date: 27.03.2019 17:08:11 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

IF NOT (EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Bank'))
BEGIN

	CREATE TABLE [dbo].[Bank](
		[bnkId] [bigint] IDENTITY(1,1) NOT NULL,
		[bnkName] [nvarchar](50) NOT NULL,
		[bnkAddress] [nvarchar](50) NULL,
		[bnkZip] [nvarchar](50) NULL,
		[bnkPlace] [nvarchar](50) NOT NULL,
		[bnkSwift] [nvarchar](50) NULL,
		[bnkClearing] [nvarchar](50) NULL,
		[bnkState] [smallint] NOT NULL,
	 CONSTRAINT [PK_Bank] PRIMARY KEY CLUSTERED 
	(
		[bnkId] ASC
	)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
	) ON [PRIMARY]
END
GO

/****** Object:  Table [dbo].[WorkRole]    Script Date: 27.03.2019 17:21:33 ******/

CREATE TABLE [dbo].[WorkRole](
	[wrrId] [bigint] IDENTITY(1,1) NOT NULL,
	[wrrName] [nvarchar](50) NOT NULL,
	[wrrState] [smallint] NOT NULL,
 CONSTRAINT [PK_WorkRole] PRIMARY KEY CLUSTERED 
(
	[wrrId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO


/****** Object:  Table [dbo].[Company]    Script Date: 27.03.2019 17:13:28 ******/
IF NOT (EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'Company'))
BEGIN
	CREATE TABLE [dbo].[Company](
		[cmpId] [bigint] IDENTITY(1,1) NOT NULL,
		[cmpName] [nvarchar](40) NULL,
		[cmpAddress] [nvarchar](40) NULL,
		[cmpZip] [int] NULL,
		[cmpPlace] [nvarchar](40) NULL,
		[cmpVatcode] [nvarchar](50) NULL,
		[cmpCurrency] [nvarchar](5) NULL,
		[cmpUid] [nvarchar](50) NULL,
		[cmpPhone] [nvarchar](50) NULL,
		[cmpMail] [nvarchar](50) NULL,
		[cmpComm1] [nvarchar](50) NULL,
		[cmpBusiness] [nvarchar](50) NULL,
		[cmpRemark] [nvarchar](50) NULL,
		[cmpJapsperServer] [nvarchar](128) NULL,
		[cmpJasperUri] [nchar](256) NULL,
		[cmpReportUsr] [nvarchar](20) NULL,
		[cmpReportPwd] [nvarchar](50) NULL,
		[cmpAhvnumber] [nvarchar](50) NULL,
		[cmpNameBookKepper] [nvarchar](50) NULL,
		[cmpSalaryDay] [smallint] NULL,
		[cmpLastEmpNbr] [int] NULL,
		[cmpState] [smallint] NOT NULL,
	 CONSTRAINT [PK__Company__745E20780425A277] PRIMARY KEY CLUSTERED 
	(
		[cmpId] ASC
	)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
	) ON [PRIMARY]
	
	ALTER TABLE [dbo].[Company] ADD  CONSTRAINT [DF_Company_cmpState]  DEFAULT ((0)) FOR [cmpState]
END
GO

/****** Object:  Table [dbo].[CostAccount]    Script Date: 27.03.2019 17:15:03 ******/

IF NOT (EXISTS (SELECT * 
                 FROM INFORMATION_SCHEMA.TABLES 
                 WHERE TABLE_SCHEMA = 'dbo' 
                 AND  TABLE_NAME = 'CostAccount'))
BEGIN
	CREATE TABLE [dbo].[CostAccount](
		[csaId] [bigint] IDENTITY(1,1) NOT NULL,
		[csaCode] [nvarchar](5) NULL,
		[csaName] [nvarchar](50) NULL,
		[csacsaId] [bigint] NULL,
		[csaState] [smallint] NULL,
	 CONSTRAINT [PK_CostAccount] PRIMARY KEY CLUSTERED 
	(
		[csaId] ASC
	)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
	) ON [PRIMARY]
	
	ALTER TABLE [dbo].[CostAccount]  WITH CHECK ADD  CONSTRAINT [FK_CostAccount_CostAccount] FOREIGN KEY([csacsaId])
	REFERENCES [dbo].[CostAccount] ([csaId])
	
	ALTER TABLE [dbo].[CostAccount] CHECK CONSTRAINT [FK_CostAccount_CostAccount]
END
GO

/****** Object:  Table [dbo].[DatabaseVersion]    Script Date: 27.03.2019 17:16:44 ******/

CREATE TABLE [dbo].[DatabaseVersion](
	[dbvId] [bigint] IDENTITY(0,1) NOT NULL,
	[dbvMajor] [int] NULL,
	[dbvMinor] [int] NULL,
	[dbvMicro] [nvarchar](40) NULL,
	[dbvReleased] [datetime] NULL,
	[dbvDescription] [ntext] NULL,
	[dbvState] [smallint] NULL,
 CONSTRAINT [PK_DatabaseVersion] PRIMARY KEY CLUSTERED 
(
	[dbvId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

/****** Object:  Table [dbo].[SalaryBvgBase]    Script Date: 27.03.2019 17:20:21 ******/

CREATE TABLE [dbo].[SalaryBvgBase](
	[sbeId] [bigint] IDENTITY(1,1) NOT NULL,
	[sbeName] [nvarchar](50) NOT NULL,
	[sbeCoordinationAmt] [numeric](10, 2) NOT NULL,
	[sbeSalarydefAmt] [numeric](10, 2) NOT NULL,
	[sbeCompany18] [numeric](6, 4) NULL,
	[sbeWorker18] [numeric](6, 4) NULL,
	[sbeCompany24] [numeric](6, 4) NULL,
	[sbeWorker24] [numeric](6, 4) NULL,
	[sbeCompany44] [numeric](6, 4) NULL,
	[sbeWorker44] [numeric](6, 4) NULL,
	[sbeCompany65] [numeric](6, 4) NULL,
	[sbeWorker65] [numeric](6, 4) NULL,
	[sbeState] [smallint] NOT NULL,
 CONSTRAINT [PK_SalaryBvgBase] PRIMARY KEY CLUSTERED 
(
	[sbeId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[SalaryBvgBaseLine]    Script Date: 27.03.2019 17:20:33 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SalaryBvgBaseLine](
	[sbxId] [bigint] IDENTITY(1,1) NOT NULL,
	[sbxValidFrom] [date] NOT NULL,
	[sbxAgeStartYear] [int] NOT NULL,
	[sbxCompany] [numeric](6, 4) NOT NULL,
	[sbxWorker] [numeric](6, 4) NOT NULL,
	[sbxsbeId] [bigint] NOT NULL,
	[sbxState] [smallint] NOT NULL,
 CONSTRAINT [PK_SalaryBvgBaseLine] PRIMARY KEY CLUSTERED 
(
	[sbxId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SalaryBvgBaseLine]  WITH CHECK ADD  CONSTRAINT [FK_SalaryBvgBaseLine_SalaryBvgBase] FOREIGN KEY([sbxsbeId])
REFERENCES [dbo].[SalaryBvgBase] ([sbeId])
GO

ALTER TABLE [dbo].[SalaryBvgBaseLine] CHECK CONSTRAINT [FK_SalaryBvgBaseLine_SalaryBvgBase]
GO

/****** Object:  Table [dbo].[SalaryCalculation]    Script Date: 27.03.2019 17:20:46 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SalaryCalculation](
	[sleId] [bigint] IDENTITY(1,1) NOT NULL,
	[sleName] [nvarchar](50) NULL,
	[sleDescription] [nvarchar](256) NULL,
	[sleFactorAhv] [numeric](8, 4) NULL,
	[sleFactorAlv] [numeric](8, 4) NULL,
	[sleFactorKtg] [numeric](8, 4) NULL,
	[sleFactorFak] [numeric](8, 4) NULL,
	[sleFactorAdmin] [numeric](8, 4) NULL,
	[sleFactorSol] [numeric](8, 4) NULL,
	[sleCoordinationAlv] [numeric](10, 2) NULL,
	[sleSldLowerBoundry] [numeric](10, 2) NULL,
	[sleSldUpperBoundry] [numeric](10, 2) NULL,
	[sleState] [smallint] NOT NULL,
 CONSTRAINT [PK_SalaryCalculation] PRIMARY KEY CLUSTERED 
(
	[sleId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[SalaryCalculationLine]    Script Date: 27.03.2019 17:21:01 ******/

CREATE TABLE [dbo].[SalaryCalculationLine](
	[slxId] [bigint] IDENTITY(1,1) NOT NULL,
	[slxValidFrom] [date] NOT NULL,
	[slxFactorAhv] [numeric](8, 4) NOT NULL,
	[slxFactorAlv] [numeric](8, 4) NOT NULL,
	[slxFactorKtg] [numeric](8, 4) NOT NULL,
	[slxFactorFak] [numeric](8, 4) NOT NULL,
	[slxFactorAdmin] [numeric](8, 4) NOT NULL,
	[slxFactorSol] [numeric](8, 4) NOT NULL,
	[slxCoordinationAlv] [numeric](10, 2) NOT NULL,
	[slxSldLowerBoundry] [numeric](10, 2) NOT NULL,
	[slxSldUpperBoundry] [numeric](10, 2) NOT NULL,
	[slxsleId] [bigint] NOT NULL,
	[slxState] [smallint] NOT NULL,
 CONSTRAINT [PK_SalaryCalculationLine] PRIMARY KEY CLUSTERED 
(
	[slxId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SalaryCalculationLine]  WITH CHECK ADD  CONSTRAINT [FK_SalaryCalculationLine_SalaryCalculationLine] FOREIGN KEY([slxsleId])
REFERENCES [dbo].[SalaryCalculation] ([sleId])
GO

ALTER TABLE [dbo].[SalaryCalculationLine] CHECK CONSTRAINT [FK_SalaryCalculationLine_SalaryCalculationLine]
GO


/****** Object:  Table [dbo].[Employee]    Script Date: 27.03.2019 17:17:07 ******/

CREATE TABLE [dbo].[Employee](
	[empId] [bigint] IDENTITY(1,1) NOT NULL,
	[empNumber] [bigint] NOT NULL,
	[empFirstName] [nvarchar](50) NULL,
	[empLastName] [nvarchar](50) NOT NULL,
	[empAddress] [nvarchar](50) NULL,
	[empZip] [nvarchar](50) NULL,
	[empPlace] [nvarchar](50) NULL,
	[empAhvnbr] [nvarchar](50) NULL,
	[empStartwork] [date] NOT NULL,
	[empEndWork] [date] NULL,
	[empCostAccount] [nvarchar](10) NOT NULL,
	[empBirthday] [date] NOT NULL,
	[empCivilState] [smallint] NULL,
	[empNbrOfKids] [smallint] NULL,
	[empKidsAddition] [numeric](8, 2) NOT NULL,
	[empSourceTax] [bit] NULL,
	[empBaseSalary] [numeric](8, 2) NULL,
	[empBankAccount] [nvarchar](50) NULL,
	[empBankIban] [nvarchar](50) NULL,
	[empbnkId] [bigint] NOT NULL,
	[empsleId] [bigint] NOT NULL,
	[empsbeId] [bigint] NOT NULL,
	[empwrrId] [bigint] NOT NULL,
	[empState] [smallint] NOT NULL,
	[empAhvnbrold] [nvarchar](50) NULL,
	[empAmtFrmRep] [numeric](8, 2) NULL,
	[empAmtFrmCar] [numeric](8, 2) NULL,
	[empAmtFrmJourney] [numeric](8, 2) NULL,
	[empFrmRemark] [nvarchar](128) NULL,
 CONSTRAINT [PK_Employee] PRIMARY KEY CLUSTERED 
(
	[empId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Employee]  WITH CHECK ADD  CONSTRAINT [FK_Employee_Bank] FOREIGN KEY([empbnkId])
REFERENCES [dbo].[Bank] ([bnkId])
GO

ALTER TABLE [dbo].[Employee] CHECK CONSTRAINT [FK_Employee_Bank]
GO

ALTER TABLE [dbo].[Employee]  WITH CHECK ADD  CONSTRAINT [FK_Employee_SalaryBvgBase] FOREIGN KEY([empsbeId])
REFERENCES [dbo].[SalaryBvgBase] ([sbeId])
GO

ALTER TABLE [dbo].[Employee] CHECK CONSTRAINT [FK_Employee_SalaryBvgBase]
GO

ALTER TABLE [dbo].[Employee]  WITH CHECK ADD  CONSTRAINT [FK_Employee_SalaryCalculation] FOREIGN KEY([empsleId])
REFERENCES [dbo].[SalaryCalculation] ([sleId])
GO

ALTER TABLE [dbo].[Employee] CHECK CONSTRAINT [FK_Employee_SalaryCalculation]
GO

ALTER TABLE [dbo].[Employee]  WITH CHECK ADD  CONSTRAINT [FK_Employee_WorkRole] FOREIGN KEY([empwrrId])
REFERENCES [dbo].[WorkRole] ([wrrId])
GO

ALTER TABLE [dbo].[Employee] CHECK CONSTRAINT [FK_Employee_WorkRole]
GO


/****** Object:  Table [dbo].[Entity]    Script Date: 27.03.2019 17:17:24 ******/

CREATE TABLE [dbo].[Entity](
	[entId] [bigint] IDENTITY(0,1) NOT NULL,
	[entName] [nvarchar](40) NULL,
	[entAbbreviation] [nvarchar](6) NULL,
	[entDataclass] [nvarchar](256) NULL,
	[entHasrowobject] [bit] NULL,
	[entReadonly] [bit] NULL,
	[entExport2sdf] [bit] NULL,
	[entSdfOrdinal] [smallint] NULL,
	[entAuditHistory] [smallint] NULL,
	[entType] [int] NULL,
	[entState] [smallint] NULL,
 CONSTRAINT [PK_Entity] PRIMARY KEY NONCLUSTERED 
(
	[entId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO


/****** Object:  Table [dbo].[Language]    Script Date: 27.03.2019 17:18:00 ******/

CREATE TABLE [dbo].[Language](
	[lngId] [bigint] IDENTITY(0,1) NOT NULL,
	[lngCode] [int] NOT NULL,
	[lngName] [nvarchar](40) NOT NULL,
	[lngIsocode] [nvarchar](4) NULL,
	[lngKeyboard] [nvarchar](40) NULL,
	[lngDefault] [bit] NULL,
	[lngState] [smallint] NULL,
 CONSTRAINT [PK_Language] PRIMARY KEY NONCLUSTERED 
(
	[lngId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[RowObject]    Script Date: 27.03.2019 17:18:38 ******/
CREATE TABLE [dbo].[RowObject](
	[objId] [bigint] IDENTITY(0,1) NOT NULL,
	[objentId] [bigint] NOT NULL,
	[objRowId] [bigint] NOT NULL,
	[objChngcnt] [bigint] NULL,
	[objState] [smallint] NULL,
	[objAdded] [datetime] NULL,
	[objAddedBy] [nvarchar](30) NULL,
	[objChanged] [datetime] NULL,
	[objChangedBy] [nvarchar](30) NULL,
	[objDeleted] [datetime] NULL,
	[objDeletedBy] [nvarchar](30) NULL,
	[objdbvId] [bigint] NULL,
 CONSTRAINT [PK_RowObject] PRIMARY KEY NONCLUSTERED 
(
	[objId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowObject]  WITH CHECK ADD  CONSTRAINT [DatabaseVersion_RowObject] FOREIGN KEY([objdbvId])
REFERENCES [dbo].[DatabaseVersion] ([dbvId])
GO

ALTER TABLE [dbo].[RowObject] CHECK CONSTRAINT [DatabaseVersion_RowObject]
GO

ALTER TABLE [dbo].[RowObject]  WITH CHECK ADD  CONSTRAINT [Entity_RowObject] FOREIGN KEY([objentId])
REFERENCES [dbo].[Entity] ([entId])
GO

ALTER TABLE [dbo].[RowObject] CHECK CONSTRAINT [Entity_RowObject]
GO


/****** Object:  Table [dbo].[RowFile]    Script Date: 27.03.2019 17:18:12 ******/
CREATE TABLE [dbo].[RowFile](
	[rimId] [bigint] IDENTITY(0,1) NOT NULL,
	[rimobjId] [bigint] NOT NULL,
	[rimName] [nvarchar](max) NULL,
	[rimIcon] [varbinary](max) NULL,
	[rimFile] [varbinary](max) NULL,
	[rimState] [smallint] NULL,
	[rimMimetype] [nvarchar](60) NULL,
	[rimNumber] [int] NOT NULL,
	[rimType] [smallint] NOT NULL,
	[rimSize] [nvarchar](20) NULL,
 CONSTRAINT [PK_RowImage] PRIMARY KEY CLUSTERED 
(
	[rimId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowFile] ADD  CONSTRAINT [DF_RowImage_rimNumber]  DEFAULT ((1)) FOR [rimNumber]
GO

ALTER TABLE [dbo].[RowFile] ADD  CONSTRAINT [DF_RowImage_rimType]  DEFAULT ((0)) FOR [rimType]
GO

ALTER TABLE [dbo].[RowFile]  WITH CHECK ADD  CONSTRAINT [RowObject_RowImage] FOREIGN KEY([rimobjId])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowFile] CHECK CONSTRAINT [RowObject_RowImage]
GO

/****** Object:  Table [dbo].[RowLabel]    Script Date: 27.03.2019 17:18:23 ******/

CREATE TABLE [dbo].[RowLabel](
	[lblId] [bigint] IDENTITY(0,1) NOT NULL,
	[lblobjId] [bigint] NOT NULL,
	[lbllngId] [bigint] NOT NULL,
	[lblLabelShort] [nvarchar](10) NULL,
	[lblLabelLong] [nvarchar](40) NULL,
	[lblState] [smallint] NULL,
 CONSTRAINT [PK_RowLabel] PRIMARY KEY CLUSTERED 
(
	[lblId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowLabel]  WITH CHECK ADD  CONSTRAINT [Language_RowLabel] FOREIGN KEY([lbllngId])
REFERENCES [dbo].[Language] ([lngId])
GO

ALTER TABLE [dbo].[RowLabel] CHECK CONSTRAINT [Language_RowLabel]
GO

ALTER TABLE [dbo].[RowLabel]  WITH CHECK ADD  CONSTRAINT [RowObject_RowLabel] FOREIGN KEY([lblobjId])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowLabel] CHECK CONSTRAINT [RowObject_RowLabel]
GO

/****** Object:  Table [dbo].[RowParameter]    Script Date: 27.03.2019 17:18:48 ******/

CREATE TABLE [dbo].[RowParameter](
	[prmId] [bigint] IDENTITY(0,1) NOT NULL,
	[prmobjId] [bigint] NOT NULL,
	[prmGroup] [nvarchar](40) NULL,
	[prmSubGroup] [nvarchar](40) NULL,
	[prmKey] [nvarchar](20) NULL,
	[prmValue] [nvarchar](128) NULL,
	[prmValueType] [smallint] NULL,
	[prmState] [smallint] NULL,
	[prmParamType] [smallint] NULL,
	[prmLookupTable] [nvarchar](40) NULL,
	[prmVisible] [bit] NULL,
 CONSTRAINT [PK_RowParameter] PRIMARY KEY NONCLUSTERED 
(
	[prmId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowParameter]  WITH CHECK ADD  CONSTRAINT [RowObject_RowParameter] FOREIGN KEY([prmobjId])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowParameter] CHECK CONSTRAINT [RowObject_RowParameter]
GO

/****** Object:  Table [dbo].[RowRelation]    Script Date: 27.03.2019 17:19:00 ******/

CREATE TABLE [dbo].[RowRelation](
	[relId] [bigint] IDENTITY(0,1) NOT NULL,
	[relName] [nvarchar](40) NOT NULL,
	[relOrder] [int] NULL,
	[relDescription] [nvarchar](80) NULL,
	[relobjId_Source] [bigint] NOT NULL,
	[relobjId_Target] [bigint] NOT NULL,
	[relState] [smallint] NULL,
 CONSTRAINT [PK_RowRelation] PRIMARY KEY CLUSTERED 
(
	[relId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowRelation]  WITH CHECK ADD  CONSTRAINT [RowObject_RowRelation_Source] FOREIGN KEY([relobjId_Source])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowRelation] CHECK CONSTRAINT [RowObject_RowRelation_Source]
GO

ALTER TABLE [dbo].[RowRelation]  WITH CHECK ADD  CONSTRAINT [RowObject_RowRelation_Target] FOREIGN KEY([relobjId_Target])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowRelation] CHECK CONSTRAINT [RowObject_RowRelation_Target]
GO

/****** Object:  Table [dbo].[RowSecurity]    Script Date: 27.03.2019 17:19:10 ******/

CREATE TABLE [dbo].[RowSecurity](
	[secId] [bigint] IDENTITY(0,1) NOT NULL,
	[secobjId] [bigint] NOT NULL,
	[secType] [int] NOT NULL,
	[secValidfrom] [datetime] NULL,
	[secValidto] [nvarchar](40) NULL,
	[secState] [smallint] NOT NULL,
	[secPermissionKey] [nvarchar](40) NULL,
 CONSTRAINT [PK_RowSecurity] PRIMARY KEY CLUSTERED 
(
	[secId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowSecurity] ADD  CONSTRAINT [DEF_RowSecurity_secType]  DEFAULT ((0)) FOR [secType]
GO

ALTER TABLE [dbo].[RowSecurity] ADD  CONSTRAINT [DEF_RowSecurity_secState]  DEFAULT ((0)) FOR [secState]
GO

ALTER TABLE [dbo].[RowSecurity]  WITH CHECK ADD  CONSTRAINT [RowObject_RowSecurity] FOREIGN KEY([secobjId])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowSecurity] CHECK CONSTRAINT [RowObject_RowSecurity]
GO

/****** Object:  Table [dbo].[RowText]    Script Date: 27.03.2019 17:19:42 ******/
CREATE TABLE [dbo].[RowText](
	[txtId] [bigint] IDENTITY(0,1) NOT NULL,
	[txtobjId] [bigint] NOT NULL,
	[txtlngId] [bigint] NOT NULL,
	[txtNumber] [int] NULL,
	[txtFreetext] [ntext] NULL,
	[txtState] [smallint] NULL,
 CONSTRAINT [PK_RowText] PRIMARY KEY NONCLUSTERED 
(
	[txtId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

ALTER TABLE [dbo].[RowText]  WITH CHECK ADD  CONSTRAINT [Language_RowText] FOREIGN KEY([txtlngId])
REFERENCES [dbo].[Language] ([lngId])
GO

ALTER TABLE [dbo].[RowText] CHECK CONSTRAINT [Language_RowText]
GO

ALTER TABLE [dbo].[RowText]  WITH CHECK ADD  CONSTRAINT [RowObject_RowText] FOREIGN KEY([txtobjId])
REFERENCES [dbo].[RowObject] ([objId])
GO

ALTER TABLE [dbo].[RowText] CHECK CONSTRAINT [RowObject_RowText]
GO


/****** Object:  Table [dbo].[Salary]    Script Date: 27.03.2019 17:19:25 ******/

CREATE TABLE [dbo].[Salary](
	[slrId] [bigint] IDENTITY(1,1) NOT NULL,
	[slrempId] [bigint] NOT NULL,
	[slrYear] [int] NULL,
	[slrDate] [date] NULL,
	[slrRemark] [nvarchar](128) NULL,
	[slrSalaryBase] [numeric](8, 2) NOT NULL,
	[slrBaseAlv] [numeric](8, 2) NULL,
	[slrBaseBvg] [numeric](8, 2) NULL,
	[slrBaseSol] [numeric](8, 2) NULL,
	[slrKidsAdditon] [numeric](8, 2) NOT NULL,
	[slrSalaryNet] [numeric](8, 2) NOT NULL,
	[slrAmountAhv] [numeric](8, 2) NOT NULL,
	[slrAmountAlv] [numeric](8, 2) NOT NULL,
	[slrAmountBvg] [numeric](8, 2) NOT NULL,
	[slrAmountFak] [numeric](8, 2) NOT NULL,
	[slrAmountSol] [numeric](8, 2) NOT NULL,
	[slrAmountAdminfees] [numeric](8, 2) NOT NULL,
	[slrAmountSourceTax] [numeric](8, 2) NULL,
	[slrBirthAddon] [numeric](8, 2) NULL,
	[slrFactorAhv] [numeric](8, 4) NULL,
	[slrFactorAlv] [numeric](8, 4) NULL,
	[slrFactorFak] [numeric](8, 4) NULL,
	[slrFactorSol] [numeric](8, 4) NULL,
	[slrFactorAdmin] [numeric](8, 4) NULL,
	[slrAmountBvgEmp] [numeric](8, 2) NULL,
	[slrType] [smallint] NOT NULL,
	[slrState] [smallint] NOT NULL,
	[slrPayDate] [datetime] NULL,
 CONSTRAINT [PK_Salary] PRIMARY KEY CLUSTERED 
(
	[slrId] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Salary] ADD  CONSTRAINT [DF_Salary_slrBaseSol]  DEFAULT ((0)) FOR [slrBaseSol]
GO

ALTER TABLE [dbo].[Salary] ADD  CONSTRAINT [DF_Salary_slrAmountSol]  DEFAULT ((0)) FOR [slrAmountSol]
GO

ALTER TABLE [dbo].[Salary] ADD  CONSTRAINT [DF_Salary_slrType]  DEFAULT ((0)) FOR [slrType]
GO

ALTER TABLE [dbo].[Salary]  WITH CHECK ADD  CONSTRAINT [FK_Salary_Salary] FOREIGN KEY([slrempId])
REFERENCES [dbo].[Employee] ([empId])
GO

ALTER TABLE [dbo].[Salary] CHECK CONSTRAINT [FK_Salary_Salary]
GO










