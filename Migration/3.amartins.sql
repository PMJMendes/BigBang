insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('FDF0DBAA-22BD-4679-AF72-9EB800CB024D', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '8E5E3504-875A-4313-91A9-9EB500C6295C', '091B8442-B7B0-40FA-B517-9EB00068A390', 0);

insert into amartins.tblProcGeneralSystem (PK, FKProcess) values ('8E5E3504-875A-4313-91A9-9EB500C6295C', 'FDF0DBAA-22BD-4679-AF72-9EB800CB024D');

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(locpostal))) PostalCity
from amartins..empresa.companhi s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into amartins.tblCompanies (PK, CompName, ShortName, FiscalNumber, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.NOME CompName, s.SIGLA ShortName, s.NIFC FiscalNulber, s.MORADA Address1, c.PK FKZipCode, s.COMPANHIA MigrationID
from amartins..empresa.companhi s
left outer join bigbang.tblPostalCodes c on c.PostalCode=s.CODPOSTAL COLLATE DATABASE_DEFAULT
where s.companhia <> 61;

insert into amartins.tblCompanies (PK, CompName, ShortName, FiscalNumber, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.NOME CompName, 'GPCV' ShortName, s.NIFC FiscalNulber, s.MORADA Address1, c.PK FKZipCode, s.COMPANHIA MigrationID
from amartins..empresa.companhi s
left outer join bigbang.tblPostalCodes c on c.PostalCode=s.CODPOSTAL COLLATE DATABASE_DEFAULT
where s.companhia = 61;

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Sede' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, 'CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' FKContactType
from amartins.tblCompanies l
inner join amartins..empresa.companhi r on r.COMPANHIA=l.MigrationID
where r.fax is not null and r.fax<>'';

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.fax InfoValue
from amartins.tblCompanies l
inner join amartins..empresa.companhi r on r.COMPANHIA=l.MigrationID
inner join amartins.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' and ContactName=N'Sede';

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.AOCUIDADO ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, '07367032-3A5D-499D-88BD-9EEE013357C9' FKContactType
from amartins.tblCompanies l
inner join amartins..empresa.companhi r on r.COMPANHIA=l.MigrationID
where r.AOCUIDADO is not null and r.AOCUIDADO<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(locpostal))) PostalCity
from amartins..empresa.contactoscomseg s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Outro' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, 'CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' FKContactType, r.IDContacto MigrationID
from amartins.tblCompanies l
inner join amartins..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is null or r.Nome='';

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, '88AF4A7C-DAB2-4E7F-B85D-9EEE01467E91' FKContactType, r.IDContacto MigrationID
from amartins.tblCompanies l
inner join amartins..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and r.Assunto='Tesouraria';

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, 'BA706479-AE31-4E69-A7F0-9EEE01336CA4' FKContactType, r.IDContacto MigrationID
from amartins.tblCompanies l
inner join amartins..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and r.Assunto like '%recibo%';

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType, r.IDContacto MigrationID
from amartins.tblCompanies l
inner join amartins..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and (r.Assunto is null or r.Assunto='' or r.Assunto='Geral');

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.Telefone InfoValue
from amartins..empresa.ContactosComseg r
inner join amartins.tblContacts l on l.MigrationID=r.IDContacto
where r.Telefone is not null and r.Telefone<>'';

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.Fax InfoValue
from amartins..empresa.ContactosComseg r
inner join amartins.tblContacts l on l.MigrationID=r.IDContacto
where r.Fax is not null and r.Fax<>'';

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, r.Email InfoValue
from amartins..empresa.ContactosComseg r
inner join amartins.tblContacts l on l.MigrationID=r.IDContacto
where r.Email is not null and r.Email<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(localidade))) PostalCity
from amartins..empresa.agente s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into amartins.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, 'F60BB994-3E08-47C2-9CC3-9EFC013D35BE' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from amartins..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.NUMINSTI=0 or a.NUMCONTR=0;

insert into amartins.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, 'C7236BA7-73AD-40ED-B6DC-9EFC013691C8' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from amartins..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.NUMINSTI!=0 and (a.NUMCONTR is null or a.NUMCONTR!=0);

insert into amartins.tblBBGroups (PK, GroupName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
g.nome GroupName, g.grupo MigrationID
from amartins..empresa.grupos g
where g.grupo != '';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select PostalCode, min(PostalCity) PostalCity from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(locpostal))) PostalCity
from credegs..empresa.cliente s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'' and s.codpostal not like '%[^-0123456789]%' and s.locpostal is not null and s.locpostal <>'') z
group by PostalCode) y;

insert into bigbang.tblProfessions (PK, ProfessionName)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct ltrim(rtrim(Profissao)) ProfessionName from amartins..empresa.cliente c
left outer join bigbang.tblProfessions p on p.ProfessionName=ltrim(rtrim(c.Profissao)) COLLATE DATABASE_DEFAULT
where p.PK is null and c.Profissao is not null and ltrim(rtrim(c.Profissao))<>'') z;

insert into bigbang.tblCAE (PK, CAEText)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct CAST(c.CODCAE AS VARCHAR(5)) + ' - ?' CAEText from amartins..empresa.cliente c
left outer join bigbang.tblCAE x on left(x.CAEText, 5) = CAST(c.CODCAE AS VARCHAR(5)) COLLATE DATABASE_DEFAULT
where c.CODCAE like '_____' and x.PK is null) z;

insert into amartins.tblBBClients (PK, ClientName, ClientNumber, Address1, Address2, FKZipCode, FiscalNumber, FKEntityType, FKEntitySubType, FKMediator,
FKProfile, FKGroup, DateOfBirth, FKSex, FKMaritalStatus, FKProfession, FKCAE, ClientNotes, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.NOME ClientName, row_number() over (order by c.CLIENTE) ClientNumber, c.MORADA Address1, c.LOCALIDADE Address2, p.PK FKZipCode, c.NCONTRIB FiscalNumber,
CASE c.TIPO_C WHEN 'I' THEN '462096E4-68A2-408A-963A-9EE600C9556A' WHEN 'E' THEN 'C5B4F500-BB57-4BFD-8248-9EE600C95ABA' ELSE '4098CF7A-B5EE-4C3F-973F-9EE600C961AA' END FKEntityType,
CASE c.TIPO_C WHEN 'C' THEN '5C7A0424-126B-467B-977A-9EE600CC13A4' ELSE NULL END FKEntitySubType,
m.PK FKMediator,
CASE c.ClienteVIP WHEN 1 THEN '63114D11-6087-4EFE-9A7E-9EE600BE52DA' ELSE '9F871430-9BBC-449F-B125-9EE600BE5A9A' END FKProfile,
g.PK FKGroup, c.DataNascimento DateOfBirth, s.PK FKSex, t.PK FKMaritalStatus, f.PK FKProfession, x.PK FKCAE, CAST(c.OBSERV AS VARCHAR(250)) ClientNotes,
c.CLIENTE MigrationID
from amartins..empresa.cliente c
left outer join bigbang.tblPostalCodes p on p.PostalCode=CAST(c.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
left outer join amartins.tblMediators m on m.MigrationID=c.MEDIACLI
left outer join amartins.tblBBGroups g on g.MigrationID=c.grupo COLLATE DATABASE_DEFAULT
left outer join bigbang.tblSex s on left(s.SexName, 1)=c.Sexo COLLATE DATABASE_DEFAULT
left outer join bigbang.tblMaritalStatuses t on upper(left(t.StatusText, 1))=upper(left(c.EstadoCivil, 1)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblProfessions f on f.ProfessionName=ltrim(rtrim(c.Profissao)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblCAE x on left(x.CAEText, 5)=CAST(c.CODCAE AS VARCHAR(5)) COLLATE DATABASE_DEFAULT
where c.NOME<>'' and c.GESTORCLI not in (91, 98, 99);

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Geral' ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and ((r.TELEFONE is not null and r.TELEFONE<>'') or (r.fax is not null and r.fax<>'') or (r.Telemovel is not null and r.Telemovel<>''));

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.TELEFONE InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.TELEFONE is not null and r.TELEFONE<>'';

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.fax InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.fax is not null and r.fax<>'';

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '60414F28-49E7-43AD-ACD9-9EDF00F41E76' FKInfoType, r.Telemovel InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.Telemovel is not null and r.Telemovel<>'';

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.CONTACTO ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and r.CONTACTO is not null and r.CONTACTO<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select PostalCode, min(PostalCity) PostalCity from
(select distinct profcodpostal PostalCode, rtrim(ltrim(upper(proflocpostal))) PostalCity
from amartins..empresa.cliente s left outer join bigbang.tblpostalcodes c on ltrim(s.profcodpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null
and s.profcodpostal is not null and s.profcodpostal <>'' and s.profcodpostal not like '%[^-0123456789]%' and s.proflocpostal is not null and s.proflocpostal <>'') z
group by PostalCode) y;

insert into amartins.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, Address2, FKZipCode, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Profissional' ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner,
r.ProfMorada Address1, r.ProfLocalidade Address2, c.PK FKZipCode, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.NOME<>'' and ((r.ProfMorada is not null and r.ProfMorada<>'') or (r.ProfLocalidade is not null and r.ProfLocalidade<>'') or (r.ProfCodPostal is not null and r.ProfCodPostal<>'') or (r.ProfTelefone is not null and r.ProfTelefone<>''));

insert into amartins.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.ProfTelefone InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Profissional'
and r.ProfTelefone is not null and r.ProfTelefone<>'';

insert into amartins.tblBBDocuments (PK, DocName, FKOwnerType, FKOwner, FKDocType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Carta de Condução' DocName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' FKDocType
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and ((r.CartaCondNum is not null and r.CartaCondNum<>'') or (r.CartaCondData is not null and r.CartaCondData<>''));

insert into amartins.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Número' InfoName, r.CartaCondNum InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblBBDocuments d on d.FKOwner=l.PK
where d.FKDocType='5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' and d.DocName=N'Carta de Condução'
and r.CartaCondNum is not null and r.CartaCondNum<>'';

insert into amartins.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Data' InfoName, r.CartaCondData InfoValue
from amartins.tblBBClients l
inner join amartins..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join amartins.tblBBDocuments d on d.FKOwner=l.PK
where d.FKDocType='5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' and d.DocName=N'Carta de Condução'
and r.CartaCondData is not null and r.CartaCondData<>'';

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'100E701A-EDC5-4D9C-A221-9F09013D7954' FKScript, i.PK FKData, u.FKUser FKManager, 0 IsRunning
from amartins.tblBBClients i
inner join amartins..empresa.cliente c on c.CLIENTE=i.MigrationID
inner join bigbang.tblUser2 u on u.MigrationID=c.GESTORCLI;

update amartins.tblBBClients set FKProcess=p.PK
from amartins.tblBBClients c inner join amartins.tblPNProcesses p on p.FKData=c.PK;

/******  Apolices  ******/

/****  Automovel ****/

/**  400 **/

insert into amartins.tblBBPolicies (PK, PolicyNumber, FKProcess, FKCompany, FKSubLine, BeginDate, FKDuration, FKFractioning, MaturityDay, MaturityMonth, EndDate,
PolicyNotes, FKMediator, BCaseStudy, FKStatus, Premium, DShareFolder, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.apolice PolicyNumber, null FKProcess, c.PK FKCompany, '22FE8580-E680-4EC7-9ABB-9EE9011AA269' FKSubLine, s.datini BeginDate, d.PK FKDuration, f.PK FKFractioning,
s.diainicio MaturityDay, s.mesinicio MaturityMonth, s.datfim EndDate, substring(s.observ, 1, 250) PolicyNotes, m.PK FKMediator, 0 BCaseStudy, case s.situacao
when 'A' then '4F115B5C-0E23-444F-AA68-9F98012CA192' when 'U' then 'FCE79588-054B-458D-9515-9F98012CB80E' else '421E16B3-BE47-4D9C-9011-9F98012C945E' end FKStatus,
round(case isnull(s.moeda, 1) when 2 then s.vpremio else s.vpremio/200.482 end, 2) Premium, s.DocuShare DShareFolder, s.MigrationID MigrationID
from amartins..empresa.apolice s
inner join amartins.tblcompanies c on c.MigrationID=s.comseg
inner join bigbang.tblDurationProfiles d on left(d.Duration, 1) COLLATE DATABASE_DEFAULT = s.duracao COLLATE DATABASE_DEFAULT
inner join bigbang.tblFractioning f on left(f.Fractioning, 1) COLLATE DATABASE_DEFAULT = s.fpagamento COLLATE DATABASE_DEFAULT
left outer join amartins.tblmediators m on m.MigrationID=s.MEDIAPOL
where s.ramo=400 and (s.situacao in ('P', 'N') or (s.situacao in ('A', 'U') and (s.datfim is null or s.datfim>'2009-12-31')))

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'CF9B8F7D-3775-4A7D-8C33-9F9100F1E484' FKCoverage, 1 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'FA2D361C-5304-4EC0-BCD7-9F9100F1EF5E' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital1, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'DE2DC8B4-CBD2-4077-8AB2-9F9100F1FE57' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital2, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '76C425CE-F085-4EB6-8F30-9F9100F205FD' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital3, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '6EEBB0AE-A75E-4E3B-A89F-9F9100F213D8' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital6, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '1FDBAD29-23DE-4326-BAC9-9F9100F21D40' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital5, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'C58F051A-61A9-4F5E-8E80-9F9100F22739' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'FE0B05DD-9962-4C46-9761-9F9100F233BD' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital4, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '97E68C38-1F6F-450D-876F-9F9100F23D1C' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco2, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '56684368-10E3-4E7E-89BB-9F9100F24625' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco8, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '946551DB-FBBA-405D-9C66-9F9100F250E9' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital8, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '9B6E2F87-EF5A-48E0-9878-9F9100F259B0' FKCoverage,
case when (upper(left(ltrim(isnull(s.capital7, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '511D1068-0972-4391-BD47-9F9100F26534' FKCoverage,
case when (upper(left(ltrim(isnull(s.risco9, '')), 3)) in ('', 'NAO', 'NÃO')) then 0 else 1 end BPresent
from amartins.tblBBPolicies p
inner join amartins..empresa.apolice s on s.MigrationID=p.MigrationID
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, '354EC66B-777C-4F6E-8CA4-9F9100F26E6E' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblBBPolicyCoverages (PK, FKPolicy, FKCoverage, BPresent)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, 'E96A9C21-8C66-473C-B4F8-9F9100F27627' FKCoverage, 0 BPresent
from amartins.tblBBPolicies p
where p.FKSubLine='22FE8580-E680-4EC7-9ABB-9EE9011AA269'

insert into amartins.tblPolicyCoInsurers (PK, FKPolicy, FKCompany, [Percent])
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKPolicy, c.PK FKCompany, cs.[percent] [Percent]
from amartins..empresa.cosseguro cs
inner join amartins..empresa.apolice s on s.cliente=cs.cliente and s.apolice=cs.apolice and s.ramo=cs.ramo and s.comseg=cs.comseg
inner join amartins.tblBBPolicies p on p.MigrationID=s.MigrationID
inner join amartins.tblCompanies c on c.MigrationID=cs.cocomseg
