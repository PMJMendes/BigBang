package bigBang.module.insurancePolicyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class InsuredObjectSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		NAME
	}

	public InsuredObjectSortParameter(){}

	public InsuredObjectSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
