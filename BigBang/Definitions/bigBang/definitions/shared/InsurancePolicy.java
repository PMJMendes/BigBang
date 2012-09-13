package bigBang.definitions.shared;

import java.io.Serializable;

public class InsurancePolicy
	extends InsurancePolicyStub
{
	private static final long serialVersionUID = 1L;

	public static class CoInsurer
		implements Serializable
	{
		private static final long serialVersionUID = 1L;
	
		public String insuranceAgencyId;
		public Double percent;

		public CoInsurer()
		{
		}

		public CoInsurer(CoInsurer orig)
		{
			this.insuranceAgencyId = orig.insuranceAgencyId;
			this.percent = orig.percent;
		}
	}

	public String managerId;
	public String insuranceAgencyId;
	public String startDate;
	public String durationId;
	public String fractioningId;
	public Integer maturityDay;
	public Integer maturityMonth;
	public String expirationDate;
	public String notes;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Double premium;
	public String operationalProfileId;
	public String docushare;

	public CoInsurer[] coInsurers;

	public Contact[] contacts;
	public Document[] documents;

	public InsurancePolicy()
	{
		contacts = new Contact[0];
		documents = new Document[0];
		headerFields = new HeaderField[0];
		columnFields = new ColumnField[0];
		extraFields = new ExtraField[0];
		exerciseData = new ExerciseData[0];
		coverages = new Coverage[0];
		columns = new ColumnHeader[0];
		changedObjects = new InsuredObject[0];
	}

	public InsurancePolicy(InsurancePolicy orig)
	{
		super(orig);

		int i;

		this.managerId = orig.managerId;
		this.insuranceAgencyId = orig.insuranceAgencyId;
		this.startDate = orig.startDate;
		this.durationId = orig.durationId;
		this.fractioningId = orig.fractioningId;
		this.maturityDay = orig.maturityDay;
		this.maturityMonth = orig.maturityMonth;
		this.expirationDate = orig.expirationDate;
		this.notes = orig.notes;
		this.mediatorId = orig.mediatorId;
		this.inheritMediatorId = orig.inheritMediatorId;
		this.inheritMediatorName = orig.inheritMediatorName;
		this.premium = orig.premium;
		this.operationalProfileId = orig.operationalProfileId;
		this.docushare = orig.docushare;

		if ( orig.coInsurers == null )
			this.coInsurers = null;
		else
		{
			this.coInsurers = new CoInsurer[orig.coInsurers.length];
			for ( i = 0; i < this.coInsurers.length; i++ )
				this.coInsurers[i] = (orig.coInsurers[i] == null ? null : new CoInsurer(orig.coInsurers[i]));
		}

		if ( orig.contacts == null )
			this.contacts = null;
		else
		{
			this.contacts = new Contact[orig.contacts.length];
			for ( i = 0; i < this.contacts.length; i++ )
				this.contacts[i] = (orig.contacts[i] == null ? null : new Contact(orig.contacts[i]));
		}

		if ( orig.documents == null )
			this.documents = null;
		else
		{
			this.documents = new Document[orig.documents.length];
			for ( i = 0; i < this.documents.length; i++ )
				this.documents[i] = (orig.documents[i] == null ? null : new Document(orig.documents[i]));
		}
	}
}
