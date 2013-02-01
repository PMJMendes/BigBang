package bigBang.definitions.shared;

import java.io.Serializable;

public class ClientGroup
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String name;
	public String id;
	public String parentGroupId;
	public ClientGroup[] subGroups;
	
	public ClientGroup(){
		subGroups = new ClientGroup[0];
	}
	
	public ClientGroup(ClientGroup original){
		this.id = original.id;
		this.name = original.name;
		this.parentGroupId = original.parentGroupId;
		this.subGroups = original.subGroups;
	}
}
