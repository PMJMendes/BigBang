insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('AADFF5E7-4484-4FE8-980D-A14F01077479', '9F186501-A9E0-4B81-8A22-A14F01065125')
and p.FKScript='4B1272F3-28B6-4DEA-AD8F-A11801168139';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('AADFF5E7-4484-4FE8-980D-A14F01077479', '9F186501-A9E0-4B81-8A22-A14F01065125')
and p.FKScript='4B1272F3-28B6-4DEA-AD8F-A11801168139';

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c.PK in ('7877C818-EA49-4344-9CA1-A14F010784AA')
and p.FKScript='4B1272F3-28B6-4DEA-AD8F-A11801168139';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c.PK in ('7877C818-EA49-4344-9CA1-A14F010784AA')
and p.FKScript='4B1272F3-28B6-4DEA-AD8F-A11801168139';

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='9F186501-A9E0-4B81-8A22-A14F01065125';

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='9F186501-A9E0-4B81-8A22-A14F01065125';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNSteps s on s.FKProcess=n.FKProcess
where n.FKController='CF249E87-43B8-47A0-B5E9-A1180130AB8F' and n.NodeCount=0
and s.FKOperation='AADFF5E7-4484-4FE8-980D-A14F01077479';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNNodes n
inner join amartins.tblPNSteps s on s.FKProcess=n.FKProcess
where n.FKController='CF249E87-43B8-47A0-B5E9-A1180130AB8F' and n.NodeCount=0
and s.FKOperation='AADFF5E7-4484-4FE8-980D-A14F01077479';
