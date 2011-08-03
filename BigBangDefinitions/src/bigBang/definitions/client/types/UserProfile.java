package bigBang.definitions.client.types;

import java.io.Serializable;

public class UserProfile implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	
	public UserProfile(){}
	
	public UserProfile(UserProfile original){
		this.id = original.id;
		this.name = original.name;
	}
}

