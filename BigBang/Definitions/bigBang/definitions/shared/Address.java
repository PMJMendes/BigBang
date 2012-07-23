package bigBang.definitions.shared;

import java.io.Serializable;

public class Address
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String street1;
	public String street2;
	public ZipCode zipCode;

	public Address()
	{
		street1 = street2 = null;
		zipCode = new ZipCode();
	}
}
