package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsurancePolicyTasksOperationsToolbar extends
BigBangOperationsToolBar {

	protected MenuItem validate;
	private MenuItem goToProcess;

	protected InsurancePolicyTasksOperationsToolbar(){
		hideAll();

		validate = new MenuItem("Validar", new Command() {

			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(validate);
		goToProcess = new MenuItem("Navegar para Apólice", new Command() {

			@Override
			public void execute() {
				onGoToProcess();
			}
		});
		addItem(goToProcess);
	}

	protected abstract void onGoToProcess();

@Override
public void onEditRequest() {
	return;
}

@Override
public void onSaveRequest() {
	return;
}

@Override
public void onCancelRequest() {
	return;
}

public abstract void onValidate();

public void allowValidate(boolean allow){
	validate.setVisible(allow);
}

public void setGoToProcessVisible() {
goToProcess.setVisible(true);	
}

}
