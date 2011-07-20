package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class ClientProcessToolBar extends OperationsToolBar {
	public ClientProcessToolBar(){
		MenuBar clientSubMenu = new MenuBar(true);
		MenuItem clientMenuItem = new MenuItem("Apólice", clientSubMenu);
		clientSubMenu.addItem("Desfazer Alterações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		clientSubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(clientMenuItem);
		
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
		
		addItem(executeMenuItem);
	}
}
