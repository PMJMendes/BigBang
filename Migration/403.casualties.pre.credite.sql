insert into credite_egs.tblBBCasualties (PK, CNumber, FKProcess, CasualtyDate, Description, Notes, BCaseStudy)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.ordem CNumber, NULL FKProcess, s.datasinistro CausaltyDate, s.sinistrado Description, substring(s.observ, 1, 250) Notes, 0 BCaseStudy
from credegs..empresa.sinistros s
where (s.ordem>=20110000 or s.datasinistro>'2010-12-31' or s.dataparticipacao>'2010-12-31' or s.datafecho>'2010-12-31' or s.estado='A' or s.estado is NULL)
and s.cliente in (select MigrationID from credite_egs.tblBBClients)

insert into credite_egs.tblBBSubCasualties (PK, SCNumber, FKProcess, FKPolicy, FKSubPolicy, ExternProcess, Description, Notes, BHasJudicial)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.ordem+0.1 SCNumber, NULL FKProcess, p.PK FKPolicy, z.PK FKSubPolicy, NULL ExternProcess, NULL Description, NULL Notes, 0 BHasJudicial
from credegs..empresa.sinistros s
inner join credegs..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.ramo=s.ramo and a.comseg=s.comseg
left outer join credite_egs.tblBBPolicies p on p.MigrationID=a.MigrationID
left outer join credite_egs.tblBBSubPolicies z on z.MigrationID=a.MigrationID
where (s.ordem>=20110000 or s.datasinistro>'2010-12-31' or s.dataparticipacao>'2010-12-31' or s.datafecho>'2010-12-31' or s.estado='A' or s.estado is NULL)
and s.ordem in (select CNumber from credite_egs.tblBBCasualties)
and (p.PK is not null or z.PK is not null)
and s.cliente in (select MigrationID from credite_egs.tblBBClients)

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'6C9562DE-D8C6-4CEC-A4DA-A02E00C735E9' FKScript, c.PK FKData, u.PK FKManager,
k.FKProcess FKParent, 0 IsRunning
from credite_egs.tblBBCasualties c
inner join credegs..empresa.sinistros s on s.ordem=c.CNumber
inner join credite_egs.tblBBClients k on k.MigrationID=s.cliente,
bigbang.tblUsers u
where u.Username='sandrav';

update credite_egs.tblBBCasualties set FKProcess=p.PK
from credite_egs.tblBBCasualties c inner join credite_egs.tblPNProcesses p on p.FKData=c.PK;

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'80B7A9BC-8710-4063-A99E-A02E01220F4E' FKScript, s.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from credite_egs.tblBBSubCasualties s
inner join credite_egs.tblBBCasualties c on c.CNumber + '.1' = s.SCNumber
inner join credite_egs.tblpnprocesses k on k.pk=c.fkprocess;

update credite_egs.tblBBSubCasualties set FKProcess=p.PK
from credite_egs.tblBBSubCasualties s inner join credite_egs.tblPNProcesses p on p.FKData=s.PK;

/** Histórico **/

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '4FC7D258-3CBB-4174-AD53-A02E01094F3D' FKOperation, s.datafecho TStamp, p.FKManager FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBCasualties c
inner join credegs..empresa.sinistros s on s.ordem=c.CNumber
inner join credite_egs.tblPNProcesses p on p.FKData=c.PK
where s.estado='F' and s.datafecho is not null;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '207F3FEC-0CB2-47D4-868A-A03600FEE4FB' FKOperation, s.datafecho TStamp, p.FKManager FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBSubCasualties c
inner join credegs..empresa.sinistros s on s.ordem + 0.1 = c.SCNumber
inner join credite_egs.tblPNProcesses p on p.FKData=c.PK
where s.estado='F' and s.datafecho is not null;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '2A0EF8C0-ECFA-4622-9C72-A03600FACBCB' FKOperation, s.dataparticipacao TStamp, p.FKManager FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBSubCasualties c
inner join credegs..empresa.sinistros s on s.ordem + 0.1 = c.SCNumber
inner join credite_egs.tblPNProcesses p on p.FKData=c.PK
where s.dataparticipacao is not null;
