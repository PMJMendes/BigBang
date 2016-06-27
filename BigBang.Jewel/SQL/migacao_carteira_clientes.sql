UPDATE [bigbang].[tblReportDefs] SET [_TSUpdate]='2016-06-03T16:19:53.970',[ReportName]=N'Relatório de Carteira de Cliente' WHERE [PK]='70a340f5-1780-42fa-8f92-35f12608bb27'

/*-- -Update(s): [bigbang].[tblReportParams] */
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-03T16:39:37.833',[NOrder]=7 WHERE [PK]='bfe36929-db96-472f-8fc1-a5ca00d08579'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-03T16:40:48.073',[NOrder]=9 WHERE [PK]='0826a9d4-a9da-4609-8c6b-a5ca00d17cd0'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-03T16:39:53.170',[NOrder]=8 WHERE [PK]='be77d789-f572-4f50-b195-a5ca00d1976b'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-03T16:40:57.180',[NOrder]=10 WHERE [PK]='747bd0a3-84da-4d09-b27b-a5ca00d1adf4'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-08T14:56:28.400',[Label]=N'Taxa Comercial',[NOrder]=11 WHERE [PK]='1fca8034-7ada-44c5-a506-a5ca00d1c48f'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-08T14:56:36.197',[Label]=N'Prémio Total Anual',[NOrder]=13 WHERE [PK]='56bd8219-586b-4f79-880f-a5ca00d1e4b1'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-03T16:21:06.663',[NOrder]=15 WHERE [PK]='ac5bb2ee-050d-4c9d-95a6-a5ca00d1f8e5'
UPDATE [bigbang].[tblReportParams] SET [_TSUpdate]='2016-06-08T15:09:41.283',[Label]=N'Segurador' WHERE [PK]='2da7ccb0-0559-437c-bcc9-a5ca00d05744'

/*-- -Insert(s): [bigbang].[tblReportParams] */
INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo]) VALUES('ac427ba0-3bfc-4566-977d-a61800c3f6b2','2016-06-01T11:53:29.100','2016-06-03T16:40:04.690','4d801865-dc6b-4a8f-8301-a032012782bf',N'Fraccionamento',6,'70a340f5-1780-42fa-8f92-35f12608bb27','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null);
INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo]) VALUES('174c168e-926c-417d-a917-a61a010c5c6c','2016-06-03T16:17:04.367','2016-06-03T16:41:36.140','4d801865-dc6b-4a8f-8301-a032012782bf',N'Franquia',12,'70a340f5-1780-42fa-8f92-35f12608bb27','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null);
INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo]) VALUES('b0558981-781a-4c4e-8655-a61a010c6ec0','2016-06-03T16:17:20.003','2016-06-03T16:21:22.850','4d801865-dc6b-4a8f-8301-a032012782bf',N'Modalidade de Pagamento',14,'70a340f5-1780-42fa-8f92-35f12608bb27','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null);
INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo]) VALUES('d1bb8772-5524-47c9-9d2c-a61a010c8da2','2016-06-03T16:17:46.357','2016-06-03T16:20:56.587','4d801865-dc6b-4a8f-8301-a032012782bf',N'Mostrar Temporários',16,'70a340f5-1780-42fa-8f92-35f12608bb27','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null);

/*-- -Insert(s): [bigbang].[tblBBTaxes] */
INSERT INTO [bigbang].[tblBBTaxes] ([PK],[_TSCreate],[_TSUpdate],[FKType],[TaxName],[FKCoverage],[FKFieldType],[Units],[DefaultValue],[BVariesByObject],[BVariesByExercise],[FKReferenceTo],[ColumnOrder],[BMandatory],[Tag],[BVisible]) VALUES('a9ba3b35-efad-477e-885a-a61f0114a7a7','2016-06-08T16:47:16.313','2016-06-08T16:47:16.313','43644146-07c4-4e63-9e87-9ec900f6eb73',N'Salário Anual','abd97644-09a1-482f-8356-9f9100e6f9a3','4d82ee91-0a9e-415e-9003-9f9601404007',N'€',null,1,0,null,0,0,'SALANU',1);
INSERT INTO [bigbang].[tblBBTaxes] ([PK],[_TSCreate],[_TSUpdate],[FKType],[TaxName],[FKCoverage],[FKFieldType],[Units],[DefaultValue],[BVariesByObject],[BVariesByExercise],[FKReferenceTo],[ColumnOrder],[BMandatory],[Tag],[BVisible]) VALUES('1bbdd544-0bc7-4854-bdba-a61f0114c963','2016-06-08T16:47:44.970','2016-06-08T16:47:44.970','43644146-07c4-4e63-9e87-9ec900f6eb73',N'Salário Anual','a1ce9acf-e258-408d-888c-9f9100e6f9a3','4d82ee91-0a9e-415e-9003-9f9601404007',N'€',null,1,0,null,0,0,'SALANU',1);

ALTER TABLE [bigbang].[tblReportParams] ADD [DefaultValue] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL

update bigbang.tblReportParams
set DefaultValue = 'true'
where FKOwner like '70A340F5-1780-42FA-8F92-35F12608BB27'
and PK not in (	'1CDE46E1-9C12-4953-95CD-31F0AC032320', 'F87D478B-92BC-4C42-A19C-F360EAD3213B', 
				'AC5BB2EE-050D-4C9D-95A6-A5CA00D1F8E5', 'D1BB8772-5524-47C9-9D2C-A61A010C8DA2'	);

/*-- -Insert(s): [madds].[tblDDLLogs] */
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('6e822957-54a3-4c47-b527-2452acef7b83','2016-06-09T12:18:36.277','2016-06-09T12:18:36.277','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'ALTER TABLE [bigbang].[tblReportParams] ADD [DefaultValue] nvarchar(250)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('d2904fb8-a97d-4831-a6ac-3cdfe6703776','2016-05-24T15:59:37.303','2016-05-24T15:59:37.303','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'ALTER TABLE [bbleiria].[tblBBDocuments] ADD [BDisplayAtPortal] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('cb592ba4-8ab9-4453-9987-47ef1e89a947','2016-05-24T15:59:38.333','2016-05-24T15:59:38.333','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBDocuments] ADD [BDisplayAtPortal] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('9e05c621-f8a1-408e-ac26-6be1cacc9314','2016-04-12T11:28:21.590','2016-04-12T11:28:21.590','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblLibertyTranslator] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''c42eef38-c2b4-45be-ae86-a5e600bad859'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [TInput] nvarchar(1) UNIQUE NOT NULL, [FKReceiptType] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [bigbang].[tblReceiptTypes] ([PK]))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('253aa042-50af-40f3-bd1e-f2656e2bf84e','2016-05-24T15:59:37.397','2016-05-24T15:59:37.397','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'ALTER TABLE [credite_egs].[tblBBDocuments] ADD [BDisplayAtPortal] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('e183c102-a260-4e7f-b7d5-f6f94ef266e2','2016-05-24T15:59:37.300','2016-05-24T15:59:37.300','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBDocuments] ADD [BDisplayAtPortal] bit');

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bcdab453-3b85-418b-b7ba-a62000cae1cf','2016-06-09T12:18:40.280','2016-06-09T12:18:40.280','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','4d801865-dc6b-4a8f-8301-a032012782bf',7,N'Default Value',N'The character sequence representing a default value for this parameter','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'DefaultValue',null);