package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ReceiptProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem issueDebitNote;

	//EXECUTE
	//internal
	protected MenuItem lackOfPaymentFlag;
	protected MenuItem enterPayment;
	protected MenuItem returnPayment;

	protected MenuItem transferToPolicy;
	//
	protected MenuItem validate;
	protected MenuItem setForReturn;
	//client
	protected MenuItem sendPaymentNotice;
	protected MenuItem cancelPaymentNotice;
	protected MenuItem sendSecondPaymentNotice;
	protected MenuItem sendReceipt;
	protected MenuItem sendPaymentToClient;
	//agency
	protected MenuItem returnToAgency;
	protected MenuItem sendPaymentToAgency;
	//mediator
	protected MenuItem sendPaymentToMediator;
	//other
	protected MenuItem generateReceipt;
	//DATA
	protected MenuItem receivePhysicalReceipt;
	protected MenuItem associateWithDebitNote;
	protected MenuItem unnecessaryDASFlag;

	//REQUESTS
	//client
	protected MenuItem requestPurchaseOrderNumber;
	protected MenuItem requestDAS;
	protected MenuItem requestSignature;
	//agency
	protected MenuItem requestPhysicalReceipt;
	protected MenuItem requestPhysicalReceiptCopy;
	protected MenuItem requestAdvanceDebit;

	//ADMIN
	protected MenuItem deleteReceipt;

	private MenuItem sendMessage;

	private MenuItem receiveMessage;

	//OTHER

	public ReceiptProcessToolBar(){
		//CREATE
		issueDebitNote = new MenuItem("Nota de Débito", new Command() {

			@Override
			public void execute() {
				onCreateCreditNote();
			}
		});
		addItem(SUB_MENU.CREATE, issueDebitNote);
		this.createSubMenu.addSeparator();
		requestPurchaseOrderNumber = new MenuItem("Pedido de Número de Encomenda", new Command() {

			@Override
			public void execute() {
				onRequestPurchaseOrderNumber();
			}
		});
		addItem(SUB_MENU.CREATE, requestPurchaseOrderNumber);
		requestDAS = new MenuItem("Pedido de DAS", new Command() {

			@Override
			public void execute() {
				onRequestDAS();
			}
		});
		addItem(SUB_MENU.CREATE, requestDAS);
		requestSignature = new MenuItem("Pedido de Assinatura", new Command() {

			@Override
			public void execute() {
				onRequestSignature();
			}
		});
		addItem(SUB_MENU.CREATE, requestSignature);
		this.createSubMenu.addSeparator();
		requestPhysicalReceipt = new MenuItem("Pedido de Recibo Não Enviado", new Command() {

			@Override
			public void execute() {
				onRequestPhysicalReceipt();
			}
		});
		addItem(SUB_MENU.CREATE, requestPhysicalReceipt);
		requestPhysicalReceiptCopy = new MenuItem("Pedido de Segunda Via de Recibo", new Command() {

			@Override
			public void execute() {
				onRequestPhysicalReceiptCopy();
			}
		});
		addItem(SUB_MENU.CREATE, requestPhysicalReceiptCopy);
		requestAdvanceDebit = new MenuItem("Pedido de Débito Antecipado em Conta Efectiva", new Command() {

			@Override
			public void execute() {
				onRequestAdvanceDebit();
			}
		});
		addItem(SUB_MENU.CREATE, requestAdvanceDebit);
		
		//EXECUTE
		enterPayment = new MenuItem("Cobrança", new Command() {

			@Override
			public void execute() {
				onEnterPayment();
			}
		});
		addItem(SUB_MENU.EXECUTE, enterPayment);
		returnPayment = new MenuItem("Devolver Pagamento", new Command() {

			@Override
			public void execute() {
				onReturnPayment();
			}
		});
		addItem(SUB_MENU.EXECUTE, returnPayment);
		lackOfPaymentFlag = new MenuItem("Indicar Falta de Pagamento", new Command() {

			@Override
			public void execute() {
				onLackOfPaymentFlag();
			}
		});
		addItem(SUB_MENU.EXECUTE, lackOfPaymentFlag);
		returnToAgency = new MenuItem("Devolução à Seguradora", new Command() {

			@Override
			public void execute() {
				onReturnToAgency();
			}
		});
		addItem(SUB_MENU.EXECUTE, returnToAgency);
		transferToPolicy = new MenuItem("Transferir para Apólice", new Command() {

			@Override
			public void execute() {
				onTransferToPolicy();
			}
		});
		addItem(SUB_MENU.EXECUTE, transferToPolicy);
		this.executeSubMenu.addSeparator();
		validate = new MenuItem("Validar", new Command() {

			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(SUB_MENU.EXECUTE, validate);
		setForReturn = new MenuItem("Marcar para Devolução", new Command() {

			@Override
			public void execute() {
				onSetForReturn();
			}
		});
		addItem(SUB_MENU.EXECUTE, setForReturn);
		this.executeSubMenu.addSeparator();
		sendPaymentNotice = new MenuItem("Enviar Aviso de Cobrança", new Command() {

			@Override
			public void execute() {
				onSendPaymentNotice();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendPaymentNotice);
		cancelPaymentNotice = new MenuItem("Cancelar Aviso de Cobrança", new Command(){

			@Override
			public void execute() {
				onCancelPaymentNotice();
			}
			
		});
		addItem(SUB_MENU.EXECUTE, cancelPaymentNotice);
		
		sendSecondPaymentNotice = new MenuItem("Enviar Segundo Aviso de Cobrança", new Command() {

			@Override
			public void execute() {
				onResendPaymentNotice();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendSecondPaymentNotice);
		sendReceipt = new MenuItem("Enviar Recibo", new Command() {

			@Override
			public void execute() {
				onSendReceipt();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendReceipt);
		sendPaymentToClient = new MenuItem("Enviar Pagamento ao Cliente", new Command() {

			@Override
			public void execute() {
				onSendPaymentToClient();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendPaymentToClient);
		this.executeSubMenu.addSeparator();
		associateWithDebitNote = new MenuItem("Associar a Nota de Débito", new Command() {

			@Override
			public void execute() {
				onAssociateWithDebitNote();
			}
		});
		addItem(SUB_MENU.EXECUTE, associateWithDebitNote);
		sendPaymentToAgency = new MenuItem("Prestação de Contas à Seguradora", new Command() {

			@Override
			public void execute() {
				onSendPaymentToAgency();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendPaymentToAgency);
		this.executeSubMenu.addSeparator();
		sendPaymentToMediator = new MenuItem("Retrocessão", new Command() {

			@Override
			public void execute() {
				onSendPaymentToMediator();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendPaymentToMediator);

		this.executeSubMenu.addSeparator();

		generateReceipt = new MenuItem("Emissão de Recibo", new Command(){

			@Override
			public void execute() {
				onGenerateReceipt();
			}

		});

		addItem(SUB_MENU.EXECUTE, generateReceipt);
		//DATA
		receivePhysicalReceipt = new MenuItem("Associar Imagem", new Command() { //TODO remove or add

			@Override
			public void execute() {
				onReceivePhysicalReceipt();
			}
		});
		//		addItem(SUB_MENU.DATA, receivePhysicalReceipt);
		unnecessaryDASFlag = new MenuItem("Indicar DAS Desnecessária", new Command() {

			@Override
			public void execute() {
				onUnnecessaryDASFlag();
			}
		});
		addItem(SUB_MENU.DATA, unnecessaryDASFlag);

		//REQUESTS
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
		requestsSubMenu.addSeparator();

		

		//ADMIN
		deleteReceipt = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteReceipt);

	}

	protected abstract void onCancelPaymentNotice();

	public abstract void onGenerateReceipt();

	protected abstract void onReceiveMessage();

	protected abstract void onSendMessage();

	public abstract void onDelete();

	public abstract void onRequestAdvanceDebit();

	public abstract void onRequestPhysicalReceiptCopy();

	public abstract void onRequestPhysicalReceipt();

	public abstract void onRequestSignature();

	public abstract void onRequestDAS();

	public abstract void onRequestPurchaseOrderNumber();

	public abstract void onUnnecessaryDASFlag();

	public abstract void onSetForReturn();

	public abstract void onLackOfPaymentFlag();

	public abstract void onEnterPayment();

	public abstract void onReturnPayment();

	public abstract void onAssociateWithDebitNote();

	public abstract void onReceivePhysicalReceipt();

	public abstract void onSendPaymentToMediator();

	public abstract void onReturnToAgency();

	public abstract void onSendPaymentToAgency();

	public abstract void onSendPaymentToClient();

	public abstract void onSendReceipt();

	public abstract void onResendPaymentNotice();

	public abstract void onSendPaymentNotice();

	public abstract void onValidate();

	public abstract void onTransferToPolicy();

	public abstract void onCreateCreditNote();

	public void allowDelete(boolean allow){
		this.deleteReceipt.setEnabled(allow);
	}

	public void allowTransfer(boolean allow) {
		this.transferToPolicy.setEnabled(allow);
	}

	public void allowAssociateDebitNote(boolean hasPermission) {
		this.associateWithDebitNote.setEnabled(hasPermission);
	}

	public void allowValidate(boolean allow){
		this.validate.setEnabled(allow);
	}

	public void allowSetForReturn(boolean allow){
		this.setForReturn.setEnabled(allow);
	}

	public void allowCreatePaymentNotice(boolean hasPermission) {
		this.sendPaymentNotice.setEnabled(hasPermission);

	}

	public void allowMarkForPayment(boolean allow) {
		this.enterPayment.setEnabled(allow);
	}

	public void allowSendReceipt(boolean allow) {
		this.sendReceipt.setEnabled(allow);
	}

	public void allowInsurerAccounting(boolean allow) {
		this.sendPaymentToAgency.setEnabled(allow);
	}

	public void allowAgentAccounting(boolean allow) {
		this.sendPaymentToMediator.setEnabled(allow);
	}

	public void allowPaymentToClient(boolean hasPermission) {
		this.sendPaymentToClient.setEnabled(hasPermission);

	}

	public void allowReturnToAgency(boolean hasPermission){
		this.returnToAgency.setEnabled(hasPermission);
	}

	public void allowCreateSignatureRequest(boolean hasPermission){
		this.requestSignature.setEnabled(hasPermission);
	}

	public void allowSetDASDesnecessary(boolean hasPermission) {
		this.unnecessaryDASFlag.setEnabled(hasPermission);
	}

	public void allowRequestDAS(boolean hasPermission) {
		this.requestDAS.setEnabled(hasPermission);
	}

	public void allowSetNotPaid(boolean hasPermission) {
		this.lackOfPaymentFlag.setEnabled(hasPermission);
	}

	public void allowReturnPayment(boolean hasPermission) {
		this.returnPayment.setEnabled(hasPermission);
	}

	public void allowGenerateReceipt(boolean hasPermission) {
		this.generateReceipt.setEnabled(hasPermission);
	}

	public void allowSendMessage(boolean hasPermission) {
		this.sendMessage.setEnabled(hasPermission);
	}

	public void allowReceiveMessage(boolean hasPermission) {
		this.receiveMessage.setEnabled(hasPermission);
	}

	public void allowCancelPaymentNotice(boolean hasPermission){
		this.cancelPaymentNotice.setEnabled(hasPermission);
	}

	public void allowCreateSecondPaymentNotice(boolean hasPermission) {
		this.sendSecondPaymentNotice.setEnabled(hasPermission);
	}
}
