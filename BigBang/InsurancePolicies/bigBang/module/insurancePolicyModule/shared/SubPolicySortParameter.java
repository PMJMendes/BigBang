package bigBang.module.insurancePolicyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class SubPolicySortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		NUMBER,
		CLIENT_NAME,
		CLIENT_NUMBER
	}

	public SubPolicySortParameter(){}

	public SubPolicySortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
