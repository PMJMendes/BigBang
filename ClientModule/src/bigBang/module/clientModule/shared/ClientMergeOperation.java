package bigBang.module.clientModule.shared;

import bigBang.library.client.Operation;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ClientMergeOperation implements Operation {

	public static final String OWNER_PROCESS_ID = ClientProcess.ID;
	public static final String ID = "clientMergeOperation";
	private final String DESCRIPTION = "A operação de fusão de clientes";
	private final String SHORT_DESCRIPTION = "Fusão";
	
	public void init() {
		// TODO Auto-generated method stub

	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public String getOwnerProcessId() {
		return OWNER_PROCESS_ID;
	}

}
