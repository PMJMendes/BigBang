package bigBang.library.shared;

import java.io.Serializable;

public class Permission
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String instanceId; // Null if this op is currently invalid for the process
	public int reserved; //JMMM: Always 0 until we implement urgency levels
}
