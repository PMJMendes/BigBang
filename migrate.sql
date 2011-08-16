delete from credite_egs.tblDocInfo;
delete from credite_egs.tblBBDocuments;
delete from credite_egs.tblContactInfo;
delete from credite_egs.tblContacts;

delete from credite_egs.tblBBClients;
delete from credite_egs.tblBBGroups;
delete from credite_egs.tblBBTaxes;
delete from credite_egs.tblBBCoverages;
delete from credite_egs.tblMediators;
delete from credite_egs.tblCompanies;
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
delete from amartins.tblMediators;
delete from amartins.tblCompanies;
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
delete from bigbang.tblUnits;

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
insert into bigbang.tblLineCategories (PK, LineCatName) values ('F04EBBD0-45BD-4921-931F-9EE9010A6B0E', N'Doença');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('56A359A3-33DF-40E5-B004-9EE9010A726A', N'Incêndio');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('ECFD23D7-2D15-43F3-B92F-9EE9010A7B34', N'Multiriscos');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('787B047E-7B15-4DA6-992D-9EE9010A857F', N'Outros Danos a Bens');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('6DB46133-2789-4B5E-B8FF-9EE9010A8E9D', N'Obras e Montagens');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('A3F44708-4666-425C-AA70-9EE9010A9E0D', N'Agrícola');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('1CFAB688-EC11-459F-96EC-9EE9010AA5A6', N'Pecuário');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('7F5F77EB-8348-4914-8525-9EE9010AB1C6', N'Automóvel');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('2442DD02-F525-4CE3-9CFF-9EE9010ABC44', N'Responsabilidade');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('E2732D85-E204-4A71-A5D4-9EE9010AC7B1', N'Mercadorias Transportadas');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('00960D0F-F31F-4A9E-AE7E-9EE9010AD157', N'Embarcações Marítimas');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('370DE151-6ABF-4561-A8BD-9EE9010ADC2A', N'Veículos Ferroviários');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2', N'Aeronaves');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('73410713-03BD-4247-84A7-9EE9010AECB7', N'Diversos');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('537C3D97-02C9-41EE-A5DE-9EE9010AF1BB', N'Vida');
insert into bigbang.tblLineCategories (PK, LineCatName) values ('696A51D8-B21A-4DA8-B8E9-9EE901153338', N'Fundo de Pensões');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF', N'Por Conta de Outrém', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('98754CEB-DD92-4ACD-9C2F-9EE9010DE2BE', N'Por Conta Própria/Independentes', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('1F781304-924F-4DE8-B0DE-9EE9010DEEA5', N'Subscritores da CGA', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CFBC796D-1F64-4646-A964-9EE9010DF63E', N'Por Área', '53DB03E7-F423-4656-A23A-9EE9010A5B87');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8C3A644D-A4D7-451E-8858-9EE9010E04E4', N'Escolar', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('04B1DE96-A737-4EA4-88E0-9EE9010E0D26', N'Bombeiros', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('1AC7ECE9-11D5-4A70-A19D-9EE9010E13F6', N'Autarcas', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CE91A9A2-57F4-420E-8FFD-9EE9010E20FB', N'Ocupações de Tempos Livres', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('0714501A-73FF-4EA8-B921-9EE9010E29EA', N'Ocupantes de Viatura', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('EB02FB3D-6457-4BE1-A540-9EE9010E307E', N'Viagem', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('5115A4A2-25BD-40B1-88F0-9EE9010E3BE6', N'Tradicional', 'BB3646B1-BE20-4ED1-B3C2-9EE9010A641D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('9F56581A-64B9-4D8E-B3CD-9EE9010E63D5', N'Individual', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40', N'Grupo', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FF0C0086-9322-4AC8-AAC8-9F340151A302', N'Aberta', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('33DA18C2-75B1-4EB7-AEF2-9F340151CC15', N'Gerida', 'F04EBBD0-45BD-4921-931F-9EE9010A6B0E');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('A711AA7E-0565-42E0-94BE-9EE9010E7901', N'Simples', '56A359A3-33DF-40E5-B004-9EE9010A726A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('2D7CCD94-DBD5-43F1-91B5-9EE9010E81DD', N'Industrial', '56A359A3-33DF-40E5-B004-9EE9010A726A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('66787325-D687-4B2D-8284-9EE9010E9C87', N'Habitação', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('438377BA-2FFA-46B5-AF35-9EE9010EA8DE', N'Comércio e/ou Serviços', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('BACE0C3D-6251-42D3-B9F3-9EE9010EB5D5', N'Industrial', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('AF00D387-86D0-4388-AC01-9EE90110F0E2', N'Condomínio', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F22E2E10-1993-4784-86A3-9EE90110F982', N'All Risks', 'ECFD23D7-2D15-43F3-B92F-9EE9010A7B34');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('E5CDFEBC-926C-4502-9F9A-9EE901110585', N'Roubo', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('700E4929-689E-484B-88F8-9EE901110B82', N'Cristais', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CBCB9041-8012-46D7-B8F2-9EE901111406', N'Avaria de Máquinas', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('47FAC08E-F9F0-4695-9CB7-9EE901111DC3', N'Máquinas Cascos', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8901C038-BA37-4F40-8359-9EE901112634', N'Lucros Cessantes', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('7AD30E66-4799-4595-A4F9-9EE90111324A', N'Equipamentos Electrónicos', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FF7777A8-06EB-4303-B0A3-9EE90111396F', N'Campista', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C918E867-909B-4962-88AF-9EE9011141B5', N'Bens em Leasing', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('172CFFAF-AF5B-4E3E-980E-9EE901114D02', N'Bens Refrigerados', '787B047E-7B15-4DA6-992D-9EE9010A857F');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('211BF6E2-9255-4FF1-89AD-9EE9011167D0', N'Obras e Montagens', '6DB46133-2789-4B5E-B8FF-9EE9010A8E9D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('DA199371-3A66-42D8-BB98-9EE9011178A4', N'Incêndio', 'A3F44708-4666-425C-AA70-9EE9010A9E0D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('3F185E97-921A-467D-8B22-9EE901117FE9', N'Colheitas', 'A3F44708-4666-425C-AA70-9EE9010A9E0D');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('0AF6AF10-6879-4D23-B4FC-9EE901118E86', N'Pecuário', '1CFAB688-EC11-459F-96EC-9EE9010AA5A6');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('59A58D39-F445-4CF5-8B6B-9EE901119B65', N'Automóvel', '7F5F77EB-8348-4914-8525-9EE9010AB1C6');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('846652EE-1711-4869-8290-9EE90111A853', N'Exploração', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('7DF2A66C-1200-430D-848A-9EE90111AF93', N'Profissional', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('4C475D8E-47D9-4ABC-8FB1-9EE90111B67A', N'Produtos', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('A672131B-0928-43C9-90BE-9EE90111C0F4', N'Ambiental', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C98B2362-63CF-4D02-800D-9EE90112CFA4', N'Convenção Mercadorias Rodoviárias', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F7634CFD-301E-4373-99DB-9EE90112E4C2', N'Transporte Rodoviário Nacional', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('2A5D7B5B-07B4-43C2-8BE0-9EE90112EE56', N'Caçadores', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('26B9F09B-7E14-4F16-8DD9-9EE90112F534', N'Animais', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('4F2055AC-8447-4A90-A886-9EE90112FB19', N'Familiar', '2442DD02-F525-4CE3-9CFF-9EE9010ABC44');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('976C78A8-A26D-4B26-B583-9EE901131575', N'Mercadorias Transportadas', 'E2732D85-E204-4A71-A5D4-9EE9010AC7B1');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('110FC92D-552E-474F-AF1E-9F340153273E', N'Transporte de Valores', 'E2732D85-E204-4A71-A5D4-9EE9010AC7B1');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('CF483414-189C-4A0A-998B-9EE901132980', N'Cascos e RC', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('C53AA3FC-D528-4A1E-9436-9EE9011332A8', N'Recreio', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('878733B8-77EB-42EF-9E7A-9EE9011349A7', N'Pessoas Transportadas', '00960D0F-F31F-4A9E-AE7E-9EE9010AD157');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('88B63B22-3BD7-4391-9C69-9EE901135CF9', N'Cascos e RC', '370DE151-6ABF-4561-A8BD-9EE9010ADC2A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('32A04D18-5E9B-4507-A261-9EE9011367AA', N'Pessoas Transportadas', '370DE151-6ABF-4561-A8BD-9EE9010ADC2A');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('89344125-0CDB-4242-BF23-9EE901138254', N'Cascos e RC', 'C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('67C79D4A-F869-43FB-BFA0-9EE9011395FE', N'Pessoas Transportadas', 'C81B00E0-1BC2-48E6-B8FE-9EE9010AE3A2');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('AA798E09-2ABE-4DF4-B874-9EE90113B032', N'Crédito', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('88DBC911-336B-4098-A0B7-9EE90113BD32', N'Caução', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('99967F7A-ADAF-4E8D-915A-9EE90113C66D', N'Assistência', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('3A0110CA-7136-4499-ABCE-9EE90113CF24', N'Protecção Jurídica', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('DB2CAB3A-5A68-41B4-B8AA-9EE90113DDA9', N'Protecção Animais', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('8B678CB7-90B8-4A51-9226-9F340153F759', N'Produtos à Medida', '73410713-03BD-4247-84A7-9EE9010AECB7');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('FDE37FD5-BD29-4AA6-8A30-9EE901151B11', N'Individual', '537C3D97-02C9-41EE-A5DE-9EE9010AF1BB');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('F4E19A84-44FD-4BCE-9EDE-9EE9011521C0', N'Grupo', '537C3D97-02C9-41EE-A5DE-9EE9010AF1BB');
insert into bigbang.tblBBLines (PK, LineName, FKCategory) values ('574254C5-E217-4F21-8151-9EE901155527', N'Pensões de Reforma/Invalidez', '696A51D8-B21A-4DA8-B8E9-9EE901153338');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('BEBB58B5-CD95-4872-B72F-9EE90118938F', N'Folhas de Férias', '44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('CA6EC5CA-FF4E-4E2C-BCF9-9EE901189BE9', N'Prémio Fixo', '44F6F0F5-459F-42E1-8FEE-9EE9010DD5AF');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('1ADF22B2-D2A1-41CB-8FC1-9EE90118A7AF', N'Única', '98754CEB-DD92-4ACD-9C2F-9EE9010DE2BE');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('68ED97FA-B9F8-40F7-B470-9EE90118B2DB', N'Folhas de Férias', '1F781304-924F-4DE8-B0DE-9EE9010DEEA5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('BB0E78F2-3817-45C6-94C7-9EE90118BA82', N'Prémio Fixo', '1F781304-924F-4DE8-B0DE-9EE9010DEEA5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('34E19434-6106-4359-93FE-9EE90118CEE0', N'Construção Civil', 'CFBC796D-1F64-4646-A964-9EE9010DF63E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('FE18F5ED-F87C-4595-B9AC-9EE90118D568', N'Agrícola', 'CFBC796D-1F64-4646-A964-9EE9010DF63E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('3C85B935-B33E-4C86-AD0C-9EE90118E58F', N'Única', '8C3A644D-A4D7-451E-8858-9EE9010E04E4');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('3DD40BED-A453-453F-8F0D-9EE90118EFAB', N'Única', '04B1DE96-A737-4EA4-88E0-9EE9010E0D26');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('264279D9-A946-4209-AE35-9EE90118F78B', N'Única', '1AC7ECE9-11D5-4A70-A19D-9EE9010E13F6');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D59C9892-A19F-429A-A462-9EE9011900E1', N'Individual', 'CE91A9A2-57F4-420E-8FFD-9EE9010E20FB');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('7A545EC7-36AC-4228-9D36-9EE90119060F', N'Grupo', 'CE91A9A2-57F4-420E-8FFD-9EE9010E20FB');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('23D97FAD-8137-48B9-8BEB-9EE9011914F8', N'Individual', '0714501A-73FF-4EA8-B921-9EE9010E29EA');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('8CDAEBD9-F366-4291-B710-9EE901191FAE', N'Frota', '0714501A-73FF-4EA8-B921-9EE9010E29EA');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('92B7DEBE-06A9-45E1-968B-9EE901192D48', N'Individual', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('FA485DD9-08FB-44DC-BDCF-9EE9011939A9', N'Grupo', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('CF5EF293-9ED1-4EC1-9760-9F34015146D4', N'Aberta', 'EB02FB3D-6457-4BE1-A540-9EE9010E307E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('12D8E75F-2C36-4359-ABD7-9EE901194453', N'Individual', '5115A4A2-25BD-40B1-88F0-9EE9010E3BE6');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('8572FE00-D359-437A-8359-9EE901194A05', N'Grupo', '5115A4A2-25BD-40B1-88F0-9EE9010E3BE6');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('2F2BFF64-61AF-4CE3-8F74-9EE90119687C', N'Reembolso', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('422AE9F5-03DE-4AE2-830F-9EE901196F1C', N'Rede Convencionada', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('1B70989B-1D54-408E-BDF7-9EE90119749F', N'Mista', '9F56581A-64B9-4D8E-B3CD-9EE9010E63D5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C8E5724B-4BD2-4AAF-ADAE-9EE901197B82', N'Reembolso', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('CD456E4C-1ED7-4819-AB22-9EE901198248', N'Rede Convencionada', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D2ACD7C9-C5A6-42A8-9E11-9EE9011987D0', N'Mista', '15A5E5BF-F045-4AF9-BF4B-9EE9010E6B40');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('F990606F-0AB6-4EE8-BFDA-9F340151B327', N'Reembolso', 'FF0C0086-9322-4AC8-AAC8-9F340151A302');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('E2E6041E-1C8A-41F4-B6B9-9F340151BAA1', N'Rede Convencionada', 'FF0C0086-9322-4AC8-AAC8-9F340151A302');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('DB605A61-8AC7-427A-8A1C-9F340151C1A9', N'Mista', 'FF0C0086-9322-4AC8-AAC8-9F340151A302');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('BB50ACAA-F557-4249-934F-9F340151D334', N'Única', '33DA18C2-75B1-4EB7-AEF2-9F340151CC15');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('995699D1-1674-4B8E-B813-9EE90119A4F7', N'Única', 'A711AA7E-0565-42E0-94BE-9EE9010E7901');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('B3174C0F-DDF9-4466-A419-9EE90119ABDE', N'Única', '2D7CCD94-DBD5-43F1-91B5-9EE9010E81DD');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('9B46DDFE-3EA5-49E3-B5EA-9EE90119C19A', N'Única', '66787325-D687-4B2D-8284-9EE9010E9C87');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('B9CF5874-AE6A-47D2-824C-9EE90119CB66', N'Única', '438377BA-2FFA-46B5-AF35-9EE9010EA8DE');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('9F79A559-E805-4238-8EC0-9EE90119D1A4', N'Única', 'BACE0C3D-6251-42D3-B9F3-9EE9010EB5D5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('ACB50670-CCFC-4FDE-8D79-9EE90119D9CB', N'Única', 'AF00D387-86D0-4388-AC01-9EE90110F0E2');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('6725D59E-DBFD-46B4-9533-9EE90119E127', N'Única', 'F22E2E10-1993-4784-86A3-9EE90110F982');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('023C01EF-D4A7-441C-B241-9EE9011A2715', N'Única', 'E5CDFEBC-926C-4502-9F9A-9EE901110585');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('252789C2-928E-467A-B081-9EE9011A2E76', N'Única', '700E4929-689E-484B-88F8-9EE901110B82');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('1E6F27F3-48D2-4D2C-B6FA-9EE9011A3542', N'Única', 'CBCB9041-8012-46D7-B8F2-9EE901111406');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D38965BA-1275-4C36-BC89-9EE9011A3C6A', N'Única', '47FAC08E-F9F0-4695-9CB7-9EE901111DC3');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('06CEF06D-73FF-4579-A8F4-9EE9011A08E8', N'Incêndio', '8901C038-BA37-4F40-8359-9EE901112634');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('DBFA4951-59CF-4008-98A5-9EE9011A0F30', N'Máquinas', '8901C038-BA37-4F40-8359-9EE901112634');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('44928C9B-ADAA-4FF1-9F55-9EE9011A1D0E', N'Multiriscos', '8901C038-BA37-4F40-8359-9EE901112634');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('6A3E99B0-9B3B-4CD8-AA06-9EE9011A457D', N'Única', '7AD30E66-4799-4595-A4F9-9EE90111324A');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('98FD6FB7-6AE8-458F-BFC4-9EE9011A4B5A', N'Única', 'FF7777A8-06EB-4303-B0A3-9EE90111396F');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C77B46AF-B098-452D-914C-9EE9011A5198', N'Única', 'C918E867-909B-4962-88AF-9EE9011141B5');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('445184E3-326B-4796-8618-9EE9011A5811', N'Em Câmaras', '172CFFAF-AF5B-4E3E-980E-9EE901114D02');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D5BEE08C-E0EA-4D25-9468-9F3401526786', N'Transportados', '172CFFAF-AF5B-4E3E-980E-9EE901114D02');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D36F47AB-0D5A-425D-8FD4-9EE9011A6837', N'Por Obra', '211BF6E2-9255-4FF1-89AD-9EE9011167D0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('6A2A5C28-6866-463C-9B6A-9F34015284C4', N'Aberta', '211BF6E2-9255-4FF1-89AD-9EE9011167D0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('079B1B49-255E-464A-A62F-9EE9011A768E', N'Única', 'DA199371-3A66-42D8-BB98-9EE9011178A4');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C6D519C6-7138-4EE9-A954-9EE9011A7CAB', N'Única', '3F185E97-921A-467D-8B22-9EE901117FE9');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('900B6EA1-D4BE-4645-93B7-9EE9011A916A', N'Única', '0AF6AF10-6879-4D23-B4FC-9EE901118E86');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('22FE8580-E680-4EC7-9ABB-9EE9011AA269', N'Individual', '59A58D39-F445-4CF5-8B6B-9EE901119B65');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('E08BDBD3-4F59-45B9-975E-9EE9011AA8AC', N'Frota', '59A58D39-F445-4CF5-8B6B-9EE901119B65');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('F61FCBEE-62D0-459B-B2A8-9F250122F59B', N'Carta', '59A58D39-F445-4CF5-8B6B-9EE901119B65');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C6EC530E-8C28-4EDB-BCE3-9EE9011AC699', N'Única', '846652EE-1711-4869-8290-9EE90111A853');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('7CC94668-25FC-47E7-ADB4-9EE9011ACA6B', N'Individual', '7DF2A66C-1200-430D-848A-9EE90111AF93');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('DF4653EC-E2FE-4FCE-832A-9F340152C13B', N'Empresarial', '7DF2A66C-1200-430D-848A-9EE90111AF93');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('533DA89B-1BEC-474B-B615-9EE9011ACF4F', N'Única', '4C475D8E-47D9-4ABC-8FB1-9EE90111B67A');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C199BF06-E397-454E-80ED-9EE9011AD3EC', N'Única', 'A672131B-0928-43C9-90BE-9EE90111C0F4');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('85EA331B-094E-4BFC-B423-9EE9011AD85E', N'Única', 'C98B2362-63CF-4D02-800D-9EE90112CFA4');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('B5EE5408-8065-4F50-BEB1-9EE9011ADD5A', N'Única', 'F7634CFD-301E-4373-99DB-9EE90112E4C2');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('5CA22680-438E-4C4E-8720-9EE9011AE247', N'Única', '2A5D7B5B-07B4-43C2-8BE0-9EE90112EE56');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('4AA42557-96B9-4EE5-8B55-9EE9011AE763', N'Única', '26B9F09B-7E14-4F16-8DD9-9EE90112F534');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('363ED4D5-26D9-4EDF-8123-9EE9011AEB24', N'Única', '4F2055AC-8447-4A90-A886-9EE90112FB19');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('722EBA20-3136-4E3B-B3F8-9EE9011B059E', N'Pontual', '976C78A8-A26D-4B26-B583-9EE901131575');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('8379A4F9-C8FF-4E48-B417-9EE9011B0BA5', N'Flutuante', '976C78A8-A26D-4B26-B583-9EE901131575');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C4475D8B-72AF-415B-83FE-9F3401532F56', N'Única', '110FC92D-552E-474F-AF1E-9F340153273E');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('3236E457-D202-48FF-9219-9EE9011B1E39', N'Única', 'CF483414-189C-4A0A-998B-9EE901132980');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('54E688CE-8F22-4047-87F3-9EE9011B233A', N'Única', 'C53AA3FC-D528-4A1E-9436-9EE9011332A8');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('1783FCCD-78F0-411C-AF5C-9EE9011B2CEA', N'Única', '878733B8-77EB-42EF-9E7A-9EE9011349A7');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('A96FF9DE-AD9E-4657-A759-9EE9011B3FE3', N'Única', '88B63B22-3BD7-4391-9C69-9EE901135CF9');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('948E3387-F8F2-4E5C-8373-9EE9011B44D4', N'Única', '32A04D18-5E9B-4507-A261-9EE9011367AA');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('11BF2FB9-DCAF-46C7-A43F-9EE9011B59E3', N'Única', '89344125-0CDB-4242-BF23-9EE901138254');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D1AF5518-1EBC-4F4A-B0C6-9EE9011B62CE', N'Única', '67C79D4A-F869-43FB-BFA0-9EE9011395FE');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('FC778D70-0752-4411-92AA-9EE9011B7092', N'Única', 'AA798E09-2ABE-4DF4-B874-9EE90113B032');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('5B47E55C-0EDD-4B31-8D4A-9EE9011B75C6', N'Individual', '88DBC911-336B-4098-A0B7-9EE90113BD32');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('D4A29EF9-6D11-40C7-A87E-9F340153906C', N'Empresarial', '88DBC911-336B-4098-A0B7-9EE90113BD32');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('EB9E9AAA-A33B-4D75-B3FF-9EE9011B7A0A', N'Individual (Viagem)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('FD480C60-2F0B-4D9A-82F8-9F340153B43D', N'Local (Multi-Riscos)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('457B9EA9-0CB7-41B2-AE89-9F340153C9AE', N'Veículo (Viagem Automóvel)', '99967F7A-ADAF-4E8D-915A-9EE90113C66D');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('A56384EE-3F17-42E7-9B2C-9EE9011B7DC7', N'Individual', '3A0110CA-7136-4499-ABCE-9EE90113CF24');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('EB87C2F0-7161-4715-81EA-9F340153E524', N'Empresarial', '3A0110CA-7136-4499-ABCE-9EE90113CF24');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('7E51C1B4-33F3-41D8-92D6-9EE9011B8221', N'Única', 'DB2CAB3A-5A68-41B4-B8AA-9EE90113DDA9');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('E91E32C6-0C55-4F49-BA88-9F340153FD6D', N'Shamir', '8B678CB7-90B8-4A51-9226-9F340153F759');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('DBBCFA61-888C-415A-9A16-9EE9011B9F52', N'Risco', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('50A579F9-8546-4923-90DE-9EE9011BA3EB', N'Rendas', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('4FF96FBB-02FA-4864-9843-9EE9011BAA79', N'Financeiro', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('8B36A6D9-C639-4CB2-AB89-9EE9011BB42E', N'Plano Poupança Reforma', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('F4333BC2-A0DB-4CD2-8FB2-9EE9011BBCAC', N'Títulos de Capitalização', 'FDE37FD5-BD29-4AA6-8A30-9EE901151B11');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('5EC79210-F2BE-4775-9A5E-9EE9011BC36E', N'Risco', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('23DA038C-F8D6-40D3-A52D-9EE9011BCC1D', N'Rendas', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('97D0A05A-0DEA-4CA5-8492-9EE9011BD540', N'Financeiro', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('F66BB2D5-CAF3-40FF-ACAC-9EE9011BE0C9', N'Plano Poupança Reforma', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('C304BE3B-7B80-412A-B277-9EE9011BFB56', N'Títulos de Capitalização', 'F4E19A84-44FD-4BCE-9EDE-9EE9011521C0');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('F94D6C1A-6456-42A6-BDA9-9EE9011C0EF0', N'Individual', '574254C5-E217-4F21-8151-9EE901155527');
insert into bigbang.tblBBSubLines (PK, SubLineName, FKLine) values ('5DE6C674-DBEA-4596-A241-9EE9011C14A9', N'Grupo', '574254C5-E217-4F21-8151-9EE901155527');

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



insert into credite_egs.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('49153B77-1391-4E3A-81D2-9EB800CB68B7', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '1822E9C1-700F-49A5-AB6F-9EB500C632D2', '091B8442-B7B0-40FA-B517-9EB00068A390', 1);

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'49153B77-1391-4E3A-81D2-9EB800CB68B7' FKProcess, PK FKOperation, FKDefaultLevel FKLevel
from bigbang.tblPNOperations
where FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5'  and (OpName like 'Manage%' or OpName like 'Create%');

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from credite_egs.tblPNProcesses p inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

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

insert into credite_egs.tblBBClients (PK, ClientName, ClientNumber, Address1, Address2, FKZipCode, FiscalNumber, FKEntityType, FKEntitySubType, FKManager, FKMediator,
FKProfile, FKGroup, DateOfBirth, FKSex, FKMaritalStatus, FKProfession, FKCAE, ClientNotes, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.NOME ClientName, row_number() over (order by c.CLIENTE) ClientNumber, c.MORADA Address1, c.LOCALIDADE Address2, p.PK FKZipCode, c.NCONTRIB FiscalNumber,
CASE c.TIPO_C WHEN 'I' THEN '462096E4-68A2-408A-963A-9EE600C9556A' WHEN 'E' THEN 'C5B4F500-BB57-4BFD-8248-9EE600C95ABA' ELSE '4098CF7A-B5EE-4C3F-973F-9EE600C961AA' END FKEntityType,
CASE c.TIPO_C WHEN 'C' THEN '5C7A0424-126B-467B-977A-9EE600CC13A4' ELSE NULL END FKEntitySubType,
u.FKUser FKManager, m.PK FKMediator,
CASE c.ClienteVIP WHEN 1 THEN '63114D11-6087-4EFE-9A7E-9EE600BE52DA' ELSE '9F871430-9BBC-449F-B125-9EE600BE5A9A' END FKProfile,
g.PK FKGroup, c.DataNascimento DateOfBirth, s.PK FKSex, t.PK FKMaritalStatus, f.PK FKProfession, x.PK FKCAE, CAST(c.OBSERV AS VARCHAR(250)) ClientNotes,
c.CLIENTE MigrationID
from credegs..empresa.cliente c
left outer join bigbang.tblPostalCodes p on p.PostalCode=CAST(c.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblUser2 u on u.MigrationID=c.GESTORCLI
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
'100E701A-EDC5-4D9C-A221-9F09013D7954' FKScript, PK FKData, FKManager FKManager, 1 IsRunning
from credite_egs.tblBBClients;

update credite_egs.tblBBClients set FKProcess=p.PK
from credite_egs.tblBBClients c inner join credite_egs.tblPNProcesses p on p.FKData=c.PK;

insert into credite_egs.tblPNSteps (PK, FKProcess, FKOperation, FKLevel)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel
from credite_egs.tblPNProcesses p, bigbang.tblPNOperations o
where p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954' and o.PK in ('A9A8F4ED-74C2-473C-873C-9F0901435C1C', 'A99DCEF5-91BA-4CFA-9E70-9F090146FAE4');

insert into credite_egs.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from credite_egs.tblPNProcesses p inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';



insert into amartins.tblPNProcesses (PK, FKScript, FKData, FKManager, IsRunning) values ('FDF0DBAA-22BD-4679-AF72-9EB800CB024D', '37A989E2-9D1F-470C-A59E-9EB1008A97A5', '8E5E3504-875A-4313-91A9-9EB500C6295C', '091B8442-B7B0-40FA-B517-9EB00068A390', 1);

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
'FDF0DBAA-22BD-4679-AF72-9EB800CB024D' FKProcess, PK FKOperation, FKDefaultLevel FKLevel
from bigbang.tblPNOperations
where FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5'  and (OpName like 'Manage%' or OpName like 'Create%');

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from amartins.tblPNProcesses p inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='37A989E2-9D1F-470C-A59E-9EB1008A97A5';

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

insert into amartins.tblBBClients (PK, ClientName, ClientNumber, Address1, Address2, FKZipCode, FiscalNumber, FKEntityType, FKEntitySubType, FKManager, FKMediator,
FKProfile, FKGroup, DateOfBirth, FKSex, FKMaritalStatus, FKProfession, FKCAE, ClientNotes, MigrationID)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
c.NOME ClientName, row_number() over (order by c.CLIENTE) ClientNumber, c.MORADA Address1, c.LOCALIDADE Address2, p.PK FKZipCode, c.NCONTRIB FiscalNumber,
CASE c.TIPO_C WHEN 'I' THEN '462096E4-68A2-408A-963A-9EE600C9556A' WHEN 'E' THEN 'C5B4F500-BB57-4BFD-8248-9EE600C95ABA' ELSE '4098CF7A-B5EE-4C3F-973F-9EE600C961AA' END FKEntityType,
CASE c.TIPO_C WHEN 'C' THEN '5C7A0424-126B-467B-977A-9EE600CC13A4' ELSE NULL END FKEntitySubType,
u.FKUser FKManager, m.PK FKMediator,
CASE c.ClienteVIP WHEN 1 THEN '63114D11-6087-4EFE-9A7E-9EE600BE52DA' ELSE '9F871430-9BBC-449F-B125-9EE600BE5A9A' END FKProfile,
g.PK FKGroup, c.DataNascimento DateOfBirth, s.PK FKSex, t.PK FKMaritalStatus, f.PK FKProfession, x.PK FKCAE, CAST(c.OBSERV AS VARCHAR(250)) ClientNotes,
c.CLIENTE MigrationID
from amartins..empresa.cliente c
left outer join bigbang.tblPostalCodes p on p.PostalCode=CAST(c.CODPOSTAL AS VARCHAR(20)) COLLATE DATABASE_DEFAULT
left outer join bigbang.tblUser2 u on u.MigrationID=c.GESTORCLI
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
'100E701A-EDC5-4D9C-A221-9F09013D7954' FKScript, PK FKData, FKManager FKManager, 1 IsRunning
from amartins.tblBBClients;

update amartins.tblBBClients set FKProcess=p.PK
from amartins.tblBBClients c inner join amartins.tblPNProcesses p on p.FKData=c.PK;

insert into amartins.tblPNSteps (PK, FKProcess, FKOperation, FKLevel)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, o.PK FKOperation, o.FKDefaultLevel FKLevel
from amartins.tblPNProcesses p, bigbang.tblPNOperations o
where p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954' and o.PK in ('A9A8F4ED-74C2-473C-873C-9F0901435C1C', 'A99DCEF5-91BA-4CFA-9E70-9F090146FAE4');

insert into amartins.tblPNNodes (PK, FKProcess, FKController, NodeCount)
select CAST(CAST(NEWID() AS BINARY(10)) + CAST(GETDATE() AS BINARY(6)) AS UNIQUEIDENTIFIER) PK,
p.PK FKProcess, c.PK FKController, c.StartCount NodeCount
from amartins.tblPNProcesses p inner join bigbang.tblPNControllers c on c.FKScript=p.FKScript
where p.FKScript='100E701A-EDC5-4D9C-A221-9F09013D7954';
