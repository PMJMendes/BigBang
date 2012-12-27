update r set BIsInternal=1
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNProcesses p on p.PK=r.FKProcess
inner join credite_egs.tblPNProcesses p2 on p2.PK=p.FKParent
inner join credite_egs.tblPNProcesses p3 on p3.PK=p2.FKParent
inner join credite_egs.tblBBPolicies a on a.PK=p3.FKData
inner join bigbang.tblBBSubLines sl on sl.PK=a.FKSubLine
inner join bigbang.tblBBLines l on l.PK=sl.FKLine
where p2.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC'
and l.FKCategory='F04EBBD0-45BD-4921-931F-9EE9010A6B0E';

update r set BIsInternal=1
from amartins.tblBBReceipts r
inner join amartins.tblPNProcesses p on p.PK=r.FKProcess
inner join amartins.tblPNProcesses p2 on p2.PK=p.FKParent
inner join amartins.tblPNProcesses p3 on p3.PK=p2.FKParent
inner join amartins.tblBBPolicies a on a.PK=p3.FKData
inner join bigbang.tblBBSubLines sl on sl.PK=a.FKSubLine
inner join bigbang.tblBBLines l on l.PK=sl.FKLine
where p2.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC'
and l.FKCategory='F04EBBD0-45BD-4921-931F-9EE9010A6B0E';
