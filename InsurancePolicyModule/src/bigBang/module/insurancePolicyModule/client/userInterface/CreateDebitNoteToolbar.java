package bigBang.module.insurancePolicyModule.client.userInterface;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CreateDebitNoteToolbar extends BigBangOperationsToolBar {

	public CreateDebitNoteToolbar(){
		MenuItem create = new MenuItem("Criar Nota de Débito", new Command() {
			
			@Override
			public void execute() {
				onCreateDebitNote();
			}
		});
		MenuItem cancel = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		
		hideAll();
		addItem(create);
		addSeparator();
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

	public abstract void onCreateDebitNote();

}
