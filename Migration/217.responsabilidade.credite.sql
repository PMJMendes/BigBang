/** 611 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'C199BF06-E397-454E-80ED-9EE9011AD3EC' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=611 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7C3B8068-9A52-4CBF-9C47-9F9101066573' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '131BB20A-A872-4BB7-9AD9-9F9101066CEF' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '25B368ED-B06C-4394-ABBB-9F9101067564' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DD23F5D9-1794-48E0-ACC6-9F9101065D8B' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '21F351D4-FB3E-44C7-B763-9F9101067CE5' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '11C289F7-01A6-4431-926D-9F91010685A0' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='C199BF06-E397-454E-80ED-9EE9011AD3EC';

/** 653 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '4AA42557-96B9-4EE5-8B55-9EE9011AE763' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=653 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, Species, Race, Age,
CityNumber, EIDTag)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName, p.PK FKPolicy, '7A9A0E31-668A-4113-A03E-9F9501403E6E' FKObjType,
s.texto2 Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL Species, NULL Race, NULL Age, NULL CityNumber, NULL EIDTag
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='4AA42557-96B9-4EE5-8B55-9EE9011AE763';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, 'D292FFEC-D494-45BB-89C3-9FE2011CFF8D' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='4AA42557-96B9-4EE5-8B55-9EE9011AE763';

/** 655 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BFD2ABF7-195B-4560-BC91-9F920158B876' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=655 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel,
EquipmentDescription, FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType, NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate,
NULL ExclusionDate, NULL MakeAndModel, s.texto1 EquipmentDescription, NULL FirstRegistryDate, NULL ManufactureYear, NULL ClientIDE, NULL InsurerIDE
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BFD2ABF7-195B-4560-BC91-9F920158B876';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, '935CE17E-FACA-47EE-88B2-9FE2011D4E4E' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BFD2ABF7-195B-4560-BC91-9F920158B876';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A53D9C4D-BE49-4B07-8FFC-9FE2011D34A3' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='BFD2ABF7-195B-4560-BC91-9F920158B876';

/** 205 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '5CA22680-438E-4C4E-8720-9EE9011AE247' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=205 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9C8C4A48-457D-490A-9743-9F910106BE15' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3E60ED38-BB1E-4653-8774-9F910106DE8E' FKCoverage,
case when s.texto4 is null and s.texto5 is null and s.texto6 is null and s.texto7 is null then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B9EC9B82-B9D1-49CF-8384-9F910106CCBD' FKCoverage,
case when s.texto8 is null then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '75A26D28-693A-475D-AF21-9F910106E574' FKCoverage, case
ltrim(isnull(s.risco7, ''))+ltrim(isnull(s.risco8, ''))+ltrim(isnull(s.risco9, ''))+ltrim(isnull(s.capital7, ''))+ltrim(isnull(s.capital8, ''))+ltrim(isnull(s.capital9, ''))
when '' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'FA44DF03-A826-4CC8-A198-9F910106C56B' FKCoverage, case
ltrim(isnull(s.risco1, ''))+ltrim(isnull(s.risco2, ''))+ltrim(isnull(s.risco3, ''))+ltrim(isnull(s.risco4, ''))+ltrim(isnull(s.risco5, ''))+ltrim(isnull(s.risco6, ''))+
ltrim(isnull(s.capital1, ''))+ltrim(isnull(s.capital2, ''))+ltrim(isnull(s.capital3, ''))+ltrim(isnull(s.capital4, ''))+ltrim(isnull(s.capital4, ''))+ltrim(isnull(s.capital5, ''))
when '' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3A2B6B56-3E2D-45C2-8158-9F910106ED2B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '37454DBD-9DFD-48CA-9D18-9F910106D662' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='59E3C0B2-03D1-44DF-8468-A06100B0E624' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6ED62082-6D39-43E6-B7D2-A06100B100D8' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case
case when isnumeric(s.capital7)=1 then cast(s.capital7 as float) else 0 end + case when isnumeric(s.capital8)=1 then cast(s.capital8 as float) else 0 end +
case when isnumeric(s.capital9)=1 then cast(s.capital9 as float) else 0 end
when 0 then null else
cast(case when isnumeric(s.capital7)=1 then cast(s.capital7 as float) else 0 end + case when isnumeric(s.capital8)=1 then cast(s.capital8 as float) else 0 end +
case when isnumeric(s.capital9)=1 then cast(s.capital9 as float) else 0 end as nvarchar(250))
end Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B2C8DB97-2717-4F81-AB2B-A06100B1682F' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case
case when ltrim(isnull(s.risco7, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco8, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco9, ''))='' then 0 else 1 end
when 0 then null else
cast(case when ltrim(isnull(s.risco7, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco8, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco9, ''))='' then 0 else 1 end as nvarchar(250))
end Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E217A429-F2C5-4104-BCA6-A06100B18758' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case
case when isnumeric(s.capital1)=1 then cast(s.capital1 as float) else 0 end + case when isnumeric(s.capital2)=1 then cast(s.capital2 as float) else 0 end +
case when isnumeric(s.capital3)=1 then cast(s.capital3 as float) else 0 end + case when isnumeric(s.capital4)=1 then cast(s.capital4 as float) else 0 end +
case when isnumeric(s.capital5)=1 then cast(s.capital5 as float) else 0 end + case when isnumeric(s.capital6)=1 then cast(s.capital6 as float) else 0 end
when 0 then null else
cast(case when isnumeric(s.capital1)=1 then cast(s.capital1 as float) else 0 end + case when isnumeric(s.capital2)=1 then cast(s.capital2 as float) else 0 end +
case when isnumeric(s.capital3)=1 then cast(s.capital3 as float) else 0 end + case when isnumeric(s.capital4)=1 then cast(s.capital4 as float) else 0 end +
case when isnumeric(s.capital5)=1 then cast(s.capital5 as float) else 0 end + case when isnumeric(s.capital6)=1 then cast(s.capital6 as float) else 0 end as nvarchar(250))
end Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='332AD900-2783-4726-A370-A06100B1ACFD' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case
case when ltrim(isnull(s.risco1, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco2, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco3, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco4, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco5, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco6, ''))='' then 0 else 1 end
when 0 then null else
cast(case when ltrim(isnull(s.risco1, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco2, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco3, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco4, ''))='' then 0 else 1 end +
case when ltrim(isnull(s.risco5, ''))='' then 0 else 1 end + case when ltrim(isnull(s.risco6, ''))='' then 0 else 1 end as nvarchar(250))
end Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3A39AA45-65B7-4701-BF04-A06100B1DA7C' and c.BPresent=1
and p.FKSubLine='5CA22680-438E-4C4E-8720-9EE9011AE247';

/** 670 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '85EA331B-094E-4BFC-B423-9EE9011AD85E' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=670 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

/** 601 Empresas, Condomínios e não identificados **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'C6EC530E-8C28-4EDB-BCE3-9EE9011AC699' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=601 and (cx.tipo_c is null or cx.tipo_c in ('C', 'E'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '2080F19E-7431-401D-9D4C-9F9601703CD2' FKTaxAsSubList
from (select distinct case when texto5 like '%fact%' then 'Facturação' when texto5 like '%sal%' then 'Salários' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='2080F19E-7431-401D-9D4C-9F9601703CD2');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, 'DFCAA549-4CD3-4B56-A020-9FE2011E5BE9' FKTaxAsSubList
from (select distinct case pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='DFCAA549-4CD3-4B56-A020-9FE2011E5BE9');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '79E271B8-8AFE-4E75-B9CC-9F9601702C73' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, '889AC7E5-8B40-4A3F-B725-9F960170910A' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, 'C372C03C-9B7A-41BC-A64E-9FE2011E750D' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '5598EADB-E9F3-4958-9514-9F9601707E4B' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa end as float) as nvarchar(250)), case s.texto4 when '' then null else s.texto4 end) Value,
p.PK FKPolicy, '873627F5-5598-4267-BACA-9F9601706653' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, 'DFCAA549-4CD3-4B56-A020-9FE2011E5BE9' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case s.pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='DFCAA549-4CD3-4B56-A020-9FE2011E5BE9')
and p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7D576D2C-0167-4A09-8E6F-9F960170527B' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, '2080F19E-7431-401D-9D4C-9F9601703CD2' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case when s.texto5 like '%fact%' then 'Facturação'
when s.texto5 like '%sal%' then 'Salários' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='2080F19E-7431-401D-9D4C-9F9601703CD2')
and p.FKSubLine='C6EC530E-8C28-4EDB-BCE3-9EE9011AC699';

/** 601 Individuais e 654 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '363ED4D5-26D9-4EDF-8123-9EE9011AEB24' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where ((s.ramo=601 and cx.tipo_c='I') or s.ramo=654)
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '92D9E834-6A84-44A3-84FA-9F910108C41E' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AC66AE95-7F18-4E7C-9311-9F910108D2F6' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '83AD45C6-A8D8-4CAC-91E5-9F910108CC98' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '63501542-FA18-4434-88E2-9F910108E065' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '0DA8AF01-DABB-4200-9524-9F910108ED25' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='027E2644-1898-4357-8BED-A06100B3027D' and c.BPresent=1
and p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='79E69745-A54D-4DC6-A15C-A06100B350CA' and c.BPresent=1
and p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='68A30598-76F1-4637-84EB-A06100B3338E' and c.BPresent=1
and p.FKSubLine='363ED4D5-26D9-4EDF-8123-9EE9011AEB24';

/** 652 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '533DA89B-1BEC-474B-B615-9EE9011ACF4F' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=652 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '7283A5CD-5E47-4D5D-8BD9-9F960170E080' FKTaxAsSubList
from (select distinct case when texto5 like '%fact%' then 'Facturação' when texto5 like '%sal%' then 'Salários' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='7283A5CD-5E47-4D5D-8BD9-9F960170E080');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, '4BA5FF8E-2644-4B27-96B0-9FE2011F02E8' FKTaxAsSubList
from (select distinct case pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='4BA5FF8E-2644-4B27-96B0-9FE2011F02E8');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, 'ACF3ED80-98ED-4384-A903-9F9601713C65' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, '8A05461D-EB27-46A0-89F5-9FE2011F2698' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '5FAB145A-6CA6-42DD-BE5D-9F9601711D4F' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '10B97833-7CFD-4BBB-B73D-9F960170CFCE' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa end as float) as nvarchar(250)), case s.texto4 when '' then null else s.texto4 end) Value,
p.PK FKPolicy, '4C62AE47-42E6-4A40-8EED-9F9601710AC7' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, '4BA5FF8E-2644-4B27-96B0-9FE2011F02E8' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case s.pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='4BA5FF8E-2644-4B27-96B0-9FE2011F02E8')
and p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '988E3D63-FDE0-49B1-AF81-9F960170F507' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, '7283A5CD-5E47-4D5D-8BD9-9F960170E080' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case when s.texto5 like '%fact%' then 'Facturação'
when s.texto5 like '%sal%' then 'Salários' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='7283A5CD-5E47-4D5D-8BD9-9F960170E080')
and p.FKSubLine='533DA89B-1BEC-474B-B615-9EE9011ACF4F';

/** 651 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'DF4653EC-E2FE-4FCE-832A-9F340152C13B' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=651 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, 'EF957844-CA2A-4D64-B1E3-9F9601718E62' FKTaxAsSubList
from (select distinct case when texto5 like '%fact%' then 'Facturação' when texto5 like '%sal%' then 'Salários' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='EF957844-CA2A-4D64-B1E3-9F9601718E62');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
x.v ItemValue, 'B6C57960-599D-4EA3-A19D-9FE2011FC53B' FKTaxAsSubList
from (select distinct case pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end v
from credegs..empresa.apolice where ramo=601) x
where x.v is not null and x.v not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='B6C57960-599D-4EA3-A19D-9FE2011FC53B');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'B147F99E-9A4A-4D98-8908-9F9601717FF5' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, 'F027892F-C035-44C3-A83A-9F960171DA2E' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, 'F6F60949-1000-47BC-994D-9FE2011FDDD4' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '4D813AFC-035F-40E2-9E60-9F960171CA20' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa end as float) as nvarchar(250)), case s.texto4 when '' then null else s.texto4 end) Value,
p.PK FKPolicy, '3D5287B5-3735-42EC-A3DC-9F960171B778' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, 'B6C57960-599D-4EA3-A19D-9FE2011FC53B' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case s.pertaxa when 1 then 'o/o' when 2 then 'o/oo' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='B6C57960-599D-4EA3-A19D-9FE2011FC53B')
and p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '64B6A3B3-1A8C-40AC-892D-9F960171A278' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
v.PK Value, p.PK FKPolicy, 'EF957844-CA2A-4D64-B1E3-9F9601718E62' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
left outer join credite_egs.tblPolicyValueItems v on v.ItemValue=case when s.texto5 like '%fact%' then 'Facturação'
when s.texto5 like '%sal%' then 'Salários' else null end
where (v.FKTaxAsSubList is null or v.FKTaxAsSubList='EF957844-CA2A-4D64-B1E3-9F9601718E62')
and p.FKSubLine='DF4653EC-E2FE-4FCE-832A-9F340152C13B';

/** 671 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '7CC94668-25FC-47E7-ADB4-9EE9011ACA6B' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=671 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '84AE1572-B05C-40B9-8F61-9F960171F750' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='7CC94668-25FC-47E7-ADB4-9EE9011ACA6B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 Value, p.PK FKPolicy, '61584895-084F-4921-9A91-9FE201203093' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='7CC94668-25FC-47E7-ADB4-9EE9011ACA6B';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, '53E6F7C8-719C-41FA-A365-9FE201201B58' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='7CC94668-25FC-47E7-ADB4-9EE9011ACA6B';
