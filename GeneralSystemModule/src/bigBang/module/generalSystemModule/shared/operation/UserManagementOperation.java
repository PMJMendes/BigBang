package bigBang.module.generalSystemModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.module.generalSystemModule.client.resources.Resources;

public class UserManagementOperation implements Operation {


	public static final String ID = "USER_MANAGEMENT_OPERATION";
	public static final String DESCRIPTION = "Gestão de utilizadores";
	public static final String SHORT_DESCRIPTION = "Utilizadores";
	public static final String OWNER_PROCESS_ID = "PROCESS_GENERAL_SYSTEM";
	
	private boolean permission;
	
	public void init() {
		
	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.userIcon());
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
