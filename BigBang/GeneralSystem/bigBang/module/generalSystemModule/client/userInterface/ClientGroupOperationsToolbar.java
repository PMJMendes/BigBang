package bigBang.module.generalSystemModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ClientGroupOperationsToolbar extends BigBangOperationsToolBar {

	private MenuItem deleteItem;

	public ClientGroupOperationsToolbar(){
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
