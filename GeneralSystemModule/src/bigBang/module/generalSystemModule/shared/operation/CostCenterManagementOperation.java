package bigBang.module.generalSystemModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.module.generalSystemModule.client.resources.Resources;

public class CostCenterManagementOperation implements Operation {

	public static final String ID = "COST_CENTER_MANAGEMENT_OPERATION";
	public static final String DESCRIPTION = "Gestão de centros de custo";
	public static final String SHORT_DESCRIPTION = "Centro de Custo";
	public static final String OWNER_PROCESS_ID = "PROCESS_GENERAL_SYSTEM";
	
	private boolean permission;
	
	public void init() {
		
	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.costCenterIcon());
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
	
	@Override
	public boolean getPermission() {
		return permission;
	}
	
	@Override
	public void setPermission(boolean p) {
		this.permission = p;
	}

}
