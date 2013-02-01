package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyRejectCloseToolbar extends BigBangOperationsToolBar {

	protected MenuItem reject, cancel;
	
	public SubCasualtyRejectCloseToolbar(){
		hideAll();
		
		reject = new MenuItem("Rejeitar Encerramento", new Command() {
			
			@Override
			public void execute() {
				onReject();
			}
		});
		addItem(reject);
		
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

	public abstract void onReject();
	
}
