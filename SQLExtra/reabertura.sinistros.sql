insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join credite_egs.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('5150E007-AE97-40EF-B4F0-A12B00DF23C6')

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join amartins.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('5150E007-AE97-40EF-B4F0-A12B00DF23C6')

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join credite_egs.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('0285FC21-45F1-4EFF-BAEF-A12B00DF7CCA', '1AEB3940-56CC-41D4-BC29-A12B00DE69D5');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join amartins.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('0285FC21-45F1-4EFF-BAEF-A12B00DF7CCA', '1AEB3940-56CC-41D4-BC29-A12B00DE69D5');

update credite_egs.tblPNSteps set FKLevel='B2E2F120-3A20-4678-9873-A12B00C4BAD1'
where FKOperation='1AEB3940-56CC-41D4-BC29-A12B00DE69D5' and FKProcess in
(select FKProcess from credite_egs.tblPNNodes where FKController='0F400849-EB81-4703-8A84-A02E010D62FA' and NodeCount=1)

update amartins.tblPNSteps set FKLevel='B2E2F120-3A20-4678-9873-A12B00C4BAD1'
where FKOperation='1AEB3940-56CC-41D4-BC29-A12B00DE69D5' and FKProcess in
(select FKProcess from amartins.tblPNNodes where FKController='0F400849-EB81-4703-8A84-A02E010D62FA' and NodeCount=1)

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join credite_egs.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('1C0415C4-B280-45BB-9418-A12B00EA29EA')

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join amartins.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('1C0415C4-B280-45BB-9418-A12B00EA29EA')

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join credite_egs.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('BF8E24E8-BCD2-4670-BEF2-A12B00EADACF');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join amartins.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('BF8E24E8-BCD2-4670-BEF2-A12B00EADACF');

update n set NodeCount=1
from credite_egs.tblPNProcesses p
inner join credite_egs.tblPNNodes n on n.FKProcess=p.FKParent
where p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E'
and p.FKData is not null and p.IsRunning=0
and n.FKController='92B645E1-519C-42C9-A653-A02E010DA61A' and n.NodeCount=0

update n set NodeCount=1
from amartins.tblPNProcesses p
inner join amartins.tblPNNodes n on n.FKProcess=p.FKParent
where p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E'
and p.FKData is not null and p.IsRunning=0
and n.FKController='92B645E1-519C-42C9-A653-A02E010DA61A' and n.NodeCount=0

update n set NodeCount=1
from credite_egs.tblPNProcesses p
inner join credite_egs.tblPNNodes n on n.FKProcess=p.PK
where p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E'
and p.FKData is not null and p.IsRunning=0
and n.FKController='2C3830AC-29B3-4CE7-9345-A036010CBBF9' and n.NodeCount=0

update n set NodeCount=1
from amartins.tblPNProcesses p
inner join amartins.tblPNNodes n on n.FKProcess=p.PK
where p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E'
and p.FKData is not null and p.IsRunning=0
and n.FKController='2C3830AC-29B3-4CE7-9345-A036010CBBF9' and n.NodeCount=0

update s set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join credite_egs.tblPNNodes n on n.FKProcess=p.PK
where s.FKOperation='BB2E2468-98F0-4B16-B8DA-A02E01085B01'
and n.FKController='92B645E1-519C-42C9-A653-A02E010DA61A'
and s.FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
and n.NodeCount>0

update s set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join amartins.tblPNNodes n on n.FKProcess=p.PK
where s.FKOperation='BB2E2468-98F0-4B16-B8DA-A02E01085B01'
and n.FKController='92B645E1-519C-42C9-A653-A02E010DA61A'
and s.FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
and n.NodeCount>0

