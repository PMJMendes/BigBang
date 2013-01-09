package bigBang.definitions.shared;

public class MedicalFileStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String reference; //Read-only, criado no server
	public String nextDate;
	public String inheritClientName;
	public String inheritObjectName;
	public boolean isRunning;
}
