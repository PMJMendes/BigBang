package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class ClientProcessToolBar extends OperationsToolBar {
	public ClientProcessToolBar(){
		addItem("Operações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		addSeparator();

		MenuBar newSubMenu = new MenuBar(true);
		MenuItem newMenuItem = new MenuItem("Criar", newSubMenu);
		newSubMenu.addItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Apólice", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Consulta de Mercado", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Sinistro", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Executar", executeSubMenu);
		executeSubMenu.addItem("Pedir Informação/Documento", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Fundir com Outro Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(executeMenuItem);
		
		MenuBar clientSubMenu = new MenuBar(true);
		MenuItem clientMenuItem = new MenuItem("Outras", clientSubMenu);
		clientSubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(clientMenuItem);
	}
}
