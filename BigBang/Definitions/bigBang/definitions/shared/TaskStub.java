package bigBang.definitions.shared;

import java.io.Serializable;

public class TaskStub
	extends SearchResult
{
	public enum Status implements Serializable {
		INVALID,
		VALID,
		PENDING,
		URGENT,
		COMPLETED
	}

	private static final long serialVersionUID = 1L;

	public String description;
	public String reference;
	public String clientName;
	public String timeStamp;
	public String dueDate;
	public String casualtyProcess;
	public Status status;
}
