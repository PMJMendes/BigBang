insert into credite_egs.tblBBExpenses (PK, ENumber, FKProcess, ExpenseDate, FKPolicyObject, FKSubPolicyObject, FKPolicyCoverage, FKSubPolicyCoverage, Damages, Settlement, BManual, Notes, Rejection)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
e.IDDesp ENumber, NULL FKProcess, e.DataDesp ExpenseDate, o.PK FKPolicyObject, NULL FKSubPolicyObject, c.PK FKPolicyCoverage, NULL FKSubPolicyCoverage
e.Valor Damages, e.ValorComp Settlement, 1 BManual, NULL Notes, Null Rejection
from credegs..empresa.saudedespesas e
inner join credegs..empresa.apolice a on a.cliente=e.cliente and a.apolice=e.apolice and a.ramo=e.ramo and a.comseg=e.comseg
inner join credite_egs.tblBBPolicies p on p.MigrationID=a.MigrationID
inner join credite_egs.tblBBPolicyCoverages c on c.FKPolicy=p.PK and c.FKCoverage=case
when e.FKCob=1 then '93CF04E9-8966-436F-9FD0-9F9100FD05A9'
when e.FKCob=2 then '8813E06E-47EF-4045-A518-9F9100FD15A4'
when e.FKCob=3 then '416992A3-83F2-4A03-BE3D-9F9100FD24CA'
when e.FKCob in (4, 5, 6, 7, 12) then 'FF6932CF-D874-4E3B-86CE-9F9100FD348C'
when e.FKCob=8 then '05CAB911-48E9-4163-BA86-9F9100FD1D4A'
when e.FKCob in (9, 10, 11) then 'D6D2A150-07DC-4D90-BD05-9F9100FD0C99' end
inner join credite_egs.tblInsuredObjects o on o.FKPolicy=p.PK and o.ObjName=CAST(e.NumObjecto AS NVARCHAR(50))
where e.FKSt<4 or e.DataDesp>'2010-12-31' or e.IDDesp in
(select FKDesp from credegs..empresa.saudehistorico where MvtDate>'2010-12-31');

insert into credite_egs.tblBBExpenses (PK, ENumber, FKProcess, ExpenseDate, FKPolicyObject, FKSubPolicyObject, FKPolicyCoverage, FKSubPolicyCoverage, Damages, Settlement, BManual, Notes, Rejection)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
e.IDDesp ENumber, NULL FKProcess, e.DataDesp ExpenseDate, NULL FKPolicyObject, o.PK FKSubPolicyObject, NULL  FKPolicyCoverage, c.PK FKSubPolicyCoverage,
e.Valor Damages, e.ValorComp Settlement, 1 BManual, NULL Notes, Null Rejection
from credegs..empresa.saudedespesas e
inner join credegs..empresa.apolice a on a.cliente=e.cliente and a.apolice=e.apolice and a.ramo=e.ramo and a.comseg=e.comseg
inner join credite_egs.tblBBSubPolicies s on s.MigrationID=a.MigrationID
inner join credite_egs.tblSubPolicyObjects o on o.FKSubPolicy=s.PK and o.MigrationAux=e.NumObjecto
inner join credite_egs.tblBBSubPolicyCoverages c on c.FKSubPolicy=s.PK and c.FKCoverage=case
when e.FKCob=1 then '93CF04E9-8966-436F-9FD0-9F9100FD05A9'
when e.FKCob=2 then '8813E06E-47EF-4045-A518-9F9100FD15A4'
when e.FKCob=3 then '416992A3-83F2-4A03-BE3D-9F9100FD24CA'
when e.FKCob in (4, 5, 6, 7, 12) then 'FF6932CF-D874-4E3B-86CE-9F9100FD348C'
when e.FKCob=8 then '05CAB911-48E9-4163-BA86-9F9100FD1D4A'
when e.FKCob in (9, 10, 11) then 'D6D2A150-07DC-4D90-BD05-9F9100FD0C99' end
where e.FKSt<4 or e.DataDesp>'2010-12-31' or e.IDDesp in
(select FKDesp from credegs..empresa.saudehistorico where MvtDate>'2010-12-31');

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'A4EFBA8F-D669-4066-A31F-A03800BFB924' FKScript, e.PK FKData, k.FKManager FKManager, k.PK FKParent, 0 IsRunning
from credite_egs.tblBBExpenses e
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.apolice a on a.cliente=d.cliente and a.apolice=d.apolice and a.ramo=d.ramo and a.comseg=d.comseg
inner join credite_egs.tblBBPolicies p on p.MigrationID=a.MigrationID
inner join credite_egs.tblPNProcesses k on k.PK=p.FKProcess;

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'A4EFBA8F-D669-4066-A31F-A03800BFB924' FKScript, e.PK FKData, k.FKManager FKManager, k.PK FKParent, 0 IsRunning
from credite_egs.tblBBExpenses e
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.apolice a on a.cliente=d.cliente and a.apolice=d.apolice and a.ramo=d.ramo and a.comseg=d.comseg
inner join credite_egs.tblBBSubPolicies s on s.MigrationID=a.MigrationID
inner join credite_egs.tblPNProcesses k on k.PK=s.FKProcess;

update credite_egs.tblBBExpenses set FKProcess=p.PK
from credite_egs.tblBBExpenses e inner join credite_egs.tblPNProcesses p on p.FKData=e.PK;

/** Histórico **/

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '0E69C7E2-78B3-4A53-B6C5-A03800C0B1B6' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=1 and h.FkStDepois=2;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '4649DEBD-80B1-43AA-A292-A03800C0D14B' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=2 and h.FkStDepois=3 and e.Settlement>0;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '14EAD818-E313-4F80-B781-A03800C324EA' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=2 and h.FkStDepois=3 and e.Settlement=0;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '64F5A5D2-8A9D-48B4-9A8D-A03800C2CFFC' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=3 and h.FkStDepois=4 and e.Settlement>0;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, 'C675919E-A13D-459F-9E1F-A03800C3F021' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=3 and h.FkStDepois=4 and e.Settlement=0;

insert into credite_egs.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, '0EEB387C-661F-4FB0-8675-A03800C5DE73' FKOperation, h.MvtDate TStamp, u.FKUser FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from credite_egs.tblBBExpenses e
inner join credite_egs.tblPNProcesses p on p.FKData=e.PK
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
inner join credegs..empresa.saudehistorico h on h.FKDesp=d.IDDesp
inner join bigbang.tblUser2 u on u.MigrationID=h.MvtUser
where h.FkStAntes=3 and h.FkStDepois=4;
