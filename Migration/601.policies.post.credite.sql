/** Não provisórias **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='85D25D21-7FF1-4347-97E3-9FD4010D3B9B')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='690D5B0C-17B7-4109-A79D-9FD4010D4BB0')

/** Anuladas **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='BA9C2B80-142B-49BE-845F-9FD4010E0E50')

/** Auto-anuladas **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('8EE660A6-B682-4A9E-B9E7-A03100CEA4CE', '74C8A735-8425-4637-8D61-A03100CEB4B3'))

/** Subs não provisórias **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='F9EDE7B0-27CD-4271-AED4-9FF301308004')

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='40EBB8EF-3077-4104-95F4-9FF301308A2B')

/** Subs anuladas **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='53AB1F35-C31C-436D-8234-9FF30130C4A9')

/** Subs auto-anuladas **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('40EBB8EF-3077-4104-95F4-9FF301308A2B', 'D7FA6B3A-CE6D-45AA-90E6-A03101016091'))

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBSubPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('DD26080F-EF6F-4F0F-8837-A0310101316B', '0B074EE5-F169-4058-AC0B-A031010143BA'))
