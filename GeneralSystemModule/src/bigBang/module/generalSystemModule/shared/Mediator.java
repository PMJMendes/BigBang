package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

import bigBang.library.shared.Address;
import bigBang.library.shared.Contact;
import bigBang.library.shared.Document;

public class Mediator
	implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String name;
	public String ISPNumber;
	public String taxNumber;
	public String NIB;
	public CommissionProfile comissionProfile;
	public Address address;
	public Contact[] contacts;
	public Document[] documents;

	public Mediator()
	{
		comissionProfile = new CommissionProfile();
		contacts = new Contact[0];
	}
}
