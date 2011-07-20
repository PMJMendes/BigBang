package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class QuoteRequestProcessToolBar extends OperationsToolBar {

	public QuoteRequestProcessToolBar(){
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
		newSubMenu.addItem("Negociação", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(newMenuItem);
		
		MenuBar insertSubMenu = new MenuBar(true);
		MenuItem insertMenuItem = new MenuItem("Inserir", insertSubMenu);
		insertSubMenu.addItem("Objecto Seguro do Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(insertMenuItem);
		
		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Executar", executeSubMenu);
		executeSubMenu.addItem("Enviar Resposta ao Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Pedir Informação/Documento ao Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(executeMenuItem);
		
		MenuBar quoteRequestSubMenu = new MenuBar(true);
		MenuItem quoteRequestMenuItem = new MenuItem("Outras", quoteRequestSubMenu);
		quoteRequestSubMenu.addItem("Desfazer Alterações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		quoteRequestSubMenu.addItem("Fechar Processo", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		quoteRequestSubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(quoteRequestMenuItem);
	}
	
}
