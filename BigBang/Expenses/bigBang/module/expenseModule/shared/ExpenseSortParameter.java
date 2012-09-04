package bigBang.module.expenseModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class ExpenseSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField{
		RELEVANCE,
		NUMBER,
		DATE
	}

	public ExpenseSortParameter()
	{
	}

	public ExpenseSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
