/** 502, 503, 504 e 505, Ano e Seguintes **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo in (502, 503, 504, 505)  and s.duracao='A'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EC6D3465-E377-4462-B74D-9F910101E5F8' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '82A53FB6-D213-47A6-AC3D-9F910101DC6B' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '19E18CA1-8332-4C81-90A9-9F910101FDBE' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A0B9FBF7-9B9B-49B6-AEB7-9F910101B82A' FKCoverage,
case when upper(left(ltrim(isnull(s.risco1, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'AB02C134-C9D6-4FE0-8B37-9F910101BF15' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '51809B49-3B3E-4ADA-BD7E-9F910101C8BF' FKCoverage,
case when upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E3C27743-4593-4C18-B0C8-9F91010211E0' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F61E0663-EA37-4955-804D-9F9101020B27' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CD6B3B44-8ABF-4660-B62B-9F9101021986' FKCoverage,
case when upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '308C5A38-248E-40A9-A016-9F910101EC81' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '0BEF6EA9-BD6B-4D8A-AB2C-9F910101F5C8' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '76A155E1-64E3-42C7-A9BB-A066015F15D6' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8AA3912F-B2BB-4EA9-A98C-9F910101D199' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '0FF7D0D0-5B0A-4821-BDEF-A066015F22F2' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto1 Value, p.PK FKPolicy, 'B755455B-46ED-4C50-A708-9F96015F89BA' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F2C7E530-6186-4F42-B459-A05F00F397B7' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, '3D91F8D5-9F5C-4006-8CDA-9F96015FCD65' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'BB3FD661-EE4C-4F46-A7E8-9F96015F9F0F' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)), null) Value,
p.PK FKPolicy, 'B0E5B073-E3CF-4F93-912E-9F96015FAC96' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '6A3FF531-8838-4314-8CDF-A05F00F38220' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='8379A4F9-C8FF-4E48-B417-9EE9011B0BA5';

/** 502, 503, 504 e 505, Temporárias **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '722EBA20-3136-4E3B-B3F8-9EE9011B059E' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo in (502, 503, 504, 505)  and s.duracao='T' and s.datini>'2010-12-31'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6462A436-1172-4447-BE24-9F9101025EB3' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5EA0A432-4AB6-458A-B8D1-9F9101025702' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7DF17FDF-C0D0-4D57-8C97-9F910102AF26' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B9497BE0-C547-4ED1-AC19-9F91010236BB' FKCoverage,
case when upper(left(ltrim(isnull(s.risco1, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A5A5D7A9-28C8-44DE-8A35-9F9101023EE0' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9B828DCF-B60D-43D9-B66B-9F910102464A' FKCoverage,
case when upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6A3F205D-EA01-4C95-B2AB-9F910102C1DA' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '40570B63-9C94-4B48-86B2-9F910102BA21' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F6916AA1-1E79-4153-9C26-9F910102CA4A' FKCoverage,
case when upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6EB31573-AB41-433F-8B02-9F91010263CA' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '21DC6FE8-58B1-46B0-A3C5-9F9101027020' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7AD5A524-46E8-4A2F-B08C-A066015EF6BB' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C482BE89-A9D5-4F37-9BB3-9F9101024F41' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E1349510-AA22-4F34-A535-A066015F050F' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, MakeAndModel,
EquipmentDescription, FirstRegistryDate, ManufactureYear, ClientIDE, InsurerIDE)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto6, '')) when '' then 'Desconhecida' else ltrim(s.texto6) end ObjName, p.PK FKPolicy, 'E3E7B018-6F07-42DA-8B54-9F9501402D25' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, s.datini InclusionDate, s.datfim ExclusionDate, NULL MakeAndModel, NULL EquipmentDescription, NULL FirstRegistryDate,
NULL ManufactureYear, NULL ClientIDE, NULL InsurerIDE
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '0662C3E5-630A-4419-A8F2-A05F00F4023B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '81CDB047-13D7-4108-AFE5-A05F00F3EC91' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(cast(s.vcapital as float) as nvarchar(250)) end, null) Value,
p.PK FKPolicy, '4FE90017-6764-4439-8A20-9F96016030C7' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto1 Value, p.PK FKPolicy, 'C388B6C5-2FE3-4EEA-A129-9F9601600EDF' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto1 Value, p.PK FKPolicy, '238FDA05-A250-4784-B46C-9F96015FFF90' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A3ABB1C3-B222-47C6-A25B-9F96016042F6' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)), null) Value,
p.PK FKPolicy, 'B3155091-0219-4DC9-9AFF-9F9601601DF9' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='722EBA20-3136-4E3B-B3F8-9EE9011B059E';

/** 510 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'FC9A126A-6ADB-4483-88CB-9FE2010916EE' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=510 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '61FE437C-992A-4357-8C48-9FE201098275' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '20A5E739-7A8B-49FF-8B87-9FE2010974CC' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '0E3B356A-AF71-454D-91CF-9FE2010A3FAF' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CAE2C8D0-97C1-4CE1-863D-9FE201093F04' FKCoverage,
case when upper(left(ltrim(isnull(s.risco1, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '44111A87-0404-48DE-8F08-9FE201094CB1' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C7593807-9CCE-49BE-B470-9FE2010958FC' FKCoverage,
case when upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6DD23B73-CEED-4CBE-A7D0-9FE2010A61D1' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EC04292A-F6C3-4E92-B71D-9FE2010A5538' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '55999917-B3D4-4006-8ADA-9FE2010A6F76' FKCoverage,
case when upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', '-', 'N') then 0 else 1 end BPresent
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1343B7D6-65E8-454A-9EBE-9FE2010990FE' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '80A572F4-D5BE-446C-A906-9FE20109B047' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '927CB215-F79A-4D52-8E22-A066015F3DD4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '38C029B4-D05B-436C-998A-9FE20109657D' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8BEF8FBB-B449-4E19-87C4-A066015F4BD0' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, '6DFC59E0-578D-49B4-853B-9FE2010B1F86' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, '7B3616B2-F7B4-4C8F-A9DF-A05F00F48739' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(cast(case s.taxa when 0 then null else s.taxa/POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as float) as nvarchar(250)), null) Value,
p.PK FKPolicy, 'DCF93229-5CB5-463C-AE32-9FE2010AF6C3' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '8E225BC1-C4EC-428A-8C67-A05F00F45F81' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FC9A126A-6ADB-4483-88CB-9FE2010916EE';

/** 311 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '6C51BDED-B9FC-4F1C-9C08-9FE2010BE557' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=311 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, FiscalNumberI, FKSex, DateOfBirth,
ClientNumberI, InsurerIDI)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'EDD94689-EFED-4B50-AA6E-9F9501402700' FKObjType, NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate,
NULL ExclusionDate, NULL FiscalNumberI, NULL FKSex, NULL DateOfBirth, NULL ClientNumberI, NULL InsurerIDI
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBClients c on c.MigrationID=s.cliente
where p.FKSubLine='6C51BDED-B9FC-4F1C-9C08-9FE2010BE557';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco3 Value, p.PK FKPolicy, 'C9C5BE56-2199-4E91-9AFB-9FE2010C61AD' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='6C51BDED-B9FC-4F1C-9C08-9FE2010BE557';
