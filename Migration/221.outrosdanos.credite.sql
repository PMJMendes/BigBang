/** 606 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=606 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D1F2EE46-AD11-44BB-B814-9F910104E3FB' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '95502563-F604-49D6-89AE-9F910104F647' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
case ltrim(isnull(s.risco4, '')+' '+isnull(s.risco5, '')) when '' then null else ltrim(isnull(s.risco4, '')+' '+isnull(s.risco5, '')) end Address1,
NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate,
case ltrim(isnull(s.texto3, '')+' '+isnull(s.texto4, '')) when '' then null else ltrim(isnull(s.texto3, '')+' '+isnull(s.texto4, '')) end MakeAndModel,
s.texto1 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.texto2)=1 then cast(replace(s.texto2, '.', '') as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2FDC6551-1017-4D50-B7C5-A06800B6A989' and c.BPresent=1
and p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '6A9DFC80-25C7-4817-BA19-9F96016B9B3F' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco8 Value, p.PK FKPolicy, '5F586F50-5375-489F-BF28-9F96016BB39E' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, '5F586F50-5375-489F-BF28-9F96016BB39E' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A12AF1FE-A3F0-47D2-B6B0-A06800B6C074' and c.BPresent=1
and p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4D245D4E-F4A1-433D-824D-A06D00BFAA65' and c.BPresent=1
and p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D684D190-F8B8-4971-9413-A06D00BF8FF0' and c.BPresent=1
and p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='67CCEF68-FB50-486B-80B3-A06800B68C81' and c.BPresent=1
and p.FKSubLine='1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542';

/** 707 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'C77B46AF-B098-452D-914C-9EE9011A5198' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=707 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '411F6DEA-5067-4FF3-8F42-9F9101051595' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E273F5DD-4914-4115-8929-9F91010526BA' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F50A82D6-014C-409A-BE12-9F9101052E9D' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CA95F6A8-888D-4C16-B1B8-9F9101051E45' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
case ltrim(isnull(s.risco4, '')+' '+isnull(s.risco5, '')) when '' then null else ltrim(isnull(s.risco4, '')+' '+isnull(s.risco5, '')) end Address1,
NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL MakeAndModel,
s.texto1 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.texto3)=1 then case when cast(s.texto3 as integer)<100 then cast(s.texto3 as integer)+1900 else cast(s.texto3 as integer) end else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3183D5B8-6F73-4659-9E40-A06800B7B28F' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9CCED70E-1069-4E53-9091-A06800B7F590' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4DD53EDD-5465-413E-99FC-A06800B822EE' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FFD9EFA5-8E19-4746-B7B6-A06800B85905' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='34F84613-1D3F-4F9A-BC66-A06800B84349' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '4AB6A2BA-C4DF-4C07-97C5-9F96016C200D' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, 'C481A43C-11B1-4A86-96E1-9F96016C100A' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, '23CDCEEA-0F32-45A7-872F-9F96016C3548' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AF60905F-EEDB-4C67-BDBF-A06800B7C427' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ADA064D5-B4A4-45BD-8E11-A06D00C01C0C' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D6B0548A-4A92-4690-A986-A06D00C0006F' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D033586A-10A4-458A-B3B2-A06800B7955F' and c.BPresent=1
and p.FKSubLine='C77B46AF-B098-452D-914C-9EE9011A5198';

/** 306 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '252789C2-928E-467A-B081-9EE9011A2E76' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=306 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL MakeAndModel,
s.texto2 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.texto3)=1 then cast(s.texto3 as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='252789C2-928E-467A-B081-9EE9011A2E76';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end  Value,
p.PK FKPolicy, 'B987F99B-060F-45C0-AEA9-9F96016CE4AF' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='252789C2-928E-467A-B081-9EE9011A2E76';

/** 609 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=609 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DC8FCF25-39D3-4E21-A087-9F9101057A9A' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1B5C3ADC-CB7B-440B-AC0D-9F9101058320' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C4008DED-FA03-4218-8BA6-9F9101058A98' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate,
s.texto2 MakeAndModel, s.texto1 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.texto3)=1 and ltrim(s.texto3)!='0' then cast(s.texto3 as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='AC5E9448-3720-4F5B-9FD8-A06800BA2400' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4D1D85B6-3AD0-4866-9764-A06800BA702C' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '865C5326-BF12-4427-A65D-9F96016D579C' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '5FC757E4-CD4A-4317-871D-9F96016D465D' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, '931EC476-FAC9-4B65-BB3F-9F96016D69E6' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2DAF9C58-304D-4EC4-9683-A06800BA378E' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='06277EFE-EE03-4A3D-9D0D-A06D00C07EBE' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco2 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1D4B3515-0443-417B-8E8A-A06D00C05B1C' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='60D4498E-3BA9-474F-BADA-A06800B9F1FE' and c.BPresent=1
and p.FKSubLine='6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D';

/** 317 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'DBFA4951-59CF-4008-98A5-9EE9011A0F30' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=317 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7A56A12F-7CB1-4411-BCE9-9F910105D27F' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DA4381A8-A7E5-4C4C-8603-9F910105E4BE' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL MakeAndModel,
s.texto6 EquipmentDescription, NULL FirstRegistryDate, NULL ManufactureYear, NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Lucro Bruto' ItemValue, '2DFD5A5D-13D0-400E-B09A-9F96016E43FB' FKTaxAsSubList
where 'Lucro Bruto' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='2DFD5A5D-13D0-400E-B09A-9F96016E43FB');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Lucro Líquido' ItemValue, '2DFD5A5D-13D0-400E-B09A-9F96016E43FB' FKTaxAsSubList
where 'Lucro Líquido' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='2DFD5A5D-13D0-400E-B09A-9F96016E43FB');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Encargos Permanentes' ItemValue, '2DFD5A5D-13D0-400E-B09A-9F96016E43FB' FKTaxAsSubList
where 'Encargos Permanentes' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='2DFD5A5D-13D0-400E-B09A-9F96016E43FB');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7300A7FF-BD1F-4EB6-B54A-9F96016E56F3' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='91D6609D-F664-4AF4-A914-A06800BC8A22' and c.BPresent=1
and p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case when s.risco6 is not null and ltrim(s.risco6) not in ('', '0') then s.risco6
when s.risco5 is not null and ltrim(s.risco5) not in ('', '0') then '18'
when s.risco4 is not null and ltrim(s.risco4) not in ('', '0') then '12'
when s.risco3 is not null and ltrim(s.risco3) not in ('', '0') then '6'
when s.risco2 is not null and ltrim(s.risco2) not in ('', '0') then '3' else null end Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='70CACD62-303C-49FA-9F45-A06800BCA7E5' and c.BPresent=1
and p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, '8BA633B1-58FE-4891-99FB-9FE2011A93E5' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, '2DFD5A5D-13D0-400E-B09A-9F96016E43FB' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case
when s.texto1 like '%liq%' then 'Lucro Líquido'
when s.texto1 like '%brut%' then 'Lucro Bruto'
when s.texto1 like '%fixo%' or s.texto1 like '%perm%' then 'Encargos Permanentes' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2DFD5A5D-13D0-400E-B09A-9F96016E43FB')
and p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7659EC0F-F713-47A1-853E-A06800BC6ACB' and c.BPresent=1
and p.FKSubLine='DBFA4951-59CF-4008-98A5-9EE9011A0F30';

/** 307 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=307 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A14BBF94-1D70-4BB6-B726-9F910105F463' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E9454C1B-8471-4F03-9850-9F910105FED2' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '87DE3B22-6312-4710-AB5B-9F91010605F8' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.texto6 SiteDescription
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Lucro Bruto' ItemValue, '59FB662D-65B1-4FDF-A8C3-9F96016EA283' FKTaxAsSubList
where 'Lucro Bruto' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='59FB662D-65B1-4FDF-A8C3-9F96016EA283');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Lucro Líquido' ItemValue, '59FB662D-65B1-4FDF-A8C3-9F96016EA283' FKTaxAsSubList
where 'Lucro Líquido' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='59FB662D-65B1-4FDF-A8C3-9F96016EA283');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Encargos Permanentes' ItemValue, '59FB662D-65B1-4FDF-A8C3-9F96016EA283' FKTaxAsSubList
where 'Encargos Permanentes' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='59FB662D-65B1-4FDF-A8C3-9F96016EA283');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '587587FF-5775-401F-9336-9F96016EC1C4' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8597650B-EB3D-4246-9230-A06800BD7D4B' and c.BPresent=1
and p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case when s.risco6 is not null and ltrim(s.risco6) not in ('', '0') then s.risco6
when s.risco5 is not null and ltrim(s.risco5) not in ('', '0') then '18'
when s.risco4 is not null and ltrim(s.risco4) not in ('', '0') then '12'
when s.risco3 is not null and ltrim(s.risco3) not in ('', '0') then '6'
when s.risco2 is not null and ltrim(s.risco2) not in ('', '0') then '3' else null end Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E7096052-3E06-4FCC-9F0F-A06800BDAA6D' and c.BPresent=1
and p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A4439BBF-322E-449E-81EB-A06800BDCB18' and c.BPresent=1
and p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, 'B1447D4D-C8F0-4D1B-AAFB-9FE2011B1488' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, '59FB662D-65B1-4FDF-A8C3-9F96016EA283' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case
when s.texto1 like '%liq%' then 'Lucro Líquido'
when s.texto1 like '%brut%' then 'Lucro Bruto'
when s.texto1 like '%fixo%' or s.texto1 like '%perm%' then 'Encargos Permanentes' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='59FB662D-65B1-4FDF-A8C3-9F96016EA283')
and p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E0351268-1B82-474C-9DAA-A06800BD4A71' and c.BPresent=1
and p.FKSubLine='44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E';

/** 610 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'D38965BA-1275-4C36-BC89-9EE9011A3C6A' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=610 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C5990A03-287E-4C0F-B5A5-9F9101061DAC' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7472432E-521A-4A67-89AF-9F9101062600' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '18B0E9A7-8E94-48E8-AB50-9F9101062D86' FKCoverage,
case when ltrim(isnull(s.risco2, ''))='' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from credite_egs.tblBBPolicies p
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate,
case ltrim(isnull(s.texto3, '')+' '+isnull(s.texto4, '')) when '' then null else ltrim(isnull(s.texto3, '')+' '+isnull(s.texto4, '')) end MakeAndModel,
s.texto1 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.texto2)=1 then cast(replace(s.texto2, '.', '') as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7BB4F79A-1D87-4E6B-AC08-A06800BE5C6D' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='38762557-3DF8-4913-99AD-A06800BEDC2E' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'E5D67280-9789-4024-A1F4-9F96016F2FBF' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A0A9FB7D-1D83-4C22-9E75-9F96016F18E3' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, 'C28138CB-D03B-48E4-8D34-9F96016F42BC' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2335C008-11B0-427F-9CC2-A06800BEA4B1' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='13FBA37C-9FAF-4EF9-95EE-A06D00C0E359' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7349BCE3-CAFE-4617-A3CE-A06D00C0CB2B' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE1D4648-3E74-46FF-81D7-A06800BE3DF0' and c.BPresent=1
and p.FKSubLine='D38965BA-1275-4C36-BC89-9EE9011A3C6A';

/** 308 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '023C01EF-D4A7-441C-B241-9EE9011A2715' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=308 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Conteúdos' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL MakeAndModel,
s.texto1 EquipmentDescription, NULL FirstRegistryDate, NULL ManufactureYear, NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='023C01EF-D4A7-441C-B241-9EE9011A2715';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, '261297CE-FACB-4324-9C08-9F96016F7DC3' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='023C01EF-D4A7-441C-B241-9EE9011A2715';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, 2-ISNULL(s.pertaxa, 2)) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, 'D38B53D0-9E78-4E09-ABE8-9FE2011CA610' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='023C01EF-D4A7-441C-B241-9EE9011A2715';
