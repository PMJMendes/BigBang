package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class CancelInfoOrDocumentRequestToolbar extends
		BigBangOperationsToolBar {

	private MenuItem confirm, cancel;
	
	public CancelInfoOrDocumentRequestToolbar(){
		hideAll();
		confirm = new MenuItem("Confirmar", new Command() {
			
			@Override
			public void execute() {
				onConfirm();
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

	public abstract void onConfirm();

}
