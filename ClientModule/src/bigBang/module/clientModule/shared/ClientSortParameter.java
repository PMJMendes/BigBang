package bigBang.module.clientModule.shared;

import bigBang.library.shared.SortOrder;
import bigBang.library.shared.SortParameter;

public class ClientSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		NAME,
		GROUP,
		NUMBER
	}

	public ClientSortParameter()
	{
	}

	public ClientSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
