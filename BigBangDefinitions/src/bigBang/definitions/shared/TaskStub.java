package bigBang.definitions.shared;

import java.io.Serializable;

public class TaskStub
	extends SearchResult
{
	public enum Status implements Serializable {
		VALID,
		PENDING,
		URGENT,
	}
	
	private static final long serialVersionUID = 1L;

	public String description;
	public String timeStamp;
	public String dueDate;
	public Status status;
}
