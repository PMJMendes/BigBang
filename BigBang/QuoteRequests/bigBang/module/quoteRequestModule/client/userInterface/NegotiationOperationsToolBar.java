package bigBang.module.quoteRequestModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class NegotiationOperationsToolBar extends BigBangOperationsToolBar{

	protected MenuItem deleteItem;
	protected MenuItem cancelItem;
	protected MenuItem receiveMessage;
	protected MenuItem grant;
	private MenuItem response;
	private MenuItem sendMessage;

	public NegotiationOperationsToolBar(){
		hideAll();
		showItem(SUB_MENU.EDIT, true);
		showItem(SUB_MENU.DATA, true);
		showItem(SUB_MENU.ADMIN, true);
		showItem(SUB_MENU.REQUESTS, true);
		showItem(SUB_MENU.EXECUTE, true);

		deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);

		cancelItem = new MenuItem("Cancelar Negociação", new Command(){

			@Override
			public void execute() {
				onCancelNegotiationRequest();

			}
		});
		addItem(SUB_MENU.DATA, cancelItem);

		sendMessage = new MenuItem("Enviar Mensagem", new Command(){

			@Override
			public void execute() {
				onSendMessage();
			}

		});
		addItem(SUB_MENU.REQUESTS, sendMessage);

		receiveMessage = new MenuItem("Receber Mensagem", new Command(){

			@Override
			public void execute() {
				onReceiveMessage();
			}

		});
		addItem(SUB_MENU.REQUESTS, receiveMessage);

		grant = new MenuItem("Adjudicar com a seguradora", new Command() {

			@Override
			public void execute() {
				onGrant();

			}
		});
		addItem(SUB_MENU.EXECUTE, grant);

		response = new MenuItem("Receber cotação da seguradora", new Command(){

			@Override
			public void execute() {
				onResponse();

			}


		});
		addItem(SUB_MENU.EXECUTE, response);	
	}


	protected abstract void onSendMessage();
	public abstract void onResponse();
	public abstract void onDeleteRequest();
	public abstract void onCancelNegotiationRequest();
	public abstract void onReceiveMessage();
	public abstract void onGrant();

	public void allowDelete(boolean allow){
		deleteItem.setEnabled(allow);
	}

	public void allowEdit(boolean allow){
		editCancelMenuItem.setEnabled(allow);
	}

	public void allowCancelNegotiation(boolean allow){
		cancelItem.setEnabled(allow);
	}

	public void allowReceiveMessage(boolean allow){
		receiveMessage.setEnabled(allow);
	}

	public void allowGrant(boolean allow) {
		grant.setEnabled(allow);

	}
	public void allowResponse(boolean hasPermission) {
		grant.setEnabled(hasPermission);

	}

	public void allowSendMessage(boolean b) {
		sendMessage.setEnabled(b);
	}

}
