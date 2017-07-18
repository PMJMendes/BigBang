/*
*
*		Insurer Request
*
*/
CREATE TABLE [bbangola].[tblBBSubCasualtyInsurerRequest]
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
);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__58FA7DC9] DEFAULT ('90a96896-f729-435c-a1c2-a73501271825') FOR [FKType];
ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSCr__57123557] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [DF__tblBBSubC___TSUp__58065990] DEFAULT (getdate()) FOR [_TSUpdate];
ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure__FKTyp__402ECFFF] DEFAULT ('525dae0e-6426-4173-a7b8-a73501249c95') FOR [FKType];
ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure___TSCr__3E46878D] DEFAULT (getdate()) FOR [_TSCreate];
ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [DF__tblInsure___TSUp__3F3AABC6] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD CONSTRAINT [PK__tblBBSub__321507875529ECE5] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bigbang].[tblInsurerRequestType] ADD CONSTRAINT [PK__tblInsur__321507873C5E3F1B] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__5AE2C63B] FOREIGN KEY
(
	[FKInsurerRequestType]
)
REFERENCES [bigbang].[tblInsurerRequestType]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__5BD6EA74] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbangola].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__59EEA202] FOREIGN KEY
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

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('101f20f4-6d98-42ce-a203-418c762a2b52','2017-03-14T10:21:36.063','2017-03-14T10:21:36.063','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'CREATE TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''90a96896-f729-435c-a1c2-a73501271825'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKInsurerRequestType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblInsurerRequestType] ([PK]), [RequestDate] datetime, [AcceptanceDate] datetime, [BConformity] bit, [ResendDate] datetime, [ClarificationDate] datetime)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('53936a82-5ec9-43cf-9de8-863e895bf4e2','2017-03-16T12:07:21.210','2017-03-16T12:07:21.210','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD [FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbangola].[tblBBSubCasualties] ([PK])');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('4b288b7c-9040-40a7-9a74-c8cccadd09c6','2017-03-14T10:20:21.427','2017-03-14T10:20:21.427','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblInsurerRequestType] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''525dae0e-6426-4173-a7b8-a73501249c95'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [RequestType] nvarchar(250) NOT NULL)');

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('68de3e0c-6809-41d8-8b32-a73600aa62da','2017-03-14T10:20:21.423','2017-03-14T10:20:21.423','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','525dae0e-6426-4173-a7b8-a73501249c95');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('51769138-21e5-453a-8b4d-a73600aa74f2','2017-03-14T10:20:36.860','2017-03-14T10:20:36.860','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','980B1995-5522-4EAB-8E35-A46E00C39917','90a96896-f729-435c-a1c2-a73501271825');

INSERT INTO [madds].[tblFormFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[FKFieldType],[FLabel],[FRow],[FCol],[ObjMemberNum],[ObjRef],[FWidth],[FHeight],[FKQuery],[FKSearchForm],[ParamTag],[DefaultText],[DefaultValue]) VALUES('2d93eb54-4dbd-4cc5-9c29-a73600c509b0','2017-03-14T11:57:23.590','2017-03-14T11:57:23.590','1a272874-d1c1-4ce3-8a93-1912137fc607','d3779e26-2e7c-4641-a852-a73600c4d1e5','41f8ff38-808b-41d3-b88a-8dca5384a637',N'Type',1,1,1,null,2,1,null,null,null,null,null);

INSERT INTO [madds].[tblForms] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FName],[FKEdited],[FKQueryResults],[FKApplication],[ClassName]) VALUES('d3779e26-2e7c-4641-a852-a73600c4d1e5','2017-03-14T11:56:35.977','2017-03-14T12:04:25.547','7fe57d40-7e89-4a3e-bee7-defce1af4a50',N'Insurer Request Types','525dae0e-6426-4173-a7b8-a73501249c95','04d4a80c-87ca-4262-beb1-a73600c6a8ef',null,null);

INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('525dae0e-6426-4173-a7b8-a73501249c95','2017-03-13T17:45:22.033','2017-03-13T17:46:38.240','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Insurer Request Type',N'The class which tipifies the types of requests an Insurer may ask.',N'tblInsurerRequestType',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('90a96896-f729-435c-a1c2-a73501271825','2017-03-13T17:54:24.350','2017-03-16T12:07:26.397','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Sub Casualty Insurer Request',N'Stores the details about an insurer request, associated to a sub-casualty',N'tblBBSubCasualtyInsurerRequest',N'com.premiumminds.BigBang.Jewel.Objects.SubCasualtyInsurerRequest','4cd3738e-2e2e-4789-ad81-9e160090f723');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('80a6d786-4786-446a-b03b-a7350124edeb','2017-03-13T17:46:31.437','2017-03-13T17:46:31.437','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','525dae0e-6426-4173-a7b8-a73501249c95',1,N'Type',N'The type of request','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,0,N'RequestType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('7f05f1c9-6e07-4f5e-aca1-a73600a598c1','2017-03-14T10:02:55.163','2017-03-14T10:02:55.163','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',1,N'Request Type',N'The type of the insurer request','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'525dae0e-6426-4173-a7b8-a73501249c95',1,0,N'FKInsurerRequestType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('8138e643-2ea7-4429-a6b7-a73600a680aa','2017-03-14T10:06:13.060','2017-03-14T10:06:13.060','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',2,N'Request Date',N'The date on which the request was made','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'RequestDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('c49ed137-847b-4912-888f-a73600a6ab62','2017-03-14T10:06:49.510','2017-03-14T10:06:49.510','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',3,N'Acceptance Date',N'The date on which the request was accepted','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'AcceptanceDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('4f74c91f-93ba-4581-b52b-a73600a70dd7','2017-03-14T10:08:13.527','2017-03-14T10:08:13.527','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',4,N'Conformity',N'Whether there is conformity with the request','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BConformity',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('41e307ef-4268-4417-bac8-a73600a759df','2017-03-14T10:09:18.407','2017-03-14T10:09:18.407','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',5,N'Resend Date',N'The date on which the request was resent to the insurer, in case there is conformity ','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'ResendDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a37c2316-37a2-45db-b027-a73600a7a984','2017-03-14T10:10:26.363','2017-03-14T10:10:26.363','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',6,N'Clarification Date',N'The date on which a clarification was requested to the insurer, in case there is no conformity','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'ClarificationDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('51de58a4-c91c-4a89-95b0-a73800c7c93a','2017-03-16T12:07:23.943','2017-03-16T12:07:23.943','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',7,N'Sub Casualty',N'The sub casualty this item belongs to','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'d5fd2d1b-59fb-4171-961a-a02e0121c81b',0,0,N'FKSubCasualty',null);

INSERT INTO [madds].[tblQueryDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKDriver],[IsDefault],[DefParamName],[DefParamType],[ParamAppliesTo],[FKEditorView],[IsReadOnly],[FKReport],[Reference],[CanCreate],[CanEditRows],[CanDelete],[FKQueryType]) VALUES('04d4a80c-87ca-4262-beb1-a73600c6a8ef','2017-03-14T12:03:17.933','2017-03-14T12:03:47.277','957cb94d-7968-4921-8797-43af37ab98d9','525dae0e-6426-4173-a7b8-a73501249c95',1,null,null,null,'0a31cfc5-ee25-4fb1-a9a2-a73600c551dd',0,null,null,1,1,1,'9d20405a-9968-4807-9980-7a6166a72283');

INSERT INTO [madds].[tblQueryFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrder],[QFName],[QFSQL],[QFWidth]) VALUES('36d65624-5863-4b16-8f6a-a73600c6c4ce','2017-03-14T12:03:41.713','2017-03-14T12:03:41.713','942704b9-8fef-40c3-8020-3911efb3f094','04d4a80c-87ca-4262-beb1-a73600c6a8ef',1,N'Type',N'[:Type]',200);

INSERT INTO [madds].[tblViewDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[IsDefault],[Reference]) VALUES('0a31cfc5-ee25-4fb1-a9a2-a73600c551dd','2017-03-14T11:58:25.180','2017-03-14T11:58:57.863','cb6d759d-0333-4830-914c-b7d0b9b728dd','525dae0e-6426-4173-a7b8-a73501249c95',1,null);

INSERT INTO [madds].[tblViewTabs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrd],[VTName],[FKVTType],[FKForm],[FKQuery],[FKReport]) VALUES('43d8c2e6-36d3-4965-8809-a73600c57647','2017-03-14T11:58:56.257','2017-03-14T11:58:56.257','e204d946-661a-4cf3-ba7a-31f55b95caf8','0a31cfc5-ee25-4fb1-a9a2-a73600c551dd',1,N'General','1febe70f-5461-48ee-b3a4-191bc47db3c5','d3779e26-2e7c-4641-a852-a73600c4d1e5',null,null);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD [FkClarificationReasonType] [uniqueidentifier] NULL;

CREATE TABLE [bigbang].[tblClarificationReasonType]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[ClarificationType] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL
);

ALTER TABLE [bigbang].[tblClarificationReasonType] ADD CONSTRAINT [DF__tblClarif__FKTyp__646C3075] DEFAULT ('3441ec0c-edf7-410e-a284-a73d00d1a699') FOR [FKType];

ALTER TABLE [bigbang].[tblClarificationReasonType] ADD CONSTRAINT [DF__tblClarif___TSCr__6283E803] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblClarificationReasonType] ADD CONSTRAINT [DF__tblClarif___TSUp__63780C3C] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblClarificationReasonType] ADD CONSTRAINT [PK__tblClari__32150787609B9F91] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKCla__665478E7] FOREIGN KEY
(
	[FkClarificationReasonType]
)
REFERENCES [bigbang].[tblClarificationReasonType]
(
	[PK]
);

ALTER TABLE [bigbang].[tblClarificationReasonType] WITH CHECK ADD CONSTRAINT [FK__tblClarif__FKTyp__656054AE] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

INSERT INTO [bigbang].[tblTNodes] ([PK],[_TSCreate],[_TSUpdate],[FKType],[NodeName],[FKNodeType],[FKParentNode],[NOrder],[Form],[Method],[FKNameSpace],[FKReport]) VALUES('6c99d897-5991-4769-a74b-a73600ce789d','2017-03-14T12:31:44.327','2017-03-14T12:35:47.287','463910cb-1b7e-4dbc-9451-9d7c0510e7a1',N'Insurer Request Types','473d0763-9693-44d5-97db-3cd4e98213dd','c55d6711-4072-4ea1-9e1a-9ebb00e62daa',28,'d3779e26-2e7c-4641-a852-a73600c4d1e5',null,null,null);
INSERT INTO [bigbang].[tblTNodes] ([PK],[_TSCreate],[_TSUpdate],[FKType],[NodeName],[FKNodeType],[FKParentNode],[NOrder],[Form],[Method],[FKNameSpace],[FKReport]) VALUES('ea91015a-2448-42d8-a3cf-a73d00d966b9','2017-03-21T13:11:32.047','2017-03-21T13:11:32.047','463910cb-1b7e-4dbc-9451-9d7c0510e7a1',N'Clarification Reason Type','473d0763-9693-44d5-97db-3cd4e98213dd','c55d6711-4072-4ea1-9e1a-9ebb00e62daa',29,'33a55152-a537-4d27-a170-a73d00d48b8b',null,null,null);

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('28c3768d-4618-41b2-90bb-04d8b4c32287','2017-03-21T12:46:31.730','2017-03-21T12:46:31.730','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblClarificationReasonType] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''3441ec0c-edf7-410e-a284-a73d00d1a699'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [ClarificationType] nvarchar(250) NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('07436891-fa5b-4eb0-8b84-71714593e7c5','2017-03-21T12:50:34.130','2017-03-21T12:50:34.130','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ADD [FKClarificationReasontType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblClarificationReasonType] ([PK])');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('7675abe2-3a7e-41f7-8d6f-918defb2825d','2017-03-21T17:35:26.480','2017-03-21T17:35:26.480','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyInsurerRequest] ALTER COLUMN [FKClarificationReasontType] uniqueidentifier');

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('ced7888e-3373-46d8-9c8e-a73d00d2888d','2017-03-21T12:46:31.727','2017-03-21T12:46:31.727','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','3441ec0c-edf7-410e-a284-a73d00d1a699');

INSERT INTO [madds].[tblFormFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[FKFieldType],[FLabel],[FRow],[FCol],[ObjMemberNum],[ObjRef],[FWidth],[FHeight],[FKQuery],[FKSearchForm],[ParamTag],[DefaultText],[DefaultValue]) VALUES('42ffa2c8-b61b-402b-98c0-a73d00d4b4d8','2017-03-21T12:54:26.430','2017-03-21T12:54:26.430','1a272874-d1c1-4ce3-8a93-1912137fc607','33a55152-a537-4d27-a170-a73d00d48b8b','41f8ff38-808b-41d3-b88a-8dca5384a637',N'Type',1,1,1,null,1,1,null,null,null,null,null);

INSERT INTO [madds].[tblForms] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FName],[FKEdited],[FKQueryResults],[FKApplication],[ClassName]) VALUES('33a55152-a537-4d27-a170-a73d00d48b8b','2017-03-21T12:53:51.200','2017-03-21T13:02:24.467','7fe57d40-7e89-4a3e-bee7-defce1af4a50',N'Clarification Reason Type','3441ec0c-edf7-410e-a284-a73d00d1a699','e8f7f02b-607f-4a3d-a676-a73d00d675e2',null,null);

INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('3441ec0c-edf7-410e-a284-a73d00d1a699','2017-03-21T12:43:18.920','2017-03-21T12:43:18.920','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Clarification Reason Type',N'The class which tipifies the possible reasons to ask for a clarification.',N'tblClarificationReasonType',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a5f9ad2d-0be3-4732-96c2-a73d00d1ff78','2017-03-21T12:44:34.760','2017-03-21T12:44:34.760','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','3441ec0c-edf7-410e-a284-a73d00d1a699',1,N'Type',N'The type of the clarification','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,0,N'ClarificationType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('5971e6b5-cff1-44de-8fce-a73d00d3a882','2017-03-21T12:50:37.463','2017-03-21T17:35:31.237','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','90a96896-f729-435c-a1c2-a73501271825',8,N'Clarification Type',N'The type of the clarification''s reason.','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'3441ec0c-edf7-410e-a284-a73d00d1a699',1,0,N'FKClarificationReasonType',null);

INSERT INTO [madds].[tblQueryDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKDriver],[IsDefault],[DefParamName],[DefParamType],[ParamAppliesTo],[FKEditorView],[IsReadOnly],[FKReport],[Reference],[CanCreate],[CanEditRows],[CanDelete],[FKQueryType]) VALUES('e8f7f02b-607f-4a3d-a676-a73d00d675e2','2017-03-21T13:00:49.627','2017-03-21T13:01:25.147','957cb94d-7968-4921-8797-43af37ab98d9','3441ec0c-edf7-410e-a284-a73d00d1a699',1,null,null,null,'97d4ea11-98ed-459d-9c09-a73d00d527d1',0,null,null,1,1,1,'9d20405a-9968-4807-9980-7a6166a72283');

INSERT INTO [madds].[tblQueryFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrder],[QFName],[QFSQL],[QFWidth]) VALUES('99f3d29e-e99c-4ce2-87a8-a73d00d69bad','2017-03-21T13:01:21.873','2017-03-21T13:01:21.873','942704b9-8fef-40c3-8020-3911efb3f094','e8f7f02b-607f-4a3d-a676-a73d00d675e2',1,N'Type',N'[:Type]',200);

INSERT INTO [madds].[tblViewDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[IsDefault],[Reference]) VALUES('97d4ea11-98ed-459d-9c09-a73d00d527d1','2017-03-21T12:56:04.553','2017-03-21T12:57:12.450','cb6d759d-0333-4830-914c-b7d0b9b728dd','3441ec0c-edf7-410e-a284-a73d00d1a699',1,null);

INSERT INTO [madds].[tblViewTabs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrd],[VTName],[FKVTType],[FKForm],[FKQuery],[FKReport]) VALUES('fbcd6e4e-60d0-4d84-9e2d-a73d00d574a0','2017-03-21T12:57:10.100','2017-03-21T12:57:10.100','e204d946-661a-4cf3-ba7a-31f55b95caf8','97d4ea11-98ed-459d-9c09-a73d00d527d1',1,N'General','1febe70f-5461-48ee-b3a4-191bc47db3c5','33a55152-a537-4d27-a170-a73d00d48b8b',null,null);


/*
*
*		Reports
*
*/
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportDefs]
           ([PK],[FKType],[ReportName],[NOrder],[FKObject],[FKParent],[FKTemplate],[FKTransaction],[Method],[FKReportType])
VALUES ('40106ADA-A414-4EFC-9236-A6EA00C64FFB', 'CA478376-2F58-4140-B5EE-A032010F48E5', 'Mapa de Sinistralidade', 
	3, 'EFFA56DF-8F3C-4361-A584-A02E00C4F0C5', '5E547C21-7C32-4366-9693-A192011951B5', null, null, 'printReportSinistralityMap', 
	'EFF85260-CB13-4599-8EFD-A032010C4AED');

INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('F361135E-DEBA-410C-AD1D-A6EA00C6AD04', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Cliente', 1, '40106ADA-A414-4EFC-9236-A6EA00C64FFB',
	'9F626929-A5F7-4CAF-AAB3-9F96014A8A9F', NULL, 'D535A99E-149F-44DC-A28B-9EE600B240F5', NULL);
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('577FF7BF-5C95-4C39-B360-A6EA00C6D3F3', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Ocorridos Desde', 2, '40106ADA-A414-4EFC-9236-A6EA00C64FFB',
	'426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, NULL);
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('A1BBB86C-BE1C-4289-AB45-A6EA00C6E69F', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Ocorridos Até', 3, '40106ADA-A414-4EFC-9236-A6EA00C64FFB',
	'426A1A8F-9F00-4A82-AA15-9F960156611B', NULL, NULL, NULL);
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('2163F2AC-15D0-4DC7-B390-A786011E5626', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Pago a Terceiros', 4, '40106ADA-A414-4EFC-9236-A6EA00C64FFB',
	'361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, NULL);
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('BA972C62-2B82-47BE-A67F-A786011EAE33', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Previamente Abertos', 5, '40106ADA-A414-4EFC-9236-A6EA00C64FFB',
	'361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, NULL);
	
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('C69C45CE-1A57-4BD5-9D33-A70100FF181C', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Mostrar Em Criação', 17, '70A340F5-1780-42FA-8F92-35F12608BB27',
	'361A1B4D-56A7-496C-A2CA-9F960154D6CA', NULL, NULL, NULL);
INSERT INTO [MADDSMasterDB].[bigbang].[tblReportParams]
           ([PK],[FKType],[Label],[NOrder],[FKOwner],[FKParamType],[Units],[FKReferenceTo],[DefaultValue])
VALUES
	('FD5E7CB6-5D04-4F38-B14E-A70100FF8545', '4D801865-DC6B-4A8F-8301-A032012782BF', 'Notas', 18, '70A340F5-1780-42FA-8F92-35F12608BB27',
	'49EB6448-E9E6-448A-82A0-9F9601404506', NULL, NULL, NULL);

UPDATE [bigbang].[tblReportParams]
set DefaultValue = NULL
WHERE PK IN ('174C168E-926C-417D-A917-A61A010C5C6C', 'B0558981-781A-4C4E-8655-A61A010C6EC0');


/*
*
*		Framing
*
*/
CREATE TABLE [bbangola].[tblBBSubCasualtyFraming]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKSubCasualty] [uniqueidentifier] NOT NULL,
	[AnalysisDate] [datetime] NULL,
	[BFramingDifficulty] [bit] NULL,
	[BValidPolicy] [bit] NULL,
	[ValidityNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[BGeneralExclusions] [bit] NULL,
	[GeneralExclusionsNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[BRelevantCoverage] [bit] NULL,
	[CoverageRelevancyNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[CoverageValue] [decimal](16,2) NULL,
	[BCoverageExclusions] [bit] NULL,
	[CoverageExclusionsNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[Franchise] [decimal](14,2) NULL,
	[FKDeductibleType] [uniqueidentifier] NULL,
	[FranchiseNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKInsurerEvaluation] [uniqueidentifier] NULL,
	[InsurerEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKExpertEvaluation] [uniqueidentifier] NULL,
	[ExpertEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[CoverageeNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL
);

CREATE TABLE [bbangola].[tblBBSubCasualtyFramingEntity]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[FKSubFraming] [uniqueidentifier] NOT NULL,
	[FKEntityType] [uniqueidentifier] NULL,
	[FKEvaluation] [uniqueidentifier] NULL,
	[EvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL
);

CREATE TABLE [bigbang].[tblBBEvaluationValues]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[Grade] [nvarchar](50) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL
);

CREATE TABLE [bigbang].[tblFramingEntityTypes]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[Type] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL
);

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval__FKTyp__7C43BA06] DEFAULT ('b0ab7a81-c2fd-485b-b18e-a7450125d9ea') FOR [FKType];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval___TSCr__7A5B7194] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval___TSUp__7B4F95CD] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__2FC35E0C] DEFAULT ('bee4608e-3f2e-4afb-a996-a745010a87ab') FOR [FKType];

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__3B3510B8] DEFAULT ('884b05c1-6377-4e24-9c4f-a74501281afb') FOR [FKType];

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSCr__2DDB159A] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSCr__394CC846] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSUp__2ECF39D3] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSUp__3A40EC7F] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin__FKTyp__71C62B93] DEFAULT ('7774017d-1e45-45fa-b638-a74501229762') FOR [FKType];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin___TSCr__6FDDE321] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin___TSUp__70D2075A] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [PK__tblBBEva__321507877596BC77] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD CONSTRAINT [PK__tblBBSub__321507872BF2CD28] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [PK__tblBBSub__3215078737647FD4] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [PK__tblFrami__321507876B192E04] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [UQ__tblBBEva__DF0ADB7A78732922] UNIQUE NONCLUSTERED
(
	[Grade] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF);

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [UQ__tblFrami__F9B8A48B6DF59AAF] UNIQUE NONCLUSTERED
(
	[Type] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF);

ALTER TABLE [bigbang].[tblBBEvaluationValues] WITH CHECK ADD CONSTRAINT [FK__tblBBEval__FKTyp__7D37DE3F] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__ FKSu__31ABA67E] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbangola].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKDed__329FCAB7] FOREIGN KEY
(
	[FKDeductibleType]
)
REFERENCES [bigbang].[tblBBDeductibleTypes]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEnt__3E117D63] FOREIGN KEY
(
	[FKEntityType]
)
REFERENCES [bigbang].[tblFramingEntityTypes]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEva__3F05A19C] FOREIGN KEY
(
	[FKEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKExp__34881329] FOREIGN KEY
(
	[FKExpertEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__3393EEF0] FOREIGN KEY
(
	[FKInsurerEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__3D1D592A] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbangola].[tblBBSubCasualtyFraming]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__30B78245] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbangola].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__3C2934F1] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bigbang].[tblFramingEntityTypes] WITH CHECK ADD CONSTRAINT [FK__tblFramin__FKTyp__72BA4FCC] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('63ca0695-e63b-487a-a9d4-c8f357f0ae1b','2017-04-24T15:05:48.913','2017-04-24T15:05:48.913','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [CoverageeNotes] nvarchar(250)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('0d2dc301-6c65-44fe-b6fb-8ad7b14a6607','2017-03-30T11:35:59.510','2017-03-30T11:35:59.510','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbangola].[tblBBSubCasualtyFraming ] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''bee4608e-3f2e-4afb-a996-a745010a87ab'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [ FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbangola].[tblBBSubCasualties] ([PK]), [AnalysisDate] datetime, [ BFramingDifficulty] bit, [BValidPolicy] bit, [ValidityNotes] nvarchar(250), [BGeneralExclusions] bit, [GeneralExclusionsNotes] nvarchar(250), [BRelevantCoverage] bit, [CoverageRelevancyNotes] nvarchar(250), [CoverageValue] decimal(16,2), [BCoverageExclusions] bit, [CoverageExclusionsNotes] bit, [Franchise] decimal(14,2), [FKDeductibleType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBDeductibleTypes] ([PK]), [FranchiseNotes] nvarchar(250), [FKInsurerEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [InsurerEvaluationNotes] nvarchar(250), [FKExpertEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [ExpertEvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('72c00222-6b02-4423-af7f-8db6633975ac','2017-03-30T11:29:42.163','2017-03-30T11:29:42.163','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblFramingEntityTypes] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''7774017d-1e45-45fa-b638-a74501229762'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [Type] nvarchar(250) UNIQUE NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('19d5d253-4d81-416d-bdb8-9130603f822b','2017-03-30T11:35:59.520','2017-03-30T11:35:59.520','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbangola].[tblBBSubCasualtyFramingEntities] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''884b05c1-6377-4e24-9c4f-a74501281afb'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbangola].[tblBBSubCasualtyFraming ] ([PK]), [FKEntityType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblFramingEntityTypes] ([PK]), [FKEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [EvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('8308ac77-58dc-4896-b0c6-e0b88dd72399','2017-03-30T11:29:42.170','2017-03-30T11:29:42.170','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblBBEvaluationValues] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''b0ab7a81-c2fd-485b-b18e-a7450125d9ea'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [Grade] nvarchar(50) UNIQUE NOT NULL)');

/*-- -Insert(s): [madds].[tblEntities] */
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('e848d522-3801-4b19-9998-a74600bd6eb6','2017-03-30T11:29:42.157','2017-03-30T11:29:42.157','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','7774017d-1e45-45fa-b638-a74501229762');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('96928a73-d42a-4576-8c49-a74600bd6eb7','2017-03-30T11:29:42.157','2017-03-30T11:29:42.157','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','b0ab7a81-c2fd-485b-b18e-a7450125d9ea');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('778f7662-7a02-4818-a586-a74600bf1da0','2017-03-30T11:35:49.870','2017-03-30T11:35:49.870','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','980B1995-5522-4EAB-8E35-A46E00C39917','bee4608e-3f2e-4afb-a996-a745010a87ab');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('c9f4ec9c-2f8e-49e4-91f2-a74600bf1da1','2017-03-30T11:35:49.870','2017-03-30T11:35:49.870','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','980B1995-5522-4EAB-8E35-A46E00C39917','884b05c1-6377-4e24-9c4f-a74501281afb');

/*-- -Insert(s): [madds].[tblFormFields] */
INSERT INTO [madds].[tblFormFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[FKFieldType],[FLabel],[FRow],[FCol],[ObjMemberNum],[ObjRef],[FWidth],[FHeight],[FKQuery],[FKSearchForm],[ParamTag],[DefaultText],[DefaultValue]) VALUES('d604ae45-33ef-41b1-91cc-a74600ab8b16','2017-03-30T10:24:34.220','2017-04-06T18:02:53.750','1a272874-d1c1-4ce3-8a93-1912137fc607','43862a2a-752c-4df6-af19-a74600ab31a3','41f8ff38-808b-41d3-b88a-8dca5384a637',N'Grade',1,1,1,null,2,1,null,null,null,null,null);
INSERT INTO [madds].[tblFormFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[FKFieldType],[FLabel],[FRow],[FCol],[ObjMemberNum],[ObjRef],[FWidth],[FHeight],[FKQuery],[FKSearchForm],[ParamTag],[DefaultText],[DefaultValue]) VALUES('06e19800-9107-42d2-9477-a74600b85948','2017-03-30T11:11:11.603','2017-03-30T11:11:11.603','1a272874-d1c1-4ce3-8a93-1912137fc607','28712f34-a979-42ca-b643-a74600b81d97','41f8ff38-808b-41d3-b88a-8dca5384a637',N'Type',1,1,1,null,1,1,null,null,null,null,null);

/*-- -Insert(s): [madds].[tblForms] */
INSERT INTO [madds].[tblForms] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FName],[FKEdited],[FKQueryResults],[FKApplication],[ClassName]) VALUES('43862a2a-752c-4df6-af19-a74600ab31a3','2017-03-30T10:23:17.893','2017-04-06T18:03:00.567','7fe57d40-7e89-4a3e-bee7-defce1af4a50',N'Evaluation Value','b0ab7a81-c2fd-485b-b18e-a7450125d9ea','62a4952d-3305-4b74-823b-a74600b0b297',null,null);
INSERT INTO [madds].[tblForms] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FName],[FKEdited],[FKQueryResults],[FKApplication],[ClassName]) VALUES('28712f34-a979-42ca-b643-a74600b81d97','2017-03-30T11:10:20.670','2017-03-30T11:22:11.857','7fe57d40-7e89-4a3e-bee7-defce1af4a50',N'Framing Entity Types','7774017d-1e45-45fa-b638-a74501229762','3d55df69-27f5-43ac-8754-a74600ba6c86',null,null);

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('d415499a-2cd7-4085-ab46-a745010b8014','2017-03-29T16:13:56.347','2017-03-30T12:11:53.647','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',1,N'Sub Casualty',N'A reference for the associated sub-casualty','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'d5fd2d1b-59fb-4171-961a-a02e0121c81b',0,0,N'FKSubCasualty',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('34de2b58-a6aa-4e5d-839b-a745010c7748','2017-03-29T16:17:27.293','2017-03-29T16:17:27.293','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',2,N'Analysis Date',N'The date on which the analysis was made','cc024bcd-c908-40d8-baaa-53025ef99cf4',null,null,1,0,N'AnalysisDate',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('20a9ab53-2355-40f3-bbd1-a745010dfaa9','2017-03-29T16:22:57.847','2017-03-30T12:12:04.640','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',3,N'Framing Difficulty',N'An indicator on whether there were some issues with the framing','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BFramingDifficulty',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('4415e718-916b-4711-842e-a745011504d6','2017-03-29T16:48:35.710','2017-03-29T16:48:35.710','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',4,N'Valid Policy',N'An indicator on whether the policy is valid','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BValidPolicy',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bd93af4e-c7d9-4262-ad4b-a74501168a38','2017-03-29T16:54:07.990','2017-03-29T16:54:07.990','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',5,N'Validity Notes',N'Notes on the policy''s validity','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'ValidityNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('b6bb4ed6-d3d2-48f4-9c40-a745011737c0','2017-03-29T16:56:36.063','2017-03-29T16:56:36.063','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',6,N'General Exclusions',N'An indicator on the existence of general exclusions','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BGeneralExclusions',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('f772a818-64a4-4065-bbed-a745011a296a','2017-03-29T17:07:19.190','2017-03-29T17:07:19.190','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',7,N'General Exclusions Notes',N'Notes about the general exclusions','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'GeneralExclusionsNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('18a7721e-fdb5-42d9-8fc9-a745011a5ce5','2017-03-29T17:08:03.127','2017-03-29T17:08:03.127','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',8,N'Relevant Coverage',N'Indicator on the apliability of the coverage','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BRelevantCoverage',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('2027f0d6-16eb-4fa6-b195-a745011ae42a','2017-03-29T17:09:58.550','2017-03-29T17:09:58.550','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',9,N'Coverage Relevancy Notes',N'Notes about the apliability of te coverages','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'CoverageRelevancyNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('1990bdff-2ff6-4416-827c-a745011bdf7b','2017-03-29T17:13:33.010','2017-03-29T17:13:33.010','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',10,N'Coverage Value',N'The value of the coverage','66af024b-2e89-4e12-93f4-87e2e15120d3',16,null,1,0,N'CoverageValue',2);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('f39f8c15-ddc1-455b-9bcf-a745011c693e','2017-03-29T17:15:30.567','2017-03-29T17:15:30.567','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',11,N'Coverage Exclusions',N'An indicator to whether there are exclusions for the coverages','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BCoverageExclusions',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bb5143ce-4685-474c-834a-a745011dd5f2','2017-03-29T17:20:41.777','2017-03-29T17:20:41.777','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',12,N'Coverage Exclusions Notes',N'Notes on the existance of coverage exclusions','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'CoverageExclusionsNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('9012274f-a2a3-4314-a16e-a745011e3974','2017-03-29T17:22:06.693','2017-03-29T17:22:06.693','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',13,N'Franchise',N'The frachise''s value','66af024b-2e89-4e12-93f4-87e2e15120d3',14,null,1,0,N'Franchise',2);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('02f1f70f-6fb4-4051-9f29-a745011e93eb','2017-03-29T17:23:23.887','2017-03-29T17:23:23.887','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',14,N'Deductible Type',N'The type for the franchise','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'0da382a9-08c7-474c-816e-a04d011607ed',1,0,N'FKDeductibleType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('1340e5ed-d9cc-4f11-8120-a745011fb21f','2017-03-29T17:27:28.110','2017-03-29T17:27:28.110','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',15,N'Franchise Notes',N'Notes on the subcasualty''s franchise','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'FranchiseNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('6b816da6-7199-4610-84ab-a74501203328','2017-03-29T17:29:18.223','2017-03-29T17:53:40.827','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',16,N'Insurer Evaluation',N'The insurer''s grade, given by the casualty''s manager','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,0,N'FKInsurerEvaluation',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('6e61b0b0-22eb-45ec-9821-a74501207779','2017-03-29T17:30:16.520','2017-03-29T17:30:16.520','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',17,N'InsurerEvaluationNotes',N'Notes on the insurer''s grade','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'InsurerEvaluationNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('2415f989-09f6-4773-a2e7-a7450120c861','2017-03-29T17:31:25.567','2017-03-29T17:53:55.160','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',18,N'Expert Evaluation',N'The grade on the expert''s performance','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,0,N'FKExpertEvaluation',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('5a821abb-5b84-437d-babd-a7450120ef6e','2017-03-29T17:31:58.887','2017-03-29T17:31:58.887','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',19,N'Expert Evaluation Notes',N'Notes on the expert''s evaluation','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'ExpertEvaluationNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('807edd2d-96fb-4e20-bc04-a7450123e326','2017-03-29T17:42:43.767','2017-03-29T17:42:43.767','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','7774017d-1e45-45fa-b638-a74501229762',1,N'Type',N'The type of the entity','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,0,1,N'Type',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('51e78805-1d95-41e0-a17b-a74501260d6e','2017-03-29T17:50:36.747','2017-03-30T10:00:06.133','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,N'Grade',N'The description for the grade to give to an insurer','94ab0a78-25a1-11da-91c2-000b6abc6ae9',50,null,0,1,N'Grade',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('e7a07351-493c-4b4f-8676-a74501285cb1','2017-03-29T17:59:01.293','2017-03-29T17:59:01.293','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','884b05c1-6377-4e24-9c4f-a74501281afb',1,N'Sub Framing',N'Key of the sub casualty''s framing','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'bee4608e-3f2e-4afb-a996-a745010a87ab',0,0,N'FKSubFraming',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('8b91eed7-148a-4edb-8af3-a74501288aa3','2017-03-29T17:59:40.503','2017-03-29T17:59:40.503','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','884b05c1-6377-4e24-9c4f-a74501281afb',2,N'Entity Type',N'The type of the related entity','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'7774017d-1e45-45fa-b638-a74501229762',1,0,N'FKEntityType',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('0917fe59-860c-4339-ab1d-a7450128ae55','2017-03-29T18:00:10.960','2017-03-29T18:00:10.960','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','884b05c1-6377-4e24-9c4f-a74501281afb',3,N'Evaluation',N'The entity note','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,0,N'FKEvaluation',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('614df3f5-7deb-45dd-bc47-a7450128d1e7','2017-03-29T18:00:41.310','2017-03-29T18:00:41.310','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','884b05c1-6377-4e24-9c4f-a74501281afb',4,N'Evaluation Notes',N'notes on the entity''s evaluation','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'EvaluationNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('2e4d37d3-3645-4e09-bbe2-a75f00f8cd00','2017-04-24T15:05:51.380','2017-04-24T15:09:12.767','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',20,N'Coverage Value Notes',N'Notes on the coverage''s value','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'CoverageeNotes',null);

/*-- -Insert(s): [madds].[tblObjects] */
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('bee4608e-3f2e-4afb-a996-a745010a87ab','2017-03-29T16:10:24.367','2017-04-11T17:35:46.153','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Sub Casualty Framing',N'This table stores the framing information for a sub-casualty',N'tblBBSubCasualtyFraming',N'com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFraming','4cd3738e-2e2e-4789-ad81-9e160090f723');
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('7774017d-1e45-45fa-b638-a74501229762','2017-03-29T17:38:00.657','2017-03-29T17:42:45.810','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Framing Entity Types',N'The types of entities used when grading, related to sub-casualty''s framing',N'tblFramingEntityTypes',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('b0ab7a81-c2fd-485b-b18e-a7450125d9ea','2017-03-29T17:49:52.787','2017-03-29T17:50:41.053','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Evaluation Values',N'A table to hold the values for an evaluation',N'tblBBEvaluationValues',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('884b05c1-6377-4e24-9c4f-a74501281afb','2017-03-29T17:58:05.220','2017-04-13T12:40:57.430','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Sub Casualty Framing Entities',N'The list of entities related to a framing',N'tblBBSubCasualtyFramingEntity',N'com.premiumminds.BigBang.Jewel.Objects.SubCasualtyFramingEntity','4cd3738e-2e2e-4789-ad81-9e160090f723');

/*-- -Insert(s): [madds].[tblQueryDefs] */
INSERT INTO [madds].[tblQueryDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKDriver],[IsDefault],[DefParamName],[DefParamType],[ParamAppliesTo],[FKEditorView],[IsReadOnly],[FKReport],[Reference],[CanCreate],[CanEditRows],[CanDelete],[FKQueryType]) VALUES('62a4952d-3305-4b74-823b-a74600b0b297','2017-03-30T10:43:20.200','2017-03-30T10:53:37.707','957cb94d-7968-4921-8797-43af37ab98d9','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,null,null,null,'b1312296-0899-4f5c-b2f6-a74600ae75e8',0,null,null,1,1,1,'9d20405a-9968-4807-9980-7a6166a72283');
INSERT INTO [madds].[tblQueryDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKDriver],[IsDefault],[DefParamName],[DefParamType],[ParamAppliesTo],[FKEditorView],[IsReadOnly],[FKReport],[Reference],[CanCreate],[CanEditRows],[CanDelete],[FKQueryType]) VALUES('3d55df69-27f5-43ac-8754-a74600ba6c86','2017-03-30T11:18:44.940','2017-03-30T11:18:53.517','957cb94d-7968-4921-8797-43af37ab98d9','7774017d-1e45-45fa-b638-a74501229762',1,null,null,null,null,0,null,null,1,1,1,'9d20405a-9968-4807-9980-7a6166a72283');

/*-- -Insert(s): [madds].[tblQueryFields] */
INSERT INTO [madds].[tblQueryFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrder],[QFName],[QFSQL],[QFWidth]) VALUES('edef2bb0-0d2f-441a-a435-a74600b12722','2017-03-30T10:44:59.643','2017-03-30T10:44:59.643','942704b9-8fef-40c3-8020-3911efb3f094','62a4952d-3305-4b74-823b-a74600b0b297',1,N'Grade',N'[:Grade]',200);
INSERT INTO [madds].[tblQueryFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrder],[QFName],[QFSQL],[QFWidth]) VALUES('abc5339a-27a2-4f1c-8d0b-a74600bb1346','2017-03-30T11:21:07.243','2017-03-30T11:21:07.243','942704b9-8fef-40c3-8020-3911efb3f094','3d55df69-27f5-43ac-8754-a74600ba6c86',1,N'Type',N'[:Type]',200);

/*-- -Insert(s): [madds].[tblViewDefs] */
INSERT INTO [madds].[tblViewDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[IsDefault],[Reference]) VALUES('b1312296-0899-4f5c-b2f6-a74600ae75e8','2017-03-30T10:35:11.507','2017-03-30T10:53:03.520','cb6d759d-0333-4830-914c-b7d0b9b728dd','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',1,null);
INSERT INTO [madds].[tblViewDefs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[IsDefault],[Reference]) VALUES('6dc0bf86-4197-49dd-b433-a74600b95e15','2017-03-30T11:14:54.170','2017-03-30T11:14:54.170','cb6d759d-0333-4830-914c-b7d0b9b728dd','7774017d-1e45-45fa-b638-a74501229762',0,null);

/*-- -Insert(s): [madds].[tblViewTabs] */
INSERT INTO [madds].[tblViewTabs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrd],[VTName],[FKVTType],[FKForm],[FKQuery],[FKReport]) VALUES('0603cf98-ac3d-46b2-9177-a74600aec354','2017-03-30T10:36:17.570','2017-03-30T10:37:21.103','e204d946-661a-4cf3-ba7a-31f55b95caf8','b1312296-0899-4f5c-b2f6-a74600ae75e8',1,N'General','1febe70f-5461-48ee-b3a4-191bc47db3c5','43862a2a-752c-4df6-af19-a74600ab31a3',null,null);
INSERT INTO [madds].[tblViewTabs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKOwner],[NOrd],[VTName],[FKVTType],[FKForm],[FKQuery],[FKReport]) VALUES('1b668897-034b-4929-8153-a74600ba17fd','2017-03-30T11:17:32.797','2017-03-30T11:17:32.797','e204d946-661a-4cf3-ba7a-31f55b95caf8','6dc0bf86-4197-49dd-b433-a74600b95e15',1,N'General','1febe70f-5461-48ee-b3a4-191bc47db3c5','28712f34-a979-42ca-b643-a74600b81d97',null,null);


/*-- -Insert(s): [bigbang].[tblBBEvaluationValues] */
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('83a74beb-84fb-4f78-a694-a74e00c077ad','2017-04-07T11:40:45.173','2017-04-19T11:44:17.490','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'0 - Não aplicável');
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('02fda7b8-b439-4e5f-8790-a74e00c08192','2017-04-07T11:40:53.613','2017-04-19T11:45:08.013','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'1 - Muito fraco');
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('4c5e9770-0c75-4659-9d55-a74e00c08e2f','2017-04-07T11:41:04.377','2017-04-19T11:44:37.733','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'2 - Fraco');
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('88b09950-3643-4590-97c6-a74e00c09991','2017-04-07T11:41:14.087','2017-04-19T11:44:41.833','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'3 - Suficiente');
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('05697dfd-edda-45bc-a021-a74e00c0b2da','2017-04-07T11:41:35.667','2017-04-19T11:44:48.973','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'4 - Bom');
INSERT INTO [bigbang].[tblBBEvaluationValues] ([PK],[_TSCreate],[_TSUpdate],[FKType],[Grade]) VALUES('a5cf1fed-1ec7-4fb8-a954-a75a00c19efa','2017-04-19T11:44:57.160','2017-04-19T11:44:57.160','b0ab7a81-c2fd-485b-b18e-a7450125d9ea',N'5 - Muito bom');


-- Changes in the notes columns' sizes for casualties and sub-casualties
ALTER TABLE bbangola.tblBBCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbangola.tblBBCasualties ALTER COLUMN Notes VARCHAR (4000);
ALTER TABLE bbangola.tblBBSubCasualties ALTER COLUMN Description VARCHAR (4000);
ALTER TABLE bbangola.tblBBSubCasualties ALTER COLUMN Notes VARCHAR (4000);

UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:24:05.130',[Size]=4000 WHERE [PK]='21870119-b746-43a8-b5b5-a02e011391cf';
UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:10:14.327',[Size]=4000 WHERE [PK]='771ec1f4-3de7-495f-b3b3-a035011b8957';
UPDATE [madds].[tblObjectMembers] SET [_TSUpdate]='2017-05-24T11:10:27.473',[Size]=4000 WHERE [PK]='455cf1c1-c0e1-4926-8e38-a035011bbafd';

CREATE TABLE [bbangola].[tblBBSubCasualtyHeadings]
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

INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('193178a5-c436-402d-a6d9-a77e00b67296','2017-05-25T11:04:16.290','2017-05-25T11:04:16.290','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','980B1995-5522-4EAB-8E35-A46E00C39917','a3e737a5-5474-41d4-842e-a77e00af9e40');

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('6a1e9e53-d5de-4418-89de-63ce5157707a','2017-05-25T11:04:28.800','2017-05-25T11:04:28.800','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'CREATE TABLE [bbangola].[tblBBSubCasualtyHeadings] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''a3e737a5-5474-41d4-842e-a77e00af9e40'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [bbangola].[tblBBSubCasualtyFraming] ([PK]), [BaseSalary] decimal(16,2), [FeedAllowance] decimal(16,2), [OtherFees12] decimal(16,2), [OtherFees14] decimal(16,2))');

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__15CE81DF] DEFAULT ('a3e737a5-5474-41d4-842e-a77e00af9e40') FOR [FKType];

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSCr__13E6396D] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [DF__tblBBSubC___TSUp__14DA5DA6] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [PK__tblBBSub__321507870F218450] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] ADD CONSTRAINT [UQ__tblBBSub__8E5E10D211FDF0FB] UNIQUE NONCLUSTERED
(
	[FKSubFraming] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY];

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__17B6CA51] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbangola].[tblBBSubCasualtyFraming]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbangola].[tblBBSubCasualtyHeadings] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__16C2A618] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [BDeclinedCasualty] [bit] NULL;
ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [BDeclineWarning] [bit] NULL;
ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [DeclinedCasualtyNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL;
ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [DeclineWarningNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL;

INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('fe4d2fe0-7b00-40d6-8000-c61a70c94ad2','2017-04-24T15:05:52.753','2017-04-24T15:05:52.753','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [CoverageeNotes] nvarchar(250)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('a0617402-7fb5-4d8e-9d04-c93c81c549c6','2017-06-21T14:44:59.917','2017-06-21T14:44:59.917','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [DeclinedCasualtyNotes] nvarchar(250)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('ca2a0724-20d3-4291-97d8-d35a82438431','2017-06-21T14:43:20.767','2017-06-21T14:43:20.767','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [BDeclinedCasualty] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('732f3454-eafa-489d-8544-d43831c6d1db','2017-06-21T14:50:46.707','2017-06-21T14:50:46.707','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [BDeclineWarning] bit');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('1f44a1c9-0fe1-4485-b2f2-feb794ae865e','2017-06-21T14:55:08.537','2017-06-21T14:55:08.537','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','980B1995-5522-4EAB-8E35-A46E00C39917',N'ALTER TABLE [bbangola].[tblBBSubCasualtyFraming] ADD [DeclineWarningNotes] nvarchar(250)');

INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('0267b2b6-4f66-4b8f-a8ee-a79900f2a13c','2017-06-21T14:43:23.310','2017-06-21T14:43:23.310','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',21,N'Casualty Declined',N'Indicator of whether the casualty was declined','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BDeclinedCasualty',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('77898b5a-ba68-4249-80d5-a79900f313e4','2017-06-21T14:45:01.143','2017-06-21T14:45:01.143','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',22,N'Casualty Declined Notes',N'Nothes about the fact that the casualty was declined','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'DeclinedCasualtyNotes',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bc7185e5-9451-44d5-b139-a79900f4aa46','2017-06-21T14:50:47.917','2017-06-21T14:50:47.917','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',23,N'Casualty Decline Warning',N'Indicator of whether the client was warned by Crédite-EGS that the casualty cound be declined','94ab0a7a-25a1-11da-91c2-000b6abc6ae9',null,null,1,0,N'BDeclineWarning',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('4f6e8d10-a469-45cc-bcc6-a79900f5dd36','2017-06-21T14:55:09.843','2017-06-21T14:55:09.843','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','bee4608e-3f2e-4afb-a996-a745010a87ab',24,N'Casualty Decline Warning Notes',N'Why there was not the perception there could be a decline','94ab0a78-25a1-11da-91c2-000b6abc6ae9',250,null,1,0,N'DeclineWarningNotes',null);

