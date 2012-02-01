package bigBang.definitions.shared;

import java.io.Serializable;

public class ExternalInfoRequest
	extends ProcessBase
{
	//The incoming component of the conversation
	public static class Incoming
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String requestId;
		public String content;
	}
	
	//The outgoing component of the conversation
	public static class Outgoing
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String requestId;
		public String content;
	}
	
	//The closing data
	public static class Closing
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String requestId;
		public String motiveId;
	}

	private static final long serialVersionUID = 1L;

	public String notes;
	public String emailId;
}
