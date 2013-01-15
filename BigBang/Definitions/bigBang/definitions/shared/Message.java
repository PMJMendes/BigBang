package bigBang.definitions.shared;

import java.io.Serializable;

public class Message
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static enum Kind
	{
		EMAIL,
		NOTE
	}

	public static class Attachment
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String id;

		// For incoming
		public String name;
		public String attachmentId; //Outlook ID
		public String docTypeId;
		public String storageId;

		// For outgoing or display
		public String docId;
	}

	public static class MsgAddress
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static enum Usage
		{
			FROM,
			TO,
			REPLYTO,
			CC,
			BCC
		}

		public String id;

		public String address; //*
		public Usage usage; //*
		public String userId; //Só para REPLYTO //*
		public String contactInfoId; //Só para FROM, TO e CC //*
		public String display; //*
		public String ownerTypeId; //Só para TO, para pre-preencher a form
		public String ownerId; //Só para TO, para pre-preencher a form
	}

	public String id;

	public String conversationId;
	public Integer order;
	public ConversationStub.Direction direction;
	public Kind kind;
	public String date; 
	public String subject; //Só pode vir a null se tiver emailId.
	public String text; //Só pode vir a null se tiver emailId.
	public String emailId; //Se fôr incoming e kind=EMAIL, obrigatório. Se não, tem que vir a null.

	public Attachment[] attachments;

	public MsgAddress[] addresses;
}
