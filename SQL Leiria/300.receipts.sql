delete from bbleiria.tblBBReceipts;

delete from bbleiria.tblPNLogs where FKProcess in
(select PK from bbleiria.tblPNProcesses where FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC')

delete from bbleiria.tblPNSteps where FKProcess in
(select PK from bbleiria.tblPNProcesses where FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC');

delete from bbleiria.tblPNNodes where FKProcess in
(select PK from bbleiria.tblPNProcesses where FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC');

delete from bbleiria.tblPNLogs where FKExternProcess in
(select PK from bbleiria.tblPNProcesses where FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC');

delete from bbleiria.tblPNProcesses where FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into bbleiria.tblBBReceipts (PK, ReceiptNumber, FKReceiptType, FKProcess, TotalPremium, CommercialPremium, Commissions, Retrocessions, FATValue, IssueDate, MaturityDate,
EndDate, DueDate, FKMediator, ReceiptNotes, ReceiptDescription, ReturnText, MigrationID, BonusMalus, BIsMalus, BIsInternal, EntryNumber, EntryYear, FKStatus, FKPolicy,
FKSubPolicy, FKSubCasualty)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.NumRecibo ReceiptNumber, rt.PK FKReceiptType, NULL FKProcess, ABS(r.PTotal) TotalPremium, ABS(r.PComercial) CommercialPremium,
ABS(ISNULL(r.ComAng, 0) + ISNULL(r.ComCob, 0) + ISNULL(r.ComCorr, 0)) Commissions, ABS(r.ComSubAg) Retrocessions,
NULL FATValue, r.DtLivre IssueDate, r.DtInicio MaturityDate, r.DtFim EndDate, r.DtLimite DueDate, NULL FKMediator,
'Devolvido por: ' + ISNULL(r.MotivoDevol, '') ReceiptNotes, r.Matricula ReceiptDescription, r.MotivoDevol ReturnText, AutoInc MigrationID, r.Bonus BonusMalus, 0 BIsMalus,
0 BIsInternal, NULL EntryNumber, NULL EntryYear, '37C3A6F7-A579-4CD2-842E-A02000C337AA' FKStatus, p.PK FKPolicy, NULL FKSubPolicy, NULL FKSubCasualty
from bbleiria.tblBBPolicies p
inner join SEGEST_SEGAPO s on right('000000000000000'+s.Numero, 15)=right('000000000000000'+p.PolicyNumber, 15) collate database_default
inner join bbleiria.tblCompanies c on c.PK=p.FKCompany
inner join SEGEST_SEGCOMP a on a.Codigo=s.Companhia
inner join bbleiria.tblCompanies c2 on c2.MigrationID=a.Codigo
inner join SEGEST_SEGREC r on r.Apolice=s.Numero
inner join bigbang.tblReceiptTypes rt on rt.TypeIndicator=r.GeneroRec collate database_default
where p.PolicyNumber not in (select PolicyNumber from bbleiria.tblBBPolicies group by PolicyNumber having count(PK)>1)
and s.Numero not in (select Numero from SEGEST_SEGAPO group by Numero having count(Numero)>1)
and (c.MigrationID=a.Codigo
or (c.MigrationID!=a.Codigo
and left(c2.ShortName, 3)=left(c.ShortName, 3)));

update r set ReceiptNotes=NULL
from bbleiria.tblBBReceipts r
inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where s.Situacao!='D' or s.MotivoDevol='FALTA PAGAMENTO';

/* Override Actas */

update bbleiria.tblBBReceipts set FKReceiptType='3B127029-C133-4EB4-AD1E-9F900111EF2A' where FKReceiptType='382ABABA-5A0A-4E88-B5D1-A09E00EE2006';

/* Sinal dos valores nos estornos */

update bbleiria.tblBBReceipts set
TotalPremium = -TotalPremium,
CommercialPremium = -CommercialPremium,
Commissions = -Commissions,
Retrocessions = -Retrocessions,
BonusMalus = -BonusMalus
where FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967';

/* Status */

update r set FKStatus='ABCA5C34-078C-4382-8FCB-A17800FE83EC'
from bbleiria.tblBBReceipts r
inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where s.Situacao in ('D', 'R');

update r set FKStatus='767EB803-6669-47EF-8649-A02000C3491F'
from bbleiria.tblBBReceipts r
inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where s.Situacao='F' and s.DtAvCob is not null;

update r set FKStatus='C359D3FF-9032-4B4D-8C34-A02000C3403D'
from bbleiria.tblBBReceipts r
inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where s.Situacao='P';

update r set FKStatus='767EB803-6669-47EF-8649-A02000C3491F'
from bbleiria.tblBBReceipts r
inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy
where p.FKProfile='8641B5C6-CABE-4260-AD70-A0A2010A028F'
and r.FKStatus='37C3A6F7-A579-4CD2-842E-A02000C337AA'

/* Processos */

insert into bbleiria.tblPNProcesses (PK, FKScript, FKData, FKManager, FKParent, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'62D0A72A-525E-450C-9917-9F8A00EB38AC' FKScript, r.PK FKData, k.FKManager FKManager,
k.PK FKParent, 1 IsRunning
from bbleiria.tblBBReceipts r
inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy
inner join bbleiria.tblPNProcesses k on k.PK=p.FKProcess;

insert into bbleiria.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, NULL FKUser, NULL FKSrouceLog
from bbleiria.tblPNProcesses p
inner join bigbang.tblPNOperations o on o.FKScript=p.FKScript
where p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into bbleiria.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bbleiria.tblPNProcesses p
inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

update r set FKProcess=p.PK
from bbleiria.tblBBReceipts r
inner join bbleiria.tblPNProcesses p on p.FKData=r.PK;

/* Estados */

/* Bloquear retrocessão - ATENÇÂO: Não sabemos o agente! */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('2FE06BB6-4858-473A-B15B-A01300D76C71')
and FKProcess not in (select FKProcess from bbleiria.tblBBReceipts where Retrocessions>0);

/* Estornos */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('3DD47B13-A36B-4F31-8717-A01300D782F6', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3')
and FKProcess in (select FKProcess from bbleiria.tblBBReceipts where FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967');

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('FF02C532-97D9-422F-95FB-A01300D7E746')
and FKProcess in (select FKProcess from bbleiria.tblBBReceipts where FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967');

/* Externos */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('A1DE70D7-61B1-49F1-BEA0-A01300D67D6D', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy where p.FKProfile='8641B5C6-CABE-4260-AD70-A0A2010A028F');

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', '9B7B3312-1BFC-4447-B970-A01300D78CB2')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy where p.FKProfile='8641B5C6-CABE-4260-AD70-A0A2010A028F');

/* Marcações para Devolução */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', '423E416A-E89B-4BD8-9179-A12A01196373')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D' and (s.MotivoDevol is null or s.MotivoDevol!='FALTA PAGAMENTO'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('F7C1344C-11F2-4772-89C6-A01300D7AC33', 'CEC07468-F1E1-46A5-9D67-A01300D7B67B')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D' and (s.MotivoDevol is null or s.MotivoDevol!='FALTA PAGAMENTO'));

/* Validações */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and (s.Situacao!='D' or s.MotivoDevol='FALTA PAGAMENTO'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('EB445293-5B7A-4150-BE73-A01300D68985')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and (s.Situacao!='D' or s.MotivoDevol='FALTA PAGAMENTO'));

/* Avisos de Cobrança */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('EB445293-5B7A-4150-BE73-A01300D68985', '3DD47B13-A36B-4F31-8717-A01300D782F6', 'A1DE70D7-61B1-49F1-BEA0-A01300D67D6D')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO')));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('5AF77A4E-857F-4957-835E-A01300D6B99A', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO')));

/* Pedidos de Assinatura */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('EB445293-5B7A-4150-BE73-A01300D68985', 'FF02C532-97D9-422F-95FB-A01300D7E746')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO')));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('09AFA5A6-D316-4713-8CE4-A01300D7FAA6')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO')));

/* Faltas de Pagamento */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO');

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('51031667-19D9-48A1-A4FE-A01300D7A207', 'F7C1344C-11F2-4772-89C6-A01300D7AC33')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO');

/* Devoluções à Seguradora */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('F7C1344C-11F2-4772-89C6-A01300D7AC33')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D');

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('74AF71D5-FD90-42E0-91FE-A10E00CD7B9E')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D');

/* Recepção de Assinaturas */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('09AFA5A6-D316-4713-8CE4-A01300D7FAA6')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and s.Situacao in ('P', 'R'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('D7B75D8F-9608-49DE-89B1-A01300D7F102', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and s.Situacao in ('P', 'R'));

/* Cobranças */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao in ('P', 'R'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao in ('P', 'R'));

/* Envio de Recibos */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R'));

/* Envio de Pagamento */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('D7B75D8F-9608-49DE-89B1-A01300D7F102', 'B26DEA4E-157B-4706-8A4D-A01300D72075')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R'));

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R'));

/* Prestação de Contas */

update bbleiria.tblPNNodes set NodeCount=0 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', 'D869D165-7E86-444A-835A-A01300D75B06', '423E416A-E89B-4BD8-9179-A12A01196373')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='R');

update bbleiria.tblPNNodes set NodeCount=1 where FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '0594F667-06E7-4B4C-9761-A01300D76456', '423E416A-E89B-4BD8-9179-A12A01196373')
and FKProcess in (select r.FKProcess from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='R');

/* Cleanup Processos */

update bbleiria.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from bbleiria.tblPNSteps s
inner join bbleiria.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join bbleiria.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC' and n.NodeCount=0);

/* Histórico */

/* Marcações para Devolução */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '9C9369BB-186D-433B-BD7B-A01300CE9125' FKOperation, ISNULL(s.DtAvCob, s.DtPagamento) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where s.Situacao='D' and (s.MotivoDevol is null or s.MotivoDevol!='FALTA PAGAMENTO');

/* Validações */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '2A501358-24D2-4877-BA9C-A01300C6E1B7' FKOperation, ISNULL(s.DtAvCob, ISNULL(s.DtPagamento, s.DtLivre)) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and (s.Situacao!='D' or s.MotivoDevol='FALTA PAGAMENTO');

/* Avisos de Cobrança */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '102E9D79-A006-4757-ADBA-A01300C863B1' FKOperation, ISNULL(s.DtAvCob, s.DtPagamento) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO'));

/* Pedidos de Assinatura */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '563A630A-276A-48E8-96D3-A01300C9AF24' FKOperation, ISNULL(s.DtAvCob, s.DtPagamento) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and (s.Situacao in ('P', 'R') or (s.Situacao='F' and s.DtAvCob is not null) or (s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO'));

/* Faltas de Pagamento */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '3323CA57-827F-4EE5-94B7-A01300CDD793' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D' and s.MotivoDevol='FALTA PAGAMENTO';

/* Devoluções à Seguradora */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, 'A34C65D4-0B2A-4083-BB16-A01300D3D013' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='D';

/* Recepção de Assinaturas */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '04C24A5B-5CED-4CC3-A983-A01300CC84EC' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F') and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967'
and s.Situacao in ('P', 'R');

/* Cobranças */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, 'F5F00701-69F7-4622-BB8C-9FB800DED93F' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao in ('P', 'R');

/* Envio de Recibos */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '002A20EF-A7B3-4DE7-B62C-A01300CC5450' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F')
and r.FKReceiptType!='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R');

/* Envio de Pagamentos */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, '08799EDB-B874-49D5-B987-A01300CCAC59' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join bbleiria.tblBBPolicies p on p.PK=r.FKPolicy inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID
where (p.FKProfile is null or p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F')
and r.FKReceiptType='BFC1AE6D-53E8-41AF-84BE-9F900111D967' and s.Situacao in ('P', 'R');

/* Prestação de Contas */

insert into bbleiria.tblPNLogs (PK, FKProcess, FKOperation, TStamp, FKUser, FKSourceLog, Undone, LogData, FKExternProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.FKProcess, 'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5' FKOperation, ISNULL(s.DtPagamento, s.DtAvCob) TStamp,
'4399662D-826D-483D-AC4C-A0FE00E13F90' FKUser, NULL FKSourceLog, 0 Undone, NULL LogData, NULL FKExternProcess
from bbleiria.tblBBReceipts r inner join SEGEST_SEGREC s on s.AutoInc=r.MigrationID where s.Situacao='R';
