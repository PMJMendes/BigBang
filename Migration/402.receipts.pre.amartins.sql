insert into amartins.tblBBReceipts (PK, ReceiptNumber, FKReceiptType, FKProcess, TotalPremium, CommercialPremium, Commissions, Retrocessions, FATValue, IssueDate, MaturityDate,
EndDate, DueDate, FKMediator, ReceiptNotes, ReceiptDescription, ReturnText, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.recibo ReceiptNumber, t.PK FKReceiptType, NULL FKProcess, r.valorrec TotalPremium, NULL CommercialPremium, r.valorcom Commissions, isnull(r.valm, 0) Retrocessions, r.valc FATValue,
NULL IssueDate, r.datavenc MaturityDate, r.datavencate EndDate, r.datalimite DueDate, NULL FKMediator, substring(r.observ, 1, 250) ReceiptNotes, r.designacao ReceiptDescription,
r.motivo ReturnText, r.MigrationID MigrationID
from amartins..empresa.recibo r
inner join bigbang.tblReceiptTypes t on t.TypeIndicator collate database_default = r.tiporec collate database_default
inner join amartins..empresa.apolice s on s.cliente=r.cliente and s.apolice=r.apolice and s.comseg=r.comseg and s.ramo=r.ramo
where (r.datarec>'2010-12-31' or r.datavenc>'2010-12-31')
and (s.MigrationID in (select MigrationID from amartins.tblBBPolicies) or s.MigrationID in (select MigrationID from amartins.tblBBSubPolicies));

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo s on s.MigrationID=r.MigrationID
inner join amartins..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.comseg=s.comseg and a.ramo=s.ramo
inner join amartins.tblBBPolicies p on p.MigrationID=a.MigrationID
inner join amartins.tblpnprocesses k on k.pk=p.fkprocess;

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from amartins.tblBBReceipts r
inner join amartins..empresa.recibo s on s.MigrationID=r.MigrationID
inner join amartins..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.comseg=s.comseg and a.ramo=s.ramo
inner join amartins.tblBBSubPolicies p on p.MigrationID=a.MigrationID
inner join amartins.tblpnprocesses k on k.pk=p.fkprocess;

update amartins.tblBBReceipts set FKProcess=p.PK
from amartins.tblBBReceipts r inner join amartins.tblPNProcesses p on p.FKData=r.PK;

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
