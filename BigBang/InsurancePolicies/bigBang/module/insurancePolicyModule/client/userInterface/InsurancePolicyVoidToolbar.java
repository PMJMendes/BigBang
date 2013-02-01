package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsurancePolicyVoidToolbar extends BigBangOperationsToolBar {

	public InsurancePolicyVoidToolbar(){
		MenuItem voidItem = new MenuItem("Confirmar Anulação", new Command() {
			
			@Override
			public void execute() {
				onVoidRequest();
			}
		});
		MenuItem cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		hideAll();
		addItem(voidItem);
		addSeparator();
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

	public abstract void onCancelRequest();
	
	public abstract void onVoidRequest();
	
}
