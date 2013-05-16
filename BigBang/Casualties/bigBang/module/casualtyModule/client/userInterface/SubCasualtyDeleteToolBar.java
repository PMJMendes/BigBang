package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubCasualtyDeleteToolBar extends BigBangOperationsToolBar {

	protected MenuItem deleteItem;
	protected MenuItem cancelItem;
	
	public SubCasualtyDeleteToolBar(){
		hideAll();
		
		deleteItem = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(deleteItem);
		
		addSeparator();
		
		cancelItem = new MenuItem("Cancelar", new Command() {
			
			@Override
			public void execute() {
				onCancelRequest();
			}
		});
		addItem(cancelItem);
	}
	
	@Override
	public void onEditRequest() {
		return;
	}

	@Override
	public void onSaveRequest() {
		return;
	}

	public abstract void onDelete();
	
}
