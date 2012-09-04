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
		public IncomingMessage message;
		public Integer replylimit;

		public Incoming()
		{
			message = new IncomingMessage();
		}
	}

	//The outgoing component of the conversation
	public static class Outgoing
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String requestId;
		public OutgoingMessage message;
		public boolean isFinal;
		public Integer replylimit;

		public Outgoing()
		{
			message = new OutgoingMessage();
		}
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

	public String parentDataObjectId;
	public String parentDataTypeId;
	public String subject;
	public IncomingMessage message;
	public Integer replylimit;
	public String originalFrom;
	public String fromInfoId;

	public ExternalInfoRequest()
	{
		message = new IncomingMessage();
	}
}
