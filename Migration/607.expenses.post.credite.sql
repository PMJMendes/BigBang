/** Paragem de processos **/

update credite_egs.tblPNProcesses set IsRunning=0
from credite_egs.tblPNProcesses p
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt=4;

/** Estado 2 **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>1
and n.FKController='6BD6E2E3-ED64-490E-8C1C-A03800F215A3');

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>1
and n.FKController='821BE3DE-73B6-4FA4-9550-A03800F22A8A');

/** Estado 3 **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>2
and n.FKController='821BE3DE-73B6-4FA4-9550-A03800F22A8A');

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>2 and e.Settlement>0
and n.FKController='3EDD4C4F-D549-4873-B8A0-A03800F234EC');

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>2 and e.Settlement=0
and n.FKController='6F0D7937-AAF1-47B0-AE2C-A03800F259B0');

/** Estado 4 **/

update credite_egs.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>2
and n.FKController in ('3EDD4C4F-D549-4873-B8A0-A03800F234EC', '6F0D7937-AAF1-47B0-AE2C-A03800F259B0'));

update credite_egs.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from credite_egs.tblPNNodes n
inner join credite_egs.tblPNProcesses p on p.PK=n.FKProcess
inner join credite_egs.tblBBExpenses e on e.PK=p.FKData
inner join credegs..empresa.saudedespesas d on d.IDDesp=e.ENumber
where d.FKSt>2
and n.FKController='621253DB-BA5F-4DB4-82C0-A03800F2696A');
