update credite_egs.tblPNSteps set FKLevel=o.FKDefaultLevel
from credite_egs.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation;

update credite_egs.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from credite_egs.tblPNSteps s
inner join credite_egs.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join credite_egs.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);



update amartins.tblPNSteps set FKLevel=o.FKDefaultLevel
from amartins.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation;

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0);
