package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class ReceiptProcessToolBar extends OperationsToolBar {

	public ReceiptProcessToolBar(){
		MenuBar receiptSubMenu = new MenuBar(true);
		MenuItem receiptMenuItem = new MenuItem("Recibo", receiptSubMenu);
		receiptSubMenu.addItem("Desfazer Alterações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		receiptSubMenu.addItem("Anular", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		receiptSubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(receiptMenuItem);
	}

}
