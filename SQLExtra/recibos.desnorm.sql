update r set FKPolicy=p.PK
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNProcesses pr on pr.PK=r.FKProcess
inner join credite_egs.tblPNProcesses pp on pp.PK=pr.FKParent
inner join credite_egs.tblBBPolicies p on p.PK=pp.FKData;

update r set FKSubPolicy=sp.PK
from credite_egs.tblBBReceipts r
inner join credite_egs.tblPNProcesses pr on pr.PK=r.FKProcess
inner join credite_egs.tblPNProcesses psp on psp.PK=pr.FKParent
inner join credite_egs.tblBBSubPolicies sp on sp.PK=psp.FKData;

update r set FKPolicy=p.PK
from amartins.tblBBReceipts r
inner join amartins.tblPNProcesses pr on pr.PK=r.FKProcess
inner join amartins.tblPNProcesses pp on pp.PK=pr.FKParent
inner join amartins.tblBBPolicies p on p.PK=pp.FKData;

update r set FKSubPolicy=sp.PK
from amartins.tblBBReceipts r
inner join amartins.tblPNProcesses pr on pr.PK=r.FKProcess
inner join amartins.tblPNProcesses psp on psp.PK=pr.FKParent
inner join amartins.tblBBSubPolicies sp on sp.PK=psp.FKData;
