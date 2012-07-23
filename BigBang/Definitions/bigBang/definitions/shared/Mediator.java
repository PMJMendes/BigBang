package bigBang.definitions.shared;

import java.io.Serializable;
import java.util.Map;

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
	public Double basePercent; // Novo!
	public Address address;
	public Map<String, Double> dealPercents; // Novo!
	public Contact[] contacts;
	public Document[] documents;

	public Mediator()
	{
		comissionProfile = new CommissionProfile();
		contacts = new Contact[0];
		dealPercents = new Map<String, Double>();
	}

	public Mediator(Mediator original){
		this.id = original.id;
		this.name = original.name;
		this.ISPNumber = original.ISPNumber;
		this.taxNumber = original.taxNumber;
		this.NIB = original.NIB;
		this.comissionProfile = original.comissionProfile;
		this.basePercent = original.basePercent;
		this.address = original.address;
		this.dealPercents = original.dealPercents;
		this.contacts = original.contacts;
		this.documents = original.documents;
	}
}
