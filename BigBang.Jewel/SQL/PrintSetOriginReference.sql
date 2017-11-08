ALTER TABLE [bbleiria].[tblBBPrintSets] ADD [FKOwner] [uniqueidentifier] NULL;
ALTER TABLE [credite_egs].[tblBBPrintSets] ADD [FKOwner] [uniqueidentifier] NULL;
ALTER TABLE [bbcomercial].[tblBBPrintSets] ADD [FKOwner] [uniqueidentifier] NULL;
ALTER TABLE [bbcomercial].[tblBBPrintSets] ADD [FKOwnerType] [uniqueidentifier] NULL;
ALTER TABLE [credite_egs].[tblBBPrintSets] ADD [FKOwnerType] [uniqueidentifier] NULL;
ALTER TABLE [bbleiria].[tblBBPrintSets] ADD [FKOwnerType] [uniqueidentifier] NULL;
ALTER TABLE [bbcomercial].[tblBBPrintSets] WITH CHECK ADD CONSTRAINT [FK__tblBBPrin__FKOwn__743883EA] FOREIGN KEY
(
	[FKOwnerType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);
ALTER TABLE [bbleiria].[tblBBPrintSets] WITH CHECK ADD CONSTRAINT [FK__tblBBPrin__FKOwn__752CA823] FOREIGN KEY
(
	[FKOwnerType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);
ALTER TABLE [credite_egs].[tblBBPrintSets] WITH CHECK ADD CONSTRAINT [FK__tblBBPrin__FKOwn__7620CC5C] FOREIGN KEY
(
	[FKOwnerType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a69cd363-7fc5-4a67-a536-a823011560b3','2017-11-06T16:49:54.117','2017-11-06T16:49:54.117','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','febf3c68-0891-4b4d-ad90-a021010a219f',5,N'Owner Type',N'The type of the entity that owns the set','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'94ab0a6b-25a1-11da-91c2-000b6abc6ae9',1,0,N'FKOwnerType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('cbfa7327-746a-4122-8a39-a8230115b5a0','2017-11-06T16:51:06.570','2017-11-06T17:34:04.553','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','febf3c68-0891-4b4d-ad90-a021010a219f',6,N'Owner',N'The entity that owns this sset. Type is given by Owner Type.','84591c55-f120-4ead-932b-f670cef90dbb',null,null,1,0,N'FKOwner',null);

/*-- -Update(s): [madds].[tblObjects] */
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-06T17:34:08.370' WHERE [PK]='febf3c68-0891-4b4d-ad90-a021010a219f'


-- Angola
ALTER TABLE [bbangola].[tblBBPrintSets] ADD [FKOwner] [uniqueidentifier] NULL;
ALTER TABLE [bbangola].[tblBBPrintSets] ADD [FKOwnerType] [uniqueidentifier] NULL;
ALTER TABLE [bbangola].[tblBBPrintSets] WITH CHECK ADD CONSTRAINT [FK__tblBBPrin__FKOwn__743883EA] FOREIGN KEY
(
	[FKOwnerType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a69cd363-7fc5-4a67-a536-a823011560b3','2017-11-06T16:49:54.117','2017-11-06T16:49:54.117','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','febf3c68-0891-4b4d-ad90-a021010a219f',5,N'Owner Type',N'The type of the entity that owns the set','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'94ab0a6b-25a1-11da-91c2-000b6abc6ae9',1,0,N'FKOwnerType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('cbfa7327-746a-4122-8a39-a8230115b5a0','2017-11-06T16:51:06.570','2017-11-06T17:34:04.553','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','febf3c68-0891-4b4d-ad90-a021010a219f',6,N'Owner',N'The entity that owns this sset. Type is given by Owner Type.','84591c55-f120-4ead-932b-f670cef90dbb',null,null,1,0,N'FKOwner',null);

/*-- -Update(s): [madds].[tblObjects] */
UPDATE [madds].[tblObjects] SET [_TSUpdate]='2017-11-06T17:34:08.370' WHERE [PK]='febf3c68-0891-4b4d-ad90-a021010a219f'