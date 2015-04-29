insert into bbleiria.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'37A989E2-9D1F-470C-A59E-9EB1008A97A5' FKScript, NULL FKData, '091B8442-B7B0-40FA-B517-9EB00068A390' FKManager, 1 IsRunning;

insert into bbleiria.tblProcGeneralSystem (PK, FKProcess)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess
from bbleiria.tblPNProcesses p where FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

update bbleiria.tblPNProcesses set FKData=g.PK
from bbleiria.tblPNProcesses p inner join bbleiria.tblProcGeneralSystem g on g.FKProcess=p.PK;

insert into bbleiria.tblCompanies (PK, CompName, ShortName, MediatorCode, FiscalNumber, Address1, Address2, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.Nome CompName, left(s.Nome, 5) ShortName, s.NumAgente MediatorCode, s.NIF FiscalNulber, s.Morada Address1, s.Localidade Address2, cast(s.Codigo as integer) MigrationID
from SEGEST_SEGCOMP s where s.Codigo not in (5, 12, 15, 18, 19, 21, 33) order by s.Nome;

insert into bbleiria.tblCompanies (PK, CompName, ShortName, MediatorCode, FiscalNumber, Address1, Address2, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.Nome + ' 1' CompName, left(s.Nome, 5) + '1' ShortName, s.NumAgente MediatorCode, s.NIF FiscalNulber, s.Morada Address1, s.Localidade Address2, cast(s.Codigo as integer) MigrationID
from SEGEST_SEGCOMP s where s.Codigo in (5, 12, 15, 18, 19, 21, 33);

insert into bbleiria.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Geral' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from bbleiria.tblCompanies l;

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, s.Telefone InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Geral'
where s.Telefone is not null and s.Telefone<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '60414F28-49E7-43AD-ACD9-9EDF00F41E76' FKInfoType, s.Telemovel InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Geral'
where s.Telemovel is not null and s.Telemovel<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, s.Fax InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Geral'
where s.Fax is not null and s.Fax<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, left(s.EmailGeral, 50) InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Geral'
where s.EmailGeral is not null and s.EmailGeral<>'';

insert into bbleiria.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Gestão Clientes' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
where s.EmailGestC is not null and s.EmailGestC<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, left(s.EmailGestC, 50) InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Gestão Clientes'
where s.EmailGestC is not null and s.EmailGestC<>'';

insert into bbleiria.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Gestão Sinistros' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
where s.EmailGestS is not null and s.EmailGestS<>'';

insert into bbleiria.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, left(s.EmailGestS, 50) InfoValue
from bbleiria.tblCompanies l
inner join segest_segcomp s on s.Codigo=l.MigrationID
inner join bbleiria.tblContacts c on c.FKOwner=l.PK and c.FKOwnerType='7B203DCA-FFAC-46B2-B849-9EBC009DB127' and c.ContactName=N'Gestão Sinistros'
where s.EmailGestS is not null and s.EmailGestS<>'';

insert into bbleiria.tblMediators (PK, MediatorName, FKProfile, MigrationID, BHasRetention)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'(Directo)' MediatorName, 'F60BB994-3E08-47C2-9CC3-9EFC013D35BE' FKProfile, 1 MigrationID, 0 BHasRetention;
