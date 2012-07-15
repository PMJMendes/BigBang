/** Não provisórias **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='85D25D21-7FF1-4347-97E3-9FD4010D3B9B')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus!='6489D7DF-A090-40B9-BD5E-9F98012C8BED'
and n.FKController='690D5B0C-17B7-4109-A79D-9FD4010D4BB0')

/** Anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='4F115B5C-0E23-444F-AA68-9F98012CA192'
and n.FKController='BA9C2B80-142B-49BE-845F-9FD4010E0E50')

/** Auto-anuladas **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('690D5B0C-17B7-4109-A79D-9FD4010D4BB0', 'E17CEFA7-9B90-4E8B-B8AF-A03100F5BD88'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBPolicies a on a.PK=p.FKData
where a.FKStatus='FCE79588-054B-458D-9515-9F98012CB80E'
and n.FKController in ('8EE660A6-B682-4A9E-B9E7-A03100CEA4CE', '74C8A735-8425-4637-8D61-A03100CEB4B3'))
