package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class MedicalFileTasksToolbar extends BigBangOperationsToolBar{

	private MenuItem goToProcess;

	public MedicalFileTasksToolbar() {
		hideAll();
		
		showItem(SUB_MENU.EDIT, true);
		
		goToProcess = new MenuItem("Navegar para Processo Auxiliar", new Command(){

			@Override
			public void execute() {
				onNavigateToAuxiliaryProcess();
			}

		});

		addItem(goToProcess);
	}

	protected abstract void onNavigateToAuxiliaryProcess();

	public void allowEdit(boolean b) {
		editCancelMenuItem.setEnabled(b);
	}
}
