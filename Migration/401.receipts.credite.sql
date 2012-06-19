insert into credite_egs.tblBBReceipts (PK, ReceiptNumber, FKReceiptType, FKProcess, TotalPremium, CommercialPremium, Commissions, Retrocessions, FATValue, IssueDate, MaturityDate,
EndDate, DueDate, FKMediator, ReceiptNotes, ReceiptDescription, ReturnText, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.recibo ReceiptNumber, t.PK FKReceiptType, NULL FKProcess, r.valorrec TotalPremium, NULL CommercialPremium, r.valorcom Commissions, isnull(r.valm, 0) Retrocessions, r.valc FATValue,
NULL IssueDate, r.datavenc MaturityDate, r.datavencate EndDate, r.datalimite DueDate, NULL FKMediator, substring(r.observ, 1, 250) ReceiptNotes, r.designacao ReceiptDescription,
r.motivo ReturnText, r.MigrationID MigrationID
from credegs..empresa.recibo r
inner join bigbang.tblReceiptTypes t on t.TypeIndicator collate database_default = r.tiporec collate database_default
inner join credegs..empresa.apolice s on s.cliente=r.cliente and s.apolice=r.apolice and s.comseg=r.comseg and s.ramo=r.ramo
where (r.datarec>'2010-12-31' or r.datavenc>'2010-12-31')
and (s.MigrationID in (select MigrationID from credite_egs.tblBBPolicies) or s.MigrationID in (select MigrationID from credite_egs.tblBBSubPolicies));

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from credite_egs.tblBBReceipts r
inner join credegs..empresa.recibo s on s.MigrationID=r.MigrationID
inner join credegs..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.comseg=s.comseg and a.ramo=s.ramo
inner join credite_egs.tblBBPolicies p on p.MigrationID=a.MigrationID
inner join credite_egs.tblpnprocesses k on k.pk=p.fkprocess;

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 0 IsRunning
from credite_egs.tblBBReceipts r
inner join credegs..empresa.recibo s on s.MigrationID=r.MigrationID
inner join credegs..empresa.apolice a on a.cliente=s.cliente and a.apolice=s.apolice and a.comseg=s.comseg and a.ramo=s.ramo
inner join credite_egs.tblBBSubPolicies p on p.MigrationID=a.MigrationID
inner join credite_egs.tblpnprocesses k on k.pk=p.fkprocess;

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, NULL FKUser, NULL FKSourceLog
from credite_egs.tblPNProcesses p
inner join bigbang.tblPNOperations o on o.FKScript=p.FKScript
where p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from credite_egs.tblPNProcesses p
inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

/** Perfil simples (AMartins) **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and n.FKController='A1DE70D7-61B1-49F1-BEA0-A01300D67D6D')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))

/** Estornos ou sinistros **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('E7B800AC-5EC8-41A0-AEC9-A01300D751F3', '3DD47B13-A36B-4F31-8717-A01300D782F6'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')

/** Com imagem ou por conferir(01+) **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where (x.docushare is not null or (c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB' and x.estado>'00'))
and n.FKController='FF6DDAAC-9699-477C-AA8E-A01300D659FD')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where (x.docushare is not null or (c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB' and x.estado>'00'))
and n.FKController in ('A06F31E3-4406-47B3-A155-A01300D665E6', 'D14DDDBD-02CC-44E2-B344-A01300D67197'))

/** À cobrança, normais **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'A1DE70D7-61B1-49F1-BEA0-A01300D67D6D', '3DD47B13-A36B-4F31-8717-A01300D782F6'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')

/** À cobrança, estornos ou sinistros, não simples **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'A1DE70D7-61B1-49F1-BEA0-A01300D67D6D', 'FF02C532-97D9-422F-95FB-A01300D7E746'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'D7B75D8F-9608-49DE-89B1-A01300D7F102'))

/** À cobrança, estornos ou sinistros, perfil simples (AMartins) **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
inner join credegs..empresa.cliente y on y.cliente=x.cliente
inner join credite_egs.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='D7B75D8F-9608-49DE-89B1-A01300D7F102')

/** Pagos, normais **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))

/** Pagos, estornos ou sinistros **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'D7B75D8F-9608-49DE-89B1-A01300D7F102'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))

/** Prestados **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='D869D165-7E86-444A-835A-A01300D75B06')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='0594F667-06E7-4B4C-9761-A01300D76456')

/** Retrocedidos **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='2FE06BB6-4858-473A-B15B-A01300D76C71')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='A1F9829D-E26E-452B-A20C-A01300D775A0')

/** Devolvidos **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBReceipts r on r.PK=p.FKData
inner join credegs..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('51031667-19D9-48A1-A4FE-A01300D7A207', 'CEC07468-F1E1-46A5-9D67-A01300D7B67B'))

/** Estado final **/

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join credite_egs.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0
and o.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC')
