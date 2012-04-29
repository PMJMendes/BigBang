package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.OperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class QuoteRequestProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem createNegotiation;

	//DATA
	protected MenuItem createManagerTransfer, includeObject, createObject, createPersonObject, createCompanyObject, createEquipmentObject, createLocationObject, createAnimalObject;

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
		createManagerTransfer = new MenuItem("Criar Transferência de Gestor", new Command() {
			
			@Override
			public void execute() {
				onCreateManagerTransfer();
			}
		});
		addItem(SUB_MENU.DATA, createManagerTransfer);
		
		includeObject =  new MenuItem("Inserir Unidade de Risco", new Command() {

			@Override
			public void execute() {
				onIncludeInsuredObject();
			}
		});
		addItem(SUB_MENU.DATA, includeObject);

		OperationsToolBar createObjectSubMenu = new OperationsToolBar(true);
		createObject =  new MenuItem("Criar Unidade de Risco", createObjectSubMenu);
		addItem(SUB_MENU.DATA, createObject);

		createPersonObject = new MenuItem("Pessoa", new Command() {

			@Override
			public void execute() {
				onCreatePersonObject();
			}
		});
		createObjectSubMenu.addItem(createPersonObject);

		createCompanyObject = new MenuItem("Empresa ou Grupo", new Command() {

			@Override
			public void execute() {
				onCreateCompanyObject();
			}
		});
		createObjectSubMenu.addItem(createCompanyObject);

		createEquipmentObject = new MenuItem("Objecto ou Equipamento", new Command() {

			@Override
			public void execute() {
				onCreateEquipmentObject();
			}
		});
		createObjectSubMenu.addItem(createEquipmentObject);

		createLocationObject = new MenuItem("Local", new Command() {

			@Override
			public void execute() {
				onCreateLocationObject();
			}
		});
		createObjectSubMenu.addItem(createLocationObject);

		createAnimalObject = new MenuItem("Animal", new Command() {

			@Override
			public void execute() {
				onCreateAnimalObject();
			}
		});
		createObjectSubMenu.addItem(createAnimalObject);


		//EXECUTE
		sendReplyToClient = new MenuItem("Enviar Resposta ao Cliente", new Command() {

			@Override
			public void execute() {
				onSendResponseToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendReplyToClient);

		//REQUESTS
		clientInfoRequest =  new MenuItem("Criar Pedido de Informação ou Documento", new Command() {

			@Override
			public void execute() {
				onInfoOrDocumentRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, clientInfoRequest);

		//ADMIN
		close =  new MenuItem("Encerrar Processo", new Command() {

			@Override
			public void execute() {
				onCloseProcess();
			}
		});
		addItem(SUB_MENU.ADMIN, close);

		adminSubMenu.addSeparator();
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

	public abstract void onCreateManagerTransfer();
	
	public abstract void onCreateInsuredObject();
	
	public abstract void onCreatePersonObject();
	
	public abstract void onCreateCompanyObject();
	
	public abstract void onCreateEquipmentObject();
	
	public abstract void onCreateLocationObject();
	
	public abstract void onCreateAnimalObject();

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
	
	public void allowClose(boolean allow) {
		this.close.setEnabled(allow);
	}
	
	public void allowCreateManagerTransfer(boolean allow) {
		this.createManagerTransfer.setEnabled(allow);
	}

	public void allowCreateInsuredObject(boolean allow) {
		this.createObject.setEnabled(allow);
		this.createPersonObject.setEnabled(allow);
		this.createCompanyObject.setEnabled(allow);
		this.createEquipmentObject.setEnabled(allow);
		this.createLocationObject.setEnabled(allow);
		this.createLocationObject.setEnabled(allow);
		this.createAnimalObject.setEnabled(allow);
	}

	public void allowCreateInfoRequest(boolean allow) {
		this.clientInfoRequest.setEnabled(allow);
	}

}
