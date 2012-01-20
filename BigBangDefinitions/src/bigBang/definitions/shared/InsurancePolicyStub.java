package bigBang.definitions.shared;

public class InsurancePolicyStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public static enum PolicyStatus {
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
	public boolean caseStudy;
	public String statusId;
	public String statusText;
	public PolicyStatus statusIcon;

	public String processId;
}
