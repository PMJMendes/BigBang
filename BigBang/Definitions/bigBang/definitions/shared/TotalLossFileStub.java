package bigBang.definitions.shared;

public class TotalLossFileStub
	extends ProcessBase
{
	private static final long serialVersionUID = 1L;

	public String reference; // Read-only, criado no server
	public String salvageTypeId;
	public String salvageTypeLabel; // Só para mostrar
	public String inheritClientName;
	public String inheritObjectName;
	public boolean isRunning;
}
