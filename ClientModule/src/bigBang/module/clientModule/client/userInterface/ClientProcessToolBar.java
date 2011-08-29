package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ClientProcessToolBar extends BigBangOperationsToolBar {
	public ClientProcessToolBar(){

		//CREATE
		addItem(SUB_MENU.CREATE, new MenuItem("Apólice", new Command() {

			@Override
			public void execute() {
				onCreatePolicy();
			}
		}));
		addItem(SUB_MENU.CREATE, new MenuItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				onCreateRiskAnalisys();
			}
		}));
		addItem(SUB_MENU.CREATE, new MenuItem("Consulta de Mercado", new Command() {

			@Override
			public void execute() {
				onCreateQuoteRequest();
			}
		}));
		addItem(SUB_MENU.CREATE, new MenuItem("Sinistro", new Command() {

			@Override
			public void execute() {
				onCreateCasualty();
			}
		}));

		//EXECUTE
		addItem(SUB_MENU.EXECUTE, new MenuItem("Fundir com Cliente", new Command() {

			@Override
			public void execute() {
				onMergeWithClient();
			}
		}));
		
		//DATA
		addItem(SUB_MENU.DATA, new MenuItem("Pedir Informação ou Documento", new Command() {

			@Override
			public void execute() {
				onRequestInfoOrDocument();
			}
		}));
	}

	//CREATE
	public abstract void onCreatePolicy();
	public abstract void onCreateRiskAnalisys();
	public abstract void onCreateQuoteRequest();
	public abstract void onCreateCasualty();

	//EXECUTE
	public abstract void onMergeWithClient();
	
	//DATA
	public abstract void onRequestInfoOrDocument();
}
