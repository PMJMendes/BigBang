/** Clients **/

ALTER TABLE [amartins].[tblBBPolicies]
ALTER COLUMN [FKClient] [uniqueidentifier] NULL;

/**  302 individuais e empresas **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
inner join amartins.tblBBClients z on z.MigrationID=s.cliente
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=302 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))
and z.FKentityType='4098CF7A-B5EE-4C3F-973F-9EE600C961AA' and z.FKEntitySubType is null;

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'EED10B99-11CE-4D01-945C-9F910103A103' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '63ACF500-8C1D-4C87-B5D8-9F910103AA46' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 1)) in ('', 'N', '-', '?')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E86D9819-234B-4185-B66B-9F910103B410' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco3, '')), 1)) in ('', 'N', '-', '?')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblExercises (PK, ExName, FKPolicy, StartDate, EndDate)
select  CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'2012' ExName, p.PK FKPolicy, '2012-01-01' StartDate, '2012-12-31' EndDate
from amartins.tblBBPolicies p
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblInsuredObjects (PK, ObjName, FKPolicy, FKObjType, Address1, Address2, FKZipCode, InclusionDate, ExclusionDate, SiteDescription)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
isnull(s.texto1, 'Desconhecida') ObjName, p.PK FKPolicy, 'CD709854-DB59-424B-904A-9F9501403847' FKObjType,
s.texto3 Address1, NULL Address2, NULL FKZipCode, NULL InclusionDate, NULL ExclusionDate, s.texto2 SiteDescription
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '2EA0A9AD-27BC-40F5-96D8-9FE201133A9B' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(s.vcapital as nvarchar(250)) Value, p.PK FKPolicy, '49279F1F-2631-4FF2-837A-9F9601620E93' FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='0662C878-2F00-448A-A05A-A05700E37A71' and c.BPresent=1
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(s.vcapital as nvarchar(250)) Value, p.PK FKPolicy, 'FBC0FA1E-E984-4C82-9E0B-9FE201129A95' FKField, NULL FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '0BFE5FCE-3201-408A-B734-9F960161BA39' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F6E729DC-08CB-47B5-A95B-9F960161E621' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, '12230A34-4E28-4259-8BF6-9FE20112CDDF' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.risco9 Value, p.PK FKPolicy, '7AD47076-E401-44CD-8B11-9FE201131578' FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='F8F32A34-6F72-4E60-9651-A05700E3A9AF' and c.BPresent=1
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
NULL Value, p.PK FKPolicy, 'F34690AF-0C6F-4891-ABA4-9F960161FA1B' FKField, o.PK FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblExercises x on x.FKPolicy=p.PK
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblBBPolicyValues (PK, Value, FKPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
cast(s.vcapital as nvarchar(250)) Value, p.PK FKPolicy, t.PK FKField, o.PK FKObject, x.PK FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages c on c.FKPolicy=p.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
inner join amartins.tblExercises x on x.FKPolicy=p.PK
inner join amartins.tblInsuredObjects o on o.FKPolicy=p.PK
where t.pk='99A1B134-5283-4CAA-A254-A05700E3604C' and c.BPresent=1
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

/**  Policies Pre **/

update amartins.tblBBPolicyValues
set Value=replace(replace(Value, ',', ''), '.', ',')
where FKField in
(select PK from bigbang.tblBBTaxes where FKFieldType='4D82EE91-0A9E-415E-9003-9F9601404007')
and Value like '%.[0-9][0-9]' and Value not like '%[a-z]%' and Value not like '%€%'
and FKPolicy in
(select PK from amartins.tblbbPolicies
where FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A' and _tscreate>='2012-11-04');

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, c.PK FKCoverage, NULL BPresent
from amartins.tblBBPolicies p
inner join bigbang.tblBBCoverages c on c.FKSubLine=p.FKSubLine
left outer join amartins.tblBBPolicyCoverages v on v.FKCoverage=c.PK and v.FKPolicy=p.PK
where c.BHeader=0 and v.PK is null
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'29145166-59AC-452E-8C2B-9F81013A39AC' FKScript, p.PK FKData, isnull(u.FKUser, k.FKManager) FKManager,
c.FKProcess FKParent, 0 IsRunning
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
inner join amartins.tblBBClients c on c.MigrationID=s.cliente
inner join amartins..empresa.cliente i on i.cliente=c.MigrationID
inner join amartins.tblpnprocesses k on k.pk=c.fkprocess
left outer join bigbang.tblUser2 u on u.MigrationID=s.GESTORAPOL
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

update amartins.tblBBPolicies set FKProcess=p.PK
from amartins.tblBBPolicies c inner join amartins.tblPNProcesses p on p.FKData=c.PK
where c.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and c._tscreate>='2012-11-04';

update amartins.tblBBPolicies set FKClient=c.PK
from amartins.tblBBPolicies p
inner join amartins.tblPNProcesses g on g.PK=p.FKProcess
inner join amartins.tblPNProcesses h on h.PK=g.FKParent
inner join amartins.tblBBClients c on c.PK=h.FKData
where p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

ALTER TABLE [amartins].[tblBBPolicies]
ALTER COLUMN [FKClient] [uniqueidentifier] NOT NULL;

/** Receipts Pre **/

insert into amartins.tblBBReceipts (PK, ReceiptNumber, FKReceiptType, FKProcess, TotalPremium, CommercialPremium, Commissions, Retrocessions, FATValue, IssueDate, MaturityDate,
EndDate, DueDate, FKMediator, ReceiptNotes, ReceiptDescription, ReturnText, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.recibo ReceiptNumber, t.PK FKReceiptType, NULL FKProcess, r.valorrec TotalPremium,
round(((r.valorrec - isnull(g.despesas, 0) - isnull(r.valc, 0))/case isnull(isnull(g.percem, h.percem), 0) when 0 then 1 else isnull(isnull(g.percem, h.percem), 0) end), 2) CommercialPremium,
r.valorcom Commissions, r.valm Retrocessions, r.valc FATValue,
NULL IssueDate, r.datavenc MaturityDate, r.datavencate EndDate, r.datalimite DueDate, NULL FKMediator, substring(r.observ, 1, 250) ReceiptNotes, r.designacao ReceiptDescription,
r.motivo ReturnText, r.MigrationID MigrationID
from amartins..empresa.recibo r
inner join bigbang.tblReceiptTypes t on t.TypeIndicator collate database_default = r.tiporec collate database_default
inner join amartins..empresa.apolice s on s.cliente=r.cliente and s.apolice=r.apolice and s.comseg=r.comseg and s.ramo=r.ramo
inner join amartins.tblbbpolicies p on p.MigrationID=s.MigrationID
inner join amartins..empresa.cliente c on c.cliente=s.cliente
inner join amartins..empresa.ramos h on h.ramo=r.ramo
left outer join amartins..empresa.comissagentes g on g.fkagente=isnull(s.mediapol, c.mediacli) and g.fkramo=r.ramo
where (r.datarec>'2010-12-31' or r.datavenc>'2010-12-31')
and (s.MigrationID in (select MigrationID from amartins.tblBBPolicies) or s.MigrationID in (select MigrationID from amartins.tblBBSubPolicies))
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

update amartins.tblBBReceipts
set FKReceiptType='91E07F5F-56BA-4A65-9659-9F900111DF95'
where FKReceiptType='382ABABA-5A0A-4E88-B5D1-A09E00EE2006'
and _tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo s on s.MigrationID=r.MigrationID
inner join amartins..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.comseg=s.comseg and a.ramo=s.ramo
inner join amartins.tblBBPolicies p on p.MigrationID=a.MigrationID
inner join amartins.tblpnprocesses k on k.pk=p.fkprocess
where r.FKProcess is null
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

update amartins.tblBBReceipts set FKProcess=p.PK
from amartins.tblBBReceipts r inner join amartins.tblPNProcesses p on p.FKData=r.PK
where r.FKProcess is null;

/** Histórico **/

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '2A501358-24D2-4877-BA9C-A01300C6E1B7' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoseg='04'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '47538724-7B79-4655-96D7-A01300C6A881' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoseg='05'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '102E9D79-A006-4757-ADBA-A01300C863B1' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='10'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '563A630A-276A-48E8-96D3-A01300C9AF24' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='10'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, 'F5F00701-69F7-4622-BB8C-9FB800DED93F' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='20'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '08799EDB-B874-49D5-B987-A01300CCAC59' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='20'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, 'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='30'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, 'C7305673-3EC9-4B1A-A437-A01300CD7D57' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='40'
and r._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, 'A34C65D4-0B2A-4083-BB16-A01300D3D013' FKOperation, h.data TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.historico h on h.cliente=x.cliente and h.apolice=x.apolice and h.recibo=x.recibo and h.comseg=x.comseg and h.ramo=x.ramo
inner join amartins.tblPNProcesses p on p.FKData=r.PK
inner join bigbang.tblUser2 u on u.MigrationID=h.OPERADOR
where h.estadoant!=h.estadoseg and h.estadoant!='' and h.estadoseg!=''
and h.estadoant='70'
and r._tscreate>'2012-11-06 22:00:00.000';

/** Casualties Pre **/

insert into amartins.tblBBSubCasualties (PK, SCNumber, FKProcess, FKPolicy, FKSubPolicy, ExternProcess, Description, Notes, BHasJudicial, GenericObject, FKCasualty)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.ordem+0.1 SCNumber, NULL FKProcess, p.PK FKPolicy, z.PK FKSubPolicy, NULL ExternProcess, NULL Description, substring(s.obsint, 1, 250) Notes, 0 BHasJudicial, s.sinistrado, c.PK
from amartins..empresa.sinistros s
inner join amartins.tblBBCasualties c on c.CNumber=s.ordem
inner join amartins..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.ramo=s.ramo and a.comseg=s.comseg
left outer join amartins.tblBBPolicies p on p.MigrationID=a.MigrationID
left outer join amartins.tblBBSubPolicies z on z.MigrationID=a.MigrationID
where (s.ordem>=20110000 or s.datasinistro>'2010-12-31' or s.dataparticipacao>'2010-12-31' or s.datafecho>'2010-12-31' or s.estado='A' or s.estado is NULL)
and s.ordem in (select CNumber from amartins.tblBBCasualties)
and (p.PK is not null or z.PK is not null)
and s.cliente in (select MigrationID from amartins.tblBBClients)
and p.FKSubLine='9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A'
and p._tscreate>='2012-11-04';

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'80B7A9BC-8710-4063-A99E-A02E01220F4E' FKScript, s.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from amartins.tblBBSubCasualties s
inner join amartins.tblBBCasualties c on c.PK = s.FKCasualty
inner join amartins.tblpnprocesses k on k.pk=c.fkprocess
where s.FKProcess is null;

update amartins.tblBBSubCasualties set FKProcess=p.PK
from amartins.tblBBSubCasualties s inner join amartins.tblPNProcesses p on p.FKData=s.PK
where s.FKProcess is null;

/** Histórico **/

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '207F3FEC-0CB2-47D4-868A-A03600FEE4FB' FKOperation, s.datafecho TStamp, p.FKManager FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBSubCasualties c
inner join amartins..empresa.sinistros s on s.ordem + 0.1 = c.SCNumber
inner join amartins.tblPNProcesses p on p.FKData=c.PK
where s.estado='F' and s.datafecho is not null
and c._tscreate>'2012-11-06 22:00:00.000';

insert into amartins.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '2A0EF8C0-ECFA-4622-9C72-A03600FACBCB' FKOperation, s.dataparticipacao TStamp, p.FKManager FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from amartins.tblBBSubCasualties c
inner join amartins..empresa.sinistros s on s.ordem + 0.1 = c.SCNumber
inner join amartins.tblPNProcesses p on p.FKData=c.PK
where s.dataparticipacao is not null
and c._tscreate>'2012-11-06 22:00:00.000';

/*****************************************/
/*****************************************/
/*****************************************/
/*****************************************/
/*****************************************/

/** Policies Post **/

/** Não provisórias **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='85D25D21-7FF1-4347-97E3-9FD4010D3B9B')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='690D5B0C-17B7-4109-A79D-9FD4010D4BB0')
and _tscreate>'2012-11-06 22:00:00.000';

/** Anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='BA9C2B80-142B-49BE-845F-9FD4010E0E50')
and _tscreate>'2012-11-06 22:00:00.000';

/** Auto-anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('8EE660A6-B682-4A9E-B9E7-A03100CEA4CE', '74C8A735-8425-4637-8D61-A03100CEB4B3'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Subs não provisórias **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='F9EDE7B0-27CD-4271-AED4-9FF301308004')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='40EBB8EF-3077-4104-95F4-9FF301308A2B')
and _tscreate>'2012-11-06 22:00:00.000';

/** Subs anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='53AB1F35-C31C-436D-8234-9FF30130C4A9')
and _tscreate>'2012-11-06 22:00:00.000';

/** Subs auto-anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('DD26080F-EF6F-4F0F-8837-A0310101316B', '0B074EE5-F169-4058-AC0B-A031010143BA'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Receipts Post **/

/** Mediador directo **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
where r.PK in
(select r.PK from amartins.tblBBReceipts r
inner join amartins.tblPNProcesses p1 on p1.PK=r.FKProcess
inner join amartins.tblPNProcesses p2 on p2.PK=p1.FKParent
inner join amartins.tblPNProcesses p3 on p3.PK=p2.FKParent,
amartins.tblBBPolicies p
inner join amartins.tblBBClients c on c.PK=p.FKClient,
amartins.tblMediators m
where (p.PK=p2.FKData or p.PK=p3.FKData)
and (m.PK=r.FKMediator or (r.FKMediator is null and m.PK=p.FKMediator) or
(r.FKMediator is null and p.FKMediator is null and m.PK=c.FKMediator))
and m.FKProfile='F60BB994-3E08-47C2-9CC3-9EFC013D35BE')
and n.FKController='2FE06BB6-4858-473A-B15B-A01300D76C71')
and _tscreate>'2012-11-06 22:00:00.000';

/** Perfil simples (AMartins) **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
where n.FKController='A1DE70D7-61B1-49F1-BEA0-A01300D67D6D')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Estornos ou sinistros **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('E7B800AC-5EC8-41A0-AEC9-A01300D751F3', '3DD47B13-A36B-4F31-8717-A01300D782F6'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')
and _tscreate>'2012-11-06 22:00:00.000';

/** Com imagem ou por conferir **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where (x.docushare is not null or x.estado>'00')
and n.FKController='FF6DDAAC-9699-477C-AA8E-A01300D659FD')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where (x.docushare is not null or x.estado>'00')
and n.FKController in ('A06F31E3-4406-47B3-A155-A01300D665E6', 'D14DDDBD-02CC-44E2-B344-A01300D67197'))
and _tscreate>'2012-11-06 22:00:00.000';

/** À cobrança, estornos ou sinistros, perfil simples (AMartins) **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'10'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'10'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='D7B75D8F-9608-49DE-89B1-A01300D7F102')
and _tscreate>'2012-11-06 22:00:00.000';

/** Pagos, normais **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Pagos, estornos ou sinistros **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'D7B75D8F-9608-49DE-89B1-A01300D7F102'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Prestados **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='D869D165-7E86-444A-835A-A01300D75B06')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='0594F667-06E7-4B4C-9761-A01300D76456')
and _tscreate>'2012-11-06 22:00:00.000';

/** Retrocedidos **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='2FE06BB6-4858-473A-B15B-A01300D76C71')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='A1F9829D-E26E-452B-A20C-A01300D775A0')
and _tscreate>'2012-11-06 22:00:00.000';

/** Devolvidos **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('51031667-19D9-48A1-A4FE-A01300D7A207', 'CEC07468-F1E1-46A5-9D67-A01300D7B67B'))
and _tscreate>'2012-11-06 22:00:00.000';

/** Casualties Post **/

/** Paragem de processos **/

update amartins.tblPNProcesses set IsRunning=0
from amartins.tblPNProcesses p
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.SCNumber
where x.estado='F' and x.datafecho is not null
and c._tscreate>'2012-11-06 22:00:00.000';

/** Abertos **/

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBCasualties c on c.PK=p.FKData
inner join amartins.tblBBSubCasualties s on s.FKCasualty=c.PK
where n.FKController='9B9889F7-270F-4A04-B5A3-A03C0111966B')
and _tscreate>'2012-11-06 22:00:00.000';

/** Fechados **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.SCNumber
where x.estado='F' and x.datafecho is not null
and n.FKController='785FE2C4-A0F3-43BD-BD92-A036010C8872')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.SCNumber
where x.estado='F' and x.datafecho is not null
and n.FKController='2C3830AC-29B3-4CE7-9345-A036010CBBF9')
and _tscreate>'2012-11-06 22:00:00.000';

/** Participacao enviada **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.SCNumber
where x.dataparticipacao is not null
and n.FKController='D518C62B-FB36-4D45-9257-A036010C5190')
and _tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.SCNumber
where x.dataparticipacao is not null
and n.FKController='5FEBB5D4-0243-4AA5-B244-A036010C5E51')
and _tscreate>'2012-11-06 22:00:00.000';

/** Processes Post **/

update amartins.tblPNSteps set FKLevel=o.FKDefaultLevel
from amartins.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
where s._tscreate>'2012-11-06 22:00:00.000';

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0)
and _tscreate>'2012-11-06 22:00:00.000';
