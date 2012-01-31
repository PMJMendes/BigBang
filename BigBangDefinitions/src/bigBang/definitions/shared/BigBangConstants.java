package bigBang.definitions.shared;

/**
 * Class that holds important information which should be shared across modules
 */
public class BigBangConstants {

	public static class EntityIds {
		public static final String PROCESS = "1967E563-01AD-4683-9D66-9E1700B7DF07";

		public static final String CLIENT_GROUP  = "DC89D454-CCCC-441E-BCE3-9EE600AFCCCD";
		public static final String CLIENT = "D535A99E-149F-44DC-A28B-9EE600B240F5";
		public static final String COST_CENTER = "4AF891C6-707B-43AE-98C3-9EB100C0419E";
		public static final String USER = "AA49E9F0-ABF5-4C11-994B-35FF72323DEF";
		public static final String USER_PROFILE = "53B25831-4028-4BF6-98B3-9F30EC6B32B9";
		public static final String MEDIATOR = "8A33B9DD-001A-401F-AA7E-9EBB00E9D24F";
		public static final String CATEGORY = "3FF829D9-5C33-4C91-A42B-9EC900F013CB";
		public static final String LINE = "A9A1CE62-06A1-4761-A1FC-9EC900F234B0";
		public static final String SUB_LINE = "FBCD74E1-A280-4443-9BB4-9EC900F4A4B9";
		public static final String INSURANCE_AGENCY = "7B203DCA-FFAC-46B2-B849-9EBC009DB127";
		public static final String COVERAGE = "007022A0-6DFA-498B-9DC4-9EC900F5219F";
		public static final String HISTORY = "F63454DC-B63C-44EC-9E5E-9E1A01004BDB";
		public static final String TASK = "F850E623-E686-4575-A2D8-9F640128FCD7";
		public static final String QUOTE_REQUEST = ""; //TODO
		public static final String QUOTE_REQUEST_NEGOTIATION = ""; //TODO
		public static final String INSURANCE_POLICY = "D0C5AE6B-D340-4171-B7A3-9F81011F5D42";
		public static final String RISK_ANALISYS = ""; //TODO
		public static final String RECEIPT = "B24E4BF7-382C-40EE-8120-9F8A00DABB81";
		public static final String POLICY_INSURED_OBJECT = "3A3316D2-9D7C-4FD1-8486-9F9C0012E119";
		public static final String POLICY_EXERCISE = "DEE32F69-B33D-4427-AD5B-9F9C001607F2";

		public static final String MANAGER_TRANSFER = "66EC1066-AD5A-430C-9A70-9F65013F5453";
		
		public static final String CONTACT = "2085F9FD-7FCE-46C0-99FD-9EC400FFBA5C";
		public static final String DOCUMENT = "794F44DB-191A-4E17-9217-9ECC012A8AC2";
		
		public static final String INSURED_OBJECT_TYPE_PERSON    = "EDD94689-EFED-4B50-AA6E-9F9501402700";
		public static final String INSURED_OBJECT_TYPE_COMPANY     = "E04D67FA-F3D9-4597-96F0-9F950140323E";
		public static final String INSURED_OBJECT_TYPE_EQUIPMENT = "E3E7B018-6F07-42DA-8B54-9F9501402D25";
		public static final String INSURED_OBJECT_TYPE_PLACE      = "CD709854-DB59-424B-904A-9F9501403847";
		public static final String INSURED_OBJECT_TYPE_ANIMAL    = "7A9A0E31-668A-4113-A03E-9F9501403E6E";
		public static final String CASUALTY = "TODO";
		public static final String COMPLAINT = "TODO";
		public static final String EXPENSE = "TODO";

		public static final String INFO_REQUEST = "TODO";

		public static final String INSURANCE_POLICY_EXERCISES = "DEE32F69-B33D-4427-AD5B-9F9C001607F2";
		public static final String INSURANCE_POLICY_INSURED_OBJECTS = "3A3316D2-9D7C-4FD1-8486-9F9C0012E119";
		
	}

	public static class TypifiedListIds {
		public static final String DOCUMENT_TYPE = "B4DBEE18-FA81-471D-A9F5-9ECC012A028D";
		public static final String PROCESS_TYPE = "905B49A4-B4A9-4330-825C-9E1600DBE7EA";
		public static final String OPERATION_TYPE = "30FBD723-9ACD-43F9-B931-9E1600DC051A";
		public static final String FIELD_VALUES = "D50A75B9-5F44-4BF2-9060-9F960121503A";
		public static final String GENDERS = "2549E951-2747-4300-982E-9ECA01756B91";
		public static final String CAEs = "14B89884-475A-4357-BB7D-9EE600C3CE6E";
		public static final String SALES_VOLUMES = "5C4E267C-B306-482F-8636-9EE600C586B6";
		public static final String CONTACT_TYPE = "228F6A73-1335-4C99-8DD0-9EEE012964BA";
		public static final String CONTACT_DETAILS_TYPE = "03C5B78E-D71C-49F2-A079-9EC40111DCC0";
		public static final String TYPIFIED_TEXT = "0F218912-1626-4A2C-BF68-9FE100F72735";
		public static final String REQUEST_TYPE = "B437ECD7-11D2-4C55-B6DD-9FE80101ECC8";
	}

	public static class OperationIds {

		public static class GeneralSystemProcess {
			public static final String CREATE_CLIENT = "214118B8-C43C-4941-9D1E-9EB500A1381C";
		}

		public static class ClientProcess {
			public static final String UPDATE_CLIENT = "AE3CC7F0-BE2C-4548-87B5-9F09014072B8";
			public static final String DELETE_CLIENT = "2227C32A-0AF5-4ECC-B807-9F090143401F";
			public static final String CREATE_CASUALTY = ""; //TODO
			public static final String MERGE_CLIENT = "";
			public static final String CREATE_INFO_REQUEST = "";
			public static final String CREATE_MANAGER_TRANSFER = "";
			public static final String CREATE_QUOTE_REQUEST = "";
			public static final String CREATE_POLICY = "";
			public static final String CREATE_RISK_ANALISYS = "";
			
		}
		
		public static class ManagerTransfer{
			public static final String CREATE_MANAGER_TRANSFER = "C75EEC27-780C-4C0C-9FD7-9F090142CB10";
			public static final String CANCEL_MANAGER_TRANSFER = "C8FCD8FD-B616-4252-942F-9F650155B3BC";
			public static final String ACCEPT_MANAGER_TRANSFER = "8B3AB41F-76F6-4311-8600-9F65014CF682";
		}

		public static class InsurancePolicyProcess {
			public static final String UPDATE_POLICY = "1F7B31EC-9388-4EA0-816C-9F81013A8ED4";
			public static final String DELETE_POLICY = "";
			public static final String CREATE_EXERCISE = "C86BB300-28C1-47EF-A48D-9FD4010F9149";
			public static final String CREATE_RECEIPT = "C4117861-16EB-40DB-A771-9F8A00EA6B32";
			public static final String CREATE_INSURED_OBJECT = "984226BB-33EA-4988-A802-9FD4010FC200";
			public static final String VALIDATE_POLICY = "841E9154-8037-46F7-8B98-9FD3011A9605";
			public static final String VOID_POLICY = "DF2C50EF-5E4A-475A-AB1E-9FD401143D8A";
			public static final String TRANSFER_BROKERAGE = null;
			public static final String CREATE_SUBSTITUTE_POLICY = null;
			public static final String CREATE_CLIENT_INFO_REQUEST = null;
			public static final String CREATE_COMPANY_INFO_REQUEST = null;
			public static final String CREATE_INSURED_OBJECT_FROM_CLIENT = "783EFBDB-44DB-4CAA-BF88-9FD4010FDB91";
			public static final String TRANSFER_MANAGER = "EF9C257E-323C-4BA0-A158-9F9000D8A755";
			public static final String EXECUTE_DETAILED_CALCULATIONS = "7DC1A798-31AA-49A1-B2DC-9FD4010F7593";
			public static final String CREATE_INFO_MANAGEMENT_PROCESS = null;
			public static final String CREATE_SUB_POLICY = null;
			public static final String CREATE_CREDIT_NOTE = "4CC51237-FC83-4B13-A8A9-9FD401100FE2";
			public static final String CREATE_NEGOTIATION = null;
			public static final String CREATE_HEALTH_EXPENSE = null;
			public static final String CREATE_RISK_ANALISYS = null;
		}

	}

	//Session related constants
	public static class Session{
		public static final int DEFAULT_DOMAIN_INDEX = 0;
		
		public static final String[] DOMAINS = new String[] {
			"CrediteEGS",
			"AMartins"
		};
	}

}
