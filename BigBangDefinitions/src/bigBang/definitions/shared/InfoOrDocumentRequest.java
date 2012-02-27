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

		public String requestId;
		public IncomingMessage message;

		public Response()
		{
			message = new IncomingMessage();
		}
	}

	//The cancellation data
	public static class Cancellation
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String requestId;
		public String motiveId;
	}

	public String parentDataObjectId;
	public String parentDataTypeId;
	public String requestTypeId;
	public OutgoingMessage message;
	public Integer replylimit;

	public InfoOrDocumentRequest()
	{
		message = new OutgoingMessage();
	}

}
