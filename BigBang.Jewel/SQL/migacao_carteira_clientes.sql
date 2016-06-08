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