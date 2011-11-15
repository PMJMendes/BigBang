package bigBang.module.insurancePolicyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class InsurancePolicySortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		NUMBER,
		CATEGORY_LINE_SUBLINE,
		CLIENT_NAME,
		CLIENT_NUMBER
	}

	public InsurancePolicySortParameter(){}

	public InsurancePolicySortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
