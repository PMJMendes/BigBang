/** 305 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'C6D519C6-7138-4EE9-A954-9EE9011A7CAB' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join amartins..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=305 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case ltrim(isnull(s.texto1, '')) when '' then 'Desconhecida' else ltrim(s.texto1) end ObjName,
p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto2 ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.texto2 is not null
and p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.texto3 is not null
and p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.texto4 is not null
and p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto5 ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.texto5 is not null
and p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
p.PK FKPolicy, '5DB14FFB-05F9-4031-BAF1-9F9601520B11' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '921EF97A-8A75-4C90-9A2F-9F960151CEBD' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '2264F87D-9D0F-4AF7-A1DD-9F960151E380' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '9C1AE892-88F1-4DC1-804E-9F960151F7E3' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='C6D519C6-7138-4EE9-A954-9EE9011A7CAB';
