package bigBang.definitions.shared;

import java.io.Serializable;

public class Contact
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public boolean isSubContact;
	public String ownerId;
	public Address address;
	public String typeId;
	public ContactInfo[] info;
	public Contact[] subContacts;

	public Contact()
	{
		address = new Address();
		info = new ContactInfo[0];
		subContacts = new Contact[0];
	}
}
