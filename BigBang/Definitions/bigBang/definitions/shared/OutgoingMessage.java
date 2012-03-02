package bigBang.definitions.shared;

import java.io.Serializable;

public class OutgoingMessage
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String toContactInfoId;
	public String[] forwardUserFullNames;
	public String internalBCCs;
	public String externalCCs;

	public String subject;
	public String text;
}
