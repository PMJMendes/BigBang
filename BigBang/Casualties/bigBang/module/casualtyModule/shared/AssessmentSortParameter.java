package bigBang.module.casualtyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class AssessmentSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE,
		REFERENCE,
		CLIENT_NAME
	}

	public AssessmentSortParameter(){}

	public AssessmentSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}

}
