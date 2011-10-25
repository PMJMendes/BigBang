package bigBang.module.tasksModule.shared;

import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;

public class TaskSortParameter
	extends SortParameter
{
	private static final long serialVersionUID = 1L;

	public static enum SortableField
	{
	}

	public TaskSortParameter()
	{
	}

	public TaskSortParameter(SortableField field, SortOrder order)
	{
		this.field = field;
		this.order = order;
	}
}
