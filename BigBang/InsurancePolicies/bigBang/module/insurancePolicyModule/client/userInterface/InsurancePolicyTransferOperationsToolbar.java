package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsurancePolicyTransferOperationsToolbar extends
		BigBangOperationsToolBar {

	private MenuItem transferItem;
	private MenuItem cancelItem;
	
	public InsurancePolicyTransferOperationsToolbar(){
		hideAll();
		this.transferItem = new MenuItem("Transferir Apólice", new Command() {

			@Override
			public void execute() {
				onTransfer();
			}
		});
		addItem(this.transferItem);
		addSeparator();
		this.cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(this.cancelItem);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public void allowTransfer(boolean allow) {
		this.transferItem.setEnabled(allow);
	}
	
	public abstract void onTransfer();
	
}
