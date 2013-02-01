package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class TotalLossSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		REFERENCE,
		CLIENT_NAME
	}

	public TotalLossSortParameter(){}

	public TotalLossSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
