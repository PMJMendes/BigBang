package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class UserOperationsToolbar extends BigBangOperationsToolBar {

	private MenuItem deleteItem;
	
	public UserOperationsToolbar(){
		this.deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, this.deleteItem);
		
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.ADMIN, true);
	}

	public abstract void onDelete();
	
	public void allowDelete(boolean allow){
		this.deleteItem.setEnabled(allow);
	}

}
