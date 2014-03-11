insert into [bigbang].[tblPNOperations] ([PK],[_TSCreate],[_TSUpdate],[FKType],[OpName],[FKScript],[FKRole],[FKDefaultLevel],[FKSourceOp],[ClassName],[FKUndoOp]) values('21a98932-1495-4d58-8dce-a2eb00b488af','2014-03-11T10:57:18.257','2014-03-11T10:57:18.257','30fbd723-9acd-43f9-b931-9e1600dc051a',N'Desfazer Circuito Simples','62d0a72a-525e-450c-9917-9f8a00eb38ac','f720401e-96c5-485b-9e8c-9f090146ea2b','7925ef60-80fc-4ea2-96a0-9eb1007ea1ff',null,N'com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerUndoShortCircuit',null);
insert into [bigbang].[tblPNSinks] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKController],[FKOperation]) values('b1dd904d-c04d-4a34-91aa-a2eb00b792cd','2014-03-11T11:08:22.240','2014-03-11T11:08:22.240','26f6690d-efd4-4863-bcfa-9e1600e2c89b','b3e4e1fb-ce05-4ec7-b93b-a01300d6c73e','21a98932-1495-4d58-8dce-a2eb00b488af');
insert into [bigbang].[tblPNSinks] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKController],[FKOperation]) values('28bb9869-f9e9-444f-b54e-a2eb00b79fbc','2014-03-11T11:08:33.270','2014-03-11T11:08:33.270','26f6690d-efd4-4863-bcfa-9e1600e2c89b','9b7b3312-1bfc-4447-b970-a01300d78cb2','21a98932-1495-4d58-8dce-a2eb00b488af');
insert into [bigbang].[tblPNSources] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOperation],[FKController]) values('0535d349-f3de-497a-873a-a2eb00b77816','2014-03-11T11:07:59.440','2014-03-11T11:07:59.440','3189e3b3-0dcc-4580-a966-9e1600e2ee41','1eb5ca7c-01d0-4120-baa2-a01300c8e4bd','9b7b3312-1bfc-4447-b970-a01300d78cb2');
insert into [bigbang].[tblPNSources] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOperation],[FKController]) values('9edab713-339f-425f-ba3d-a2eb00b7af6b','2014-03-11T11:08:46.653','2014-03-11T11:08:46.653','3189e3b3-0dcc-4580-a966-9e1600e2ee41','21a98932-1495-4d58-8dce-a2eb00b488af','a1de70d7-61b1-49f1-bea0-a01300d67d6d');

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK='21A98932-1495-4D58-8DCE-A2EB00B488AF'
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK='21A98932-1495-4D58-8DCE-A2EB00B488AF'
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

update n set NodeCount=0
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNSteps s on s.FKProcess=l.FKProcess
inner join credite_egs.tblPNNodes n on n.FKProcess=l.FKProcess
inner join credite_egs.tblBBReceipts r on r.FKProcess=l.FKProcess
inner join credite_egs.tblBBPolicies p on p.PK=r.FKPolicy
inner join credite_egs.tblBBClients c on c.PK=p.FKClient
where s.FKOperation='47538724-7B79-4655-96D7-A01300C6A881' and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
and n.FKController='D14DDDBD-02CC-44E2-B344-A01300D67197'
and l.FKOperation in ('F5F00701-69F7-4622-BB8C-9FB800DED93F', 'A34C65D4-0B2A-4083-BB16-A01300D3D013') and l.Undone=0
and ((p.FKProfile is not null and p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F')
or (p.FKProfile is null and c.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F'));

update n set NodeCount=1
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNNodes n on n.FKProcess=l.FKProcess
inner join credite_egs.tblBBReceipts r on r.FKProcess=l.FKProcess
inner join credite_egs.tblBBPolicies p on p.PK=r.FKPolicy
inner join credite_egs.tblBBClients c on c.PK=p.FKClient
where n.FKController='9B7B3312-1BFC-4447-B970-A01300D78CB2'
and l.FKOperation in ('47538724-7B79-4655-96D7-A01300C6A881', '2A501358-24D2-4877-BA9C-A01300C6E1B7') and l.Undone=0
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '4775B720-B90B-4279-B008-9F900111E6AD')
and ((p.FKProfile is not null and p.FKProfile in ('51ED12A4-95A9-44B0-928D-A01500DC83EB', '8641B5C6-CABE-4260-AD70-A0A2010A028F'))
or (p.FKProfile is null and c.FKProfile in ('51ED12A4-95A9-44B0-928D-A01500DC83EB', '8641B5C6-CABE-4260-AD70-A0A2010A028F')));

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('21A98932-1495-4D58-8DCE-A2EB00B488AF')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', '9B7B3312-1BFC-4447-B970-A01300D78CB2')
and n.NodeCount=0;

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('47538724-7B79-4655-96D7-A01300C6A881', '2A501358-24D2-4877-BA9C-A01300C6E1B7', '38DB0FE1-B1DE-4C7B-B7CA-A01300C5BAC6', '9C9369BB-186D-433B-BD7B-A01300CE9125')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197')
and n.NodeCount=0;

update n set NodeCount=0
from amartins.tblPNLogs l
inner join amartins.tblPNSteps s on s.FKProcess=l.FKProcess
inner join amartins.tblPNNodes n on n.FKProcess=l.FKProcess
inner join amartins.tblBBReceipts r on r.FKProcess=l.FKProcess
inner join amartins.tblBBPolicies p on p.PK=r.FKPolicy
inner join amartins.tblBBClients c on c.PK=p.FKClient
inner join amartins.tblPNLogs l2 on l2.FKProcess=l.FKProcess
where s.FKOperation='47538724-7B79-4655-96D7-A01300C6A881' and s.FKLevel!='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
and n.FKController='D14DDDBD-02CC-44E2-B344-A01300D67197'
and l.FKOperation in ('F5F00701-69F7-4622-BB8C-9FB800DED93F', 'A34C65D4-0B2A-4083-BB16-A01300D3D013') and l.Undone=0
and ((p.FKProfile is not null and p.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F')
or (p.FKProfile is null and c.FKProfile!='8641B5C6-CABE-4260-AD70-A0A2010A028F'));

update n set NodeCount=1
from amartins.tblPNLogs l
inner join amartins.tblPNNodes n on n.FKProcess=l.FKProcess
inner join amartins.tblBBReceipts r on r.FKProcess=l.FKProcess
inner join amartins.tblBBPolicies p on p.PK=r.FKPolicy
inner join amartins.tblBBClients c on c.PK=p.FKClient
where n.FKController='9B7B3312-1BFC-4447-B970-A01300D78CB2'
and l.FKOperation in ('47538724-7B79-4655-96D7-A01300C6A881', '2A501358-24D2-4877-BA9C-A01300C6E1B7') and l.Undone=0
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '4775B720-B90B-4279-B008-9F900111E6AD')
and ((p.FKProfile is not null and p.FKProfile in ('51ED12A4-95A9-44B0-928D-A01500DC83EB', '8641B5C6-CABE-4260-AD70-A0A2010A028F'))
or (p.FKProfile is null and c.FKProfile in ('51ED12A4-95A9-44B0-928D-A01500DC83EB', '8641B5C6-CABE-4260-AD70-A0A2010A028F')));

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNSteps s
inner join amartins.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('21A98932-1495-4D58-8DCE-A2EB00B488AF')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', '9B7B3312-1BFC-4447-B970-A01300D78CB2')
and n.NodeCount=0;

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNSteps s
inner join amartins.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('47538724-7B79-4655-96D7-A01300C6A881', '2A501358-24D2-4877-BA9C-A01300C6E1B7', '38DB0FE1-B1DE-4C7B-B7CA-A01300C5BAC6', '9C9369BB-186D-433B-BD7B-A01300CE9125')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197')
and n.NodeCount=0;
