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
		public String[] attachedDocumentIds;
		public int replylimit;
		public OutgoingHeaders headers;
	}

	public static class Cancellation
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public String internalMotiveId;
		public String externalMotiveId;
		public boolean sendResponseToInsuranceAgency;
	}

	public static class Response
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public String notes;
		public String emailId;
		public AttachmentDocUpgrade[] upgrades;
	}

	public static class Grant
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String negotiationId;
		public String[] securedObjectIds;
		public String[] lineIds;
		public String effectiveDate;
		public OutgoingHeaders headers;
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
