package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NewInsurancePolicyOperationsToolbar extends
		BigBangOperationsToolBar {

	private MenuItem createInsuredObject, createExercise;
	
	public NewInsurancePolicyOperationsToolbar(){
		hideAll();
		
		createInsuredObject = new MenuItem("Criar Unidade de Risco", new Command() {
			
			@Override
			public void execute() {
				onCreateObject();
			}
		});
		addItem(SUB_MENU.DATA, createInsuredObject);
		
		createExercise = new MenuItem("Abrir Exercício", new Command() {
			
			@Override
			public void execute() {
				onCreateExercise();
			}
		});
		addItem(SUB_MENU.DATA, createExercise);
		
		showItem(SUB_MENU.DATA, true);
		showItem(SUB_MENU.EDIT, true);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}
	
	public abstract void onCreateObject();

	public abstract void onCreateExercise();

}
