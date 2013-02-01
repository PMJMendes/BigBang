package bigBang.definitions.shared;

import java.io.Serializable;

public class ContactInfo
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String typeId;
	public String value;

	public ContactInfo()
	{
	}

	public ContactInfo(ContactInfo orig)
	{
		this.id = orig.id;
		this.typeId = orig.typeId;
		this.value = orig.value;
	}
}
