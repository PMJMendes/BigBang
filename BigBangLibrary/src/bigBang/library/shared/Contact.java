package bigBang.library.shared;

import java.io.Serializable;

public class Contact
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String entityTypeId;
	public String entityId;
	public Address address;
	public ContactInfo[] info;
	public Contact[] subContacts;

	public Contact()
	{
		address = new Address();
		info = new ContactInfo[0];
		subContacts = new Contact[0];
	}
}
