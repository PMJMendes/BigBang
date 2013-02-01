insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, credite_egs.tblPNProcesses p
where o.PK in ('CA57DD58-CC1B-4359-81F5-A14900DFCB81', '39EB5130-F961-4EC8-8441-A14900DF4A3F')
and p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o, amartins.tblPNProcesses p
where o.PK in ('CA57DD58-CC1B-4359-81F5-A14900DFCB81', '39EB5130-F961-4EC8-8441-A14900DF4A3F')
and p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, credite_egs.tblPNProcesses p
where c.PK in ('190DE6E0-B79B-479F-AC24-A14900DF7ECE', '839E5E1E-14B8-4444-B0F0-A14900DF629B')
and p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c, amartins.tblPNProcesses p
where c.PK in ('190DE6E0-B79B-479F-AC24-A14900DF7ECE', '839E5E1E-14B8-4444-B0F0-A14900DF629B')
and p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='39EB5130-F961-4EC8-8441-A14900DF4A3F';

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation='39EB5130-F961-4EC8-8441-A14900DF4A3F';
