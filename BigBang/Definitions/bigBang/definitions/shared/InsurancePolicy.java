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

	public static class Coverage
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String coverageId;
		public String coverageName;
		public boolean mandatory;
		public int order;
		public Boolean presentInPolicy;

		public Coverage()
		{
		}

		public Coverage(Coverage orig)
		{
			this.coverageId = orig.coverageId;
			this.coverageName = orig.coverageName;
			this.mandatory = orig.mandatory;
			this.order = orig.order;
			this.presentInPolicy = orig.presentInPolicy;
		}
	}

	public static class ColumnHeader
		implements Serializable
	{
		private static final long serialVersionUID = 1L;

		public String label;
		public FieldType type;
		public String unitsLabel;
		public String refersToId;

		public ColumnHeader()
		{
		}

		public ColumnHeader(ColumnHeader orig)
		{
			this.label = orig.label;
			this.type = orig.type;
			this.unitsLabel = orig.unitsLabel;
			this.refersToId = orig.refersToId;
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

	public Coverage[] coverages;
	public ColumnHeader[] columns;

	public InsuredObject emptyObject;
	public InsuredObject[] changedObjects;

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
		this.emptyObject = (orig.emptyObject == null ? null : new InsuredObject(orig.emptyObject));

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

		if ( orig.coverages == null )
			this.coverages = null;
		else
		{
			this.coverages = new Coverage[orig.coverages.length];
			for ( i = 0; i < this.coverages.length; i++ )
				this.coverages[i] = (orig.coverages[i] == null ? null : new Coverage(orig.coverages[i]));
		}

		if ( orig.columns == null )
			this.columns = null;
		else
		{
			this.columns = new ColumnHeader[orig.columns.length];
			for ( i = 0; i < this.columns.length; i++ )
				this.columns[i] = (orig.columns[i] == null ? null : new ColumnHeader(orig.columns[i]));
		}

		if ( orig.changedObjects == null )
			this.changedObjects = null;
		else
		{
			this.changedObjects = new InsuredObject[orig.changedObjects.length];
			for ( i = 0; i < this.changedObjects.length; i++ )
				this.changedObjects[i] = (orig.changedObjects[i] == null ? null : new InsuredObject(orig.changedObjects[i]));
		}
	}
}
