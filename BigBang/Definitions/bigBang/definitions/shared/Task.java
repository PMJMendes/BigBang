package bigBang.definitions.shared;

public class Task
	extends TaskStub
{
	private static final long serialVersionUID = 1L;

	public String processTypeId;
	public String objectTypeId;

	public String longDesc;

	public String[] processIds;
	public String[] objectIds;
	public String[] operationIds;

	public String reportId;
	public String[] params;
}
