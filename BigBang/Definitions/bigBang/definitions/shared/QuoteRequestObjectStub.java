package bigBang.definitions.shared;

public class QuoteRequestObjectStub
	extends CompositeFieldContainer
{
	private static final long serialVersionUID = 1L;

	public String unitIdentification;
	public Address address;
	public String typeId;
	public String typeText;

	public InsuredObjectStub.Change change;

	public QuoteRequestObjectStub()
	{
	}

	public QuoteRequestObjectStub(QuoteRequestObjectStub orig)
	{
		super(orig);
	
		this.unitIdentification = orig.unitIdentification;
		this.address = (orig.address == null ? null : new Address(orig.address));
		this.typeId = orig.typeId;
		this.typeText = orig.typeText;
		this.change = orig.change;
	}

	public QuoteRequestObjectStub(QuoteRequestObjectStub orig, QuoteRequestObjectStub data)
	{
		super(data);
	
		this.unitIdentification = orig.unitIdentification;
		this.address = (orig.address == null ? null : new Address(orig.address));
		this.typeId = orig.typeId;
		this.typeText = orig.typeText;
		this.change = orig.change;
	}
}
