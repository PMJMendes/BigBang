package bigBang.definitions.shared;

import java.io.Serializable;

public class DASRequest
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static class Response
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String requestId;
		public IncomingMessage message; // ATENÇÃO: Por enquanto, isto não é para usar!

		public Response()
		{
			message = new IncomingMessage();
		}
	}

	public static class Cancellation
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String requestId;
		public String motiveId;
	}

	public String receiptId;
	public int replylimit;
	public Receipt receiptAux;
	public OutgoingMessage message; // ATENÇÃO: Por enquanto, isto não é para usar!

	public DASRequest()
	{
		message = new OutgoingMessage();
	}
}
