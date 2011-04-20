package bigBang.module.generalSystemModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.module.generalSystemModule.client.resources.Resources;
import bigBang.module.generalSystemModule.shared.ModuleConstants;

public class TaxManagementOperation implements Operation {


	public static final String ID = "TAX_MANAGEMENT_OPERATION";
	public static final String DESCRIPTION = "Gestão de Impostos e coeficientes";
	public static final String SHORT_DESCRIPTION = "Impostos e coef.";
	public static final String OWNER_PROCESS_ID = ModuleConstants.ProcessTypeIDs.GENERAL_SYSTEM;
	
	private boolean permission;
	
	public void init() {
		
	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.taxesIcon());
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
