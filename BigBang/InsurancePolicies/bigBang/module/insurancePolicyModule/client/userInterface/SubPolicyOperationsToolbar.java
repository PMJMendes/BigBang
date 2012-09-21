package bigBang.module.insurancePolicyModule.client.userInterface;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

public abstract class SubPolicyOperationsToolbar extends BigBangOperationsToolBar {

	//EXECUTE
	protected MenuItem executeDetailedCalculations;
	protected MenuItem validate;

	//ADMIN
	protected MenuItem deleteItem;
	protected MenuItem voidItem;

	//DATA
	protected MenuItem transferToPolicy;
	protected MenuItem createInsuredObject;

	//REQUESTS
	protected MenuItem infoOrDocumentRequest;

	//CREATE
	protected MenuItem createReceipt;
	protected MenuItem healthExpense;

	public SubPolicyOperationsToolbar(){
		//CREATE
		createReceipt = new MenuItem("Recibo", new Command() {

			@Override
			public void execute() {
				onCreateReceipt();
			}
		});
		addItem(SUB_MENU.CREATE, createReceipt);
		
		healthExpense = new MenuItem("Despesa de Saúde", new Command() {
			
			@Override
			public void execute() {
				onCreateHealthExpense();
			}
		});
		addItem(SUB_MENU.CREATE, healthExpense);
		//EXECUTE
		executeDetailedCalculations = new MenuItem("Cálculos Detalhados", new Command() {

			@Override
			public void execute() {
				onPerformCalculations();
			}
		});
		addItem(SUB_MENU.EXECUTE, executeDetailedCalculations);
		validate = new MenuItem("Validar Apólice Adesão", new Command() {

			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(SUB_MENU.EXECUTE, validate);

		//REQUESTS
		infoOrDocumentRequest = new MenuItem("Pedido de Informação ou Documento", new Command() {
			
			@Override
			public void execute() {
				onCreateInfoOrDocumentRequest();
			}
		});
		addItem(SUB_MENU.REQUESTS, infoOrDocumentRequest);

		//ADMIN
		voidItem = new MenuItem("Anular", new Command() {

			@Override
			public void execute() {
				onVoid();
			}
		});
		addItem(SUB_MENU.ADMIN, voidItem);
		deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);

		//DATA
		transferToPolicy = new MenuItem("Transferir para Apólice", new Command() {

			@Override
			public void execute() {
				onTransferToPolicy();
			}
		});
		addItem(SUB_MENU.DATA, transferToPolicy);
	}

	protected abstract void onCreateHealthExpense();

	public void allowDelete(boolean allow){
		this.deleteItem.setEnabled(allow);
	}

	public void allowCreateInsuredObject(boolean allow) {
		this.createInsuredObject.setEnabled(allow);
	}
	
	public void allowPerformCalculations(boolean allow) {
		this.executeDetailedCalculations.setEnabled(allow);
	}
	
	public void allowTransferToPolicy(boolean allow) {
		this.transferToPolicy.setEnabled(allow);
	}

	public void allowCreateInfoOrDocumentRequest(boolean allow) {
		this.infoOrDocumentRequest.setEnabled(allow);
	}
	
	public void allowCreateReceipt(boolean allow) {
		this.createReceipt.setEnabled(allow);
	}
	
	public void allowValidate(boolean allow) {
		this.validate.setEnabled(allow);
	}

	public void allowVoid(boolean allow) {
		this.voidItem.setEnabled(allow);
	}
	
	public abstract void onDelete();

	public abstract void onIncludeInsuredObject();

	public abstract void onIncludeInsuredObjectFromClient();

	public abstract void onCreateInsuredObjectFromClient();

	public abstract void onExcludeInsuredObject();

	public abstract void onPerformCalculations();

	public abstract void onValidate();

	public abstract void onTransferToPolicy();

	public abstract void onCreateInfoOrDocumentRequest();

	public abstract void onCreateReceipt();

	public abstract void onVoid();

	public void allowCreateHealthExpense(boolean allow) {
		this.healthExpense.setEnabled(allow);
	}

	public void allowIncludeInsuredObject(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	public void allowCreateInsuredObjectFromClient(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	public void allowExcludeInsuredObject(boolean allow) {
		//TODO		
	}

	public void allowIncludeInsuredObjectFromClient(boolean allow) {
		//TODO
		}



}
