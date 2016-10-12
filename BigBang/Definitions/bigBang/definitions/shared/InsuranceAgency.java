package bigBang.definitions.shared;

import java.io.Serializable;

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
	public String accountingCode;
	public boolean taxRetention;
	public Contact[] contacts;
	public Document[] documents;

	public InsuranceAgency()
	{
		ownMediatorCodes = new String[0];
		contacts = new Contact[0];
		documents = new Document[0];
	}
	
	public InsuranceAgency(InsuranceAgency original){
		this.id = original.id;
		this.name = original.name;
		this.acronym = original.acronym;
		this.ISPNumber = original.ISPNumber;
		this.ownMediatorCodes = original.ownMediatorCodes;
		this.taxNumber = original.taxNumber;
		this.NIB = original.NIB;
		this.address = original.address;
		this.accountingCode = original.accountingCode;
		this.taxRetention = original.taxRetention;
		this.contacts = original.contacts;
		this.documents = original.documents;
	}
}
