insert into bbleiria.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join bbleiria.tblPNProcesses p on p.FKScript=c.FKScript;

insert into bbleiria.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join bbleiria.tblPNProcesses p on p.FKScript=o.FKScript;

update bbleiria.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from bbleiria.tblPNSteps s
inner join bbleiria.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join bbleiria.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);
