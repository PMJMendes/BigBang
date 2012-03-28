package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ChoiceFromListToolbar extends BigBangOperationsToolBar{


	protected MenuItem confirm, cancel;

	public ChoiceFromListToolbar(){
		hideAll();

		confirm = new MenuItem("Confirmar", new Command() {

			@Override
			public void execute() {
				onConfirmChoice();
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

	public abstract void onConfirmChoice();


	public void setEnabled(boolean b) {
		confirm.setEnabled(b);
		cancel.setEnabled(b);
		
	}


	public void enableConfirm(boolean b) {
		confirm.setEnabled(b);
		
	}
}

