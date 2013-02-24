update q set FKClient=p2.FKData
from credite_egs.tblBBQuoteRequests q
inner join credite_egs.tblPNProcesses p1 on p1.PK=q.FKProcess
inner join credite_egs.tblPNProcesses p2 on p2.PK=p1.FKParent

update q set FKClient=p2.FKData
from amartins.tblBBQuoteRequests q
inner join amartins.tblPNProcesses p1 on p1.PK=q.FKProcess
inner join amartins.tblPNProcesses p2 on p2.PK=p1.FKParent
