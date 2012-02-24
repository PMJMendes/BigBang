package bigBang.definitions.shared;

import java.io.Serializable;

public class OutgoingHeaders
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String toContactInfoId;
	public String[] forwardUserIds;
	public String internalBCCs;
	public String externalCCs;
}
