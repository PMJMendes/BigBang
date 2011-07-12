package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

public class ClientGroup
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String name;
	public String id;
	public String parentGroupId;
	public String[] memberIds;
}
