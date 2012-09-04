/**  102 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BEBB58B5-CD95-4872-B72F-9EE90118938F' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=102 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '53F0CDF9-593E-41D1-BD15-9F9100E78B08' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '49C7C6B7-6ED1-4DA1-9AD8-9F9100E7ABC1' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2EEC5EEC-C3A6-4716-9F01-9F9100E79EDA' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B6B0084F-30AF-4653-8695-9F9100E7B493' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B2A427E0-700D-4BBF-B861-9F9100E7948C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy,
case when '2012-01-01' > p.BeginDate then '2012-01-01' else p.BeginDate end StartDate,
case when '2012-12-31' < isnull(p.EndDate, '2013-01-01') then '2012-12-31' else p.EndDate end EndDate
from credite_egs.tblBBPolicies p
where (p.EndDate is null or p.EndDate > '2011-12-31')
and p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'12' ItemValue, '7F234FF5-3367-4F2C-A75A-A0AF00C10698' FKTaxAsSubList;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'12.75' ItemValue, '7F234FF5-3367-4F2C-A75A-A0AF00C10698' FKTaxAsSubList;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'13.5' ItemValue, '7F234FF5-3367-4F2C-A75A-A0AF00C10698' FKTaxAsSubList;

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, 'C24074CB-BDC2-45CC-BE49-9F9601419367' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'E9A3F14D-34D3-4228-BE13-9F960141A724' FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto6 when '' then null else s.texto6 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where t.pk='7149A17D-B9CC-4089-BDE9-A05300BF3B86' and c.BPresent=1
and p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='4DD89432-397E-4A42-82A2-A05300BF54CD' and c.BPresent=1
and p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

/**  101 e 104 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=101 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, 'Empregadas Domesticas. ' + substring(isnull(s.observ, ''), 1, 220) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=104 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6B184045-3035-48A6-9902-9F9100E7C53F' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A6A7D27A-0F81-4788-95FC-9F9100E7E178' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '95C04EDA-4660-4F78-81F0-9F9100E7D5F0' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '12585AC1-CC8D-4494-807D-9F9100E7E809' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D50487E2-F410-43B7-B61D-9F9100E7CD3E' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
(select PK from bigbang.tblCAE where left(CAEText, 5)=s.texto8 collate database_default) Value,
p.PK FKPolicy, 'ED2193DC-BE06-4C3A-A789-9F960142E94B' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, '0D72CCD6-7B82-49B0-973A-9F960142F6E0' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto3 when '' then null else s.texto3 end) Value,
p.PK FKPolicy, '2DCB0554-5B3A-42F8-93F2-9F9601470CC5' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.ramo=101 and p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto4 when '' then null else s.texto4 end) Value,
p.PK FKPolicy, '2DCB0554-5B3A-42F8-93F2-9F9601470CC5' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where s.ramo=104 and p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='CB510292-CE74-41A8-9D0D-A05300BF80B0' and c.BPresent=1
and p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='50B3FEB9-B849-4729-8EFD-A05300BF9EAD' and c.BPresent=1
and p.FKSubLine='CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9';

/** 105 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=105 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '39D5636E-10FE-4C2F-AD8B-9F9100E80AC1' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '25C5D7A7-7B31-4992-BFF2-9F9100E82BC4' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'A5ABCEB9-FE61-46E0-A3BD-9F9100E81CED' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E4E72739-9725-44DB-BF5D-9F9100E83324' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'D3172D15-A14B-4EFB-8972-9F9100E812C9' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
(select PK from bigbang.tblCAE where left(CAEText, 5)=s.texto8 collate database_default) Value,
p.PK FKPolicy, '6E025370-7924-4E61-89DB-9F9601477233' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, 'E5DB84BF-E580-4161-A010-9F960147856D' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, '1CBF1E55-C86B-4065-A16D-9F960147A76D' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto4 when '' then null else s.texto4 end) Value,
p.PK FKPolicy, '4618A421-8105-4677-8D36-9F960147BC35' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0A0197F6-E06C-4A22-8F7E-A05300C1A3AD' and c.BPresent=1
and p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0CED76AA-08B9-493C-8DA4-A05300C1DE17' and c.BPresent=1
and p.FKSubLine='1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF';

/**  112 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '68ED97FA-B9F8-40F7-B470-9EE90118B2DB' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=112 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '31CC0700-01B8-4954-9881-9F9100E84636' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4C8BBEF8-A8DB-4B06-AC47-9F9100E86B79' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E10D89AF-EAA8-41C2-A84D-9F9100E85EAE' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4CB3AFC2-78AE-42FD-B3E6-9F9100E8727B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'F81F3669-F569-45E6-BBE8-9F9100E85461' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy,
case when '2012-01-01' > p.BeginDate then '2012-01-01' else p.BeginDate end StartDate,
case when '2012-12-31' < isnull(p.EndDate, '2013-01-01') then '2012-12-31' else p.EndDate end EndDate
from credite_egs.tblBBPolicies p
where (p.EndDate is null or p.EndDate > '2011-12-31')
and p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'12' ItemValue, 'F546B8E3-9C5E-47C3-A45C-A0BE00C2E361' FKTaxAsSubList;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'12.75' ItemValue, 'F546B8E3-9C5E-47C3-A45C-A0BE00C2E361' FKTaxAsSubList;

insert into credite_egs.tblPolicyValueItems (PK, ItemValue, FKTaxAsSubList)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'13.5' ItemValue, 'F546B8E3-9C5E-47C3-A45C-A0BE00C2E361' FKTaxAsSubList;

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, '1875AE3F-0CF7-4C08-A839-9F9601480889' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '9A13CE88-1ED4-445E-A0DD-9F9601482417' FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto6 when '' then null else s.texto6 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, x.PK FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblExercises x on x.FKPolicy=p.PK
where t.pk='C2F7B36C-7188-4348-BC32-A05300C2CAE8' and c.BPresent=1
and p.FKSubLine='BEBB58B5-CD95-4872-B72F-9EE90118938F';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='79E2D443-8F48-474A-B0A6-A05300C2E5E4' and c.BPresent=1
and p.FKSubLine='68ED97FA-B9F8-40F7-B470-9EE90118B2DB';

/**  111 **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'BB0E78F2-3817-45C6-94C7-9EE90118BA82' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=111 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '29676A25-26AB-4809-90EB-9F9100E88732' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '128A47EF-D57E-4F23-AA7E-9F9100E8A400' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '684D0EBF-F41C-4488-8268-9F9100E897E3' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '95277E1D-2C6B-408C-B6BE-9F9100E8AB8B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'B8225845-E4C7-4BAC-8375-9F9100E88F91' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
(select PK from bigbang.tblCAE where left(CAEText, 5)=s.texto8 collate database_default) Value,
p.PK FKPolicy, '84B116AB-FF45-4386-9B60-9F960148489A' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.texto9 Value, p.PK FKPolicy, '30B7992C-69C5-4C3F-BD01-9F9601485A32' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(cast(case s.taxa when 0 then null else s.taxa*POWER(10.0, ISNULL(s.pertaxa, 1)-1) end as nvarchar(250)), case s.texto3 when '' then null else s.texto3 end) Value,
p.PK FKPolicy, '04CEEE97-6D16-403B-9959-9F9601491D19' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='892E1C7F-4B92-4BE7-A79D-A05300C60FA9' and c.BPresent=1
and p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='1652D416-635E-40B4-992A-A05300C6407C' and c.BPresent=1
and p.FKSubLine='BB0E78F2-3817-45C6-94C7-9EE90118BA82';

/** 103 Ano e Seguintes **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, 'FE18F5ED-F87C-4595-B9AC-9EE90118D568' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=103 and s.duracao='A'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '16BEE498-3A6C-4DCD-9439-9F9100E7139F' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '5DA0C14C-7CC1-46A2-B4C3-9F9100E7337B' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4B3319E4-701A-44A5-A099-9F9100E725DD' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '4349A3EC-7C3E-4BFC-9A23-9F9100E73E5F' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2BFD79A2-DB7A-4BB4-88F8-9F9100E71C6C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.texto9 SiteDescription
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='A41D5117-07C6-471E-BC5C-A05300B803AF' and c.BPresent=1
and p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'E862CAB7-8784-4C1D-9F0C-9F9601404C21' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'E636ED38-8BC6-49F6-A009-9F9601405E96' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '555D1EE3-3970-4842-88DE-9F96014074DE' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '3AEE54D8-0996-44E1-91D3-9F960140BA63' FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='BC261E66-30D1-4C34-BB31-A05300B7B3A0' and c.BPresent=1
and p.FKSubLine='FE18F5ED-F87C-4595-B9AC-9EE90118D568';

/** 103 Temporarias **/

insert into credite_egs.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '34E19434-6106-4359-93FE-9EE90118CEE0' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from credegs..empresa.apolice s
inner join credite_egs.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=103 and s.duracao='T'
and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and s.cliente in (select MigrationID from credite_egs.tblBBClients);

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '2843E249-6C9B-44CF-B416-9F9100E751C9' FKCoverage, 1 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E06C685D-2949-47D7-8590-9F9100E76D71' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '45194DE6-FEF5-4D05-8397-9F9100E761DB' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '986D75A0-AC49-4114-9FE1-9F9100E775D7' FKCoverage, 0 BPresent
from credite_egs.tblBBPolicies p
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '28D984C1-2F50-405A-A38E-9F9100E7591C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'N', 'NAO', 'NÃO', '---', 'SÓ ')) then 0 else 1 end BPresent
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'Desconhecida' ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
NULL Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, NULL SiteDescription
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'CB6E2669-8A15-4E90-B873-9F960140D7AE' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '7DDC1DDC-BAB5-40FA-AC38-9F960140F4D9' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '2A93FAF6-9785-4F8D-8208-9F96014108DC' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '9832F94C-34C3-4485-8EF0-9F960141441C' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'D1E6CE6C-D510-4B44-8372-9F9601415802' FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ISNULL(case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end, case s.texto2 when '' then null else s.texto2 end) Value,
p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='E6B8DA63-D3FC-4E11-A8DE-A05300BE9202' and c.BPresent=1
and p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';

insert into credite_egs.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from credegs..empresa.apolice s
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='51B4E4B3-A20E-4E23-A8C8-A05300BED7AC' and c.BPresent=1
and p.FKSubLine='34E19434-6106-4359-93FE-9EE90118CEE0';
