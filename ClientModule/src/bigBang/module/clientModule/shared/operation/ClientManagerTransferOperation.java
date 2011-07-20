package bigBang.module.clientModule.shared.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.resources.Resources;
import bigBang.module.clientModule.client.userInterface.presenter.ClientManagerTransferOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.view.ClientManagerTransferOperationView;

public class ClientManagerTransferOperation implements Operation {

	public static final String OWNER_PROCESS_ID = ""; //ClientProcess.ID;
	public static final String ID = "clientManagerTransferOperation";
	private final String DESCRIPTION = "A operação de transferência do gestor de clientes";
	private final String SHORT_DESCRIPTION = "Transf. Gestor";
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
		return AbstractImagePrototype.create(r.clientManagerIcon());
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getShortDescription() {
		return SHORT_DESCRIPTION;
	}

	public ViewPresenter getPresenter() {
		if(this.presenter == null){
			this.presenter = (ViewPresenter) new ClientManagerTransferOperationViewPresenter(null, null, this.getView());
			((ClientManagerTransferOperationViewPresenter)this.presenter).setOperation(this);
		}
		return this.presenter;
	}

	public View getView() {
		if(this.view == null)
			this.view = new ClientManagerTransferOperationView();
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
