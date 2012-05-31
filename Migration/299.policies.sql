insert into credite_egs.tblPolicyCoInsurers (PK, FKPolicy, FKCompany, [Percent])
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, c.PK FKCompany, cs.[percent] [Percent]
from credegs..empresa.cosseguro cs
inner join credegs..empresa.apolice s on s.cliente=cs.cliente and s.apolice=cs.apolice and s.ramo=cs.ramo and s.comseg=cs.comseg
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblCompanies c on c.MigrationID=cs.cocomseg;

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'29145166-59AC-452E-8C2B-9F81013A39AC' FKScript, p.PK FKData, isnull(u.FKUser, k.FKManager) FKManager,
c.FKProcess FKParent, 0 IsRunning
from credite_egs.tblBBPolicies p
inner join credegs..empresa.apolice s on s.MigrationID=p.MigrationID
inner join credite_egs.tblBBClients c on c.MigrationID=s.cliente
inner join credegs..empresa.cliente i on i.cliente=c.MigrationID
inner join credite_egs.tblpnprocesses k on k.pk=c.fkprocess
left outer join bigbang.tblUser2 u on u.MigrationID=s.GESTORAPOL;

update credite_egs.tblBBPolicies set FKProcess=p.PK
from credite_egs.tblBBPolicies c inner join credite_egs.tblPNProcesses p on p.FKData=c.PK;

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'08C796D6-5622-4FF1-B3AB-9FF300F28ABC' FKScript, b.PK FKData, isnull(u.FKUser, k.FKManager) FKManager,
p.FKProcess FKParent, 0 IsRunning
from credite_egs.tblBBSubPolicies b
inner join credegs..empresa.apolice ss on ss.MigrationID=b.MigrationID
inner join credegs..empresa.apolice s on s.cliente=ss.texto4 and s.apolice=ss.texto1 and s.ramo=ss.texto2 and s.comseg=ss.texto3
inner join credite_egs.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join credite_egs.tblpnprocesses k on k.pk=p.fkprocess
left outer join bigbang.tblUser2 u on u.MigrationID=ss.GESTORAPOL
where ss.ramo in (10708, 10710);

update credite_egs.tblBBSubPolicies set FKProcess=p.PK
from credite_egs.tblBBSubPolicies c inner join credite_egs.tblPNProcesses p on p.FKData=c.PK;



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
