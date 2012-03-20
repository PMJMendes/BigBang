package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class QuoteRequestProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem createNegotiation;
	
	//DATA
	protected MenuItem includeObject, createObject;
	
	//EXECUTE
	protected MenuItem sendReplyToClient;
	
	//REQUESTS
	protected MenuItem clientInfoRequest;
	
	//ADMIN
	protected MenuItem close, delete;
	
	public QuoteRequestProcessToolBar(){

		//CREATE
		createNegotiation = new MenuItem("Negociação", new Command() {

			@Override
			public void execute() {
				onCreateNegotiation();
			}
		});
		addItem(SUB_MENU.CREATE, createNegotiation);

		//DATA
		includeObject =  new MenuItem("Inserir Unidade de Risco", new Command() {

			@Override
			public void execute() {
				onIncludeInsuredObject();
			}
		});
		addItem(SUB_MENU.DATA, includeObject);
		
		createObject =  new MenuItem("Criar Objecto Seguro", new Command() {

			@Override
			public void execute() {
				onCreateInsuredObject();
			}
		});
		addItem(SUB_MENU.DATA, createObject);

		//EXECUTE
		sendReplyToClient = new MenuItem("Enviar Resposta ao Cliente", new Command() {

			@Override
			public void execute() {
				onSendResponseToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendReplyToClient);

		//REQUESTS
		clientInfoRequest =  new MenuItem("Pedir Informação ou Documento", new Command() {

			@Override
			public void execute() {
				onInfoOrDocumentRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, clientInfoRequest);

		//ADMIN
		close =  new MenuItem("Fechar Processo", new Command() {

			@Override
			public void execute() {
				onCloseProcess();
			}
		});
		addItem(SUB_MENU.ADMIN, close);

		delete =  new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, delete);	
	}

	public abstract void onCreateNegotiation();
	
	public abstract void onIncludeInsuredObject();
	
	public abstract void onCreateInsuredObject();
	
	public abstract void onSendResponseToClient();
	
	public abstract void onInfoOrDocumentRequest();
	
	public abstract void onCloseProcess();
	
	public abstract void onDelete();
	
	public void allowEdit(boolean allow){
		this.setEditionAvailable(allow);
	}
	
	public void allowDelete(boolean allow) {
		this.delete.setEnabled(allow);
	}
}
