package bigBang.definitions.shared;

import java.io.Serializable;

public class Exercise
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;

	public String ownerId;

	public String label;
	public String startDate;
	public String endDate;
}
