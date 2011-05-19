delete from credite_egs.tblDocInfo;
delete from credite_egs.tblBBDocuments;
delete from credite_egs.tblContactInfo;
delete from credite_egs.tblContacts;
delete from credite_egs.tblBBClients;
delete from credite_egs.tblBBGroups;
delete from credite_egs.tblBBTaxes;
delete from credite_egs.tblBBCoverages;
delete from credite_egs.tblCompanies;
delete from credite_egs.tblMediators;
delete from credite_egs.tblProcGeneralSystem;

delete from credite_egs.tblPNLogs;
delete from credite_egs.tblPNNodes;
delete from credite_egs.tblPNSteps;
delete from credite_egs.tblPNProcesses;

delete from amartins.tblDocInfo;
delete from amartins.tblBBDocuments;
delete from amartins.tblContactInfo;
delete from amartins.tblContacts;
delete from amartins.tblBBClients;
delete from amartins.tblBBGroups;
delete from amartins.tblBBTaxes;
delete from amartins.tblBBCoverages;
delete from amartins.tblCompanies;
delete from amartins.tblMediators;
delete from amartins.tblProcGeneralSystem;

delete from amartins.tblPNLogs;
delete from amartins.tblPNNodes;
delete from amartins.tblPNSteps;
delete from amartins.tblPNProcesses;

delete from bigbang.tblBBSubLines;
delete from bigbang.tblBBLines;
delete from bigbang.tblLineCategories;

delete from bigbang.tblUser2;
delete from bigbang.tblBBCostCenters;
delete from bigbang.tblUsers;
delete from bigbang.tblWorkspaces;
delete from bigbang.tblProfiles;

delete from bigbang.tblBBClientSubTypes;
delete from bigbang.tblBBClientTypes;
delete from bigbang.tblBBCompanySizes;
delete from bigbang.tblBBSalesVolumes;
delete from bigbang.tblCommissionProfiles;
delete from bigbang.tblContactInfoType;
delete from bigbang.tblDocTypes;
delete from bigbang.tblMaritalStatuses;
delete from bigbang.tblOpProfiles;
delete from bigbang.tblProfessions;
delete from bigbang.tblSex;
delete from bigbang.tblUnits;

delete from bigbang.tblCAE;
delete from bigbang.tblPostalCodes;

insert into bigbang.tblBBClientTypes (PK, EntityType) values ('462096E4-68A2-408A-963A-9EE600C9556A', 'Individual');
insert into bigbang.tblBBClientTypes (PK, EntityType) values ('C5B4F500-BB57-4BFD-8248-9EE600C95ABA', 'Empresa');
insert into bigbang.tblBBClientTypes (PK, EntityType) values ('4098CF7A-B5EE-4C3F-973F-9EE600C961AA', 'Outros');
insert into bigbang.tblBBClientSubTypes (PK, EntitySubtype) values ('5C7A0424-126B-467B-977A-9EE600CC13A4', 'Condomínio');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('96467849-6FE1-4113-928C-9EDF00F40FB9', 'Email');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('01C8D0CA-074E-45AA-8A17-9EDF00F41586', 'Telefone');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('60414F28-49E7-43AD-ACD9-9EDF00F41E76', 'Telemóvel');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('172EC088-AA55-433B-BBC3-9EDF00F42266', 'Fax');
insert into bigbang.tblOpProfiles (PK, OpProfileName) values ('63114D11-6087-4EFE-9A7E-9EE600BE52DA', 'VIP');
insert into bigbang.tblOpProfiles (PK, OpProfileName) values ('9F871430-9BBC-449F-B125-9EE600BE5A9A', 'Normal');

insert into bigbang.tblProfiles (PK, PfName) values ('061388D9-16A6-443F-A69E-9EB000685026', 'Root');
insert into bigbang.tblProfiles (PK, PfName) values ('258A1C88-C916-40CB-8CD5-9EB8007F2AEB', 'Sem Perfil');
insert into bigbang.tblWorkspaces (PK, FKProfile, FKNameSpace) values ('28A44306-131C-4A18-BCF2-9EB000685EEA', '061388D9-16A6-443F-A69E-9EB000685026', 'C37B81F0-860F-4868-9177-9E15008B3EFD');
insert into bigbang.tblUsers (PK, FullName, Username, FKProfile) values ('091B8442-B7B0-40FA-B517-9EB00068A390', 'Administrator', 'root', '061388D9-16A6-443F-A69E-9EB000685026');
insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID) values ('FC6BB26D-90E2-46C9-9C79-9ED900C124AE', 'INF', 'Informática', 13);
insert into bigbang.tblUser2 (PK, FKUser, FKCostCenter, MigrationID) values ('ABD75E9E-FB45-4E47-A261-9ED900C2ABBD', '091B8442-B7B0-40FA-B517-9EB00068A390', 'FC6BB26D-90E2-46C9-9C79-9ED900C124AE', 91);

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('49153B77-1391-4E3A-81D2-9EB800CB68B7', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '1822E9C1-700F-49A5-AB6F-9EB500C632D2', '091B8442-B7B0-40FA-B517-9EB00068A390', 1);
insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel) select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, '49153B77-1391-4E3A-81D2-9EB800CB68B7' FKProcess, PK FKOperation, FKDefaultLevel FKLevel from bigbang.tblPNOperations;

insert into credite_egs.tblProcGeneralSystem (PK, FKProcess) values ('1822E9C1-700F-49A5-AB6F-9EB500C632D2', '49153B77-1391-4E3A-81D2-9EB800CB68B7');

insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('FDF0DBAA-22BD-4679-AF72-9EB800CB024D', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '8E5E3504-875A-4313-91A9-9EB500C6295C', '091B8442-B7B0-40FA-B517-9EB00068A390', 1);
insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel) select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, 'FDF0DBAA-22BD-4679-AF72-9EB800CB024D' FKProcess, PK FKOperation, FKDefaultLevel FKLevel from bigbang.tblPNOperations;

insert into amartins.tblProcGeneralSystem (PK, FKProcess) values ('8E5E3504-875A-4313-91A9-9EB500C6295C', 'FDF0DBAA-22BD-4679-AF72-9EB800CB024D');

insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
UPPER(LEFT(NomeEquipa, 3)) CCCode, NomeEquipa CCName, IDEquipa MigrationID
from credegs..empresa.equipa
where IDEquipa not in (9, 13);
insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'GNC' CCCode, NomeEquipa CCName, IDEquipa MigrationID
from credegs..empresa.equipa
where IDEquipa=9;

insert into bigbang.tblUsers (PK, FullName, Username, FKProfile)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
Nome FullName, username Username, '258A1C88-C916-40CB-8CD5-9EB8007F2AEB' FKProfile
from credegs..empresa.gestor where gestor not in (91, 98, 99);

insert into bigbang.tblUser2 (PK, FKUser, UserEmail, FKCostCenter, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
u.PK FKUser, g.email UserEmail, c.PK FKCostCenter, g.gestor MigrationID
from credegs..empresa.gestor g
inner join bigbang.tblUsers u on u.Username=g.username COLLATE DATABASE_DEFAULT
inner join bigbang.tblBBCostCenters c on c.MigrationID=g.FKEquipa
where gestor not in (91, 98, 99);

