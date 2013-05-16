update n set NodeCount=1-n.NodeCount
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNProcesses p on p.PK=r.FKProcess
inner join credite_egs.tblPNProcesses p2 on p2.PK=p.FKParent
inner join credite_egs.tblPNNodes n on n.FKProcess=p.PK
and n.FKController='423E416A-E89B-4BD8-9179-A12A01196373'
left outer join credite_egs.tblPNLogs l on l.FKProcess=p.PK
and l.FKOperation in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013') and l.Undone=0
where (l.PK is null and n.NodeCount=0) or (l.PK is not null and n.NodeCount>0);

update n set NodeCount=0
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNNodes n on n.FKProcess=r.FKProcess
where r.BIsInternal=1
and n.FKController='423E416A-E89B-4BD8-9179-A12A01196373';

update credite_egs.tblPNSteps set FKLevel=o.FKDefaultLevel
from credite_egs.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
where o.PK in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013',
'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57');

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013',
'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57')
and PK in
(select s.PK
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join credite_egs.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);

update n set NodeCount=1-n.NodeCount
from amartins.tblBBReceipts r
inner join amartins.tblPNProcesses p on p.PK=r.FKProcess
inner join amartins.tblPNProcesses p2 on p2.PK=p.FKParent
inner join amartins.tblPNNodes n on n.FKProcess=p.PK
and n.FKController='423E416A-E89B-4BD8-9179-A12A01196373'
left outer join amartins.tblPNLogs l on l.FKProcess=p.PK
and l.FKOperation in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013') and l.Undone=0
where (l.PK is null and n.NodeCount=0) or (l.PK is not null and n.NodeCount>0);

update n set NodeCount=0
from amartins.tblBBReceipts r
inner join amartins.tblPNNodes n on n.FKProcess=r.FKProcess
where r.BIsInternal=1
and n.FKController='423E416A-E89B-4BD8-9179-A12A01196373';

update amartins.tblPNSteps set FKLevel=o.FKDefaultLevel
from amartins.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
where o.PK in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013',
'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57');

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5'
where FKOperation in ('B0D0475F-9628-46A7-9C41-A10E01300E46', 'A34C65D4-0B2A-4083-BB16-A01300D3D013',
'A07D96EF-CF7E-4287-8C3B-A01300CD2AF5', 'C7305673-3EC9-4B1A-A437-A01300CD7D57')
and PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);
