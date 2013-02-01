package bigBang.definitions.shared;

public class ConversationStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum Direction
	{
		INCOMING,
		OUTGOING
	}

	public String parentDataObjectId;
	public String parentDataTypeId;
	public String requestTypeId;
	public String requestTypeLabel;
	public String subject;
	public ConversationStub.Direction pendingDir;
}
