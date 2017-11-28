/*
 * 
 * NOTA
 * 
 * Apesar do integrador da Libery estar aqui, não foi passado a produção.
 * 
 * Para passar, tem que se correr este SQL.
 * 
 * No entanto, nunca foi testado e a ideia foi abandonada ou "adiada até um dia"
 * 
 * Eventualmente pode-se apagar este ficheiro e o com.premiumminds.BigBang.Jewel.FileIO.Liberty.java podem ser eliminados
 * 
 */




-- Creates the translators and file processors
/*-- -Insert(s): [bigbang].[tblBBFileProcessers] */
INSERT INTO [bigbang].[tblBBFileProcessers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKFormat],[ProcesserClass]) VALUES('220cbce9-7bd8-42c4-b932-a5e201009acd','2016-04-08T15:34:16.163','2016-04-08T15:34:16.163','648cb5a9-3abb-4995-b5fa-a0a600edf739','14adae21-c09f-443b-bc2d-a5e200c7e2e0',N'com.premiumminds.BigBang.Jewel.FileIO.Liberty');

/*-- -Insert(s): [bigbang].[tblFileFields] */
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('17843e9e-c64a-4c25-a0df-a5e200cdd39f','2016-04-08T12:29:23.520','2016-04-08T12:29:54.953','760c9fb0-c31f-45b7-a682-e95d31742034',N'Agent',1,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('05489914-bc6d-4340-b004-a5e200ce2154','2016-04-08T12:30:29.930','2016-04-08T12:30:29.930','760c9fb0-c31f-45b7-a682-e95d31742034',N'Client',2,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('7f5b2abb-ec0d-4e31-a01f-a5e200ce8b72','2016-04-08T12:32:00.383','2016-04-08T12:32:00.383','760c9fb0-c31f-45b7-a682-e95d31742034',N'Product',3,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',5);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('1d7d0028-4d8d-4cdc-888a-a5e200cec147','2016-04-08T12:32:46.320','2016-04-08T12:32:46.320','760c9fb0-c31f-45b7-a682-e95d31742034',N'Policy',4,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('896aa47e-cabf-4301-8ccc-a5e200cf8540','2016-04-08T12:35:33.550','2016-04-08T12:35:33.550','760c9fb0-c31f-45b7-a682-e95d31742034',N'Campaign',5,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',3);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('eb34891c-0612-411e-8386-a5e200d05c42','2016-04-08T12:38:37.023','2016-04-08T12:38:37.023','760c9fb0-c31f-45b7-a682-e95d31742034',N'Facturator',6,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',3);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('f129be20-711d-4274-b913-a5e200d0db4d','2016-04-08T12:40:25.430','2016-04-08T12:40:25.430','760c9fb0-c31f-45b7-a682-e95d31742034',N'Receipt',7,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',14);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('1d4b2798-d510-4b14-a0b0-a5e200d17cba','2016-04-08T12:42:43.187','2016-04-08T12:42:43.187','760c9fb0-c31f-45b7-a682-e95d31742034',N'Start Date',8,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',8);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('6937e9fa-82c4-4d58-82b6-a5e200d1d4bd','2016-04-08T12:43:58.287','2016-04-08T12:43:58.287','760c9fb0-c31f-45b7-a682-e95d31742034',N'End Date',9,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',8);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('e7372b59-3bf4-4ead-b0d7-a5e200d28c89','2016-04-08T12:46:35.127','2016-04-08T12:46:35.127','760c9fb0-c31f-45b7-a682-e95d31742034',N'Payment Date',10,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',8);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('d7e9105c-5a0c-464a-a826-a5e200d337db','2016-04-08T12:49:01.320','2016-04-08T12:49:01.320','760c9fb0-c31f-45b7-a682-e95d31742034',N'IsFirst',11,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('a286177a-4c8d-4833-bec1-a5e200d460be','2016-04-08T12:53:14.663','2016-04-08T12:53:14.663','760c9fb0-c31f-45b7-a682-e95d31742034',N'Adherer',12,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',11);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('b3c3c897-0dcd-4bbd-800b-a5e200d4cce3','2016-04-08T12:54:46.950','2016-04-08T12:54:46.950','760c9fb0-c31f-45b7-a682-e95d31742034',N'Total Premium Sign',13,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('db79029d-e319-47ab-b3ee-a5e200d594e7','2016-04-08T12:57:37.630','2016-04-08T12:57:37.630','760c9fb0-c31f-45b7-a682-e95d31742034',N'Total Premium',14,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',11);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('8994a900-bb37-4c40-aeb4-a5e200d61b29','2016-04-08T12:59:32.193','2016-04-08T12:59:32.193','760c9fb0-c31f-45b7-a682-e95d31742034',N'Free Space 1',15,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('019059e9-e013-4800-be4a-a5e200d659a7','2016-04-08T13:00:25.637','2016-04-08T13:00:25.637','760c9fb0-c31f-45b7-a682-e95d31742034',N'Sales Premium Sign',16,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('17872bfa-899e-49b9-9fdf-a5e200d76ae7','2016-04-08T13:04:18.697','2016-04-08T13:04:18.697','760c9fb0-c31f-45b7-a682-e95d31742034',N'Sales Premium',17,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',11);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('c7fa1233-6f7e-406a-a10a-a5e200f129d0','2016-04-08T14:38:02.937','2016-04-08T14:38:02.937','760c9fb0-c31f-45b7-a682-e95d31742034',N'Free Space 2',18,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('f54d0b3e-e9c4-4e95-809e-a5e200f1fbb3','2016-04-08T14:41:02.040','2016-04-08T14:41:02.040','760c9fb0-c31f-45b7-a682-e95d31742034',N'Commission Sign',19,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('49976d7d-941c-410e-9bdf-a5e200f3422a','2016-04-08T14:45:40.633','2016-04-08T14:45:40.633','760c9fb0-c31f-45b7-a682-e95d31742034',N'Commission',20,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',11);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('d08cf963-fe4f-4378-bcb3-a5e200f3d183','2016-04-08T14:47:42.947','2016-04-08T14:47:42.947','760c9fb0-c31f-45b7-a682-e95d31742034',N'Free Space 3',21,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('d8fdafbc-4292-4316-9c6f-a5e200f40a7d','2016-04-08T14:48:31.570','2016-04-08T14:57:53.127','760c9fb0-c31f-45b7-a682-e95d31742034',N'IRS Sign',22,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('8dc2c5a8-e96d-47b3-aa85-a5e200f6da55','2016-04-08T14:58:45.833','2016-04-08T14:59:03.903','760c9fb0-c31f-45b7-a682-e95d31742034',N'IRS',23,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',11);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('266375b4-5dfe-49e4-9be3-a5e200f7c44d','2016-04-08T15:02:05.487','2016-04-08T15:02:05.487','760c9fb0-c31f-45b7-a682-e95d31742034',N'Channel',24,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',2);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('f6b1a584-d587-47af-9eb0-a5e200f9050f','2016-04-08T15:06:39.200','2016-04-08T15:06:39.200','760c9fb0-c31f-45b7-a682-e95d31742034',N'State',25,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('2dad757f-9053-45b1-af03-a5e200f937e1','2016-04-08T15:07:22.567','2016-04-08T15:07:22.567','760c9fb0-c31f-45b7-a682-e95d31742034',N'State Date',26,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',8);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('67883ebb-6d15-4407-8352-a5e200fbbcd7','2016-04-08T15:16:32.937','2016-04-08T15:16:32.937','760c9fb0-c31f-45b7-a682-e95d31742034',N'Payment Mode',27,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',3);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('29c4be82-3a0a-4032-a769-a5e200fc48e6','2016-04-08T15:18:32.450','2016-04-08T15:18:32.450','760c9fb0-c31f-45b7-a682-e95d31742034',N'Type',28,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('3b99ed67-a6c9-4fd5-869c-a5e200fc8e2d','2016-04-08T15:19:31.567','2016-04-08T15:19:31.567','760c9fb0-c31f-45b7-a682-e95d31742034',N'Collector',29,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('a2b45c7d-92f3-4252-91cc-a5e200fdebf7','2016-04-08T15:24:30.053','2016-04-08T15:24:30.053','760c9fb0-c31f-45b7-a682-e95d31742034',N'Collection Method',30,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',12);
INSERT INTO [bigbang].[tblFileFields] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FieldName],[FieldOrder],[Mandatory],[FKFType],[FKSection],[FieldLength]) VALUES('627571be-9e20-4dbf-9b9d-a5e200fe6db3','2016-04-08T15:26:20.760','2016-04-08T15:26:20.760','760c9fb0-c31f-45b7-a682-e95d31742034',N'Company',31,1,'1d1ac009-0d37-4ae3-8d49-9e89119f7230','a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5',1);

/*-- -Insert(s): [bigbang].[tblFileSections] */
INSERT INTO [bigbang].[tblFileSections] ([PK],[_TSCreate],[_TSUpdate],[FKType],[SectionName],[SectionOrder],[MinCount],[MaxCount],[Terminator],[FKParentSection],[FKFSpec],[Separator]) VALUES('a2b0bcf0-bb05-4260-ba2b-a5e200c91cc5','2016-04-08T12:12:13.703','2016-04-08T12:12:13.703','c8f77d53-4489-4787-8e31-29c30af00162',N'Body',1,1,null,null,null,'14adae21-c09f-443b-bc2d-a5e200c7e2e0',null);

/*-- -Insert(s): [bigbang].[tblFileSpecs] */
INSERT INTO [bigbang].[tblFileSpecs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FSpecName],[FKFormat],[Encoding]) VALUES('14adae21-c09f-443b-bc2d-a5e200c7e2e0','2016-04-08T12:07:45.820','2016-04-08T12:07:45.820','5514358c-2fcf-4769-981f-3c11bb25ba76',N'Recibos Liberty','0e635595-f993-4abe-884e-3d2784d59f96',N'UTF-8');


--  Creates the type translator table for liberty's receipt's import
CREATE TABLE [bigbang].[tblLibertyTranslator]
(
	[PK] [uniqueidentifier] ROWGUIDCOL NOT NULL,
	[_VER] [timestamp] NOT NULL,
	[_TSCreate] [datetime] NOT NULL,
	[_TSUpdate] [datetime] NOT NULL,
	[FKType] [uniqueidentifier] NOT NULL,
	[TInput] [nvarchar](1) COLLATE SQL_Latin1_General_CP1_CI_AI NOT NULL,
	[FKReceiptType] [uniqueidentifier] NOT NULL
)

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [DF__tblLibert__FKTyp__273C368E] DEFAULT ('c42eef38-c2b4-45be-ae86-a5e600bad859') FOR [FKType]

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [DF__tblLibert___TSCr__2553EE1C] DEFAULT (getdate()) FOR [_TSCreate]

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [DF__tblLibert___TSUp__26481255] DEFAULT (getdate()) FOR [_TSUpdate]

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [PK__tblLiber__321507871DB2CC54] PRIMARY KEY CLUSTERED
(
	[PK] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [UQ__tblLiber__4434E839208F38FF] UNIQUE NONCLUSTERED
(
	[FKReceiptType] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY]

ALTER TABLE [bigbang].[tblLibertyTranslator] ADD CONSTRAINT [UQ__tblLiber__FB362692236BA5AA] UNIQUE NONCLUSTERED
(
	[TInput] ASC
)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, IGNORE_DUP_KEY = OFF) ON [PRIMARY]

ALTER TABLE [bigbang].[tblLibertyTranslator] WITH CHECK ADD CONSTRAINT [FK__tblLibert__FKRec__29247F00] FOREIGN KEY
(
	[FKReceiptType]
)
REFERENCES [bigbang].[tblReceiptTypes]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION

ALTER TABLE [bigbang].[tblLibertyTranslator] WITH CHECK ADD CONSTRAINT [FK__tblLibert__FKTyp__28305AC7] FOREIGN KEY
(
	[FKType]
)
REFERENCES [madds].[tblObjects]
(
	[PK]
)
ON DELETE NO ACTION
ON UPDATE NO ACTION

/*-- -Insert(s): [madds].[tblDDLLogs] */
INSERT INTO [madds].[tblDDLLogs] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNSpace],[DDLText]) VALUES('9e05c621-f8a1-408e-ac26-6be1cacc9314','2016-04-12T11:28:21.590','2016-04-12T11:28:21.590','f57bcb6b-ec2d-4828-bbc9-3ef660ecb900','c37b81f0-860f-4868-9177-9e15008b3efd',N'CREATE TABLE [bigbang].[tblLibertyTranslator] ([PK] uniqueidentifier ROWGUIDCOL NOT NULL PRIMARY KEY, [_VER] rowversion, [_TSCreate] datetime DEFAULT GETDATE() NOT NULL, [_TSUpdate] datetime DEFAULT GETDATE() NOT NULL, [FKType] uniqueidentifier DEFAULT ''c42eef38-c2b4-45be-ae86-a5e600bad859'' NOT NULL REFERENCES [madds].[tblObjects] ([PK]), [TInput] nvarchar(1) UNIQUE NOT NULL, [FKReceiptType] uniqueidentifier UNIQUE NOT NULL FOREIGN KEY REFERENCES [bigbang].[tblReceiptTypes] ([PK]))');

/*-- -Insert(s): [madds].[tblEntities] */
INSERT INTO [madds].[tblEntities] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKNameSpace],[FKObject]) VALUES('7128ab07-dc2e-4806-92f3-a5e600bd1012','2016-04-12T11:28:21.397','2016-04-12T11:28:21.397','94ab0a6d-25a1-11da-91c2-000b6abc6ae9','c37b81f0-860f-4868-9177-9e15008b3efd','c42eef38-c2b4-45be-ae86-a5e600bad859');

/*-- -Insert(s): [madds].[tblObjectMembers] */
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('bf82a958-6acb-4f04-93f4-a5e600bc00fe','2016-04-12T11:24:30.083','2016-04-12T11:24:30.083','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','c42eef38-c2b4-45be-ae86-a5e600bad859',1,N'Input',N'The type as it exists in the receipts'' file','94ab0a78-25a1-11da-91c2-000b6abc6ae9',1,null,0,1,N'TInput',null);
INSERT INTO [madds].[tblObjectMembers] ([PK],[_TSCreate],[_TSUpdate],[FKType],[FKObject],[NOrder],[MemberName],[MemberComments],[FKTypeDef],[Size],[FKRefersTo],[Nullable],[Unique],[TableColumn],[Precision]) VALUES('a0d3cdf8-c491-4772-a4ef-a5e600bc78f0','2016-04-12T11:26:12.430','2016-04-12T11:26:12.430','94ab0a6f-25a1-11da-91c2-000b6abc6ae9','c42eef38-c2b4-45be-ae86-a5e600bad859',2,N'Receipt Type',N'The type of BB''s receipt associated with th input','94ab0a77-25a1-11da-91c2-000b6abc6ae9',null,'afe7cc47-b44f-442d-8cf4-9f8a00db2637',0,1,N'FKReceiptType',null);

/*-- -Insert(s): [madds].[tblObjects] */
INSERT INTO [madds].[tblObjects] ([PK],[_TSCreate],[_TSUpdate],[FKType],[ObjName],[ObjComments],[MainTable],[ClassName],[FKApplication]) VALUES('c42eef38-c2b4-45be-ae86-a5e600bad859','2016-04-12T11:20:17.180','2016-04-12T11:26:17.953','94ab0a6b-25a1-11da-91c2-000b6abc6ae9',N'Liberty Translator',N'Receipt type Translator for Liberty',N'tblLibertyTranslator',null,'9003fda0-e35b-4c2c-85f8-9e15008b262c');

-- Inserts the rows in the Liberty translator
INSERT INTO bigbang.tblLibertyTranslator([PK],[TInput],[FKReceiptType])
     VALUES(NEWID(), 'N', '36564F0F-2180-4794-B0EC-9F900111D2A8');
           
INSERT INTO bigbang.tblLibertyTranslator([PK],[TInput],[FKReceiptType])
     VALUES(NEWID(), 'C', '6B91D626-4CAD-4F53-8FD6-9F900111C39F');        

INSERT INTO bigbang.tblLibertyTranslator([PK],[TInput],[FKReceiptType])
     VALUES(NEWID(), 'E', 'BFC1AE6D-53E8-41AF-84BE-9F900111D967');        
 
INSERT INTO bigbang.tblLibertyTranslator([PK],[TInput],[FKReceiptType])
     VALUES(NEWID(), 'M', '3B127029-C133-4EB4-AD1E-9F900111EF2A');      
     
/*-- -Insert(s): [bigbang].[tblBBReceiptImportStatus] */
INSERT INTO [bigbang].[tblBBReceiptImportStatus] ([PK],[_TSCreate],[_TSUpdate],[FKType],[StatusText]) VALUES('23d38aaf-742a-4cd4-8951-3398fd37031e','2016-04-13T16:39:16.617','2016-04-13T16:39:16.617','ee1bf5d6-4116-410f-9a9b-a0a700f4f569',N'Código de Agente incorrecto');
INSERT INTO [bigbang].[tblBBReceiptImportStatus] ([PK],[_TSCreate],[_TSUpdate],[FKType],[StatusText]) VALUES('9f9f36cf-4a94-421e-84ed-4e1811053160','2016-04-14T11:30:29.573','2016-04-14T11:30:29.573','ee1bf5d6-4116-410f-9a9b-a0a700f4f569',N'Pagamento Criado');
INSERT INTO [bigbang].[tblBBReceiptImportStatus] ([PK],[_TSCreate],[_TSUpdate],[FKType],[StatusText]) VALUES('cd9f9f90-8315-4ef5-86fa-63b889eaa332','2016-04-15T16:36:12.803','2016-04-15T16:36:12.803','ee1bf5d6-4116-410f-9a9b-a0a700f4f569',N'Pagamento já existente');
     