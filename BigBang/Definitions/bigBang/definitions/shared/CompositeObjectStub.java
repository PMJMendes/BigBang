package bigBang.definitions.shared;

public class CompositeObjectStub
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
	public String inclusionDate;
	public String exclusionDate;
	public String typeId;
	public String typeText;

	public Change change;

	public CompositeObjectStub()
	{
	}

	public CompositeObjectStub(CompositeObjectStub orig)
	{
		super(orig);
	
		this.unitIdentification = orig.unitIdentification;
		this.address = (orig.address == null ? null : new Address(orig.address));
		this.inclusionDate = orig.inclusionDate;
		this.exclusionDate = orig.exclusionDate;
		this.typeId = orig.typeId;
		this.typeText = orig.typeText;
		this.change = orig.change;
	}

	public CompositeObjectStub(CompositeObjectStub data, CompositeObjectStub struct)
	{
		super(struct);
	
		this.unitIdentification = data.unitIdentification;
		this.address = (data.address == null ? null : new Address(data.address));
		this.inclusionDate = data.inclusionDate;
		this.exclusionDate = data.exclusionDate;
		this.typeId = data.typeId;
		this.typeText = data.typeText;
		this.change = data.change;
	}
}
