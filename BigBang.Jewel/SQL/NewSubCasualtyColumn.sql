-- new columns
ALTER TABLE [credite_egs].[tblBBSubCasualties] ADD [BTotalLoss] [bit] NULL;
ALTER TABLE [bbleiria].[tblBBSubCasualties] ADD [BTotalLoss] [bit] NULL;
ALTER TABLE [bbcomercial].[tblBBSubCasualties] ADD [BTotalLoss] [bit] NULL

-- madds
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('c16beb8f-58db-4169-81e9-56736aa93a61','2017-10-11T10:48:49.163','2017-10-11T10:48:49.163','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'ALTER TABLE [bbleiria].[tblBBSubCasualties] ADD [BTotalLoss] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('6a54408d-91ca-4382-b1ef-8d23e9411d5f','2017-10-11T10:48:53.383','2017-10-11T10:48:53.383','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBSubCasualties] ADD [BTotalLoss] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('ba8d4656-3429-42b6-8e1f-d19f8bae509a','2017-10-11T10:48:49.153','2017-10-11T10:48:49.153','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBSubCasualties] ADD [BTotalLoss] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('02bd5e8d-74d5-4371-bf0f-d09b9a048af4','2017-10-11T10:48:49.170','2017-10-11T10:48:49.170','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'ALTER TABLE [credite_egs].[tblBBSubCasualties] ADD [BTotalLoss] bit');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('acd854c5-baac-49eb-958f-a80900b2376f','2017-10-11T10:48:52.013','2017-10-11T10:48:52.013','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','d5fd2d1b-59fb-4171-961a-a02e0121c81b',20,N'Is Total Loss',N'Whether this sub-casualty represents a total loss.','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BTotalLoss',null);



-- Angola
ALTER TABLE [bbangola].[tblBBSubCasualties] ADD [BTotalLoss] [bit] NULL;
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('c16beb8f-58db-4169-81e9-56736aa93a61','2017-10-11T10:48:49.163','2017-10-11T10:48:49.163','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917' ,N'ALTER TABLE [bbangola].[tblBBSubCasualties] ADD [BTotalLoss] bit');
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('acd854c5-baac-49eb-958f-a80900b2376f','2017-10-11T10:48:52.013','2017-10-11T10:48:52.013','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','d5fd2d1b-59fb-4171-961a-a02e0121c81b',20,N'Is Total Loss',N'Whether this sub-casualty represents a total loss.','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BTotalLoss',null);