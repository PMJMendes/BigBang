update amartins.tblBBPolicyValues
set Value=replace(replace(Value, ',', ''), '.', ',')
where FKField in
(select PK from bigbang.tblBBTaxes where FKFieldType='4D82EE91-0A9E-415E-9003-9F9601404007')
and Value like '%.[0-9][0-9]' and Value not like '%[a-z]%' and Value not like '%€%';

update amartins.tblBBSubPolicyValues
set Value=replace(replace(Value, ',', ''), '.', ',')
where FKField in
(select PK from bigbang.tblBBTaxes where FKFieldType='4D82EE91-0A9E-415E-9003-9F9601404007')
and Value like '%.[0-9][0-9]' and Value not like '%[a-z]%' and Value not like '%€%';

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, c.PK FKCoverage, NULL BPresent
from amartins.tblBBPolicies p
inner join bigbang.tblBBCoverages c on c.FKSubLine=p.FKSubLine
left outer join amartins.tblBBPolicyCoverages v on v.FKCoverage=c.PK and v.FKPolicy=p.PK
where c.BHeader=0 and v.PK is null;

insert into amartins.tblBBSubPolicyCoverages (PK, FKSubPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.PK FKSubPolicy, c.PK FKCoverage, NULL BPresent
from amartins.tblBBSubPolicies s
inner join amartins.tblPNProcesses sp on sp.PK=s.FKProcess
inner join amartins.tblPNProcesses pp on pp.PK=sp.FKParent
inner join amartins.tblBBPolicies p on p.PK=pp.FKData
inner join bigbang.tblBBCoverages c on c.FKSubLine=p.FKSubLine
left outer join amartins.tblBBSubPolicyCoverages v on v.FKCoverage=c.PK and v.FKSubPolicy=s.PK
where c.BHeader=0 and v.PK is null

insert into amartins.tblPolicyCoInsurers (PK, FKPolicy, FKCompany, [Percent])
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, c.PK FKCompany, cs.[percent] [Percent]
from amartins..empresa.cosseguro cs
inner join amartins..empresa.apolice s on s.cliente=cs.cliente and s.apolice=cs.apolice and s.ramo=cs.ramo and s.comseg=cs.comseg
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblCompanies c on c.MigrationID=cs.cocomseg;

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'29145166-59AC-452E-8C2B-9F81013A39AC' FKScript, p.PK FKData, isnull(u.FKUser, k.FKManager) FKManager,
c.FKProcess FKParent, 0 IsRunning
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
inner join amartins.tblBBClients c on c.MigrationID=s.cliente
inner join amartins..empresa.cliente i on i.cliente=c.MigrationID
inner join amartins.tblpnprocesses k on k.pk=c.fkprocess
left outer join bigbang.tblUser2 u on u.MigrationID=s.GESTORAPOL;

update amartins.tblBBPolicies set FKProcess=p.PK
from amartins.tblBBPolicies c inner join amartins.tblPNProcesses p on p.FKData=c.PK;

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'08C796D6-5622-4FF1-B3AB-9FF300F28ABC' FKScript, b.PK FKData, isnull(u.FKUser, k.FKManager) FKManager,
p.FKProcess FKParent, 0 IsRunning
from amartins.tblBBSubPolicies b
inner join amartins..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join amartins..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblpnprocesses k on k.pk=p.fkprocess
left outer join bigbang.tblUser2 u on u.MigrationID=ss.GESTORAPOL
where ss.ramo in (10708, 10710);

update amartins.tblBBSubPolicies set FKProcess=p.PK
from amartins.tblBBSubPolicies c inner join amartins.tblPNProcesses p on p.FKData=c.PK;

update amartins.tblBBPolicies set FKClient=c.PK
from amartins.tblBBPolicies p
inner join amartins.tblPNProcesses g on g.PK=p.FKProcess
inner join amartins.tblPNProcesses h on h.PK=g.FKParent
inner join amartins.tblBBClients c on c.PK=h.FKData;

ALTER TABLE [amartins].[tblBBPolicies]
ALTER COLUMN [FKClient] [uniqueidentifier] NOT NULL;
