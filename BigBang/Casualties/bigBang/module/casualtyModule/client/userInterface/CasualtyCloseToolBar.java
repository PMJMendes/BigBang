package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CasualtyCloseToolBar extends BigBangOperationsToolBar {

	protected MenuItem closeItem;
	protected MenuItem cancelItem;
	
	public CasualtyCloseToolBar(){
		hideAll();
		
		closeItem = new MenuItem("Encerrar", new Command() {
			
			@Override
			public void execute() {
				onClose();
			}
		});
		addItem(closeItem);
		
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

	public abstract void onClose();
	
}
