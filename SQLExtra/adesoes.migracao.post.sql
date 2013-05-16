update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='F9EDE7B0-27CD-4271-AED4-9FF301308004')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='40EBB8EF-3077-4104-95F4-9FF301308A2B')

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='53AB1F35-C31C-436D-8234-9FF30130C4A9')

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('DD26080F-EF6F-4F0F-8837-A0310101316B', '0B074EE5-F169-4058-AC0B-A031010143BA'))

update amartins.tblPNSteps set FKLevel=o.FKDefaultLevel
from amartins.tblPNSteps s
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
where p.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC';

update amartins.tblPNSteps set FKLevel='6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5' where PK in
(select s.PK
from amartins.tblPNSteps s
inner join amartins.tblPNProcesses p on p.PK=s.FKProcess
inner join bigbang.tblPNOperations o on o.PK=s.FKOperation
inner join bigbang.tblPNSinks g on g.FKOperation=o.PK
inner join bigbang.tblPNControllers c on c.PK=g.FKController
inner join amartins.tblPNNodes n on n.FKController=c.PK and n.FKProcess=p.PK
where n.NodeCount=0
and p.FKScript='08C796D6-5622-4FF1-B3AB-9FF300F28ABC');
