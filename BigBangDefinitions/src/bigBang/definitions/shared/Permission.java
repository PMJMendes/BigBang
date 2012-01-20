package bigBang.definitions.shared;

import java.io.Serializable;

public class Permission
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final class URGENCY_LEVEL{
		public static final int STANDARD = 0;
	}

	public String id;
	public String instanceId; // Null if this op is currently invalid for the process
	public int reserved; //JMMM: Always 0 until we implement urgency levels
	
	public boolean hasInstancePermission(){
		return instanceId != null;
	}
	
	public String getOperationInstanceId(){
		return instanceId;
	}
	
	public String getOperationTypeId(){
		return id;
	}
	
}
