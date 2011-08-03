package bigBang.definitions.client.types;

import java.io.Serializable;

public class CostCenter implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CostCenter(){
		members = new User[0];
	}

	public CostCenter(CostCenter original){
		this.id = original.id;
		this.name = original.name;
		this.code = original.code;
		this.managerId = original.managerId;
		this.members = original.members;
	}
	
	public String id;
	public String name;
	public String code;
	public String managerId;
	public User[] members;
	
}
