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
