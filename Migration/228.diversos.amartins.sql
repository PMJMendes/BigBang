/** 207 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'EB9E9AAA-A33B-4D75-B3FF-9EE9011B7A0A' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=207 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex, DateOfBirth,
ClientNumberI, InsurerIDI)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName, p.PK FKPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL FiscalNumberI, NULL FKSex, NULL DateOfBirth, NULL ClientNumberI, NULL InsurerIDI
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='EB9E9AAA-A33B-4D75-B3FF-9EE9011B7A0A';

/** 607 Empresas, Condomínios e não identificados **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'D4A29EF9-6D11-40C7-A87E-9F340153906C' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join amartins..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=607 and (cx.tipo_c is null or cx.tipo_c in ('E', 'C'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '5690C6FF-ED43-4485-AC84-9F96015497EF' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D4A29EF9-6D11-40C7-A87E-9F340153906C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value, p.PK FKPolicy,
'EB648FF6-DCB7-4DED-8DB6-9F960154A91A' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D4A29EF9-6D11-40C7-A87E-9F340153906C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto1 Value, p.PK FKPolicy, '5A6E803B-FAE3-4505-AB95-9F960154BB66' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D4A29EF9-6D11-40C7-A87E-9F340153906C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '85FAECC2-F488-497D-89FC-9F96015506F1' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D4A29EF9-6D11-40C7-A87E-9F340153906C';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7D4087C5-2FA4-494A-99AE-9F96015518CB' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D4A29EF9-6D11-40C7-A87E-9F340153906C';

/** 607 Individuais **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join amartins..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=607 and cx.tipo_c='I'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, 'A86A00EB-940D-4666-B4D8-9F96015549CA' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value, p.PK FKPolicy,
'2BB9ADF9-77E3-4AB4-AC3F-9F9601559BBE' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto1 Value, p.PK FKPolicy, '34D3F54E-783F-4391-A3FA-9F960155AADD' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '4828A7A7-1747-465F-9AE9-9F960155DDD6' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7ADB3D52-CCC6-4E60-95CE-9F960155F017' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6';

/** 398 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'E91E32C6-0C55-4F49-BA88-9F340153FD6D' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=398 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel, EquipmentDescription,
FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
o.NumObjecto ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType, NULL Address1, NULL Address2, NULL FKZipCode,
o.ValidoDesde InclusionDate, o.ValidoAte ExclusionDate, o.Texto1 MakeAndModel, NULL EquipmentDescription, NULL FirstRegistryDate, NULL ManufactureYear,
o.NumObjecto ClientIDE, NULL InsurerIDE
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.objectos o on o.cliente=s.cliente and o.apolice=s.apolice and o.comseg=s.comseg and o.ramo=s.ramo
where o.validoate is null or o.validoate>'2010-12-31'
and p.FKSubLine='E91E32C6-0C55-4F49-BA88-9F340153FD6D';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
replace(ox.Texto2, '.', ',') Value, p.PK FKPolicy, 'BF80A9FC-AD3F-4630-8DD3-9F9601565146' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins..empresa.objectos ox on ox.cliente=s.cliente and ox.apolice=s.apolice and ox.comseg=s.comseg and ox.ramo=s.ramo
and CAST(ox.NumObjecto AS NVARCHAR(50))=o.ObjName
where p.FKSubLine='E91E32C6-0C55-4F49-BA88-9F340153FD6D';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ox.Texto3 Value, p.PK FKPolicy, '64883643-ED00-498E-82D4-9F960156930B' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins..empresa.objectos ox on ox.cliente=s.cliente and ox.apolice=s.apolice and ox.comseg=s.comseg and ox.ramo=s.ramo
and CAST(ox.NumObjecto AS NVARCHAR(50))=o.ObjName
where p.FKSubLine='E91E32C6-0C55-4F49-BA88-9F340153FD6D';

/** 604 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '7E51C1B4-33F3-41D8-92D6-9EE9011B8221' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=604 and s.situacao in ('P', 'N')
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '52AA32B2-CE11-4B6B-BE3E-9F9100FB7D34' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1AFF5FC4-BD46-47A1-A8F1-9F9100FB8C44' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7DB2A784-8EC3-491F-B601-9F9100FB9F9C' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BE2F0D11-66D9-44CE-8396-9F9100FB85E5' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D4CC7608-056B-4D25-B20E-9F9100FB94F5' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, Species, Race, Age,
CityNumber, EIDTag)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName, p.PK FKPolicy, '7A9A0E31-668A-4113-A03E-9F9501403E6E' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL Species, NULL Race, NULL Age, NULL CityNumber, NULL EIDTag
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2D0CD0D6-4DBF-4C89-898C-A06800E91BF2' and c.BPresent=1
and p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '55590901-142F-4A77-BFB4-9F960156D3EE' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, null) Value,
p.PK FKPolicy, 'C43A6CE0-D5E5-4C4D-BEDA-9F960156BB65' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FC9D58DE-DDEC-48FA-9BFF-A06800E8FF93' and c.BPresent=1
and p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='152D3E43-B4D7-4040-848A-A06800E9327F' and c.BPresent=1
and p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='16B5C1A2-C586-404A-8529-A06800E94B82' and c.BPresent=1
and p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='25D9F7E6-923F-4BBC-BC34-A06800E97128' and c.BPresent=1
and p.FKSubLine='7E51C1B4-33F3-41D8-92D6-9EE9011B8221';

/** 208 Empresas, Condomínios e não identificados **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'EB87C2F0-7161-4715-81EA-9F340153E524' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join amartins..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=208 and (cx.tipo_c is null or cx.tipo_c in ('E', 'C'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '1E6CA07C-70A2-4D44-B53D-9F960156F30D' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='EB87C2F0-7161-4715-81EA-9F340153E524';

/** 208 Individuais **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'A56384EE-3F17-42E7-9B2C-9EE9011B7DC7' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join amartins..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=208 and cx.tipo_c='I'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '6A50F969-A705-4333-B2F0-9FE200C97061' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='A56384EE-3F17-42E7-9B2C-9EE9011B7DC7';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '959E987D-1B4F-4BDE-A792-9F9601573360' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='A56384EE-3F17-42E7-9B2C-9EE9011B7DC7';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '47805477-2AA0-4682-B083-9F9601572380' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='A56384EE-3F17-42E7-9B2C-9EE9011B7DC7';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '304B704B-9F51-4612-B0C1-A06800EF4D57' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='A56384EE-3F17-42E7-9B2C-9EE9011B7DC7';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '26BD8B84-7B72-4834-8B53-9F96015716BD' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
and p.FKSubLine='A56384EE-3F17-42E7-9B2C-9EE9011B7DC7';
