package bigBang.definitions.shared;

public class Object2Stub
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
	
	public Object2Stub()
	{
		headerFields = null;
		columnFields = null;
		extraFields = null;
		exerciseData = null;
	}
}
