package bigBang.definitions.shared;

public class Conversation
	extends ConversationStub
{
	private static final long serialVersionUID = 1L;

	public String managerId;
	public ConversationStub.Direction startDir;
	public Integer replylimit; //*

	public Message[] messages;
}
