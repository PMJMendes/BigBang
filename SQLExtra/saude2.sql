insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join credite_egs.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('C581CE0F-42EC-4EFB-AF69-A12A01196B69', '423E416A-E89B-4BD8-9179-A12A01196373')

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from bigbang.tblPNControllers c
inner join amartins.tblPNProcesses p on p.FKScript=c.FKScript
where c.PK in ('C581CE0F-42EC-4EFB-AF69-A12A01196B69', '423E416A-E89B-4BD8-9179-A12A01196373')

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join credite_egs.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('E870B538-B7AE-4B33-B13E-A12A011BD386');

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel, FKUser, FKSrouceLog)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, '6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' FKLevel, null FKUser, null FKSrouceLog
from bigbang.tblPNOperations o
inner join amartins.tblPNProcesses p on p.FKScript=o.FKScript
where o.PK in ('E870B538-B7AE-4B33-B13E-A12A011BD386');

update credite_egs.tblPNNodes set NodeCount=0
where FKController in ('E7B800AC-5EC8-41A0-AEC9-A01300D751F3', '423E416A-E89B-4BD8-9179-A12A01196373')
and FKProcess in
(select FKProcess from credite_egs.tblBBReceipts where ReceiptNotes like 'custo%2013%')

update amartins.tblPNNodes set NodeCount=0
where FKController in ('E7B800AC-5EC8-41A0-AEC9-A01300D751F3', '423E416A-E89B-4BD8-9179-A12A01196373')
and FKProcess in
(select FKProcess from amartins.tblBBReceipts where ReceiptNotes like 'custo%2013%')

update credite_egs.tblPNNodes set NodeCount=1
where FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
and FKProcess in
(select FKProcess from credite_egs.tblBBReceipts where ReceiptNotes like 'custo%2013%')

update amartins.tblPNNodes set NodeCount=1
where FKController='C581CE0F-42EC-4EFB-AF69-A12A01196B69'
and FKProcess in
(select FKProcess from amartins.tblBBReceipts where ReceiptNotes like 'custo%2013%')
