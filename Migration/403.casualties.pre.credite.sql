insert into credite_egs.tblBBCasualties (PK, CNumber, FKProcess, CasualtyDate, Description, Notes, BCaseStudy)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.ordem CNumber, NULL FKProcess, s.datasinistro CausaltyDate, s.sinistrado Description, substring(s.observ, 1, 250) Notes, 0 BCaseStudy
from credegs..empresa.sinistros s
where (s.ordem>=20110000 or s.datasinistro>'2010-12-31' or s.dataparticipacao>'2010-12-31' or s.datafecho>'2010-12-31' or s.estado='A' or s.estado is NULL)
and s.cliente in (select MigrationID from credite_egs.tblBBClients)

insert into credite_egs.tblBBSubCasualties (PK, SCNumber, FKProcess, FKPolicy, FKSubPolicy, ExternProcess, Description, Notes, BHasJudicial, GenericObject)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.ordem+0.1 SCNumber, NULL FKProcess, p.PK FKPolicy, z.PK FKSubPolicy, NULL ExternProcess, NULL Description, substring(s.obsint, 1, 250) Notes, 0 BHasJudicial, s.sinistrado
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
where u.Username='sandra';

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

update p2 set fkmanager=(select pk from bigbang.tblusers where username=
case z.pk when '7F5F77EB-8348-4914-8525-9EE9010AB1C6' then 'poliveira' when '6DB46133-2789-4B5E-B8FF-9EE9010A8E9D' then 'sandra' else
case x.pk when '457B9EA9-0CB7-41B2-AE89-9F340153C9AE' then 'poliveira' else
case when g.groupname like '%mundicenter%' or g.groupname like '%indaqua%' or g.groupname like '%acciona%' or g.groupname like '%tertir%'  or l.clientnumber=13086 then 'sandrav'
when l.clientnumber=12962 then 'sandra'
when l.clientnumber in (16273, 16274, 16275) then 'poliveira' else
case when x.pk='E91E32C6-0C55-4F49-BA88-9F340153FD6D' or z.pk='537C3D97-02C9-41EE-A5DE-9EE9010AF1BB' or y.pk in ('CBCB9041-8012-46D7-B8F2-9EE901111406', '2D7CCD94-DBD5-43F1-91B5-9EE9010E81DD', 'F22E2E10-1993-4784-86A3-9EE90110F982', 'BACE0C3D-6251-42D3-B9F3-9EE9010EB5D5', '8901C038-BA37-4F40-8359-9EE901112634', '47FAC08E-F9F0-4695-9CB7-9EE901111DC3', '846652EE-1711-4869-8290-9EE90111A853', '4C475D8E-47D9-4ABC-8FB1-9EE90111B67A', '7DF2A66C-1200-430D-848A-9EE90111AF93') then 'sandra'
when z.pk in ('56A359A3-33DF-40E5-B004-9EE9010A726A', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34', '53DB03E7-F423-4656-A23A-9EE9010A5B87', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D', '787B047E-7B15-4DA6-992D-9EE9010A857F', 'A3F44708-4666-425C-AA70-9EE9010A9E0D', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44', 'E2732D85-E204-4A71-A5D4-9EE9010AC7B1', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157', '73410713-03BD-4247-84A7-9EE9010AECB7') then 'sandrav'
else 'sandra'
end end end end)
from credite_egs.tblbbsubcasualties s
inner join credite_egs.tblpnprocesses p1 on p1.pk=s.fkprocess
inner join credite_egs.tblpnprocesses p2 on p2.pk=p1.fkparent
inner join credite_egs.tblbbcasualties c on c.pk=p2.fkdata
inner join credite_egs.tblbbpolicies p on p.pk=s.fkpolicy
inner join bigbang.tblbbsublines x on x.pk=p.fksubline
inner join bigbang.tblbblines y on y.pk=x.fkline
inner join bigbang.tbllinecategories z on z.pk=y.fkcategory
inner join credite_egs.tblpnprocesses p3 on p3.pk=p2.fkparent
inner join credite_egs.tblbbclients l on l.pk=p3.fkdata
left outer join credite_egs.tblbbgroups g on g.pk=l.fkgroup;

update p1 set fkmanager=p2.fkmanager
from credite_egs.tblbbsubcasualties s
inner join credite_egs.tblpnprocesses p1 on p1.pk=s.fkprocess
inner join credite_egs.tblpnprocesses p2 on p2.pk=p1.fkparent;

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
