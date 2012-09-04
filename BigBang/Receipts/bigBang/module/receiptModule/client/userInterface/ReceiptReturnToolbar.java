package bigBang.module.receiptModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ReceiptReturnToolbar extends BigBangOperationsToolBar{
	
	protected MenuItem confirm, cancel;

	public ReceiptReturnToolbar(){
		hideAll();
		
		confirm = new MenuItem("Marcar para devolução", new Command() {
			
			@Override
			public void execute() {
				onSetForReturn();
			}
		});
		addItem(confirm);
		addSeparator();
		
		cancel = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancel);
	}

	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}
	
	public abstract void onSetForReturn();
}
