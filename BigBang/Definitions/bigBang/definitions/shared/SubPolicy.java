package bigBang.definitions.shared;

public class SubPolicy
	extends SubPolicyStub
{
	private static final long serialVersionUID = 1L;

	public String managerId;
	public String managerName;
	public String startDate;
	public String fractioningId;
	public String expirationDate;
	public String notes;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public Double premium;
	public String docushare;
	public String inheritSubLineId;
	public String inheritClientId;
	public String inheritClientNumber;
	public String inheritClientName;
	public Double totalPremium;

	public Contact[] contacts;
	public Document[] documents;

	public SubPolicy()
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

	public SubPolicy(SubPolicy orig)
	{
		super(orig);

		int i;

		this.managerId = orig.managerId;
		this.managerName = orig.managerName;
		this.startDate = orig.startDate;
		this.fractioningId = orig.fractioningId;
		this.expirationDate = orig.expirationDate;
		this.notes = orig.notes;
		this.inheritMediatorId = orig.inheritMediatorId;
		this.inheritMediatorName = orig.inheritMediatorName;
		this.premium = orig.premium;
		this.docushare = orig.docushare;
		this.inheritSubLineId = orig.inheritSubLineId;
		this.inheritClientId = orig.inheritClientId;
		this.inheritClientNumber = orig.inheritClientNumber;
		this.inheritClientName = orig.inheritClientName;
		this.totalPremium = orig.totalPremium;

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
