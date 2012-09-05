package bigBang.definitions.shared;

public class InsuredObjectStub
	extends ComplexFieldContainer
{
	private static final long serialVersionUID = 1L;

	public String ownerId;

	public String unitIdentification;
	public Address address;
	public String inclusionDate;
	public String exclusionDate;
	public String typeId;
	public String typeText;
	
	public InsuredObjectStub()
	{
		headerFields = null;
		columnFields = null;
		extraFields = null;
		exerciseData = null;
	}
}
