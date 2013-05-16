update s set FKPolicy=p.PK
from credite_egs.tblBBSubPolicies s
inner join credite_egs.tblPNProcesses ps on ps.PK=s.FKProcess
inner join credite_egs.tblPNProcesses pp on pp.PK=ps.FKParent
inner join credite_egs.tblBBPolicies p on p.PK=pp.FKData;

update s set FKPolicy=p.PK
from amartins.tblBBSubPolicies s
inner join amartins.tblPNProcesses ps on ps.PK=s.FKProcess
inner join amartins.tblPNProcesses pp on pp.PK=ps.FKParent
inner join amartins.tblBBPolicies p on p.PK=pp.FKData;
