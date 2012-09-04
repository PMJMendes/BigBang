package bigBang.library.client.userInterface.view;

public interface OperationView {

	public String[] getImplementedOperations();
	public boolean implementsOperation(String operation);
	
	public void setProcesses(String[] processIds);
	public void setObjects(String[] objectIds);
	
}
