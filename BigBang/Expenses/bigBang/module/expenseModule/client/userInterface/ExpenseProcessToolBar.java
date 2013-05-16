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
	protected MenuItem receiveAcceptance;
	protected MenuItem receiveRejection;
	//client
	protected MenuItem returnToClient;
	protected MenuItem notifyClient;
	//other
	//REQUESTS
	//client
	protected MenuItem sendMessage;
	//agency
	protected MenuItem receiveMessage;

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

		//agency

		participateToInsurer = new MenuItem("Participar à seguradora", new Command() {

			@Override
			public void execute() {
				participateToInsurer();
			}
		});
		addItem(SUB_MENU.EXECUTE, participateToInsurer);
		receiveAcceptance = new MenuItem("Receber Aceitação", new Command() {

			@Override
			public void execute() {
				onReceiveAcceptance();
			}
		});
		addItem(SUB_MENU.EXECUTE, receiveAcceptance);
		receiveRejection = new MenuItem("Receber Recusa", new Command() {

			@Override
			public void execute() {
				onReceiveRejection();
			}
		});
		addItem(SUB_MENU.EXECUTE, receiveRejection);
		
		executeSubMenu.addSeparator();
		
		//client
		returnToClient = new MenuItem("Devolver ao cliente", new Command() {

			@Override
			public void execute() {
				onReturnToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, returnToClient);

		notifyClient = new MenuItem("Notificar cliente", new Command() {

			@Override
			public void execute() {
				onNotifyClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, notifyClient);

		//REQUESTS
		//client

		sendMessage = new MenuItem("Enviar Mensagem", new Command() {

			@Override
			public void execute() {
				sendMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, sendMessage);

		//agency
		receiveMessage = new MenuItem("Receber Mensagem", new Command() {

			@Override
			public void execute() {
				receiveMessage();
			}
		});
		addItem(SUB_MENU.REQUESTS, receiveMessage);


		createMenuItem.setVisible(false);
		dataMenuItem.setVisible(false);
	}

	protected abstract void onReceiveAcceptance();

	protected abstract void onReceiveRejection();

	protected abstract void receiveMessage();

	protected abstract void sendMessage();

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

	public void allowSendMessage(boolean allow){
		sendMessage.setEnabled(allow);
	}

	public void allowReceiveMessage(boolean allow){
		receiveMessage.setEnabled(allow);
	}

	public void allowReceiveAcceptance(boolean allow) {
		receiveAcceptance.setEnabled(allow);
	}

	public void allowReceiveRejection(boolean hasPermission) {
		receiveRejection.setEnabled(hasPermission);

	}


}
