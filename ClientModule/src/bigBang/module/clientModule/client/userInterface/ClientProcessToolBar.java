package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public class ClientProcessToolBar extends BigBangOperationsToolBar {
	public ClientProcessToolBar(){
		addItem(SUB_MENU.CREATE, new MenuItem("Apólice", new Command() {
			
			@Override
			public void execute() {
				
			}
		}));
	}

	@Override
	public void onEditRequest() {
		// TODO Auto-generated method stub
		setSaveModeEnabled(true);
	}

	@Override
	public void onSaveRequest() {
		// TODO Auto-generated method stub
		setSaveModeEnabled(false);
	}

	@Override
	public void onCancelRequest() {
		// TODO Auto-generated method stub
	}
}
