package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	// NameSpaces
    public static final UUID NSID_BigBang  = UUID.fromString("C37B81F0-860F-4868-9177-9E15008B3EFD");
    public static final UUID NSID_CredEGS  = UUID.fromString("A1CC31A3-E471-4568-B5FC-9E15008E98C9");
    public static final UUID NSID_AMartins = UUID.fromString("90CB8FFB-1555-49D8-9594-9E15008EDC72");

	// Objects
    public static final UUID ObjID_AppParams                = UUID.fromString("5485B660-A102-441A-912C-7B960584399F");
    public static final UUID ObjID_Template                 = UUID.fromString("F13322EE-E13E-4562-B723-9FF100F5E6BE");
	public static final UUID ObjID_PrintSet                 = UUID.fromString("FEBF3C68-0891-4B4D-AD90-A021010A219F");
	public static final UUID ObjID_PrintSetDocument         = UUID.fromString("526FDF76-E1D2-4FA8-B048-A021010ADF38");
	public static final UUID ObjID_PrintSetDetail           = UUID.fromString("6780283B-97E8-4ED3-B4BF-A021010B98C2");
	public static final UUID ObjID_InsurerReceipt           = UUID.fromString("180DA3B9-FFA2-4A0E-BEBB-A094010EF654");
	public static final UUID ObjID_FileProcesser            = UUID.fromString("648CB5A9-3ABB-4995-B5FA-A0A600EDF739");

	// Agenda Objects
	public static final UUID ObjID_AgendaItem               = UUID.fromString("F850E623-E686-4575-A2D8-9F640128FCD7");
	public static final UUID ObjID_AgendaProcess            = UUID.fromString("3620AAEE-A950-421C-A5D3-9F640135D7D8");
	public static final UUID ObjID_AgendaOp                 = UUID.fromString("3C01C389-71FE-43A8-AEE7-9F640135635D");

	// General Objects
	public static final UUID ObjID_GenSys                   = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");

	public static final UUID ObjID_CostCenter               = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations              = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");
	public static final UUID ObjID_Company                  = UUID.fromString("7B203DCA-FFAC-46B2-B849-9EBC009DB127");
	public static final UUID ObjID_Mediator                 = UUID.fromString("8A33B9DD-001A-401F-AA7E-9EBB00E9D24F");
	public static final UUID ObjID_MediatorDeal             = UUID.fromString("2EAECB2F-ADCE-4E9B-B8B0-A09700BCB82A");
	public static final UUID ObjID_Contact                  = UUID.fromString("2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C");
	public static final UUID ObjID_ContactInfo              = UUID.fromString("069434F6-8EE7-4DFE-96C9-9EC401014608");
	public static final UUID ObjID_Document                 = UUID.fromString("794F44DB-191A-4E17-9217-9ECC012A8AC2");
	public static final UUID ObjID_DocInfo                  = UUID.fromString("75620A04-D0C7-43D4-8425-9ECC012C0E91");
	public static final UUID ObjID_LineCategory             = UUID.fromString("3FF829D9-5C33-4C91-A42B-9EC900F013CB");
	public static final UUID ObjID_Line                     = UUID.fromString("A9A1CE62-06A1-4761-A1FC-9EC900F234B0");
	public static final UUID ObjID_SubLine                  = UUID.fromString("FBCD74E1-A280-4443-9BB4-9EC900F4A4B9");
	public static final UUID ObjID_Coverage                 = UUID.fromString("007022A0-6DFA-498B-9DC4-9EC900F5219F");
	public static final UUID ObjID_Tax                      = UUID.fromString("43644146-07C4-4E63-9E87-9EC900F6EB73");
	public static final UUID ObjID_ClientGroup              = UUID.fromString("DC89D454-CCCC-441E-BCE3-9EE600AFCCCD");

	// Client Objects
	public static final UUID ObjID_Client                   = UUID.fromString("D535A99E-149F-44DC-A28B-9EE600B240F5");

	// Quote Request Objects
	public static final UUID ObjID_QuoteRequest             = UUID.fromString("6ABD2A4A-ECBD-46A8-A4E6-A00500CDAE4A");
	public static final UUID ObjID_QuoteRequestSubLine      = UUID.fromString("DC56BAB3-CEB5-4F5C-A23E-A00500FD2AB2");
	public static final UUID ObjID_QuoteRequestCoverage     = UUID.fromString("5BE87B3A-787B-4C02-BACF-A00500FF499B");
	public static final UUID ObjID_QuoteRequestObject       = UUID.fromString("B594AB7F-573F-4401-A369-A00500F9D2D8");
	public static final UUID ObjID_QuoteRequestValue        = UUID.fromString("2E9A755C-55C9-4A75-9BE1-A005010066F9");

	// Negotiation Objects
	public static final UUID ObjID_Negotiation              = UUID.fromString("0D50EB51-725D-4741-8618-9FFD00E918D3");

	// Policy Objects
	public static final UUID ObjID_Policy                   = UUID.fromString("D0C5AE6B-D340-4171-B7A3-9F81011F5D42");
	public static final UUID ObjID_PolicyCoInsurer          = UUID.fromString("A74A6A17-ACF6-463C-983B-9FEB00FA4957");
	public static final UUID ObjID_PolicyCoverage           = UUID.fromString("AE5F9DD7-2863-4C5B-AE0B-9F9C00229D02");
	public static final UUID ObjID_PolicyObject             = UUID.fromString("3A3316D2-9D7C-4FD1-8486-9F9C0012E119");
	public static final UUID ObjID_PolicyExercise           = UUID.fromString("DEE32F69-B33D-4427-AD5B-9F9C001607F2");
	public static final UUID ObjID_PolicyValue              = UUID.fromString("F1D23471-0AA8-40A3-A691-9F9C001843CC");

	// Sub-Policy Objects
	public static final UUID ObjID_SubPolicy                = UUID.fromString("C7BC8D2F-BD61-43D5-9347-9FF300EE9986");
	public static final UUID ObjID_SubPolicyCoverage        = UUID.fromString("53EF35E5-AACD-4916-A212-9FF800CEAA89");
	public static final UUID ObjID_SubPolicyObject          = UUID.fromString("C3480E17-821B-4DCB-BF42-9FF800D1C470");
	public static final UUID ObjID_SubPolicyValue           = UUID.fromString("523A1872-89AE-491F-BBF4-9FF800D69B6A");

	// Receipt Objects
	public static final UUID ObjID_Receipt                  = UUID.fromString("B24E4BF7-382C-40EE-8120-9F8A00DABB81");
	public static final UUID ObjID_DebitNote                = UUID.fromString("812774ED-4016-4024-921C-9FF000EAC900");
	public static final UUID ObjID_InsurerAccountingSet     = UUID.fromString("93E5E190-9A9E-4A3D-BF43-A02900CA48F7");
	public static final UUID ObjID_InsurerAccountingMap     = UUID.fromString("47166D34-899D-4B6B-92FD-A02900D53271");
	public static final UUID ObjID_InsurerAccountingDetail  = UUID.fromString("D1A14730-C986-4F4A-9FE3-A02900D68135");
	public static final UUID ObjID_MediatorAccountingSet    = UUID.fromString("539A2C1F-59DD-4B60-81F8-A02900F8E72F");
	public static final UUID ObjID_MediatorAccountingMap    = UUID.fromString("FFA5422A-E194-411B-AAA1-A02900FA19DC");
	public static final UUID ObjID_MediatorAccountingDetail = UUID.fromString("0FBA21E1-741C-4DCE-8482-A02900FDBA69");

	// Casualty Objects
	public static final UUID ObjID_Casualty                 = UUID.fromString("EFFA56DF-8F3C-4361-A584-A02E00C4F0C5");

	// SubCasualty Objects
	public static final UUID ObjID_SubCasualty              = UUID.fromString("D5FD2D1B-59FB-4171-961A-A02E0121C81B");
	public static final UUID ObjID_SubCasualtyItem          = UUID.fromString("A62F4F4F-7F10-4391-ABF4-A035012009A9");

	// Expense Objects
	public static final UUID ObjID_Expense                  = UUID.fromString("09963260-CDB1-4207-B856-A03800B8AFC8");

	// Manager Transfer Objects
	public static final UUID ObjID_MgrXFer                  = UUID.fromString("66EC1066-AD5A-430C-9A70-9F65013F5453");
	public static final UUID ObjID_MgrXFerProc              = UUID.fromString("324A0D54-4ED9-4925-97D2-9F6D0135A37E");

	// Info Request Objects
	public static final UUID ObjID_InfoRequest              = UUID.fromString("1D10FF86-70B5-4362-A8F3-9F7C00BC6DE4");
	public static final UUID ObjID_RequestAddress           = UUID.fromString("C865EAD8-526A-4447-9CBB-9FE80109E9C5");

	// Extern Request Objects
	public static final UUID ObjID_ExternRequest            = UUID.fromString("BD9E1BEA-D02B-40FE-BB3E-9FEA00CA7ECD");

	// Signature Request Objects
	public static final UUID ObjID_SignatureRequest         = UUID.fromString("A864FABC-8DCE-4D97-9A50-A02A00F7CBB5");

	// DAS Requset Objects
	public static final UUID ObjID_DASRequest               = UUID.fromString("F7137CCF-744D-4BD8-918A-A03000F0F182");

	// Report Objects
	public static final UUID ObjID_ReportDef                = UUID.fromString("CA478376-2F58-4140-B5EE-A032010F48E5");
	public static final UUID ObjID_ReportParam              = UUID.fromString("4D801865-DC6B-4A8F-8301-A032012782BF");

	// Typified Lists
	public static final UUID ObjID_ContactType              = UUID.fromString("228F6A73-1335-4C99-8DD0-9EEE012964BA");
	public static final UUID ObjID_CInfoType                = UUID.fromString("03C5B78E-D71C-49F2-A079-9EC40111DCC0");
	public static final UUID ObjID_DocType                  = UUID.fromString("B4DBEE18-FA81-471D-A9F5-9ECC012A028D");
	public static final UUID ObjID_CommProfile              = UUID.fromString("5F40713C-1FE7-4715-AC24-9EBB00E53392");
	public static final UUID ObjID_ClientType               = UUID.fromString("40A63D68-E2A6-40AB-B3A4-9EE600B91EBE");
	public static final UUID ObjID_ClientSubtype            = UUID.fromString("1907DB1B-6F75-4715-A210-9EE600B9D072");
	public static final UUID ObjID_OpProfile                = UUID.fromString("11921769-CDF3-447A-8E6B-9ECA01761BAA");
	public static final UUID ObjID_Sex                      = UUID.fromString("2549E951-2747-4300-982E-9ECA01756B91");
	public static final UUID ObjID_MaritalStatus            = UUID.fromString("B560424E-1DF9-46CA-8B67-9ECA01774B00");
	public static final UUID ObjID_Profession               = UUID.fromString("088C8CCE-A1D1-4976-808F-9ECA0177AB56");
	public static final UUID ObjID_CAE                      = UUID.fromString("14B89884-475A-4357-BB7D-9EE600C3CE6E");
	public static final UUID ObjID_Size                     = UUID.fromString("5E489662-2AAF-4348-95E0-9EE600C4A71F");
	public static final UUID ObjID_SalesVolume              = UUID.fromString("5C4E267C-B306-482F-8636-9EE600C586B6");
	public static final UUID ObjID_Durations                = UUID.fromString("A522A5B3-245B-4F8B-97F1-9F810120FAB6");
	public static final UUID ObjID_Fractioning              = UUID.fromString("28B2D442-2CF3-4E14-8430-9F8101229DA8");
	public static final UUID ObjID_ReceiptType              = UUID.fromString("AFE7CC47-B44F-442D-8CF4-9F8A00DB2637");
	public static final UUID ObjID_ReceiptReturnMotives     = UUID.fromString("D6298E98-6FBD-47E9-9D4D-A08A0102DF87");
	public static final UUID ObjID_PolicyStatus             = UUID.fromString("EF9C2F88-2AC7-44CF-9AD6-9F98012A7236");
	public static final UUID ObjID_ObjectType               = UUID.fromString("6ADF0A94-5004-41AF-A7FF-9F95013DABB8");
	public static final UUID ObjID_ExercisePeriod           = UUID.fromString("93315017-5744-4E43-95AB-9F9601200F1E");
	public static final UUID ObjID_RequestType              = UUID.fromString("B437ECD7-11D2-4C55-B6DD-9FE80101ECC8");
	public static final UUID ObjID_RequestCancelMotives     = UUID.fromString("F79E335C-FD6F-43BE-8535-9FE900C1CAF4");
	public static final UUID ObjID_PolicyVoidingMotives     = UUID.fromString("B36C9AB7-0052-4C2E-B392-9FF200CF6022");
	public static final UUID ObjID_ReceiptStatus            = UUID.fromString("35C78290-7FFE-4D7B-BD9D-A02000BC6D38");
	public static final UUID ObjID_PaymentType              = UUID.fromString("C480B530-C6E2-49A0-BE15-A02200E88280");
	public static final UUID ObjID_Bank                     = UUID.fromString("9954EADC-D7BE-4858-B1DE-A02200F45247");
	public static final UUID ObjID_DamageType               = UUID.fromString("6D2B02AF-A092-475F-85C7-A035012321FE");

	public static final UUID ObjID_FieldType                = UUID.fromString("0D8E4FEE-AE4D-4429-A517-9EC900F80A20");
	public static final UUID ObjID_FieldValues              = UUID.fromString("D50A75B9-5F44-4BF2-9060-9F960121503A");

	public static final UUID ObjID_TypifiedText             = UUID.fromString("0F218912-1626-4A2C-BF68-9FE100F72735");


	// Process Scripts
	public static final UUID ProcID_GenSys           = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");
	public static final UUID ProcID_Client           = UUID.fromString("100E701A-EDC5-4D9C-A221-9F09013D7954");
	public static final UUID ProcID_QuoteRequest     = UUID.fromString("E4F23C44-FEDF-440A-95D0-A00500D3B286");
	public static final UUID ProcID_Negotiation      = UUID.fromString("5A22FEB4-A96A-4BF6-B5EB-9FFD00EBD384");
	public static final UUID ProcID_Policy           = UUID.fromString("29145166-59AC-452E-8C2B-9F81013A39AC");
	public static final UUID ProcID_SubPolicy        = UUID.fromString("08C796D6-5622-4FF1-B3AB-9FF300F28ABC");
	public static final UUID ProcID_Receipt          = UUID.fromString("62D0A72A-525E-450C-9917-9F8A00EB38AC");
	public static final UUID ProcID_Casualty         = UUID.fromString("6C9562DE-D8C6-4CEC-A4DA-A02E00C735E9");
	public static final UUID ProcID_SubCasualty      = UUID.fromString("80B7A9BC-8710-4063-A99E-A02E01220F4E");
	public static final UUID ProcID_Expense          = UUID.fromString("A4EFBA8F-D669-4066-A31F-A03800BFB924");
	public static final UUID ProcID_MgrXFer          = UUID.fromString("BBF5FFD1-4249-48AE-BB2D-9F6501420E7B");
	public static final UUID ProcID_InfoRequest      = UUID.fromString("5DBEF1CD-DEB8-4731-8C6F-9FE500F50BB9");
	public static final UUID ProcID_ExternRequest    = UUID.fromString("83759D78-C30C-4928-B600-9FEA00CC2349");
	public static final UUID ProcID_SignatureRequest = UUID.fromString("D705396D-10CE-42E5-BB24-A02A00FF6115");
	public static final UUID ProcID_DASRequest       = UUID.fromString("41F2CCA4-5114-4A1F-8E97-A03000F1F404");

	// Operations

	// General System Operations
	public static final UUID OPID_General_ManageCostCenters              = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_General_ManageUsers                    = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");
	public static final UUID OPID_General_ManageMediators                = UUID.fromString("EDA3659D-4BE9-4779-B0C9-9EB500A07726");
	public static final UUID OPID_General_ManageCompanies                = UUID.fromString("83081039-C3C0-4BAC-8B24-9EB500A0A3EB");
	public static final UUID OPID_General_ManageLines                    = UUID.fromString("7CE5A4B8-9E13-4F14-BDDA-9EB500A0D171");
	public static final UUID OPID_General_ManageCoefficients             = UUID.fromString("BF76CE8C-C9A7-408B-B623-9EB500A0F07F");
	public static final UUID OPID_General_ManageGroups                   = UUID.fromString("08E5A828-0AF2-4347-A57A-9EB500A11526");
	public static final UUID OPID_General_CreateClient                   = UUID.fromString("214118B8-C43C-4941-9D1E-9EB500A1381C");
	public static final UUID OPID_General_TriggerDeleteClient            = UUID.fromString("EA560433-17DF-4E0A-A7BC-9F3300EAFD78");
	public static final UUID OPID_General_UndoGroups                     = UUID.fromString("742FCF0E-FD17-439A-A810-9F3300CCF026");
	public static final UUID OPID_General_UndoCoefficients               = UUID.fromString("B841D5C5-CCA9-4E25-B88C-9F3300E64E61");
	public static final UUID OPID_General_UndoCostCenters                = UUID.fromString("438CC18F-51A7-4037-A5B4-9F3300CCBD74");
	public static final UUID OPID_General_UndoCompanies                  = UUID.fromString("266E812E-53F0-4968-AC6C-9F3300E68E03");
	public static final UUID OPID_General_UndoLines                      = UUID.fromString("D4E44325-B8F8-450B-9949-9F3300E6AB0E");
	public static final UUID OPID_General_UndoMediators                  = UUID.fromString("3A1572FF-B1C6-4DEC-9819-9F3300E6DA84");
	public static final UUID OPID_General_UndoUsers                      = UUID.fromString("F71DF6CC-C58E-4AA7-9BE5-9F3300E6FD4F");
	public static final UUID OPID_General_UndoDeleteClient               = UUID.fromString("711B9533-8298-4DA5-BFD1-9F3D01211F51");

	// Client Operations
	public static final UUID OPID_Client_AutoProcessData                 = UUID.fromString("A9A8F4ED-74C2-473C-873C-9F0901435C1C");
	public static final UUID OPID_Client_TriggerDisallowPolicies         = UUID.fromString("6A031641-DEC8-46D3-B402-9F0901474FE9");
	public static final UUID OPID_Client_AutoProcessSubProcs             = UUID.fromString("62F70BDD-B9AC-4733-99FF-9F8B0128BAD5");
	public static final UUID OPID_Client_TriggerDisallowDelete           = UUID.fromString("6B5518F6-01E1-4C75-94B3-9F8B0129365E");
	public static final UUID OPID_Client_ManageData                      = UUID.fromString("AE3CC7F0-BE2C-4548-87B5-9F09014072B8");
	public static final UUID OPID_Client_UndoManageData                  = UUID.fromString("61097A3F-2322-4C06-B015-9F090143A35B");
	public static final UUID OPID_Client_MergeIntoAnother                = UUID.fromString("895BB8D7-1263-4230-B1C4-9F8801189ADF");
	public static final UUID OPID_Client_ExternMergeOtherHere            = UUID.fromString("A5B43B14-43A6-4C0F-A410-9F0901425E48");
	public static final UUID OPID_Client_UndoMergeOtherHere              = UUID.fromString("33704B93-F5A5-481F-A0EF-9F090143B96C");
	public static final UUID OPID_Client_CreateInfoRequest               = UUID.fromString("26185760-A862-4CE4-9620-9F090142B18A");
	public static final UUID OPID_Client_ExecMgrXFer                     = UUID.fromString("C75EEC27-780C-4C0C-9FD7-9F090142CB10");
	public static final UUID OPID_Client_UndoExecMgrXFer                 = UUID.fromString("E7ABBF15-4DB6-4376-92FD-9F6D01792783");
	public static final UUID OPID_Client_ExternMassUndoMgrXFer           = UUID.fromString("91530E4E-9F84-4240-B1F3-9F6D017FE928");
	public static final UUID OPID_Client_CreateQuoteRequest              = UUID.fromString("F9BF0190-20E3-45F8-9EBE-9F090142E605");
	public static final UUID OPID_Client_ExternDeleteQuoteRequest        = UUID.fromString("68251204-CDAC-41CC-AADC-9F8B012A2274");
	public static final UUID OPID_Client_UndoDeleteQuoteRequest          = UUID.fromString("AAA14707-B11F-42B6-BEF5-9F8B012B1424");
	public static final UUID OPID_Client_CreatePolicy                    = UUID.fromString("5903FA18-8F4D-4DD3-B4AE-9F090143186E");
	public static final UUID OPID_Client_ExternDeletePolicy              = UUID.fromString("8BFB93C8-2CCF-423F-9E35-9F8200CA7E06");
	public static final UUID OPID_Client_UndoDeletePolicy                = UUID.fromString("E5E66BC3-4B16-42F4-B653-9F8200CE2893");
	public static final UUID OPID_Client_CreateRiskAnalysis              = UUID.fromString("AEC0C809-28EF-478D-B5B1-9F8B012E1757");
	public static final UUID OPID_Client_ExternDeleteRiskAnalysis        = UUID.fromString("D301D131-D156-49EF-B727-9F8B012A49B7");
	public static final UUID OPID_Client_UndoDeleteRiskAnalysis          = UUID.fromString("0BD92466-BCEB-4854-BF84-9F8B012AF683");
	public static final UUID OPID_Client_CreateCasualty                  = UUID.fromString("6CCF24EB-AF80-4B63-A50A-9F8B012E01BB");
	public static final UUID OPID_Client_ExternDeleteCasualty            = UUID.fromString("EFC6FC1E-BC09-4D94-B60D-9F8B012A707E");
	public static final UUID OPID_Client_UndoDeleteCasualty              = UUID.fromString("A7FB514E-B632-4247-9BCE-9F8B012B2DF6");
	public static final UUID OPID_Client_DeleteClient                    = UUID.fromString("2227C32A-0AF5-4ECC-B807-9F090143401F");
	public static final UUID OPID_Client_ExternResumeClient              = UUID.fromString("380BC979-5C71-4991-A382-9F8801257EB5");

	// Quote Request Operations
	public static final UUID OPID_QuoteRequest_AutoProcessSubs           = UUID.fromString("3062A359-E26D-4255-8A65-A00C01215B6D");
	public static final UUID OPID_QuoteRequest_TriggerDisallowDelete     = UUID.fromString("01782F69-5492-469D-BF5A-A00C01219EA0");
	public static final UUID OPID_QuoteRequest_ManageData                = UUID.fromString("450E32C7-C544-4030-92C7-A00C0121D1B6");
	public static final UUID OPID_QuoteRequest_UndoManageData            = UUID.fromString("906BE06B-71EF-4D8E-AA4B-A00C012219FC");
	public static final UUID OPID_QuoteRequest_IncludeClientAsObject     = UUID.fromString("640FB958-3E02-4E2A-9415-A00C0122859F");
	public static final UUID OPID_QuoteRequest_SendProposal              = UUID.fromString("C26CAAB3-85F2-45C0-9627-A00C0122B98E");
	public static final UUID OPID_QuoteRequest_ReceiveProposalReply      = UUID.fromString("C22A16C5-F572-49AD-AC03-A00C01230826");
	public static final UUID OPID_QuoteRequest_UndoReceiveReply          = UUID.fromString("A640F147-ECFF-4AA4-930C-A00C0123A5D4");
	public static final UUID OPID_QuoteRequest_CreateInfoRequest         = UUID.fromString("113799FC-D92B-42DB-A472-A00C0124AB91");
	public static final UUID OPID_QuoteRequest_ExecMgrXFer               = UUID.fromString("D47D25C0-E8AE-4AC7-AB76-A00C01252C9F");
	public static final UUID OPID_QuoteRequest_UndoExecMgrXFer           = UUID.fromString("B64F3E1D-5337-46DB-BF47-A00C0125CC76");
	public static final UUID OPID_QuoteRequest_ExternMassUndoMgrXFer     = UUID.fromString("206D9DA9-5E3A-41CF-9EB0-A00C0125843C");
	public static final UUID OPID_QuoteRequest_CreateNegotiation         = UUID.fromString("97C06D8F-B1DD-4687-B507-A00C0126B601");
	public static final UUID OPID_QuoteRequest_ExternDeleteNegotiation   = UUID.fromString("CB11B57B-0F6F-469C-B3D5-A00C0126F655");
	public static final UUID OPID_QuoteRequest_UndoDeleteNegotiation     = UUID.fromString("EFD36D93-A8C1-4750-B782-A00C012725DE");
	public static final UUID OPID_QuoteRequest_CreateRiskAnalysis        = UUID.fromString("EAC1BAEA-8AB0-4113-86A8-A00C012770D4");
	public static final UUID OPID_QuoteRequest_ExternDeleteRiskAnalysis  = UUID.fromString("0F4F53A1-FD75-4E85-B829-A00C0127C1D9");
	public static final UUID OPID_QuoteRequest_UndoDeleteRiskAnalysis    = UUID.fromString("8263DABA-62E4-42AB-A58A-A00C0127F8EB");
	public static final UUID OPID_QuoteRequest_CloseProcess              = UUID.fromString("2E49B889-594C-49A6-B25D-A00C012876B8");
	public static final UUID OPID_QuoteRequest_UndoCloseProcess          = UUID.fromString("74B79297-107F-42BE-908A-A00C0128B741");
	public static final UUID OPID_QuoteRequest_DeleteQuoteRequest        = UUID.fromString("57172AC9-8CDF-4C34-825F-A00C01294D35");
	public static final UUID OPID_QuoteRequest_ExternResumeQuoteRequest  = UUID.fromString("CCDEE802-5BD8-40C9-B5B2-A00C01298D5C");

	// Negotiation Operations
	public static final UUID OPID_Negotiation_ManageData                 = UUID.fromString("FA636D7A-FEF3-4F87-9D1A-9FFD010242ED");
	public static final UUID OPID_Negotiation_UndoManageData             = UUID.fromString("8628348C-CAAE-4886-AE40-9FFD0102612E");
	public static final UUID OPID_Negotiation_SendQuoteRequest           = UUID.fromString("A71B027F-716B-48B8-9FDC-9FFD0102962F");
	public static final UUID OPID_Negotiation_RepeatQuoteRequest         = UUID.fromString("7F12F9E3-9F5D-4365-9F30-9FFD0102D342");
	public static final UUID OPID_Negotiation_CancelNegotiation          = UUID.fromString("500AAB3B-9035-4F4F-B12E-9FFD0102F303");
	public static final UUID OPID_Negotiation_UndoCancelNegotiation      = UUID.fromString("6E6BB71D-9F2C-4F3B-A9A4-9FFD01031517");
	public static final UUID OPID_Negotiation_ReceiveQuote               = UUID.fromString("E43282B7-FBE2-49FB-B227-9FFD01035C99");
	public static final UUID OPID_Negotiation_UndoReceiveQuote           = UUID.fromString("1756DE91-A0C6-4C0A-A03F-9FFD0103A47D");
	public static final UUID OPID_Negotiation_CreateExternRequest        = UUID.fromString("16FCE46C-EFE4-497A-BFB8-9FFD01048B6C");
	public static final UUID OPID_Negotiation_UndoCreateExternRequest    = UUID.fromString("04BC7C61-E053-4E1B-91A7-A00000FA0B50");
	public static final UUID OPID_Negotiation_CreateInfoRequest          = UUID.fromString("3280A4B4-BA71-418E-AF36-9FFD0104681E");
	public static final UUID OPID_Negotiation_ExternAllowGrant           = UUID.fromString("2E88EAFA-3C5B-485D-A94C-9FFD0104BEC3");
	public static final UUID OPID_Negotiation_SendGrant                  = UUID.fromString("65EB3752-A87B-4658-9FF1-9FFD0104EA07");
	public static final UUID OPID_Negotiation_TriggerDisallowPolicies    = UUID.fromString("684E6897-BF20-4F35-8EB1-9FFD01052561");
	public static final UUID OPID_Negotiation_CreatePolicy               = UUID.fromString("2255D403-CADD-4B3E-B329-9FFD01055C53");
	public static final UUID OPID_Negotiation_DeleteNegotiation          = UUID.fromString("796C9A67-2B7E-40D0-974A-9FFD0105890E");
	public static final UUID OPID_Negotiation_ExternResumeNegotiation    = UUID.fromString("1E5926C3-5F36-4FC6-810F-9FFD0105AB76");
	public static final UUID OPID_Negotiation_CloseProcess               = UUID.fromString("8B449385-BAD2-416B-AA49-9FFD0125214A");
	public static final UUID OPID_Negotiation_UndoCloseProcess           = UUID.fromString("B37BD1B9-BDF4-442E-AA77-9FFD0125C4AC");

	// Policy Operations
	public static final UUID OPID_Policy_AutoProcessSubProcs             = UUID.fromString("84A323A5-8F42-4913-B796-9FD301102BAC");
	public static final UUID OPID_Policy_TriggerDisallowDelete           = UUID.fromString("FE146209-A749-4463-B73B-9FD3010F0E45");
	public static final UUID OPID_Policy_PerformComputations             = UUID.fromString("7DC1A798-31AA-49A1-B2DC-9FD4010F7593");
	public static final UUID OPID_Policy_ManageData                      = UUID.fromString("1F7B31EC-9388-4EA0-816C-9F81013A8ED4");
	public static final UUID OPID_Policy_UndoManageData                  = UUID.fromString("98304BAB-0C9F-4089-879C-9F81013AFDB7");
	public static final UUID OPID_Policy_ValidatePolicy                  = UUID.fromString("841E9154-8037-46F7-8B98-9FD3011A9605");
	public static final UUID OPID_Policy_ForceValidatePolicy             = UUID.fromString("607BC2A4-136A-474E-A54F-9FD3011B17BE");
	public static final UUID OPID_Policy_UndoValidatePolicy              = UUID.fromString("7E90AFE9-ED4D-4EA6-83B8-9FD3011BE799");
	public static final UUID OPID_Policy_OpenNewExercise                 = UUID.fromString("C86BB300-28C1-47EF-A48D-9FD4010F9149");
	public static final UUID OPID_Policy_UndoOpenExercise                = UUID.fromString("D7CB22BA-C37D-40DD-948B-9FD4010FAA85");
	public static final UUID OPID_Policy_IncludeObject                   = UUID.fromString("984226BB-33EA-4988-A802-9FD4010FC200");
	public static final UUID OPID_Policy_IncludeClientAsObject           = UUID.fromString("783EFBDB-44DB-4CAA-BF88-9FD4010FDB91");
	public static final UUID OPID_Policy_UndoIncludeObject               = UUID.fromString("6C57D11D-6E56-405B-AACE-9FD4010FF6F5");
	public static final UUID OPID_Policy_ExcludeObject                   = UUID.fromString("CB5A1425-C032-4678-B9B2-9FD401149CB8");
	public static final UUID OPID_Policy_UndoExcludeObject               = UUID.fromString("6B10D212-CE69-4E02-9078-9FD40114D696");
	public static final UUID OPID_Policy_CreateDebitNote                 = UUID.fromString("4CC51237-FC83-4B13-A8A9-9FD401100FE2");
	public static final UUID OPID_Policy_UndoDebitNote                   = UUID.fromString("16C44D7D-6D09-4957-9051-9FD40110291E");
	public static final UUID OPID_Policy_ExternDisallowUndoDebitNote     = UUID.fromString("1E90FD94-1C63-4984-AB2E-9FD401105E35");
	public static final UUID OPID_Policy_CreateRecurringInfoProcess      = UUID.fromString("5A89B28E-3098-490F-8AD0-9FD401109F6D");
	public static final UUID OPID_Policy_CreateInfoRequest               = UUID.fromString("DF06C139-4D61-44D1-81EE-9FD30118E9D3");
	public static final UUID OPID_Policy_CreateCompInfoRequest           = UUID.fromString("3F0120C1-F64E-4806-8AB2-9FD40110D264");
	public static final UUID OPID_Policy_ExecMgrXFer                     = UUID.fromString("EF9C257E-323C-4BA0-A158-9F9000D8A755");
	public static final UUID OPID_Policy_UndoExecMgrXFer                 = UUID.fromString("5364E6D1-B55D-445F-A961-9F9000DA6202");
	public static final UUID OPID_Policy_ExternMassUndoMgrXFer           = UUID.fromString("E8FC9B51-A3EF-4E0C-AA18-9F9000DC5D1C");
	public static final UUID OPID_Policy_CreateReceipt                   = UUID.fromString("C4117861-16EB-40DB-A771-9F8A00EA6B32");
	public static final UUID OPID_Policy_ExternDeleteReceipt             = UUID.fromString("92982C76-0AEA-4028-A29B-9F8A00EA9F48");
	public static final UUID OPID_Policy_UndoDeleteReceipt               = UUID.fromString("DE651450-BDA1-4DBA-A269-9F8A00EACA60");
	public static final UUID OPID_Policy_CreateSubPolicy                 = UUID.fromString("DF27A2DA-1B36-4F1C-B242-9FD40110F495");
	public static final UUID OPID_Policy_ExternDeleteSubPolicy           = UUID.fromString("99831C5E-E137-4B5E-B18D-9FD401111B36");
	public static final UUID OPID_Policy_UndoDeleteSubPolicy             = UUID.fromString("41F07FC7-51A1-4B62-A37C-9FD4011138CA");
	public static final UUID OPID_Policy_CreateNegotiation               = UUID.fromString("852BF3BF-F04A-494D-A308-9FD4011152C4");
	public static final UUID OPID_Policy_ExternDeleteNegotiation         = UUID.fromString("5EFE2E2E-6B7B-43B4-8970-9FD4011166E9");
	public static final UUID OPID_Policy_UndoDeleteNegotiation           = UUID.fromString("CA6217B8-34C8-49E4-ADB7-9FD401117C22");
	public static final UUID OPID_Policy_CreateRiskAnalysis              = UUID.fromString("731AAB07-BBE4-4BA2-8A37-9FD40111E7EA");
	public static final UUID OPID_Policy_ExternDeleteRiskAnalysis        = UUID.fromString("54284976-C57C-4485-85E8-9FD401120237");
	public static final UUID OPID_Policy_UndoDeleteRiskAnalysis          = UUID.fromString("643F0129-F12B-4205-BFE7-9FD40112B6E7");
	public static final UUID OPID_Policy_CreateHealthExpense             = UUID.fromString("A7A22C6D-ED66-444D-9E5E-9FD40112D52C");
	public static final UUID OPID_Policy_ExternDeleteHealthExpense       = UUID.fromString("C0DAB4FC-A8CE-4D48-A31D-9FD40112FC86");
	public static final UUID OPID_Policy_UndoDeleteHealthExpense         = UUID.fromString("8979C0F8-09BF-4397-BA9E-9FD401131CF8");
	public static final UUID OPID_Policy_PolicySubstitution              = UUID.fromString("66504952-BCFD-4B6A-B8F0-9FD401138D07");
	public static final UUID OPID_Policy_UndoPolicySubstitution          = UUID.fromString("F51B633C-C6C2-46D6-A1B3-9FD40113ADFB");
	public static final UUID OPID_Policy_MediationTransfer               = UUID.fromString("9568FAA4-ADBF-41AE-8FB9-9FD401140765");
	public static final UUID OPID_Policy_UndoMediationTransfer           = UUID.fromString("D663E77C-D7FD-440E-B0FE-9FD4011422B6");
	public static final UUID OPID_Policy_VoidPolicy                      = UUID.fromString("DF2C50EF-5E4A-475A-AB1E-9FD401143D8A");
	public static final UUID OPID_Policy_UndoVoidPolicy                  = UUID.fromString("3C1FD4F4-4100-4A22-A88C-9FD401145205");
	public static final UUID OPID_Policy_DeletePolicy                    = UUID.fromString("9B993DB1-CF37-4E34-965A-9F8200C986B9");
	public static final UUID OPID_Policy_ExternResumePolicy              = UUID.fromString("0B95B090-DB27-4A4C-91EA-9FD301166C79");
	public static final UUID OPID_Policy_TransferToClient                = UUID.fromString("E8DBFC9B-C24F-4F62-BE4E-9FF200B406C8");
	public static final UUID OPID_Policy_UndoTransferToClient            = UUID.fromString("7D4D06DC-8652-4E4E-B1F8-9FF200B460A5");
	public static final UUID OPID_Policy_ExternAutoVoid                  = UUID.fromString("34FB7D31-175E-423A-8FEC-A03100CD86B2");
	public static final UUID OPID_Policy_ExternUndoAutoVoid              = UUID.fromString("9F4ABCF3-F2FA-4515-A2F9-A03100CDC836");
	public static final UUID OPID_Policy_AutoBlockOnAutoVoid             = UUID.fromString("C5762CA8-E911-468F-BC4D-A03100CE29AF");
	public static final UUID OPID_Policy_AutoUnblockOnUndoAutoVoid       = UUID.fromString("9B392086-8DEE-4402-BD5A-A03100CE6B71");

	// Sub Policy Operations
	public static final UUID OPID_SubPolicy_AutoProcessSubProcs          = UUID.fromString("60E3DCEF-E669-4816-8006-9FF30102C132");
	public static final UUID OPID_SubPolicy_TriggerDisallowDelete        = UUID.fromString("AAD72297-A110-4465-8390-9FF300F3D933");
	public static final UUID OPID_SubPolicy_PerformComputations          = UUID.fromString("85224AA5-3C12-470F-9539-9FF30101B35E");
	public static final UUID OPID_SubPolicy_ManageData                   = UUID.fromString("F8F2980B-5591-4BD1-8DD1-9FF3010015D1");
	public static final UUID OPID_SubPolicy_UndoManageData               = UUID.fromString("60976E13-CB9C-4C5E-88F9-9FF300F87C98");
	public static final UUID OPID_SubPolicy_ValidateSubPolicy            = UUID.fromString("E22C1D4C-2AF6-485F-BC02-9FF3010B1C80");
	public static final UUID OPID_SubPolicy_ForceValidateSubPolicy       = UUID.fromString("0E50240A-5BEA-4ACE-B99D-9FF3010ABC04");
	public static final UUID OPID_SubPolicy_UndoValidateSubPolicy        = UUID.fromString("183A3278-F3FC-439D-8A3C-9FF300FF81BD");
	public static final UUID OPID_SubPolicy_IncludeObject                = UUID.fromString("BFCAD808-59DC-497E-8639-9FF301021C58");
	public static final UUID OPID_SubPolicy_IncludeClientAsObject        = UUID.fromString("F06D3CAE-9290-4E4B-8F9E-9FF301027E17");
	public static final UUID OPID_SubPolicy_UndoIncludeObject            = UUID.fromString("7B348E4D-3D1A-4908-95A2-9FF300FE27B3");
	public static final UUID OPID_SubPolicy_ExcludeObject                = UUID.fromString("0B3F9561-C1E4-4C32-B5E0-9FF3010146C2");
	public static final UUID OPID_SubPolicy_UndoExcludeObject            = UUID.fromString("FB75A458-8E37-4E6F-82E9-9FF300FDDFAC");
	public static final UUID OPID_SubPolicy_CreateInfoRequest            = UUID.fromString("E9507402-B03D-4544-B770-9FF300F64319");
	public static final UUID OPID_SubPolicy_CreateCompInfoRequest        = UUID.fromString("BE82EDA9-A3FB-43DC-BA7F-9FF300F6CCDD");
	public static final UUID OPID_SubPolicy_CreateReceipt                = UUID.fromString("2B9FED57-FA3B-4451-9EBC-9FF300F72555");
	public static final UUID OPID_SubPolicy_ExternDeleteReceipt          = UUID.fromString("0B624360-B064-45C4-A5B1-9FF30100D367");
	public static final UUID OPID_SubPolicy_UndoDeleteReceipt            = UUID.fromString("7D65FDC7-C3F0-458F-80AB-9FF300FD5789");
	public static final UUID OPID_SubPolicy_CreateHealthExpense          = UUID.fromString("BDAD68E7-7BA6-43AD-B10A-9FF300F5EE50");
	public static final UUID OPID_SubPolicy_ExternDeleteHealthExpense    = UUID.fromString("97EDDDF3-ED6A-42B9-8A76-9FF301008C8E");
	public static final UUID OPID_SubPolicy_UndoDeleteHealthExpense      = UUID.fromString("9EDFAD67-EC79-4126-B7DD-9FF300F8F40F");
	public static final UUID OPID_SubPolicy_VoidSubPolicy                = UUID.fromString("FA8BF9F2-FEB2-49C7-A408-9FF300F34C38");
	public static final UUID OPID_SubPolicy_UndoVoidSubPolicy            = UUID.fromString("3475679D-E70F-47BE-893F-9FF300F813EA");
	public static final UUID OPID_SubPolicy_DeleteSubPolicy              = UUID.fromString("CB4B3B9F-AACF-45A7-AD58-9FF3011AD0C2");
	public static final UUID OPID_SubPolicy_ExternResumeSubPolicy        = UUID.fromString("DF5DC735-7CB3-4830-9777-9FF3010A1C54");
	public static final UUID OPID_SubPolicy_TransferToPolicy             = UUID.fromString("E89D08A1-7BE7-4F2B-9E06-9FF3010A6138");
	public static final UUID OPID_SubPolicy_UndoTransferToPolicy         = UUID.fromString("063AF772-A40C-4895-9F95-9FF300FEA254");
	public static final UUID OPID_SubPolicy_ExternAutoVoid               = UUID.fromString("658F89A9-006A-4A19-BEF0-A03101001F8B");
	public static final UUID OPID_SubPolicy_ExternUndoAutoVoid           = UUID.fromString("0D1980F1-5BE1-4F55-B56D-A031010047E1");
	public static final UUID OPID_SubPolicy_AutoBlockOnAutoVoid          = UUID.fromString("12CD7FE7-5386-4499-B0A6-A0310100C4F8");
	public static final UUID OPID_SubPolicy_AutoUnblockOnUndoAutoVoid    = UUID.fromString("12F09413-EBE1-4D65-8B61-A0310100F4CB");

	// Receipt Operations
	public static final UUID OPID_Receipt_AutoDisallowDelete             = UUID.fromString("C79AE4F9-F00E-40B1-9DF4-A01300C42A2C");
	public static final UUID OPID_Receipt_AutoCheckDebitNote             = UUID.fromString("5594413C-34CB-465F-AA2E-A01300C4BBD8");
	public static final UUID OPID_Receipt_TriggerDisallowDebitNote       = UUID.fromString("EB057F61-048B-4690-8DD7-A01300D256AD");
	public static final UUID OPID_Receipt_ReceiveImage                   = UUID.fromString("E99A9029-BBF0-4E76-AC49-A01300C4FB8C");
	public static final UUID OPID_Receipt_TriggerImageOnCreate           = UUID.fromString("D6A00787-5509-4F47-A681-A01300C56C1F");
	public static final UUID OPID_Receipt_UndoReceiveImage               = UUID.fromString("38DB0FE1-B1DE-4C7B-B7CA-A01300C5BAC6");
	public static final UUID OPID_Receipt_ManageData                     = UUID.fromString("581AA15C-DF2C-4DF9-B3B3-9F8A00EDBADB");
	public static final UUID OPID_Receipt_UndoManageData                 = UUID.fromString("6D530E4A-8585-433F-9BE9-9F8A00EDE805");
	public static final UUID OPID_Receipt_TransferToPolicy               = UUID.fromString("1434D98F-3A70-4654-86DA-A01300C64A49");
	public static final UUID OPID_Receipt_UndoTransferToPolicy           = UUID.fromString("B1A231A4-ECEA-4DFE-A9E0-A01300C67694");
	public static final UUID OPID_Receipt_ValidateReceipt                = UUID.fromString("47538724-7B79-4655-96D7-A01300C6A881");
	public static final UUID OPID_Receipt_TriggerAutoValidate            = UUID.fromString("2A501358-24D2-4877-BA9C-A01300C6E1B7");
	public static final UUID OPID_Receipt_UndoValidateReceipt            = UUID.fromString("2026BF51-58EE-4663-92A2-A01300C70FBF");
	public static final UUID OPID_Receipt_ExternForceReverse             = UUID.fromString("5C10D8AF-8862-45B3-9CCC-A01300C79228");
	public static final UUID OPID_Receipt_CreateDebitNote                = UUID.fromString("AE201DB6-6C90-461B-B594-A01300C7C092");
	public static final UUID OPID_Receipt_UndoDebitNote                  = UUID.fromString("D0565F1D-F0B3-4FD8-AD3F-A01300C82715");
	public static final UUID OPID_Receipt_CreatePaymentNotice            = UUID.fromString("102E9D79-A006-4757-ADBA-A01300C863B1");
	public static final UUID OPID_Receipt_CreateSecondPaymentNotice      = UUID.fromString("6BC653FD-3265-4A3C-9307-A01300C8B2BC");
	public static final UUID OPID_Receipt_ExternForceShortCircuit        = UUID.fromString("1EB5CA7C-01D0-4120-BAA2-A01300C8E4BD");
	public static final UUID OPID_Receipt_CreateOrderNumberRequest       = UUID.fromString("7AC19720-D364-4FBD-A01F-A01300C92666");
	public static final UUID OPID_Receipt_CreateDASRequest               = UUID.fromString("4CAD549D-2903-4A42-9767-A01300C98B51");
	public static final UUID OPID_Receipt_CreateSignatureRequest         = UUID.fromString("563A630A-276A-48E8-96D3-A01300C9AF24");
	public static final UUID OPID_Receipt_CreateRequestForReceipt        = UUID.fromString("2815599A-E132-4805-9D34-A01300C9DF96");
	public static final UUID OPID_Receipt_CreateRequestForDuplicate      = UUID.fromString("BBF7D68B-29AB-43E1-902E-A01300CA0FC2");
	public static final UUID OPID_Receipt_CreateRequestForEarlyDebit     = UUID.fromString("F777EB4E-DB06-4ADC-93C3-A01300CA6F47");
	public static final UUID OPID_Receipt_Payment                        = UUID.fromString("F5F00701-69F7-4622-BB8C-9FB800DED93F");
	public static final UUID OPID_Receipt_AssociateWithDebitNote         = UUID.fromString("1810BACF-DB6C-4D2E-8093-A01300CAD6C2");
	public static final UUID OPID_Receipt_UndoPayment                    = UUID.fromString("826FF04D-10CF-444D-B795-A01300CB44C0");
	public static final UUID OPID_Receipt_TriggerForceDAS                = UUID.fromString("8AC9B4DA-6144-4EFC-8EFC-A01300CB76D3");
	public static final UUID OPID_Receipt_DASNotNecessary                = UUID.fromString("6E8D98B8-4901-4705-9C58-A01300CBA222");
	public static final UUID OPID_Receipt_UndoDASNotNecessary            = UUID.fromString("C6810F5C-C983-4E4C-8851-A01300CBD7A5");
	public static final UUID OPID_Receipt_ExternReceiveDAS               = UUID.fromString("EE580116-A45A-43AE-A2ED-A01300CBFDD9");
	public static final UUID OPID_Receipt_ExternUndoReceiveDAS           = UUID.fromString("936CE1AD-007A-4F11-AFD2-A01300CC23A5");
	public static final UUID OPID_Receipt_SendReceipt                    = UUID.fromString("002A20EF-A7B3-4DE7-B62C-A01300CC5450");
	public static final UUID OPID_Receipt_ExternAllowSendPayment         = UUID.fromString("04C24A5B-5CED-4CC3-A983-A01300CC84EC");
	public static final UUID OPID_Receipt_SendPayment                    = UUID.fromString("08799EDB-B874-49D5-B987-A01300CCAC59");
	public static final UUID OPID_Receipt_InsurerAccounting              = UUID.fromString("A07D96EF-CF7E-4287-8C3B-A01300CD2AF5");
	public static final UUID OPID_Receipt_UndoInsurerAccounting          = UUID.fromString("A153FBE9-046E-4AB3-A1CA-A01300CD5825");
	public static final UUID OPID_Receipt_MediatorAccounting             = UUID.fromString("C7305673-3EC9-4B1A-A437-A01300CD7D57");
	public static final UUID OPID_Receipt_UndoMediatorAccounting         = UUID.fromString("7859E03B-470B-4996-A701-A01300CDA907");
	public static final UUID OPID_Receipt_NotPayedIndication             = UUID.fromString("3323CA57-827F-4EE5-94B7-A01300CDD793");
	public static final UUID OPID_Receipt_UndoNotPayedIndication         = UUID.fromString("F8C9AB98-5369-45CD-832B-A01300CE5B02");
	public static final UUID OPID_Receipt_SetReturnToInsurer             = UUID.fromString("9C9369BB-186D-433B-BD7B-A01300CE9125");
	public static final UUID OPID_Receipt_UndoSetReturnToInsurer         = UUID.fromString("11981877-E4D0-439F-A411-A01300CEBB2C");
	public static final UUID OPID_Receipt_ReturnToInsurer                = UUID.fromString("A34C65D4-0B2A-4083-BB16-A01300D3D013");
	public static final UUID OPID_Receipt_DeleteReceipt                  = UUID.fromString("994D421F-E414-41EF-8D02-9F8A00EEE620");
	public static final UUID OPID_Receipt_ExternResumeReceipt            = UUID.fromString("FFD66FBD-5C98-4134-9B6C-A01300CEE472");
	public static final UUID OPID_Receipt_ExternBlockDirectRetrocession  = UUID.fromString("50AF21D9-8DE1-4038-A380-A099010257A4");

	//Casualty Operations
	public static final UUID OPID_Casualty_AutoProcessVents              = UUID.fromString("4DB7EF2A-DB71-4BF4-810B-A02E00FA9F94");
	public static final UUID OPID_Casualty_TriggerDisallowClose          = UUID.fromString("22B38E8A-BCCE-41C5-A749-A02E00FB362B");
	public static final UUID OPID_Casualty_AutoProcessSubs               = UUID.fromString("E4B66E9E-0806-4C0C-AD9C-A02E01017305");
	public static final UUID OPID_Casualty_TriggerDisallowDelete         = UUID.fromString("5392D50D-B4B2-4353-83B7-A02E0101CC92");
	public static final UUID OPID_Casualty_ManageData                    = UUID.fromString("574AE481-418F-4159-BE7D-A02E010397DF");
	public static final UUID OPID_Casualty_UndoManageData                = UUID.fromString("72B671A3-FF13-41FA-B195-A02E0103DC7B");
	public static final UUID OPID_Casualty_TransferToClient              = UUID.fromString("6E9F94D4-5D75-4488-AFC0-A02E0104321A");
	public static final UUID OPID_Casualty_UndoTransferToClient          = UUID.fromString("F7C75D4E-10D6-4A9B-9EFD-A02E0104699E");
	public static final UUID OPID_Casualty_CreateInfoRequest             = UUID.fromString("59FB24D3-979B-4C2E-A69E-A02E0104A04D");
	public static final UUID OPID_Casualty_ExecMgrXFer                   = UUID.fromString("C6E44C82-211F-494B-BC2A-A02E0104E0FB");
	public static final UUID OPID_Casualty_UndoExecMgrXFer               = UUID.fromString("F88C1449-AC2C-47CC-BFD1-A02E010715CE");
	public static final UUID OPID_Casualty_ExternMassUndoMgrXFer         = UUID.fromString("8A512C04-5962-42F9-AF86-A02E010521F8");
	public static final UUID OPID_Casualty_CreateSubCasualty             = UUID.fromString("5E84E2C5-5461-4D0D-9B77-A02E0107D0EA");
	public static final UUID OPID_Casualty_ExternCloseSubCasualty        = UUID.fromString("785F4B6A-B615-4723-A5EC-A02E010805D5");
	public static final UUID OPID_Casualty_ExternUndoCloseSubCasualty    = UUID.fromString("49C500E6-3322-428D-B5D4-A02E01083224");
	public static final UUID OPID_Casualty_ReopenSubCasualty             = UUID.fromString("BB2E2468-98F0-4B16-B8DA-A02E01085B01");
	public static final UUID OPID_Casualty_ExternDeleteSubCasualty       = UUID.fromString("33159CB1-1D73-4356-8DEA-A02E0108C821");
	public static final UUID OPID_Casualty_UndoDeleteSubCasualty         = UUID.fromString("A8EDC558-D006-44CA-8CB3-A02E010903B4");
	public static final UUID OPID_Casualty_CloseProcess                  = UUID.fromString("4FC7D258-3CBB-4174-AD53-A02E01094F3D");
	public static final UUID OPID_Casualty_TriggerCloseProcess           = UUID.fromString("00E77AFB-F6F6-401E-8B86-A03C00C7FDBD");
	public static final UUID OPID_Casualty_UndoCloseProcess              = UUID.fromString("CAF2B2EF-7E3C-4C94-8DC1-A02E010983D6");
	public static final UUID OPID_Casualty_DeleteCasualty                = UUID.fromString("21AEC620-C547-404A-8956-A02E0109A821");
	public static final UUID OPID_Casualty_ExternResumeCasualty          = UUID.fromString("FD65F22E-C32E-43F0-BCAA-A02E0109D010");

	//SubCasualty Operations
	public static final UUID OPID_SubCasualty_AutoProcessSubs            = UUID.fromString("B7E57ED8-3FC1-45D8-9068-A03600FA2A46");
	public static final UUID OPID_SubCasualty_TriggerDisallowDelete      = UUID.fromString("86DF6C79-EB51-4194-90A5-A03600FA4AA6");
	public static final UUID OPID_SubCasualty_ManageData                 = UUID.fromString("A1DE87C0-1878-4533-A82D-A03600FA6B23");
	public static final UUID OPID_SubCasualty_UndoManageData             = UUID.fromString("A1C01BE4-345D-40BE-BAB6-A03600FA8372");
	public static final UUID OPID_SubCasualty_SendNotification           = UUID.fromString("2A0EF8C0-ECFA-4622-9C72-A03600FACBCB");
	public static final UUID OPID_SubCasualty_RepeatNotification         = UUID.fromString("CF403FB6-EF33-4322-8821-A03600FAE804");
	public static final UUID OPID_SubCasualty_MarkNotificationAccepted   = UUID.fromString("90B13FAF-E069-48BC-87BE-A03600FB12BB");
	public static final UUID OPID_SubCasualty_UndoMarkNotifAccepted      = UUID.fromString("23D04925-1360-4362-83B4-A03600FB2A70");
	public static final UUID OPID_SubCasualty_CreateInfoRequest          = UUID.fromString("94DEF8C7-6740-4C66-85A3-A03600FC056A");
	public static final UUID OPID_SubCasualty_CreateExternRequest        = UUID.fromString("7859A887-03B0-423A-B104-A03600FC360D");
	public static final UUID OPID_SubCasualty_CreatePaymentValidation    = UUID.fromString("BE6489E7-3308-4F93-BB45-A03600FC7282");
	public static final UUID OPID_SubCasualty_CreateAssessment           = UUID.fromString("634A8C33-50A7-41D6-AF28-A03600FC9F95");
	public static final UUID OPID_SubCasualty_CreateBudgetAcceptance     = UUID.fromString("4C7F01F3-A6DF-497E-B840-A03600FCD16D");
	public static final UUID OPID_SubCasualty_CreateReplacementVehicle   = UUID.fromString("64EBC7FA-646D-4F6E-9EBB-A03600FD1473");
	public static final UUID OPID_SubCasualty_CreateTemporaryDisability  = UUID.fromString("F2AE7668-271F-4BE4-AB20-A03600FD320B");
	public static final UUID OPID_SubCasualty_CreateThirdPartySettlement = UUID.fromString("92D495E7-F32C-4F44-A912-A03600FD5081");
	public static final UUID OPID_SubCasualty_CreateSettlementFollowup   = UUID.fromString("0ACEA6D6-F14A-4885-AE83-A03600FD7301");
	public static final UUID OPID_SubCasualty_CreateCapitalReinstatement = UUID.fromString("B20E61F4-4CDF-4785-A228-A03600FDDF6C");
	public static final UUID OPID_SubCasualty_MarkForClosing             = UUID.fromString("CDDBD0AC-B19E-4576-890B-A03600FE0E49");
	public static final UUID OPID_SubCasualty_UndoMarkForClosing         = UUID.fromString("DADA8570-814F-442E-AD1E-A03600FE289C");
	public static final UUID OPID_SubCasualty_RejectClosing              = UUID.fromString("B7D949CE-7E2A-418B-AC4F-A03600FE57C9");
	public static final UUID OPID_SubCasualty_UndoRejectClosing          = UUID.fromString("84903CBA-B976-4A3F-976C-A03600FEA084");
	public static final UUID OPID_SubCasualty_CloseProcess               = UUID.fromString("207F3FEC-0CB2-47D4-868A-A03600FEE4FB");
	public static final UUID OPID_SubCasualty_UndoCloseProcess           = UUID.fromString("B7ADE091-68D5-446A-A66D-A03600FF2B14");
	public static final UUID OPID_SubCasualty_ExternReopenProcess        = UUID.fromString("8B3C5B6C-D71F-44B3-BB65-A03600FF5B14");
	public static final UUID OPID_SubCasualty_ExternDisallowUndoClose    = UUID.fromString("4E5E17C8-529D-4730-B454-A03C00F586F6");
	public static final UUID OPID_SubCasualty_ExternReallowUndoClose     = UUID.fromString("A731A2C0-8B9B-404F-9EF0-A03C00F5B819");
	public static final UUID OPID_SubCasualty_DeleteSubCasualty          = UUID.fromString("9CD4186F-90A7-4523-8D2F-A03600FF750D");
	public static final UUID OPID_SubCasualty_ExternResumeSubCasualty    = UUID.fromString("76D0E948-4211-4753-BB35-A03600FF9D83");

	// Expense Operations
	public static final UUID OPID_Expense_AutoProcessSubs                = UUID.fromString("4B8CC46D-68E4-455B-A16F-A03800BFFFE5");
	public static final UUID OPID_Expense_TriggerDisallowDelete          = UUID.fromString("F6D38624-32AA-47E2-A3B4-A03800C025D8");
	public static final UUID OPID_Expense_ManageData                     = UUID.fromString("2C9995DB-186C-40A4-91F2-A03800C0415E");
	public static final UUID OPID_Expense_UndoManageData                 = UUID.fromString("EA5FC2DE-4467-4F7E-8CFA-A03800C06038");
	public static final UUID OPID_Expense_SendNotification               = UUID.fromString("0E69C7E2-78B3-4A53-B6C5-A03800C0B1B6");
	public static final UUID OPID_Expense_ReceiveAcceptance              = UUID.fromString("4649DEBD-80B1-43AA-A292-A03800C0D14B");
	public static final UUID OPID_Expense_UndoReceiveAcceptance          = UUID.fromString("DD308611-DCFB-40AB-AEA0-A03800C0F624");
	public static final UUID OPID_Expense_NotifyClient                   = UUID.fromString("64F5A5D2-8A9D-48B4-9A8D-A03800C2CFFC");
	public static final UUID OPID_Expense_ReceiveReturn                  = UUID.fromString("14EAD818-E313-4F80-B781-A03800C324EA");
	public static final UUID OPID_Expense_UndoReceiveReturn              = UUID.fromString("DE92FC98-509C-4537-8647-A03800C34087");
	public static final UUID OPID_Expense_ReturnToClient                 = UUID.fromString("C675919E-A13D-459F-9E1F-A03800C3F021");
	public static final UUID OPID_Expense_CreateInfoRequest              = UUID.fromString("B2370F23-09C7-4B3B-BAA1-A03800C43B95");
	public static final UUID OPID_Expense_CreateExternRequest            = UUID.fromString("9D618AB8-53F5-4D5C-8CB7-A03800C56E4F");
	public static final UUID OPID_Expense_CreatePaymentValidation        = UUID.fromString("8D11C94A-E3B7-45C9-AB9A-A03800C5AB4B");
	public static final UUID OPID_Expense_CloseProcess                   = UUID.fromString("0EEB387C-661F-4FB0-8675-A03800C5DE73");
	public static final UUID OPID_Expense_UndoCloseProcess               = UUID.fromString("BB8902A3-C9BF-4D10-97E1-A03800C61F7B");
	public static final UUID OPID_Expense_DeleteExpense                  = UUID.fromString("B8A25252-7CC5-4468-9BDE-A03800C6480C");
	public static final UUID OPID_Expense_ExternResumeExpense            = UUID.fromString("6BEB3E92-8942-4C4C-BFBE-A03800C67760");

	// Manager Transfer Operations
	public static final UUID OPID_MgrXFer_ExecXFer                       = UUID.fromString("8B3AB41F-76F6-4311-8600-9F65014CF682");
	public static final UUID OPID_MgrXFer_UndoExecXFer                   = UUID.fromString("C8ED82AD-B227-4124-8317-9F650155DFFD");

	// Info Request Operations
	public static final UUID OPID_InfoReq_ReceiveReply                   = UUID.fromString("1497B196-8AE7-4E3F-8962-9FE500FAFEC6");
	public static final UUID OPID_InfoReq_UndoReceiveReply               = UUID.fromString("1FF0C190-59FF-4C71-BCD0-9FE500FB8BFF");
	public static final UUID OPID_InfoReq_RepeatRequest                  = UUID.fromString("342772ED-99A8-4662-9439-9FE500FABF29");
	public static final UUID OPID_InfoReq_CancelRequest                  = UUID.fromString("E4CAAC1D-C89F-4956-9F0E-9FE500FB443D");
	public static final UUID OPID_InfoReq_UndoCancelRequest              = UUID.fromString("34144359-1DE1-43ED-A279-9FE500FBCEB8");

	// Extern Request Operations
	public static final UUID OPID_ExternReq_SendInformation              = UUID.fromString("870ADF31-4DA8-41D2-BECD-9FEA00CE0A05");
	public static final UUID OPID_ExternReq_ReceiveAdditionalInfo        = UUID.fromString("18376BE2-9005-425A-98D0-9FEA00CE7865");
	public static final UUID OPID_ExternReq_CloseProcess                 = UUID.fromString("C8D7C92A-FA92-4A74-8A4F-9FEA00CEF1CB");
	public static final UUID OPID_ExternReq_UndoReceiveAdditional        = UUID.fromString("6EDCCFBE-8F31-476C-A819-9FEA00E4F48C");
	public static final UUID OPID_ExternReq_UndoCloseProcess             = UUID.fromString("6E1C0EFE-768E-4F60-BA1B-9FEA00E52E09");
	public static final UUID OPID_ExternReq_ExternAbortProcess           = UUID.fromString("CE0ECB00-E333-435D-8E0C-9FEA010EEACB");
	public static final UUID OPID_ExternReq_AutoLockProcess              = UUID.fromString("FD7023DF-C4EE-4111-94AC-9FEA0111DD3D");
	public static final UUID OPID_ExternReq_TriggerCloseProcess          = UUID.fromString("0CE5EA9B-2A72-434C-84AA-A00001275389");

	// Signature Request Operations
	public static final UUID OPID_SigReq_ReceiveReply                    = UUID.fromString("4AAD2648-E6D7-47AF-A91E-A02A0100B3A5");
	public static final UUID OPID_SigReq_UndoReceiveReply                = UUID.fromString("4A9D0CF2-5EA6-493C-921E-A02A01016F22");
	public static final UUID OPID_SigReq_RepeatRequest                   = UUID.fromString("C22398E7-D1E7-4F27-94A9-A02A01007804");
	public static final UUID OPID_SigReq_CancelRequest                   = UUID.fromString("03E8D6B1-04B8-4B1B-9FFA-A02A01013616");
	public static final UUID OPID_SigReq_UndoCancelRequest               = UUID.fromString("E0123F30-F89B-473F-8208-A02A0101B054");

	// DAS Request Operations
	public static final UUID OPID_DASRequest_ReceiveReply                = UUID.fromString("5217F6B9-7117-43CA-AA20-A03000F278C8");
	public static final UUID OPID_DASRequest_UndoReceiveReply            = UUID.fromString("8772A924-B997-4C64-B9F5-A03000F2D2B8");
	public static final UUID OPID_DASRequest_RepeatRequest               = UUID.fromString("D9757AE7-7F2F-4A29-BF7F-A03000F2533D");
	public static final UUID OPID_DASRequest_CancelRequest               = UUID.fromString("27BE85A3-F216-49F2-A54B-A03000F2A27A");
	public static final UUID OPID_DASRequest_UndoCancelRequest           = UUID.fromString("435C94A8-F8C8-4FA0-9149-A03000F30495");

	// Urgency Levels
	public static final UUID UrgID_Invalid   = UUID.fromString("6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5");
	public static final UUID UrgID_Valid     = UUID.fromString("7925EF60-80FC-4EA2-96A0-9EB1007EA1FF");
	public static final UUID UrgID_Pending   = UUID.fromString("C8B78B35-C9D7-4D5A-A65C-9EB1007EA842");
	public static final UUID UrgID_Urgent    = UUID.fromString("23A9B8C2-91E8-4188-885F-9EB1007ED540");
	public static final UUID UrgID_Completed = UUID.fromString("86C2D8A9-B754-4C8A-A81C-9EB1007EE286");

	// User Profiles
	public static final UUID ProfileID_Root = UUID.fromString("061388D9-16A6-443F-A69E-9EB000685026");

	// Email Usage Types
	public static final UUID UsageID_To      = UUID.fromString("0A9B0272-7685-44F8-8F65-9FE8010B18EE");
	public static final UUID UsageID_CC      = UUID.fromString("6457BD3C-7D2E-4830-944C-9FE8010B1DB6");
	public static final UUID UsageID_BCC     = UUID.fromString("04708D74-AA0A-4A22-9E6F-9FE8010B21F0");
	public static final UUID UsageID_ReplyTo = UUID.fromString("70AE501E-B15D-418F-9A39-9FE8010B2842");

	// Mediator Commission Profiles
	public static final UUID MCPID_None       = UUID.fromString("F60BB994-3E08-47C2-9CC3-9EFC013D35BE");	
	public static final UUID MCPID_Issuing    = UUID.fromString("C5BE51A9-7E0F-4970-962A-9EFC0135E9E1");
	public static final UUID MCPID_Percentage = UUID.fromString("CECC8014-200C-4C4F-9F47-9EFC01368139");
	public static final UUID MCPID_Negotiated = UUID.fromString("C7236BA7-73AD-40ED-B6DC-9EFC013691C8");
	public static final UUID MCPID_Special    = UUID.fromString("071CE678-956B-4D41-94DE-9EFC013688B5");

	// Client Types 
	public static final UUID TypeID_Individual = UUID.fromString("462096E4-68A2-408A-963A-9EE600C9556A");
	public static final UUID TypeID_Company    = UUID.fromString("C5B4F500-BB57-4BFD-8248-9EE600C95ABA");
	public static final UUID TypeID_Other      = UUID.fromString("4098CF7A-B5EE-4C3F-973F-9EE600C961AA");

	// Client Profiles
	public static final UUID ProfID_Normal = UUID.fromString("9F871430-9BBC-449F-B125-9EE600BE5A9A");
	public static final UUID ProfID_VIP    = UUID.fromString("63114D11-6087-4EFE-9A7E-9EE600BE52DA");
	public static final UUID ProfID_Simple = UUID.fromString("51ED12A4-95A9-44B0-928D-A01500DC83EB");

	// Object Types
	public static final UUID ObjTypeID_Person    = UUID.fromString("EDD94689-EFED-4B50-AA6E-9F9501402700");
	public static final UUID ObjTypeID_Group     = UUID.fromString("E04D67FA-F3D9-4597-96F0-9F950140323E");
	public static final UUID ObjTypeID_Equipment = UUID.fromString("E3E7B018-6F07-42DA-8B54-9F9501402D25");
	public static final UUID ObjTypeID_Site      = UUID.fromString("CD709854-DB59-424B-904A-9F9501403847");
	public static final UUID ObjTypeID_Animal    = UUID.fromString("7A9A0E31-668A-4113-A03E-9F9501403E6E");

	// Policy Field Types
	public static final UUID FieldID_Boolean   = UUID.fromString("361A1B4D-56A7-496C-A2CA-9F960154D6CA");
	public static final UUID FieldID_Date      = UUID.fromString("426A1A8F-9F00-4A82-AA15-9F960156611B");
	public static final UUID FieldID_List      = UUID.fromString("C32C7BE9-FCEB-457E-8C3A-9F9601403AC1");
	public static final UUID FieldID_Number    = UUID.fromString("4D82EE91-0A9E-415E-9003-9F9601404007");
	public static final UUID FieldID_Reference = UUID.fromString("9F626929-A5F7-4CAF-AAB3-9F96014A8A9F");
	public static final UUID FieldID_Text      = UUID.fromString("49EB6448-E9E6-448A-82A0-9F9601404506");

	// Policy Status Codes
	public static final UUID StatusID_InProgress = UUID.fromString("6489D7DF-A090-40B9-BD5E-9F98012C8BED");
	public static final UUID StatusID_Valid      = UUID.fromString("421E16B3-BE47-4D9C-9011-9F98012C945E");
	public static final UUID StatusID_Voided     = UUID.fromString("4F115B5C-0E23-444F-AA68-9F98012CA192");

	// Receipt Types
	public static final UUID RecType_Continuing = UUID.fromString("6B91D626-4CAD-4F53-8FD6-9F900111C39F");
	public static final UUID RecType_New        = UUID.fromString("36564F0F-2180-4794-B0EC-9F900111D2A8");
	public static final UUID RecType_Reversal   = UUID.fromString("BFC1AE6D-53E8-41AF-84BE-9F900111D967");
	public static final UUID RecType_Casualty   = UUID.fromString("91E07F5F-56BA-4A65-9659-9F900111DF95");
	public static final UUID RecType_Backcharge = UUID.fromString("4775B720-B90B-4279-B008-9F900111E6AD");
	public static final UUID RecType_Adjustment = UUID.fromString("3B127029-C133-4EB4-AD1E-9F900111EF2A");

	// Receipt Status Codes
	public static final UUID StatusID_New        = UUID.fromString("37C3A6F7-A579-4CD2-842E-A02000C337AA");
	public static final UUID StatusID_Payable    = UUID.fromString("C359D3FF-9032-4B4D-8C34-A02000C3403D");
	public static final UUID StatusID_DASPending = UUID.fromString("767EB803-6669-47EF-8649-A02000C3491F");
	public static final UUID StatusID_Paid       = UUID.fromString("8C43ED6B-A047-4549-9922-A02000C35098");
	public static final UUID StatusID_Closed     = UUID.fromString("B86D383A-70E9-4B03-87C7-A02000C35B32");

	// Receipt Payment Types
	public static final UUID PayID_Cheque          = UUID.fromString("06EA2F87-BB0C-4B02-BCDB-A02200EB462D");
	public static final UUID PayID_CurrentAccount  = UUID.fromString("637DBB57-3EFE-4728-BAB8-A02200EB4EC9");
	public static final UUID PayID_Cash            = UUID.fromString("40B9ACC7-A99A-4DC2-BAEF-A02200EB59B3");
	public static final UUID PayID_DirectToInsurer = UUID.fromString("A44F6D02-83A2-4D96-BF5F-A02200EB6857");
	public static final UUID PayID_BankTransfer    = UUID.fromString("5E1BEB0E-FABC-44F4-97BB-A02200EB78A5");
	public static final UUID PayID_Compensation    = UUID.fromString("AF1779DA-BB19-4BBD-B1FA-A02200EB84B6");
	public static final UUID PayID_FromTheInsurer  = UUID.fromString("9FD1C899-B395-4744-974B-A02200EB9014");

	// Template IDs
	public static final UUID TID_DebitNote           = UUID.fromString("BD024115-3572-41D4-B60C-9FF100F7E0C4");
	public static final UUID TID_PaymentNotice       = UUID.fromString("1852C013-F43F-4BBD-9895-A0200107AF84");
	public static final UUID TID_ReceiptCoverLetter  = UUID.fromString("874B735A-E564-47A8-B902-A02300E5ED8F");
	public static final UUID TID_ReceiptReturnLetter = UUID.fromString("13931DA8-6208-4074-ABD5-A0290116442A");
	public static final UUID TID_PaymentCoverLetter  = UUID.fromString("08014404-6149-4EBB-88EA-A02A00E8069F");
	public static final UUID TID_SignatureRequest    = UUID.fromString("4D2CDC36-F897-49A3-BF46-A02A010C0925");
	public static final UUID TID_DASRequest          = UUID.fromString("FBB27FB9-81CC-46DC-A2C2-A0300100AA93");
	public static final UUID TID_ExpenseMap          = UUID.fromString("19601CD2-9C7D-48E1-AA75-A03D00CD6E31");
	public static final UUID TID_ExpenseNotice       = UUID.fromString("E2C31990-C896-479C-8DA6-A03F00BD8B3A");
	public static final UUID TID_ExpenseReturn       = UUID.fromString("660446FF-8D1D-492C-AF06-A03F00BE1817");
	public static final UUID TID_InsurerAccounting   = UUID.fromString("501A9331-3FAB-478F-A682-A0270124E6B3");
	public static final UUID TID_MediatorAccounting  = UUID.fromString("E9A0F1C0-AE70-49AD-887E-A0990128BB1E");

	// Contact Types
	public static final UUID CtTypeID_ReceiptReturn = UUID.fromString("BA706479-AE31-4E69-A7F0-9EEE01336CA4");

	// Contact Info Types
	public static final UUID CInfoID_Email = UUID.fromString("96467849-6FE1-4113-928C-9EDF00F40FB9");

	// Document Types
	public static final UUID DocID_DebitNote              = UUID.fromString("785739A7-648C-4B51-9610-9FF10128C5CE");
	public static final UUID DocID_ReceiptScan            = UUID.fromString("041C081D-518F-4890-BEBF-A014011EFC8D");
	public static final UUID DocID_CutReceiptImage        = UUID.fromString("EE2BD86C-D93D-40E2-BC32-A021011074AF");
	public static final UUID DocID_PaymentNotice          = UUID.fromString("1D69CC65-7BE8-493C-858E-A02100D78299");
	public static final UUID DocID_ReceiptCoverLetter     = UUID.fromString("C47E392F-82E1-46D0-9F2A-A02300F4B244");
	public static final UUID DocID_ReceiptReturnLetter    = UUID.fromString("CBCC510A-7DE0-41DA-A8E5-A029011CACE4");
	public static final UUID DocID_PaymentCoverLetter     = UUID.fromString("18E61B06-BD30-4338-82B2-A02A00E88AF7");
	public static final UUID DocID_SignatureRequestLetter = UUID.fromString("7D054350-6B89-4742-91E5-A02A01105D11");
	public static final UUID DocID_DASRequestLetter       = UUID.fromString("9F722D93-12BF-4D33-92A3-A0300100FA21");
	public static final UUID DocID_ExpenseScan            = UUID.fromString("DD17A31E-CF99-40B1-8529-A0A100D41EC1");
	public static final UUID DocID_ExpenseRequest         = UUID.fromString("55825900-E20A-4519-BC4F-A03D00BF135A");
	public static final UUID DocID_ExpenseNotice          = UUID.fromString("E95D510B-396E-49B0-9ECA-A03F00BF1CFE");
	public static final UUID DocID_ExpenseReturn          = UUID.fromString("B78E445E-E061-40DA-914D-A03F00BF2AB3");
	public static final UUID DocID_InsurerReceipt         = UUID.fromString("CF2849F7-7967-4CCD-8731-A09400D4310C");
	public static final UUID DocID_MediatorPayment        = UUID.fromString("F6663F27-83B8-40DE-AB00-A0990123C856");

	// Report Types
	public static final UUID RTypeID_Parameter   = UUID.fromString("EFF85260-CB13-4599-8EFD-A032010C4AED");
	public static final UUID RTypeID_PrintSet    = UUID.fromString("F3B52F31-9C2F-4AD3-B566-A032010C5077");
	public static final UUID RTypeID_Transaction = UUID.fromString("3108E23A-45C2-4F1C-983E-A032010C566E");
	public static final UUID RTypeID_SubMenu     = UUID.fromString("7C398EAA-53D0-4DFB-B6B3-A032010C5CA8");

	// FK Constants
	public static final int FKCostCenter_In_UserDecoration = 2;
	public static final int ZipCode_In_PostalCode = 0;
	public static final int FKOwner_In_ContactInfo = 0;
	public static final int FKOwnerType_In_Contact = 1;
	public static final int FKOwner_In_Contact = 2;
	public static final int FKOwner_In_DocInfo = 0;
	public static final int FKOwnerType_In_Document = 1;
	public static final int FKOwner_In_Document = 2;
	public static final int FKCoverage_In_Tax = 1;
	public static final int Order_In_Tax = 8;
	public static final int FKSubLine_In_Coverage = 1;
	public static final int Mandatory_In_Coverage = 2;
	public static final int FKLine_In_SubLine = 1;
	public static final int FKParent_In_Group = 1;
	public static final int FKRequest_In_ReqSubLine = 0;
	public static final int FKRequest_In_ReqObject = 1;
	public static final int FKReqSubLine_In_ReqCoverage = 0;
	public static final int FKReqSubLine_In_ReqValue = 1;
	public static final int FKObject_In_ReqValue = 3;
	public static final int FKPolicy_In_CoInsurer = 0;
	public static final int FKPolicy_In_PolicyCoverage = 0;
	public static final int FKPolicy_In_Object = 1;
	public static final int FKPolicy_In_Exercise = 1;
	public static final int FKPolicy_In_Value = 1;
	public static final int FKObject_In_Value = 3;
	public static final int FKExercise_In_Value = 4;
	public static final int FKSubPolicy_In_SubPolicyCoverage = 0;
	public static final int FKSubPolicy_In_SubPolicyObject = 1;
	public static final int FKSubPolicy_In_SubPolicyValue = 1;
	public static final int FKObject_In_SubPolicyValue = 3;
	public static final int FKExercise_In_SubPolicyValue = 4;
	public static final int FKProcess_In_DebitNote = 1;
	public static final int FKReceipt_In_DebitNote = 6;
}
