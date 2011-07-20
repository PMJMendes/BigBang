package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class ReceiptProcessToolBar extends OperationsToolBar {

	public ReceiptProcessToolBar(){
		addItem("Operações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		addSeparator();
		
		MenuBar insertSubMenu = new MenuBar(true);
		MenuItem insertMenuItem = new MenuItem("Inserir", insertSubMenu);
		insertSubMenu.addItem("Nota de Débito", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(insertMenuItem);
		
		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Executar", executeSubMenu);
		executeSubMenu.addItem("Cobrar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Colocar para Devolução", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Devolver", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("TODO", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(executeMenuItem);
		
		MenuBar receiptSubMenu = new MenuBar(true);
		MenuItem receiptMenuItem = new MenuItem("Outras", receiptSubMenu);
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
