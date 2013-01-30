insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c.PK in ('0516F865-2E4A-4097-BD17-A15600B7722B', '163AA531-9691-49DE-863F-A15600B7685F')
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('36A6571E-0601-47CE-A53E-A15600BA69D5', '1427C47A-EDB0-4B93-8BDF-A15600BA07A2')
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

update n set NodeCount=1
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNNodes n on n.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and n.FKController='163AA531-9691-49DE-863F-A15600B7685F';

update s set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and s.FKOperation='36A6571E-0601-47CE-A53E-A15600BA69D5';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and s.FKOperation='36A6571E-0601-47CE-A53E-A15600BA69D5';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNLogs l
inner join credite_egs.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and s.FKOperation='9C9369BB-186D-433B-BD7B-A01300CE9125';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c.PK in ('0516F865-2E4A-4097-BD17-A15600B7722B', '163AA531-9691-49DE-863F-A15600B7685F')
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('36A6571E-0601-47CE-A53E-A15600BA69D5', '1427C47A-EDB0-4B93-8BDF-A15600BA07A2')
and p.FKScript='62D0A72A-525E-450C-9917-9F8A00EB38AC';

update n set NodeCount=1
from amartins.tblPNLogs l
inner join amartins.tblPNNodes n on n.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and n.FKController='163AA531-9691-49DE-863F-A15600B7685F';

update s set FKLevel='7925EF60-80FC-4EA2-96A0-9EB1007EA1FF'
from amartins.tblPNLogs l
inner join amartins.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and s.FKOperation='36A6571E-0601-47CE-A53E-A15600BA69D5';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNLogs l
inner join amartins.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='F5F00701-69F7-4622-BB8C-9FB800DED93F'
and s.FKOperation='36A6571E-0601-47CE-A53E-A15600BA69D5';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNLogs l
inner join amartins.tblPNSteps s on s.FKProcess=l.FKProcess
where l.FKOperation='B0D0475F-9628-46A7-9C41-A10E01300E46'
and s.FKOperation='9C9369BB-186D-433B-BD7B-A01300CE9125';
