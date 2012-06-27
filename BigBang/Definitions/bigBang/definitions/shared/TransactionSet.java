package bigBang.definitions.shared;

import java.io.Serializable;

public class TransactionSet
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String date;
	public String user;
	public boolean isComplete;

	public String userName;
}
