package bigBang.definitions.shared;

public class QuoteRequestObjectStub
	extends CompositeFieldContainer
{
	private static final long serialVersionUID = 1L;

	public static enum Change
	{
		NONE,
		CREATED,
		MODIFIED,
		DELETED
	}

	public String unitIdentification;
	public Address address;
	public String typeId;
	public String typeText;

	public Change change;

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

	public QuoteRequestObjectStub(QuoteRequestObjectStub data, QuoteRequestObjectStub struct)
	{
		super(struct);
	
		this.unitIdentification = data.unitIdentification;
		this.address = (data.address == null ? null : new Address(data.address));
		this.typeId = data.typeId;
		this.typeText = data.typeText;
		this.change = data.change;
	}
}
