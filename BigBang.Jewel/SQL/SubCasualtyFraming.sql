-- Table creation 

CREATE TABLE [bbcomercial].[tblBBSubCasualtyFraming]
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
	[CoverageExclusionsNotes] [bit] NULL,
	[Franchise] [decimal](14,2) NULL,
	[FKDeductibleType] [uniqueidentifier] NULL,
	[FranchiseNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKInsurerEvaluation] [uniqueidentifier] NULL,
	[InsurerEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKExpertEvaluation] [uniqueidentifier] NULL,
	[ExpertEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL
);

CREATE TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity]
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

CREATE TABLE [bbleiria].[tblBBSubCasualtyFraming]
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
	[CoverageExclusionsNotes] [bit] NULL,
	[Franchise] [decimal](14,2) NULL,
	[FKDeductibleType] [uniqueidentifier] NULL,
	[FranchiseNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKInsurerEvaluation] [uniqueidentifier] NULL,
	[InsurerEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKExpertEvaluation] [uniqueidentifier] NULL,
	[ExpertEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL
);

CREATE TABLE [bbleiria].[tblBBSubCasualtyFramingEntity]
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

CREATE TABLE [credite_egs].[tblBBSubCasualtyFraming]
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
	[CoverageExclusionsNotes] [bit] NULL,
	[Franchise] [decimal](14,2) NULL,
	[FKDeductibleType] [uniqueidentifier] NULL,
	[FranchiseNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKInsurerEvaluation] [uniqueidentifier] NULL,
	[InsurerEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL,
	[FKExpertEvaluation] [uniqueidentifier] NULL,
	[ExpertEvaluationNotes] [nvarchar](250) COLLATE SQL_Latin1_General_CP1_CI_AI NULL
);

CREATE TABLE [credite_egs].[tblBBSubCasualtyFramingEntity]
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

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval__FKTyp__7C43BA06] DEFAULT ('b0ab7a81-c2fd-485b-b18e-a7450125d9ea') FOR [FKType];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval___TSCr__7A5B7194] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [DF__tblBBEval___TSUp__7B4F95CD] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__03E4DBCE] DEFAULT ('bee4608e-3f2e-4afb-a996-a745010a87ab') FOR [FKType];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__0F568E7A] DEFAULT ('884b05c1-6377-4e24-9c4f-a74501281afb') FOR [FKType];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__19D41CED] DEFAULT ('bee4608e-3f2e-4afb-a996-a745010a87ab') FOR [FKType];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__2545CF99] DEFAULT ('884b05c1-6377-4e24-9c4f-a74501281afb') FOR [FKType];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__2FC35E0C] DEFAULT ('bee4608e-3f2e-4afb-a996-a745010a87ab') FOR [FKType];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC__FKTyp__3B3510B8] DEFAULT ('884b05c1-6377-4e24-9c4f-a74501281afb') FOR [FKType];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSCr__01FC935C] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSCr__0D6E4608] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSCr__17EBD47B] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSCr__235D8727] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSCr__2DDB159A] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSCr__394CC846] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSUp__02F0B795] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSUp__0E626A41] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSUp__18DFF8B4] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSUp__2451AB60] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] ADD CONSTRAINT [DF__tblBBSubC___TSUp__2ECF39D3] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [DF__tblBBSubC___TSUp__3A40EC7F] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin__FKTyp__71C62B93] DEFAULT ('7774017d-1e45-45fa-b638-a74501229762') FOR [FKType];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin___TSCr__6FDDE321] DEFAULT (getdate()) FOR [_TSCreate];

ALTER TABLE [bigbang].[tblFramingEntityTypes] ADD CONSTRAINT [DF__tblFramin___TSUp__70D2075A] DEFAULT (getdate()) FOR [_TSUpdate];

ALTER TABLE [bigbang].[tblBBEvaluationValues] ADD CONSTRAINT [PK__tblBBEva__321507877596BC77] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] ADD CONSTRAINT [PK__tblBBSub__3215078700144AEA] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [PK__tblBBSub__321507870B85FD96] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] ADD CONSTRAINT [PK__tblBBSub__3215078716038C09] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [PK__tblBBSub__3215078721753EB5] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] ADD CONSTRAINT [PK__tblBBSub__321507872BF2CD28] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] ADD CONSTRAINT [PK__tblBBSub__3215078737647FD4] PRIMARY KEY CLUSTERED
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

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__ FKSu__05CD2440] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [credite_egs].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__ FKSu__1BBC655F] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbleiria].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__ FKSu__31ABA67E] FOREIGN KEY
(
	[FKSubCasualty]
)
REFERENCES [bbcomercial].[tblBBSubCasualties]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKDed__06C14879] FOREIGN KEY
(
	[FKDeductibleType]
)
REFERENCES [bigbang].[tblBBDeductibleTypes]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKDed__1CB08998] FOREIGN KEY
(
	[FKDeductibleType]
)
REFERENCES [bigbang].[tblBBDeductibleTypes]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKDed__329FCAB7] FOREIGN KEY
(
	[FKDeductibleType]
)
REFERENCES [bigbang].[tblBBDeductibleTypes]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEnt__1232FB25] FOREIGN KEY
(
	[FKEntityType]
)
REFERENCES [bigbang].[tblFramingEntityTypes]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEnt__28223C44] FOREIGN KEY
(
	[FKEntityType]
)
REFERENCES [bigbang].[tblFramingEntityTypes]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEnt__3E117D63] FOREIGN KEY
(
	[FKEntityType]
)
REFERENCES [bigbang].[tblFramingEntityTypes]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEva__13271F5E] FOREIGN KEY
(
	[FKEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEva__2916607D] FOREIGN KEY
(
	[FKEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKEva__3F05A19C] FOREIGN KEY
(
	[FKEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKExp__08A990EB] FOREIGN KEY
(
	[FKExpertEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKExp__1E98D20A] FOREIGN KEY
(
	[FKExpertEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

SALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKExp__34881329] FOREIGN KEY
(
	[FKExpertEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__07B56CB2] FOREIGN KEY
(
	[FKInsurerEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__1DA4ADD1] FOREIGN KEY
(
	[FKInsurerEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKIns__3393EEF0] FOREIGN KEY
(
	[FKInsurerEvaluation]
)
REFERENCES [bigbang].[tblBBEvaluationValues]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__113ED6EC] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [credite_egs].[tblBBSubCasualtyFraming]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__272E180B] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbleiria].[tblBBSubCasualtyFraming]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKSub__3D1D592A] FOREIGN KEY
(
	[FKSubFraming]
)
REFERENCES [bbcomercial].[tblBBSubCasualtyFraming]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__04D90007] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [credite_egs].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__104AB2B3] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__1AC84126] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbleiria].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__2639F3D2] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFraming] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__30B78245] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
);

ALTER TABLE [bbcomercial].[tblBBSubCasualtyFramingEntity] WITH CHECK ADD CONSTRAINT [FK__tblBBSubC__FKTyp__3C2934F1] FOREIGN KEY
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


-- SQL to add the data to madds
/*-- -Insert(s): [madds].[tblDDLLogs] */
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('b31ff416-0623-4576-b436-652bbac49ac3','2017-03-30T11:35:54.857','2017-03-30T11:35:54.857','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'CREATE TABLE [bbleiria].[tblBBSubCasualtyFramingEntities] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''884b05c1-6377-4e24-9c4f-a74501281afb'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbleiria].[tblBBSubCasualtyFraming ] ([PK]), [FKEntityType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblFramingEntityTypes] ([PK]), [FKEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [EvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('993a8f1d-c89f-4d43-9a46-8aaae3836e9b','2017-03-30T11:35:49.877','2017-03-30T11:35:49.877','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'CREATE TABLE [credite_egs].[tblBBSubCasualtyFraming ] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''bee4608e-3f2e-4afb-a996-a745010a87ab'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [ FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblBBSubCasualties] ([PK]), [AnalysisDate] datetime, [ BFramingDifficulty] bit, [BValidPolicy] bit, [ValidityNotes] nvarchar(250), [BGeneralExclusions] bit, [GeneralExclusionsNotes] nvarchar(250), [BRelevantCoverage] bit, [CoverageRelevancyNotes] nvarchar(250), [CoverageValue] decimal(16,2), [BCoverageExclusions] bit, [CoverageExclusionsNotes] bit, [Franchise] decimal(14,2), [FKDeductibleType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBDeductibleTypes] ([PK]), [FranchiseNotes] nvarchar(250), [FKInsurerEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [InsurerEvaluationNotes] nvarchar(250), [FKExpertEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [ExpertEvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('0d2dc301-6c65-44fe-b6fb-8ad7b14a6607','2017-03-30T11:35:59.510','2017-03-30T11:35:59.510','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbcomercial].[tblBBSubCasualtyFraming ] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''bee4608e-3f2e-4afb-a996-a745010a87ab'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [ FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbcomercial].[tblBBSubCasualties] ([PK]), [AnalysisDate] datetime, [ BFramingDifficulty] bit, [BValidPolicy] bit, [ValidityNotes] nvarchar(250), [BGeneralExclusions] bit, [GeneralExclusionsNotes] nvarchar(250), [BRelevantCoverage] bit, [CoverageRelevancyNotes] nvarchar(250), [CoverageValue] decimal(16,2), [BCoverageExclusions] bit, [CoverageExclusionsNotes] bit, [Franchise] decimal(14,2), [FKDeductibleType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBDeductibleTypes] ([PK]), [FranchiseNotes] nvarchar(250), [FKInsurerEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [InsurerEvaluationNotes] nvarchar(250), [FKExpertEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [ExpertEvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('72c00222-6b02-4423-af7f-8db6633975ac','2017-03-30T11:29:42.163','2017-03-30T11:29:42.163','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblFramingEntityTypes] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''7774017d-1e45-45fa-b638-a74501229762'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [Type] nvarchar(250) UNIQUE NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('19d5d253-4d81-416d-bdb8-9130603f822b','2017-03-30T11:35:59.520','2017-03-30T11:35:59.520','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','3ede450f-17ef-4d91-ac72-a4c3012591f1',N'CREATE TABLE [bbcomercial].[tblBBSubCasualtyFramingEntities] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''884b05c1-6377-4e24-9c4f-a74501281afb'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbcomercial].[tblBBSubCasualtyFraming ] ([PK]), [FKEntityType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblFramingEntityTypes] ([PK]), [FKEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [EvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('24f412b3-b2c0-4a12-8ac3-bdceeb4ddd3d','2017-03-30T11:35:54.847','2017-03-30T11:35:54.847','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','337a0a77-8942-43aa-9ec0-a47001171a77',N'CREATE TABLE [bbleiria].[tblBBSubCasualtyFraming ] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''bee4608e-3f2e-4afb-a996-a745010a87ab'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [ FKSubCasualty] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [bbleiria].[tblBBSubCasualties] ([PK]), [AnalysisDate] datetime, [ BFramingDifficulty] bit, [BValidPolicy] bit, [ValidityNotes] nvarchar(250), [BGeneralExclusions] bit, [GeneralExclusionsNotes] nvarchar(250), [BRelevantCoverage] bit, [CoverageRelevancyNotes] nvarchar(250), [CoverageValue] decimal(16,2), [BCoverageExclusions] bit, [CoverageExclusionsNotes] bit, [Franchise] decimal(14,2), [FKDeductibleType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBDeductibleTypes] ([PK]), [FranchiseNotes] nvarchar(250), [FKInsurerEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [InsurerEvaluationNotes] nvarchar(250), [FKExpertEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [ExpertEvaluationNotes] nvarchar(250))');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('8308ac77-58dc-4896-b0c6-e0b88dd72399','2017-03-30T11:29:42.170','2017-03-30T11:29:42.170','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblBBEvaluationValues] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''b0ab7a81-c2fd-485b-b18e-a7450125d9ea'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [Grade] nvarchar(50) UNIQUE NOT NULL)');
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('a911bda3-32ec-4b3f-9509-ff08b08089ea','2017-03-30T11:35:49.887','2017-03-30T11:35:49.887','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','a1cc31a3-e471-4568-b5fc-9e15008e98c9',N'CREATE TABLE [credite_egs].[tblBBSubCasualtyFramingEntities] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''884b05c1-6377-4e24-9c4f-a74501281afb'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [FKSubFraming] uniqueidentifier NOT NULL FOREIGN KEY REFERENCES [credite_egs].[tblBBSubCasualtyFraming ] ([PK]), [FKEntityType] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblFramingEntityTypes] ([PK]), [FKEvaluation] uniqueidentifier FOREIGN KEY REFERENCES [bigbang].[tblBBEvaluationValues] ([PK]), [EvaluationNotes] nvarchar(250))');

/*-- -Insert(s): [madds].[tblEntities] */
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('e848d522-3801-4b19-9998-a74600bd6eb6','2017-03-30T11:29:42.157','2017-03-30T11:29:42.157','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','7774017d-1e45-45fa-b638-a74501229762');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('96928a73-d42a-4576-8c49-a74600bd6eb7','2017-03-30T11:29:42.157','2017-03-30T11:29:42.157','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','b0ab7a81-c2fd-485b-b18e-a7450125d9ea');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('778f7662-7a02-4818-a586-a74600bf1da0','2017-03-30T11:35:49.870','2017-03-30T11:35:49.870','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','a1cc31a3-e471-4568-b5fc-9e15008e98c9','bee4608e-3f2e-4afb-a996-a745010a87ab');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('c9f4ec9c-2f8e-49e4-91f2-a74600bf1da1','2017-03-30T11:35:49.870','2017-03-30T11:35:49.870','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','a1cc31a3-e471-4568-b5fc-9e15008e98c9','884b05c1-6377-4e24-9c4f-a74501281afb');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('c2ca2fa1-c2b7-4908-945b-a74600bf2372','2017-03-30T11:35:54.837','2017-03-30T11:35:54.837','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','337a0a77-8942-43aa-9ec0-a47001171a77','bee4608e-3f2e-4afb-a996-a745010a87ab');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('138fd844-5a6a-4a57-a1dc-a74600bf2373','2017-03-30T11:35:54.840','2017-03-30T11:35:54.840','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','337a0a77-8942-43aa-9ec0-a47001171a77','884b05c1-6377-4e24-9c4f-a74501281afb');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('35311285-496b-4018-b369-a74600bf28ea','2017-03-30T11:35:59.503','2017-03-30T11:35:59.503','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','3ede450f-17ef-4d91-ac72-a4c3012591f1','bee4608e-3f2e-4afb-a996-a745010a87ab');
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('45937cd5-03bd-4e36-b37f-a74600bf28eb','2017-03-30T11:35:59.507','2017-03-30T11:35:59.507','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','3ede450f-17ef-4d91-ac72-a4c3012591f1','884b05c1-6377-4e24-9c4f-a74501281afb');

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