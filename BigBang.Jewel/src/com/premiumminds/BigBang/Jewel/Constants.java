package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	// NameSpaces
    public static final UUID NSID_BigBang  = UUID.fromString("C37B81F0-860F-4868-9177-9E15008B3EFD");
    public static final UUID NSID_CredEGS  = UUID.fromString("A1CC31A3-E471-4568-B5FC-9E15008E98C9");
    public static final UUID NSID_AMartins = UUID.fromString("90CB8FFB-1555-49D8-9594-9E15008EDC72");

	// Objects
    public static final UUID ObjID_AppParams            = UUID.fromString("5485B660-A102-441A-912C-7B960584399F");

	// Agenda Objects
	public static final UUID ObjID_AgendaItem           = UUID.fromString("F850E623-E686-4575-A2D8-9F640128FCD7");
	public static final UUID ObjID_AgendaProcess        = UUID.fromString("3620AAEE-A950-421C-A5D3-9F640135D7D8");
	public static final UUID ObjID_AgendaOp             = UUID.fromString("3C01C389-71FE-43A8-AEE7-9F640135635D");

	// General Objects
	public static final UUID ObjID_GenSys               = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");

	public static final UUID ObjID_CostCenter           = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations          = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");
	public static final UUID ObjID_Company              = UUID.fromString("7B203DCA-FFAC-46B2-B849-9EBC009DB127");
	public static final UUID ObjID_Mediator             = UUID.fromString("8A33B9DD-001A-401F-AA7E-9EBB00E9D24F");
	public static final UUID ObjID_Contact              = UUID.fromString("2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C");
	public static final UUID ObjID_ContactInfo          = UUID.fromString("069434F6-8EE7-4DFE-96C9-9EC401014608");
	public static final UUID ObjID_Document             = UUID.fromString("794F44DB-191A-4E17-9217-9ECC012A8AC2");
	public static final UUID ObjID_DocInfo              = UUID.fromString("75620A04-D0C7-43D4-8425-9ECC012C0E91");
	public static final UUID ObjID_LineCategory         = UUID.fromString("3FF829D9-5C33-4C91-A42B-9EC900F013CB");
	public static final UUID ObjID_Line                 = UUID.fromString("A9A1CE62-06A1-4761-A1FC-9EC900F234B0");
	public static final UUID ObjID_SubLine              = UUID.fromString("FBCD74E1-A280-4443-9BB4-9EC900F4A4B9");
	public static final UUID ObjID_Coverage             = UUID.fromString("007022A0-6DFA-498B-9DC4-9EC900F5219F");
	public static final UUID ObjID_Tax                  = UUID.fromString("43644146-07C4-4E63-9E87-9EC900F6EB73");
	public static final UUID ObjID_ClientGroup          = UUID.fromString("DC89D454-CCCC-441E-BCE3-9EE600AFCCCD");

	// Client Objects
	public static final UUID ObjID_Client               = UUID.fromString("D535A99E-149F-44DC-A28B-9EE600B240F5");

	// Policy Objects
	public static final UUID ObjID_Policy               = UUID.fromString("D0C5AE6B-D340-4171-B7A3-9F81011F5D42");
	public static final UUID ObjID_PolicyCoverage       = UUID.fromString("AE5F9DD7-2863-4C5B-AE0B-9F9C00229D02");
	public static final UUID ObjID_PolicyObject         = UUID.fromString("3A3316D2-9D7C-4FD1-8486-9F9C0012E119");
	public static final UUID ObjID_PolicyExercise       = UUID.fromString("DEE32F69-B33D-4427-AD5B-9F9C001607F2");
	public static final UUID ObjID_PolicyValue          = UUID.fromString("F1D23471-0AA8-40A3-A691-9F9C001843CC");

	// Receipt Objects
	public static final UUID ObjID_Receipt              = UUID.fromString("B24E4BF7-382C-40EE-8120-9F8A00DABB81");

	// Manager Transfer Objects
	public static final UUID ObjID_MgrXFer              = UUID.fromString("66EC1066-AD5A-430C-9A70-9F65013F5453");
	public static final UUID ObjID_MgrXFerProc          = UUID.fromString("324A0D54-4ED9-4925-97D2-9F6D0135A37E");

	// Info Request Objects
	public static final UUID ObjID_InfoRequest          = UUID.fromString("1D10FF86-70B5-4362-A8F3-9F7C00BC6DE4");
	public static final UUID ObjID_RequestAddress       = UUID.fromString("C865EAD8-526A-4447-9CBB-9FE80109E9C5");

	// Extern Request Objects
	public static final UUID ObjID_ExternRequest        = UUID.fromString("BD9E1BEA-D02B-40FE-BB3E-9FEA00CA7ECD");

	// Typified Lists
	public static final UUID ObjID_ContactType          = UUID.fromString("228F6A73-1335-4C99-8DD0-9EEE012964BA");
	public static final UUID ObjID_CInfoType            = UUID.fromString("03C5B78E-D71C-49F2-A079-9EC40111DCC0");
	public static final UUID ObjID_DocType              = UUID.fromString("B4DBEE18-FA81-471D-A9F5-9ECC012A028D");
	public static final UUID ObjID_CommProfile          = UUID.fromString("5F40713C-1FE7-4715-AC24-9EBB00E53392");
	public static final UUID ObjID_ClientType           = UUID.fromString("40A63D68-E2A6-40AB-B3A4-9EE600B91EBE");
	public static final UUID ObjID_ClientSubtype        = UUID.fromString("1907DB1B-6F75-4715-A210-9EE600B9D072");
	public static final UUID ObjID_OpProfile            = UUID.fromString("11921769-CDF3-447A-8E6B-9ECA01761BAA");
	public static final UUID ObjID_Sex                  = UUID.fromString("2549E951-2747-4300-982E-9ECA01756B91");
	public static final UUID ObjID_MaritalStatus        = UUID.fromString("B560424E-1DF9-46CA-8B67-9ECA01774B00");
	public static final UUID ObjID_Profession           = UUID.fromString("088C8CCE-A1D1-4976-808F-9ECA0177AB56");
	public static final UUID ObjID_CAE                  = UUID.fromString("14B89884-475A-4357-BB7D-9EE600C3CE6E");
	public static final UUID ObjID_Size                 = UUID.fromString("5E489662-2AAF-4348-95E0-9EE600C4A71F");
	public static final UUID ObjID_SalesVolume          = UUID.fromString("5C4E267C-B306-482F-8636-9EE600C586B6");
	public static final UUID ObjID_Durations            = UUID.fromString("A522A5B3-245B-4F8B-97F1-9F810120FAB6");
	public static final UUID ObjID_Fractioning          = UUID.fromString("28B2D442-2CF3-4E14-8430-9F8101229DA8");
	public static final UUID ObjID_ReceiptType          = UUID.fromString("AFE7CC47-B44F-442D-8CF4-9F8A00DB2637");
	public static final UUID ObjID_PolicyStatus         = UUID.fromString("EF9C2F88-2AC7-44CF-9AD6-9F98012A7236");
	public static final UUID ObjID_ObjectType           = UUID.fromString("6ADF0A94-5004-41AF-A7FF-9F95013DABB8");
	public static final UUID ObjID_ExercisePeriod       = UUID.fromString("93315017-5744-4E43-95AB-9F9601200F1E");
	public static final UUID ObjID_RequestType          = UUID.fromString("B437ECD7-11D2-4C55-B6DD-9FE80101ECC8");
	public static final UUID ObjID_RequestCancelMotives = UUID.fromString("F79E335C-FD6F-43BE-8535-9FE900C1CAF4");

	public static final UUID ObjID_FieldType            = UUID.fromString("0D8E4FEE-AE4D-4429-A517-9EC900F80A20");
	public static final UUID ObjID_FieldValues          = UUID.fromString("D50A75B9-5F44-4BF2-9060-9F960121503A");

	public static final UUID ObjID_TypifiedText         = UUID.fromString("0F218912-1626-4A2C-BF68-9FE100F72735");

	// Process Scripts
	public static final UUID ProcID_GenSys        = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");
	public static final UUID ProcID_Client        = UUID.fromString("100E701A-EDC5-4D9C-A221-9F09013D7954");
	public static final UUID ProcID_Policy        = UUID.fromString("29145166-59AC-452E-8C2B-9F81013A39AC");
	public static final UUID ProcID_Receipt       = UUID.fromString("62D0A72A-525E-450C-9917-9F8A00EB38AC");
	public static final UUID ProcID_MgrXFer       = UUID.fromString("BBF5FFD1-4249-48AE-BB2D-9F6501420E7B");
	public static final UUID ProcID_InfoRequest   = UUID.fromString("5DBEF1CD-DEB8-4731-8C6F-9FE500F50BB9");
	public static final UUID ProcID_ExternRequest = UUID.fromString("83759D78-C30C-4928-B600-9FEA00CC2349");

	// Operations

	// General System Operations
	public static final UUID OPID_General_ManageCostCenters          = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_General_ManageUsers                = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");
	public static final UUID OPID_General_ManageMediators            = UUID.fromString("EDA3659D-4BE9-4779-B0C9-9EB500A07726");
	public static final UUID OPID_General_ManageCompanies            = UUID.fromString("83081039-C3C0-4BAC-8B24-9EB500A0A3EB");
	public static final UUID OPID_General_ManageLines                = UUID.fromString("7CE5A4B8-9E13-4F14-BDDA-9EB500A0D171");
	public static final UUID OPID_General_ManageCoefficients         = UUID.fromString("BF76CE8C-C9A7-408B-B623-9EB500A0F07F");
	public static final UUID OPID_General_ManageGroups               = UUID.fromString("08E5A828-0AF2-4347-A57A-9EB500A11526");
	public static final UUID OPID_General_CreateClient               = UUID.fromString("214118B8-C43C-4941-9D1E-9EB500A1381C");
	public static final UUID OPID_General_TriggerDeleteClient        = UUID.fromString("EA560433-17DF-4E0A-A7BC-9F3300EAFD78");
	public static final UUID OPID_General_UndoGroups                 = UUID.fromString("742FCF0E-FD17-439A-A810-9F3300CCF026");
	public static final UUID OPID_General_UndoCoefficients           = UUID.fromString("B841D5C5-CCA9-4E25-B88C-9F3300E64E61");
	public static final UUID OPID_General_UndoCostCenters            = UUID.fromString("438CC18F-51A7-4037-A5B4-9F3300CCBD74");
	public static final UUID OPID_General_UndoCompanies              = UUID.fromString("266E812E-53F0-4968-AC6C-9F3300E68E03");
	public static final UUID OPID_General_UndoLines                  = UUID.fromString("D4E44325-B8F8-450B-9949-9F3300E6AB0E");
	public static final UUID OPID_General_UndoMediators              = UUID.fromString("3A1572FF-B1C6-4DEC-9819-9F3300E6DA84");
	public static final UUID OPID_General_UndoUsers                  = UUID.fromString("F71DF6CC-C58E-4AA7-9BE5-9F3300E6FD4F");
	public static final UUID OPID_General_UndoDeleteClient           = UUID.fromString("711B9533-8298-4DA5-BFD1-9F3D01211F51");

	// Client Operations
	public static final UUID OPID_Client_AutoProcessData             = UUID.fromString("A9A8F4ED-74C2-473C-873C-9F0901435C1C");
	public static final UUID OPID_Client_TriggerDisallowPolicies     = UUID.fromString("6A031641-DEC8-46D3-B402-9F0901474FE9");
	public static final UUID OPID_Client_AutoProcessSubProcs         = UUID.fromString("62F70BDD-B9AC-4733-99FF-9F8B0128BAD5");
	public static final UUID OPID_Client_TriggerDisallowDelete       = UUID.fromString("6B5518F6-01E1-4C75-94B3-9F8B0129365E");
	public static final UUID OPID_Client_ManageData                  = UUID.fromString("AE3CC7F0-BE2C-4548-87B5-9F09014072B8");
	public static final UUID OPID_Client_UndoManageData              = UUID.fromString("61097A3F-2322-4C06-B015-9F090143A35B");
	public static final UUID OPID_Client_MergeIntoAnother            = UUID.fromString("895BB8D7-1263-4230-B1C4-9F8801189ADF");
	public static final UUID OPID_Client_ExternMergeOtherHere        = UUID.fromString("A5B43B14-43A6-4C0F-A410-9F0901425E48");
	public static final UUID OPID_Client_UndoMergeOtherHere          = UUID.fromString("33704B93-F5A5-481F-A0EF-9F090143B96C");
	public static final UUID OPID_Client_CreateInfoRequest           = UUID.fromString("26185760-A862-4CE4-9620-9F090142B18A");
	public static final UUID OPID_Client_CreateMgrXFer               = UUID.fromString("C75EEC27-780C-4C0C-9FD7-9F090142CB10");
	public static final UUID OPID_Client_TriggerAllowUndoMgrXFer     = UUID.fromString("91530E4E-9F84-4240-B1F3-9F6D017FE928");
	public static final UUID OPID_Client_UndoDirectMgrXFer           = UUID.fromString("E7ABBF15-4DB6-4376-92FD-9F6D01792783");
	public static final UUID OPID_Client_ExternEndMgrXFer            = UUID.fromString("77BB2541-4AA8-4980-BE98-9F8B01297CB6");
	public static final UUID OPID_Client_ExternUndoEndMgrXFer        = UUID.fromString("AC843270-9D90-489A-A99B-9F8B0129C6AD");
	public static final UUID OPID_Client_CreateQuoteRequest          = UUID.fromString("F9BF0190-20E3-45F8-9EBE-9F090142E605");
	public static final UUID OPID_Client_ExternDeleteQuoteRequest    = UUID.fromString("68251204-CDAC-41CC-AADC-9F8B012A2274");
	public static final UUID OPID_Client_UndoDeleteQuoteRequest      = UUID.fromString("AAA14707-B11F-42B6-BEF5-9F8B012B1424");
	public static final UUID OPID_Client_CreatePolicy                = UUID.fromString("5903FA18-8F4D-4DD3-B4AE-9F090143186E");
	public static final UUID OPID_Client_ExternDeletePolicy          = UUID.fromString("8BFB93C8-2CCF-423F-9E35-9F8200CA7E06");
	public static final UUID OPID_Client_UndoDeletePolicy            = UUID.fromString("E5E66BC3-4B16-42F4-B653-9F8200CE2893");
	public static final UUID OPID_Client_CreateRiskAnalysis          = UUID.fromString("AEC0C809-28EF-478D-B5B1-9F8B012E1757");
	public static final UUID OPID_Client_ExternDeleteRiskAnalysis    = UUID.fromString("D301D131-D156-49EF-B727-9F8B012A49B7");
	public static final UUID OPID_Client_UndoDeleteRiskAnalysis      = UUID.fromString("0BD92466-BCEB-4854-BF84-9F8B012AF683");
	public static final UUID OPID_Client_CreateCasualty              = UUID.fromString("6CCF24EB-AF80-4B63-A50A-9F8B012E01BB");
	public static final UUID OPID_Client_ExternDeleteCasualty        = UUID.fromString("EFC6FC1E-BC09-4D94-B60D-9F8B012A707E");
	public static final UUID OPID_Client_UndoDeleteCasualty          = UUID.fromString("A7FB514E-B632-4247-9BCE-9F8B012B2DF6");
	public static final UUID OPID_Client_DeleteClient                = UUID.fromString("2227C32A-0AF5-4ECC-B807-9F090143401F");
	public static final UUID OPID_Client_ExternResumeClient          = UUID.fromString("380BC979-5C71-4991-A382-9F8801257EB5");

	// Policy Operations
	public static final UUID OPID_Policy_AutoProcessSubProcs         = UUID.fromString("84A323A5-8F42-4913-B796-9FD301102BAC");
	public static final UUID OPID_Policy_TriggerDisallowDelete       = UUID.fromString("FE146209-A749-4463-B73B-9FD3010F0E45");
	public static final UUID OPID_Policy_PerformComputations         = UUID.fromString("7DC1A798-31AA-49A1-B2DC-9FD4010F7593");
	public static final UUID OPID_Policy_ManageData                  = UUID.fromString("1F7B31EC-9388-4EA0-816C-9F81013A8ED4");
	public static final UUID OPID_Policy_UndoManageData              = UUID.fromString("98304BAB-0C9F-4089-879C-9F81013AFDB7");
	public static final UUID OPID_Policy_ValidatePolicy              = UUID.fromString("841E9154-8037-46F7-8B98-9FD3011A9605");
	public static final UUID OPID_Policy_ForceValidatePolicy         = UUID.fromString("607BC2A4-136A-474E-A54F-9FD3011B17BE");
	public static final UUID OPID_Policy_UndoValidatePolicy          = UUID.fromString("7E90AFE9-ED4D-4EA6-83B8-9FD3011BE799");
	public static final UUID OPID_Policy_OpenNewExercise             = UUID.fromString("C86BB300-28C1-47EF-A48D-9FD4010F9149");
	public static final UUID OPID_Policy_UndoOpenExercise            = UUID.fromString("D7CB22BA-C37D-40DD-948B-9FD4010FAA85");
	public static final UUID OPID_Policy_IncludeObject               = UUID.fromString("984226BB-33EA-4988-A802-9FD4010FC200");
	public static final UUID OPID_Policy_IncludeClientAsObject       = UUID.fromString("783EFBDB-44DB-4CAA-BF88-9FD4010FDB91");
	public static final UUID OPID_Policy_UndoIncludeObject           = UUID.fromString("6C57D11D-6E56-405B-AACE-9FD4010FF6F5");
	public static final UUID OPID_Policy_ExcludeObject               = UUID.fromString("CB5A1425-C032-4678-B9B2-9FD401149CB8");
	public static final UUID OPID_Policy_UndoExcludeObject           = UUID.fromString("6B10D212-CE69-4E02-9078-9FD40114D696");
	public static final UUID OPID_Policy_CreateDebitNote             = UUID.fromString("4CC51237-FC83-4B13-A8A9-9FD401100FE2");
	public static final UUID OPID_Policy_UndoDebitNote               = UUID.fromString("16C44D7D-6D09-4957-9051-9FD40110291E");
	public static final UUID OPID_Policy_ExternDisallowUndoDebitNote = UUID.fromString("1E90FD94-1C63-4984-AB2E-9FD401105E35");
	public static final UUID OPID_Policy_CreateRecurringInfoProcess  = UUID.fromString("5A89B28E-3098-490F-8AD0-9FD401109F6D");
	public static final UUID OPID_Policy_CreateInfoRequest           = UUID.fromString("DF06C139-4D61-44D1-81EE-9FD30118E9D3");
	public static final UUID OPID_Policy_CreateCompInfoRequest       = UUID.fromString("3F0120C1-F64E-4806-8AB2-9FD40110D264");
	public static final UUID OPID_Policy_CreateMgrXFer               = UUID.fromString("EF9C257E-323C-4BA0-A158-9F9000D8A755");
	public static final UUID OPID_Policy_TriggerAllowUndoMgrXFer     = UUID.fromString("E8FC9B51-A3EF-4E0C-AA18-9F9000DC5D1C");
	public static final UUID OPID_Policy_UndoDirectMgrXFer           = UUID.fromString("5364E6D1-B55D-445F-A961-9F9000DA6202");
	public static final UUID OPID_Policy_ExternEndMgrXFer            = UUID.fromString("FBC4C10F-FFDF-4A19-AC20-9F9000DD7CAB");
	public static final UUID OPID_Policy_ExternUndoEndMgrXFer        = UUID.fromString("D9013C73-53E9-418E-B7FF-9F9000DE9427");
	public static final UUID OPID_Policy_CreateReceipt               = UUID.fromString("C4117861-16EB-40DB-A771-9F8A00EA6B32");
	public static final UUID OPID_Policy_ExternDeleteReceipt         = UUID.fromString("92982C76-0AEA-4028-A29B-9F8A00EA9F48");
	public static final UUID OPID_Policy_UndoDeleteReceipt           = UUID.fromString("DE651450-BDA1-4DBA-A269-9F8A00EACA60");
	public static final UUID OPID_Policy_CreateSubPolicy             = UUID.fromString("DF27A2DA-1B36-4F1C-B242-9FD40110F495");
	public static final UUID OPID_Policy_ExternDeleteSubPolicy       = UUID.fromString("99831C5E-E137-4B5E-B18D-9FD401111B36");
	public static final UUID OPID_Policy_UndoDeleteSubPolicy         = UUID.fromString("41F07FC7-51A1-4B62-A37C-9FD4011138CA");
	public static final UUID OPID_Policy_CreateNegotiation           = UUID.fromString("852BF3BF-F04A-494D-A308-9FD4011152C4");
	public static final UUID OPID_Policy_ExternDeleteNegotiation     = UUID.fromString("5EFE2E2E-6B7B-43B4-8970-9FD4011166E9");
	public static final UUID OPID_Policy_UndoDeleteNegotiation       = UUID.fromString("CA6217B8-34C8-49E4-ADB7-9FD401117C22");
	public static final UUID OPID_Policy_CreateRiskAnalysis          = UUID.fromString("731AAB07-BBE4-4BA2-8A37-9FD40111E7EA");
	public static final UUID OPID_Policy_ExternDeleteRiskAnalysis    = UUID.fromString("54284976-C57C-4485-85E8-9FD401120237");
	public static final UUID OPID_Policy_UndoDeleteRiskAnalysis      = UUID.fromString("643F0129-F12B-4205-BFE7-9FD40112B6E7");
	public static final UUID OPID_Policy_CreateHealthExpense         = UUID.fromString("A7A22C6D-ED66-444D-9E5E-9FD40112D52C");
	public static final UUID OPID_Policy_ExternDeleteHealthExpense   = UUID.fromString("C0DAB4FC-A8CE-4D48-A31D-9FD40112FC86");
	public static final UUID OPID_Policy_UndoDeleteHealthExpense     = UUID.fromString("8979C0F8-09BF-4397-BA9E-9FD401131CF8");
	public static final UUID OPID_Policy_PolicySubstitution          = UUID.fromString("66504952-BCFD-4B6A-B8F0-9FD401138D07");
	public static final UUID OPID_Policy_UndoPolicySubstitution      = UUID.fromString("F51B633C-C6C2-46D6-A1B3-9FD40113ADFB");
	public static final UUID OPID_Policy_MediationTransfer           = UUID.fromString("9568FAA4-ADBF-41AE-8FB9-9FD401140765");
	public static final UUID OPID_Policy_UndoMediationTransfer       = UUID.fromString("D663E77C-D7FD-440E-B0FE-9FD4011422B6");
	public static final UUID OPID_Policy_VoidPolicy                  = UUID.fromString("DF2C50EF-5E4A-475A-AB1E-9FD401143D8A");
	public static final UUID OPID_Policy_UndoVoidPolicy              = UUID.fromString("3C1FD4F4-4100-4A22-A88C-9FD401145205");
	public static final UUID OPID_Policy_DeletePolicy                = UUID.fromString("9B993DB1-CF37-4E34-965A-9F8200C986B9");
	public static final UUID OPID_Policy_ExternResumePolicy          = UUID.fromString("0B95B090-DB27-4A4C-91EA-9FD301166C79");

	// Receipt Operations
	public static final UUID OPID_Receipt_ManageData                 = UUID.fromString("581AA15C-DF2C-4DF9-B3B3-9F8A00EDBADB");
	public static final UUID OPID_Receipt_UndoManageData             = UUID.fromString("6D530E4A-8585-433F-9BE9-9F8A00EDE805");
	public static final UUID OPID_Receipt_DeleteReceipt              = UUID.fromString("994D421F-E414-41EF-8D02-9F8A00EEE620");
	public static final UUID OPID_Receipt_Payment                    = UUID.fromString("F5F00701-69F7-4622-BB8C-9FB800DED93F");

	// Manager Transfer Operations
	public static final UUID OPID_MgrXFer_AcceptXFer                 = UUID.fromString("8B3AB41F-76F6-4311-8600-9F65014CF682");
	public static final UUID OPID_MgrXFer_CancelXFer                 = UUID.fromString("C8FCD8FD-B616-4252-942F-9F650155B3BC");
	public static final UUID OPID_MgrXFer_UndoAcceptXFer             = UUID.fromString("C8ED82AD-B227-4124-8317-9F650155DFFD");
	public static final UUID OPID_MgrXFer_UndoCancelXFer             = UUID.fromString("38B353D0-4D69-45E2-BCF8-9F650155F824");

	// Info Request Operations
	public static final UUID OPID_InfoReq_ReceiveReply               = UUID.fromString("1497B196-8AE7-4E3F-8962-9FE500FAFEC6");
	public static final UUID OPID_InfoReq_UndoReceiveReply           = UUID.fromString("1FF0C190-59FF-4C71-BCD0-9FE500FB8BFF");
	public static final UUID OPID_InfoReq_RepeatRequest              = UUID.fromString("342772ED-99A8-4662-9439-9FE500FABF29");
	public static final UUID OPID_InfoReq_CancelRequest              = UUID.fromString("E4CAAC1D-C89F-4956-9F0E-9FE500FB443D");
	public static final UUID OPID_InfoReq_UndoCancelRequest          = UUID.fromString("34144359-1DE1-43ED-A279-9FE500FBCEB8");

	// Extern Request Operations
	public static final UUID OPID_ExternReq_SendInformation          = UUID.fromString("870ADF31-4DA8-41D2-BECD-9FEA00CE0A05");
	public static final UUID OPID_ExternReq_ReceiveAdditionalInfo    = UUID.fromString("18376BE2-9005-425A-98D0-9FEA00CE7865");
	public static final UUID OPID_ExternReq_CloseProcess             = UUID.fromString("C8D7C92A-FA92-4A74-8A4F-9FEA00CEF1CB");
	public static final UUID OPID_ExternReq_UndoReceiveAdditional    = UUID.fromString("6EDCCFBE-8F31-476C-A819-9FEA00E4F48C");
	public static final UUID OPID_ExternReq_UndoCloseProcess         = UUID.fromString("6E1C0EFE-768E-4F60-BA1B-9FEA00E52E09");

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

	// Client Types 
	public static final UUID TypeID_Individual = UUID.fromString("462096E4-68A2-408A-963A-9EE600C9556A");
	public static final UUID TypeID_Company    = UUID.fromString("C5B4F500-BB57-4BFD-8248-9EE600C95ABA");
	public static final UUID TypeID_Other      = UUID.fromString("4098CF7A-B5EE-4C3F-973F-9EE600C961AA");

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
	public static final int FKPolicy_In_PolicyCoverage = 0;
	public static final int FKPolicy_In_Object = 1;
	public static final int FKPolicy_In_Exercise = 1;
	public static final int FKPolicy_In_Value = 1;
	public static final int FKObject_In_Value = 3;
	public static final int FKExercise_In_Value = 4;
}
