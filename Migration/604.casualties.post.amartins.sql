/** Paragem de processos **/

update amartins.tblPNProcesses set IsRunning=0
from amartins.tblPNProcesses p
inner join amartins.tblBBCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem=c.CNumber
where x.estado='F' and x.datafecho is not null;

/** Fechados **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem=c.CNumber
where x.estado='F' and x.datafecho is not null
and n.FKController in ('D85A35CE-A745-4E8F-A53C-A02E010D4D8F', '6D769D8A-613D-4826-91A6-A03C00C56759'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem=c.CNumber
where x.estado='F' and x.datafecho is not null
and n.FKController='0F400849-EB81-4703-8A84-A02E010D62FA')

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.CNumber
where x.estado='F' and x.datafecho is not null
and n.FKController='785FE2C4-A0F3-43BD-BD92-A036010C8872')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.CNumber
where x.estado='F' and x.datafecho is not null
and n.FKController='2C3830AC-29B3-4CE7-9345-A036010CBBF9')

/** Participacao enviada **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.CNumber
where x.dataparticipacao is not null
and n.FKController='D518C62B-FB36-4D45-9257-A036010C5190')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBSubCasualties c on c.PK=p.FKData
inner join amartins..empresa.sinistros x on x.ordem + 0.1 = c.CNumber
where x.dataparticipacao is not null
and n.FKController='5FEBB5D4-0243-4AA5-B244-A036010C5E51')
