package bigBang.module.clientModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ClientProcessToolBar extends BigBangOperationsToolBar {
	
	//CREATE
	private MenuItem createPolicyItem;
	private MenuItem riskAnalysisItem;
	private MenuItem quoteRequestItem;
	private MenuItem casualtyItem;
	
	//EXECUTE
	private MenuItem clientMergeItem;
	
	//DATA
	private MenuItem managerTransferItem;
	
	//REQUESTS
	private MenuItem infoRequestItem;
	
	//ADMIN
	protected MenuItem deleteItem;
	
	//OTHER
	
	public ClientProcessToolBar(){

		//CREATE
		createPolicyItem =  new MenuItem("Apólice", new Command() {

			@Override
			public void execute() {
				onCreatePolicy();
			}
		});
		addItem(SUB_MENU.CREATE, createPolicyItem);
		
		riskAnalysisItem = new MenuItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				onCreateRiskAnalisys();
			}
		});
		addItem(SUB_MENU.CREATE, riskAnalysisItem);
		
		quoteRequestItem = new MenuItem("Consulta de Mercado", new Command() {

			@Override
			public void execute() {
				onCreateQuoteRequest();
			}
		});
		addItem(SUB_MENU.CREATE,quoteRequestItem);
		
		casualtyItem = new MenuItem("Sinistro", new Command() {

			@Override
			public void execute() {
				onCreateCasualty();
			}
		});
		addItem(SUB_MENU.CREATE, casualtyItem);


		//EXECUTE
		clientMergeItem = new MenuItem("Fundir com Cliente", new Command() {

			@Override
			public void execute() {
				onMergeWithClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, clientMergeItem);
		
		//DATA
		managerTransferItem = new MenuItem("Criar Transferência de Gestor", new Command() {

			@Override
			public void execute() {
				onTransferToManager();
			}
		});
		addItem(SUB_MENU.DATA, managerTransferItem);
		
		//REQUESTS
		infoRequestItem = new MenuItem("Pedir Informação ou Documento", new Command() {

			@Override
			public void execute() {
				onRequestInfoOrDocument();
			}
		});
		addItem(SUB_MENU.REQUESTS, infoRequestItem);
		
		adminSubMenu.addSeparator();
		
		this.deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, this.deleteItem);
		
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
	public abstract void onTransferToManager();
	
	//ADMIN
	public abstract void onDelete();

	public void allowDelete(boolean allow){
		this.deleteItem.setEnabled(allow);
	}
	
	public void allowInfoOrDocumentRequest(boolean allow) {
		this.infoRequestItem.setEnabled(allow);
	}

	public void allowManagerTransfer(boolean allow) {
		this.managerTransferItem.setEnabled(allow);
	}

	public void allowClientMerge(boolean allow) {
		this.clientMergeItem.setEnabled(allow);
	}

	public void allowCreatePolicy(boolean allow) {
		this.createPolicyItem.setEnabled(allow);
	}

	public void allowCreateRiskAnalysis(boolean allow) {
		this.riskAnalysisItem.setEnabled(allow);
	}

	public void allowCreateQuoteRequest(boolean allow) {
		this.quoteRequestItem.setEnabled(allow);
	}

	public void allowCreateCasualty(boolean allow) {
		this.casualtyItem.setEnabled(allow);
	}
}
