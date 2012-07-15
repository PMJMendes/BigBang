/** Perfil simples (AMartins) **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and n.FKController='A1DE70D7-61B1-49F1-BEA0-A01300D67D6D')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))

/** Estornos ou sinistros **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('E7B800AC-5EC8-41A0-AEC9-A01300D751F3', '3DD47B13-A36B-4F31-8717-A01300D782F6'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
where r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')

/** Com imagem ou por conferir **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where (x.docushare is not null or (c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB' and x.estado>'00'))
and n.FKController='FF6DDAAC-9699-477C-AA8E-A01300D659FD')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where (x.docushare is not null or (c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB' and x.estado>'00'))
and n.FKController in ('A06F31E3-4406-47B3-A155-A01300D665E6', 'D14DDDBD-02CC-44E2-B344-A01300D67197'))

/** À cobrança, normais **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'A1DE70D7-61B1-49F1-BEA0-A01300D67D6D', '3DD47B13-A36B-4F31-8717-A01300D782F6'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E')

/** À cobrança, estornos ou sinistros, não simples **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'A1DE70D7-61B1-49F1-BEA0-A01300D67D6D', 'FF02C532-97D9-422F-95FB-A01300D7E746'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile!='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'D7B75D8F-9608-49DE-89B1-A01300D7F102'))

/** À cobrança, estornos ou sinistros, perfil simples (AMartins) **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='FF02C532-97D9-422F-95FB-A01300D7E746')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
inner join amartins..empresa.cliente y on y.cliente=x.cliente
inner join amartins.tblBBClients c on c.MigrationID=y.cliente
where x.estado>'10' and c.FKProfile='51ED12A4-95A9-44B0-928D-A01500DC83EB'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController='D7B75D8F-9608-49DE-89B1-A01300D7F102')

/** Pagos, normais **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'E7B800AC-5EC8-41A0-AEC9-A01300D751F3'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType not in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))

/** Pagos, estornos ou sinistros **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E', 'D7B75D8F-9608-49DE-89B1-A01300D7F102'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'20' and x.estado<'50'
and r.FKReceiptType in ('BFC1AE6D-53E8-41AF-84BE-9F900111D967', '91E07F5F-56BA-4A65-9659-9F900111DF95')
and n.FKController in ('B26DEA4E-157B-4706-8A4D-A01300D72075', '22BEF3BC-4308-4E80-8BAE-A01300D7DB70'))

/** Prestados **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='D869D165-7E86-444A-835A-A01300D75B06')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'30' and x.estado<'50'
and n.FKController='0594F667-06E7-4B4C-9761-A01300D76456')

/** Retrocedidos **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='2FE06BB6-4858-473A-B15B-A01300D76C71')

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>'40' and x.estado<'50'
and n.FKController='A1F9829D-E26E-452B-A20C-A01300D775A0')

/** Devolvidos **/

update amartins.tblPNNodes set NodeCount=0 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('D14DDDBD-02CC-44E2-B344-A01300D67197', 'B3E4E1FB-CE05-4EC7-B93B-A01300D6C73E'))

update amartins.tblPNNodes set NodeCount=1 where PK in
(select n.PK
from amartins.tblPNNodes n
inner join amartins.tblPNProcesses p on p.PK=n.FKProcess
inner join amartins.tblBBReceipts r on r.PK=p.FKData
inner join amartins..empresa.recibo x on x.MigrationID=r.MigrationID
where x.estado>='50'
and n.FKController in ('51031667-19D9-48A1-A4FE-A01300D7A207', 'CEC07468-F1E1-46A5-9D67-A01300D7B67B'))
