insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o._TSCreate>'2012-11-19' and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o._TSCreate>'2012-11-19' and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c._TSCreate>'2012-11-19' and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c._TSCreate>'2012-11-19' and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join credite_egs.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);
