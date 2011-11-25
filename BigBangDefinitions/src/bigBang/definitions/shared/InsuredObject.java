package bigBang.definitions.shared;

public class InsuredObject
	extends SearchResult
{
	private static final long serialVersionUID = 1L;

	public String unitIdentification;
	public Address address;
	public String inclusionDate;
	public String exclusionDate;

	public String tempObjectId; // Temporary ID for an insured object in a policy scratch pad

	public InsuredObject()
	{
		tempObjectId = null;
	}
}
