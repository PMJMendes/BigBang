package bigBang.module.tasksModule.shared;

import bigBang.definitions.shared.SearchParameter;

public class TaskSearchParameter
	extends SearchParameter
{
	private static final long serialVersionUID = 1L;

	public String processId;
	public String operationId;
	public String afterTimestamp;
}
