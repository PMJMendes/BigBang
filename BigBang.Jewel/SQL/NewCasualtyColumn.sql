-- nova coluna
ALTER TABLE [bbleiria].[tblBBCasualties] ADD [BFraud] [bit] NULL;
ALTER TABLE [bbcomercial].[tblBBCasualties] ADD [BFraud] [bit] NULL;
ALTER TABLE [credite_egs].[tblBBCasualties] ADD [BFraud] [bit] NULL;

-- madds
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('831c0053-dfbc-4873-963a-11bdd53dfebc','2017-08-30T12:51:26.723','2017-08-30T12:51:26.723','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'ALTER TABLE [credite_egs].[tblBBCasualties] ADD [BFraud] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('4bfcafcf-1a73-4c1d-92c7-491f140c4144','2017-08-30T12:51:26.707','2017-08-30T12:51:26.707','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBCasualties] ADD [BFraud] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('eadf024a-1778-4048-af7c-b11448650a9c','2017-08-30T12:51:26.717','2017-08-30T12:51:26.717','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'ALTER TABLE [bbleiria].[tblBBCasualties] ADD [BFraud] bit');

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('9cf96953-ab6c-4e18-8ed3-a7df00d3e4bf','2017-08-30T12:51:28.873','2017-08-30T12:51:28.873','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','effa56df-8f3c-4361-a584-a02e00c4f0c5',9,N'Is Fraud',N'Whether this casualty was considered a fraud','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BFraud',null);

/*-- -Update(s): [madds].[tblObjects] */
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-08-30T12:51:33.627' WHERE [PK]='effa56df-8f3c-4361-a584-a02e00c4f0c5';



-- Angola
ALTER TABLE [bbangola].[tblBBCasualties] ADD [BFraud] [bit] NULL;

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('831c0053-dfbc-4873-963a-11bdd53dfebc','2017-08-30T12:51:26.723','2017-08-30T12:51:26.723','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [credite_egs].[tblBBCasualties] ADD [BFraud] bit');
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('9cf96953-ab6c-4e18-8ed3-a7df00d3e4bf','2017-08-30T12:51:28.873','2017-08-30T12:51:28.873','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','effa56df-8f3c-4361-a584-a02e00c4f0c5',9,N'Is Fraud',N'Whether this casualty was considered a fraud','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BFraud',null);
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-08-30T12:51:33.627' WHERE [PK]='effa56df-8f3c-4361-a584-a02e00c4f0c5';