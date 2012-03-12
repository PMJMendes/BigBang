package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsurancePolicyTransferToClientToolbar extends BigBangOperationsToolBar {

	private MenuItem transfer;
	
	public InsurancePolicyTransferToClientToolbar(){
		transfer = new MenuItem("Transferir Apólice", new Command() {
			
			@Override
			public void execute() {
				onTransferToClientRequest();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(transfer);
		addSeparator();
		addItem(cancelItem);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public abstract void onCancelRequest();
	
	public abstract void onTransferToClientRequest();

	public void allowTransferToClient(boolean allow) {
		transfer.setEnabled(allow);
	}
	
}
