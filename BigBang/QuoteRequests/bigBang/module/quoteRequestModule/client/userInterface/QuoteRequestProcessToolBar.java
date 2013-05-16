package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class QuoteRequestProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem createNegotiation;

	//DATA
	protected MenuItem createManagerTransfer;

	//EXECUTE 
	protected MenuItem sendReplyToClient;

	//REQUESTS
	protected MenuItem sendMessage;

	//ADMIN
	protected MenuItem close, delete;

	private MenuItem receiveMessage;

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
		
		//EXECUTE
		sendReplyToClient = new MenuItem("Enviar Resposta ao Cliente", new Command() {

			@Override
			public void execute() {
				onSendResponseToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendReplyToClient);

		//REQUESTS
		sendMessage =  new MenuItem("Enviar Mensagem", new Command() {

			@Override
			public void execute() {
				onSendMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, sendMessage);
		
		receiveMessage =  new MenuItem("Receber Mensagem", new Command() {

			@Override
			public void execute() {
				onReceiveMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, receiveMessage);


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

	public abstract void onReceiveMessage();

	public abstract void onCreateNegotiation();

	public abstract void onCreateManagerTransfer();
	
	public abstract void onSendResponseToClient();

	public abstract void onSendMessage();

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

	public void allowSendMessage(boolean allow) {
		this.sendMessage.setEnabled(allow);
	}

	public void allowReceiveMessage(boolean hasPermission) {
		this.receiveMessage.setEnabled(hasPermission);
	}

	public void allowCreateNegotiation(boolean hasPermission) {
		this.createNegotiation.setEnabled(hasPermission);
	}

	public void allowManagerTransfer(boolean hasPermission) {
		this.createManagerTransfer.setEnabled(hasPermission);
	}

}
