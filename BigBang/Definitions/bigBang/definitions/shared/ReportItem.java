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

	public static class ReportParam
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public static enum ParamType
		{
			NUMERIC,
			TEXT,
			LIST,
			REFERENCE,
			BOOLEAN,
			DATE
		}

		public String label;
		public ParamType type;
		public String unitsLabel;
		public String refersToId;
	}

	public String id;
	public String label;
	public ItemType type;
}
