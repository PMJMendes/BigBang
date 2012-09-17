package bigBang.definitions.shared;

public class InsuredObjectStub
	extends ComplexFieldContainer
{
	private static final long serialVersionUID = 1L;

	public static enum Change
	{
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
	
	public InsuredObjectStub()
	{
		headerFields = null;
		columnFields = null;
		extraFields = null;
		exerciseData = null;
	}

	public InsuredObjectStub(InsuredObjectStub orig)
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
}
