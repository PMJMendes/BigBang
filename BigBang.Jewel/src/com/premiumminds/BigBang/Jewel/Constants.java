package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	public static final UUID ObjID_CostCenter  = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");
	public static final UUID ObjID_Company     = UUID.fromString("7B203DCA-FFAC-46B2-B849-9EBC009DB127");
	public static final UUID ObjID_Mediator    = UUID.fromString("8A33B9DD-001A-401F-AA7E-9EBB00E9D24F");
	public static final UUID ObjID_Contact     = UUID.fromString("2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C");
	public static final UUID ObjID_ContactInfo = UUID.fromString("069434F6-8EE7-4DFE-96C9-9EC401014608");

	public static final UUID ObjID_CInfoType   = UUID.fromString("03C5B78E-D71C-49F2-A079-9EC40111DCC0");
	public static final UUID ObjID_CommProfile = UUID.fromString("5F40713C-1FE7-4715-AC24-9EBB00E53392");

	public static final UUID ObjID_GenSys      = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");

	public static final UUID ProfID_Root = UUID.fromString("061388D9-16A6-443F-A69E-9EB000685026");

	public static final UUID ProcID_GenSys = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");

	public static final UUID OPID_ManageCostCenters = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_ManageUsers       = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");
	public static final UUID OPID_ManageMediators   = UUID.fromString("EDA3659D-4BE9-4779-B0C9-9EB500A07726");
	public static final UUID OPID_ManageCompanies   = UUID.fromString("83081039-C3C0-4BAC-8B24-9EB500A0A3EB");
	public static final UUID OPID_ManageLines       = UUID.fromString("7CE5A4B8-9E13-4F14-BDDA-9EB500A0D171");

	public static final int FKCostCenter_In_UserDecoration = 2;
	public static final int ZipCode_In_PostalCode = 0;
	public static final int FKOwner_In_ContactInfo = 0;
	public static final int FKOwnerType_In_Contact = 1;
	public static final int FKOwner_In_Contact = 2;
}
