package bigBang.definitions.shared;

public class InsurancePolicyStub
	extends StructuredFieldContainer
{
	private static final long serialVersionUID = 1L;

	public static enum PolicyStatus
	{
		PROVISIONAL,
		VALID,
		OBSOLETE
	}

	public String number;
	public String clientId;
	public String clientNumber;
	public String clientName;
	public String categoryId;
	public String categoryName;
	public String lineId;
	public String lineName;
	public String subLineId;
	public String subLineName;
	public String insuredObject;
	public boolean caseStudy;
	public String statusId;
	public String statusText;
	public PolicyStatus statusIcon;
	
	public InsurancePolicyStub()
	{
		headerFields = null;
		columnFields = null;
		extraFields = null;
		exerciseData = null;
	}
	
	public InsurancePolicyStub(InsurancePolicyStub orig)
	{
		super(orig);

		this.number = orig.number;
		this.clientId = orig.clientId;
		this.clientNumber = orig.clientNumber;
		this.clientName = orig.clientName;
		this.categoryId = orig.categoryId;
		this.categoryName = orig.categoryName;
		this.lineId = orig.lineId;
		this.lineName = orig.lineName;
		this.subLineId = orig.subLineId;
		this.subLineName = orig.subLineName;
		this.insuredObject = orig.insuredObject;
		this.caseStudy = orig.caseStudy;
		this.statusId = orig.statusId;
		this.statusText = orig.statusText;
		this.statusIcon = orig.statusIcon;
	}
}
