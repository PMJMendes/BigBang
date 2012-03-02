package bigBang.definitions.shared;

import java.io.Serializable;

public class IncomingMessage
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static class AttachmentUpgrade
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String name;
		public String docTypeId;
		public String storageId;
	}

	public String notes;
	public String emailId;
	public AttachmentUpgrade[] upgrades;
}
