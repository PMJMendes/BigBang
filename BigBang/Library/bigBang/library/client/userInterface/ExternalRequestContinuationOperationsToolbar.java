package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ExternalRequestContinuationOperationsToolbar extends
		BigBangOperationsToolBar {

	protected MenuItem confirm, cancel;
	
	public ExternalRequestContinuationOperationsToolbar(){
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

	protected abstract void onConfirm();
	
}
