package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	public static final UUID ObjID_CostCenter   = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations  = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");
	public static final UUID ObjID_Company      = UUID.fromString("7B203DCA-FFAC-46B2-B849-9EBC009DB127");
	public static final UUID ObjID_Mediator     = UUID.fromString("8A33B9DD-001A-401F-AA7E-9EBB00E9D24F");
	public static final UUID ObjID_Contact      = UUID.fromString("2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C");
	public static final UUID ObjID_ContactInfo  = UUID.fromString("069434F6-8EE7-4DFE-96C9-9EC401014608");
	public static final UUID ObjID_Document     = UUID.fromString("794F44DB-191A-4E17-9217-9ECC012A8AC2");
	public static final UUID ObjID_DocInfo      = UUID.fromString("75620A04-D0C7-43D4-8425-9ECC012C0E91");
	public static final UUID ObjID_Line         = UUID.fromString("A9A1CE62-06A1-4761-A1FC-9EC900F234B0");
	public static final UUID ObjID_SubLine      = UUID.fromString("FBCD74E1-A280-4443-9BB4-9EC900F4A4B9");
	public static final UUID ObjID_Coverage     = UUID.fromString("007022A0-6DFA-498B-9DC4-9EC900F5219F");
	public static final UUID ObjID_Tax          = UUID.fromString("43644146-07C4-4E63-9E87-9EC900F6EB73");

	public static final UUID ObjID_ContactType  = UUID.fromString("228F6A73-1335-4C99-8DD0-9EEE012964BA");
	public static final UUID ObjID_CInfoType    = UUID.fromString("03C5B78E-D71C-49F2-A079-9EC40111DCC0");
	public static final UUID ObjID_DocType      = UUID.fromString("B4DBEE18-FA81-471D-A9F5-9ECC012A028D");
	public static final UUID ObjID_CommProfile  = UUID.fromString("5F40713C-1FE7-4715-AC24-9EBB00E53392");
	public static final UUID ObjID_LineCategory = UUID.fromString("3FF829D9-5C33-4C91-A42B-9EC900F013CB");
	public static final UUID ObjID_ValueUnit    = UUID.fromString("0D8E4FEE-AE4D-4429-A517-9EC900F80A20");

	public static final UUID ObjID_GenSys       = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");

	public static final UUID ProfID_Root = UUID.fromString("061388D9-16A6-443F-A69E-9EB000685026");

	public static final UUID ProcID_GenSys = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");

	public static final UUID OPID_ManageCostCenters  = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_ManageUsers        = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");
	public static final UUID OPID_ManageMediators    = UUID.fromString("EDA3659D-4BE9-4779-B0C9-9EB500A07726");
	public static final UUID OPID_ManageCompanies    = UUID.fromString("83081039-C3C0-4BAC-8B24-9EB500A0A3EB");
	public static final UUID OPID_ManageLines        = UUID.fromString("7CE5A4B8-9E13-4F14-BDDA-9EB500A0D171");
	public static final UUID OPID_ManageCoefficients = UUID.fromString("BF76CE8C-C9A7-408B-B623-9EB500A0F07F");

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
}
