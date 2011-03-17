package bigBang.module.generalSystemModule.shared;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.shared.Operation;

public class CostCenterManagementOperation implements Operation {

	public static final String ID = "COST_CENTER_MANAGEMENT_OPERATION";
	public static final String DESCRIPTION = "Gestão de centros de custo";
	public static final String SHORT_DESCRIPTION = "Gestão C. custo";
	public static final String OWNER_PROCESS_ID = "PROCESS_GENERAL_SYSTEM";
	
	public void init() {
		
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
