package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SignatureRequestOperationsToolbar extends BigBangOperationsToolBar{

	private MenuItem cancelItem, repeatItem, receiveResponseItem;

	public SignatureRequestOperationsToolbar(){
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
				onRepeatSignatureRequestRequest();
			}
		});

		addItem(repeatItem);
		addSeparator();

		cancelItem = new MenuItem("Cancelar Pedido", new Command() {

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

	public abstract void onRepeatSignatureRequestRequest();

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
