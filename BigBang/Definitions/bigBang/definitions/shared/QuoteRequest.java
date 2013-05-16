package bigBang.definitions.shared;

public class QuoteRequest
	extends QuoteRequestStub
{
	private static final long serialVersionUID = 1L;

	public String managerId;
	public String mediatorId;
	public String inheritMediatorId;
	public String inheritMediatorName;
	public String notes;
	public String docushare;

	public Contact[] contacts;
	public Document[] documents;

	public QuoteRequest()
	{
	}

	public QuoteRequest(QuoteRequest orig)
	{
		super(orig);

		int i;

		this.managerId = orig.managerId;
		this.mediatorId = orig.mediatorId;
		this.inheritMediatorId = orig.inheritMediatorId;
		this.inheritMediatorName = orig.inheritMediatorName;
		this.notes = orig.notes;
		this.docushare = orig.docushare;

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
