package bigBang.library.client.userInterface;

import org.gwt.mosaic.ui.client.MessageBox;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Implements an abstract class for the process toolbars in the BigBang project.
 * 
 * @author Francisco Cabrita @ Premium Minds Lda.
 */
public abstract class BigBangOperationsToolBar extends OperationsToolBar {

	public static enum SUB_MENU {
		CREATE,
		EXECUTE,
		DATA,
		ADMIN,
		EDIT
	}

	protected MenuBar createSubMenu;
	protected MenuBar executeSubMenu;
	protected MenuBar dataSubMenu;
	protected MenuBar adminSubMenu;

	protected MenuItem editCancelMenuItem;
	protected MenuItem saveMenuItem;
	protected MenuItem createMenuItem;
	protected MenuItem executeMenuItem;
	protected MenuItem dataMenuItem;
	protected MenuItem adminMenuItem;

	protected boolean saveModeEnabled;
	protected boolean confirmOnCancel = true;

	protected final String EDIT_TEXT = "Editar";
	protected final String SAVE_TEXT = "Guardar";
	protected final String CANCEL_TEXT = "Cancelar";

	/**
	 * The constructor
	 */
	public BigBangOperationsToolBar(){
		this.createSubMenu = new MenuBar(true);
		this.executeSubMenu = new MenuBar(true);
		this.dataSubMenu = new MenuBar(true);
		this.adminSubMenu = new MenuBar(true);

		createMenuItem = new BigBangMenuItem("Criar", this.createSubMenu);
		executeMenuItem = new BigBangMenuItem("Executar", this.executeSubMenu);
		dataMenuItem = new BigBangMenuItem("Dados", this.dataSubMenu);
		adminMenuItem = new BigBangMenuItem("Admin", this.adminSubMenu);

		editCancelMenuItem = new BigBangMenuItem("", new Command() {

			@Override
			public void execute() {
				if(BigBangOperationsToolBar.this.saveModeEnabled){
					if(confirmOnCancel){
						MessageBox.confirm("Cancelar alterações", "Tem certeza que pretende cancelar as alterações realizadas?", new MessageBox.ConfirmationCallback() {

							@Override
							public void onResult(boolean result) {
								if(result){
									setSaveModeEnabled(false);
									onCancelRequest();
								}
							}
						});
					}else{
						setSaveModeEnabled(false);
						onCancelRequest();
					}
				}else{
					setSaveModeEnabled(true);
					onEditRequest();
				}
			}
		});
		editCancelMenuItem.setWidth("60px");
		editCancelMenuItem.getElement().getStyle().setProperty("textAlign", "center");

		saveMenuItem = new BigBangMenuItem(SAVE_TEXT,new Command() {

			@Override
			public void execute() {
				onSaveRequest();
			}
		}); 

		this.addItem(this.editCancelMenuItem);
		this.addItem(this.saveMenuItem);
		this.addSeparator();
		this.addItem(this.createMenuItem);
		this.addItem(this.executeMenuItem);
		this.addItem(this.dataMenuItem);
		this.addItem(this.adminMenuItem);

		showAll();
		this.setSaveModeEnabled(false);
	}

	/**
	 * Adds a menu item to a submenu identified in the argument
	 * @param submenu The submenu identification
	 * @param item The item to add
	 */
	public void addItem(SUB_MENU submenu, MenuItem item) {
		switch(submenu){
		case CREATE:
			this.createSubMenu.addItem(item);
			break;
		case EXECUTE:
			this.executeSubMenu.addItem(item);
			break;
		case DATA:
			this.dataSubMenu.addItem(item);
			break;
		case ADMIN:
			this.adminSubMenu.addItem(item);
			break;
		default:
			throw new RuntimeException("The submenu reference was not recognized.");
		}
	}

	/**
	 * Sets the menu bar to show the save item.
	 * @param enabled If true, the save item will be shown
	 */
	public void setSaveModeEnabled(boolean enabled) {
		this.saveModeEnabled = enabled;
		this.editCancelMenuItem.setText(enabled ? CANCEL_TEXT : EDIT_TEXT);
		this.saveMenuItem.setEnabled(enabled);
	}

	/**
	 * Allows the edit command to be called.
	 * @param available If true, the edit mode will be available, otherwise it won't.
	 */
	public void setEditionAvailable(boolean available){
		if(!available)
			setSaveModeEnabled(false);
		for(MenuItem i : getItems()){
			if(i == this.saveMenuItem){
				if(!available)
					this.saveMenuItem.setEnabled(false);
			}else{
				i.setEnabled(available);
			}
		}
	}

	/**
	 * Returns whether or not the save mode is enabled
	 * @return true if the save mode is enabled, false otherwise
	 */
	public boolean isSaveModeEnabled(){
		return this.saveModeEnabled;
	}

	/**
	 * Shows all the menu items
	 */
	public void showAll(){
		this.editCancelMenuItem.setVisible(true);
		this.saveMenuItem.setVisible(true);
		this.createMenuItem.setVisible(true);
		this.executeMenuItem.setVisible(true);
		this.dataMenuItem.setVisible(true);
		this.adminMenuItem.setVisible(true);
	}

	/**
	 * Hides all the menu items
	 */
	public void hideAll(){
		this.editCancelMenuItem.setVisible(false);
		this.saveMenuItem.setVisible(false);
		this.createMenuItem.setVisible(false);
		this.executeMenuItem.setVisible(false);
		this.dataMenuItem.setVisible(false);
		this.adminMenuItem.setVisible(false);
	}

	/**
	 * Shows or hides a menu item
	 * @param menu The menu item to be shown/hidden
	 * @param show If true the item will be shown, otherwise it will be hidden
	 */
	public void showItem(SUB_MENU menu, boolean show) {
		switch(menu){
		case ADMIN:
			this.adminMenuItem.setVisible(show);
			break;
		case CREATE:
			this.createMenuItem.setVisible(show);
			break;
		case DATA:
			this.adminMenuItem.setVisible(show);
			break;
		case EDIT:
			this.saveMenuItem.setVisible(show);
			this.editCancelMenuItem.setVisible(show);
			break;
		case EXECUTE:
			this.executeMenuItem.setVisible(show);
			break;
		default:
			throw new RuntimeException("The requested menu item was not found");
		}
	}

	public void showConfirmOnCancel(boolean confirm) {
		this.confirmOnCancel = confirm;
	}

	/**
	 * Invoked when the edit button is pressed by the user.
	 */
	public abstract void onEditRequest();

	/**
	 * Invoked when the save button is pressed by the user.
	 */
	public abstract void onSaveRequest();

	/**
	 * Invoked when the cancel button is pressed by the user
	 */
	public abstract void onCancelRequest();

}
