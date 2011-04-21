package bigBang.module.generalSystemModule.shared;

import java.io.Serializable;

import bigBang.library.shared.Address;
import bigBang.library.shared.Contact;
import bigBang.library.shared.Document;

public class InsuranceAgency
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String acronym; 
	public String ISPNumber;
	public String[] ownMediatorCodes;
	public String taxNumber;
	public String NIB;
	public Address address;
	public Contact[] contacts;
	public Document[] documents;

	public InsuranceAgency()
	{
		ownMediatorCodes = new String[0];
		contacts = new Contact[0];
	}
}
