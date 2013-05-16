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
	private MenuItem sendMessage;
	private MenuItem receiveMessage;
	
	//ADMIN
	protected MenuItem deleteItem;
	private MenuItem markAsInternational;
	
	//OTHER
	
	public ClientProcessToolBar(){

		//CREATE
		quoteRequestItem = new MenuItem("Consulta de Mercado", new Command() {

			@Override
			public void execute() {
				onCreateQuoteRequest();
			}
		});
		addItem(SUB_MENU.CREATE,quoteRequestItem);
		
		createPolicyItem =  new MenuItem("Apólice", new Command() {

			@Override
			public void execute() {
				onCreatePolicy();
			}
		});
		addItem(SUB_MENU.CREATE, createPolicyItem);

		casualtyItem = new MenuItem("Sinistro", new Command() {

			@Override
			public void execute() {
				onCreateCasualty();
			}
		});
		addItem(SUB_MENU.CREATE, casualtyItem);
		
		riskAnalysisItem = new MenuItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				onCreateRiskAnalisys();
			}
		});
		addItem(SUB_MENU.CREATE, riskAnalysisItem);
		

		//EXECUTE
		clientMergeItem = new MenuItem("Fundir com Cliente", new Command() {

			@Override
			public void execute() {
				onMergeWithClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, clientMergeItem);
		
		markAsInternational = new MenuItem("Marcar como Estrangeiro", new Command(){

			@Override
			public void execute() {
				onMarkAsInternational();
			}
			
			
		});
		
		addItem(SUB_MENU.DATA, markAsInternational);
		
		//DATA
		managerTransferItem = new MenuItem("Criar Transferência de Gestor", new Command() {

			@Override
			public void execute() {
				onTransferToManager();
			}
		});
		addItem(SUB_MENU.DATA, managerTransferItem);
		
		//REQUESTS
		sendMessage = new MenuItem("Enviar Mensagem", new Command() {

			@Override
			public void execute() {
				onSendMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, sendMessage);
		
		receiveMessage = new MenuItem("Receber Mensagem", new Command() {

			@Override
			public void execute() {
				onReceiveMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, receiveMessage);
		
		//ADMIN
		this.deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, this.deleteItem);
		
	}

	protected abstract void onMarkAsInternational();
	//CREATE
	public abstract void onCreatePolicy();
	public abstract void onCreateRiskAnalisys();
	public abstract void onCreateQuoteRequest();
	public abstract void onCreateCasualty();

	//EXECUTE
	public abstract void onMergeWithClient();
	
	//DATA
	public abstract void onSendMessage();
	public abstract void onReceiveMessage();
	public abstract void onTransferToManager();
	
	//ADMIN
	public abstract void onDelete();

	public void allowDelete(boolean allow){
		this.deleteItem.setEnabled(allow);
	}
	
	public void allowSendMessage(boolean allow) {
		this.sendMessage.setEnabled(allow);
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
	
	public void allowReceiveMessage(boolean allow){
		this.receiveMessage.setEnabled(allow);
	}
	
	public void allowMarkAsInternational(boolean allow){
		this.markAsInternational.setEnabled(allow);
	}
}
