package bigBang.library.client.userInterface;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ManagerTransferToolBar extends BigBangOperationsToolBar {

	protected final String ACCEPT_TEXT = "Receber Processos";
	protected final String CANCEL_TEXT = "Cancelar Transferência";
	public MenuItem accept;
	public MenuItem cancel;

	public ManagerTransferToolBar() {
		hideAll();
		
		accept = new MenuItem(ACCEPT_TEXT, new Command() {
			
			@Override
			public void execute() {
				onAccept();
			}
		});
		addItem(accept);
		
		cancel = new MenuItem(CANCEL_TEXT, new Command() {
			
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

	public abstract void onAccept();
	
	public void allowAccept(boolean allow) {
		this.accept.setVisible(allow);
	}
	
	public void allowCancel(boolean allow) {
		this.cancel.setVisible(allow);
	}
	
}
