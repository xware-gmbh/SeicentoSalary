/****** Entity ****/            
IF NOT EXISTS (SELECT 1 FROM [dbo].[Entity] WHERE [entname] = 'Bank')  
BEGIN 
    INSERT INTO [dbo].[Entity]   
        ([entName]  
        ,[entAbbreviation]  
        ,[entHasrowobject]
        ,[entState])  
    VALUES 
        ('Bank', 'bnk' ,1,1),
        ('Company', 'cmp' ,1,1),
        ('CostAccount', 'csa' ,1,1),
        ('DatabaseVersion', 'dbv' ,1,1),
        ('Employee', 'emp' ,1,1),
        ('Entity', 'ent' ,1,1),
        ('Language', 'lng' ,1,1),
        ('ItemGroup', 'itg' ,1,1),
        ('RowFile', '' ,1,1),
        ('RowLabel', '' ,1,1),
        ('RowObject', '' ,0,1),
        ('RowParameter', '' ,1,1),
        ('RowRelation', '' ,1,1),
        ('RowSecurity', '' ,1,1),
        ('RowText', '' ,1,1),
        ('Salary', 'slr' ,1,1),
        ('SalaryBvgBase', 'sbe' ,1,1),
        ('SalaryBvgBaseLine', 'sbx' ,1,1),
        ('SalaryCalculation', 'sle' ,1,1),
        ('SalaryCalculationLine', 'slx' ,1,1),
        ('WorkRole', 'wrr' ,1,1)
 
END             



/****** Language ****/            
IF NOT EXISTS (SELECT 1 FROM [dbo].[Language] WHERE [lngCode] = 1)  
BEGIN 
    INSERT INTO [dbo].[Language]   
        ([lngCode]  
        ,[lngName]  
        ,[lngIsocode]  
        ,[lngKeyboard]  
        ,[lngDefault]  
        ,[lngState])  
    VALUES 
     (1, 'Deutsch', 'CH', 'de_ch', 1 ,1)
 
END

/****** Company ****/            
IF NOT EXISTS (SELECT 1 FROM [dbo].[Company] WHERE [cmpState] = 1)  
BEGIN 
    INSERT INTO [dbo].[Company]   
        ([cmpName]  
        ,[cmpState])  
    VALUES 
     ('Demo Firma', 1)
 
END

/****** Workrole ****/            
IF NOT EXISTS (SELECT 1 FROM [dbo].[Workrole] WHERE [wrrname] = 'Consultant')  
BEGIN 
    INSERT INTO [dbo].[Workrole]   
        ([wrrName]  
        ,[wrrState])  
    VALUES 
     ('Consultant', 1),
     ('Project-Manager', 1),
     ('Employee', 1)
     
END

/****** DatabaseVersion ****/
IF NOT EXISTS (SELECT 1 FROM [dbo].[DatabaseVersion] WHERE [dbvstate] = 1)  
BEGIN 
    INSERT INTO [dbo].[DatabaseVersion]   
        ([dbvMajor]  
        ,[dbvMinor]  
        ,[dbvState])  
    VALUES 
     (1, 0, 1),
     (1, 1, 1)
     
END
