insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join credite_egs.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('CF04BC5B-1CD6-49E0-9F3D-A13300EF0B7C')

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join amartins.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('CF04BC5B-1CD6-49E0-9F3D-A13300EF0B7C')

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '7925EF60-80FC-4EA2-96A0-9EB1007EA1FF' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join credite_egs.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('E7305369-657B-4CC3-B769-A13300EEF0CD');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '7925EF60-80FC-4EA2-96A0-9EB1007EA1FF' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join amartins.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('E7305369-657B-4CC3-B769-A13300EEF0CD');

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join credite_egs.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('A58119C9-7B91-4B64-BF1F-A13300EF84B1');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join amartins.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('A58119C9-7B91-4B64-BF1F-A13300EF84B1');
