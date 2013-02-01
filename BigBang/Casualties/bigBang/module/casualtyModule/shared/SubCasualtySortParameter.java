package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class SubCasualtySortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		NUMBER
	}

	public SubCasualtySortParameter(){}

	public SubCasualtySortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
