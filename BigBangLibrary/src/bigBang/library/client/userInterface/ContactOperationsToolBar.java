package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class ContactOperationsToolBar extends BigBangOperationsToolBar{

	private MenuItem deleteItem;

	public ContactOperationsToolBar(){
		
		hideAll();
		this.deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		
		addItem(deleteItem);
		showItem(BigBangOperationsToolBar.SUB_MENU.EDIT, true);
		showItem(SUB_MENU.CREATE, true);
		addItem(SUB_MENU.CREATE, new MenuItem("Sub Contacto", new Command() {

			@Override
			public void execute() {
				onCreateSubContact();
			}
		}));
	}
	
	
	public void allowEdit(boolean b){
		
		createMenuItem.setEnabled(b);
		deleteItem.setEnabled(b);
		
	}
	
	public abstract void onCreateSubContact();

	public abstract void onDelete();
	
	
}
