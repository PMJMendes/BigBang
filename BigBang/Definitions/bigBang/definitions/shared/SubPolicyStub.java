package bigBang.definitions.shared;

public class SubPolicyStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum PolicyStatus {
		PROVISIONAL,
		VALID,
		OBSOLETE
	}

	public String number;
	public String mainPolicyId;
	public String mainPolicyNumber;
	public String clientId;
	public String clientNumber;
	public String clientName;
	public String inheritCategoryName;
	public String inheritLineName;
	public String inheritSubLineName;
	public String inheritCompanyName;
	public String statusId;
	public String statusText;
	public PolicyStatus statusIcon;
}
