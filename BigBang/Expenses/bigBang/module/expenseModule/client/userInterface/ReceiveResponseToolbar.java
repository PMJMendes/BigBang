package bigBang.module.expenseModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ReceiveResponseToolbar extends BigBangOperationsToolBar{

	public ReceiveResponseToolbar(){
		MenuItem confirmResponse = new MenuItem("Confirmar", new Command() {

			@Override
			public void execute() {
				onConfirmResponse();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(confirmResponse);
		addSeparator();
		addItem(cancelItem);
	}
	
	protected abstract void onConfirmResponse();
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	@Override
	public abstract void onCancelRequest();

}
