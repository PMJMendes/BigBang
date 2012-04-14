package bigBang.definitions.shared;

import java.io.Serializable;

public class ReportItem
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static enum ItemType
	{
		PARAM,
		PRINTSET,
		TRANSACTIONSET,
		CATEGORY
	}

	public String id;
	public String label;
	public ItemType type;
}
