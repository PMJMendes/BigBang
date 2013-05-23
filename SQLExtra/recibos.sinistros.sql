insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c.PK in ('92D8C026-5925-4E69-A28B-A1C50105CC23')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('0B9E41A6-5466-4D59-AD3C-A1C501067A1E', '2881008F-E08B-400F-A384-A1C501047FB5', '204BDF28-EB94-4A6C-92A2-A1C50101E80C')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('0B9E41A6-5466-4D59-AD3C-A1C501067A1E', '2881008F-E08B-400F-A384-A1C501047FB5', '204BDF28-EB94-4A6C-92A2-A1C50101E80C')
and n.FKController='EC6BA1E1-9831-4B9E-9807-A036010C956F'
and n.NodeCount=0;

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='2881008F-E08B-400F-A384-A1C501047FB5';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c.PK in ('92D8C026-5925-4E69-A28B-A1C50105CC23')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('0B9E41A6-5466-4D59-AD3C-A1C501067A1E', '2881008F-E08B-400F-A384-A1C501047FB5', '204BDF28-EB94-4A6C-92A2-A1C50101E80C')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNSteps s
inner join amartins.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('0B9E41A6-5466-4D59-AD3C-A1C501067A1E', '2881008F-E08B-400F-A384-A1C501047FB5', '204BDF28-EB94-4A6C-92A2-A1C50101E80C')
and n.FKController='EC6BA1E1-9831-4B9E-9807-A036010C956F'
and n.NodeCount=0;

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='2881008F-E08B-400F-A384-A1C501047FB5';
