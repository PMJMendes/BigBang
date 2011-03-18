package bigBang.module.clientModule.shared;

import java.io.Serializable;

import bigBang.library.client.Process;

import com.google.gwt.core.client.GWT;

public class ClientProcess extends Process implements Serializable {

	private static final long serialVersionUID = -571313258157748790L;
	
	public static final String ID = "PROCESS CLIENT"; 
	private final String DESCRIPTION = "Cliente"; 
	private final String SHORT_DESCRIPTION = "Cliente"; 

	public ClientProcess(){
		ClientSearchOperation clientSearchOperation = new ClientSearchOperation();
		ClientMergeOperation clientMergeOperation = new ClientMergeOperation();
		try {
			this.registerOperation(clientSearchOperation);
			this.registerOperation(clientMergeOperation);
		} catch (Exception e) {
			GWT.log("Error creating client process : " + e.getMessage());
		}
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

	public void init() {
		super.init();
	}

}
