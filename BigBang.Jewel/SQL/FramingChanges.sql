-- Changes in the notes columns' sizes for casualties and sub-casualties
ALTER TABLE credite_egs.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbleiria.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);

ALTER TABLE credite_egs.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbleiria.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);

ALTER TABLE credite_egs.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbleiria.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);

ALTER TABLE credite_egs.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbleiria.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbcomercial.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);

UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:24:05.130',[Size]=4000 WHERE [PK]='21870119-b746-43a8-b5b5-a02e011391cf';
UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:10:14.327',[Size]=4000 WHERE [PK]='771ec1f4-3de7-495f-b3b3-a035011b8957';
UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:10:27.473',[Size]=4000 WHERE [PK]='455cf1c1-c0e1-4926-8e38-a035011bbafd';

-- New table creation
CREATE TABLE [bbcomercial].[tblBBSubCasualtyHeadings]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKSubFraming] [uniqueidentifier] NOT NULL,
	[BaseSalary] [decimal](16,2) NULL,
	[FeedAllowance] [decimal](16,2) NULL,
	[OtherFees12] [decimal](16,2) NULL,
	[OtherFees14] [decimal](16,2) NULL
);

CREATE TABLE [bbleiria].[tblBBSubCasualtyHeadings]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKSubFraming] [uniqueidentifier] NOT NULL,
	[BaseSalary] [decimal](16,2) NULL,
	[FeedAllowance] [decimal](16,2) NULL,
	[OtherFees12] [decimal](16,2) NULL,
	[OtherFees14] [decimal](16,2) NULL
);

CREATE TABLE [credite_egs].[tblBBSubCasualtyHeadings]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKSubFraming] [uniqueidentifier] NOT NULL,
	[BaseSalary] [decimal](16,2) NULL,
	[FeedAllowance] [decimal](16,2) NULL,
	[OtherFees12] [decimal](16,2) NULL,
	[OtherFees14] [decimal](16,2) NULL
);

INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('a3e737a5-5474-41d4-842e-a77e00af9e40','2017-05-25T10:39:24.387','2017-05-25T10:58:00.767','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Sub Casualty Framing Headings',N'This table stores the financial headings of the sub casualty framing for work accidents',N'tblBBSubCasualtyHeadings',N'com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingHeadings','4cd3738e-2e2e-4789-ad81-9e160090f723');
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('6dbdec4d-6a94-4cae-91a2-a77e00b16cea','2017-05-25T10:45:59.197','2017-05-25T10:45:59.197','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','a3e737a5-5474-41d4-842e-a77e00af9e40',1,N'Sub Framing',N'Key of the sub casualty''s framing','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'bee4608e-3f2e-4afb-a996-a745010a87ab',0,1,N'FKSubFraming',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('52b30690-e5a6-471b-94a7-a77e00b26d4d','2017-05-25T10:49:37.977','2017-05-25T10:49:37.977','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','a3e737a5-5474-41d4-842e-a77e00af9e40',2,N'Base Salary',N'The base salary''s value','66af024b-2e89-4e12-93f4-87e2e15120d3',16,null,1,0,N'BaseSalary',2);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('e7d4ef16-a8b8-4d6a-91fe-a77e00b2beb5','2017-05-25T10:50:47.447','2017-05-25T10:50:47.447','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','a3e737a5-5474-41d4-842e-a77e00af9e40',3,N'Feed Allowance',N'The Feed Allowance''s value','66af024b-2e89-4e12-93f4-87e2e15120d3',16,null,1,0,N'FeedAllowance',2);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('de19db24-3c8d-4190-befd-a77e00b3e7fc','2017-05-25T10:55:01.113','2017-05-25T10:55:01.113','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','a3e737a5-5474-41d4-842e-a77e00af9e40',4,N'Other Fees 12',N'The value for the other fees of type 12','66af024b-2e89-4e12-93f4-87e2e15120d3',16,null,1,0,N'OtherFees12',2);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('d6089d35-6cf6-41b3-8bb7-a77e00b4b61e','2017-05-25T10:57:57.017','2017-05-25T10:57:57.017','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','a3e737a5-5474-41d4-842e-a77e00af9e40',5,N'Other Fees 14',N'The value for the other fees of type 14','66af024b-2e89-4e12-93f4-87e2e15120d3',16,null,1,0,N'OtherFees14',2);

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('193178a5-c436-402d-a6d9-a77e00b67296','2017-05-25T11:04:16.290','2017-05-25T11:04:16.290','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','a1cc31a3-e471-4568-b5fc-9e15008e98c9','a3e737a5-5474-41d4-842e-a77e00af9e40');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('fa0d8835-949e-4021-9f76-a77e00b67bb0','2017-05-25T11:04:24.057','2017-05-25T11:04:24.057','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','337a0a77-8942-43aa-9ec0-a47001171a77','a3e737a5-5474-41d4-842e-a77e00af9e40');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('b61ce961-dfad-4d66-b227-a77e00b6813e','2017-05-25T11:04:28.797','2017-05-25T11:04:28.797','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','3ede450f-17ef-4d91-ac72-a4c3012591f1','a3e737a5-5474-41d4-842e-a77e00af9e40');

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('2010f57d-2afc-49cb-b90e-128736d0d79f','2017-05-25T11:04:24.060','2017-05-25T11:04:24.060','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'CREATE TABLE [bbleiria].[tblBBSubCasualtyHeadings] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''a3e737a5-5474-41d4-842e-a77e00af9e40'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [bbleiria].[tblBBSubCasualtyFraming] ([PK]), [BaseSalary] decimal(16,2), [FeedAllowance] decimal(16,2), [OtherFees12] decimal(16,2), [OtherFees14] decimal(16,2))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('e8ac9fc7-505b-47f2-8b3b-4d93ce210dff','2017-05-25T11:04:16.290','2017-05-25T11:04:16.290','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'CREATE TABLE [credite_egs].[tblBBSubCasualtyHeadings] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''a3e737a5-5474-41d4-842e-a77e00af9e40'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblBBSubCasualtyFraming] ([PK]), [BaseSalary] decimal(16,2), [FeedAllowance] decimal(16,2), [OtherFees12] decimal(16,2), [OtherFees14] decimal(16,2))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('6a1e9e53-d5de-4418-89de-63ce5157707a','2017-05-25T11:04:28.800','2017-05-25T11:04:28.800','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''a3e737a5-5474-41d4-842e-a77e00af9e40'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [bbcomercial].[tblBBSubCasualtyFraming] ([PK]), [BaseSalary] decimal(16,2), [FeedAllowance] decimal(16,2), [OtherFees12] decimal(16,2), [OtherFees14] decimal(16,2))');

ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__0A5CCF33] DEFAULT ('a3e737a5-5474-41d4-842e-a77e00af9e40') FOR [FKType];
ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__15CE81DF] DEFAULT ('a3e737a5-5474-41d4-842e-a77e00af9e40') FOR [FKType];
ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__7EEB1C87] DEFAULT ('a3e737a5-5474-41d4-842e-a77e00af9e40') FOR [FKType];
ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSCr__087486C1] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSCr__13E6396D] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSCr__7D02D415] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSUp__0968AAFA] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSUp__14DA5DA6] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSUp__7DF6F84E] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [PK__tblBBSub__3215078703AFD1A4] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [PK__tblBBSub__321507870F218450] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];

ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [PK__tblBBSub__32150787783E1EF8] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];

ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [UQ__tblBBSub__8E5E10D2068C3E4F] UNIQUE NONCLUSTERED
(
	[FKSubFraming] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [UQ__tblBBSub__8E5E10D211FDF0FB] UNIQUE NONCLUSTERED
(
	[FKSubFraming] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY];

ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [UQ__tblBBSub__8E5E10D27B1A8BA3] UNIQUE NONCLUSTERED
(
	[FKSubFraming] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY];

ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__00D364F9] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [credite_egs].[tblBBSubCasualtyFraming]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__0C4517A5] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbleiria].[tblBBSubCasualtyFraming]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__17B6CA51] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbcomercial].[tblBBSubCasualtyFraming]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbleiria].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__0B50F36C] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbcomercial].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__16C2A618] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [credite_egs].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__7FDF40C0] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;




