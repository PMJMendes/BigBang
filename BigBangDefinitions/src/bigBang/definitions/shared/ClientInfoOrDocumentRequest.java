package bigBang.definitions.shared;

import java.io.Serializable;

public class ClientInfoOrDocumentRequest implements Serializable {
	
	//The response to an info or document request
	public static class Response implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public String id;
		public String requestId;
		public String content;
		public Client clientData;
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
