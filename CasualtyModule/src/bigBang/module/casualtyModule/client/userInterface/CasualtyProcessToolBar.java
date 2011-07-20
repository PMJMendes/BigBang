package bigBang.module.casualtyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class CasualtyProcessToolBar extends OperationsToolBar {
	
	public CasualtyProcessToolBar(){
		addItem("Operações", new Command() {
			
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				
			}
		});
		addSeparator();
		
		MenuBar newSubMenu = new MenuBar(true);
		MenuItem newMenuItem = new MenuItem("Criar", newSubMenu);
		newSubMenu.addItem("Sub-Sinistro", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(newMenuItem);
		
		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Sinistro", executeSubMenu);
		executeSubMenu.addItem("Pedir Informação ou Documento ao Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(executeMenuItem);
		
		MenuBar casualtySubMenu = new MenuBar(true);
		MenuItem policyMenuItem = new MenuItem("Outras", casualtySubMenu);
		casualtySubMenu.addItem("Encerrar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		casualtySubMenu.addItem("Reabrir", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		casualtySubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(policyMenuItem);
	}

}
