package bigBang.module.riskAnalisysModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class RiskAnalisysProcessToolBar extends OperationsToolBar {

	public RiskAnalisysProcessToolBar(){
		addItem("Operações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		addSeparator();
		
		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Executar", executeSubMenu);
		executeSubMenu.addItem("Marcar Visita", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Enviar Relatório Final", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Pedir Informação ou Documento", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		addItem(executeMenuItem);
		
		MenuBar riskAnalisysSubMenu = new MenuBar(true);
		MenuItem riskAnalisysMenuItem = new MenuItem("Outras", riskAnalisysSubMenu);
		riskAnalisysSubMenu.addItem("Encerrar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(riskAnalisysMenuItem);
	}
	
}
