package bigBang.definitions.shared;

import java.io.Serializable;

public class IncomingMessage
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
		public String attachmentId;
		public String docTypeId;
		public String storageId;
	}

	public Kind kind;
	public String notes;
	public String emailId;
	public bigBang.definitions.shared.Message.AttachmentUpgrade[] upgrades;
	
	public IncomingMessage(Message message) {
		this.emailId = message.emailId;
		this.kind = Message.Kind.NOTE.equals(message.kind) ? Kind.NOTE : Kind.EMAIL;
		this.notes = Kind.NOTE.equals(this.kind) ? message.text : null;
		this.upgrades = message.incomingAttachments;
	}
	
	public Message toMessage(){
		Message toReturn = new Message();
		
		toReturn.emailId = this.emailId;
		toReturn.text = Kind.NOTE.equals(this.kind) ? this.notes : null;
		toReturn.kind = Kind.NOTE.equals(this.kind) ? Message.Kind.NOTE : Message.Kind.EMAIL;
		toReturn.incomingAttachments = this.upgrades;
		
		return toReturn;
	}
	
	public IncomingMessage() {
	}
}
