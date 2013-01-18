insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('E422084B-7F43-4DD2-8335-A1480120992C', '63993739-7EA0-4FE3-8737-A14801201EB2')
and p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('E422084B-7F43-4DD2-8335-A1480120992C', '63993739-7EA0-4FE3-8737-A14801201EB2')
and p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c.PK in ('6E071CAE-F954-431C-9C41-A148012038B9')
and p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c.PK in ('6E071CAE-F954-431C-9C41-A148012038B9')
and p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='63993739-7EA0-4FE3-8737-A14801201EB2';

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='63993739-7EA0-4FE3-8737-A14801201EB2';
