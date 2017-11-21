package bigBang.definitions.shared;

import java.io.Serializable;

public class User
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String decoId;
	public String name;
	public String username;
	public String password; //Filled only for the current user
	public UserProfile profile;
	public String costCenterId;
	public String email;
	public String delegateId;
	public String defaultPrinter;
	public String mediatorId;
	public String title;
	public String phone;
	public boolean changeInsurer;

	public User() {};

	public User(User original)
	{
		this.id = original.id;
		this.decoId = original.decoId;
		this.name = original.name;
		this.username = original.username;
		this.password = original.password;
		this.profile = original.profile;
		this.costCenterId = original.costCenterId;
		this.email = original.email;
		this.defaultPrinter = original.defaultPrinter;
		this.mediatorId = original.mediatorId;
		this.title = original.title;
		this.phone = original.phone;
		this.changeInsurer = original.changeInsurer;
	}
}
