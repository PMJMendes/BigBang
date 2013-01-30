insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('8C3D3E86-C1AB-4D40-873D-A15600DA7F73')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNSteps s on s.FKProcess=n.FKProcess
where n.FKController='EC6BA1E1-9831-4B9E-9807-A036010C956F' and n.NodeCount=0
and s.FKOperation='8C3D3E86-C1AB-4D40-873D-A15600DA7F73';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('8C3D3E86-C1AB-4D40-873D-A15600DA7F73')
and p.FKScript='80B7A9BC-8710-4063-A99E-A02E01220F4E';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNNodes n
inner join amartins.tblPNSteps s on s.FKProcess=n.FKProcess
where n.FKController='EC6BA1E1-9831-4B9E-9807-A036010C956F' and n.NodeCount=0
and s.FKOperation='8C3D3E86-C1AB-4D40-873D-A15600DA7F73';
