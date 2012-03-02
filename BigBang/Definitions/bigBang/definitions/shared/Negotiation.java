package bigBang.definitions.shared;

import java.io.Serializable;

public class Negotiation
	extends NegotiationStub
{
	public static class QuoteRequestInfo
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public OutgoingMessage message;
		public String[] attachedDocumentIds;
		public int replylimit;

		public QuoteRequestInfo()
		{
			message = new OutgoingMessage();
		}
	}

	public static class Cancellation
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public String internalMotiveId;
		public boolean sendResponseToInsuranceAgency;
		public OutgoingMessage message;

		public Cancellation()
		{
			message = new OutgoingMessage();
		}
	}

	public static class Response
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public IncomingMessage message;

		public Response()
		{
			message = new IncomingMessage();
		}
	}

	public static class Grant
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public OutgoingMessage message;
		public String effectDate;
//		public String[] securedObjectIds;
//		public String[] lineIds;

		public Grant()
		{
			message = new OutgoingMessage();
		}
	}

	public static class Deletion
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public String motive;
	}

	private static final long serialVersionUID = 1L;

	public String managerId;

	public String notes;
	public Contact[] contacts;
	public Document[] documents;
}
