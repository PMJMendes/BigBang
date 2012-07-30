/** 602 e 603, Ano e Seguintes **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '6A2A5C28-6866-463C-9B6A-9F34015284C4' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo in (602, 603)  and s.duracao='A'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2E2E54BC-A3A9-4DF5-AC7B-9F910104152E' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '31C91A8B-C641-40B2-ABE3-9F9101041EDE' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1B9F8A1B-913F-4AFF-B964-9F910104270C' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '594DF37A-D25B-47FE-BD52-9F91010435DA' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A823B953-A929-4F0D-BFC8-9F9101044666' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7BB40741-FEEC-4BD8-A46D-9F9101045521' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9576E644-AE83-486F-BA2B-9F9101045D7E' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7032E7C5-7CC5-4643-9A8C-9FE20114D7E0' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '67CD7CE2-5A22-4487-889D-9FE20114E495' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from amartins.tblBBPolicies p
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto3 Value, p.PK FKPolicy, '3BD09F37-E57A-4A25-A09D-9F960162F9A5' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto7 Value, p.PK FKPolicy, 'F6C40420-99FF-4C28-9BC0-9F9601632FD7' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '37B8656D-95F8-4A67-A93D-9F96016292EE' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, 'A93279D9-1638-4C50-8342-9F960162B6FB' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='76A02F12-C16E-4A70-898B-A059011B1D4F' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='8B2AE327-8C89-426D-99E5-A059011BF4C5' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='6E96925C-05F0-453C-B393-A059011BAB4C' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1512BBB9-59F3-469D-AA27-A059011C30CF' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='85A403C0-EF14-453F-9AF3-A059011CD276' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E9FC137C-2E06-468E-B192-A059011FE838' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='14139D13-4511-43A4-85EB-A059011D4DB8' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='166743A6-8071-48CC-B50E-A059011F13A1' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5AC2DBF3-7ECD-4853-9CF1-A059011E125D' and c.BPresent=1
and p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F5DBE6C1-3072-49AB-BC11-A05A0117EE7F' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='6A2A5C28-6866-463C-9B6A-9F34015284C4';

/** 602 e 603, Temporárias **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo in (602, 603)  and s.duracao='T'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A3408C3C-77E4-4289-937E-9F91010475BD' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3113A7B6-6313-43A8-AD26-9F9101048166' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital6, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '16FAB155-1E09-4132-87E7-9F9101048AC0' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital7, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '81AF6C0F-EAC3-4B2A-8C15-9F91010498E5' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C498BED1-5EB3-4E1F-89BF-9F910104A780' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4CA702EE-402F-41B4-BD98-9F910104B2D4' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '089B9C30-369D-44F0-8126-9F910104BA21' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3FF93A8A-BA70-421D-9B3D-9FE20116A9C5' FKCoverage,
case when (upper(left(ltrim(isnull(s.texto7, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C6268A9D-A4F3-4EC2-949B-9FE20116BAD6' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

set language portuguese;
insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Período Inicial' ExName, p.PK FKPolicy,
case isdate(s.texto4) when 0 then s.datini else case when s.datini>cast(s.texto4 as datetime) then s.datini else cast(s.texto4 as datetime) end end StartDate,
case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end EndDate
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Prorrogação' ExName, p.PK FKPolicy,
case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end StartDate,
case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end EndDate
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where datediff(day, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end) > 0
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';
set language us_english;

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, '0BABC8B1-2BCE-4986-9A84-9F960164A630' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'EAF7B09E-4E3A-43C1-9DF3-9F960164D88A' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '35446D4E-6B46-457B-917B-9F96016406A9' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '095CE487-5B9F-4644-B259-9F9601642797' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, '2EEB4A71-C0E2-4C10-97A3-9F960163F46D' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'B4E9220B-0FDF-4815-AB32-9F96016503E1' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D3D272D9-67E9-4AC0-A6AF-A05901136796' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='41D26619-1EF5-4A76-BA53-A059011962A0' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B172325E-AAFC-4C8F-9A4A-A0590119A22D' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F7F06132-AEEF-401C-8FBF-A0590119B7F1' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7223DF62-CEE6-4533-A453-A0590119D75F' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BBB81A07-3DE3-4250-A2FD-A05901185CDA' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='519B5DAC-A3B0-4A29-BC49-A05901187AD3' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='374832D7-F8DD-4516-BA6B-A05901190007' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E0990F6E-2C2C-4694-A120-A0590119287E' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9D691A9B-4DEF-4ED8-B0D4-A05901140956' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='950E382A-65DE-4776-8965-A059011444A9' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='3C8E2B29-15DF-4180-A68E-A0590117E7ED' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2745C251-5655-4A0E-85CB-A05901181373' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='7031FCAB-9F7B-40BA-8307-A05901173992' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7C5D780-07FA-4616-B0CB-A059011762CD' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D7E08F12-84D8-4C20-8102-A05901148431' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(month, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto7) when 0 then null else cast(s.texto7 as datetime) end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A0486FD1-504D-4D8B-8EE6-A0590114C6FC' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D261B6FF-3C03-45C3-9211-A0590114DFBE' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(day, case isdate(s.texto4) when 0 then null else cast(s.texto4 as datetime) end, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end) Value,
p.PK FKPolicy, 'EADF0C7B-800D-4377-8A65-9F9601648906' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Período Inicial'
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto5 Value, p.PK FKPolicy, '1CF2B1BE-72DE-42E8-9A69-9F960164BD1C' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Período Inicial'
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(day, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end) Value,
p.PK FKPolicy, 'EADF0C7B-800D-4377-8A65-9F9601648906' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Prorrogação'
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, '1CF2B1BE-72DE-42E8-9A69-9F960164BD1C' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Prorrogação'
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where t.pk='9EAF174D-B1AD-480A-B7CB-A0590113DF4D' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where t.pk='9101800D-9078-4E48-8B2B-A0590117C168' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='68732B13-EE44-479C-BB4E-A05901130EBD' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital6 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F72A15FA-7FA5-46F2-BEE7-A05901133EA7' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A71D8956-F497-4E3A-9DB0-A05901139AD2' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where t.pk='2DE406C6-7C67-4160-AF53-A0590112E7EC' and c.BPresent=1
and p.FKSubLine='D36F47AB-0D5A-425D-8FD4-9EE9011A6837';

/** 612 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '691EF827-EF46-4C49-A828-9F92015826D8' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=612 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '432A3622-E2E9-4A0B-828B-9F960168D415' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='691EF827-EF46-4C49-A828-9F92015826D8';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A091A524-A17D-4DF2-807F-9F96016A1311' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='691EF827-EF46-4C49-A828-9F92015826D8';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, 'C0B506F5-F916-4EEE-9D5C-9FE20117DE80' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='691EF827-EF46-4C49-A828-9F92015826D8';

/** 620 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '04AEE708-28AF-4C5C-B441-A09E00C2A4B3' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=620
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from amartins.tblBBClients);

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5414DAD9-9FD2-484F-94B8-A09E00C310B1' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5070828D-E742-4155-92EC-A09E00C32263' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital6, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6658CCE6-3C51-4D2E-AD05-A09E00C39336' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital7, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A5F43484-1AED-4A68-B2C1-A09E00C38402' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EB23B002-0B9B-4EFA-9CC1-A09E00C3340A' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CE4C269F-E71C-4B2D-B1B5-A09E00C36096' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2FB834B6-86E2-4B17-9DF4-A09E00C34CD8' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A84345F9-B8C7-487B-8764-A09E00C340F0' FKCoverage,
case when (upper(left(ltrim(isnull(s.texto7, '')), 1)) in ('', '-', 'N', 'E')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '62C5C812-9392-4A4B-9BB3-A09E00C36F49' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

set language portuguese;
insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Período Inicial' ExName, p.PK FKPolicy,
case isdate(s.texto4) when 0 then s.datini else case when s.datini>cast(s.texto4 as datetime) then s.datini else cast(s.texto4 as datetime) end end StartDate,
case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end EndDate
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Prorrogação' ExName, p.PK FKPolicy,
case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end StartDate,
case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end EndDate
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where datediff(day, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end) > 0
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';
set language us_english;

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto4 Value, p.PK FKPolicy, 'D7C86D0C-3252-4A9F-A376-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'A2C9AD28-C34A-4632-855B-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'CEA7AE66-EAF7-4ABE-B83A-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'EF9C23B7-36D7-4A4C-83BC-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, 'C7CACF73-A7C0-47C1-8A2E-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'D6FBC88C-9C1D-4588-8389-A09E00C7531B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='2DBC2A4C-1395-4539-AC91-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='321426ED-77A3-418C-9368-A09E00CBDAE9' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='54E6470B-E2C9-4629-ADD7-A09E00CC454C' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco7 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A0F02F08-23E5-485C-B1DF-A09E00CBFA1B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='D2F4A8BB-8BBC-42A5-AAD8-A09E00CC1C30' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B1FF7511-327F-4EFC-91D7-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='97DB49DE-E278-4B36-82C9-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='001E23CF-25AB-4137-829D-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1F42044F-7D90-4613-A86B-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='650EC361-0F0E-4692-A79D-A09E00CB838D' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='F8C554DA-C5FD-4AE0-BC5B-A09E00CB5788' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DF73A869-898F-4919-9CAC-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5B4D7A22-897C-4768-BCC8-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='BC45D9A1-5837-4C2C-9261-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FA5B7A76-DC6B-4868-9468-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco4 Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9D54849C-FE83-4513-B17E-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(month, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto7) when 0 then null else cast(s.texto7 as datetime) end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EE573D14-696E-4D8C-8B13-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E15A5547-F1D4-4894-8AE2-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(day, case isdate(s.texto4) when 0 then null else cast(s.texto4 as datetime) end, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end) Value,
p.PK FKPolicy, '30D0A182-42E6-4C40-B335-A09E00C7531B' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Período Inicial'
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto5 Value, p.PK FKPolicy, 'BA7093BD-2AE3-469E-B04C-A09E00C7531B' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Período Inicial'
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

set language portuguese;
insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
datediff(day, case isdate(s.texto5) when 0 then null else cast(s.texto5 as datetime) end, case isdate(s.texto6) when 0 then null else cast(s.texto6 as datetime) end) Value,
p.PK FKPolicy, '30D0A182-42E6-4C40-B335-A09E00C7531B' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Prorrogação'
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';
set language us_english;

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto6 Value, p.PK FKPolicy, 'BA7093BD-2AE3-469E-B04C-A09E00C7531B' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where x.ExName like 'Prorrogação'
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where t.pk='150C48A9-F308-4574-9F83-A09E00CBA43C' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where t.pk='64D59BB2-88B0-4AE9-ADAE-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco1 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='B7E795E7-7A84-48F3-89C3-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.capital6 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='73944AE3-8BDD-41F7-9B88-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco5 Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='FDA38B3E-584B-41B3-AB55-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(cast(s.vcapital as float) as nvarchar(250)) Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='CAFDE7A7-FEA1-4DD2-B7BC-A09E00C7531B' and c.BPresent=1
and p.FKSubLine='04AEE708-28AF-4C5C-B441-A09E00C2A4B3';
