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

	public static class AttachmentUpgrade
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
	
		public String name;
		public String attachmentId; //Outlook ID
		public String docTypeId;
		public String storageId;
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
		public String contactInfoId; //Só para FROM e TO //*
		public String display; //*
	}

	public String id;

	public String conversationId;
	public Integer order;
	public Conversation.Direction direction;
	public Kind kind; //*
	public String date; 
	public String subject; //Só pode vir a null se tiver emailId. //*
	public String text; //Só pode vir a null se tiver emailId. //*
	public String emailId; //Se fôr incoming e kind=EMAIL, obrigatório. Se não, tem que vir a null. //*

	public AttachmentUpgrade[] incomingAttachments; //*
	public String[] outgoingAttachmentIds; //*

	public MsgAddress[] addresses;  //*
}
