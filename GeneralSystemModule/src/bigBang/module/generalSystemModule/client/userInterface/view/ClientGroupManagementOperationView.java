package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;

public class ClientGroupManagementOperationView extends View implements ClientGroupManagementOperationViewPresenter.Display{

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected ClientGroupFormView groupForm;
	
	public ClientGroupManagementOperationView(){
		
	}
	
}
