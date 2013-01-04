package bigBang.definitions.shared;

public class Assessment
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String reference; //Read-only, criado no server
	public String subCasualtyId;
	public String subCasualtyNumber;
	public String scheduledDate;
	public String effectiveDate;
	public Boolean isConditional;
	public Boolean isTotalLoss;
	public String notes;
}
