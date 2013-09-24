insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('F4CF2D2D-3B52-47F6-80C1-A2430114E5F2', '6C0E956E-DE6F-4531-B967-A243010FEDD4')
and p.FKScript='29145166-59AC-452E-8C2B-9F81013A39AC';

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('315149E0-F4A9-4194-84E7-A24301194762', '51BD2488-7589-493B-A4E4-A24301182876')
and p.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('6C0E956E-DE6F-4531-B967-A243010FEDD4')
and n.FKController='8EE660A6-B682-4A9E-B9E7-A03100CEA4CE'
and n.NodeCount=0;

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('315149E0-F4A9-4194-84E7-A24301194762')
and n.FKController='DD26080F-EF6F-4F0F-8837-A0310101316B'
and n.NodeCount=0;

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('51BD2488-7589-493B-A4E4-A24301182876', 'F4CF2D2D-3B52-47F6-80C1-A2430114E5F2');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('F4CF2D2D-3B52-47F6-80C1-A2430114E5F2', '6C0E956E-DE6F-4531-B967-A243010FEDD4')
and p.FKScript='29145166-59AC-452E-8C2B-9F81013A39AC';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('315149E0-F4A9-4194-84E7-A24301194762', '51BD2488-7589-493B-A4E4-A24301182876')
and p.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC';

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNSteps s
inner join amartins.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('6C0E956E-DE6F-4531-B967-A243010FEDD4')
and n.FKController='8EE660A6-B682-4A9E-B9E7-A03100CEA4CE'
and n.NodeCount=0;

update s set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
from amartins.tblPNSteps s
inner join amartins.tblPNNodes n on n.FKProcess=s.FKProcess
where s.FKOperation in ('315149E0-F4A9-4194-84E7-A24301194762')
and n.FKController='DD26080F-EF6F-4F0F-8837-A0310101316B'
and n.NodeCount=0;

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('51BD2488-7589-493B-A4E4-A24301182876', 'F4CF2D2D-3B52-47F6-80C1-A2430114E5F2');
