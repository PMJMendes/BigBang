package bigBang.definitions.shared;

import java.io.Serializable;

public class Address
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String street1;
	public String street2;
	public String street3;
	public ZipCode zipCode;

	public Address()
	{
		street1 = street2 = null;
		zipCode = new ZipCode();
	}

	public Address(Address orig)
	{
		this.street1 = orig.street1;
		this.street2 = orig.street2;
		this.street3 = orig.street3;
		this.zipCode = (orig.zipCode == null ? null : new ZipCode(orig.zipCode));
	}
}
