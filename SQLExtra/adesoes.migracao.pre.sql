update amartins.tblBBPolicies set MigrationID=60000 where PolicyNumber='500500';

update amartins.tblBBClients set MigrationID=20437 where PK='2872BE40-1C04-467C-8DE5-A0FE00E1D2B6';

insert into amartins.tblBBSubPolicies (PK, SubPolicyNumber, FKProcess, FKClient, BeginDate, EndDate, FKFractioning, SubPolicyNotes, FKStatus, Premium, DShareFolder, MigrationID, FKPolicy)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
ss.apolice SubPolicyNumber, null FKProcess, c.PK FKClient, ss.datini BeginDate, ss.datfim EndDate, f.PK FKFractioning, substring(ss.observ, 1, 250) SubPolicyNotes,
case ss.situacao when 'P' then '6489D7DF-A090-40B9-BD5E-9F98012C8BED' when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192'
when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(ss.moeda, 1) when 2 then ss.vpremio else ss.vpremio/200.482 end, 2) Premium, ss.DocuShare DShareFolder, ss.MigrationID MigrationID, p.PK FKPolicy
from amartins..empresa.apolice ss
inner join amartins.tblbbclients c on c.MigrationID=ss.cliente
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = ss.fpagamento COLLATE DATABASE_DEFAULT
inner join amartins..empresa.apolice sm on sm.cliente=ss.texto4 and sm.apolice=ss.texto1 and sm.ramo=ss.texto2 and sm.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=sm.MigrationID
where ss.ramo=10709 and ss.texto2=709 and (ss.situacao in ('P', 'N') or (ss.situacao in ('A', 'U') and (ss.datfim is null or ss.datfim>'2009-12-31')));

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
b.PK FKSubPolicy, z.FKCoverage, z.BPresent
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblBBPolicyCoverages z on z.FKPolicy=p.PK
where s.ramo=709 and ss.ramo=10709;

insert into amartins.tblBBSubPolicyValues (PK, Value, FKSubPolicy, FKField, FKObject, FKExercise)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
case s.vcapital when 0 then null else cast(s.vcapital as nvarchar(250)) end Value,
ss.PK FKSubPolicy, t.PK FKField, NULL FKObject, NULL FKExercise
from amartins..empresa.apolice s
inner join amartins.tblBBSubPolicies ss on ss.MigrationID=s.MigrationID
inner join amartins.tblBBSubPolicyCoverages c on c.FKSubPolicy=ss.PK
inner join bigbang.tblBBTaxes t on t.FKCoverage=c.FKCoverage
where t.pk='80751260-60E7-4031-8F08-A06E00E6BF68' and c.BPresent=1
and s.ramo=10709 and s.texto2=709;

update amartins.tblBBSubPolicyValues
set Value=replace(replace(Value, ',', ''), '.', ',')
where FKField in
(select PK from bigbang.tblBBTaxes where FKFieldType='4D82EE91-0A9E-415E-9003-9F9601404007')
and Value like '%.[0-9][0-9]' and Value not like '%[a-z]%' and Value not like '%€%';

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'08C796D6-5622-4FF1-B3AB-9FF300F28ABC' FKScript, b.PK FKData, k.FKManager FKManager,
p.FKProcess FKParent, 0 IsRunning
from amartins.tblBBSubPolicies b
inner join amartins.tblBBPolicies p on p.PK=b.FKPolicy
inner join amartins.tblpnprocesses k on k.pk=p.fkprocess;

update amartins.tblBBSubPolicies set FKProcess=p.PK
from amartins.tblBBSubPolicies c inner join amartins.tblPNProcesses p on p.FKData=c.PK;
