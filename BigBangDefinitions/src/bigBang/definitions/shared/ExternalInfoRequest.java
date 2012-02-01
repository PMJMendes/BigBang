package bigBang.definitions.shared;

import java.io.Serializable;

public class ExternalInfoRequest implements Serializable {

	//The response to an info or document request
	public static class Response implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public String id;
		public String requestId;
		public String content;
		public Negotiation negotiation; //The altered negotiation data
	}
	
	//The cancellation data
	public static class Cancellation implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public String id;
		public String requestId;
		public String motive;
		
	}

	private static final long serialVersionUID = 1L;

	public String id;
	public String documentType;
	public String text;
	public int replylimit;
	public String[] forwardUserIds;
	public String internalBCCs;
	public String externalCCs;

}
