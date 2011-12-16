package bigBang.module.clientModule.shared;

public class ModuleConstants
{
	public class ListIDs
	{
		public static final String OperationalProfiles = "11921769-CDF3-447A-8E6B-9ECA01761BAA";
		public static final String MaritalStatuses     = "B560424E-1DF9-46CA-8B67-9ECA01774B00";
		public static final String Professions         = "088C8CCE-A1D1-4976-808F-9ECA0177AB56";
		public static final String ClientSubtypes      = "1907DB1B-6F75-4715-A210-9EE600B9D072";
		public static final String CompanySizes        = "5E489662-2AAF-4348-95E0-9EE600C4A71F";
		public static final String CLIENT_DELETION_MOTIVES = "103F49C3-1D27-40C5-9B8E-9F50011952D7";
	}

	public class ClientTypeIDs
	{
		public static final String Person  = "462096E4-68A2-408A-963A-9EE600C9556A";
		public static final String Company = "C5B4F500-BB57-4BFD-8248-9EE600C95ABA";
		public static final String Other   = "4098CF7A-B5EE-4C3F-973F-9EE600C961AA";
	}

	public class ProcessTypeIDs
	{
		public static final String GENERAL_SYSTEM = "628f5da6-434f-46a8-9c88-9eb1008a689a";
		public static final String CLIENT         = ""; //TODO
	}

	public class OpTypeIDs
	{
		public static final String ManageClientGroups = "08E5A828-0AF2-4347-A57A-9EB500A11526";
		public static final String CreateClient       = "214118B8-C43C-4941-9D1E-9EB500A1381C";
		public static final String DELETE_CLIENT	  = "2227C32A-0AF5-4ECC-B807-9F090143401F";
		public static final String ChangeClientData   = "AE3CC7F0-BE2C-4548-87B5-9F09014072B8";
		public static final String CREATE_MANAGER_TRANSFER = "C75EEC27-780C-4C0C-9FD7-9F090142CB10";
		public static final String CREATE_INFO_OR_DOCUMENT_REQUEST = "26185760-A862-4CE4-9620-9F090142B18A";
		public static final String MERGE_CLIENT = "895BB8D7-1263-4230-B1C4-9F8801189ADF";
		public static final String MERGE_INTO_THIS_CLIENT = "A5B43B14-43A6-4C0F-A410-9F0901425E48";
		public static final String CREATE_POLICY = "5903FA18-8F4D-4DD3-B4AE-9F090143186E";
		public static final String CREATE_RISK_ANALYSIS = ""; //TODO FJVC IMPORTANT
		public static final String CREATE_QUOTE_REQUEST = "";
		public static final String CREATE_CASUALTY = "";
	}
}
