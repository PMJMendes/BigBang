package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CreateInsurancePolicyToolbar extends BigBangOperationsToolBar {

	protected MenuItem confirm, cancel;
	
	public CreateInsurancePolicyToolbar(){
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
