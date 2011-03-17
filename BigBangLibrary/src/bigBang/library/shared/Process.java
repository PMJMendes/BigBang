package bigBang.library.shared;

import java.util.HashMap;

import bigBang.library.shared.event.NewNotificationEvent;
import bigBang.library.shared.event.NewNotificationEventHandler;
import bigBang.library.shared.userInterface.TextBadge;

public abstract class Process implements Identifiable {

	private final String ID = ""; 
	private final String DESCRIPTION = "";
	private final String SHORT_DESCRIPTION = "";
	private TextBadge mainMenuTextBadge;
	private HashMap <String, Operation> operations;

	public Process(){
	}

	public String getId(){
		return ID;
	}

	public String getDescription() {
		return this.DESCRIPTION;
	}

	public String getShortDescription(){
		return this.SHORT_DESCRIPTION;
	}

	public HashMap <String, Operation> getOperations(){
		if(this.operations == null)
			init();
		return this.operations;
	}
	
	public TextBadge getMainMenuTextBadge(){
		if(this.mainMenuTextBadge == null){
			this.mainMenuTextBadge = new TextBadge("");
		}
		return this.mainMenuTextBadge;
	}

	protected void registerOperation(Operation operation) throws Exception {
		if(this.operations == null)
			init();

		if(this.operations.containsKey(operation.getId()))
			throw new Exception("The operation ID is already assigned to a managed operation");

		this.operations.put(operation.getId(), operation);
	}

	public void init() {
		this.operations = new HashMap <String, Operation> ();
	}

}
