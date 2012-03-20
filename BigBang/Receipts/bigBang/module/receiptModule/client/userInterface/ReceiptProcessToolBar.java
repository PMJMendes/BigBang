package bigBang.module.receiptModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class ReceiptProcessToolBar extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem issueDebitNote;
	
	//EXECUTE
	//internal
	protected MenuItem transferToPolicy;
	protected MenuItem validate;
	//client
	protected MenuItem sendPaymentNotice;
	protected MenuItem sendSecondPaymentNotice;
	protected MenuItem sendReceipt;
	protected MenuItem sendPaymentToClient;
	//agency
	protected MenuItem returnToAgency;
	protected MenuItem sendPaymentToAgency;
	//mediator
	protected MenuItem sendPaymentToMediator;
	
	//DATA
	protected MenuItem receivePhysicalReceipt;
	protected MenuItem associateWithDebitNote;
	protected MenuItem enterPayment;
	protected MenuItem lackOfPaymentFlag;
	protected MenuItem setForReturn;
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
		
		//EXECUTE
		transferToPolicy = new MenuItem("Transferência para Outra Apólice", new Command() {
			
			@Override
			public void execute() {
				onTransferToPolicy();
			}
		});
		addItem(SUB_MENU.EXECUTE, transferToPolicy);
		validate = new MenuItem("Validação", new Command() {
			
			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(SUB_MENU.EXECUTE, validate);
		this.executeSubMenu.addSeparator();
		sendPaymentNotice = new MenuItem("Enviar Aviso de Cobrança", new Command() {
			
			@Override
			public void execute() {
				onSendPaymentNotice();
			}
		});
		addItem(SUB_MENU.EXECUTE, sendPaymentNotice);
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
		returnToAgency = new MenuItem("Devolver à Seguradora", new Command() {
			
			@Override
			public void execute() {
				onReturnToAgency();
			}
		});
		addItem(SUB_MENU.EXECUTE, returnToAgency);
		sendPaymentToAgency = new MenuItem("Prestação de Contas", new Command() {
			
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
		
		//DATA
		receivePhysicalReceipt = new MenuItem("Associar Imagem", new Command() {
			
			@Override
			public void execute() {
				onReceivePhysicalReceipt();
			}
		});
		addItem(SUB_MENU.DATA, receivePhysicalReceipt);
		associateWithDebitNote = new MenuItem("Associar a Nota de Débito", new Command() {
			
			@Override
			public void execute() {
				onAssociateWithDebitNote();
			}
		});
		addItem(SUB_MENU.DATA, associateWithDebitNote);
		enterPayment = new MenuItem("Cobrança", new Command() {
			
			@Override
			public void execute() {
				onEnterPayment();
			}
		});
		addItem(SUB_MENU.DATA, enterPayment);
		lackOfPaymentFlag = new MenuItem("Indicar Falta de Pagamento", new Command() {
			
			@Override
			public void execute() {
				onLackOfPaymentFlag();
			}
		});
		addItem(SUB_MENU.DATA, lackOfPaymentFlag);
		setForReturn = new MenuItem("Marcar para Devolução", new Command() {
			
			@Override
			public void execute() {
				onSetForReturn();
			}
		});
		addItem(SUB_MENU.DATA, setForReturn);
		unnecessaryDASFlag = new MenuItem("Indicar DAS Desnecessária", new Command() {
			
			@Override
			public void execute() {
				onUnnecessaryDASFlag();
			}
		});
		addItem(SUB_MENU.DATA, unnecessaryDASFlag);
		
		//REQUESTS
		requestPurchaseOrderNumber = new MenuItem("Criar Pedido de Número de Encomenda", new Command() {
			
			@Override
			public void execute() {
				onRequestPurchaseOrderNumber();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestPurchaseOrderNumber);
		requestDAS = new MenuItem("Criar Pedido de DAS", new Command() {
			
			@Override
			public void execute() {
				onRequestDAS();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestDAS);
		requestSignature = new MenuItem("Criar Pedido de Assinatura", new Command() {
			
			@Override
			public void execute() {
				onRequestSignature();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestSignature);
		this.requestsSubMenu.addSeparator();
		requestPhysicalReceipt = new MenuItem("Criar Pedido de Recibo Não Enviado", new Command() {
			
			@Override
			public void execute() {
				onRequestPhysicalReceipt();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestPhysicalReceipt);
		requestPhysicalReceiptCopy = new MenuItem("Criar Pedido de Segunda Via de Recibo", new Command() {
			
			@Override
			public void execute() {
				onRequestPhysicalReceiptCopy();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestPhysicalReceiptCopy);
		requestAdvanceDebit = new MenuItem("Criar Pedido de Débito Antecipado em Conta Efectiva", new Command() {
			
			@Override
			public void execute() {
				onRequestAdvanceDebit();
			}
		});
		addItem(SUB_MENU.REQUESTS, requestAdvanceDebit);
		
		//ADMIN
		deleteReceipt = new MenuItem("Eliminar", new Command() {
			
			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteReceipt);
		
	}
	
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

}
