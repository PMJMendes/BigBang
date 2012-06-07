/** 501 Empresas **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '3236E457-D202-48FF-9219-9EE9011B1E39' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=501 and cx.tipo_c='E'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4A5E2C8E-7830-491C-99A8-9F9101004D0A' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5E2F6DC1-5EE3-4B6A-8550-9F9101006355' FKCoverage,
case when (upper(left(ltrim(isnull(s.texto9, '')), 3)) in ('', 'NAO', 'NÃO', '0')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8AA9BE17-566F-4A6B-8A79-9F910100554C' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '50272739-401E-4D2B-95F8-9F9101005B76' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital7, '')), 3)) in ('', 'NAO', 'NÃO', '0')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6026C770-8E47-4943-B4F1-9F9101006DDF' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.risco6 MakeAndModel, s.texto2 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.risco5)=1 and cast(s.risco5 as integer)>1900 and cast(s.risco5 as integer)<2200 then cast(s.risco5 as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join bigbang.tblBBDeductibleTypes v on v.DeductibleType=case when s.risco8 like '%~%%' escape '~' then '%'
when s.risco8 like '%€%' then '€' else null end
where t.pk='C557698D-51CA-4B48-A06A-A06100C2F07F' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '306840CE-8F79-47CB-B289-9F96015C5B7A' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, 'D53F2140-88E1-47C8-874B-A069011131AE' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'BC3E0D27-5416-45ED-AA7D-9F96015C41D0' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BFAE6504-E2D3-4E79-8E5D-A06100C2A3F3' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0F3C3A34-07D1-45F9-93EA-A06100C2D660' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco8 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0A8E29FC-54C3-4D88-8A52-A06100C3035A' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital7 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DBDC3C2A-89AA-47A8-A47E-A06100C32C63' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BFE1FFB7-5718-4F83-B94B-A06100C33DB9' and c.BPresent=1
and p.FKSubLine='3236E457-D202-48FF-9219-9EE9011B1E39';

/** 501 Individuais, Condomínios e não identificados **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '54E688CE-8F22-4047-87F3-9EE9011B233A' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=501 and (cx.tipo_c is null or cx.tipo_c in ('I', 'C'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '64683E51-0576-45CA-A911-9F910100AD60' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CB837DAA-D439-468D-9231-9F910100BE71' FKCoverage,
case when (upper(left(ltrim(isnull(s.texto9, '')), 3)) in ('', 'NAO', 'NÃO', '0')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5A15DC72-34D7-4B94-AD15-9F910100B72F' FKCoverage, case
ltrim(isnull(s.capital1, ''))+ltrim(isnull(s.capital2, ''))+ltrim(isnull(s.capital3, ''))+ltrim(isnull(s.capital4, ''))+
ltrim(isnull(s.capital5, ''))+ltrim(isnull(s.capital6, ''))+ltrim(isnull(s.capital8, ''))+ltrim(isnull(s.capital9, ''))
when '' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '24C363F2-7643-4CA1-A01D-9F910100DBB6' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B2FB9290-FA6E-430E-A154-9F910100D2BA' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '93D65159-E2D6-47EA-B7B1-9F910100C87B' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital7, '')), 3)) in ('', 'NAO', 'NÃO', '0')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1286733B-7E4E-432D-9A1C-9F910100E354' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.risco6 MakeAndModel, s.texto2 EquipmentDescription, NULL FirstRegistryDate,
case when isnumeric(s.risco5)=1 and cast(s.risco5 as integer)>1900 and cast(s.risco5 as integer)<2200 then cast(s.risco5 as integer) else null end ManufactureYear,
NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
left outer join bigbang.tblBBDeductibleTypes v on v.DeductibleType=case when s.risco8 like '%~%%' escape '~' then '%'
when s.risco8 like '%€%' then '€' else null end
where t.pk='8566DA08-C6D5-4D86-959F-A06100C4C338' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A45BA48A-011B-47A7-851F-A06100C54543' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '0A8D1332-2808-4A0A-9D1E-9F96015CEAAD' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto8 Value, p.PK FKPolicy, '8A7452E9-D43A-4630-8513-9FE201025B5F' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)) Value,
p.PK FKPolicy, 'B1FF7F18-F403-44AA-909A-A069011161ED' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7682B27F-ABD7-451C-AF2B-9FE20102401D' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F77276FF-7867-4D7B-AE10-A06100C4804B' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1C2E1A4C-D871-4454-A9C4-A06100C4A69D' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco8 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='767B0CB3-A7C8-4E9B-92C3-A06100C4DBF7' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3C2403E0-7FCF-4EB3-96CB-A06100C4F81D' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E7B6FB37-5C42-413A-9866-A06100C5266A' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='40640DEC-36AD-4380-865B-A06100C55779' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital7 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='773F1543-970C-4FAA-9327-A06100C574A9' and c.BPresent=1
and p.FKSubLine='54E688CE-8F22-4047-87F3-9EE9011B233A';
