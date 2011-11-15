package bigBang.definitions.shared;

import java.io.Serializable;

public class InsuredObject
	implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String id;
	public String unitIdentification;
	public Address address;
	public String inclusionDate;
	public String exclusionDate;

	public String scratchPadId; // Temporary ID for an insured object in a policy scratch pad
	public int objectIndex; // Index of the insured object in the policy scratch pad

	public InsuredObject()
	{
		scratchPadId = null;
		objectIndex = -1;
	}
}
