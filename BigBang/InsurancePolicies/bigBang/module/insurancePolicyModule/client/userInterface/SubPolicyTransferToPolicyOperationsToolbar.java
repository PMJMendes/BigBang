package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubPolicyTransferToPolicyOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem confirmTransfer, cancelTransfer;
	
	public SubPolicyTransferToPolicyOperationsToolbar(){
		hideAll();
		
		confirmTransfer = new MenuItem("Confirmar Transferência", new Command() {
			
			@Override
			public void execute() {
				onTransfer();
			}
		});
		addItem(confirmTransfer);
		addSeparator();
		
		cancelTransfer = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancelTransfer);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public abstract void onTransfer();
	
}
