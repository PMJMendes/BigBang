package bigBang.module.expenseModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ExpenseProcessToolBar extends BigBangOperationsToolBar{



	//ADMIN
	protected MenuItem deleteExpense;
	protected MenuItem closeProcess;

	//EXECUTE
	//agency
	protected MenuItem participateToInsurer;
	//client
	protected MenuItem returnToClient;
	protected MenuItem notifyClient;
	//other
	protected MenuItem validate;
	//REQUESTS
	//client
	protected MenuItem infoOrDocumentRequest;
	//agency
	protected MenuItem infoFromInsurer;
	protected MenuItem receiveResponse;


	public ExpenseProcessToolBar() {

		//ADMIN
		deleteExpense = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDeleteRequest();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteExpense);

		closeProcess = new MenuItem("Fechar Processo", new Command() {

			@Override
			public void execute() {
				onCloseProcess();
			}
		});
		addItem(SUB_MENU.ADMIN, closeProcess);

		//EXECUTE

		//client
		returnToClient = new MenuItem("Devolver ao cliente", new Command() {

			@Override
			public void execute() {
				onReturnToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, returnToClient);

		notifyClient = new MenuItem("Notificar resultados ao cliente", new Command() {

			@Override
			public void execute() {
				onNotifyClient();
			}
		});

		//agency

		participateToInsurer = new MenuItem("Participar à seguradora", new Command() {

			@Override
			public void execute() {
				participateToInsurer();
			}
		});
		addItem(SUB_MENU.EXECUTE, participateToInsurer);
		
		//other
		validate = new MenuItem("Validar", new Command() {
			
			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(SUB_MENU.EXECUTE, validate);
		
		//REQUESTS
		//client
		
		infoOrDocumentRequest = new MenuItem("Pedido de Informação ao Cliente", new Command() {
			
			@Override
			public void execute() {
				onInfoOrDocumentRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, infoOrDocumentRequest);
		
		//agency
		infoFromInsurer = new MenuItem("Pedido de Informação à Seguradora", new Command() {
			
			@Override
			public void execute() {
				onInfoFromInsurer();
			}
		});
		addItem(SUB_MENU.REQUESTS, infoFromInsurer);
		
		receiveResponse = new MenuItem("Receber Resposta", new Command() {
			
			@Override
			public void execute() {
				onReceiveResponse();
			}
		});
		requestsSubMenu.addSeparator();
		addItem(SUB_MENU.REQUESTS, receiveResponse);
		
		createMenuItem.setVisible(false);
		dataMenuItem.setVisible(false);
	}

	protected abstract void onReceiveResponse();

	protected abstract void onInfoFromInsurer();
	
	protected abstract void onInfoOrDocumentRequest();
		
	protected abstract void onValidate();

	protected abstract void onNotifyClient();

	protected abstract void participateToInsurer();

	protected abstract void onReturnToClient();

	protected abstract void onDeleteRequest();

	@Override
	public abstract void onEditRequest();

	@Override
	public abstract void onSaveRequest();

	@Override
	public abstract void onCancelRequest();

	public abstract void onCloseProcess();


	public void allowDelete(boolean allow) {
		deleteExpense.setEnabled(allow);
	}

	public void allowReturnToClient(boolean allow){
		returnToClient.setEnabled(allow);
	}

	public void allowNotifyClient(boolean allow){
		notifyClient.setEnabled(allow);
	}

	public void allowCloseProcess(boolean allow){
		closeProcess.setEnabled(allow);
	}

	public void allowEdit(boolean allow){
		editCancelMenuItem.setEnabled(allow);
	}

	public void allowParticipateToInsurer(boolean allow){
		participateToInsurer.setEnabled(allow);
	}
	
	public void allowValidate(boolean allow){
		validate.setEnabled(allow);
	}
	
	public void allowInfoOrDocumentRequest(boolean allow){
		infoOrDocumentRequest.setEnabled(allow);
	}
	
	public void allowInfoFromInsurer(boolean allow){
		infoFromInsurer.setEnabled(allow);
	}

	public void allowReceiveResponse(boolean allow) {
		receiveResponse.setEnabled(allow);
	}


}
