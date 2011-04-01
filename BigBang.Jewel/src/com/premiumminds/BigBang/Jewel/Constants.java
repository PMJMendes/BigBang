package com.premiumminds.BigBang.Jewel;

import java.util.*;

public class Constants
{
	public static final UUID ObjID_GenSys      = UUID.fromString("628F5DA6-434F-46A8-9C88-9EB1008A689A");
	public static final UUID ObjID_CostCenter  = UUID.fromString("4AF891C6-707B-43AE-98C3-9EB100C0419E");
	public static final UUID ObjID_Decorations = UUID.fromString("87C6A1DB-2381-47E4-ADE5-9EB800836FF7");

	public static final UUID ProfID_Root = UUID.fromString("061388D9-16A6-443F-A69E-9EB000685026");

	public static final UUID ProcID_GenSys = UUID.fromString("37A989E2-9D1F-470C-A59E-9EB1008A97A5");

	public static final UUID OPID_ManageCostCenters = UUID.fromString("39A4A919-F3D0-4966-8CBB-9EB100A38EE8");
	public static final UUID OPID_ManageUsers       = UUID.fromString("03F5BBC1-5AB5-45DF-BAEF-9EB500A043FD");

	public static final int FKCostCenter_In_UserDecoration = 2;
}
