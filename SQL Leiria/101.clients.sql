insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select replace(left(l.postal, charindex(' ', l.postal)-1), '/', '-') PostalCode,
min(rtrim(ltrim(replace(substring(l.postal, charindex(' ', l.postal), 1000), '-', ' ')))) PostalCity
from SEGEST_SEGCLI l
left join bigbang.tblPostalCodes p on p.PostalCode = replace(left(l.postal, charindex(' ', l.postal)-1), '/', '-') collate DATABASE_DEFAULT
where left(l.postal, 1) in ('1', '2', '3', '4', '5', '6', '7', '8', '9') and charindex(' ', l.postal) in (5, 9) and p.PK is null
group by replace(left(l.postal, charindex(' ', l.postal)-1), '/', '-')) y

insert into bbleiria.tblBBClients (PK, ClientName, ClientNumber, Address1, Address2, FKZipCode, FiscalNumber, FKEntityType, FKEntitySubType, FKMediator,
FKProfile,  NIBNumber, DateOfBirth, FKSex, FKProfession, FKCAE, ActivityNotes, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.Nome ClientName, row_number() over (order by c.Codigo) ClientNumber, c.Morada Address1, c.Localidade Address2, p.PK FKZipCode, c.NIF FiscalNumber,
case codtablivre when 1 then '462096E4-68A2-408A-963A-9EE600C9556A' when 2 then 'C5B4F500-BB57-4BFD-8248-9EE600C95ABA' else '4098CF7A-B5EE-4C3F-973F-9EE600C961AA' end FKEntityType,
case codtablivre when 4 then '5C7A0424-126B-467B-977A-9EE600CC13A4' when 3 then '01003AA3-63A9-43AC-97EC-A219009B5B9F' else null end FKEntitySubType,
m.PK FKMediator, '9F871430-9BBC-449F-B125-9EE600BE5A9A' FKProfile, c.NIB NIBNumber,
case when c.DtNasc<'1900' then null else c.DtNasc end DateOfBirth,
s.PK FKSex, f.PK FKProfession, x.PK FKCAE, c.DesActiv ActivityNotes,
cast(c.Codigo as integer) MigrationID
from SEGEST_SEGCLI c
left outer join bigbang.tblPostalCodes p on p.PostalCode=case when charindex(' ', c.postal)>0 then left(c.postal, charindex(' ', c.postal)-1) else c.postal end collate database_default
cross join bbleiria.tblMediators m
left outer join bigbang.tblSex s on left(s.SexName, 1)=c.Sexo COLLATE DATABASE_DEFAULT
left outer join bigbang.tblProfessions f on f.ProfessionName=c.desactiv collate database_default
left outer join bigbang.tblCAE x on left(x.CAEText, 5)=left(c.CodCAE, 5) collate database_default;

insert into bbleiria.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Geral' ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
where (r.TELEFONE is not null and r.TELEFONE<>'') or (r.fax is not null and r.fax<>'') or (r.Telemovel is not null and r.Telemovel<>'') or (r.Email is not null and r.Email<>'');

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.TELEFONE InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5'
where r.TELEFONE is not null and r.TELEFONE<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.fax InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5'
where r.fax is not null and r.fax<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '60414F28-49E7-43AD-ACD9-9EDF00F41E76' FKInfoType, r.Telemovel InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5'
where r.Telemovel is not null and r.Telemovel<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, left(r.Email, 50) InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5'
where r.Email is not null and r.Email<>'';

insert into bbleiria.tblBBDocuments (PK, DocName, FKOwnerType, FKOwner, FKDocType, RefDate)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'BI' DocName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner,
'5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' FKDocType, GetDate() RefDate
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
where (r.NumBI is not null and r.NumBI<>'') or (r.EmitBI is not null and r.EmitBI<>'') or (r.ValidBI is not null and r.ValidBI<>'') or (r.EmissBI is not null and r.EmissBI<>'');

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Número' InfoName, r.NumBI InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'BI'
where r.NumBI is not null and r.NumBI<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Data Emiss' InfoName, r.EmitBI InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'BI'
where r.EmitBI is not null and r.EmitBI<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Validade' InfoName, r.ValidBI InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'BI'
where r.ValidBI is not null and r.ValidBI<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Local Emiss' InfoName, r.EmissBI InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'BI'
where r.EmissBI is not null and r.EmissBI<>'';

insert into bbleiria.tblBBDocuments (PK, DocName, FKOwnerType, FKOwner, FKDocType, RefDate)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Carta Condução' DocName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner,
'5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' FKDocType, GetDate() RefDate
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
where (r.NumCarta is not null and r.NumCarta<>'') or (r.EmitCarta is not null and r.EmitCarta<>'') or (r.ValidCarta is not null and r.ValidCarta<>'') or (r.EmissCarta is not null and r.EmissCarta<>'');

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Número' InfoName, r.NumCarta InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'Carta Condução'
where r.NumCarta is not null and r.NumCarta<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Data Emiss' InfoName, r.EmitCarta InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'Carta Condução'
where r.EmitCarta is not null and r.EmitCarta<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Validade' InfoName, r.ValidCarta InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'Carta Condução'
where r.ValidCarta is not null and r.ValidCarta<>'';

insert into bbleiria.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Local Emiss' InfoName, r.EmissCarta InfoValue
from bbleiria.tblBBClients l
inner join SEGEST_SEGCLI r on r.Codigo=l.MigrationID
inner join bbleiria.tblBBDocuments d on d.FKOwner=l.PK and d.FKOwnerType='D535A99E-149F-44DC-A28B-9EE600B240F5' and d.DocName=N'Carta Condução'
where r.EmissCarta is not null and r.EmissCarta<>'';

insert into bbleiria.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'100E701A-EDC5-4D9C-A221-9F09013D7954' FKScript, i.PK FKData, '93068539-F6DB-4892-9C7D-A48801228E2E' FKManager, 1 IsRunning
from bbleiria.tblBBClients i;

update bbleiria.tblBBClients set FKProcess=p.PK
from bbleiria.tblBBClients c inner join bbleiria.tblPNProcesses p on p.FKData=c.PK;
