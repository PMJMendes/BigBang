CREATE TABLE [credite_egs].[tblMedicalRelapses]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKFile] [uniqueidentifier] NOT NULL,
	[Label] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL,
	[RelapseDate] [datetime] NOT NULL
) ON [PRIMARY];

CREATE TABLE [bbleiria].[tblMedicalRelapses]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKFile] [uniqueidentifier] NOT NULL,
	[Label] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL,
	[RelapseDate] [datetime] NOT NULL
) ON [PRIMARY];

ALTER TABLE [bbleiria].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica__FKTyp__05630FEC] DEFAULT ('250dedb4-4f0d-475c-83ed-a83900ffa253') FOR [FKType];
ALTER TABLE [credite_egs].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica__FKTyp__7CCDC9EB] DEFAULT ('250dedb4-4f0d-475c-83ed-a83900ffa253') FOR [FKType];
ALTER TABLE [bbleiria].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSCr__037AC77A] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [credite_egs].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSCr__7AE58179] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bbleiria].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSUp__046EEBB3] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [credite_egs].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSUp__7BD9A5B2] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [bbleiria].[tblMedicalRelapses] ADD CONSTRAINT [PK__tblMedic__3215078701927F08] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
ALTER TABLE [credite_egs].[tblMedicalRelapses] ADD CONSTRAINT [PK__tblMedic__3215078778FD3907] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
ALTER TABLE [bbleiria].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKFil__074B585E] FOREIGN KEY
(
	[FKFile]
)
REFERENCES [bbleiria].[tblMedicalFiles]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE [credite_egs].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKFil__7EB6125D] FOREIGN KEY
(
	[FKFile]
)
REFERENCES [credite_egs].[tblMedicalFiles]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE [bbleiria].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKTyp__06573425] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE [credite_egs].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKTyp__7DC1EE24] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('52e2a77f-b7fe-4e86-9fa7-7504445cc791','2017-11-28T15:36:20.920','2017-11-28T15:36:20.920','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'CREATE TABLE [credite_egs].[tblMedicalRelapses] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''250dedb4-4f0d-475c-83ed-a83900ffa253'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKFile] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblMedicalFiles] ([PK]), [Label] nvarchar(250) NOT NULL, [RelapseDate] datetime NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('3484940e-470b-4aaf-8a9d-dcfd2fa3b34c','2017-11-28T16:19:25.893','2017-11-28T16:19:25.893','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'CREATE TABLE [bbleiria].[tblMedicalRelapses] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''250dedb4-4f0d-475c-83ed-a83900ffa253'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKFile] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbleiria].[tblMedicalFiles] ([PK]), [Label] nvarchar(250) NOT NULL, [RelapseDate] datetime NOT NULL)');

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('54b951a7-bf49-4a73-9234-a83901012d02','2017-11-28T15:36:20.917','2017-11-28T15:36:20.917','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','a1cc31a3-e471-4568-b5fc-9e15008e98c9','250dedb4-4f0d-475c-83ed-a83900ffa253');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('995fa88d-49d0-4950-8262-a839010d0246','2017-11-28T16:19:25.890','2017-11-28T16:19:25.890','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','337a0a77-8942-43aa-9ec0-a47001171a77','250dedb4-4f0d-475c-83ed-a83900ffa253');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('579a9493-4c2f-4b14-82f9-a83900ffe9a3','2017-11-28T15:31:44.983','2017-11-28T15:31:44.983','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',1,N'File',N'The medical file this relapse belongs to','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'835e3722-a13b-451b-aec8-a13401125446',0,0,N'FKFile',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('68dfbbba-644d-4aeb-892e-a839010022fd','2017-11-28T15:32:33.927','2017-11-28T15:32:33.927','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',2,N'Label',N'A small descriptive label for the relapse','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,0,N'Label',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('2512de82-c3a1-4728-b8f3-a83901004b76','2017-11-28T15:33:08.457','2017-11-28T15:33:08.457','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',3,N'Date',N'The date the relapse took place','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,0,0,N'RelapseDate',null);

INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('250dedb4-4f0d-475c-83ed-a83900ffa253','2017-11-28T15:30:44.130','2017-11-28T16:15:44.540','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Medical Relapse',N'A Relapse log in a medical file',N'tblMedicalRelapses',N'com.premiumminds.BigBang.Jewel.Objects.MedicalRelapse','4cd3738e-2e2e-4789-ad81-9e160090f723');


-- Angola
CREATE TABLE [bbangola].[tblMedicalRelapses]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKFile] [uniqueidentifier] NOT NULL,
	[Label] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL,
	[RelapseDate] [datetime] NOT NULL
) ON [PRIMARY];

ALTER TABLE [bbangola].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica__FKTyp__05630FEC] DEFAULT ('250dedb4-4f0d-475c-83ed-a83900ffa253') FOR [FKType];
ALTER TABLE [bbangola].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSCr__037AC77A] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bbangola].[tblMedicalRelapses] ADD CONSTRAINT [DF__tblMedica___TSUp__046EEBB3] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [bbangola].[tblMedicalRelapses] ADD CONSTRAINT [PK__tblMedic__3215078701927F08] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
ALTER TABLE [bbangola].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKFil__074B585E] FOREIGN KEY
(
	[FKFile]
)
REFERENCES [[bbangola]].[tblMedicalFiles]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE [bbangola].[tblMedicalRelapses] WITH CHECK ADD CONSTRAINT [FK__tblMedica__FKTyp__06573425] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('52e2a77f-b7fe-4e86-9fa7-7504445cc791','2017-11-28T15:36:20.920','2017-11-28T15:36:20.920','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'CREATE TABLE [credite_egs].[tblMedicalRelapses] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''250dedb4-4f0d-475c-83ed-a83900ffa253'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKFile] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblMedicalFiles] ([PK]), [Label] nvarchar(250) NOT NULL, [RelapseDate] datetime NOT NULL)');

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('54b951a7-bf49-4a73-9234-a83901012d02','2017-11-28T15:36:20.917','2017-11-28T15:36:20.917','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','980B1995-5522-4EAB-8E35-A46E00C39917','250dedb4-4f0d-475c-83ed-a83900ffa253');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('579a9493-4c2f-4b14-82f9-a83900ffe9a3','2017-11-28T15:31:44.983','2017-11-28T15:31:44.983','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',1,N'File',N'The medical file this relapse belongs to','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'835e3722-a13b-451b-aec8-a13401125446',0,0,N'FKFile',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('68dfbbba-644d-4aeb-892e-a839010022fd','2017-11-28T15:32:33.927','2017-11-28T15:32:33.927','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',2,N'Label',N'A small descriptive label for the relapse','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,0,N'Label',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('2512de82-c3a1-4728-b8f3-a83901004b76','2017-11-28T15:33:08.457','2017-11-28T15:33:08.457','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','250dedb4-4f0d-475c-83ed-a83900ffa253',3,N'Date',N'The date the relapse took place','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,0,0,N'RelapseDate',null);

INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('250dedb4-4f0d-475c-83ed-a83900ffa253','2017-11-28T15:30:44.130','2017-11-28T16:15:44.540','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Medical Relapse',N'A Relapse log in a medical file',N'tblMedicalRelapses',N'com.premiumminds.BigBang.Jewel.Objects.MedicalRelapse','4cd3738e-2e2e-4789-ad81-9e160090f723');
