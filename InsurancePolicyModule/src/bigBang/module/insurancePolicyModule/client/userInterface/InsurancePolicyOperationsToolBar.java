package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.OperationsToolBar;

public class InsurancePolicyOperationsToolBar extends OperationsToolBar {

	public InsurancePolicyOperationsToolBar(){
		addItem("Operações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		addSeparator();
		
		MenuBar executeSubMenu = new MenuBar(true);
		MenuItem executeMenuItem = new MenuItem("Executar", executeSubMenu);
		executeSubMenu.addItem("Pedir Informação/Documento ao Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Pedir Informação/Documento á Seguradora", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Emitir Nota de Crédito", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		executeSubMenu.addItem("Executar Cálculos Detalhados", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		this.addItem(executeMenuItem);

		MenuBar newSubMenu = new MenuBar(true);
		MenuItem newItem = new MenuItem("Criar", newSubMenu);
		newSubMenu.addItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Apólice de Substituição", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Apólice Adesão", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Despesa de Saúde", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Recibo", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Negociação anual", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Apólice Adesão", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		newSubMenu.addItem("Processos de Gestão de Informação", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		this.addItem(newItem);

		MenuBar insertSubMenu = new MenuBar(true);
		MenuItem insertMenuItem = new MenuItem("Inserir", insertSubMenu);
		insertSubMenu.addItem("Objecto Seguro", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		insertSubMenu.addItem("Objecto Seguro do Cliente", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});

		this.addItem(insertMenuItem);
		
		MenuBar policySubMenu = new MenuBar(true);
		MenuItem policyMenuItem = new MenuItem("Outras", policySubMenu);
		policySubMenu.addItem("Desfazer Alterações", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		policySubMenu.addItem("Anular", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		policySubMenu.addItem("Eliminar", new Command() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub

			}
		});
		
		addItem(policyMenuItem);
	}
}
