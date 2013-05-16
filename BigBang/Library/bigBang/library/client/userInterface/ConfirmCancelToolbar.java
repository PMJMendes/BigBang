package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;


public abstract class ConfirmCancelToolbar extends BigBangOperationsToolBar {

	protected MenuItem confirm, cancel;
	
	public ConfirmCancelToolbar(){
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

	@Override
	public abstract void onCancelRequest();
	
	public abstract void onConfirm();

}
