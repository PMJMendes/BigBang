package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CreateExpenseOperationsToolbar extends BigBangOperationsToolBar{

	public CreateExpenseOperationsToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}
	
}
