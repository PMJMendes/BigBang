-- cá
ALTER TABLE [credite_egs].[tblBBSubCasualties] ADD [BRelapse] [bit] NULL;
ALTER TABLE [bbleiria].[tblBBSubCasualties] ADD [BRelapse] [bit] NULL;

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('e4999b6b-86af-41c7-8f50-b992277532d6','2017-11-27T16:25:56.817','2017-11-27T16:25:56.817','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'ALTER TABLE [credite_egs].[tblBBSubCasualties] ADD [BRelapse] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('d1e4de41-50db-4c5c-9adc-c83c806d3a37','2017-11-27T16:25:56.813','2017-11-27T16:25:56.813','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'ALTER TABLE [bbleiria].[tblBBSubCasualties] ADD [BRelapse] bit');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('1815a770-e014-498c-b436-a838010ece59','2017-11-27T16:25:58.493','2017-11-27T16:25:58.493','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','d5fd2d1b-59fb-4171-961a-a02e0121c81b',21,N'Is  Relapse',N'Whether this sub-casualty had a relapse','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BRelapse',null);

UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-27T16:25:59.850' WHERE [PK]='d5fd2d1b-59fb-4171-961a-a02e0121c81b'

INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue]) VALUES('e8bb9b01-f9bd-4337-96bb-a8380114ad65','2017-11-27T16:47:21.100','2017-11-27T16:47:21.100','4d801865-dc6b-4a8f-8301-a032012782bf',N'Só Recaídas?',8,'40106ada-a414-4efc-9236-a6ea00c64ffb','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null,null);

-- angola
ALTER TABLE [bbangola].[tblBBSubCasualties] ADD [BRelapse] [bit] NULL;
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('e4999b6b-86af-41c7-8f50-b992277532d6','2017-11-27T16:25:56.817','2017-11-27T16:25:56.817','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [credite_egs].[tblBBSubCasualties] ADD [BRelapse] bit');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('1815a770-e014-498c-b436-a838010ece59','2017-11-27T16:25:58.493','2017-11-27T16:25:58.493','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','d5fd2d1b-59fb-4171-961a-a02e0121c81b',21,N'Is  Relapse',N'Whether this sub-casualty had a relapse','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BRelapse',null);
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-27T16:25:59.850' WHERE [PK]='d5fd2d1b-59fb-4171-961a-a02e0121c81b'

INSERT INTO [bigbang].[tblReportParams] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue]) VALUES('e8bb9b01-f9bd-4337-96bb-a8380114ad65','2017-11-27T16:47:21.100','2017-11-27T16:47:21.100','4d801865-dc6b-4a8f-8301-a032012782bf',N'Só Recaídas?',8,'40106ada-a414-4efc-9236-a6ea00c64ffb','361a1b4d-56a7-496c-a2ca-9f960154d6ca',null,null,null);
