package bigBang.module.receiptModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class DASRequestOperationsToolbar extends BigBangOperationsToolBar{

	private MenuItem cancelItem, repeatItem, receiveResponseItem;

	public DASRequestOperationsToolbar(){


		hideAll();

		receiveResponseItem = new MenuItem("Receber Resposta", new Command() {

			@Override
			public void execute() {
				onReceiveResponseRequest();
			}
		});

		addItem(receiveResponseItem);
		addSeparator();

		repeatItem = new MenuItem("Repetir Pedido", new Command() {

			@Override
			public void execute() {
				onRepeatDASRequestRequest();
			}
		});

		addItem(repeatItem);
		addSeparator();

		cancelItem = new MenuItem("Cancelar", new Command() {

			@Override
			public void execute() {
				onCancelRequest();
			}
		});
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

	@Override
	public abstract void onCancelRequest();

	public abstract void onRepeatDASRequestRequest();

	public abstract void onReceiveResponseRequest();

	public void allowCancel(boolean hasPermission) {
		cancelItem.setEnabled(hasPermission);
	}

	public void allowReceiveResponse(boolean hasPermission) {
		receiveResponseItem.setEnabled(hasPermission);

	}

	public void allowRepeatRequest(boolean hasPermission) {
		repeatItem.setEnabled(hasPermission);		
	}

	public void setEnabled(boolean b) {
		allowCancel(b);
		allowRepeatRequest(b);
		allowReceiveResponse(b);
	}
}

