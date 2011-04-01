package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String username;
	public String password; //Filled only for the current user
	public String profileId;
	public String costCenterId;
	public String email;
}
