package bigBang.library.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class DocumentOperationsToolBar extends BigBangOperationsToolBar{

	private MenuItem delete;

	public DocumentOperationsToolBar(){
		super();
		delete = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDeleteRequest();
			}

		});

		this.addItem(SUB_MENU.ADMIN, delete);
		this.hideAll();
		this.showItem(SUB_MENU.EDIT, true);
		this.adminSubMenu.setVisible(true);
		this.showItem(SUB_MENU.ADMIN, true);
		this.setHeight("21px");
		this.setWidth("100%");
		this.adminSubMenu.getElement().getStyle().setZIndex(12000);

	}

	@Override
	public abstract void onEditRequest();
	@Override
	public abstract void onSaveRequest();
	@Override
	public abstract void onCancelRequest();
	
	public abstract void onDeleteRequest();
	
}
