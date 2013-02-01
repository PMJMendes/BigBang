package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class InsuredObjectOperationsToolbar extends BigBangOperationsToolBar {

	private MenuItem deleteItem;
	
	public InsuredObjectOperationsToolbar(){
		hideAll();
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);
		
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.ADMIN, true);
	}
	
	public void allowDelete(boolean allow) {
		deleteItem.setEnabled(allow);
	}
	

	public void allowEdit(boolean allow) {
		editCancelMenuItem.setEnabled(allow);
	}
	
	public abstract void onDeleteRequest();

}
