package bigBang.module.clientModule.shared.operation;

import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.resources.Resources;
import bigBang.module.clientModule.client.userInterface.presenter.ClientMergeOperationViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ClientMergeOperation implements Operation {

	public static final String OWNER_PROCESS_ID = ""; //ClientProcess.ID;
	public static final String ID = "clientMergeOperation";
	private final String DESCRIPTION = "A operação de fusão de clientes";
	private final String SHORT_DESCRIPTION = "Fusão";
	private boolean permission;
	
	private View view;
	private ViewPresenter presenter;
	
	public void init() {

	}

	public String getId() {
		return ID;
	}

	public AbstractImagePrototype getIcon() {
		Resources r = GWT.create(Resources.class);
		return AbstractImagePrototype.create(r.mergeIcon());
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public ViewPresenter getPresenter() {
		if(this.presenter == null){
			this.presenter = (ViewPresenter) new ClientMergeOperationViewPresenter(null, null, this.getView());
			((ClientMergeOperationViewPresenter)this.presenter).setOperation(this);
		}
		return this.presenter;
	}

	public View getView() {
		return this.view;
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
