package bigBang.definitions.shared;

import java.io.Serializable;

public class Exercise
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String label;
	public String startDate;
	public String endDate;

	public String temporaryId; // Temporary ID for an exercise in a policy scratch pad

}
