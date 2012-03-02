package bigBang.module.quoteRequestModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class NegotiationSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE
	}

	public NegotiationSortParameter(){}

	public NegotiationSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
