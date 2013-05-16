package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class TransferToPolicyOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem confirmTransfer, cancelTransfer;
	
	public TransferToPolicyOperationsToolbar(){
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
