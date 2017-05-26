package bigBang.definitions.shared;

public class SubPolicyStub
	extends StructuredFieldContainer
{
	private static final long serialVersionUID = 1L;

	public String number;
	public String mainPolicyId;
	public String mainPolicyNumber;
	public String clientId;
	public String clientNumber;
	public String clientName;
	public String inheritCategoryName;
	public String inheritCategoryId;
	public String inheritLineName;
	public String inheritSubLineName;
	public String inheritCompanyName;
	public String statusId;
	public String statusText;
	public InsurancePolicy.PolicyStatus statusIcon;
	
	public SubPolicyStub()
	{
		headerFields = null;
		columnFields = null;
		extraFields = null;
		exerciseData = null;
	}
	
	public SubPolicyStub(SubPolicyStub orig)
	{
		super(orig);

		this.number = orig.number;
		this.mainPolicyId = orig.mainPolicyId;
		this.mainPolicyNumber = orig.mainPolicyNumber;
		this.clientId = orig.clientId;
		this.clientNumber = orig.clientNumber;
		this.clientName = orig.clientName;
		this.inheritCategoryName = orig.inheritCategoryName;
		this.inheritCategoryId = orig.inheritCategoryId;
		this.inheritLineName = orig.inheritLineName;
		this.inheritSubLineName = orig.inheritSubLineName;
		this.inheritCompanyName = orig.inheritCompanyName;
		this.statusId = orig.statusId;
		this.statusText = orig.statusText;
		this.statusIcon = orig.statusIcon;
	}
}
