package bigBang.definitions.shared;

public class AssessmentStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String reference; //Read-only, criado no server
	public String scheduledDate;
	public String effectiveDate;
	public String inheritClientName;
	public String inheritObjectName;
	public boolean isRunning;
}
