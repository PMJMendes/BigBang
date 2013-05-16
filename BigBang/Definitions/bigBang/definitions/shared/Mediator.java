package bigBang.definitions.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Mediator
	implements Serializable
{
	public static class MediatorException
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String clientId;
		public String policyId;
		public Double percent;
	}

	private static final long serialVersionUID = 1L;

	public String id;
	public String name;
	public String ISPNumber;
	public String taxNumber;
	public String NIB;
	public CommissionProfile comissionProfile;
	public Double basePercent;
	public boolean hasRetention; // Novo!
	public Address address;
	public String accountingCode;
	public Map<String, Double> dealPercents;
	public MediatorException[] exceptions;
	public Contact[] contacts;
	public Document[] documents;

	public Mediator()
	{
		comissionProfile = new CommissionProfile();
		dealPercents = new HashMap<String, Double>();
		exceptions = new MediatorException[0];
		contacts = new Contact[0];
		documents = new Document[0];
	}

	public Mediator(Mediator original)
	{
		this.id = original.id;
		this.name = original.name;
		this.ISPNumber = original.ISPNumber;
		this.taxNumber = original.taxNumber;
		this.NIB = original.NIB;
		this.comissionProfile = original.comissionProfile;
		this.basePercent = original.basePercent;
		this.address = original.address;
		this.accountingCode = original.accountingCode;
		this.dealPercents = original.dealPercents;
		this.exceptions = original.exceptions;
		this.contacts = original.contacts;
		this.documents = original.documents;
	}
}
