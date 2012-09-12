package bigBang.definitions.shared;

import java.io.Serializable;

public class Contact
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String ownerTypeId;
	public String ownerId;
	public Address address;
	public String typeId;
	public String typeLabel;
	public ContactInfo[] info;
	public Contact[] subContacts;

	public Contact()
	{
		address = new Address();
		info = new ContactInfo[0];
		subContacts = new Contact[0];
	}

	public Contact(Contact orig)
	{
		int i;

		this.id = orig.id;
		this.name = orig.name;
		this.ownerTypeId = orig.ownerTypeId;
		this.ownerId = orig.ownerId;
		this.address = (orig.address == null ? null : new Address(orig.address));
		this.typeId = orig.typeId;
		this.typeLabel = orig.typeLabel;

		if ( orig.info == null )
			this.info = null;
		else
		{
			this.info = new ContactInfo[orig.info.length];
			for ( i = 0; i < this.info.length; i++ )
				this.info[i] = (orig.info[i] == null ? null : new ContactInfo(orig.info[i]));
		}

		if ( orig.subContacts == null )
			this.subContacts = null;
		else
		{
			this.subContacts = new Contact[orig.subContacts.length];
			for ( i = 0; i < this.subContacts.length; i++ )
				this.subContacts[i] = (orig.subContacts[i] == null ? null : new Contact(orig.subContacts[i]));
		}
	}
}
