package bigBang.definitions.shared;

import java.io.Serializable;

public class TransactionSet
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	String id;
	String date;
	String user;
	boolean isComplete;
}
