package bigBang.definitions.shared;

public class Conversation
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum Direction
	{
		INCOMING,
		OUTGOING
	}

	public String parentDataObjectId; //*
	public String parentDataTypeId; //*
	public String managerId;
	public String requestTypeId; //*
	public String subject;
	public Direction startDir;
	public Direction pendingDir;
	public Integer replylimit; //*

	public Message[] messages;
}
