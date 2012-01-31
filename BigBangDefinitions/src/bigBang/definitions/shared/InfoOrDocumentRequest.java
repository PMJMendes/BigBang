package bigBang.definitions.shared;

import java.io.Serializable;

public class InfoOrDocumentRequest
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	//The response to an info or document request
	public static class Response
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id;
		public String requestId;
		public String content;
	}

	//The cancellation data
	public static class Cancellation
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

//		public String id;
		public String requestId;
		public String motiveId;
	}

	public String parentDataObjectId;
	public String parentDataTypeId;
	public String requestTypeId;
	public String subject;
	public String text;
	public int replylimit;
	public String toContactInfoId;
	public String[] forwardUserIds;
	public String internalBCCs;
	public String externalCCs;
}
