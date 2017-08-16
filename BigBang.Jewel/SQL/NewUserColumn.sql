ALTER TABLE [bigbang].[tblUser2] ADD [BChangeInsurer] [bit] NULL;

/*-- -Insert(s): [madds].[tblDDLLogs] */
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('49b5b677-9943-4769-b8e0-1680d9cd0214','2017-08-16T11:36:24.663','2017-08-16T11:36:24.663','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'ALTER TABLE [bigbang].[tblUser2] ADD [BChangeInsurer] bit');

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('4fbd398a-85bf-43eb-87c5-a7d100bf59e6','2017-08-16T11:36:41.340','2017-08-16T11:36:41.340','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','87c6a1db-2381-47e4-ade5-9eb800836ff7',9,N'Change Insurer',N'Whether the user may change a policy''s insurer','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BChangeInsurer',null);

/*-- -Update(s): [madds].[tblObjects] */
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-08-16T11:36:44.243' WHERE [PK]='87c6a1db-2381-47e4-ade5-9eb800836ff7'