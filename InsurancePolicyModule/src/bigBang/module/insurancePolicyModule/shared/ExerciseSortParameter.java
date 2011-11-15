package bigBang.module.insurancePolicyModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class ExerciseSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
		RELEVANCE
	}

	public ExerciseSortParameter(){}

	public ExerciseSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
