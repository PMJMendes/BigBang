package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.userInterface.BigBangOperationsToolBar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public abstract class InsurancePolicyOperationsToolBar_OLD extends BigBangOperationsToolBar {

	//CREATE
	protected MenuItem receiptItem;
	protected MenuItem riskAnalysis;
	protected MenuItem healthExpense;
	protected MenuItem negotiation;
	protected MenuItem issueDebitNote;
	protected MenuItem subPolicy;
	protected MenuItem infoManagementProcess;

	//EXECUTE
	protected MenuItem detailedCalculations;
	protected MenuItem validate;
	protected MenuItem clientTransfer;

	//DATA
	protected MenuItem managerTransfer;
	protected MenuItem clientAsInsuredObject;
	protected MenuItem includeInsuredObject;
	protected MenuItem createInsuredObject;
	protected MenuItem createExercise;

	//REQUESTS
	protected MenuItem agencyInfoRequestItem;
	protected MenuItem clientInfoRequestItem;

	//ADMIN
	protected MenuItem substitutePolicy;
	protected MenuItem deleteItem;
	protected MenuItem brokerageTransfer;
	protected MenuItem voidPolicy;

	//OTHER


	public InsurancePolicyOperationsToolBar_OLD(){
		//CREATE
		receiptItem = new MenuItem("Recibo", new Command() {

			@Override
			public void execute() {
				onCreateReceipt();
			}
		});
		addItem(SUB_MENU.CREATE, receiptItem);
		riskAnalysis = new MenuItem("Análise de Risco", new Command() {

			@Override
			public void execute() {
				onCreateRiskAnalysis();
			}
		});
		addItem(SUB_MENU.CREATE, riskAnalysis);
		healthExpense = new MenuItem("Despesa de Saúde", new Command() {

			@Override
			public void execute() {
				onCreateHealthExpense();
			}
		});
		addItem(SUB_MENU.CREATE, healthExpense);
		negotiation = new MenuItem("Negociação", new Command() {

			@Override
			public void execute() {
				onCreateNegotiation();
			}
		});
		addItem(SUB_MENU.CREATE, negotiation);
		issueDebitNote = new MenuItem("Emitir Nota de Débito", new Command() {

			@Override
			public void execute() {
				onIssueDebitNote();
			}
		});
		addItem(SUB_MENU.CREATE, issueDebitNote);
		subPolicy = new MenuItem("Apólice Adesão", new Command() {

			@Override
			public void execute() {
				onCreateSubPolicy();
			}
		});
		addItem(SUB_MENU.CREATE, subPolicy);
		infoManagementProcess = new MenuItem("Processo de Gestão de Informação", new Command() {

			@Override
			public void execute() {
				onCreateInfoManagementProcess();
			}
		});
		addItem(SUB_MENU.CREATE, infoManagementProcess);

		//EXECUTE
		detailedCalculations = new MenuItem("Cálculos Detalhados", new Command() {

			@Override
			public void execute() {
				onExecuteDecailedCalculations();
			}
		});
		addItem(SUB_MENU.EXECUTE, detailedCalculations);
		validate = new MenuItem("Validar Apólice", new Command() {

			@Override
			public void execute() {
				onValidate();
			}
		});
		addItem(SUB_MENU.EXECUTE, validate);

		//DATA
		managerTransfer = new MenuItem("Criar Transferência de Gestor", new Command() {

			@Override
			public void execute() {
				onCreateManagerTransfer();
			}
		});
		addItem(SUB_MENU.DATA, managerTransfer);
		includeInsuredObject = new MenuItem("Incluir Unidade de Risco", new Command() {

			@Override
			public void execute() {
				onIncludeInsuredObject();
			}
		});
		addItem(SUB_MENU.DATA, includeInsuredObject);
		clientAsInsuredObject = new MenuItem("Criar Unidade de Risco a Partir do Cliente", new Command() {

			@Override
			public void execute() {
				onCreateInsuredObjectFromClient();
			}
		});
		addItem(SUB_MENU.DATA, clientAsInsuredObject);
		createInsuredObject = new MenuItem("Criar Unidade de Risco", new Command() {

			@Override
			public void execute() {
				onCreateInsuredObject();
			}
		});
		addItem(SUB_MENU.DATA, createInsuredObject);	
		createExercise = new MenuItem("Abrir Exercício", new Command() {

			@Override
			public void execute() {
				onCreateExercise();
			}
		});
		addItem(SUB_MENU.DATA, createExercise);	

		//REQUESTS
		agencyInfoRequestItem = new MenuItem("Pedido de Informação à Seguradora", new Command() {

			@Override
			public void execute() {
				onRequestInfoFromAgency();
			}
		});
		addItem(SUB_MENU.REQUESTS, agencyInfoRequestItem);
		clientInfoRequestItem = new MenuItem("Pedido de Informação ao Cliente", new Command() {

			@Override
			public void execute() {
				onRequestInfoFromClient();
			}
		});
		addItem(SUB_MENU.REQUESTS, clientInfoRequestItem);

		//ADMIN
		substitutePolicy = new MenuItem("Criar Apólice de Substituição", new Command() {

			@Override
			public void execute() {
				onCreateSubstitutePolicy();
			}
		});
		addItem(SUB_MENU.ADMIN, substitutePolicy);
		deleteItem = new MenuItem("Eliminar", new Command() {

			@Override
			public void execute() {
				onDelete();
			}
		});
		addItem(SUB_MENU.ADMIN, deleteItem);	
		brokerageTransfer = new MenuItem("Transferência de Mediação", new Command() {

			@Override
			public void execute() {
				onBrokerageTransfer();
			}
		});
		addItem(SUB_MENU.ADMIN, brokerageTransfer);
		voidPolicy = new MenuItem("Anular Apólice", new Command() {

			@Override
			public void execute() {
				onVoidPolicy();
			}
		});
		addItem(SUB_MENU.ADMIN, voidPolicy);
		
		clientTransfer = new MenuItem("Transferir para Cliente", new Command(){

			@Override
			public void execute() {
				onTransferToClient();
				
			}
			
		});
		addItem(SUB_MENU.EXECUTE, clientTransfer);
		
	}
	
	public void allowTransferToClient(boolean allow){
		this.clientTransfer.setEnabled(allow);
	}

	public void allowCreateReceipt(boolean allow){
		this.receiptItem.setEnabled(allow);
	};

	public void allowIncludeInsuredObject(boolean allow){
		this.includeInsuredObject.setEnabled(allow);
	}
	
	public void allowCreateInsuredObject(boolean allow){
		this.createInsuredObject.setEnabled(allow);
	}

	public void allowCreateExercise(boolean allow) {
		this.createExercise.setEnabled(allow);
	}

	public void allowDelete(boolean allow) {
		this.deleteItem.setEnabled(allow);
	}
	
	public void allowValidate(boolean allow) {
		this.validate.setEnabled(allow);
	}
	
	public void allowVoidPolicy(boolean allow) {
		this.voidPolicy.setEnabled(allow);
	}

	public void allowTransferBrokerage(boolean allow) {
		this.brokerageTransfer.setEnabled(allow);
	}

	public void allowCreateSubstitutepolicy(boolean allow) {
		this.substitutePolicy.setEnabled(allow);
	}

	public void allowRequestClientInfo(boolean allow) {
		this.clientInfoRequestItem.setEnabled(allow);
	}

	public void allowRequestAgencyInfo(boolean allow) {
		this.agencyInfoRequestItem.setEnabled(allow);
	}

	public void allowCreateInsuredObjectFromClient(boolean allow) {
		this.clientAsInsuredObject.setEnabled(allow);
	}

	public void allowTransferManager(boolean allow) {
		this.managerTransfer.setEnabled(allow);
	}

	public void allowExecuteDetailedalculations(boolean allow) {
		this.detailedCalculations.setEnabled(allow);
	}

	public void allowCreateInfoManagementProcess(boolean allow) {
		this.infoManagementProcess.setEnabled(allow);
	}

	public void allowCreateSubPolicy(boolean allow) {
		this.subPolicy.setEnabled(allow);
	}

	public void allowIssueDebitNote(boolean allow) {
		this.issueDebitNote.setEnabled(allow);
	}

	public void allowCreateNegotiation(boolean allow) {
		this.negotiation.setEnabled(allow);
	}

	public void allowCreateHealthExpense(boolean allow) {
		this.healthExpense.setEnabled(allow);
	}

	public void allowCreateRiskAnalisys(boolean allow) {
		this.riskAnalysis.setEnabled(allow);
	}
	

	public abstract void onVoidPolicy();

	public abstract void onBrokerageTransfer();

	public abstract void onDelete();

	public abstract void onValidate();
	
	public abstract void onCreateSubstitutePolicy();

	public abstract void onRequestInfoFromClient();

	public abstract void onRequestInfoFromAgency();

	public abstract void onCreateInsuredObject();
	
	public abstract void onIncludeInsuredObject();

	public abstract void onCreateExercise();

	public abstract void onCreateInsuredObjectFromClient();

	public abstract void onCreateManagerTransfer();

	public abstract void onExecuteDecailedCalculations();

	public abstract void onCreateInfoManagementProcess();

	public abstract void onCreateSubPolicy();

	public abstract void onIssueDebitNote();

	public abstract void onCreateNegotiation();

	public abstract void onCreateHealthExpense();

	public abstract void onCreateRiskAnalysis();

	public abstract void onCreateReceipt();
	
	public abstract void onTransferToClient();

}
