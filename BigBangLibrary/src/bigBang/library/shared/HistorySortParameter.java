package bigBang.library.shared;

import bigBang.definitions.client.dataAccess.SortOrder;
import bigBang.definitions.client.dataAccess.SortParameter;

public class HistorySortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		TIMESTAMP
	}

	public HistorySortParameter()
	{
	}

	public HistorySortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
