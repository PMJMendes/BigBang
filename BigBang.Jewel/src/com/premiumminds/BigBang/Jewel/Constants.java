package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	// Objects
    public static final UUID ObjectID_AppParams  = UUID.fromString("5485B660-A102-441A-912C-7B960584399F");

	// General Objects
	public static final UUID ObjID_GenSys        = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");

	public static final UUID ObjID_CostCenter    = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations   = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");
	public static final UUID ObjID_Company       = UUID.fromString("7B203DCA-FFAC-46B2-B849-9EBC009DB127");
	public static final UUID ObjID_Mediator      = UUID.fromString("8A33B9DD-001A-401F-AA7E-9EBB00E9D24F");
	public static final UUID ObjID_Contact       = UUID.fromString("2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C");
	public static final UUID ObjID_ContactInfo   = UUID.fromString("069434F6-8EE7-4DFE-96C9-9EC401014608");
	public static final UUID ObjID_Document      = UUID.fromString("794F44DB-191A-4E17-9217-9ECC012A8AC2");
	public static final UUID ObjID_DocInfo       = UUID.fromString("75620A04-D0C7-43D4-8425-9ECC012C0E91");
	public static final UUID ObjID_Line          = UUID.fromString("A9A1CE62-06A1-4761-A1FC-9EC900F234B0");
	public static final UUID ObjID_SubLine       = UUID.fromString("FBCD74E1-A280-4443-9BB4-9EC900F4A4B9");
	public static final UUID ObjID_Coverage      = UUID.fromString("007022A0-6DFA-498B-9DC4-9EC900F5219F");
	public static final UUID ObjID_Tax           = UUID.fromString("43644146-07C4-4E63-9E87-9EC900F6EB73");
	public static final UUID ObjID_ClientGroup   = UUID.fromString("DC89D454-CCCC-441E-BCE3-9EE600AFCCCD");

	// Agenda Objects
	public static final UUID ObjID_AgendaItem    = UUID.fromString("F850E623-E686-4575-A2D8-9F640128FCD7");
	public static final UUID ObjID_AgendaProcess = UUID.fromString("3620AAEE-A950-421C-A5D3-9F640135D7D8");
	public static final UUID ObjID_AgendaOp      = UUID.fromString("3C01C389-71FE-43A8-AEE7-9F640135635D");

	// Client Objects
	public static final UUID ObjID_Client        = UUID.fromString("D535A99E-149F-44DC-A28B-9EE600B240F5");

	// Policy Objects
	public static final UUID ObjID_Policy        = UUID.fromString("D0C5AE6B-D340-4171-B7A3-9F81011F5D42");

	// Sub-Process Objects
	public static final UUID ObjID_MgrXFer       = UUID.fromString("66EC1066-AD5A-430C-9A70-9F65013F5453");
	public static final UUID ObjID_MgrXFerProc   = UUID.fromString("324A0D54-4ED9-4925-97D2-9F6D0135A37E");

	// Typified Lists
	public static final UUID ObjID_ContactType   = UUID.fromString("228F6A73-1335-4C99-8DD0-9EEE012964BA");
	public static final UUID ObjID_CInfoType     = UUID.fromString("03C5B78E-D71C-49F2-A079-9EC40111DCC0");
	public static final UUID ObjID_DocType       = UUID.fromString("B4DBEE18-FA81-471D-A9F5-9ECC012A028D");
	public static final UUID ObjID_CommProfile   = UUID.fromString("5F40713C-1FE7-4715-AC24-9EBB00E53392");
	public static final UUID ObjID_LineCategory  = UUID.fromString("3FF829D9-5C33-4C91-A42B-9EC900F013CB");
	public static final UUID ObjID_ValueUnit     = UUID.fromString("0D8E4FEE-AE4D-4429-A517-9EC900F80A20");
	public static final UUID ObjID_ClientType    = UUID.fromString("40A63D68-E2A6-40AB-B3A4-9EE600B91EBE");
	public static final UUID ObjID_ClientSubtype = UUID.fromString("1907DB1B-6F75-4715-A210-9EE600B9D072");
	public static final UUID ObjID_OpProfile     = UUID.fromString("11921769-CDF3-447A-8E6B-9ECA01761BAA");
	public static final UUID ObjID_Sex           = UUID.fromString("2549E951-2747-4300-982E-9ECA01756B91");
	public static final UUID ObjID_MaritalStatus = UUID.fromString("B560424E-1DF9-46CA-8B67-9ECA01774B00");
	public static final UUID ObjID_Profession    = UUID.fromString("088C8CCE-A1D1-4976-808F-9ECA0177AB56");
	public static final UUID ObjID_CAE           = UUID.fromString("14B89884-475A-4357-BB7D-9EE600C3CE6E");
	public static final UUID ObjID_Size          = UUID.fromString("5E489662-2AAF-4348-95E0-9EE600C4A71F");
	public static final UUID ObjID_SalesVolume   = UUID.fromString("5C4E267C-B306-482F-8636-9EE600C586B6");
	public static final UUID ObjID_Durations     = UUID.fromString("A522A5B3-245B-4F8B-97F1-9F810120FAB6");
	public static final UUID ObjID_Fractioning   = UUID.fromString("28B2D442-2CF3-4E14-8430-9F8101229DA8");

	// Process Scripts
	public static final UUID ProcID_GenSys  = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");
	public static final UUID ProcID_Client  = UUID.fromString("100E701A-EDC5-4D9C-A221-9F09013D7954");
	public static final UUID ProcID_Policy  = UUID.fromString("29145166-59AC-452E-8C2B-9F81013A39AC");
	public static final UUID ProcID_MgrXFer = UUID.fromString("BBF5FFD1-4249-48AE-BB2D-9F6501420E7B");

	// Operations

	// General System Operations
	public static final UUID OPID_ManageCostCenters   = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_ManageUsers         = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");
	public static final UUID OPID_ManageMediators     = UUID.fromString("EDA3659D-4BE9-4779-B0C9-9EB500A07726");
	public static final UUID OPID_ManageCompanies     = UUID.fromString("83081039-C3C0-4BAC-8B24-9EB500A0A3EB");
	public static final UUID OPID_ManageLines         = UUID.fromString("7CE5A4B8-9E13-4F14-BDDA-9EB500A0D171");
	public static final UUID OPID_ManageCoefficients  = UUID.fromString("BF76CE8C-C9A7-408B-B623-9EB500A0F07F");
	public static final UUID OPID_ManageGroups        = UUID.fromString("08E5A828-0AF2-4347-A57A-9EB500A11526");
	public static final UUID OPID_CreateClient        = UUID.fromString("214118B8-C43C-4941-9D1E-9EB500A1381C");
	public static final UUID OPID_TriggerDeleteClient = UUID.fromString("EA560433-17DF-4E0A-A7BC-9F3300EAFD78");
	public static final UUID OPID_UndoGroups          = UUID.fromString("742FCF0E-FD17-439A-A810-9F3300CCF026");
	public static final UUID OPID_UndoCoefficients    = UUID.fromString("B841D5C5-CCA9-4E25-B88C-9F3300E64E61");
	public static final UUID OPID_UndoCostCenters     = UUID.fromString("438CC18F-51A7-4037-A5B4-9F3300CCBD74");
	public static final UUID OPID_UndoCompanies       = UUID.fromString("266E812E-53F0-4968-AC6C-9F3300E68E03");
	public static final UUID OPID_UndoLines           = UUID.fromString("D4E44325-B8F8-450B-9949-9F3300E6AB0E");
	public static final UUID OPID_UndoMediators       = UUID.fromString("3A1572FF-B1C6-4DEC-9819-9F3300E6DA84");
	public static final UUID OPID_UndoUsers           = UUID.fromString("F71DF6CC-C58E-4AA7-9BE5-9F3300E6FD4F");
	public static final UUID OPID_UndoDeleteClient    = UUID.fromString("711B9533-8298-4DA5-BFD1-9F3D01211F51");

	// Client Operations
	public static final UUID OPID_TriggerAllowPolicies    = UUID.fromString("A99DCEF5-91BA-4CFA-9E70-9F090146FAE4");
	public static final UUID OPID_TriggerDisallowPolicies = UUID.fromString("6A031641-DEC8-46D3-B402-9F0901474FE9");
	public static final UUID OPID_AutoProcessData         = UUID.fromString("A9A8F4ED-74C2-473C-873C-9F0901435C1C");
	public static final UUID OPID_CreateMgrXFer           = UUID.fromString("C75EEC27-780C-4C0C-9FD7-9F090142CB10");
	public static final UUID OPID_ManageClientData        = UUID.fromString("AE3CC7F0-BE2C-4548-87B5-9F09014072B8");
	public static final UUID OPID_DeleteClient            = UUID.fromString("2227C32A-0AF5-4ECC-B807-9F090143401F");
	public static final UUID OPID_UndoManageClientData    = UUID.fromString("61097A3F-2322-4C06-B015-9F090143A35B");
	public static final UUID OPID_EndMgrXFer              = UUID.fromString("B8D56EF0-F619-4EB2-8F26-9F0901438388");
	public static final UUID OPID_UndoEndMgrXFer          = UUID.fromString("955E3DFE-F74A-46A5-9A42-9F6800EADA3D");
	public static final UUID OPID_TriggerAllowUndoMgrXFer = UUID.fromString("91530E4E-9F84-4240-B1F3-9F6D017FE928");
	public static final UUID OPID_UndoDirectMgrXFer       = UUID.fromString("E7ABBF15-4DB6-4376-92FD-9F6D01792783");
	public static final UUID OPID_CreateInfoRequest       = UUID.fromString("26185760-A862-4CE4-9620-9F090142B18A");
	public static final UUID OPID_MergeOtherClient        = UUID.fromString("A5B43B14-43A6-4C0F-A410-9F0901425E48");
	public static final UUID OPID_UndoMergeOtherClient    = UUID.fromString("33704B93-F5A5-481F-A0EF-9F090143B96C");
	public static final UUID OPID_CreatePolicy            = UUID.fromString("5903FA18-8F4D-4DD3-B4AE-9F090143186E");
	public static final UUID OPID_TriggerDeletePolicy     = UUID.fromString("8BFB93C8-2CCF-423F-9E35-9F8200CA7E06");
	public static final UUID OPID_UndoDeletePolicy        = UUID.fromString("E5E66BC3-4B16-42F4-B653-9F8200CE2893");

	// Policy Operations
	public static final UUID OPID_ManagePolicyData        = UUID.fromString("1F7B31EC-9388-4EA0-816C-9F81013A8ED4");
	public static final UUID OPID_UndoManagePolicyData    = UUID.fromString("98304BAB-0C9F-4089-879C-9F81013AFDB7");
	public static final UUID OPID_DeletePolicy            = UUID.fromString("9B993DB1-CF37-4E34-965A-9F8200C986B9");

	// Manager Transfer Operations
	public static final UUID OPID_AcceptXFer     = UUID.fromString("8B3AB41F-76F6-4311-8600-9F65014CF682");
	public static final UUID OPID_CancelXFer     = UUID.fromString("C8FCD8FD-B616-4252-942F-9F650155B3BC");
	public static final UUID OPID_UndoAcceptXFer = UUID.fromString("C8ED82AD-B227-4124-8317-9F650155DFFD");
	public static final UUID OPID_UndoCancelXFer = UUID.fromString("38B353D0-4D69-45E2-BCF8-9F650155F824");

	// Urgency Levels
	public static final UUID UrgID_Invalid   = UUID.fromString("6FDEA9C9-55E0-4214-8BC2-9EB1007E9BA5");
	public static final UUID UrgID_Valid     = UUID.fromString("7925EF60-80FC-4EA2-96A0-9EB1007EA1FF");
	public static final UUID UrgID_Pending   = UUID.fromString("C8B78B35-C9D7-4D5A-A65C-9EB1007EA842");
	public static final UUID UrgID_Urgent    = UUID.fromString("23A9B8C2-91E8-4188-885F-9EB1007ED540");
	public static final UUID UrgID_Completed = UUID.fromString("86C2D8A9-B754-4C8A-A81C-9EB1007EE286");

	// User Profiles
	public static final UUID ProfileID_Root = UUID.fromString("061388D9-16A6-443F-A69E-9EB000685026");

	// Client Types 
	public static final UUID TypeID_Individual = UUID.fromString("462096E4-68A2-408A-963A-9EE600C9556A");
	public static final UUID TypeID_Company    = UUID.fromString("C5B4F500-BB57-4BFD-8248-9EE600C95ABA");
	public static final UUID TypeID_Other      = UUID.fromString("4098CF7A-B5EE-4C3F-973F-9EE600C961AA");

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
	public static final int FKSubLine_In_Coverage = 1;
	public static final int FKLine_In_SubLine = 1;
	public static final int FKParent_In_Group = 1;
}
