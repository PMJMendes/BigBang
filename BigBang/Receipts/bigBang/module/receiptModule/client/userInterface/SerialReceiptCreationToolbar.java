package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SerialReceiptCreationToolbar extends BigBangOperationsToolBar{


	protected MenuItem confirm, cancel;

	public SerialReceiptCreationToolbar(){
		hideAll();

		confirm = new MenuItem("Guardar", new Command() {

			@Override
			public void execute() {
				saveReceipt();
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

	public abstract void saveReceipt();


	public void setEnabled(boolean b) {
		confirm.setEnabled(false);
		cancel.setEnabled(false);
		
	}
}

