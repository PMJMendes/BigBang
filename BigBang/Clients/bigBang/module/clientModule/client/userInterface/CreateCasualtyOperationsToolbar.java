package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class CreateCasualtyOperationsToolbar extends BigBangOperationsToolBar {

	public CreateCasualtyOperationsToolbar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

}
