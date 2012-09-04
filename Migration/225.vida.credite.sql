/** 704 Empresas, Condomínios e não identificados **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '4AD324A9-6973-409F-913D-A06E00D2A060' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=704 and (cx.tipo_c is null or cx.tipo_c in ('E', 'C'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2F18C57E-E660-4801-8858-9F910109C8F6' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='4AD324A9-6973-409F-913D-A06E00D2A060';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3A4F9145-3CAF-4562-8F03-9F910109CE73' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='4AD324A9-6973-409F-913D-A06E00D2A060';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'BCDA7B41-60E0-466E-BA8F-9FE20121EEED' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='4AD324A9-6973-409F-913D-A06E00D2A060';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '913E456A-E25C-4DB9-BF77-9F960172797E' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='4AD324A9-6973-409F-913D-A06E00D2A060';

/** 704 Individuais **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'CF4EB699-9C49-4058-A1A9-A06E00D298CD' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=704 and cx.tipo_c='I'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7BCA85DF-8D1F-4677-A61B-9F910115DBE9' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D579D992-18E5-4DCF-B546-9F910115E0D1' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'D4CDDA0E-00A6-4CE9-A7DF-9FE20126065E' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'DC153A10-FD3E-4882-B9EF-9F9601740A86' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='C1F154DE-D5E9-49E8-B19D-A06800D87111' and c.BPresent=1
and p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='ED98F84F-6737-42E8-BF97-A06800D85469' and c.BPresent=1
and p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='FEFDD7ED-1103-4D2D-956E-A06800D89465' and c.BPresent=1
and p.FKSubLine='CF4EB699-9C49-4058-A1A9-A06E00D298CD';

/** 711 Empresas, Condomínios e não identificados **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'F16457F7-2861-47E5-A262-A06E00D2A060' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=711 and (cx.tipo_c is null or cx.tipo_c in ('E', 'C'))
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '1A9DCF3C-D9C4-47B9-BF7C-9FE201228DE3' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='F16457F7-2861-47E5-A262-A06E00D2A060';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '36335D6A-42F6-456B-9FA5-9F960172D372' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='F16457F7-2861-47E5-A262-A06E00D2A060';

/** 711 Individuais **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'EFE0902C-FCD1-4B9F-B87B-A06E00D298CD' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=711 and cx.tipo_c='I'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '4271E12A-D276-4386-88EA-9FE2012748B6' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='EFE0902C-FCD1-4B9F-B87B-A06E00D298CD';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'B7639D24-F074-4F89-99FF-9F9601745588' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='EFE0902C-FCD1-4B9F-B87B-A06E00D298CD';

/** 709 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=709 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8A6B8014-DF2C-45B0-85CC-A06E00E5095F' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '294AFE67-D0B3-4688-8693-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B6D074C0-7417-408E-903B-A06E00E5095F' FKCoverage,
case when ltrim(isnull(s.risco2, ''))='' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B444F311-2465-4AFB-9AFF-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '34DCC3B4-8420-457D-B797-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B2FE65E0-7566-451C-8BF5-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B35F6999-3C57-4F6B-8EF4-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '861660FA-846D-4C6B-B5F3-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6FD04A1A-24EE-4D1A-9344-A06E00E5095F' FKCoverage,
case when ltrim(isnull(s.risco3, ''))='' then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E4FB0975-1A0E-4408-A8F4-A06E00E5095F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Constante' ItemValue, '104753F0-15E5-437B-9223-A06E00E6BF68' FKTaxAsSubList
where 'Constante' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='104753F0-15E5-437B-9223-A06E00E6BF68');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Crescente' ItemValue, '104753F0-15E5-437B-9223-A06E00E6BF68' FKTaxAsSubList
where 'Crescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='104753F0-15E5-437B-9223-A06E00E6BF68');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Decrescente' ItemValue, '104753F0-15E5-437B-9223-A06E00E6BF68' FKTaxAsSubList
where 'Decrescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='104753F0-15E5-437B-9223-A06E00E6BF68');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '104753F0-15E5-437B-9223-A06E00E6BF68' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='25356B45-54CC-44DF-9D0A-A06E00E6BF68' and c.BPresent=1
and p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='281181BE-8311-4B1A-8A9E-A06E00E6BF68' and c.BPresent=1
and p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='DBE34DF2-4D16-4EE9-B60C-A06E00E6BF68' and c.BPresent=1
and p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='9BBA03AF-1F5E-4145-9586-A06E00E6BF68' and c.BPresent=1
and p.FKSubLine='1ACC21FA-7CC0-4AF5-A22D-A06E00E37DCE';

/** 702 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '50DD38B3-31F0-423F-BE88-A06E00D22C8C' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=702 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '3EBB7E80-EE46-47FD-89FD-9F91010A1107' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F914811A-23C9-47FC-97D1-9F91010A7B31' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F88BDA0F-2F89-4C34-A32C-9F91010A352C' FKCoverage,
case when left(ltrim(isnull(s.risco2, '')), 1) in  ('', '-') then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '73C32AE7-8328-4FC2-B33F-9F91010A5415' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '86958CAA-7B5B-4341-AB08-9F91010A6210' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '16620BB3-6F89-41BC-A52C-9F91010A715C' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DF9B1F30-C17C-4F70-AE00-9F91010A472E' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A93BA557-50C7-4A19-914A-9F91010A87B5' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '54387776-D07C-40A3-8C50-9F91010A1922' FKCoverage,
case when left(ltrim(isnull(s.risco3, '')), 1) in  ('', '-', 'N') then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '397674BD-6E5E-43FE-958D-9F91010A258B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Constante' ItemValue, 'D455F2AD-F045-42EC-9441-9F9601739A10' FKTaxAsSubList
where 'Constante' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='D455F2AD-F045-42EC-9441-9F9601739A10');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Crescente' ItemValue, 'D455F2AD-F045-42EC-9441-9F9601739A10' FKTaxAsSubList
where 'Crescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='D455F2AD-F045-42EC-9441-9F9601739A10');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Decrescente' ItemValue, 'D455F2AD-F045-42EC-9441-9F9601739A10' FKTaxAsSubList
where 'Decrescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='D455F2AD-F045-42EC-9441-9F9601739A10');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'D455F2AD-F045-42EC-9441-9F9601739A10' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E0BDF884-0709-44F3-8934-A06800D61119' and c.BPresent=1
and p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='B4D07089-4372-43C0-B658-A06800D5D224' and c.BPresent=1
and p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='EC047D6E-D767-4057-846B-A06800D6CF90' and c.BPresent=1
and p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='66556FE2-FF38-40F8-9858-A06800D6A406' and c.BPresent=1
and p.FKSubLine='50DD38B3-31F0-423F-BE88-A06E00D22C8C';

/** 701 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BB944A23-9634-4FB4-BCC9-A06E00D223FE' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
inner join credegs..empresa.cliente cx on cx.cliente=s.cliente
where s.ramo=701 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E5A4C02A-D819-49FA-BA4E-9F9101162420' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C2A9B895-0CFA-4345-86B8-9F910116875C' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DCEA7856-7DC7-48A5-B8B8-9F9101164963' FKCoverage,
case when left(ltrim(isnull(s.risco2, '')), 1) in  ('', '-') then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '7310F998-FD37-48E7-AF77-9F910116676B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8E610386-4730-47DF-AFF6-9F910116713E' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '382DC0E5-B7D2-4024-8633-9F9101167D82' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E9CDAC3D-F5AF-4E66-881F-9F9101165ADD' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '8E73DD94-434B-4532-9ED6-9F9101162BB9' FKCoverage,
case when left(ltrim(isnull(s.risco3, '')), 1) in  ('', '-', 'N') then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'BB828E94-889C-49F9-BE90-9F91011639FF' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Constante' ItemValue, 'C3563291-7658-4B53-BF5F-9F9601751EBB' FKTaxAsSubList
where 'Constante' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='C3563291-7658-4B53-BF5F-9F9601751EBB');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Crescente' ItemValue, 'C3563291-7658-4B53-BF5F-9F9601751EBB' FKTaxAsSubList
where 'Crescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='C3563291-7658-4B53-BF5F-9F9601751EBB');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Decrescente' ItemValue, 'C3563291-7658-4B53-BF5F-9F9601751EBB' FKTaxAsSubList
where 'Decrescente' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='C3563291-7658-4B53-BF5F-9F9601751EBB');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Uma Cabeça' ItemValue, 'DF8C62FF-FEB9-4950-805D-A09E00ECA51A' FKTaxAsSubList
where 'Uma Cabeça' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='DF8C62FF-FEB9-4950-805D-A09E00ECA51A');

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Duas Cabeças' ItemValue, 'DF8C62FF-FEB9-4950-805D-A09E00ECA51A' FKTaxAsSubList
where 'Duas Cabeças' not in
(select ItemValue from credite_egs.tblPolicyValueItems where FKTaxAsSubList='DF8C62FF-FEB9-4950-805D-A09E00ECA51A');

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'C3563291-7658-4B53-BF5F-9F9601751EBB' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='5CF3505C-65E1-426B-BA01-A06800DAFC39' and c.BPresent=1
and p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0CFA2C44-BC03-4ADE-8A31-A06800DAA4EE' and c.BPresent=1
and p.FKSubLine='BB944A23-9634-4FB4-BCC9-A06E00D223FE';
