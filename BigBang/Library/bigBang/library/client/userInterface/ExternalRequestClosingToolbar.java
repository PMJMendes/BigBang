package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ExternalRequestClosingToolbar extends BigBangOperationsToolBar {

	protected MenuItem close, cancel;
	
	public ExternalRequestClosingToolbar(){
		hideAll();
		
		close = new MenuItem("Encerrar Processo", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(close);
		
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

	public abstract void onClose();
	
}
