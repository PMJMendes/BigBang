package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

public class CostCenter implements Serializable {

	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String code;
	public String managerId;
	public User[] members;
	
}
