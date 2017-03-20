-- Create tables
CREATE TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKInsurerRequestType] [uniqueidentifier] NULL,
	[RequestDate] [datetime] NULL,
	[AcceptanceDate] [datetime] NULL,
	[BConformity] [bit] NULL,
	[ResendDate] [datetime] NULL,
	[ClarificationDate] [datetime] NULL,
	[FKSubCasualty] [uniqueidentifier] NOT NULL
);

CREATE TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKInsurerRequestType] [uniqueidentifier] NULL,
	[RequestDate] [datetime] NULL,
	[AcceptanceDate] [datetime] NULL,
	[BConformity] [bit] NULL,
	[ResendDate] [datetime] NULL,
	[ClarificationDate] [datetime] NULL,
	[FKSubCasualty] [uniqueidentifier] NOT NULL
);

CREATE TABLE [bigbang].[tblInsurerRequestType]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[RequestType] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL
) ;

CREATE TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKInsurerRequestType] [uniqueidentifier] NULL,
	[RequestDate] [datetime] NULL,
	[AcceptanceDate] [datetime] NULL,
	[BConformity] [bit] NULL,
	[ResendDate] [datetime] NULL,
	[ClarificationDate] [datetime] NULL,
	[FKSubCasualty] [uniqueidentifier] NOT NULL
);

-- Alter tables
ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__47CFF1C7] DEFAULT ('90a96896-f729-435c-a1c2-a73501271825') FOR [FKType];

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__506537C8] DEFAULT ('90a96896-f729-435c-a1c2-a73501271825') FOR [FKType];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__58FA7DC9] DEFAULT ('90a96896-f729-435c-a1c2-a73501271825') FOR [FKType];

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSCr__45E7A955] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSCr__4E7CEF56] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSCr__57123557] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSUp__46DBCD8E] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSUp__4F71138F] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSUp__58065990] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure__FKTyp__402ECFFF] DEFAULT ('525dae0e-6426-4173-a7b8-a73501249c95') FOR [FKType];

ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure___TSCr__3E46878D] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure___TSUp__3F3AABC6] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [PK__tblBBSub__3215078743FF60E3] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [PK__tblBBSub__321507874C94A6E4] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [PK__tblBBSub__321507875529ECE5] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [PK__tblInsur__321507873C5E3F1B] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__49B83A39] FOREIGN KEY
(
	[FKInsurerRequestType]
)
REFERENCES [bigbang].[tblInsurerRequestType]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__524D803A] FOREIGN KEY
(
	[FKInsurerRequestType]
)
REFERENCES [bigbang].[tblInsurerRequestType]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__5AE2C63B] FOREIGN KEY
(
	[FKInsurerRequestType]
)
REFERENCES [bigbang].[tblInsurerRequestType]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__5BD6EA74] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbcomercial].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__5CCB0EAD] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbleiria].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__5DBF32E6] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [credite_egs].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__48C41600] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__51595C01] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__59EEA202] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bigbang].[tblInsurerRequestType] WITH CHECK ADD CONSTRAINT [FK__tblInsure__FKTyp__4122F438] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

-- Inserts
/*-- -Insert(s): [madds].[tblDDLLogs] */
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('ba57ebe6-641e-4709-8e6d-2120ff12bab4','2017-03-16T12:07:21.223','2017-03-16T12:07:21.223','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'ALTER TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ADD [FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblBBSubCasualties] ([PK])');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('101f20f4-6d98-42ce-a203-418c762a2b52','2017-03-14T10:21:36.063','2017-03-14T10:21:36.063','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''90a96896-f729-435c-a1c2-a73501271825'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKInsurerRequestType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblInsurerRequestType] ([PK]), [RequestDate] datetime, [AcceptanceDate] datetime, [BConformity] bit, [ResendDate] datetime, [ClarificationDate] datetime)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('6bc8c24e-2aa8-45e7-b8ce-4b2d48d7336b','2017-03-14T10:21:30.270','2017-03-14T10:21:30.270','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'CREATE TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''90a96896-f729-435c-a1c2-a73501271825'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKInsurerRequestType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblInsurerRequestType] ([PK]), [RequestDate] datetime, [AcceptanceDate] datetime, [BConformity] bit, [ResendDate] datetime, [ClarificationDate] datetime)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('53936a82-5ec9-43cf-9de8-863e895bf4e2','2017-03-16T12:07:21.210','2017-03-16T12:07:21.210','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbcomercial].[tblBBSubCasualtyInsurerRequest] ADD [FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbcomercial].[tblBBSubCasualties] ([PK])');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('4b288b7c-9040-40a7-9a74-c8cccadd09c6','2017-03-14T10:20:21.427','2017-03-14T10:20:21.427','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblInsurerRequestType] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''525dae0e-6426-4173-a7b8-a73501249c95'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [RequestType] nvarchar(250) NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('419a30b3-b057-424c-8a99-d9d9857f30e2','2017-03-14T10:20:36.863','2017-03-14T10:20:36.863','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'CREATE TABLE [credite_egs].[tblBBSubCasualtyInsurerRequest] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''90a96896-f729-435c-a1c2-a73501271825'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKInsurerRequestType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblInsurerRequestType] ([PK]), [RequestDate] datetime, [AcceptanceDate] datetime, [BConformity] bit, [ResendDate] datetime, [ClarificationDate] datetime)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('72e7281a-96f9-4125-871c-fb4745f68240','2017-03-16T12:07:21.220','2017-03-16T12:07:21.220','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'ALTER TABLE [bbleiria].[tblBBSubCasualtyInsurerRequest] ADD [FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbleiria].[tblBBSubCasualties] ([PK])');

/*-- -Insert(s): [madds].[tblEntities] */
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('7128ab07-dc2e-4806-92f3-a5e600bd1012','2016-04-12T11:28:21.397','2016-04-12T11:28:21.397','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','c42eef38-c2b4-45be-ae86-a5e600bad859');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('68de3e0c-6809-41d8-8b32-a73600aa62da','2017-03-14T10:20:21.423','2017-03-14T10:20:21.423','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','525dae0e-6426-4173-a7b8-a73501249c95');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('51769138-21e5-453a-8b4d-a73600aa74f2','2017-03-14T10:20:36.860','2017-03-14T10:20:36.860','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','a1cc31a3-e471-4568-b5fc-9e15008e98c9','90a96896-f729-435c-a1c2-a73501271825');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('9510b125-58f0-4420-af8c-a73600aab387','2017-03-14T10:21:30.263','2017-03-14T10:21:30.263','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','337a0a77-8942-43aa-9ec0-a47001171a77','90a96896-f729-435c-a1c2-a73501271825');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('c1ded980-4025-480b-9edb-a73600aaba51','2017-03-14T10:21:36.057','2017-03-14T10:21:36.057','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','3ede450f-17ef-4d91-ac72-a4c3012591f1','90a96896-f729-435c-a1c2-a73501271825');

/*-- -Insert(s): [madds].[tblFormFields] */
INSERT INTO [madds].[tblFormFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[FKFieldType],[FLabel],[FRow],[FCol],[ObjMemberNum],[ObjRef],[FWidth],[FHeight],[FKQuery],[FKSearchForm],[ParamTag],[DefaultText],[DefaultValue]) VALUES('2d93eb54-4dbd-4cc5-9c29-a73600c509b0','2017-03-14T11:57:23.590','2017-03-14T11:57:23.590','1a272874-d1c1-4ce3-8a93-1912137fc607','d3779e26-2e7c-4641-a852-a73600c4d1e5','41f8ff38-808b-41d3-b88a-8dca5384a637',N'Type',1,1,1,null,2,1,null,null,null,null,null);

/*-- -Insert(s): [madds].[tblForms] */
INSERT INTO [madds].[tblForms] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FName],[FKEdited],[FKQueryResults],[FKApplication],[ClassName]) VALUES('d3779e26-2e7c-4641-a852-a73600c4d1e5','2017-03-14T11:56:35.977','2017-03-14T12:04:25.547','7fe57d40-7e89-4a3e-bee7-defce1af4a50',N'Insurer Request Types','525dae0e-6426-4173-a7b8-a73501249c95','04d4a80c-87ca-4262-beb1-a73600c6a8ef',null,null);

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bf82a958-6acb-4f04-93f4-a5e600bc00fe','2016-04-12T11:24:30.083','2016-04-12T11:24:30.083','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','c42eef38-c2b4-45be-ae86-a5e600bad859',1,N'Input',N'The type as it exists in the receipts'' file','94ab0a78-25a1-11da-91c2-000b6abc6ae9',1,null,0,1,N'TInput',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a0d3cdf8-c491-4772-a4ef-a5e600bc78f0','2016-04-12T11:26:12.430','2016-04-12T11:26:12.430','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','c42eef38-c2b4-45be-ae86-a5e600bad859',2,N'Receipt Type',N'The type of BB''s receipt associated with th input','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'afe7cc47-b44f-442d-8cf4-9f8a00db2637',0,1,N'FKReceiptType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('80a6d786-4786-446a-b03b-a7350124edeb','2017-03-13T17:46:31.437','2017-03-13T17:46:31.437','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','525dae0e-6426-4173-a7b8-a73501249c95',1,N'Type',N'The type of request','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,0,N'RequestType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('7f05f1c9-6e07-4f5e-aca1-a73600a598c1','2017-03-14T10:02:55.163','2017-03-14T10:02:55.163','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',1,N'Request Type',N'The type of the insurer request','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'525dae0e-6426-4173-a7b8-a73501249c95',1,0,N'FKInsurerRequestType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('8138e643-2ea7-4429-a6b7-a73600a680aa','2017-03-14T10:06:13.060','2017-03-14T10:06:13.060','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',2,N'Request Date',N'The date on which the request was made','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'RequestDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('c49ed137-847b-4912-888f-a73600a6ab62','2017-03-14T10:06:49.510','2017-03-14T10:06:49.510','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',3,N'Acceptance Date',N'The date on which the request was accepted','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'AcceptanceDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('4f74c91f-93ba-4581-b52b-a73600a70dd7','2017-03-14T10:08:13.527','2017-03-14T10:08:13.527','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',4,N'Conformity',N'Whether there is conformity with the request','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BConformity',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('41e307ef-4268-4417-bac8-a73600a759df','2017-03-14T10:09:18.407','2017-03-14T10:09:18.407','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',5,N'Resend Date',N'The date on which the request was resent to the insurer, in case there is conformity ','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'ResendDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a37c2316-37a2-45db-b027-a73600a7a984','2017-03-14T10:10:26.363','2017-03-14T10:10:26.363','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',6,N'Clarification Date',N'The date on which a clarification was requested to the insurer, in case there is no conformity','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'ClarificationDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('51de58a4-c91c-4a89-95b0-a73800c7c93a','2017-03-16T12:07:23.943','2017-03-16T12:07:23.943','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',7,N'Sub Casualty',N'The sub casualty this item belongs to','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'d5fd2d1b-59fb-4171-961a-a02e0121c81b',0,0,N'FKSubCasualty',null);

/*-- -Insert(s): [madds].[tblObjects] */
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('525dae0e-6426-4173-a7b8-a73501249c95','2017-03-13T17:45:22.033','2017-03-13T17:46:38.240','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Insurer Request Type',N'The class which tipifies the types of requests an Insurer may ask.',N'tblInsurerRequestType',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('90a96896-f729-435c-a1c2-a73501271825','2017-03-13T17:54:24.350','2017-03-16T12:07:26.397','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Sub Casualty Insurer Request',N'Stores the details about an insurer request, associated to a sub-casualty',N'tblBBSubCasualtyInsurerRequest',N'com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest','4cd3738e-2e2e-4789-ad81-9e160090f723');

/*-- -Insert(s): [madds].[tblQueryDefs] */
INSERT INTO [madds].[tblQueryDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKDriver],[IsDefault],[DefParamName],[DefParamType],[ParamAppliesTo],[FKEditorView],[IsReadOnly],[FKReport],[Reference],[CanCreate],[CanEditRows],[CanDelete],[FKQueryType]) VALUES('04d4a80c-87ca-4262-beb1-a73600c6a8ef','2017-03-14T12:03:17.933','2017-03-14T12:03:47.277','957cb94d-7968-4921-8797-43af37ab98d9','525dae0e-6426-4173-a7b8-a73501249c95',1,null,null,null,'0a31cfc5-ee25-4fb1-a9a2-a73600c551dd',0,null,null,1,1,1,'9d20405a-9968-4807-9980-7a6166a72283');

/*-- -Insert(s): [madds].[tblQueryFields] */
INSERT INTO [madds].[tblQueryFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrder],[QFName],[QFSQL],[QFWidth]) VALUES('36d65624-5863-4b16-8f6a-a73600c6c4ce','2017-03-14T12:03:41.713','2017-03-14T12:03:41.713','942704b9-8fef-40c3-8020-3911efb3f094','04d4a80c-87ca-4262-beb1-a73600c6a8ef',1,N'Type',N'[:Type]',200);

/*-- -Insert(s): [madds].[tblViewDefs] */
INSERT INTO [madds].[tblViewDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[IsDefault],[Reference]) VALUES('0a31cfc5-ee25-4fb1-a9a2-a73600c551dd','2017-03-14T11:58:25.180','2017-03-14T11:58:57.863','cb6d759d-0333-4830-914c-b7d0b9b728dd','525dae0e-6426-4173-a7b8-a73501249c95',1,null);

/*-- -Insert(s): [madds].[tblViewTabs] */
INSERT INTO [madds].[tblViewTabs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrd],[VTName],[FKVTType],[FKForm],[FKQuery],[FKReport]) VALUES('43d8c2e6-36d3-4965-8809-a73600c57647','2017-03-14T11:58:56.257','2017-03-14T11:58:56.257','e204d946-661a-4cf3-ba7a-31f55b95caf8','0a31cfc5-ee25-4fb1-a9a2-a73600c551dd',1,N'General','1febe70f-5461-48ee-b3a4-191bc47db3c5','d3779e26-2e7c-4641-a852-a73600c4d1e5',null,null);