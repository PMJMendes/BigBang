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
		public String notes;
		public String emailId;
		public int replylimit;
	}

	//The outgoing component of the conversation
	public static class Outgoing
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		public String requestId;
		public OutgoingMessage message;
		public boolean isFinal;
		public int replylimit;

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
	public String emailId;
	public String subject;
	public String text;
	public String originalFrom;
	public int replylimit;
	public AttachmentDocUpgrade[] upgrades;
}
