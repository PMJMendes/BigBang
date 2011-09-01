package bigBang.definitions.shared;

import java.io.Serializable;

public class Negotiation implements Serializable {

	public static class Deletion implements Serializable {

		private static final long serialVersionUID = 1L;

		public String id;
		public String negotiationId;
		public String motive;

	}
	
	//The cancellation for a negotiation
	public static class Cancellation implements Serializable {

		private static final long serialVersionUID = 1L;

		public String id;
		public String internalMotive;
		public String externalMotive;
		public boolean sendResponseToInsuranceAgency;
	}
	
	//The adjudication for a negotiation
	public static class Adjudication implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public String id;
		public String[] securedObjectIds;
		public String[] lineIds;
		public String effectiveDate;
	}
	
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String ownerQuoteRequestId;

}
