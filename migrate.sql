delete from credite_egs.tblDocInfo;
delete from credite_egs.tblBBDocuments;
delete from credite_egs.tblContactInfo;
delete from credite_egs.tblContacts;

delete from credite_egs.tblMgrXFerProcs;
delete from credite_egs.tblMgrXFers;

delete from credite_egs.tblRequestAddresses;
delete from credite_egs.tblInfoRequests;

delete from credite_egs.tblBBReceipts;
delete from credite_egs.tblBBPolicyValues;
delete from credite_egs.tblExercises;
delete from credite_egs.tblInsuredObjects;
delete from credite_egs.tblBBPolicyCoverages;
delete from credite_egs.tblBBPolicies;
delete from credite_egs.tblBBClients;
delete from credite_egs.tblBBGroups;
delete from credite_egs.tblMediators;
delete from credite_egs.tblCompanies;
delete from credite_egs.tblProcGeneralSystem;

delete from credite_egs.tblAgendaOperations;
delete from credite_egs.tblAgendaProcesses;
delete from credite_egs.tblAgendaItems;

delete from credite_egs.tblPolicyValueItems;

delete from credite_egs.tblPNLogs;
delete from credite_egs.tblPNNodes;
delete from credite_egs.tblPNSteps;
delete from credite_egs.tblPNProcesses;

delete from amartins.tblDocInfo;
delete from amartins.tblBBDocuments;
delete from amartins.tblContactInfo;
delete from amartins.tblContacts;

delete from amartins.tblMgrXFerProcs;
delete from amartins.tblMgrXFers;

delete from amartins.tblRequestAddresses;
delete from amartins.tblInfoRequests;

delete from amartins.tblBBReceipts;
delete from amartins.tblBBPolicyValues;
delete from amartins.tblExercises;
delete from amartins.tblInsuredObjects;
delete from amartins.tblBBPolicyCoverages;
delete from amartins.tblBBPolicies;
delete from amartins.tblBBClients;
delete from amartins.tblBBGroups;
delete from amartins.tblMediators;
delete from amartins.tblCompanies;
delete from amartins.tblProcGeneralSystem;

delete from amartins.tblAgendaOperations;
delete from amartins.tblAgendaProcesses;
delete from amartins.tblAgendaItems;

delete from amartins.tblPolicyValueItems;

delete from amartins.tblPNLogs;
delete from amartins.tblPNNodes;
delete from amartins.tblPNSteps;
delete from amartins.tblPNProcesses;

delete from bigbang.tblBBTaxes;
delete from bigbang.tblBBCoverages;
delete from bigbang.tblBBSubLines;
delete from bigbang.tblBBLines;
delete from bigbang.tblLineCategories;

delete from bigbang.tblUser2;
delete from bigbang.tblBBCostCenters;
delete from bigbang.tblUsers;
delete from bigbang.tblWorkspaces;
/*delete from bigbang.tblProfiles;*/

delete from bigbang.tblBBClientSubTypes;
delete from bigbang.tblBBClientTypes;
delete from bigbang.tblBBCompanySizes;
delete from bigbang.tblBBSalesVolumes;
delete from bigbang.tblCommissionProfiles;
delete from bigbang.tblContactInfoType;
delete from bigbang.tblContactTypes;
delete from bigbang.tblDocTypes;
delete from bigbang.tblMaritalStatuses;
delete from bigbang.tblOpProfiles;
delete from bigbang.tblProfessions;
delete from bigbang.tblSex;

delete from bigbang.tblCAE;
delete from bigbang.tblPostalCodes;

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity, PostalCounty, PostalDistrict, PostalCountry)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct CodPostal PostalCode, LocPostal PostalCity, Concelho PostalCounty, Distrito PostalDistrict, Pais PostalCountry
from codigospostaisSQL.dbo.CodPostais) z;

insert into bigbang.tblCAE (PK, CAEText)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
Codigo + ' - ' + Texto CAEText
from caeSQL.dbo.CAEs where Nivel=5;

insert into bigbang.tblBBClientTypes (PK, EntityType) values ('462096E4-68A2-408A-963A-9EE600C9556A', N'Individual');
insert into bigbang.tblBBClientTypes (PK, EntityType) values ('C5B4F500-BB57-4BFD-8248-9EE600C95ABA', N'Empresa');
insert into bigbang.tblBBClientTypes (PK, EntityType) values ('4098CF7A-B5EE-4C3F-973F-9EE600C961AA', N'Outros');
insert into bigbang.tblBBClientSubTypes (PK, EntitySubtype) values ('5C7A0424-126B-467B-977A-9EE600CC13A4', N'Condomínio');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('96467849-6FE1-4113-928C-9EDF00F40FB9', N'Email');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('01C8D0CA-074E-45AA-8A17-9EDF00F41586', N'Telefone');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('60414F28-49E7-43AD-ACD9-9EDF00F41E76', N'Telemóvel');
insert into bigbang.tblContactInfoType (PK, TypeName) values ('172EC088-AA55-433B-BBC3-9EDF00F42266', N'Fax');
insert into bigbang.tblContactTypes (PK, ContactType) values ('07367032-3A5D-499D-88BD-9EEE013357C9', N'Ao Cuidado');
insert into bigbang.tblContactTypes (PK, ContactType) values ('BA706479-AE31-4E69-A7F0-9EEE01336CA4', N'Devolução de Recibos');
insert into bigbang.tblContactTypes (PK, ContactType) values ('04F6BC3C-0283-47F0-9670-9EEE013350D9', N'Geral');
insert into bigbang.tblContactTypes (PK, ContactType) values ('CF3019C6-8A9C-495C-B9D0-9EEE01335BC6', N'Local');
insert into bigbang.tblContactTypes (PK, ContactType) values ('88AF4A7C-DAB2-4E7F-B85D-9EEE01467E91', N'Tesouraria');
insert into bigbang.tblCommissionProfiles (PK, ProfileName) values ('F60BB994-3E08-47C2-9CC3-9EFC013D35BE', N'Sem Comissões');
insert into bigbang.tblCommissionProfiles (PK, ProfileName) values ('CECC8014-200C-4C4F-9F47-9EFC01368139', N'Percentagem');
insert into bigbang.tblCommissionProfiles (PK, ProfileName) values ('C5BE51A9-7E0F-4970-962A-9EFC0135E9E1', N'Angariação');
insert into bigbang.tblCommissionProfiles (PK, ProfileName) values ('071CE678-956B-4D41-94DE-9EFC013688B5', N'Especial');
insert into bigbang.tblCommissionProfiles (PK, ProfileName) values ('C7236BA7-73AD-40ED-B6DC-9EFC013691C8', N'Negociado');
insert into bigbang.tblDocTypes (PK, DocType) values ('5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A', N'Carta de Condução');
insert into bigbang.tblMaritalStatuses (PK, StatusText) values ('9ED463DB-ABC5-46EE-82A7-9F0300C6D631', N'Casado');
insert into bigbang.tblMaritalStatuses (PK, StatusText) values ('BFB58864-4B91-4078-AF9F-9F0300C6E52D', N'Divorciado');
insert into bigbang.tblMaritalStatuses (PK, StatusText) values ('BE742798-9D7F-4B23-BFBA-9F0300C6CFA5', N'Solteiro');
insert into bigbang.tblMaritalStatuses (PK, StatusText) values ('DB379F5C-9343-403B-B28A-9F0300C6DDFB', N'Viúvo');
insert into bigbang.tblOpProfiles (PK, OpProfileName) values ('63114D11-6087-4EFE-9A7E-9EE600BE52DA', N'VIP');
insert into bigbang.tblOpProfiles (PK, OpProfileName) values ('9F871430-9BBC-449F-B125-9EE600BE5A9A', N'Normal');
insert into bigbang.tblSex (PK, SexName) values ('77E22CFB-CA90-4918-B9B2-9F0300C39AE2', N'Feminino');
insert into bigbang.tblSex (PK, SexName) values ('E86BA460-B499-4254-84F5-9F0300C3A256', N'Masculino');

/*insert into bigbang.tblProfiles (PK, PfName) values ('061388D9-16A6-443F-A69E-9EB000685026', N'Root');*/
/*insert into bigbang.tblProfiles (PK, PfName) values ('258A1C88-C916-40CB-8CD5-9EB8007F2AEB', N'Sem Perfil');*/
insert into bigbang.tblWorkspaces (PK, FKProfile, FKNameSpace) values ('28A44306-131C-4A18-BCF2-9EB000685EEA', '061388D9-16A6-443F-A69E-9EB000685026', 'C37B81F0-860F-4868-9177-9E15008B3EFD');
insert into bigbang.tblUsers (PK, FullName, Username, Passwd, FKProfile) values ('091B8442-B7B0-40FA-B517-9EB00068A390', N'Administrator', N'root', 'C5-06-09-A7-D7-96-23-72-F0-C6-F4-F3-10-DD-88-CA', '061388D9-16A6-443F-A69E-9EB000685026');
insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID) values ('FC6BB26D-90E2-46C9-9C79-9ED900C124AE', N'INF', N'Informática', 13);
insert into bigbang.tblUser2 (PK, FKUser, FKCostCenter, MigrationID) values ('ABD75E9E-FB45-4E47-A261-9ED900C2ABBD', '091B8442-B7B0-40FA-B517-9EB00068A390', 'FC6BB26D-90E2-46C9-9C79-9ED900C124AE', 91);

insert into bigbang.tblLineCategories (PK, LineCatName) values ('53DB03E7-F423-4656-A23A-9EE9010A5B87', N'Acidentes de Trabalho');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('BB3646B1-BE20-4ED1-B3C2-9EE9010A641D', N'Acidentes Pessoais');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2', N'Aeronaves');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('A3F44708-4666-425C-AA70-9EE9010A9E0D', N'Agro Pecuário');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('7F5F77EB-8348-4914-8525-9EE9010AB1C6', N'Automóvel');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('73410713-03BD-4247-84A7-9EE9010AECB7', N'Diversos');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('F04EBBD0-45BD-4921-931F-9EE9010A6B0E', N'Doença');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('00960D0F-F31F-4A9E-AE7E-9EE9010AD157', N'Embarcações Marítimas');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('696A51D8-B21A-4DA8-B8E9-9EE901153338', N'Fundo de Pensões');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('56A359A3-33DF-40E5-B004-9EE9010A726A', N'Incêndio');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('E2732D85-E204-4A71-A5D4-9EE9010AC7B1', N'Mercadorias Transportadas');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('ECFD23D7-2D15-43F3-B92F-9EE9010A7B34', N'Multiriscos');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('6DB46133-2789-4B5E-B8FF-9EE9010A8E9D', N'Obras e Montagens');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('787B047E-7B15-4DA6-992D-9EE9010A857F', N'Outros Danos a Bens');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('2442DD02-F525-4CE3-9CFF-9EE9010ABC44', N'Responsabilidade');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('370DE151-6ABF-4561-A8BD-9EE9010ADC2A', N'Veículos Ferroviários');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('537C3D97-02C9-41EE-A5DE-9EE9010AF1BB', N'Vida');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CFBC796D-1F64-4646-A964-9EE9010DF63E', N'Por Área', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF', N'Por Conta de Outrém', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('98754CEB-DD92-4ACD-9C2F-9EE9010DE2BE', N'Por Conta Própria/Independentes', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('1F781304-924F-4DE8-B0DE-9EE9010DEEA5', N'Subscritores da CGA', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('1AC7ECE9-11D5-4A70-A19D-9EE9010E13F6', N'Autarcas', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('04B1DE96-A737-4EA4-88E0-9EE9010E0D26', N'Bombeiros', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8C3A644D-A4D7-451E-8858-9EE9010E04E4', N'Escolar', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CE91A9A2-57F4-420E-8FFD-9EE9010E20FB', N'Ocupações de Tempos Livres', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('0714501A-73FF-4EA8-B921-9EE9010E29EA', N'Ocupantes de Viatura', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('5115A4A2-25BD-40B1-88F0-9EE9010E3BE6', N'Tradicional', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('EB02FB3D-6457-4BE1-A540-9EE9010E307E', N'Viagem', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('89344125-0CDB-4242-BF23-9EE901138254', N'Cascos e RC', 'C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('67C79D4A-F869-43FB-BFA0-9EE9011395FE', N'Pessoas Transportadas', 'C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('3F185E97-921A-467D-8B22-9EE901117FE9', N'Colheitas', 'A3F44708-4666-425C-AA70-9EE9010A9E0D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('DA199371-3A66-42D8-BB98-9EE9011178A4', N'Incêndio', 'A3F44708-4666-425C-AA70-9EE9010A9E0D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('D907C48E-810F-4918-800F-9F9201561F42', N'Pecuário', 'A3F44708-4666-425C-AA70-9EE9010A9E0D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('59A58D39-F445-4CF5-8B6B-9EE901119B65', N'Automóvel', '7F5F77EB-8348-4914-8525-9EE9010AB1C6');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('E8950343-8476-428B-AFA1-9F9201568929', N'Carta', '7F5F77EB-8348-4914-8525-9EE9010AB1C6');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('99967F7A-ADAF-4E8D-915A-9EE90113C66D', N'Assistência', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('88DBC911-336B-4098-A0B7-9EE90113BD32', N'Caução', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('AA798E09-2ABE-4DF4-B874-9EE90113B032', N'Crédito', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8B678CB7-90B8-4A51-9226-9F340153F759', N'Produtos à Medida', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('DB2CAB3A-5A68-41B4-B8AA-9EE90113DDA9', N'Protecção Animais', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('3A0110CA-7136-4499-ABCE-9EE90113CF24', N'Protecção Jurídica', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FF0C0086-9322-4AC8-AAC8-9F340151A302', N'Aberta', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('33DA18C2-75B1-4EB7-AEF2-9F340151CC15', N'Gerida', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40', N'Grupo', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('9F56581A-64B9-4D8E-B3CD-9EE9010E63D5', N'Individual', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CF483414-189C-4A0A-998B-9EE901132980', N'Cascos e RC', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('878733B8-77EB-42EF-9E7A-9EE9011349A7', N'Pessoas Transportadas', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C53AA3FC-D528-4A1E-9436-9EE9011332A8', N'Recreio', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('574254C5-E217-4F21-8151-9EE901155527', N'Pensões de Reforma/Invalidez', '696A51D8-B21A-4DA8-B8E9-9EE901153338');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('2D7CCD94-DBD5-43F1-91B5-9EE9010E81DD', N'Industrial', '56A359A3-33DF-40E5-B004-9EE9010A726A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('A711AA7E-0565-42E0-94BE-9EE9010E7901', N'Simples', '56A359A3-33DF-40E5-B004-9EE9010A726A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('976C78A8-A26D-4B26-B583-9EE901131575', N'Mercadorias Transportadas', 'E2732D85-E204-4A71-A5D4-9EE9010AC7B1');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('110FC92D-552E-474F-AF1E-9F340153273E', N'Transporte de Valores', 'E2732D85-E204-4A71-A5D4-9EE9010AC7B1');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F22E2E10-1993-4784-86A3-9EE90110F982', N'All Risks', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('438377BA-2FFA-46B5-AF35-9EE9010EA8DE', N'Comércio e/ou Serviços', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('AF00D387-86D0-4388-AC01-9EE90110F0E2', N'Condomínio', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('66787325-D687-4B2D-8284-9EE9010E9C87', N'Habitação', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('BACE0C3D-6251-42D3-B9F3-9EE9010EB5D5', N'Industrial', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('211BF6E2-9255-4FF1-89AD-9EE9011167D0', N'Obras e Montagens', '6DB46133-2789-4B5E-B8FF-9EE9010A8E9D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('76C1F3CF-B8A5-428E-ADAC-9F9201581A7F', N'RC Excesso', '6DB46133-2789-4B5E-B8FF-9EE9010A8E9D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CBCB9041-8012-46D7-B8F2-9EE901111406', N'Avaria de Máquinas', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C918E867-909B-4962-88AF-9EE9011141B5', N'Bens em Leasing', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('172CFFAF-AF5B-4E3E-980E-9EE901114D02', N'Bens Refrigerados', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FF7777A8-06EB-4303-B0A3-9EE90111396F', N'Campista', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('700E4929-689E-484B-88F8-9EE901110B82', N'Cristais', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('7AD30E66-4799-4595-A4F9-9EE90111324A', N'Equipamentos Electrónicos', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8901C038-BA37-4F40-8359-9EE901112634', N'Lucros Cessantes', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('47FAC08E-F9F0-4695-9CB7-9EE901111DC3', N'Máquinas Cascos', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('E5CDFEBC-926C-4502-9F9A-9EE901110585', N'Roubo', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('A672131B-0928-43C9-90BE-9EE90111C0F4', N'Ambiental', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('26B9F09B-7E14-4F16-8DD9-9EE90112F534', N'Animais', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('DB296936-7961-48CC-A926-9F920158AE61', N'Armas', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('2A5D7B5B-07B4-43C2-8BE0-9EE90112EE56', N'Caçadores', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C98B2362-63CF-4D02-800D-9EE90112CFA4', N'Convenção Mercadorias Rodoviárias', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('846652EE-1711-4869-8290-9EE90111A853', N'Exploração', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('4F2055AC-8447-4A90-A886-9EE90112FB19', N'Familiar', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('4C475D8E-47D9-4ABC-8FB1-9EE90111B67A', N'Produtos', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('7DF2A66C-1200-430D-848A-9EE90111AF93', N'Profissional', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F7634CFD-301E-4373-99DB-9EE90112E4C2', N'Transporte Rodoviário Nacional', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('88B63B22-3BD7-4391-9C69-9EE901135CF9', N'Cascos e RC', '370DE151-6ABF-4561-A8BD-9EE9010ADC2A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('32A04D18-5E9B-4507-A261-9EE9011367AA', N'Pessoas Transportadas', '370DE151-6ABF-4561-A8BD-9EE9010ADC2A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', N'Grupo', '537C3D97-02C9-41EE-A5DE-9EE9010AF1BB');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FDE37FD5-BD29-4AA6-8A30-9EE901151B11', N'Individual', '537C3D97-02C9-41EE-A5DE-9EE9010AF1BB');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('FE18F5ED-F87C-4595-B9AC-9EE90118D568', N'Agrícola', 'CFBC796D-1F64-4646-A964-9EE9010DF63E', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('34E19434-6106-4359-93FE-9EE90118CEE0', N'Construção Civil', 'CFBC796D-1F64-4646-A964-9EE9010DF63E', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('BEBB58B5-CD95-4872-B72F-9EE90118938F', N'Folhas de Férias', '44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '1DC4E820-CAAC-4FE2-A650-9F9601246815');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', N'Prémio Fixo', '44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', N'Única', '98754CEB-DD92-4ACD-9C2F-9EE9010DE2BE', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('68ED97FA-B9F8-40F7-B470-9EE90118B2DB', N'Folhas de Férias', '1F781304-924F-4DE8-B0DE-9EE9010DEEA5', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '1DC4E820-CAAC-4FE2-A650-9F9601246815');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('BB0E78F2-3817-45C6-94C7-9EE90118BA82', N'Prémio Fixo', '1F781304-924F-4DE8-B0DE-9EE9010DEEA5', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('264279D9-A946-4209-AE35-9EE90118F78B', N'Única', '1AC7ECE9-11D5-4A70-A19D-9EE9010E13F6', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('3DD40BED-A453-453F-8F0D-9EE90118EFAB', N'Única', '04B1DE96-A737-4EA4-88E0-9EE9010E0D26', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('3C85B935-B33E-4C86-AD0C-9EE90118E58F', N'Única', '8C3A644D-A4D7-451E-8858-9EE9010E04E4', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('7A545EC7-36AC-4228-9D36-9EE90119060F', N'Única', 'CE91A9A2-57F4-420E-8FFD-9EE9010E20FB', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('8CDAEBD9-F366-4291-B710-9EE901191FAE', N'Frota', '0714501A-73FF-4EA8-B921-9EE9010E29EA', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('23D97FAD-8137-48B9-8BEB-9EE9011914F8', N'Individual', '0714501A-73FF-4EA8-B921-9EE9010E29EA', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('8572FE00-D359-437A-8359-9EE901194A05', N'Grupo', '5115A4A2-25BD-40B1-88F0-9EE9010E3BE6', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('12D8E75F-2C36-4359-ABD7-9EE901194453', N'Individual', '5115A4A2-25BD-40B1-88F0-9EE9010E3BE6', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('CF5EF293-9ED1-4EC1-9760-9F34015146D4', N'Aberta', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('FA485DD9-08FB-44DC-BDCF-9EE9011939A9', N'Grupo', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('92B7DEBE-06A9-45E1-968B-9EE901192D48', N'Individual', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', N'Única', '89344125-0CDB-4242-BF23-9EE901138254', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', N'Única', '67C79D4A-F869-43FB-BFA0-9EE9011395FE', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C6D519C6-7138-4EE9-A954-9EE9011A7CAB', N'Única', '3F185E97-921A-467D-8B22-9EE901117FE9', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('079B1B49-255E-464A-A62F-9EE9011A768E', N'Única', 'DA199371-3A66-42D8-BB98-9EE9011178A4', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('485527A9-70ED-48AB-97F6-9F92015636C9', N'Única', 'D907C48E-810F-4918-800F-9F9201561F42', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', N'Frota', '59A58D39-F445-4CF5-8B6B-9EE901119B65', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('22FE8580-E680-4EC7-9ABB-9EE9011AA269', N'Individual', '59A58D39-F445-4CF5-8B6B-9EE901119B65', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('80694B60-B2C9-4841-83E2-9F920156B362', N'Frota', 'E8950343-8476-428B-AFA1-9F9201568929', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('85458FB2-93F5-4677-A850-9F920156BC56', N'Individual', 'E8950343-8476-428B-AFA1-9F9201568929', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('EB9E9AAA-A33B-4D75-B3FF-9EE9011B7A0A', N'Individual (Viagem)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('FD480C60-2F0B-4D9A-82F8-9F340153B43D', N'Local (Multi-Riscos)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('457B9EA9-0CB7-41B2-AE89-9F340153C9AE', N'Veículo (Viagem Automóvel)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D4A29EF9-6D11-40C7-A87E-9F340153906C', N'Empresarial', '88DBC911-336B-4098-A0B7-9EE90113BD32', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6', N'Individual', '88DBC911-336B-4098-A0B7-9EE90113BD32', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('FC778D70-0752-4411-92AA-9EE9011B7092', N'Única', 'AA798E09-2ABE-4DF4-B874-9EE90113B032', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('E91E32C6-0C55-4F49-BA88-9F340153FD6D', N'Shamir', '8B678CB7-90B8-4A51-9226-9F340153F759', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('7E51C1B4-33F3-41D8-92D6-9EE9011B8221', N'Única', 'DB2CAB3A-5A68-41B4-B8AA-9EE90113DDA9', '7A9A0E31-668A-4113-A03E-9F9501403E6E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('EB87C2F0-7161-4715-81EA-9F340153E524', N'Empresarial', '3A0110CA-7136-4499-ABCE-9EE90113CF24', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('A56384EE-3F17-42E7-9B2C-9EE9011B7DC7', N'Individual', '3A0110CA-7136-4499-ABCE-9EE90113CF24', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('DB605A61-8AC7-427A-8A1C-9F340151C1A9', N'Mista', 'FF0C0086-9322-4AC8-AAC8-9F340151A302', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', N'Rede Convencionada', 'FF0C0086-9322-4AC8-AAC8-9F340151A302', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('F990606F-0AB6-4EE8-BFDA-9F340151B327', N'Reembolso', 'FF0C0086-9322-4AC8-AAC8-9F340151A302', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('BB50ACAA-F557-4249-934F-9F340151D334', N'Única', '33DA18C2-75B1-4EB7-AEF2-9F340151CC15', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', N'Mista', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('CD456E4C-1ED7-4819-AB22-9EE901198248', N'Rede Convencionada', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', N'Reembolso', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('1B70989B-1D54-408E-BDF7-9EE90119749F', N'Mista', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('422AE9F5-03DE-4AE2-830F-9EE901196F1C', N'Rede Convencionada', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('2F2BFF64-61AF-4CE3-8F74-9EE90119687C', N'Reembolso', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('3236E457-D202-48FF-9219-9EE9011B1E39', N'Única', 'CF483414-189C-4A0A-998B-9EE901132980', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', N'Única', '878733B8-77EB-42EF-9E7A-9EE9011349A7', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('54E688CE-8F22-4047-87F3-9EE9011B233A', N'Única', 'C53AA3FC-D528-4A1E-9436-9EE9011332A8', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('5DE6C674-DBEA-4596-A241-9EE9011C14A9', N'Grupo', '574254C5-E217-4F21-8151-9EE901155527', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('F94D6C1A-6456-42A6-BDA9-9EE9011C0EF0', N'Individual', '574254C5-E217-4F21-8151-9EE901155527', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('B3174C0F-DDF9-4466-A419-9EE90119ABDE', N'Única', '2D7CCD94-DBD5-43F1-91B5-9EE9010E81DD', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('995699D1-1674-4B8E-B813-9EE90119A4F7', N'Única', 'A711AA7E-0565-42E0-94BE-9EE9010E7901', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('FC9A126A-6ADB-4483-88CB-9FE2010916EE', N'Continuada', '976C78A8-A26D-4B26-B583-9EE901131575', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', N'Flutuante', '976C78A8-A26D-4B26-B583-9EE901131575', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('722EBA20-3136-4E3B-B3F8-9EE9011B059E', N'Pontual', '976C78A8-A26D-4B26-B583-9EE901131575', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('6C51BDED-B9FC-4F1C-9C08-9FE2010BE557', N'Por Pessoa', '110FC92D-552E-474F-AF1E-9F340153273E', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C4475D8B-72AF-415B-83FE-9F3401532F56', N'Por Viatura', '110FC92D-552E-474F-AF1E-9F340153273E', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('6725D59E-DBFD-46B4-9533-9EE90119E127', N'Única', 'F22E2E10-1993-4784-86A3-9EE90110F982', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('B9CF5874-AE6A-47D2-824C-9EE90119CB66', N'Única', '438377BA-2FFA-46B5-AF35-9EE9010EA8DE', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', N'Única', 'AF00D387-86D0-4388-AC01-9EE90110F0E2', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', N'Única', '66787325-D687-4B2D-8284-9EE9010E9C87', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('9F79A559-E805-4238-8EC0-9EE90119D1A4', N'Única', 'BACE0C3D-6251-42D3-B9F3-9EE9010EB5D5', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('6A2A5C28-6866-463C-9B6A-9F34015284C4', N'Aberta', '211BF6E2-9255-4FF1-89AD-9EE9011167D0', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D36F47AB-0D5A-425D-8FD4-9EE9011A6837', N'Por Obra', '211BF6E2-9255-4FF1-89AD-9EE9011167D0', 'CD709854-DB59-424B-904A-9F9501403847', 'C393E3A8-CEBB-425D-8CAE-9F9601246E83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('691EF827-EF46-4C49-A828-9F92015826D8', N'Única', '76C1F3CF-B8A5-428E-ADAC-9F9201581A7F', 'E04D67FA-F3D9-4597-96F0-9F950140323E', 'C393E3A8-CEBB-425D-8CAE-9F9601246E83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', N'Única', 'CBCB9041-8012-46D7-B8F2-9EE901111406', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C77B46AF-B098-452D-914C-9EE9011A5198', N'Única', 'C918E867-909B-4962-88AF-9EE9011141B5', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('445184E3-326B-4796-8618-9EE9011A5811', N'Em Câmaras', '172CFFAF-AF5B-4E3E-980E-9EE901114D02', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D5BEE08C-E0EA-4D25-9468-9F3401526786', N'Transportados', '172CFFAF-AF5B-4E3E-980E-9EE901114D02', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', N'Única', 'FF7777A8-06EB-4303-B0A3-9EE90111396F', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('252789C2-928E-467A-B081-9EE9011A2E76', N'Única', '700E4929-689E-484B-88F8-9EE901110B82', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', N'Única', '7AD30E66-4799-4595-A4F9-9EE90111324A', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('06CEF06D-73FF-4579-A8F4-9EE9011A08E8', N'Incêndio', '8901C038-BA37-4F40-8359-9EE901112634', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('DBFA4951-59CF-4008-98A5-9EE9011A0F30', N'Máquinas', '8901C038-BA37-4F40-8359-9EE901112634', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', N'Multiriscos', '8901C038-BA37-4F40-8359-9EE901112634', 'CD709854-DB59-424B-904A-9F9501403847', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('D38965BA-1275-4C36-BC89-9EE9011A3C6A', N'Única', '47FAC08E-F9F0-4695-9CB7-9EE901111DC3', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('023C01EF-D4A7-441C-B241-9EE9011A2715', N'Única', 'E5CDFEBC-926C-4502-9F9A-9EE901110585', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C199BF06-E397-454E-80ED-9EE9011AD3EC', N'Única', 'A672131B-0928-43C9-90BE-9EE90111C0F4', 'CD709854-DB59-424B-904A-9F9501403847', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('4AA42557-96B9-4EE5-8B55-9EE9011AE763', N'Única', '26B9F09B-7E14-4F16-8DD9-9EE90112F534', '7A9A0E31-668A-4113-A03E-9F9501403E6E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('BFD2ABF7-195B-4560-BC91-9F920158B876', N'Única', 'DB296936-7961-48CC-A926-9F920158AE61', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('5CA22680-438E-4C4E-8720-9EE9011AE247', N'Única', '2A5D7B5B-07B4-43C2-8BE0-9EE90112EE56', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('85EA331B-094E-4BFC-B423-9EE9011AD85E', N'Única', 'C98B2362-63CF-4D02-800D-9EE90112CFA4', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C6EC530E-8C28-4EDB-BCE3-9EE9011AC699', N'Única', '846652EE-1711-4869-8290-9EE90111A853', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('363ED4D5-26D9-4EDF-8123-9EE9011AEB24', N'Única', '4F2055AC-8447-4A90-A886-9EE90112FB19', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('533DA89B-1BEC-474B-B615-9EE9011ACF4F', N'Única', '4C475D8E-47D9-4ABC-8FB1-9EE90111B67A', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('DF4653EC-E2FE-4FCE-832A-9F340152C13B', N'Empresarial', '7DF2A66C-1200-430D-848A-9EE90111AF93', 'E04D67FA-F3D9-4597-96F0-9F950140323E', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('7CC94668-25FC-47E7-ADB4-9EE9011ACA6B', N'Individual', '7DF2A66C-1200-430D-848A-9EE90111AF93', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('B5EE5408-8065-4F50-BEB1-9EE9011ADD5A', N'Única', 'F7634CFD-301E-4373-99DB-9EE90112E4C2', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', N'Única', '88B63B22-3BD7-4391-9C69-9EE901135CF9', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('948E3387-F8F2-4E5C-8373-9EE9011B44D4', N'Única', '32A04D18-5E9B-4507-A261-9EE9011367AA', 'E3E7B018-6F07-42DA-8B54-9F9501402D25', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('97D0A05A-0DEA-4CA5-8492-9EE9011BD540', N'Financeiro', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('F66BB2D5-CAF3-40FF-ACAC-9EE9011BE0C9', N'Plano Poupança Reforma', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', N'Rendas', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('5EC79210-F2BE-4775-9A5E-9EE9011BC36E', N'Risco', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('C304BE3B-7B80-412A-B277-9EE9011BFB56', N'Títulos de Capitalização', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('4FF96FBB-02FA-4864-9843-9EE9011BAA79', N'Financeiro', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('8B36A6D9-C639-4CB2-AB89-9EE9011BB42E', N'Plano Poupança Reforma', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('50A579F9-8546-4923-90DE-9EE9011BA3EB', N'Rendas', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('DBBCFA61-888C-415A-9A16-9EE9011B9F52', N'Risco', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '8A81B8E7-B267-4674-ABCA-9F960124622B');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine, FKObjectType, FKPeriodType) values ('F4333BC2-A0DB-4CD2-8FB2-9EE9011BBCAC', N'Títulos de Capitalização', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11', 'EDD94689-EFED-4B50-AA6E-9F9501402700', '0E99E224-9778-408B-829C-9F9601245A83');
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D0198709-B571-4F50-B1C0-9F9100E6F9A3', N'(Cabeçalho)', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('16BEE498-3A6C-4DCD-9439-9F9100E7139F', N'Legal', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5DA0C14C-7CC1-46A2-B4C3-9F9100E7337B', N'Despesas Médicas no Estrangeiro', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4B3319E4-701A-44A5-A099-9F9100E725DD', N'Extensão Territorial', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4349A3EC-7C3E-4BFC-9A23-9F9100E73E5F', N'Repatriamento', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2BFD79A2-DB7A-4BB4-88F8-9F9100E71C6C', N'Salário Integral', 'FE18F5ED-F87C-4595-B9AC-9EE90118D568', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('ADFAF00A-5818-44FC-B261-9F9100E6F9A3', N'(Cabeçalho)', '34E19434-6106-4359-93FE-9EE90118CEE0', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2843E249-6C9B-44CF-B416-9F9100E751C9', N'Legal', '34E19434-6106-4359-93FE-9EE90118CEE0', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E06C685D-2949-47D7-8590-9F9100E76D71', N'Despesas Médicas no Estrangeiro', '34E19434-6106-4359-93FE-9EE90118CEE0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('45194DE6-FEF5-4D05-8397-9F9100E761DB', N'Extensão Territorial', '34E19434-6106-4359-93FE-9EE90118CEE0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('986D75A0-AC49-4114-9FE1-9F9100E775D7', N'Repatriamento', '34E19434-6106-4359-93FE-9EE90118CEE0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('28D984C1-2F50-405A-A38E-9F9100E7591C', N'Salário Integral', '34E19434-6106-4359-93FE-9EE90118CEE0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9078E11B-BBAD-4D2F-A1D7-9F9100E6F9A3', N'(Cabeçalho)', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('53F0CDF9-593E-41D1-BD15-9F9100E78B08', N'Legal', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('49C7C6B7-6ED1-4DA1-9AD8-9F9100E7ABC1', N'Despesas Médicas no Estrangeiro', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2EEC5EEC-C3A6-4716-9F01-9F9100E79EDA', N'Extensão Territorial', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B6B0084F-30AF-4653-8695-9F9100E7B493', N'Repatriamento', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B2A427E0-700D-4BBF-B861-9F9100E7948C', N'Salário Integral', 'BEBB58B5-CD95-4872-B72F-9EE90118938F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('ABD97644-09A1-482F-8356-9F9100E6F9A3', N'(Cabeçalho)', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6B184045-3035-48A6-9902-9F9100E7C53F', N'Legal', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A6A7D27A-0F81-4788-95FC-9F9100E7E178', N'Despesas Médicas no Estrangeiro', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('95C04EDA-4660-4F78-81F0-9F9100E7D5F0', N'Extensão Territorial', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('12585AC1-CC8D-4494-807D-9F9100E7E809', N'Repatriamento', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D50487E2-F410-43B7-B61D-9F9100E7CD3E', N'Salário Integral', 'CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A3D2B92E-A446-481D-9C9F-9F9100E6F9A3', N'(Cabeçalho)', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('39D5636E-10FE-4C2F-AD8B-9F9100E80AC1', N'Legal', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('25C5D7A7-7B31-4992-BFF2-9F9100E82BC4', N'Despesas Médicas no Estrangeiro', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A5ABCEB9-FE61-46E0-A3BD-9F9100E81CED', N'Extensão Territorial', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E4E72739-9725-44DB-BF5D-9F9100E83324', N'Repatriamento', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D3172D15-A14B-4EFB-8972-9F9100E812C9', N'Salário Integral', '1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('39650737-B4BC-4ACA-8E70-9F9100E6F9A3', N'(Cabeçalho)', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('31CC0700-01B8-4954-9881-9F9100E84636', N'Legal', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4C8BBEF8-A8DB-4B06-AC47-9F9100E86B79', N'Despesas Médicas no Estrangeiro', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E10D89AF-EAA8-41C2-A84D-9F9100E85EAE', N'Extensão Territorial', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4CB3AFC2-78AE-42FD-B3E6-9F9100E8727B', N'Repatriamento', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F81F3669-F569-45E6-BBE8-9F9100E85461', N'Salário Integral', '68ED97FA-B9F8-40F7-B470-9EE90118B2DB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A1CE9ACF-E258-408D-888C-9F9100E6F9A3', N'(Cabeçalho)', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('29676A25-26AB-4809-90EB-9F9100E88732', N'Legal', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('128A47EF-D57E-4F23-AA7E-9F9100E8A400', N'Despesas Médicas no Estrangeiro', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('684D0EBF-F41C-4488-8268-9F9100E897E3', N'Extensão Territorial', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('95277E1D-2C6B-408C-B6BE-9F9100E8AB8B', N'Repatriamento', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B8225845-E4C7-4BAC-8375-9F9100E88F91', N'Salário Integral', 'BB0E78F2-3817-45C6-94C7-9EE90118BA82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DD77CBB5-1B90-4CA1-A5A5-9F9100E6F9A3', N'(Cabeçalho)', '264279D9-A946-4209-AE35-9EE90118F78B', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('85FD8DFC-9C07-4F46-A008-9F9100E94CA5', N'Despesas de Funeral', '264279D9-A946-4209-AE35-9EE90118F78B', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C0E493B3-CAFE-4847-8807-9F9100E91324', N'Despesas de Tratamento', '264279D9-A946-4209-AE35-9EE90118F78B', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7FFE5DEF-09E4-411D-A15E-9F9100E91F21', N'Incapacidade Temporária', '264279D9-A946-4209-AE35-9EE90118F78B', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('15DD035B-A8AE-46C1-98F5-9F9100E90A32', N'Morte ou Invalidez Permanente', '264279D9-A946-4209-AE35-9EE90118F78B', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('922106F7-85FE-42CD-9328-9FE200BAE1B2', N'Coberturas Facultativas', '264279D9-A946-4209-AE35-9EE90118F78B', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D4AE818B-DE1E-41D6-988F-9F9100E6F9A3', N'(Cabeçalho)', '3DD40BED-A453-453F-8F0D-9EE90118EFAB', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9E7830EB-AE30-47ED-8573-9F9100E981FE', N'Despesas de Tratamento', '3DD40BED-A453-453F-8F0D-9EE90118EFAB', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A71632CF-0BCB-476B-9BCE-9F9100E978BF', N'Incapacidade Temporária Absoluta', '3DD40BED-A453-453F-8F0D-9EE90118EFAB', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('22367F2D-A7F4-4B6B-93AD-9F9100E96B26', N'Morte ou Invalidez Permanente', '3DD40BED-A453-453F-8F0D-9EE90118EFAB', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1333BE8D-892E-409A-A1E4-9F9100E98CAD', N'Coberturas Facultativas', '3DD40BED-A453-453F-8F0D-9EE90118EFAB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('78C9F3B9-93D9-4A39-BBFE-9F9100E6F9A3', N'(Cabeçalho)', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8BF7D70B-42DB-4112-B1D5-9F9100E9F9FA', N'Despesas de Funeral', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DD92034F-5A35-4167-BE0F-9F9100E9EF40', N'Despesas de Tratamento', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C5040BC5-8D7F-444E-B3CB-9F9100EA1B63', N'Extensão de Coberturas', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('82E12F62-749D-49BA-B231-9F9100E9D534', N'Invalidez Permanente', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D5BE7708-5361-4577-9472-9F9100E9CCA3', N'Morte', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0627C3E7-6B91-4D1D-969F-9F9100E9E14D', N'Morte ou Invalidez Permanente', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B9D9BC06-341D-4028-A8AE-9F9100EA0629', N'Responsabilidade Civil Alunos', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0FC142A4-6CCA-4C2E-A353-9F9100EA1167', N'Responsabilidade Civil Estabelecimento', '3C85B935-B33E-4C86-AD0C-9EE90118E58F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4A934E55-390C-4F36-A5E1-9F9100E6F9A3', N'(Cabeçalho)', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('16FEFA79-EC6D-4A72-AE7C-9F9100EA6E24', N'Despesas de Funeral', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('33BDD13E-B656-46DB-8D38-9F9100EAA202', N'Despesas de Substituição ou Reparação de Próteses', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7D697781-7B0B-4DCD-99F7-9F9100EA62A6', N'Despesas de Tratamento e Repatriamento', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('03845F86-8361-4FED-8F09-9F9100EA7C79', N'Incapacidade Temporária Absoluta', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3066B070-B0ED-44C6-92E0-9F9100EA8CC3', N'Incapacidade Temporária Absoluta Hospitalar', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A9BB9834-AD38-4944-92C5-9F9100EA4B4C', N'Invalidez Permanente', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A389A2B0-E3A3-4507-B1A1-9F9100EA42F9', N'Morte', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DA28BCE4-27DF-498D-9DE1-9F9100EA553A', N'Morte ou Invalidez Permanente', '7A545EC7-36AC-4228-9D36-9EE90119060F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('83C9938E-156B-49A4-920F-9F9100E6F9A3', N'(Cabeçalho)', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B252D297-1E75-4571-95BE-9F9100EB37AB', N'Bagagens', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('77CEB809-D7B0-4A7A-9C6B-9F9100EB171D', N'Despesas de Funeral', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('67D27A14-484E-45A7-B117-9F9100EB0D99', N'Despesas de Tratamento e Repatriamento', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DAEA1358-764A-4F60-9A2A-9F9100EB2779', N'Incapacidade Temporária Absoluta Hospitalar', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('102A0AEF-5296-43C5-8762-9F9100EAE5C2', N'Invalidez Permanente', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('ACC58723-C3DB-45E4-96C1-9F9100EAD7AF', N'Morte', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('491D63FD-79C7-4324-A6B5-9F9100EAF457', N'Morte ou Invalidez Permanente', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CCB7489D-EAF0-4A78-A8C1-9F9100EB304C', N'Vestuário', '8CDAEBD9-F366-4291-B710-9EE901191FAE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B2935000-09FA-41EB-BC23-9F9100E6F9A3', N'(Cabeçalho)', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('35DA5FEE-CA6D-47B7-AB17-9F9100EE6F6C', N'Bagagens', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3BFC68E9-0AEA-412E-8736-9F9100EE272A', N'Despesas de Funeral', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E1187E96-78C0-499A-92AF-9F9100EE00E1', N'Despesas de Tratamento ou Repatriamento', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DD08D88D-D50C-4676-9904-9F9100EE4B26', N'Incapacidade Temporária Absoluta Hospitalar', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7BB0AF1E-4032-4D8F-AA46-9F9100EDBDB9', N'Invalidez Permanente', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B9304A4B-3AB6-4404-97E9-9F9100EB5FE0', N'Morte', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('05F08CAB-5633-410D-BB0B-9F9100EDE748', N'Morte ou Invalidez Permanente', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F1F64F0A-9935-40FD-84EC-9F9100EE5586', N'Vestuário', '23D97FAD-8137-48B9-8BEB-9EE9011914F8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('772630B1-8EDA-4C38-A21C-9F9100E6F9A3', N'(Cabeçalho)', '8572FE00-D359-437A-8359-9EE901194A05', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8F979BD1-0552-4489-9E39-9F9100EF95B7', N'Despesas de Funeral', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('96F0704B-60E5-4A05-AFDC-9F9100EF2AB1', N'Despesas de Tratamento', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CC08152E-97B1-4D3F-BBFC-9F9100EFA240', N'Extensão de Cobertura', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F6CA1956-1E67-4D23-92A6-9F9100EF4FA4', N'Incapacidade Temporária', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5FDE630B-C6D8-471F-AD4D-9F9100EF6005', N'Incapacidade Temporária com Int. Hospitalar', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C0187C77-8284-40FE-B160-9F9100EED6CD', N'Invalidez Permanente', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6FB28062-2669-4F40-A76C-9F9100EEC7F8', N'Morte', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('60ECCDA4-4348-4267-B397-9F9100EF0FA9', N'Morte ou Invalidez Permanente', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DBDB405F-B318-4A6E-881A-9F9100EFD5AA', N'Outras Coberturas', '8572FE00-D359-437A-8359-9EE901194A05', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', N'(Cabeçalho)', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A50E35D3-6133-4A2D-B649-9F9100F03A17', N'Despesas de Funeral', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('093F401E-AAD3-4B90-B9F1-9F9100F002E1', N'Despesas de Tratamento', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5635665A-D469-4D0C-AB93-9F9100F04356', N'Extensão de Cobertura', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DB7BDA6B-3AD1-4F50-BFB2-9F9100F00D87', N'Incapacidade Temporária', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('81EE9674-2117-4A14-A7CA-9F9100F0234F', N'Incapacidade Temporária com Int. Hospitalar', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('00C5FC98-23B5-46E4-821B-9F9100EFF572', N'Invalidez Permanente', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('77B30273-EDB0-44F4-8933-9F9100EFECB4', N'Morte', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('233419B1-F85D-4DE1-9864-9F9100F04B92', N'Outras Coberturas', '12D8E75F-2C36-4359-ABD7-9EE901194453', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('69D55B4D-C1EB-409B-A71F-9F9100E6F9A3', N'(Cabeçalho)', 'CF5EF293-9ED1-4EC1-9760-9F34015146D4', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('98F93D87-397D-4932-B3B8-9F9100E6F9A3', N'(Cabeçalho)', 'FA485DD9-08FB-44DC-BDCF-9EE9011939A9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8B481840-216E-4A6F-BA44-9F9100E6F9A3', N'(Cabeçalho)', '92B7DEBE-06A9-45E1-968B-9EE901192D48', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7D9388E0-3EEB-4540-B24B-9F9100E6F9A3', N'(Cabeçalho)', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('341DD8F6-B49F-434F-AFF7-9F9100F08C56', N'Responsabilidade Civil', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0397D532-C81A-49B2-ABB2-9F9100F0A856', N'Assistência', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8FC3B0AC-F171-40C5-B41C-9F9100F09AAF', N'Danos Próprios', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('10C0C0EF-3DF3-45E6-9E56-9F9100F0A0DA', N'Ocupantes', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('20FC09E8-7625-4470-92C0-9F9100F0B53D', N'Outras Coberturas Complementares', '11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AA0226DF-F7E2-4267-A55A-9F9100E6F9A3', N'(Cabeçalho)', 'D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('380495C9-B7B2-46E6-9028-9F9100F0DDEE', N'Morte ou Invalidez Permanente', 'D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0FF74595-5DAA-40C2-AC7E-9F9100F0F25C', N'Despesas de Funeral', 'D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6D0EC43D-C3C4-4DBD-95A8-9F9100F0E71B', N'Despesas de Tratamento', 'D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6B5267B2-F5FB-4E23-A337-9F9100F0FA3B', N'Outras Coberturas', 'D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F7188DB5-563A-49B1-9278-9F9100E6F9A3', N'(Cabeçalho)', 'C6D519C6-7138-4EE9-A954-9EE9011A7CAB', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A5F93E55-C47D-4D4A-AC44-9F9100E6F9A3', N'(Cabeçalho)', '079B1B49-255E-464A-A62F-9EE9011A768E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('91718D5E-4550-4E92-B4AC-9F920156595F', N'(Cabeçalho)', '485527A9-70ED-48AB-97F6-9F92015636C9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', N'(Cabeçalho)', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('46B29BCF-C20C-436C-ABE5-9F9100F13C5A', N'Responsabilidade Civil', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1B09503B-2F55-4E93-82B7-9F9100F16C43', N'Actos de Vandalismo', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BDB856DA-08BC-4311-9DEE-9F9100F18AF4', N'Assistência em Viagem', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DCBE1EBD-89E2-4780-9297-9F9100F147B7', N'Choque, Colisão ou Capotamento', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B88E4073-406D-48AC-8B85-9F9100F16431', N'Fenómenos da Natureza', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8F859C81-E86C-4BDE-90CD-9F9100F15B35', N'Furto ou Roubo', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('59B88B59-3D62-42B8-A189-9F9100F17659', N'Greves ou Tumultos', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E73592C7-C9A0-4DA8-8C92-9F9100F153FF', N'Incêndio, Raio ou Explosão', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B037498E-417C-45ED-A752-9F9100F1B4AD', N'Ocupantes', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6BBEADFA-B560-42EA-ADA2-9F9100F1BDF0', N'Ocupantes e Condutor', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FB4E251D-9B97-4A84-BADB-9F9100F1AD3B', N'Privação de Uso', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F198F05F-C7BD-4AA2-9F96-9F9100F19693', N'Protecção Jurídica', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('80E48C27-13C9-4057-BCF0-9F9100F18170', N'Quebra Isolada de Vidros', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5F57EC4F-4EFE-493E-A6B8-9F9100F1C445', N'Valor em Novo', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D6D65422-8EF5-4C08-98B4-9F9100F1A27D', N'Veículo de Substituição', 'E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4982E6DF-8BAA-458C-9119-9F9100E6F9A3', N'(Cabeçalho)', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CF9B8F7D-3775-4A7D-8C33-9F9100F1E484', N'Responsabilidade Civil', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1FDBAD29-23DE-4326-BAC9-9F9100F21D40', N'Actos de Vandalismo', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('97E68C38-1F6F-450D-876F-9F9100F23D1C', N'Assistência em Viagem', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FA2D361C-5304-4EC0-BCD7-9F9100F1EF5E', N'Choque, Colisão ou Capotamento', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6EEBB0AE-A75E-4E3B-A89F-9F9100F213D8', N'Fenómenos da Natureza', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('76C425CE-F085-4EB6-8F30-9F9100F205FD', N'Furto ou Roubo', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C58F051A-61A9-4F5E-8E80-9F9100F22739', N'Greves ou Tumultos', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DE2DC8B4-CBD2-4077-8AB2-9F9100F1FE57', N'Incêndio, Raio ou Explosão', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('511D1068-0972-4391-BD47-9F9100F26534', N'Ocupantes', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('354EC66B-777C-4F6E-8CA4-9F9100F26E6E', N'Ocupantes e Condutor', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9B6E2F87-EF5A-48E0-9878-9F9100F259B0', N'Privação de Uso', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('56684368-10E3-4E7E-89BB-9F9100F24625', N'Protecção Jurídica', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FE0B05DD-9962-4C46-9761-9F9100F233BD', N'Quebra Isolada de Vidros', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E96A9C21-8C66-473C-B4F8-9F9100F27627', N'Valor em Novo', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('946551DB-FBBA-405D-9C66-9F9100F250E9', N'Veículo de Substituição', '22FE8580-E680-4EC7-9ABB-9EE9011AA269', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3BACED07-2925-43FC-A826-9F920156C2D1', N'(Cabeçalho)', '80694B60-B2C9-4841-83E2-9F920156B362', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('651AB3A5-9EE1-459C-8B34-9F920156E9FB', N'Responsabilidade Civil', '80694B60-B2C9-4841-83E2-9F920156B362', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('19097AF5-3B1D-4C5A-B08B-9F920156F522', N'Choque, Colisão ou Capotamento', '80694B60-B2C9-4841-83E2-9F920156B362', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F11820BB-A71A-4A4F-B5A2-9F92015708B4', N'Furto ou Roubo', '80694B60-B2C9-4841-83E2-9F920156B362', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('86F1ED84-BA95-4AC0-981C-9F9201570069', N'Incêndio, Raio ou Explosão', '80694B60-B2C9-4841-83E2-9F920156B362', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B575B7DA-578B-4752-B503-9F92015710FB', N'Outras Coberturas', '80694B60-B2C9-4841-83E2-9F920156B362', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4388DE20-3D55-47DA-ADA9-9F920156C862', N'(Cabeçalho)', '85458FB2-93F5-4677-A850-9F920156BC56', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4DC4B22B-0880-4BAF-A93F-9F9201572406', N'Responsabilidade Civil', '85458FB2-93F5-4677-A850-9F920156BC56', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4F92B241-C67E-4B88-8B66-9F9201572EBC', N'Choque, Colisão ou Capotamento', '85458FB2-93F5-4677-A850-9F920156BC56', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('89B2E9A5-9176-4618-8A3F-9F920157432F', N'Furto ou Roubo', '85458FB2-93F5-4677-A850-9F920156BC56', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0D30423F-7C48-4388-866A-9F92015739CB', N'Incêndio, Raio ou Explosão', '85458FB2-93F5-4677-A850-9F920156BC56', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2228705E-E723-4F8D-A505-9F9201574ADB', N'Outras Coberturas', '85458FB2-93F5-4677-A850-9F920156BC56', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F43ED40B-CCEE-4C20-90FD-9F9100E6F9A3', N'(Cabeçalho)', 'EB9E9AAA-A33B-4D75-B3FF-9EE9011B7A0A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('75BC6E33-97F0-4D6C-A319-9F9100E6F9A3', N'(Cabeçalho)', 'FD480C60-2F0B-4D9A-82F8-9F340153B43D', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C63B5392-89F3-4B21-940B-9F9100E6F9A3', N'(Cabeçalho)', '457B9EA9-0CB7-41B2-AE89-9F340153C9AE', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', N'(Cabeçalho)', 'D4A29EF9-6D11-40C7-A87E-9F340153906C', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('06197715-35E1-4BDC-9BC7-9F9100E6F9A3', N'(Cabeçalho)', '5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A7815292-7D9C-4751-83F0-9F9100E6F9A3', N'(Cabeçalho)', 'FC778D70-0752-4411-92AA-9EE9011B7092', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('52CFE26F-42BC-4D4F-957C-9F9100E6F9A3', N'(Cabeçalho)', 'E91E32C6-0C55-4F49-BA88-9F340153FD6D', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('444FE5C1-8A7F-4FAB-AFDD-9F9100E6F9A3', N'(Cabeçalho)', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('52AA32B2-CE11-4B6B-BE3E-9F9100FB7D34', N'Despesas Médicas e Medicamentosas', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1AFF5FC4-BD46-47A1-A8F1-9F9100FB8C44', N'Morte', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7DB2A784-8EC3-491F-B601-9F9100FB9F9C', N'Outras Coberturas', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BE2F0D11-66D9-44CE-8396-9F9100FB85E5', N'Prática de Caça', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D4CC7608-056B-4D25-B20E-9F9100FB94F5', N'Responsabilidade Civil', '7E51C1B4-33F3-41D8-92D6-9EE9011B8221', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E849CCC0-FB25-4872-B600-9F9100E6F9A3', N'(Cabeçalho)', 'EB87C2F0-7161-4715-81EA-9F340153E524', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FDB4E489-8433-4163-B770-9F9100E6F9A3', N'(Cabeçalho)', 'A56384EE-3F17-42E7-9B2C-9EE9011B7DC7', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', N'(Cabeçalho)', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6BAF112B-C5E9-4FF0-A0C7-9F9100FBC050', N'Internamento Hospitalar', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5091EE55-C18C-4F43-B402-9F9100FBCF5B', N'Ambulatório', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('486CC859-D2A1-4C70-88E8-9F9100FBFDE4', N'Doenças Graves', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('25209EFA-0A9B-4127-BB67-9F9100FBD701', N'Estomatologia', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('82C211F8-FDA5-4D7F-A580-9F9100FC104E', N'Extensão Territorial', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('97508E04-2328-4AE2-A848-9F9100FBE230', N'Medicamentos', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3043ECBD-DAF2-4933-B227-9F9100FC1836', N'Outras Coberturas', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2A884F86-5752-4367-9091-9F9100FBC6C5', N'Parto', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('386596D0-0C43-4181-ACF7-9F9100FBE9A7', N'Próteses', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2EE268DD-F91E-4C1C-AD99-9F9100FBF54B', N'Próteses e Ortóteses', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EB60AC74-F6C4-4615-880A-9F9100FC07B4', N'Segunda Opinião Médica', 'DB605A61-8AC7-427A-8A1C-9F340151C1A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', N'(Cabeçalho)', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('45823667-05AC-4F75-8F73-9F9100FC2F2E', N'Internamento Hospitalar', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F36EF742-51E3-494D-8AA6-9F9100FC3C1E', N'Ambulatório', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('212FBAE7-B7DD-412F-90A0-9F9100FC68DC', N'Doenças Graves', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F607DB83-88D8-4997-8130-9F9100FC43BF', N'Estomatologia', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6345B6E1-E79B-45CF-80CA-9F9100FC7E66', N'Extensão Territorial', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1F0977A5-C0EF-477F-9210-9F9100FC4AD0', N'Medicamentos', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AB99B436-91CB-4DC3-B0C7-9F9100FC8637', N'Outras Coberturas', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('746EE69B-F8EA-4F02-B566-9F9100FC34DF', N'Parto', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AD416A46-6C74-4A54-9FBB-9F9100FC5214', N'Próteses', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A8158091-7DFE-4E89-A3B4-9F9100FC5B6E', N'Próteses e Ortóteses', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('902909C4-985D-491F-BA0A-9F9100FC760E', N'Segunda Opinião Médica', 'E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', N'(Cabeçalho)', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('538A5E80-E43D-42AC-A303-9F9100FC960C', N'Internamento Hospitalar', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9076D1C2-90E6-49B6-820B-9F9100FCA321', N'Ambulatório', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E47ABFD8-2C44-4130-B3EA-9F9100FCC990', N'Doenças Graves', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F006A858-4377-4979-94B3-9F9100FCAB24', N'Estomatologia', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('128DBA1E-73B2-4ACE-8327-9F9100FCDAE9', N'Extensão Territorial', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1D9EE6CD-F646-4D82-8F64-9F9100FCB22C', N'Medicamentos', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CC2471E4-7EDF-4290-B670-9F9100FCEBA7', N'Outras Coberturas', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('29B4953D-F07E-4F2E-AA90-9F9100FC9BB3', N'Parto', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('64772CFA-55E7-4BE5-826D-9F9100FCB8CC', N'Próteses', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('70D081FD-BE0E-45F3-ABB6-9F9100FCC19E', N'Próteses e Ortóteses', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4CF5D75F-503E-4A9C-A899-9F9100FCD259', N'Segunda Opinião Médica', 'F990606F-0AB6-4EE8-BFDA-9F340151B327', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9745381A-347E-4C0E-89C4-9F9100E6F9A3', N'(Cabeçalho)', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('93CF04E9-8966-436F-9FD0-9F9100FD05A9', N'Internamento Hospitalar', 'BB50ACAA-F557-4249-934F-9F340151D334', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8813E06E-47EF-4045-A518-9F9100FD15A4', N'Ambulatório', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('36ECE754-5AA7-48EB-973D-9F9100FD3D13', N'Doenças Graves', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('05CAB911-48E9-4163-BA86-9F9100FD1D4A', N'Estomatologia', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2454B340-04ED-475E-97D8-9F9100FD4FE3', N'Extensão Territorial', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('416992A3-83F2-4A03-BE3D-9F9100FD24CA', N'Medicamentos', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DE5C910E-0DAD-4068-867B-9F9100FD59AE', N'Outras Coberturas', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D6D2A150-07DC-4D90-BD05-9F9100FD0C99', N'Parto', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5466E21A-F8C7-4601-A822-9F9100FD2C18', N'Próteses', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FF6932CF-D874-4E3B-86CE-9F9100FD348C', N'Próteses e Ortóteses', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('272FEA4D-5433-48DF-8F68-9F9100FD46E3', N'Segunda Opinião Médica', 'BB50ACAA-F557-4249-934F-9F340151D334', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', N'(Cabeçalho)', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BB5C7FF1-78B8-4E99-86E3-9F9100FD6CA8', N'Internamento Hospitalar', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5B4F49E2-E70C-4F04-AF43-9F9100FD78CC', N'Ambulatório', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('510B19FD-C7DE-482E-A9DB-9F9100FD9CA9', N'Doenças Graves', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7B9ECEE1-B84F-44B7-A70A-9F9100FD7EC2', N'Estomatologia', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DBA54160-177F-443B-84AF-9F9100FDAF46', N'Extensão Territorial', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F95693A4-5A7F-4088-8DBB-9F9100FD8642', N'Medicamentos', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('54748891-5ACA-4B50-B0D1-9F9100FDCC06', N'Outras Coberturas', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2CEC2563-0099-4CF6-A76A-9F9100FD7182', N'Parto', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7D54F404-4710-458C-A2EF-9F9100FD8D61', N'Próteses', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('033BD33C-65B3-47FB-8EBC-9F9100FD95CC', N'Próteses e Ortóteses', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('27B5802D-46E7-4938-9D47-9F9100FDA673', N'Segunda Opinião Médica', 'D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', N'(Cabeçalho)', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('86158294-BC3E-480B-BC97-9F9100FDE030', N'Internamento Hospitalar', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B263B84B-0B36-4B81-B32D-9F9100FDEB48', N'Ambulatório', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2697EC08-0FDD-4174-9B96-9F9100FE4CCE', N'Doenças Graves', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1B929B6D-E471-45AA-B533-9F9100FDF217', N'Estomatologia', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('012F7718-4904-4AAD-AAEB-9F9100FE602B', N'Extensão Territorial', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('324F3B1E-208F-4A23-B485-9F9100FE31B8', N'Medicamentos', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('17E05A70-937B-4E08-A607-9F9100FE69E2', N'Outras Coberturas', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D572D20D-0E39-4C66-AF1A-9F9100FDE588', N'Parto', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BB2E9DB8-3AAF-438B-9C35-9F9100FE3A7C', N'Próteses', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DB9447DE-C7D7-41B9-8C64-9F9100FE44E6', N'Próteses e Ortóteses', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5BE8E894-40B6-454D-AEE9-9F9100FE570C', N'Segunda Opinião Médica', 'CD456E4C-1ED7-4819-AB22-9EE901198248', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', N'(Cabeçalho)', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('99F35BF2-9C15-4166-9B22-9F9100FE7FD4', N'Internamento Hospitalar', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B71A0880-9A77-477B-9531-9F9100FE8D84', N'Ambulatório', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('93EF0AB4-F4B7-4D48-9490-9F9100FEB28A', N'Doenças Graves', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6D2AB153-039A-4584-96A2-9F9100FE945C', N'Estomatologia', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C9994D93-3485-498D-A345-9F9100FEC82C', N'Extensão Territorial', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F50DF3D3-F77E-491F-BD26-9F9100FE9B5F', N'Medicamentos', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B308D706-4CB6-479B-AEB8-9F9100FED1EC', N'Outras Coberturas', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3CE58987-C7B1-49DB-AAAB-9F9100FE8596', N'Parto', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C9FB1272-1CD9-43D1-9819-9F9100FEA25F', N'Próteses', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8AE1588C-881B-48A6-8419-9F9100FEAB04', N'Próteses e Ortóteses', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F5521D83-8B3F-4573-A77C-9F9100FEBE45', N'Segunda Opinião Médica', 'C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EBF88313-E4D2-4647-A133-9F9100E6F9A3', N'(Cabeçalho)', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2A3482A8-D87C-4FFD-8ACE-9F9100FEE4C6', N'Internamento Hospitalar', '1B70989B-1D54-408E-BDF7-9EE90119749F', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EC823B27-6B82-49A3-B38C-9F9100FEF2D9', N'Ambulatório', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FC69D7A0-6497-4FD6-AA4B-9F9100FF162B', N'Doenças Graves', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('05642DCD-3757-4121-8F37-9F9100FEF97D', N'Estomatologia', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D014DEEF-2C61-4953-BA9C-9F9100FF2A0F', N'Extensão Territorial', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1E784212-3503-4F38-9A0E-9F9100FF0051', N'Medicamentos', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('76054AB7-2ED2-4B36-91E4-9F9100FF31EA', N'Outras Coberturas', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6E621002-C94E-4387-8404-9F9100FEE9C0', N'Parto', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B09F89D3-EB1E-43A2-975E-9F9100FF05AE', N'Próteses', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F84934FE-FD08-4217-8838-9F9100FF0DA4', N'Próteses e Ortóteses', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0500A741-ED5B-43BC-B7BC-9F9100FF2045', N'Segunda Opinião Médica', '1B70989B-1D54-408E-BDF7-9EE90119749F', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CA9799E4-836E-4EA2-99DA-9F9100E6F9A3', N'(Cabeçalho)', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5DFE7500-2361-4EA9-BAEE-9F9100FF417D', N'Internamento Hospitalar', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CC622E47-27BA-4409-98D3-9F9100FF4E71', N'Ambulatório', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('686018FB-A520-47D2-A5E0-9F9100FF7190', N'Doenças Graves', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E7B1B9B2-4C5C-42A8-9995-9F9100FF553B', N'Estomatologia', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4D142E58-52E8-4C2E-833E-9F9100FF83D8', N'Extensão Territorial', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B4CC45E4-BA82-44F6-BB97-9F9100FF5C05', N'Medicamentos', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BFF044A2-D6ED-4FEE-921E-9F9100FF8BFD', N'Outras Coberturas', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6D69C345-7FE5-4DC6-A301-9F9100FF46B8', N'Parto', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('22748379-83E5-4DC4-9A65-9F9100FF62BD', N'Próteses', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BE7D02D6-B95A-4FA1-8059-9F9100FF6AA4', N'Próteses e Ortóteses', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D455C95D-F6DB-4AF9-BF5A-9F9100FF7B22', N'Segunda Opinião Médica', '422AE9F5-03DE-4AE2-830F-9EE901196F1C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6453F5B5-0295-4008-8931-9F9100E6F9A3', N'(Cabeçalho)', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A1214F6A-99AC-41E8-9F8D-9F9100FF9E79', N'Internamento Hospitalar', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EB35158B-3D87-444E-9440-9F9100FFA9D1', N'Ambulatório', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('43051DB2-DBEE-415B-A585-9F9100FFCC47', N'Doenças Graves', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('54BC46D1-C140-4DC7-9BD8-9F9100FFB01D', N'Estomatologia', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1BFDDB3A-728B-424B-A1A4-9F9100FFE072', N'Extensão Territorial', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EB33D883-8E0D-4F35-8D15-9F9100FFB692', N'Medicamentos', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9C66E69E-0ABB-45F9-8DB6-9F9100FFE9A6', N'Outras Coberturas', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2189B403-577A-4835-BA66-9F9100FFA3DF', N'Parto', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9437872C-C874-4F0E-9424-9F9100FFBB92', N'Próteses', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6BCFEE14-665B-4900-BE3F-9F9100FFC4E7', N'Próteses e Ortóteses', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('906A8E1C-BA36-496A-8EEC-9F9100FFD6FC', N'Segunda Opinião Médica', '2F2BFF64-61AF-4CE3-8F74-9EE90119687C', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8C6DA2D0-D416-4AC3-BCF1-9F9100E6F9A3', N'(Cabeçalho)', '3236E457-D202-48FF-9219-9EE9011B1E39', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4A5E2C8E-7830-491C-99A8-9F9101004D0A', N'Responsabilidade Civil', '3236E457-D202-48FF-9219-9EE9011B1E39', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5E2F6DC1-5EE3-4B6A-8550-9F9101006355', N'Assistência', '3236E457-D202-48FF-9219-9EE9011B1E39', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8AA9BE17-566F-4A6B-8A79-9F910100554C', N'Danos Próprios', '3236E457-D202-48FF-9219-9EE9011B1E39', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('50272739-401E-4D2B-95F8-9F9101005B76', N'Ocupantes', '3236E457-D202-48FF-9219-9EE9011B1E39', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6026C770-8E47-4943-B4F1-9F9101006DDF', N'Outras Coberturas Complementares', '3236E457-D202-48FF-9219-9EE9011B1E39', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0704B12C-BF86-4784-A9C3-9F9100E6F9A3', N'(Cabeçalho)', '1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('569B9115-55D9-4153-96B0-9F910100934C', N'Despesas de Funeral', '1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BC6EDE9E-FD95-4D7C-9BC9-9F9101008AE1', N'Despesas de Tratamento', '1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7ACB286E-5C44-49AD-A1D8-9F91010082F4', N'Morte ou Invalidez Permanente', '1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E1C0FC56-9080-4232-A5AD-9F9101009B1E', N'Outras Coberturas', '1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('100A4884-1207-4364-95E6-9F9100E6F9A3', N'(Cabeçalho)', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('64683E51-0576-45CA-A911-9F910100AD60', N'Responsabilidade Civil', '54E688CE-8F22-4047-87F3-9EE9011B233A', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CB837DAA-D439-468D-9231-9F910100BE71', N'Assistência', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5A15DC72-34D7-4B94-AD15-9F910100B72F', N'Danos Próprios', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('24C363F2-7643-4CA1-A01D-9F910100DBB6', N'Despesas de Funeral', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B2FB9290-FA6E-430E-A154-9F910100D2BA', N'Despesas de Tratamento', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('93D65159-E2D6-47EA-B7B1-9F910100C87B', N'Morte ou Invalidez Permanente', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1286733B-7E4E-432D-9A1C-9F910100E354', N'Outras Coberturas', '54E688CE-8F22-4047-87F3-9EE9011B233A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E825E923-83DC-4C7E-88CC-9F9100E6F9A3', N'(Cabeçalho)', '5DE6C674-DBEA-4596-A241-9EE9011C14A9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6C9EA44A-A5A1-4C8D-B710-9F910100FC08', N'Reforma', '5DE6C674-DBEA-4596-A241-9EE9011C14A9', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1AA58D0B-2772-4961-B3D3-9F91010120E0', N'Outras Coberturas Opcionais', '5DE6C674-DBEA-4596-A241-9EE9011C14A9', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B5861614-E5FE-408E-869A-9F9100E6F9A3', N'(Cabeçalho)', 'F94D6C1A-6456-42A6-BDA9-9EE9011C0EF0', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9BFBC511-26F8-454D-879D-9F91010101A7', N'Reforma Velhice ou Invalidez', 'F94D6C1A-6456-42A6-BDA9-9EE9011C0EF0', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('65A99833-F491-4957-A9E8-9F910101303F', N'Outras Coberturas Opcionais', 'F94D6C1A-6456-42A6-BDA9-9EE9011C0EF0', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8E9D5E87-C3E3-4C9C-AEA3-9F9100E6F9A3', N'(Cabeçalho)', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C2F8E2E2-20A5-4B29-ACF7-9F91010152D0', N'Incêndio, Raio e Explosão', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6D283F48-FEC1-44DD-9426-9F9101016880', N'Fenómenos Sísmicos', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5BE422AE-163F-4F44-83DF-9F9101016007', N'Inundações', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EA4D2E55-9F49-4654-BA4A-9FE201057D81', N'Outras Coberturas', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('48106845-7034-4EA2-BAB0-9F9101015950', N'Tempestades', 'B3174C0F-DDF9-4466-A419-9EE90119ABDE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C45BAF71-0712-4B47-A457-9F9100E6F9A3', N'(Cabeçalho)', '995699D1-1674-4B8E-B813-9EE90119A4F7', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C5666246-E2DE-4F2D-96B4-9F910101841F', N'Incêndio, Raio e Explosão', '995699D1-1674-4B8E-B813-9EE90119A4F7', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2BF4EBE9-9E4D-491F-845E-9F91010192AA', N'Inundações', '995699D1-1674-4B8E-B813-9EE90119A4F7', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('73CAAA93-61D8-4BA2-AC45-9F9101019A01', N'Outras Coberturas', '995699D1-1674-4B8E-B813-9EE90119A4F7', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6B3382E2-C928-4011-99F2-9F9101018AB7', N'Tempestades', '995699D1-1674-4B8E-B813-9EE90119A4F7', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A3BC62B4-C5A2-40C6-B5A4-9FE201093075', N'(Cabeçalho)', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('61FE437C-992A-4357-8C48-9FE201098275', N'Acidentes de Aviação', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('20A5E739-7A8B-49FF-8B87-9FE2010974CC', N'Acidentes de Viação', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0E3B356A-AF71-454D-91CF-9FE2010A3FAF', N'Actos de Vandalismo', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CAE2C8D0-97C1-4CE1-863D-9FE201093F04', N'Cláusula A', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('44111A87-0404-48DE-8F08-9FE201094CB1', N'Cláusula B', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C7593807-9CCE-49BE-B470-9FE2010958FC', N'Cláusula C', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6DD23B73-CEED-4CBE-A7D0-9FE2010A61D1', N'Obras de Arte', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EC04292A-F6C3-4E92-B71D-9FE2010A5538', N'Operações de Carga e Descarga', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('55999917-B3D4-4006-8ADA-9FE2010A6F76', N'Outras Coberturas', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1343B7D6-65E8-454A-9EBE-9FE2010990FE', N'Postal', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('80A572F4-D5BE-446C-A906-9FE20109B047', N'Riscos de Frigorífico', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('38C029B4-D05B-436C-998A-9FE20109657D', N'Riscos de Guerra', 'FC9A126A-6ADB-4483-88CB-9FE2010916EE', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('ED96F780-2AC9-4047-A59C-9F9100E6F9A3', N'(Cabeçalho)', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EC6D3465-E377-4462-B74D-9F910101E5F8', N'Acidentes de Aviação', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('82A53FB6-D213-47A6-AC3D-9F910101DC6B', N'Acidentes de Viação', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('19E18CA1-8332-4C81-90A9-9F910101FDBE', N'Actos de Vandalismo', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A0B9FBF7-9B9B-49B6-AEB7-9F910101B82A', N'Cláusula A', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AB02C134-C9D6-4FE0-8B37-9F910101BF15', N'Cláusula B', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('51809B49-3B3E-4ADA-BD7E-9F910101C8BF', N'Cláusula C', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E3C27743-4593-4C18-B0C8-9F91010211E0', N'Obras de Arte', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F61E0663-EA37-4955-804D-9F9101020B27', N'Operações de Carga e Descarga', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CD6B3B44-8ABF-4660-B62B-9F9101021986', N'Outras Coberturas', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('308C5A38-248E-40A9-A016-9F910101EC81', N'Postal', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0BEF6EA9-BD6B-4D8A-AB2C-9F910101F5C8', N'Riscos de Frigorífico', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8AA3912F-B2BB-4EA9-A98C-9F910101D199', N'Riscos de Guerra', '8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('60D07084-0797-4730-9CA4-9F9100E6F9A3', N'(Cabeçalho)', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6462A436-1172-4447-BE24-9F9101025EB3', N'Acidentes de Aviação', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5EA0A432-4AB6-458A-B8D1-9F9101025702', N'Acidentes de Viação', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7DF17FDF-C0D0-4D57-8C97-9F910102AF26', N'Actos de Vandalismo', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B9497BE0-C547-4ED1-AC19-9F91010236BB', N'Cláusula A', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A5A5D7A9-28C8-44DE-8A35-9F9101023EE0', N'Cláusula B', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9B828DCF-B60D-43D9-B66B-9F910102464A', N'Cláusula C', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6A3F205D-EA01-4C95-B2AB-9F910102C1DA', N'Obras de Arte', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('40570B63-9C94-4B48-86B2-9F910102BA21', N'Operações de Carga e Descarga', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F6916AA1-1E79-4153-9C26-9F910102CA4A', N'Outras Coberturas', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6EB31573-AB41-433F-8B02-9F91010263CA', N'Postal', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('21DC6FE8-58B1-46B0-A3C5-9F9101027020', N'Riscos de Frigorífico', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C482BE89-A9D5-4F37-9BB3-9F9101024F41', N'Riscos de Guerra', '722EBA20-3136-4E3B-B3F8-9EE9011B059E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2D8FCBE6-2038-4EBA-ACE8-9FE2010BF60A', N'(Cabeçalho)', '6C51BDED-B9FC-4F1C-9C08-9FE2010BE557', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('30007AA1-7C88-4867-8649-9F9100E6F9A3', N'(Cabeçalho)', 'C4475D8B-72AF-415B-83FE-9F3401532F56', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F941F570-3DAA-49FD-A6D6-9F9100E6F9A3', N'(Cabeçalho)', '6725D59E-DBFD-46B4-9533-9EE90119E127', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0EB1BE38-CC71-4D6D-874B-9F910102FF24', N'Cobertura Base "All Risks"', '6725D59E-DBFD-46B4-9533-9EE90119E127', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8B5618D5-94D4-4336-BFDE-9F9101030A8B', N'Fenómenos Sísmicos', '6725D59E-DBFD-46B4-9533-9EE90119E127', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('37C4613B-9579-4628-8E91-9F9101031743', N'Outras Coberturas Complementares', '6725D59E-DBFD-46B4-9533-9EE90119E127', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3DFA5847-6E6A-423C-9795-9F9100E6F9A3', N'(Cabeçalho)', 'B9CF5874-AE6A-47D2-824C-9EE90119CB66', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F3B82F64-090B-4485-818F-9F9101032EDA', N'Cobertura Base', 'B9CF5874-AE6A-47D2-824C-9EE90119CB66', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('781DECC9-E1A3-4CAD-9ACF-9F9101033774', N'Fenómenos Sísmicos', 'B9CF5874-AE6A-47D2-824C-9EE90119CB66', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5FD94064-918E-4F3F-8DBF-9F91010343DC', N'Outras Coberturas Complementares', 'B9CF5874-AE6A-47D2-824C-9EE90119CB66', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', N'(Cabeçalho)', 'ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('49CAD742-A580-49F6-9696-9F9101037B55', N'Cobertura Base', 'ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E38303D3-3A41-48B1-B2DA-9F9101038573', N'Fenómenos Sísmicos', 'ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5D3583E9-DA6D-4500-8CAB-9F9101039206', N'Outras Coberturas Complementares', 'ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', N'(Cabeçalho)', '9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('EED10B99-11CE-4D01-945C-9F910103A103', N'Cobertura Base', '9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('63ACF500-8C1D-4C87-B5D8-9F910103AA46', N'Fenómenos Sísmicos', '9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E86D9819-234B-4185-B66B-9F910103B410', N'Outras Coberturas Complementares', '9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A166C109-CD59-4045-882A-9F9100E6F9A3', N'(Cabeçalho)', '9F79A559-E805-4238-8EC0-9EE90119D1A4', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A552FA54-1AE9-4605-8A30-9F910103C3FC', N'Cobertura Base', '9F79A559-E805-4238-8EC0-9EE90119D1A4', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0D76DECE-1F69-47DB-AAEF-9F910103CC21', N'Fenómenos Sísmicos', '9F79A559-E805-4238-8EC0-9EE90119D1A4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7BA2BA33-58AB-4413-843E-9F910103D5CF', N'Outras Coberturas Complementares', '9F79A559-E805-4238-8EC0-9EE90119D1A4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', N'(Cabeçalho)', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2E2E54BC-A3A9-4DF5-AC7B-9F910104152E', N'Danos à Obra', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('31C91A8B-C641-40B2-ABE3-9F9101041EDE', N'Bens Existentes ou Adjacentes', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A823B953-A929-4F0D-BFC8-9F9101044666', N'Instalações Temporárias de Estaleiro', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7032E7C5-7CC5-4643-9A8C-9FE20114D7E0', N'Manutenção Completa', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9576E644-AE83-486F-BA2B-9F9101045D7E', N'Manutenção Simples', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7BB40741-FEEC-4BD8-A46D-9F9101045521', N'Máquinas e Equipamentos Auxiliares', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('67CD7CE2-5A22-4487-889D-9FE20114E495', N'Outras Coberturas', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('594DF37A-D25B-47FE-BD52-9F91010435DA', N'Perdas Antecipadas de Exploração (ALOP)', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1B9F8A1B-913F-4AFF-B964-9F910104270C', N'Responsabilidade Civil', '6A2A5C28-6866-463C-9B6A-9F34015284C4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', N'(Cabeçalho)', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A3408C3C-77E4-4289-937E-9F91010475BD', N'Danos à Obra', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3113A7B6-6313-43A8-AD26-9F9101048166', N'Bens Existentes ou Adjacentes', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C498BED1-5EB3-4E1F-89BF-9F910104A780', N'Instalações Temporárias de Estaleiro', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3FF93A8A-BA70-421D-9B3D-9FE20116A9C5', N'Manutenção Completa', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('089B9C30-369D-44F0-8126-9F910104BA21', N'Manutenção Simples', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4CA702EE-402F-41B4-BD98-9F910104B2D4', N'Máquinas e Equipamentos Auxiliares', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C6268A9D-A4F3-4EC2-949B-9FE20116BAD6', N'Outras Coberturas', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('81AF6C0F-EAC3-4B2A-8C15-9F91010498E5', N'Perdas Antecipadas de Exploração (ALOP)', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('16FAB155-1E09-4132-87E7-9F9101048AC0', N'Responsabilidade Civil', 'D36F47AB-0D5A-425D-8FD4-9EE9011A6837', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('93182C13-5E8D-40E1-8F4F-9F9201582C09', N'(Cabeçalho)', '691EF827-EF46-4C49-A828-9F92015826D8', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2AE5588F-E63A-4E74-984B-9F9100E6F9A3', N'(Cabeçalho)', '1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D1F2EE46-AD11-44BB-B814-9F910104E3FB', N'Cobertura Base', '1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('76941449-7E58-4F97-9A67-9F910104ED47', N'Fenómenos Sísmicos', '1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('95502563-F604-49D6-89AE-9F910104F647', N'Outras Coberturas', '1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BEFAE2E7-09B2-4239-B772-9F9100E6F9A3', N'(Cabeçalho)', 'C77B46AF-B098-452D-914C-9EE9011A5198', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('411F6DEA-5067-4FF3-8F42-9F9101051595', N'Cobertura Base', 'C77B46AF-B098-452D-914C-9EE9011A5198', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E273F5DD-4914-4115-8929-9F91010526BA', N'Fenómenos Sísmicos', 'C77B46AF-B098-452D-914C-9EE9011A5198', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F50A82D6-014C-409A-BE12-9F9101052E9D', N'Outras Coberturas', 'C77B46AF-B098-452D-914C-9EE9011A5198', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CA95F6A8-888D-4C16-B1B8-9F9101051E45', N'Responsabilidade Civil', 'C77B46AF-B098-452D-914C-9EE9011A5198', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2A717F7F-6426-43E1-9A3D-9F9100E6F9A3', N'(Cabeçalho)', '445184E3-326B-4796-8618-9EE9011A5811', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('12E63265-0B08-4531-ACBF-9F9100E6F9A3', N'(Cabeçalho)', 'D5BEE08C-E0EA-4D25-9468-9F3401526786', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C0A422A0-B174-4AF4-B729-9F9100E6F9A3', N'(Cabeçalho)', '98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7B39B045-C090-407D-86D4-9F9101055426', N'Danos Próprios', '98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E44059D6-4D56-41F2-A9CA-9F910105693F', N'Outras Coberturas', '98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('CF7666F7-AC58-4FD2-BAE8-9F91010560DE', N'Responsabilidade Civil Geral', '98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FEA4DEC7-3816-40ED-94D6-9F9100E6F9A3', N'(Cabeçalho)', '252789C2-928E-467A-B081-9EE9011A2E76', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9B7E6C4B-4E9F-45C9-8A91-9F9100E6F9A3', N'(Cabeçalho)', '6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DC8FCF25-39D3-4E21-A087-9F9101057A9A', N'Cobertura Base', '6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1B5C3ADC-CB7B-440B-AC0D-9F9101058320', N'Fenómenos Sísmicos', '6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C4008DED-FA03-4218-8BA6-9F9101058A98', N'Outras Coberturas', '6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AB53B232-FE89-4C7B-AAB0-9F9100E6F9A3', N'(Cabeçalho)', '06CEF06D-73FF-4579-A8F4-9EE9011A08E8', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('16B4FCA7-EBB9-483F-8B00-9F9101059B56', N'Cobertura Base', '06CEF06D-73FF-4579-A8F4-9EE9011A08E8', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('26C79E77-7A58-4333-AD7B-9F910105A4E9', N'Fenómenos Sísmicos', '06CEF06D-73FF-4579-A8F4-9EE9011A08E8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('325A3DDC-F0D5-4D93-B2AF-9F910105ADFC', N'Outras Coberturas', '06CEF06D-73FF-4579-A8F4-9EE9011A08E8', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8D605CEF-5F7F-4295-A729-9F9100E6F9A3', N'(Cabeçalho)', 'DBFA4951-59CF-4008-98A5-9EE9011A0F30', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7A56A12F-7CB1-4411-BCE9-9F910105D27F', N'Cobertura Base', 'DBFA4951-59CF-4008-98A5-9EE9011A0F30', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0EB9BF0A-0590-4267-956B-9F910105DB98', N'Fenómenos Sísmicos', 'DBFA4951-59CF-4008-98A5-9EE9011A0F30', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DA4381A8-A7E5-4C4C-8603-9F910105E4BE', N'Outras Coberturas', 'DBFA4951-59CF-4008-98A5-9EE9011A0F30', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7A50BFD2-0415-4B53-80DD-9F9100E6F9A3', N'(Cabeçalho)', '44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A14BBF94-1D70-4BB6-B726-9F910105F463', N'Cobertura Base', '44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E9454C1B-8471-4F03-9850-9F910105FED2', N'Fenómenos Sísmicos', '44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('87DE3B22-6312-4710-AB5B-9F91010605F8', N'Outras Coberturas', '44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3D3ADCEF-3B68-4B90-8A62-9F9100E6F9A3', N'(Cabeçalho)', 'D38965BA-1275-4C36-BC89-9EE9011A3C6A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C5990A03-287E-4C0F-B5A5-9F9101061DAC', N'Cobertura Base', 'D38965BA-1275-4C36-BC89-9EE9011A3C6A', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7472432E-521A-4A67-89AF-9F9101062600', N'Fenómenos Sísmicos', 'D38965BA-1275-4C36-BC89-9EE9011A3C6A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('18B0E9A7-8E94-48E8-AB50-9F9101062D86', N'Outras Coberturas', 'D38965BA-1275-4C36-BC89-9EE9011A3C6A', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0D834A44-FA65-47F8-9F63-9F9100E6F9A3', N'(Cabeçalho)', '023C01EF-D4A7-441C-B241-9EE9011A2715', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2D04D698-852B-4B08-985C-9F9100E6F9A3', N'(Cabeçalho)', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7C3B8068-9A52-4CBF-9C47-9F9101066573', N'Danos à Água', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('131BB20A-A872-4BB7-9AD9-9F9101066CEF', N'Danos a Espécies', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('25B368ED-B06C-4394-ABBB-9F9101067564', N'Danos a Habitats', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DD23F5D9-1794-48E0-ACC6-9F9101065D8B', N'Danos ao Solo', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('21F351D4-FB3E-44C7-B763-9F9101067CE5', N'Danos ao Ar', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('11C289F7-01A6-4431-926D-9F91010685A0', N'Outras Coberturas', 'C199BF06-E397-454E-80ED-9EE9011AD3EC', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E50FD5B1-BC58-4A7C-AB76-9F9100E6F9A3', N'(Cabeçalho)', '4AA42557-96B9-4EE5-8B55-9EE9011AE763', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A19BDF86-4FF8-4BA0-A96A-9F920158BDAB', N'(Cabeçalho)', 'BFD2ABF7-195B-4560-BC91-9F920158B876', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7A7954FD-87C6-4457-A5FF-9F9100E6F9A3', N'(Cabeçalho)', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9C8C4A48-457D-490A-9743-9F910106BE15', N'Responsabilidade Civil', '5CA22680-438E-4C4E-8720-9EE9011AE247', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3E60ED38-BB1E-4653-8774-9F910106DE8E', N'Acidentes Pessoais', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('B9EC9B82-B9D1-49CF-8384-9F910106CCBD', N'Assistência', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('75A26D28-693A-475D-AF21-9F910106E574', N'Cães', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FA44DF03-A826-4CC8-A198-9F910106C56B', N'Danos em Armas', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3A2B6B56-3E2D-45C2-8158-9F910106ED2B', N'Outras Coberturas', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('37454DBD-9DFD-48CA-9D18-9F910106D662', N'Protecção Jurídica', '5CA22680-438E-4C4E-8720-9EE9011AE247', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AF9713B9-8932-4A15-A2A5-9F9100E6F9A3', N'(Cabeçalho)', '85EA331B-094E-4BFC-B423-9EE9011AD85E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', N'(Cabeçalho)', 'C6EC530E-8C28-4EDB-BCE3-9EE9011AC699', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3A3F581A-096A-4EAF-B8BD-9F9100E6F9A3', N'(Cabeçalho)', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('92D9E834-6A84-44A3-84FA-9F910108C41E', N'Responsabilidade Civil', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AC66AE95-7F18-4E7C-9311-9F910108D2F6', N'Animais', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('83AD45C6-A8D8-4CAC-91E5-9F910108CC98', N'Armas', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('63501542-FA18-4434-88E2-9F910108E065', N'Proprietário de Imóveis', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('0DA8AF01-DABB-4200-9524-9F910108ED25', N'Velocípede sem Motor', '363ED4D5-26D9-4EDF-8123-9EE9011AEB24', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D88F8444-DB16-4565-AC0A-9F9100E6F9A3', N'(Cabeçalho)', '533DA89B-1BEC-474B-B615-9EE9011ACF4F', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', N'(Cabeçalho)', 'DF4653EC-E2FE-4FCE-832A-9F340152C13B', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('6FEEBD34-C41F-4986-AF84-9F9100E6F9A3', N'(Cabeçalho)', '7CC94668-25FC-47E7-ADB4-9EE9011ACA6B', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('30FDEC09-25EB-441C-B284-9F9100E6F9A3', N'(Cabeçalho)', 'B5EE5408-8065-4F50-BEB1-9EE9011ADD5A', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C27E18BB-B859-48C6-9782-9F9100E6F9A3', N'(Cabeçalho)', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('1D2B27C7-BC48-48D8-8DA5-9F9101095F83', N'Responsabilidade Civil', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('ECA5B8FA-B688-42E7-9C89-9F91010974A1', N'Assistência', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('9F635AD8-FFC6-49F4-B706-9F9101096716', N'Danos Próprios', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5CC9A410-B2D4-431F-9A92-9F9101096D2E', N'Ocupantes', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4E6FFB93-64BD-4E38-B23B-9F9101098003', N'Outras Coberturas Complementares', 'A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('588362BA-A691-4FEF-A058-9F9100E6F9A3', N'(Cabeçalho)', '948E3387-F8F2-4E5C-8373-9EE9011B44D4', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('FCD2ED0A-81F7-4938-9B97-9F9101099571', N'Morte ou Invalidez Permanente', '948E3387-F8F2-4E5C-8373-9EE9011B44D4', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4EA16167-668B-4187-A843-9F910109A854', N'Despesas de Funeral', '948E3387-F8F2-4E5C-8373-9EE9011B44D4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('504579ED-FC0C-48EB-B092-9F9101099F04', N'Despesas de Tratamento', '948E3387-F8F2-4E5C-8373-9EE9011B44D4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('124C3E53-F61A-4350-9F7F-9F910109B066', N'Outras Coberturas', '948E3387-F8F2-4E5C-8373-9EE9011B44D4', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('179F8846-B6C1-4906-A559-9F9100E6F9A3', N'(Cabeçalho)', '97D0A05A-0DEA-4CA5-8492-9EE9011BD540', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2F18C57E-E660-4801-8858-9F910109C8F6', N'Morte', '97D0A05A-0DEA-4CA5-8492-9EE9011BD540', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3A4F9145-3CAF-4562-8F03-9F910109CE73', N'Vida', '97D0A05A-0DEA-4CA5-8492-9EE9011BD540', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AACCEAF3-0292-44D1-B734-9F9100E6F9A3', N'(Cabeçalho)', 'F66BB2D5-CAF3-40FF-ACAC-9EE9011BE0C9', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('5F85E073-686C-4A98-AC36-9F9100E6F9A3', N'(Cabeçalho)', '23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('4BDBFD72-2BA0-4819-BD44-9F910109EDD6', N'Renda Temporária', '23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('2D24571E-0188-44F1-8129-9F91010A0373', N'Renda Vitalícia Diferida', '23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('92F66EBE-F550-45F1-B0AE-9F910109F831', N'Renda Vitalícia Imediata', '23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7F4CB77E-62DB-470E-BE34-9F9100E6F9A3', N'(Cabeçalho)', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3EBB7E80-EE46-47FD-89FD-9F91010A1107', N'Morte', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F914811A-23C9-47FC-97D1-9F91010A7B31', N'Efeito Duplo', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F88BDA0F-2F89-4C34-A32C-9F91010A352C', N'Invalidez Absoluta e Definitiva', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('73C32AE7-8328-4FC2-B33F-9F91010A5415', N'Invalidez para Qualquer Actividade', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('86958CAA-7B5B-4341-AB08-9F91010A6210', N'Invalidez por Acidente', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('16620BB3-6F89-41BC-A52C-9F91010A715C', N'Invalidez por Acidente de Circulação', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DF9B1F30-C17C-4F70-AE00-9F91010A472E', N'Invalidez Profissional Total e Permanente', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A93BA557-50C7-4A19-914A-9F91010A87B5', N'Majoração do Capital por Filhos Menores', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('54387776-D07C-40A3-8C50-9F91010A1922', N'Morte por Acidente', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('397674BD-6E5E-43FE-958D-9F91010A258B', N'Morte por Acidente de Circulação', '5EC79210-F2BE-4775-9A5E-9EE9011BC36E', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('F4CDAE65-6D51-46E0-A908-9F9100E6F9A3', N'(Cabeçalho)', 'C304BE3B-7B80-412A-B277-9EE9011BFB56', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('771B59AB-B0F8-4895-A94B-9F91010CA1D5', N'Morte', 'C304BE3B-7B80-412A-B277-9EE9011BFB56', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('27EC40EB-7549-45B0-984C-9F91010CA802', N'Vida', 'C304BE3B-7B80-412A-B277-9EE9011BFB56', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('AB4D7468-A888-4987-8834-9F9100E6F9A3', N'(Cabeçalho)', '4FF96FBB-02FA-4864-9843-9EE9011BAA79', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7BCA85DF-8D1F-4677-A61B-9F910115DBE9', N'Morte', '4FF96FBB-02FA-4864-9843-9EE9011BAA79', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D579D992-18E5-4DCF-B546-9F910115E0D1', N'Vida', '4FF96FBB-02FA-4864-9843-9EE9011BAA79', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7D7361F6-C786-478F-A340-9F9100E6F9A3', N'(Cabeçalho)', '8B36A6D9-C639-4CB2-AB89-9EE9011BB42E', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C60C654D-39BA-4542-9E3F-9F9100E6F9A3', N'(Cabeçalho)', '50A579F9-8546-4923-90DE-9EE9011BA3EB', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D0DDEB2C-7728-4901-9751-9F910116009A', N'Renda Temporária', '50A579F9-8546-4923-90DE-9EE9011BA3EB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E608D1F5-390B-49F5-B57A-9F910116159D', N'Renda Vitalícia Diferida', '50A579F9-8546-4923-90DE-9EE9011BA3EB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C5F779A4-95C1-467A-AA34-9F9101160B20', N'Renda Vitalícia Imediata', '50A579F9-8546-4923-90DE-9EE9011BA3EB', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('D39D1528-B185-4AF8-B363-9F9100E6F9A3', N'(Cabeçalho)', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E5A4C02A-D819-49FA-BA4E-9F9101162420', N'Morte', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('C2A9B895-0CFA-4345-86B8-9F910116875C', N'Efeito Duplo', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('DCEA7856-7DC7-48A5-B8B8-9F9101164963', N'Invalidez Absoluta e Definitiva', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('7310F998-FD37-48E7-AF77-9F910116676B', N'Invalidez para Qualquer Actividade', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8E610386-4730-47DF-AFF6-9F910116713E', N'Invalidez por Acidente', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('382DC0E5-B7D2-4024-8633-9F9101167D82', N'Invalidez por Acidente de Circulação', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('E9CDAC3D-F5AF-4E66-881F-9F9101165ADD', N'Invalidez Profissional Total e Permanente', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('70D2184F-1142-4F1D-B69C-9F9101169665', N'Majoração de Capital por Filhos Menores', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('8E73DD94-434B-4532-9ED6-9F9101162BB9', N'Morte por Acidente', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('BB828E94-889C-49F9-BE90-9F91011639FF', N'Morte por Acidente de Circulação', 'DBBCFA61-888C-415A-9A16-9EE9011B9F52', 0, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('39CF4652-FFD3-499C-84B3-9F9100E6F9A3', N'(Cabeçalho)', 'F4333BC2-A0DB-4CD2-8FB2-9EE9011BBCAC', 0, 1);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('3CC97546-CE07-4EBD-B8D0-9F910116A883', N'Morte', 'F4333BC2-A0DB-4CD2-8FB2-9EE9011BBCAC', 1, 0);
insert into bigbang.tblBBCoverages (PK, CoverageName, FKSubLine, BMandatory, BHeader) values ('A1C35E78-17A0-434D-BA6D-9F910116AD1B', N'Vida', 'F4333BC2-A0DB-4CD2-8FB2-9EE9011BBCAC', 1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('555D1EE3-3970-4842-88DE-9F96014074DE', N'Área', 'D0198709-B571-4F50-B1C0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'ha', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E862CAB7-8784-4C1D-9F0C-9F9601404C21', N'Salário Homem', 'D0198709-B571-4F50-B1C0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E636ED38-8BC6-49F6-A009-9F9601405E96', N'Salário Mulher', 'D0198709-B571-4F50-B1C0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3AEE54D8-0996-44E1-91D3-9F960140BA63', N'Culturas Predominantes', 'D0198709-B571-4F50-B1C0-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9832F94C-34C3-4485-8EF0-9F960141441C', N'Cobertura Pretendida', 'ADFAF00A-5818-44FC-B261-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2A93FAF6-9785-4F8D-8208-9F96014108DC', N'Área Coberta Total', 'ADFAF00A-5818-44FC-B261-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'm2', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CB6E2669-8A15-4E90-B873-9F960140D7AE', N'Duração da Obra', 'ADFAF00A-5818-44FC-B261-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7DDC1DDC-BAB5-40FA-AC38-9F960140F4D9', N'Número de Pisos', 'ADFAF00A-5818-44FC-B261-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D1E6CE6C-D510-4B44-8372-9F9601415802', N'Salários por Categoria Profissional', 'ADFAF00A-5818-44FC-B261-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 1, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E9A3F14D-34D3-4228-BE13-9F960141A724', N'Número de Funcionários', '9078E11B-BBAD-4D2F-A1D7-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 1, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C24074CB-BDC2-45CC-BE49-9F9601419367', N'Taxa Comercial', '9078E11B-BBAD-4D2F-A1D7-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3D94F083-5852-4FFC-B949-9F9601417E42', N'Volume de Salários', '9078E11B-BBAD-4D2F-A1D7-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C0A3AF0C-D331-4A71-B6F7-9F9601469CCA', N'Período de Rendimentos', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9F5B33BF-E810-4E02-B538-9F960146E911', N'Outros Rendimentos', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('075E73F1-5B5F-44B6-B02C-9F960146CC1E', N'Subsídio de Alimentação', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2DCB0554-5B3A-42F8-93F2-9F9601470CC5', N'Taxa Comercial', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A6FF8A41-97D8-42D8-9A87-9F960146B2D5', N'Vencimento', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0D72CCD6-7B82-49B0-973A-9F960142F6E0', N'Actividade Empregador', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('ED2193DC-BE06-4C3A-A789-9F960142E94B', N'CAE Empregador', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, '14B89884-475A-4357-BB7D-9EE600C3CE6E', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('969E9157-A2C3-47AF-B430-9F9601430968', N'Profissão', 'ABD97644-09A1-482F-8356-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 1, 0, '088C8CCE-A1D1-4976-808F-9ECA0177AB56', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1CBF1E55-C86B-4065-A16D-9F960147A76D', N'Salário Anual', 'A3D2B92E-A446-481D-9C9F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4618A421-8105-4677-8D36-9F960147BC35', N'Taxa Comercial', 'A3D2B92E-A446-481D-9C9F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E5DB84BF-E580-4161-A010-9F960147856D', N'Actividade', 'A3D2B92E-A446-481D-9C9F-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6E025370-7924-4E61-89DB-9F9601477233', N'CAE', 'A3D2B92E-A446-481D-9C9F-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, '14B89884-475A-4357-BB7D-9EE600C3CE6E', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9A13CE88-1ED4-445E-A0DD-9F9601482417', N'Número de Funcionários', '39650737-B4BC-4ACA-8E70-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 1, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1875AE3F-0CF7-4C08-A839-9F9601480889', N'Taxa Comercial', '39650737-B4BC-4ACA-8E70-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('62324808-C9C8-4FC5-9A71-9F960147F026', N'Volume de Salários', '39650737-B4BC-4ACA-8E70-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BB29BE97-1772-41D7-B14C-9F960148B24B', N'Período de Rendimentos', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E5B7140F-A1DE-4895-8CD0-9F960149087B', N'Outros Rendimentos', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A93C43C9-2C91-4AAB-8339-9F960148F110', N'Subsídio de Alimentação', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('04CEEE97-6D16-403B-9959-9F9601491D19', N'Taxa Comercial', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F56E4CCA-4860-457B-8B10-9F960148D964', N'Vencimento', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('30B7992C-69C5-4C3F-BD01-9F9601485A32', N'Actividade Empregador', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('84B116AB-FF45-4386-9B60-9F960148489A', N'CAE Empregador', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, '14B89884-475A-4357-BB7D-9EE600C3CE6E', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2A7460F0-C67A-40F3-8351-9F9601486DD3', N'Profissão', 'A1CE9ACF-E258-408D-888C-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 1, 0, '088C8CCE-A1D1-4976-808F-9ECA0177AB56', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D9723120-478D-4088-8FB6-9FE200B92AF7', N'Função', 'DD77CBB5-1B90-4CA1-A5A5-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6DD51C81-C07B-41CC-8376-9FE200B9570A', N'Capital Total', 'DD77CBB5-1B90-4CA1-A5A5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('77620953-9AAD-4969-8C0E-9FE200B9A399', N'Prémio por Pessoa', 'DD77CBB5-1B90-4CA1-A5A5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C00C25DE-0127-467A-AD92-9F9601496529', N'Capital', 'D4AE818B-DE1E-41D6-988F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3C404EC8-40AE-40DC-9442-9FE200BB527C', N'Número de Pessoas Seguras', 'D4AE818B-DE1E-41D6-988F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('34EE5DDA-D824-4E4C-8485-9F9601497939', N'Prémio Comercial por Pessoa', 'D4AE818B-DE1E-41D6-988F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('92BB55A6-ECB1-485B-BA01-9FE200BDA884', N'Capital', '78C9F3B9-93D9-4A39-BBFE-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('39C969A0-2C32-4ED7-9EB2-9FE200BDDFE8', N'Número de Pessoas Seguras', '78C9F3B9-93D9-4A39-BBFE-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CB9A971D-0440-440D-BD21-9F9601499F7E', N'Prémio Comercial por Pessoa', '78C9F3B9-93D9-4A39-BBFE-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('06E3329B-A0D6-4A48-9140-9F96014A1518', N'Capital Total', '4A934E55-390C-4F36-A5E1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C67655ED-4A6B-4440-944E-9FE200BE5828', N'Número de Pessoas Seguras', '4A934E55-390C-4F36-A5E1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('725471B6-BAE2-4FFB-AF45-9F96014A2C14', N'Prémio Comercial por Pessoa', '4A934E55-390C-4F36-A5E1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A34B5D19-9AE7-4EBD-A43A-9F96014A005E', N'Actividade ou Ocupação', '4A934E55-390C-4F36-A5E1-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3145F718-A1CF-4BEE-8165-9FE200BEBF82', N'Modalidade', '83C9938E-156B-49A4-920F-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D89B2378-CC08-4C94-82A1-9F96014A9291', N'Apólice Automóvel', '83C9938E-156B-49A4-920F-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E2E4470E-826B-44E5-9D78-9FE200BF02C3', N'Modalidade', 'B2935000-09FA-41EB-BC23-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4B3ADBE0-922D-4BF3-802B-9F96014EADD3', N'Apólice Automóvel', 'B2935000-09FA-41EB-BC23-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('26F7DA80-E53E-456D-B4EE-9FE200BF9656', N'Âmbito Territorial', '772630B1-8EDA-4C38-A21C-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5D0AC8DE-79F5-456F-B77A-9F96014EF6C9', N'Riscos', '772630B1-8EDA-4C38-A21C-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('766D67E6-8785-48CA-AEA0-9FE200BFC011', N'Capital', '772630B1-8EDA-4C38-A21C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6B9B4E2A-07C2-400A-ADEC-9F96014EDF9B', N'Taxa Comercial', '772630B1-8EDA-4C38-A21C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FCDE1D8F-6C4B-4A3A-8C69-9F96014F8B37', N'Âmbito Territorial', 'C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2C3DAFAD-4CC8-4628-9D8F-9F96014F442D', N'Riscos', 'C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('54DBBC29-6DB2-44D9-9A24-9F96014F3407', N'Capital', 'C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A84141A1-80B5-49B4-B78F-9F96014F71F8', N'Actividade', 'C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5986DBFF-B011-4089-9140-9F96014F60E3', N'Profissão', 'C570ED92-0BAF-4BC0-9ED6-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 1, 0, '088C8CCE-A1D1-4976-808F-9ECA0177AB56', -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('45AE72E5-C635-4416-866E-9FE200C140E6', N'Prémio Comercial', '69D55B4D-C1EB-409B-A71F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3B54C37E-8F9E-4DD5-912C-9F96014FD246', N'Itinerário', '69D55B4D-C1EB-409B-A71F-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E7E8F5F9-AE09-4A6A-B0B3-9F96014FC1DE', N'Nome do Plano', '69D55B4D-C1EB-409B-A71F-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D884A326-6D84-4B6B-8FFA-9F960150194B', N'Itinerário', '98F93D87-397D-4932-B3B8-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9CE6202C-C0E6-43A6-85D7-9F96014FF941', N'Nome do Plano', '98F93D87-397D-4932-B3B8-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9EAF3658-57F5-4378-8676-9F9601503F66', N'Itinerário', '8B481840-216E-4A6F-BA44-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4321F690-54E5-48BB-A68B-9F960150322D', N'Nome do Plano', '8B481840-216E-4A6F-BA44-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C30EBAF7-AE20-4164-9E79-9F9601506FBC', N'Âmbito Territorial', '7D9388E0-3EEB-4540-B24B-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D53D8410-FF06-476F-BFBD-9FE200C2616E', N'Lotação', 'AA0226DF-F7E2-4267-A55A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F75BE58D-034C-4589-A4F5-9FE200C24672', N'Âmbito Territorial', 'AA0226DF-F7E2-4267-A55A-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5DB14FFB-05F9-4031-BAF1-9F9601520B11', N'Capital a Segurar', 'F7188DB5-563A-49B1-9278-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2264F87D-9D0F-4AF7-A1DD-9F960151E380', N'Produção a Segurar', 'F7188DB5-563A-49B1-9278-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'tons', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9C1AE892-88F1-4DC1-804E-9F960151F7E3', N'Valor por Quilo', 'F7188DB5-563A-49B1-9278-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('921EF97A-8A75-4C90-9A2F-9F960151CEBD', N'Culturas', 'F7188DB5-563A-49B1-9278-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E5F4D421-2428-4445-BE9C-9F9601525B35', N'Capital', 'A5F93E55-C47D-4D4A-AC44-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9AF09383-EF3B-4C57-A796-9F96015240B1', N'Espécies Seguras', 'A5F93E55-C47D-4D4A-AC44-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0ECEEDE3-1656-4E22-BDD1-9F960152833F', N'Tipo de Gado', '91718D5E-4550-4E92-B4AC-9F920156595F', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('467E49D7-8688-4369-966D-9F960152F115', N'Capital Total', '91718D5E-4550-4E92-B4AC-9F920156595F', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F6FB6FCE-938B-4EAB-8183-9F960152DD75', N'Número de Cabeças', '91718D5E-4550-4E92-B4AC-9F920156595F', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('81376483-4959-4437-8E3D-9FE200C36B61', N'Classe', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('25AD3681-98B9-4632-82E3-9F96015320AC', N'Cilindrada', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'cc', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9D62D86D-1DC8-4048-9B25-9F9601534A24', N'Peso Bruto', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'Kg', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C5D66137-FCCC-4EBA-B491-9F960153336F', N'Potência', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'cv', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E25B2B6C-E427-42F6-AEE7-9F960153722A', N'Prémio por Viatura', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3ED7869E-5F42-4C22-9D87-9F9601535F61', N'Valor da Viatura', 'B35DAFA8-0498-4E3C-87B0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F06B4F99-7365-4B14-80E9-9FE200C434D9', N'Classe', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E3E0F7E1-72D8-49FE-9CDB-9F960153F0C5', N'Condutor Habitual', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CBBD1B2C-C7AC-47E1-86F0-9F960153A25A', N'Cilindrada', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A89D4E2E-2D09-41A8-B0B1-9F960153C8CD', N'Peso Bruto', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'Kg', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E3CA60A3-89FB-4D1A-842E-9F960153B62F', N'Potência', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'cc', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A393AFDC-B124-4A11-BF10-9FE200C46F9C', N'Prémio por Viatura', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C22CEF50-DE06-4C4C-9D63-9F960153DEF5', N'Valor da Viatura', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A2193914-371F-4403-976F-9FE200C4524F', N'Nome do Plano', '4982E6DF-8BAA-458C-9119-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('92B46925-57A6-4B9C-AC49-9FE200C4BB70', N'Prémio por Pessoa', '3BACED07-2925-43FC-A826-9F920156C2D1', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('84181A15-48C8-48C6-8F9A-9F9601543227', N'Categorias', '3BACED07-2925-43FC-A826-9F920156C2D1', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5FDFB0DE-B358-490F-AACB-9F9601542417', N'Número da Carta de Condução', '3BACED07-2925-43FC-A826-9F920156C2D1', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('69F7F446-FB1A-4B09-A964-9FE200C4ED2B', N'Data da Carta', '3BACED07-2925-43FC-A826-9F920156C2D1', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EFC02135-3143-4FBA-A379-9F9601546AEE', N'Categorias', '4388DE20-3D55-47DA-ADA9-9F920156C862', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1FC2B293-A349-4A7E-9ED7-9F96015455F5', N'Número da Carta de Condução', '4388DE20-3D55-47DA-ADA9-9F920156C862', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D04E7D02-DFFA-4E79-94E4-9FE200C5B63F', N'Data da Carta', '4388DE20-3D55-47DA-ADA9-9F920156C862', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7D4087C5-2FA4-494A-99AE-9F96015518CB', N'Tipo de Caução', 'DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EB648FF6-DCB7-4DED-8DB6-9F960154A91A', N'Capital', 'DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5690C6FF-ED43-4485-AC84-9F96015497EF', N'Beneficiário', 'DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5A6E803B-FAE3-4505-AB95-9F960154BB66', N'Descrição', 'DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('85FAECC2-F488-497D-89FC-9F96015506F1', N'First Demand', 'DC5BEAA8-8E7D-4001-957B-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7ADB3D52-CCC6-4E60-95CE-9F960155F017', N'Tipo de Caução', '06197715-35E1-4BDC-9BC7-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2BB9ADF9-77E3-4AB4-AC3F-9F9601559BBE', N'Capital', '06197715-35E1-4BDC-9BC7-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A86A00EB-940D-4666-B4D8-9F96015549CA', N'Beneficiário', '06197715-35E1-4BDC-9BC7-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('34D3F54E-783F-4391-A3FA-9F960155AADD', N'Descrição', '06197715-35E1-4BDC-9BC7-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4828A7A7-1747-465F-9AE9-9F960155DDD6', N'First Demand', '06197715-35E1-4BDC-9BC7-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E0CD58EC-8F97-42FF-9E91-9FE200C78670', N'Condições de Pagamento', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C2BB3A61-DD7C-43E9-8531-9F9601561FFB', N'% Cobertura Mercado Externo', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FD978202-7DE8-4B4F-8711-9F9601560F85', N'% Cobertura Mercado Interno', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('97D94A86-AE6F-4B2A-8AF4-9FE200C80E4A', N'Franquia Individual', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('01DFC9E4-78E7-48D3-8643-9FE200C883FA', N'Limite de Crédito Máximo', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7D4EEBF5-EDBC-4756-90BE-9FE200C83851', N'Limite Máximo Indemnização', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('72B223BB-34B9-4E3C-BD2C-9FE200C8C8EC', N'Limite Mínimo Indemnização', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8544DB5B-0488-488C-90BA-9FE200C8A269', N'Percentagem de Garantia', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('742FE0CF-8C53-40C2-B7EB-9FE200C7E777', N'Taxa de Prémio', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('30D7654E-7D46-4522-96BE-9FE200C7C507', N'Vendas Seguradas', 'A7815292-7D9C-4751-83F0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BF80A9FC-AD3F-4630-8DD3-9F9601565146', N'Custo Unitário', '52CFE26F-42BC-4D4F-957C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('64883643-ED00-498E-82D4-9F960156930B', N'Data de Venda', '52CFE26F-42BC-4D4F-957C-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C43A6CE0-D5E5-4C4D-BEDA-9F960156BB65', N'Capital', '444FE5C1-8A7F-4FAB-AFDD-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('55590901-142F-4A77-BFB4-9F960156D3EE', N'Âmbito Territorial', '444FE5C1-8A7F-4FAB-AFDD-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1E6CA07C-70A2-4D44-B53D-9F960156F30D', N'Actividade', 'E849CCC0-FB25-4872-B600-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6A50F969-A705-4333-B2F0-9FE200C97061', N'Agregado Familiar', 'FDB4E489-8433-4163-B770-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('959E987D-1B4F-4BDE-A792-9F9601573360', N'Âmbito Territorial', 'FDB4E489-8433-4163-B770-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('47805477-2AA0-4682-B083-9F9601572380', N'Animais', 'FDB4E489-8433-4163-B770-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('26BD8B84-7B72-4834-8B53-9F96015716BD', N'Viaturas', 'FDB4E489-8433-4163-B770-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2A4C2D81-ED02-48AD-B6DD-9FE200CA630F', N'Grau de Parentesco', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EBBFEF55-3334-4A99-AE4E-9FE200CA7D72', N'NIB', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('030B85CD-71C5-4891-9113-9FE200CACE3C', N'Número de Cônjuges', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('48BD5524-3DB0-4F82-8BEA-9FE200CD6620', N'Número de Filhos Maiores', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('17B5E1DA-D77A-43A5-AB12-9FE200CD8792', N'Número de Filhos Menores', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CD45F751-7A25-4BA9-99B4-9FE200CAAD4B', N'Número de Titulares', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F686E0E4-1517-41BB-97D7-9FE200CDB525', N'Percentagem Comparticipação', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('20CD0AC8-9FFA-4928-852B-9F9601577C06', N'Prémio Comercial Cônjuge', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CF516E0B-C36F-476C-BFCC-9FE200CA297D', N'Prémio Comercial Filho Maior', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('591DCF99-34E8-4211-A17C-9F960157A4F7', N'Prémio Comercial Filho Menor', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D56812DE-E5F5-4C20-B4B5-9F9601575FB8', N'Prémio Comercial Titular', '389ECD30-2DA0-4743-8DDB-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('AD2BDC4A-FB76-4531-B6C7-9FE200EA53DF', N'Grau de Parentesco', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8118E542-ECB6-4FB0-BC0C-9FE200EC19FF', N'NIB', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('17798432-8AF2-4135-B792-9FE200F23697', N'Número de Cônjuges', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F3C6A12C-1EDC-4D73-BF1D-9FE200F25296', N'Número de Filhos Maiores', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B38F365D-0828-4E56-9471-9FE200F5443B', N'Número de Filhos Menores', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7B3DF1B7-5162-4DF1-BDCE-9FE200EDF0B0', N'Número de Titulares', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5ECAC9CB-5A5D-4651-B9A1-9FE200F578C8', N'Percentagem Comparticipação', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('88F74A16-1A9C-4B63-B635-9F960157E5FD', N'Prémio Comercial Cônjuge', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('35A7C9CA-E949-4297-B5AB-9FE200EA2C8A', N'Prémio Comercial Filho Maior', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C1C6B0C1-4120-41D5-8F32-9F960157FC5B', N'Prémio Comercial Filho Menor', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6CCA6707-6835-48F2-B000-9F960157C8AF', N'Prémio Comercial Titular', '3B337F28-D4C0-44BE-B44D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0DFB3CA1-DC45-419E-B3DE-9FE200F5DBDD', N'Grau de Parentesco', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('65B0A61E-50EA-457C-85B3-9FE200F5FE04', N'NIB', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1D303F55-13AB-4371-8E97-9FE200F66993', N'Número de Cônjuges', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('65E94A73-99A9-41BC-A32F-9FE200F68633', N'Número de Filhos Maiores', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EC8F7D75-DE0E-466F-9F29-9FE200F69FC6', N'Número de Filhos Menores', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A1A36173-CF8E-4084-B0E2-9FE200F64CB1', N'Número de Titulares', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5C7861EC-55B1-46DD-9157-9FE200F6B9FB', N'Percentagem Comparticipação', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C7270847-253B-4168-94B0-9F9601583E07', N'Prémio Comercial Cônjuge', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A2DD65FD-BBBD-464B-A025-9FE200F5BC71', N'Prémio Comercial Filho Maior', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('933F52F3-B5E6-423E-BA9B-9F9601585865', N'Prémio Comercial Filho Menor', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5F1CD0AD-D797-48B3-9F44-9F9601582245', N'Prémio Comercial Titular', 'FF0E80FD-9604-4EAD-81B1-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2F143086-7F99-4C04-B124-9FE200F7625B', N'Grau de Parentesco', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C27777E1-D2C5-4AEE-A324-9FE200F777DD', N'NIB', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4F2BAAD0-2CE1-4A2E-86BE-9FE200F7A8BA', N'Número de Cônjuges', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3A71CEAF-2575-42ED-9E66-9FE200F7C208', N'Número de Filhos Maiores', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9A57A2B6-274F-49E2-8F66-9FE200F7DA26', N'Número de Filhos Menores', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6C3C83BB-F58A-4A13-ABE7-9FE200F78F2A', N'Número de Titulares', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1900305E-D7FC-47D5-820F-9FE200F7FAC3', N'Percentagem Comparticipação', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DDFE3B7D-63C1-49BF-B9EE-9F960158B2D7', N'Prémio Comercial Cônjuge', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E8BE9944-565F-4643-8AE8-9FE200F744BA', N'Prémio Comercial Filho Maior', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('848CD03A-86FA-4B18-B435-9F960158C848', N'Prémio Comercial Filho Menor', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6C6DB25D-D65B-4E24-8022-9F96015895E7', N'Prémio Comercial Titular', '9745381A-347E-4C0E-89C4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('114DFA0B-6CE2-447E-8357-9FE200F8CE83', N'Grau de Parentesco', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A42FB9D5-6D48-4F39-B57E-9FE200F8E6E3', N'NIB', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('71C0CF9C-708B-424D-A783-9FE200F91EA5', N'Número de Cônjuges', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('13B596A7-2D07-46E4-A3F0-9FE200F940C2', N'Número de Filhos Maiores', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('379E59B2-171A-4E3E-A091-9FE200F95919', N'Número de Filhos Menores', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A9DB3E35-014B-4227-AF33-9FE200F9008F', N'Número de Titulares', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E753D357-BBBD-4200-97E9-9FE200F96FC3', N'Percentagem Comparticipação', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B06A6DFC-2CE4-47B5-9264-9F9601591A37', N'Prémio Comercial Cônjuge', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4375B767-61AE-43AA-8785-9FE200F87A80', N'Prémio Comercial Filho Maior', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C6C4542E-553A-42F8-B197-9F960159429C', N'Prémio Comercial Filho Menor', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('85438D80-76EA-464F-99A6-9F960158FA52', N'Prémio Comercial Titular', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3D68A53A-FF8F-49E5-8189-9FE200F8B4A6', N'Número de Certificado', 'A60CFB40-CBD4-46B1-9C89-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DB9548CE-2426-4499-A8E5-9FE200FA4EE4', N'Grau de Parentesco', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0FA5CD4E-82F8-4869-ABFE-9FE200FA654D', N'NIB', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('67A4D15B-F546-4798-ADE2-9FE200FAC23F', N'Número de Cônjuges', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1BF676CB-6E3C-4F6A-A9D3-9FE200FADAA4', N'Número de Filhos Maiores', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D0FA31BC-E7A3-4305-A62B-9FE200FAEFDD', N'Número de Filhos Menores', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5395D1AD-12DD-429D-9682-9FE200FAA46B', N'Número de Titulares', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F1CEA9C0-05BF-49D6-A0B2-9FE200FB0683', N'Percentagem Comparticipação', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('24C80107-3AFC-48C6-AAAB-9F9601598DB6', N'Prémio Comercial Cônjuge', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('47398504-BB20-4568-A09E-9FE200F9FF66', N'Prémio Comercial Filho Maior', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C18B1ACC-DAAC-4E2B-995F-9F960159A0F4', N'Prémio Comercial Filho Menor', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('594966F0-36A6-474E-9E80-9F96015976EA', N'Prémio Comercial Titular', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C30A549B-A218-4C4B-9033-9FE200FA29C0', N'Número de Certificado', 'BE826D3A-9929-4CFE-80A3-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C68B1A09-BC3C-4749-ABD2-9FE200FBB3E2', N'Grau de Parentesco', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('93451425-8607-48D3-95F7-9FE200FBD6EF', N'NIB', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3CC7BB11-9429-4C52-84C5-9FE200FC0C2A', N'Número de Cônjuges', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3DFF9B4B-1AB7-41CC-BD67-9FE200FC28EE', N'Número de Filhos Maiores', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DD8ECF3C-56DC-4270-9853-9FE200FC8BE3', N'Número de Filhos Menores', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A5E004F1-3D6E-4E7A-BFBA-9FE200FBF2FC', N'Número de Titulares', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A4CDB098-14D4-47AF-8841-9FE200FCA0D1', N'Percentagem Comparticipação', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6F7A09B2-BF05-4D40-966C-9F960159CFB7', N'Prémio Comercial Cônjuges', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F71B1C80-8E43-4985-8595-9FE200FB6CFC', N'Prémio Comercial Filho Maior', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D5DFE8CC-AC01-4E26-A82E-9F960159E195', N'Prémio Comercial Filho Menor', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0B60D535-1861-420F-9946-9F960159B739', N'Prémio Comercial Titular', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5050BC72-5E5E-48BD-B9C9-9FE200FB9508', N'Número de Certificado', '177F8FDC-F6C8-4AB8-BC90-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('725FD92F-5498-42AE-958D-9F96015A6527', N'Grau de Parentesco', 'EBF88313-E4D2-4647-A133-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9506C4E0-8621-4FBB-A7AF-9F96015AF38C', N'NIB', 'EBF88313-E4D2-4647-A133-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EDCEBF51-000E-401E-B41E-9F96015A4FEB', N'Percentagem de Comparticipação', 'EBF88313-E4D2-4647-A133-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EC2A5ECB-8C9E-4AEC-82FD-9F96015A2A0D', N'Nome do Plano', 'EBF88313-E4D2-4647-A133-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('81F3906F-951F-4AD0-96AB-9F96015B882D', N'Grau de Parentesco', 'CA9799E4-836E-4EA2-99DA-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BDC2BA17-FC15-45CB-BEA4-9F96015B95ED', N'NIB', 'CA9799E4-836E-4EA2-99DA-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2BB63C16-1812-4134-9A26-9F96015B64F8', N'Percentagem de Comparticipação', 'CA9799E4-836E-4EA2-99DA-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0427F6EA-B3FA-4A6A-9B03-9F96015B3BC5', N'Nome do Plano', 'CA9799E4-836E-4EA2-99DA-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8E625AF1-4926-4065-A2FC-9F96015C07DD', N'Grau de Parentesco', '6453F5B5-0295-4008-8931-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3EC6B93E-98A4-41DE-9183-9F96015C17D9', N'NIB', '6453F5B5-0295-4008-8931-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('43353415-CC40-4152-9F0B-9F96015BF519', N'Percentagem de Comparticipação', '6453F5B5-0295-4008-8931-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F0E381BA-C39C-4F83-9414-9F96015BCDF8', N'Nome do Plano', '6453F5B5-0295-4008-8931-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BC3E0D27-5416-45ED-AA7D-9F96015C41D0', N'Tipo de Embarcação', '8C6DA2D0-D416-4AC3-BCF1-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('306840CE-8F79-47CB-B289-9F96015C5B7A', N'Área de Navegação', '8C6DA2D0-D416-4AC3-BCF1-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('01759ABD-BEA9-4BF8-B9D8-9FE20101D60C', N'Tipo de Embarcação', '0704B12C-BF86-4784-A9C3-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4A3AA414-6002-403B-9C3E-9FE20101F33F', N'Lotação', '0704B12C-BF86-4784-A9C3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('906D3E5A-E519-4DA3-BF73-9F96015CB422', N'Área de Navegação', '0704B12C-BF86-4784-A9C3-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7682B27F-ABD7-451C-AF2B-9FE20102401D', N'Tipo de Embarcação', '100A4884-1207-4364-95E6-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8A7452E9-D43A-4630-8513-9FE201025B5F', N'Lotação', '100A4884-1207-4364-95E6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0A8D1332-2808-4A0A-9D1E-9F96015CEAAD', N'Área de Navegação', '100A4884-1207-4364-95E6-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0F17281A-BA8F-4A8B-94B1-9F96015D4EAA', N'Tipo de Valor', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D5E81DFB-142B-4C6F-A10D-9F96015D376E', N'Variante', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7D8170D9-45AA-468B-8D33-9FE201039E21', N'Idade do Vencimento da Adesão', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8958A53A-50FC-4C1A-9623-9F96015D9543', N'Percentagem Contribuição', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('49CA1A7D-CE48-4456-BB67-9F96015D82E2', N'Fundo Contributivo', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('371311FE-986D-478F-8E62-9F96015D23C2', N'Data de Vencimento da Adesão', 'E825E923-83DC-4C7E-88CC-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('03B34332-338D-4A3D-B1C3-9F96015E0540', N'Tipo de Valor', 'B5861614-E5FE-408E-869A-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CB48875D-ECAA-4C7B-8909-9F96015DF4D6', N'Variante', 'B5861614-E5FE-408E-869A-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('44F77682-1AE5-4E10-BE63-9F96015DE2A4', N'Data de Vencimento da Adesão', 'B5861614-E5FE-408E-869A-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6525BD79-5BF0-42D0-8719-9F96015E2699', N'Ano de Construção', '8E9D5E87-C3E3-4C9C-AEA3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C2E79A22-5E7E-47A2-AC13-9FE201064022', N'Capital Total', '8E9D5E87-C3E3-4C9C-AEA3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DE5D6DFE-B31A-4FBB-9681-9F96015E5AE9', N'Capital Total Actual', '8E9D5E87-C3E3-4C9C-AEA3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1842AED2-63A4-4B38-AC94-9FE2010680E2', N'Percentagem de Actualização', '8E9D5E87-C3E3-4C9C-AEA3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B2B2A3F5-7C52-4FAF-8128-9F96015F37E0', N'Indexação', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('957BBB5C-F70B-4338-90A7-9F96015F291C', N'Ano de Construção', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C21E1D54-4FB0-4C82-A757-9FE20107AF0A', N'Capital Total', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('62CF16A7-DB7D-413F-9B90-9F96015F5E71', N'Capital Total Actual', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('288422C6-F651-4AC7-AB91-9FE20107DA17', N'Índice Inicial', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('814F6172-6C14-4CBA-AD6B-9FE20107FB20', N'Percentagem de Actualização', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7344D9F9-25D3-49A9-B9A2-9F96015F4A65', N'Valor do Índice', 'C45BAF71-0712-4B47-A457-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D03482CF-C295-451F-8EC5-9FE2010ACF82', N'Capital', 'A3BC62B4-C5A2-40C6-B5A4-9FE201093075', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DCF93229-5CB5-463C-AE32-9FE2010AF6C3', N'Taxa', 'A3BC62B4-C5A2-40C6-B5A4-9FE201093075', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3A3F1614-5D6B-4119-BA91-9FE2010AB5AB', N'Âmbito Territorial', 'A3BC62B4-C5A2-40C6-B5A4-9FE201093075', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6DFC59E0-578D-49B4-853B-9FE2010B1F86', N'Descrição da Carga', 'A3BC62B4-C5A2-40C6-B5A4-9FE201093075', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3D91F8D5-9F5C-4006-8CDA-9F96015FCD65', N'Limite Máximo por Transporte', 'ED96F780-2AC9-4047-A59C-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B755455B-46ED-4C50-A708-9F96015F89BA', N'Âmbito Territorial', 'ED96F780-2AC9-4047-A59C-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BB3FD661-EE4C-4F46-A7E8-9F96015F9F0F', N'Meios de Transporte', 'ED96F780-2AC9-4047-A59C-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B0E5B073-E3CF-4F93-912E-9F96015FAC96', N'Taxas', 'ED96F780-2AC9-4047-A59C-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4FE90017-6764-4439-8A20-9F96016030C7', N'Capital', '60D07084-0797-4730-9CA4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B3155091-0219-4DC9-9AFF-9F9601601DF9', N'Taxa', '60D07084-0797-4730-9CA4-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C388B6C5-2FE3-4EEA-A129-9F9601600EDF', N'Local de Destino', '60D07084-0797-4730-9CA4-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('238FDA05-A250-4784-B46C-9F96015FFF90', N'Local de Origem', '60D07084-0797-4730-9CA4-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A3ABB1C3-B222-47C6-A25B-9F96016042F6', N'Meio de Transporte', '60D07084-0797-4730-9CA4-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C9C5BE56-2199-4E91-9AFB-9FE2010C61AD', N'Capital Máximo por Transporte', '2D8FCBE6-2038-4EBA-ACE8-9FE2010BF60A', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('96D0D08A-18E6-4A55-AD79-9F96016074D1', N'Capital Máximo por Transporte', '30007AA1-7C88-4867-8649-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4E99522D-9702-4ED6-AEB3-9F9601609C70', N'Ano de Construção', 'F941F570-3DAA-49FD-A6D6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A69C2C27-58C1-4F11-A6E6-9F960160C312', N'Capital Total', 'F941F570-3DAA-49FD-A6D6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6B62D489-4343-43B0-80B8-9F960160AF0F', N'Capital Total Actual', 'F941F570-3DAA-49FD-A6D6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6765F19E-9E1E-4443-8B43-9F960160D586', N'Percentagem de Actualização', 'F941F570-3DAA-49FD-A6D6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9B5A371B-4BBF-4E98-9664-9FE2010FB89D', N'Indexação', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('39EFE8C9-0CD1-4E80-B4D6-9FE2010F8841', N'Ano de Construção', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('13B8F0A5-8619-4DD2-A2C7-9FE201103085', N'Capital Total', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5DEF6A6E-8C51-4568-99DC-9FE201101663', N'Capital Total Actual', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5E8FA292-473D-40FD-B119-9FE201105095', N'Índice Inicial', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9C7879C3-B603-47E6-809C-9FE201107018', N'Percentagem de Actualização', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9DFDFE3A-B0FD-4D2C-A0B6-9FE2010FDD54', N'Valor do Índice', '3DFA5847-6E6A-423C-9795-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9383149C-C42C-400A-999B-9F9601617804', N'Indexação', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0E65601B-FE7B-4038-8AE6-9F960161A0C2', N'Ano de Construção', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B08019DC-9691-47AF-BA6B-9FE20111D9BA', N'Capital Total', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1E5BB5FA-AB00-4F26-BD0E-9FE20111C4B7', N'Capital Total Actual', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A2314B47-84AC-40A7-A8B0-9FE20111FA81', N'Índice Inicial', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DBA5E54B-8E8A-4CF2-A54B-9FE201122962', N'Percentagem de Actualização', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4455401C-2953-4189-99B4-9F9601618EB3', N'Valor do Índice', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9E3DEA8E-C55E-4165-9499-9F9601615AFB', N'Parcial', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E012B76E-A81F-4887-A5A4-9F960161688A', N'Partes Comuns', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('93E1CD06-59E2-4565-BE5F-9F9601614B90', N'Totalidade', 'C8EBA718-DA8D-46A6-9F3A-9F9100E6F9A3', '361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F6E729DC-08CB-47B5-A95B-9F960161E621', N'Indexação', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0BFE5FCE-3201-408A-B734-9F960161BA39', N'Ano de Construção', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FBC0FA1E-E984-4C82-9E0B-9FE201129A95', N'Capital Total', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('49279F1F-2631-4FF2-837A-9F9601620E93', N'Capital Total Actual', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('12230A34-4E28-4259-8BF6-9FE20112CDDF', N'Índice Inicial', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7AD47076-E401-44CD-8B11-9FE201131578', N'Percentagem de Actualização', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F34690AF-0C6F-4891-ABA4-9F960161FA1B', N'Valor do Índice', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2EA0A9AD-27BC-40F5-96D8-9FE201133A9B', N'Nome do Produto', '1CBAE773-DF83-4787-8F2F-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0B11FF1D-D93A-4B90-A895-9F9601622A96', N'Ano de Construção', 'A166C109-CD59-4045-882A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('665BE758-425D-4CF2-A669-9F9601625522', N'Capital Total', 'A166C109-CD59-4045-882A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4A8DF296-1F08-49BD-A4AC-9F9601624111', N'Capital Total Actual', 'A166C109-CD59-4045-882A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F6E65C21-15F1-4CB7-ABB8-9F96016265DA', N'Percentagem de Actualização', 'A166C109-CD59-4045-882A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8DE66B63-6F3F-47CA-A09F-9FE20115576B', N'Tipo de Empreitada', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3BD09F37-E57A-4A25-A09D-9F960162F9A5', N'Período Constr. Máximo / Empreitada', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F391DA77-E785-4DFD-9903-9FE201165E06', N'Período de Comissionamento', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('039C0174-ACCE-4A2C-87B9-9FE20115BBFA', N'Período de Construção', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5D0891E3-1262-4C88-8D94-9FE201164055', N'Período de Ensaio', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F6C40420-99FF-4C28-9BC0-9F9601632FD7', N'Período de Manutenção', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'meses', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A93279D9-1638-4C50-8342-9F960162B6FB', N'Valor Máximo por Empreitada', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0428F93F-F41A-47D3-B132-9FE20115398C', N'Pasta de Arquivo', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('37B8656D-95F8-4A67-A93D-9F96016292EE', N'Tipologias de Empreitada Excluídas', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('59744146-CAFF-43C4-9655-9FE201159439', N'Apólice RC Excesso', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 1, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1C0810ED-1373-4FDE-A739-9FE20115ED09', N'Início do Período de Construção', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BF8FE403-115D-47A3-9605-9FE2011609EE', N'Termo do Período de Construção', '79C9CC5D-D0E0-4EE1-9203-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('35446D4E-6B46-457B-917B-9F96016406A9', N'Tipo de Empreitada', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B4E9220B-0FDF-4815-AB32-9F96016503E1', N'Período de Comissionamento', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EADF0C7B-800D-4377-8A65-9F9601648906', N'Período de Construção', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EAF7B09E-4E3A-43C1-9DF3-9F960164D88A', N'Período de Ensaio', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'dias', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2EEB4A71-C0E2-4C10-97A3-9F960163F46D', N'Pasta de Arquivo', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('095CE487-5B9F-4644-B259-9F9601642797', N'Apólice RC Excesso', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0BABC8B1-2BCE-4986-9A84-9F960164A630', N'Início do Período de Construção', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1CF2B1BE-72DE-42E8-9A69-9F960164BD1C', N'Termo do Período de Construção', 'D7F31DF5-CE4F-4A7E-A708-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 0, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C0B506F5-F916-4EEE-9D5C-9FE20117DE80', N'Capital Excesso', '93182C13-5E8D-40E1-8F4F-9F9201582C09', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A091A524-A17D-4DF2-807F-9F96016A1311', N'Limite Indemnização por Sinistro', '93182C13-5E8D-40E1-8F4F-9F9201582C09', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('432A3622-E2E9-4A0B-828B-9F960168D415', N'Apólice Obras e Montagens', '93182C13-5E8D-40E1-8F4F-9F9201582C09', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5F586F50-5375-489F-BF28-9F96016BB39E', N'Taxa Comercial', '2AE5588F-E63A-4E74-984B-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6A9DFC80-25C7-4817-BA19-9F96016B9B3F', N'Âmbito Territorial', '2AE5588F-E63A-4E74-984B-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('98DF6662-0E60-455D-9140-9F96016B8889', N'Credor', '2AE5588F-E63A-4E74-984B-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('23CDCEEA-0F32-45A7-872F-9F96016C3548', N'Taxa Comercial', 'BEFAE2E7-09B2-4239-B772-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4AB6A2BA-C4DF-4C07-97C5-9F96016C200D', N'Âmbito Territorial', 'BEFAE2E7-09B2-4239-B772-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C481A43C-11B1-4A86-96E1-9F96016C100A', N'Credor', 'BEFAE2E7-09B2-4239-B772-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6287FBC3-F99C-43D6-A043-9F96016C7846', N'Capital', '2A717F7F-6426-43E1-9A3D-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9A4CE2DD-9E1D-4116-AB46-9F96016C683C', N'Tipo de Bens', '2A717F7F-6426-43E1-9A3D-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('465BF75E-3C28-475B-BFE2-9F96016CA8A1', N'Capital', '12E63265-0B08-4531-ACBF-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7880E598-7AB5-4323-BA37-9F96016C97F8', N'Tipo de Bens', '12E63265-0B08-4531-ACBF-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B987F99B-060F-45C0-AEA9-9F96016CE4AF', N'Capital', 'FEA4DEC7-3816-40ED-94D6-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('931EC476-FAC9-4B65-BB3F-9F96016D69E6', N'Taxa Comercial', '9B7E6C4B-4E9F-45C9-8A91-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'%', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('865C5326-BF12-4427-A65D-9F96016D579C', N'Âmbito Territorial', '9B7E6C4B-4E9F-45C9-8A91-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5FC757E4-CD4A-4317-871D-9F96016D465D', N'Credor', '9B7E6C4B-4E9F-45C9-8A91-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6FE7F4E5-B57D-4854-AACF-9F96016DC65D', N'Tipo de Capital', 'AB53B232-FE89-4C7B-AAB0-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A500821D-45B3-4ABF-A5B5-9F96016E06AA', N'Período de Indemnização', 'AB53B232-FE89-4C7B-AAB0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'meses', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6E559021-ACC8-406F-91A8-9FE2011A1164', N'Taxa', 'AB53B232-FE89-4C7B-AAB0-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('95A21CEA-2E1A-4B2B-9043-9F96016DE7BD', N'Apólice de Incêndio', 'AB53B232-FE89-4C7B-AAB0-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2DFD5A5D-13D0-400E-B09A-9F96016E43FB', N'Tipo de Capital', '8D605CEF-5F7F-4295-A729-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('00BBF116-B9C6-4560-A1B6-9F96016E6D78', N'Período de Indemnização', '8D605CEF-5F7F-4295-A729-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'meses', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8BA633B1-58FE-4891-99FB-9FE2011A93E5', N'Taxa', '8D605CEF-5F7F-4295-A729-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7300A7FF-BD1F-4EB6-B54A-9F96016E56F3', N'Apólice de Avaria de Máquinas', '8D605CEF-5F7F-4295-A729-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('59FB662D-65B1-4FDF-A8C3-9F96016EA283', N'Tipo de Capital', '7A50BFD2-0415-4B53-80DD-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1A30D112-DC55-412C-9179-9F96016EDA8E', N'Período de Indemnização', '7A50BFD2-0415-4B53-80DD-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'meses', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B1447D4D-C8F0-4D1B-AAFB-9FE2011B1488', N'Taxa', '7A50BFD2-0415-4B53-80DD-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('587587FF-5775-401F-9336-9F96016EC1C4', N'Apólice de Multi-Riscos', '7A50BFD2-0415-4B53-80DD-9F9100E6F9A3', '9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, NULL, 0, 0, 'D0C5AE6B-D340-4171-B7A3-9F81011F5D42', -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C28138CB-D03B-48E4-8D34-9F96016F42BC', N'Taxa Comercial', '3D3ADCEF-3B68-4B90-8A62-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E5D67280-9789-4024-A1F4-9F96016F2FBF', N'Âmbito Territorial', '3D3ADCEF-3B68-4B90-8A62-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A0A9FB7D-1D83-4C22-9E75-9F96016F18E3', N'Credor', '3D3ADCEF-3B68-4B90-8A62-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('261297CE-FACB-4324-9C08-9F96016F7DC3', N'Capital', '0D834A44-FA65-47F8-9F63-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D38B53D0-9E78-4E09-ABE8-9FE2011CA610', N'Taxa', '0D834A44-FA65-47F8-9F63-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/oo', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E1EDFE55-C39A-419E-B84C-9F96016FA6C2', N'Actividade', '2D04D698-852B-4B08-985C-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D292FFEC-D494-45BB-89C3-9FE2011CFF8D', N'Capital', 'E50FD5B1-BC58-4A7C-AB76-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('A53D9C4D-BE49-4B07-8FFC-9FE2011D34A3', N'Tipo de Arma', 'A19BDF86-4FF8-4BA0-A96A-9F920158BDAB', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('935CE17E-FACA-47EE-88B2-9FE2011D4E4E', N'Capital', 'A19BDF86-4FF8-4BA0-A96A-9F920158BDAB', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D855C924-DB60-4505-BD25-9F9601700B1C', N'Capital', 'AF9713B9-8932-4A15-A2A5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1D36714A-29B0-4E30-B898-9FE2011DC495', N'Prémio', 'AF9713B9-8932-4A15-A2A5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1169D2A4-D463-412B-9357-9FE2011DDE3C', N'Âmbito Territorial', 'AF9713B9-8932-4A15-A2A5-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DFCAA549-4CD3-4B56-A020-9FE2011E5BE9', N'Tipo de Taxa', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2080F19E-7431-401D-9D4C-9F9601703CD2', N'Variante', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C372C03C-9B7A-41BC-A64E-9FE2011E750D', N'Capital', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5598EADB-E9F3-4958-9514-9F9601707E4B', N'Prémio Comercial Mínimo', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('873627F5-5598-4267-BACA-9F9601706653', N'Taxa de Acerto', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/o ou o/oo', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7D576D2C-0167-4A09-8E6F-9F960170527B', N'Valor da Facturação ou Salários', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('79E271B8-8AFE-4E75-B9CC-9F9601702C73', N'Actividade', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('889AC7E5-8B40-4A3F-B725-9F960170910A', N'Âmbito Territorial', '370072F8-E5C7-4CDC-AFAF-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5BAF6A5E-8603-4573-B988-9F960170AF40', N'Âmbito Territorial', '3A3F581A-096A-4EAF-B8BD-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4BA5FF8E-2644-4B27-96B0-9FE2011F02E8', N'Tipo de Taxa', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7283A5CD-5E47-4D5D-8BD9-9F960170E080', N'Variante', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('8A05461D-EB27-46A0-89F5-9FE2011F2698', N'Capital', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5FAB145A-6CA6-42DD-BE5D-9F9601711D4F', N'Prémio Comercial Mínimo', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4C62AE47-42E6-4A40-8EED-9F9601710AC7', N'Taxa de Acerto', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/o ou o/oo', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('988E3D63-FDE0-49B1-AF81-9F960170F507', N'Valor da Facturação ou Salários', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('ACF3ED80-98ED-4384-A903-9F9601713C65', N'Âmbito Territorial', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('10B97833-7CFD-4BBB-B73D-9F960170CFCE', N'Produto ou Actividade', 'D88F8444-DB16-4565-AC0A-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B6C57960-599D-4EA3-A19D-9FE2011FC53B', N'Tipo de Taxa', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('EF957844-CA2A-4D64-B1E3-9F9601718E62', N'Variante', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F6F60949-1000-47BC-994D-9FE2011FDDD4', N'Capital', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4D813AFC-035F-40E2-9E60-9F960171CA20', N'Prémio Comercial Mínimo', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3D5287B5-3735-42EC-A3DC-9F960171B778', N'Taxa de Acerto', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'o/o ou o/oo', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('64B6A3B3-1A8C-40AC-892D-9F960171A278', N'Valor da Facturação ou Salários', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B147F99E-9A4A-4D98-8908-9F9601717FF5', N'Actividade', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('F027892F-C035-44C3-A83A-9F960171DA2E', N'Âmbito Territorial', '02FEA8AA-8A68-4E85-BEC5-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('53E6F7C8-719C-41FA-A365-9FE201201B58', N'Capital', '6FEEBD34-C41F-4986-AF84-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('84AE1572-B05C-40B9-8F61-9F960171F750', N'Actividade', '6FEEBD34-C41F-4986-AF84-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('61584895-084F-4921-9A91-9FE201203093', N'Âmbito Territorial', '6FEEBD34-C41F-4986-AF84-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FFDC3E20-CDC7-4598-8CA1-9F960172194C', N'Capital', '30FDEC09-25EB-441C-B284-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9D858E59-5B84-434E-82F3-9FE201205DE8', N'Prémio', '30FDEC09-25EB-441C-B284-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('0FAB2713-ED1C-4F65-9B1B-9F9601724AC4', N'Âmbito Territorial', 'C27E18BB-B859-48C6-9782-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('936EE010-6003-4312-94F1-9FE20120CE19', N'Lotação', '588362BA-A691-4FEF-A058-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('5CA8327B-89C7-4DAB-ADFC-9FE20120E730', N'Âmbito Territorial', '588362BA-A691-4FEF-A058-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('BCDA7B41-60E0-466E-BA8F-9FE20121EEED', N'Tipo de Contrato', '179F8846-B6C1-4906-A559-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3CB9D194-D8BF-45D8-BB55-9F9601729B4A', N'Capital Inicial', '179F8846-B6C1-4906-A559-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'UC's ou €', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7E32325F-24AC-499B-83B6-9F960172AD4B', N'Duração', '179F8846-B6C1-4906-A559-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('913E456A-E25C-4DB9-BF77-9F960172797E', N'Valor Inicial da Unidade de Conta', '179F8846-B6C1-4906-A559-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1A9DCF3C-D9C4-47B9-BF7C-9FE201228DE3', N'Tipo de Contrato', 'AACCEAF3-0292-44D1-B734-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('3F37C60E-1B48-476C-A085-9F960172E34D', N'Capital Inicial', 'AACCEAF3-0292-44D1-B734-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'UC's ou €', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('2980918A-27B2-4FBD-A253-9F960172F3EC', N'Duração', 'AACCEAF3-0292-44D1-B734-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('36335D6A-42F6-456B-9FA5-9F960172D372', N'Valor Inicial da Unidade de Conta', 'AACCEAF3-0292-44D1-B734-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FA78FCD1-6E6C-470E-B6D2-9F9601732AA2', N'Duração do Pagamento do Prémio', '5F85E073-686C-4A98-AC36-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6DB3F970-C791-407D-886B-9F9601736563', N'Indexação', '5F85E073-686C-4A98-AC36-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('579C19BB-77B5-4281-91B5-9FE20122F540', N'Data Início da Renda', '5F85E073-686C-4A98-AC36-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D455F2AD-F045-42EC-9441-9F9601739A10', N'Tipo de Capital', '7F4CB77E-62DB-470E-BE34-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('79241724-2655-4504-9290-9F960173839E', N'Capital Base', '7F4CB77E-62DB-470E-BE34-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E329E04C-7833-4488-A103-9F960173E6D0', N'Duração', 'F4CDAE65-6D51-46E0-A908-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B4C6237C-448C-49FE-AAAB-9F960173D5E1', N'Quantidade de Títulos', 'F4CDAE65-6D51-46E0-A908-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('196E7834-CF8B-4CE0-BA0C-9F960173C363', N'Valor Nominal do Título', 'F4CDAE65-6D51-46E0-A908-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D4CDDA0E-00A6-4CE9-A7DF-9FE20126065E', N'Tipo de Contrato', 'AB4D7468-A888-4987-8834-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('7434A92A-2187-410E-8653-9F9601741C39', N'Capital Inicial', 'AB4D7468-A888-4987-8834-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'UC's ou €', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CEEC8D41-9EAF-4539-A810-9F96017432AB', N'Duração', 'AB4D7468-A888-4987-8834-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('DC153A10-FD3E-4882-B9EF-9F9601740A86', N'Valor Inicial da Unidade de Conta', 'AB4D7468-A888-4987-8834-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4271E12A-D276-4386-88EA-9FE2012748B6', N'Tipo de Contrato', '7D7361F6-C786-478F-A340-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('804E4C26-8EAD-4BC4-B2C0-9F9601746408', N'Capital Inicial', '7D7361F6-C786-478F-A340-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'UC's ou €', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('9EEDEB79-BDA6-4C6A-9B78-9F960174767C', N'Duração', '7D7361F6-C786-478F-A340-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('B7639D24-F074-4F89-99FF-9F9601745588', N'Valor Inicial da Unidade de Conta', '7D7361F6-C786-478F-A340-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('6E0D0F26-C3E1-4723-9991-9F960174D4B4', N'Fraccionamento da Renda', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E15F27C4-9F6C-4C5E-8BF1-9FE20127D8E0', N'Duração do Pagamento da Renda', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('FA8F29A5-17B0-419C-962C-9F960174AC8E', N'Duração do Pagamento do Prémio', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('4BA2D479-CD78-4A78-B2D9-9F96017496E0', N'Entrega Inicial', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('CDA6EC2D-E2C0-4272-A731-9F960174C1E7', N'Valor da Renda', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 0, NULL, -1, 0);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D3278B4D-53E6-4463-BA36-9F960174EA13', N'Indexação', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('E65A1294-F628-44CF-8DFB-9FE20127BCFE', N'Data Início da Renda', 'C60C654D-39BA-4542-9E3F-9F9100E6F9A3', '426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('C3563291-7658-4B53-BF5F-9F9601751EBB', N'Tipo de Capital', 'D39D1528-B185-4AF8-B363-9F9100E6F9A3', 'C32C7BE9-FCEB-457E-8C3A-9F9601403AC1', NULL, NULL, 0, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('856DB2F7-354F-4CFE-9704-9F96017509F7', N'Capital Base', 'D39D1528-B185-4AF8-B363-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 1, 1, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('1C8EC89C-0401-4373-8296-9F96017581B5', N'Duração', '39CF4652-FFD3-499C-84B3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'anos', NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('D546ADED-EB25-4B56-AE38-9F9601757149', N'Quantidade de Títulos', '39CF4652-FFD3-499C-84B3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', NULL, NULL, 1, 0, NULL, -1, 1);
insert into bigbang.tblBBTaxes (PK, TaxName, FKCoverage, FKFieldType, Units, DefaultValue, BVariesByObject, BVariesByExercise, FKReferenceTo, ColumnOrder, BMandatory) values ('11A6B260-5426-4AE7-98EE-9F9601756071', N'Valor Nominal do Título', '39CF4652-FFD3-499C-84B3-9F9100E6F9A3', '4D82EE91-0A9E-415E-9003-9F9601404007', N'€', NULL, 0, 0, NULL, -1, 1);

insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
UPPER(LEFT(NomeEquipa, 3)) CCCode, NomeEquipa CCName, IDEquipa MigrationID
from credegs..empresa.equipa
where IDEquipa not in (9, 13);
insert into bigbang.tblBBCostCenters (PK, CCCode, CCName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'GNC' CCCode, NomeEquipa CCName, IDEquipa MigrationID
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



insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('49153B77-1391-4E3A-81D2-9EB800CB68B7', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '1822E9C1-700F-49A5-AB6F-9EB500C632D2', '091B8442-B7B0-40FA-B517-9EB00068A390', 0);

insert into credite_egs.tblProcGeneralSystem (PK, FKProcess) values ('1822E9C1-700F-49A5-AB6F-9EB500C632D2', '49153B77-1391-4E3A-81D2-9EB800CB68B7');

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(locpostal))) PostalCity
from credegs..empresa.companhi s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into credite_egs.tblCompanies (PK, CompName, ShortName, FiscalNumber, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.NOME CompName, s.SIGLA ShortName, s.NIFC FiscalNulber, s.MORADA Address1, c.PK FKZipCode, s.COMPANHIA MigrationID
from credegs..empresa.companhi s
left outer join bigbang.tblPostalCodes c on c.PostalCode=s.CODPOSTAL COLLATE DATABASE_DEFAULT
where s.companhia <> 61;

insert into credite_egs.tblCompanies (PK, CompName, ShortName, FiscalNumber, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
s.NOME CompName, 'GPCV' ShortName, s.NIFC FiscalNulber, s.MORADA Address1, c.PK FKZipCode, s.COMPANHIA MigrationID
from credegs..empresa.companhi s
left outer join bigbang.tblPostalCodes c on c.PostalCode=s.CODPOSTAL COLLATE DATABASE_DEFAULT
where s.companhia = 61;

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Sede' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, 'CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' FKContactType
from credite_egs.tblCompanies l
inner join credegs..empresa.companhi r on r.COMPANHIA=l.MigrationID
where r.fax is not null and r.fax<>'';

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.fax InfoValue
from credite_egs.tblCompanies l
inner join credegs..empresa.companhi r on r.COMPANHIA=l.MigrationID
inner join credite_egs.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' and ContactName=N'Sede';

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.AOCUIDADO ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner, '07367032-3A5D-499D-88BD-9EEE013357C9' FKContactType
from credite_egs.tblCompanies l
inner join credegs..empresa.companhi r on r.COMPANHIA=l.MigrationID
where r.AOCUIDADO is not null and r.AOCUIDADO<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(locpostal))) PostalCity
from credegs..empresa.contactoscomseg s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Outro' ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, 'CF3019C6-8A9C-495C-B9D0-9EEE01335BC6' FKContactType, r.IDContacto MigrationID
from credite_egs.tblCompanies l
inner join credegs..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is null or r.Nome='';

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, '88AF4A7C-DAB2-4E7F-B85D-9EEE01467E91' FKContactType, r.IDContacto MigrationID
from credite_egs.tblCompanies l
inner join credegs..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and r.Assunto='Tesouraria';

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, 'BA706479-AE31-4E69-A7F0-9EEE01336CA4' FKContactType, r.IDContacto MigrationID
from credite_egs.tblCompanies l
inner join credegs..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and r.Assunto like '%recibo%';

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, FKZipCode, FKContactType, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.Nome ContactName, '7B203DCA-FFAC-46B2-B849-9EBC009DB127' FKOwnerType, l.PK FKOwner,
r.Morada Address1, c.PK FKZipCode, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType, r.IDContacto MigrationID
from credite_egs.tblCompanies l
inner join credegs..empresa.ContactosComseg r on r.FKCompanhia=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.Nome is not null and r.Nome<>'' and (r.Assunto is null or r.Assunto='' or r.Assunto='Geral');

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.Telefone InfoValue
from credegs..empresa.ContactosComseg r
inner join credite_egs.tblContacts l on l.MigrationID=r.IDContacto
where r.Telefone is not null and r.Telefone<>'';

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.Fax InfoValue
from credegs..empresa.ContactosComseg r
inner join credite_egs.tblContacts l on l.MigrationID=r.IDContacto
where r.Fax is not null and r.Fax<>'';

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
l.PK FKContact, '96467849-6FE1-4113-928C-9EDF00F40FB9' FKInfoType, r.Email InfoValue
from credegs..empresa.ContactosComseg r
inner join credite_egs.tblContacts l on l.MigrationID=r.IDContacto
where r.Email is not null and r.Email<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct codpostal PostalCode, rtrim(ltrim(upper(localidade))) PostalCity
from credegs..empresa.agente s left outer join bigbang.tblpostalcodes c on ltrim(s.codpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null and s.codpostal is not null and s.codpostal <>'') z;

insert into credite_egs.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, 'F60BB994-3E08-47C2-9CC3-9EFC013D35BE' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from credegs..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.NUMINSTI=0 and a.SpecialCalc!=1;

insert into credite_egs.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, '071CE678-956B-4D41-94DE-9EFC013688B5' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from credegs..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.SpecialCalc=1;

insert into credite_egs.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, 'CECC8014-200C-4C4F-9F47-9EFC01368139' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from credegs..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.NUMINSTI!=0 and a.SpecialCalc!=1 and a.PERCEM!=0;

insert into credite_egs.tblMediators (PK, MediatorName, FiscalNumber, FKProfile, Address1, FKZipCode, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
a.NOME MediatorName, a.NUMCONTR FiscalNulber, 'C5BE51A9-7E0F-4970-962A-9EFC0135E9E1' FKProfile, a.MORADA Address1, c.PK FKZipCode, a.AGENTE MigrationID
from credegs..empresa.agente a
left outer join bigbang.tblPostalCodes c on c.PostalCode=CAST(a.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
where a.NUMINSTI!=0 and a.SpecialCalc!=1 and (a.PERCEM is null or a.PERCEM=0);

insert into credite_egs.tblBBGroups (PK, GroupName, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
g.nome GroupName, g.grupo MigrationID
from credegs..empresa.grupos g
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
(select distinct ltrim(rtrim(Profissao)) ProfessionName from credegs..empresa.cliente c
left outer join bigbang.tblProfessions p on p.ProfessionName=ltrim(rtrim(c.Profissao)) COLLATE DATABASE_DEFAULT
where p.PK is null and c.Profissao is not null and ltrim(rtrim(c.Profissao))<>'') z;

insert into bigbang.tblCAE (PK, CAEText)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select distinct CAST(c.CODCAE AS VARCHAR(5)) + ' - ?' CAEText from credegs..empresa.cliente c
left outer join bigbang.tblCAE x on left(x.CAEText, 5) = CAST(c.CODCAE AS VARCHAR(5)) COLLATE DATABASE_DEFAULT
where c.CODCAE like '_____' and x.PK is null) z;

insert into credite_egs.tblBBClients (PK, ClientName, ClientNumber, Address1, Address2, FKZipCode, FiscalNumber, FKEntityType, FKEntitySubType, FKMediator,
FKProfile, FKGroup, DateOfBirth, FKSex, FKMaritalStatus, FKProfession, FKCAE, ClientNotes, MigrationID, DShareFolder)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.NOME ClientName, row_number() over (order by c.CLIENTE) ClientNumber, c.MORADA Address1, c.LOCALIDADE Address2, p.PK FKZipCode, c.NCONTRIB FiscalNumber,
CASE c.TIPO_C WHEN 'I' THEN '462096E4-68A2-408A-963A-9EE600C9556A' WHEN 'E' THEN 'C5B4F500-BB57-4BFD-8248-9EE600C95ABA' ELSE '4098CF7A-B5EE-4C3F-973F-9EE600C961AA' END FKEntityType,
CASE c.TIPO_C WHEN 'C' THEN '5C7A0424-126B-467B-977A-9EE600CC13A4' ELSE NULL END FKEntitySubType,
m.PK FKMediator,
CASE c.ClienteVIP WHEN 1 THEN '63114D11-6087-4EFE-9A7E-9EE600BE52DA' ELSE '9F871430-9BBC-449F-B125-9EE600BE5A9A' END FKProfile,
g.PK FKGroup, c.DataNascimento DateOfBirth, s.PK FKSex, t.PK FKMaritalStatus, f.PK FKProfession, x.PK FKCAE, CAST(c.OBSERV AS VARCHAR(250)) ClientNotes,
c.CLIENTE MigrationID, c.DocuShare DShareFolder
from credegs..empresa.cliente c
left outer join bigbang.tblPostalCodes p on p.PostalCode=CAST(c.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
left outer join credite_egs.tblMediators m on m.MigrationID=c.MEDIACLI
left outer join credite_egs.tblBBGroups g on g.MigrationID=c.grupo COLLATE DATABASE_DEFAULT
left outer join bigbang.tblSex s on left(s.SexName, 1)=c.Sexo COLLATE DATABASE_DEFAULT
left outer join bigbang.tblMaritalStatuses t on upper(left(t.StatusText, 1))=upper(left(c.EstadoCivil, 1)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblProfessions f on f.ProfessionName=ltrim(rtrim(c.Profissao)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblCAE x on left(x.CAEText, 5)=CAST(c.CODCAE AS VARCHAR(5)) COLLATE DATABASE_DEFAULT
where c.NOME<>'' and c.GESTORCLI not in (91, 98, 99);

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Geral' ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and ((r.TELEFONE is not null and r.TELEFONE<>'') or (r.fax is not null and r.fax<>'') or (r.Telemovel is not null and r.Telemovel<>''));

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.TELEFONE InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.TELEFONE is not null and r.TELEFONE<>'';

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '172EC088-AA55-433B-BBC3-9EDF00F42266' FKInfoType, r.fax InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.fax is not null and r.fax<>'';

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '60414F28-49E7-43AD-ACD9-9EDF00F41E76' FKInfoType, r.Telemovel InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Geral'
and r.Telemovel is not null and r.Telemovel<>'';

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
r.CONTACTO ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and r.CONTACTO is not null and r.CONTACTO<>'';

insert into bigbang.tblPostalCodes (PK, PostalCode, PostalCity)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK, * from
(select PostalCode, min(PostalCity) PostalCity from
(select distinct profcodpostal PostalCode, rtrim(ltrim(upper(proflocpostal))) PostalCity
from credegs..empresa.cliente s left outer join bigbang.tblpostalcodes c on ltrim(s.profcodpostal)=c.postalcode COLLATE DATABASE_DEFAULT
where c.postalcode is null
and s.profcodpostal is not null and s.profcodpostal <>'' and s.profcodpostal not like '%[^-0123456789]%' and s.proflocpostal is not null and s.proflocpostal <>'') z
group by PostalCode) y;

insert into credite_egs.tblContacts (PK, ContactName, FKOwnerType, FKOwner, Address1, Address2, FKZipCode, FKContactType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Profissional' ContactName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner,
r.ProfMorada Address1, r.ProfLocalidade Address2, c.PK FKZipCode, '04F6BC3C-0283-47F0-9670-9EEE013350D9' FKContactType
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
left outer join bigbang.tblPostalCodes c on c.PostalCode=r.CodPostal COLLATE DATABASE_DEFAULT
where r.NOME<>'' and ((r.ProfMorada is not null and r.ProfMorada<>'') or (r.ProfLocalidade is not null and r.ProfLocalidade<>'') or (r.ProfCodPostal is not null and r.ProfCodPostal<>'') or (r.ProfTelefone is not null and r.ProfTelefone<>''));

insert into credite_egs.tblContactInfo (PK, FKContact, FKInfoType, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.PK FKContact, '01C8D0CA-074E-45AA-8A17-9EDF00F41586' FKInfoType, r.ProfTelefone InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblContacts c on c.FKOwner=l.PK
where c.FKContactType='04F6BC3C-0283-47F0-9670-9EEE013350D9' and ContactName=N'Profissional'
and r.ProfTelefone is not null and r.ProfTelefone<>'';

insert into credite_egs.tblBBDocuments (PK, DocName, FKOwnerType, FKOwner, FKDocType)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
N'Carta de Condução' DocName, 'D535A99E-149F-44DC-A28B-9EE600B240F5' FKOwnerType, l.PK FKOwner, '5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' FKDocType
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
where r.NOME<>'' and ((r.CartaCondNum is not null and r.CartaCondNum<>'') or (r.CartaCondData is not null and r.CartaCondData<>''));

insert into credite_egs.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Número' InfoName, r.CartaCondNum InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblBBDocuments d on d.FKOwner=l.PK
where d.FKDocType='5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' and d.DocName=N'Carta de Condução'
and r.CartaCondNum is not null and r.CartaCondNum<>'';

insert into credite_egs.tblDocInfo (PK, FKOwner, InfoName, InfoValue)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
d.PK FKOwner, N'Data' InfoName, r.CartaCondData InfoValue
from credite_egs.tblBBClients l
inner join credegs..empresa.cliente r on r.CLIENTE=l.MigrationID
inner join credite_egs.tblBBDocuments d on d.FKOwner=l.PK
where d.FKDocType='5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A' and d.DocName=N'Carta de Condução'
and r.CartaCondData is not null and r.CartaCondData<>'';

insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'100E701A-EDC5-4D9C-A221-9F09013D7954' FKScript, i.PK FKData, u.FKUser FKManager, 0 IsRunning
from credite_egs.tblBBClients i
inner join credegs..empresa.cliente c on c.CLIENTE=i.MigrationID
inner join bigbang.tblUser2 u on u.MigrationID=c.GESTORCLI;

update credite_egs.tblBBClients set FKProcess=p.PK
from credite_egs.tblBBClients c inner join credite_egs.tblPNProcesses p on p.FKData=c.PK;



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
