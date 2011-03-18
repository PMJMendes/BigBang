package bigBang.module.tasksModule.shared;

import java.io.Serializable;

public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String operationId;
	public String operationInstanceId;
	public String targetId;
	public String id;
	
	public String description;
	public String entryDate;
	public String requesterName;

}
