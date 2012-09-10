package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.FieldContainer.ColumnField;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.ColumnHeader;
import bigBang.definitions.shared.InsurancePolicy.Coverage;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

import com.google.gwt.core.client.GWT;



public class InsurancePolicySearchOperationViewPresenter implements ViewPresenter{

	public static class Services
	{
		public static final InsurancePolicyServiceAsync insurancePolicyService =
				GWT.create(InsurancePolicyService.class);
	}

	public static enum Action {
		ON_POLICY_SELECTED,
		ON_NEW_RESULTS,
		NEW_EXERCISE,
		NEW_INSURED_OBJECT,
		DELETE_INSURED_OBJECT,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE,
		CREATE_RECEIPT,
		INCLUDE_INSURED_OBJECT,
		CREATE_INSURED_OBJECT,
		CREATE_EXERCISE,
		VOID_POLICY,
		TRANSFER_BROKERAGE,
		CREATE_SUBSTITUTE_POLICY,
		REQUEST_CLIENT_INFO,
		REQUEST_AGENCY_INFO,
		CREATE_INSURED_OBJECT_FROM_CLIENT,
		TRANSFER_MANAGER,
		VALIDATE,
		EXECUTE_DETAILED_CALCULATIONS,
		CREATE_INFO_MANAGEMENT_PROCESS,
		CREATE_SUB_POLICY, 
		ISSUE_DEBIT_NOTE,
		CREATE_NEGOTIATION, 
		CREATE_EXPENSE,
		CREATE_RISK_ANALISYS, 
		TRANSFER_TO_CLIENT,
	}
	public interface Display {
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValueSelectables<InsurancePolicyStub> getList();
		HasEditableValue<InsurancePolicy> getPolicyHeaderForm();
		HasEditableValue<InsuredObject> getInsuredObjectHeaderForm();
		void allowDeleteInsuredObject(boolean allow);
		HasEditableValue<FieldContainer> getCommonFieldsForm();

		HasValue<ExerciseData> getExerciseForm();
		HasValue<String> getExerciseSelector();
		void allowCreateNewExercise(boolean allow);

		HasValue<String> getPolicyNotesForm();
		HasValueSelectables<InsuredObjectStub> getInsuredObjectsList();

		//PERMISSIONS

		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowCreateReceipt(boolean allow);
		void allowCreateInsuredObject(boolean allow);
		void allowIncludeInsuredObject(boolean allow);
		void allowValidatePolicy(boolean allow);
		void allowVoidPolicy(boolean allow);
		void allowTransferBrokerage(boolean allow);
		void allowCreateSubstitutePolicy(boolean allow);
		void allowRequestClientInfo(boolean allow);
		void allowRequestAgencyInfo(boolean allow);
		void allowCreateInsuredObjectFromClient(boolean allow);
		void allowTransferManager(boolean allow);
		void allowExecuteDetailedCalculations(boolean allow);
		void allowCreateInfoManagementProcess(boolean allow);
		void allowCreateSubPolicy(boolean allow);
		void allowIssueDebitNote(boolean allow);
		void allowCreateNegotiation(boolean allow);
		void allowCreateHealthExpense(boolean allow);
		void allowCreateRiskAnalisys(boolean allow);
		void allowTransferToClient(boolean allow);

		//children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<SubPolicyStub> getSubPoliciesList();
		HasValueSelectables<ReceiptStub> getReceiptsList();
		HasValueSelectables<ExpenseStub> getExpensesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();



		Widget asWidget();

		void setAvailableExercises(ExerciseData[] exerciseData);

		HasValue<InsurancePolicyStub> getPolicySelector();

		void setExerciseVisible(boolean b);

		void setTableValues(Coverage[] coverages, ColumnHeader[] columns,
				ColumnField[] fields);
	}

	private InsurancePolicyBroker broker;
	private Display view;
	private boolean bound;

	public InsurancePolicySearchOperationViewPresenter(Display view){
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
		setView((UIObject) view);
	}


	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(this.bound)
			return;

		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selected = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				InsurancePolicyStub selectedValue = selected == null ? null : selected.getValue();
				String selectedPolicyId = selectedValue == null ? new String() : selectedValue.id;
				selectedPolicyId = selectedPolicyId == null ? new String() : selectedPolicyId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(selectedPolicyId.isEmpty()){
					item.removeParameter("policyid");
				}else{
					item.setParameter("policyid", selectedPolicyId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		this.view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

				switch(action.getAction()){
				case EDIT:
					onEdit();
					break;
				case CANCEL_EDIT:
					onCancelEdit();
					break;
				case SAVE:
					onSave();
					break;
				case DELETE:
					onDelete();
					break;
				case CREATE_RECEIPT:
					item.pushIntoStackParameter("display", "createreceipt");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_EXERCISE:
					createExercise();
					break;
				case INCLUDE_INSURED_OBJECT:
					item.pushIntoStackParameter("display", "includeinsuredobject");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INSURED_OBJECT:
					createInsuredObject();
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					item.pushIntoStackParameter("display", "createinsuredobjectfromclient");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_EXPENSE:
					item.pushIntoStackParameter("display", "createexpense");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INFO_MANAGEMENT_PROCESS:
					item.pushIntoStackParameter("display", "createinfomanagementprocess");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_NEGOTIATION:
					item.pushIntoStackParameter("display", "negotiation");
					item.setParameter("negotiationid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_RISK_ANALISYS:
					item.pushIntoStackParameter("display", "createriskanalisys");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_SUB_POLICY:
					item.pushIntoStackParameter("display", "subpolicy");
					//TODO BUSCAR ID item.setParameter("policyid", view.getForm().getValue().id);
					item.setParameter("subpolicyid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_SUBSTITUTE_POLICY:
					item.pushIntoStackParameter("display", "createsubstitutepolicy");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case EXECUTE_DETAILED_CALCULATIONS:
					onExecuteDetailedCalculations();
					break;
				case VALIDATE:
					onValidatePolicy();
					break;
				case ISSUE_DEBIT_NOTE:
					item.setParameter("show", "issuecreditnote");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case REQUEST_AGENCY_INFO:
					onRequestCompanyInfo();
					break;
				case REQUEST_CLIENT_INFO:
					onRequestClientInfo();
					break;
				case TRANSFER_BROKERAGE:
					item.pushIntoStackParameter("display", "brokeragetransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_MANAGER:
					item.setParameter("show", "transfermanager");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case VOID_POLICY:
					onVoidPolicy();
					break;
				case TRANSFER_TO_CLIENT:
					transferToClient();
					break;
				case ON_NEW_RESULTS:
					onNewResults();
					break;
				}
			}
		});
		
		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Contact selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Contact>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showContact(selectedValue);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				Document selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<Document>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showDocument(selectedValue);
				}
			}
		});
		view.getSubPoliciesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				SubPolicyStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<SubPolicyStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showSubPolicy(selectedValue);
				}
			}
		});
		view.getReceiptsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ReceiptStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<ReceiptStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showReceipt(selectedValue);
				}
			}
		});
		view.getExpensesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ExpenseStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<ExpenseStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showExpense(selectedValue);
				}
			}
		});
		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				BigBangProcess selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<BigBangProcess>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showSubProcess(selectedValue);
				}
			}
		});
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				HistoryItemStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<HistoryItemStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showHistory(selectedValue);
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		this.bound = true;
	}
	
	

	protected void showExpense(ExpenseStub selectedValue) {
		// TODO Auto-generated method stub		
	}


	protected void showSubProcess(BigBangProcess selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void showHistory(HistoryItemStub selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void showReceipt(ReceiptStub selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void showSubPolicy(SubPolicyStub selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void showDocument(Document selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void showContact(Contact selectedValue) {
		// TODO Auto-generated method stub
		
	}


	protected void onNewResults() {
		// TODO Auto-generated method stub
		
	}


	protected void onVoidPolicy() {
		// TODO Auto-generated method stub
		
	}


	protected void createInsuredObject() {
		// TODO Auto-generated method stub

	}


	protected void createExercise() {
		// TODO Auto-generated method stub

	}


	protected void onDelete() {
		// TODO Auto-generated method stub

	}


	protected void onEdit() {
		// TODO Auto-generated method stub

	}


	protected void onSave() {
		// TODO Auto-generated method stub

	}


	protected void onCancelEdit() {
		// TODO Auto-generated method stub

	}



	@Override
	public void setParameters(HasParameters parameterHolder) {

		String policyId = parameterHolder.getParameter("policyid");
		broker.getPolicy(policyId,new ResponseHandler<InsurancePolicy>() {
			
			@Override
			public void onResponse(InsurancePolicy response) {
				
				view.getPolicyHeaderForm().setValue(response);
				view.getPolicyNotesForm().setValue(response.notes);

				if(response.exerciseData == null){
					view.setExerciseVisible(false);
				}
				else{
					view.setAvailableExercises(response.exerciseData);
					view.getExerciseForm().setValue(response.exerciseData[0]);
					view.getExerciseSelector().setValue(response.exerciseData[0].id);
					view.setExerciseVisible(true);
				}
				view.getPolicySelector().setValue(response);
				
				FieldContainer container = broker.getContextForPolicy(response.id, response.hasExercises ? response.exerciseData[0].id : null);
				view.setTableValues(response.coverages, response.columns, container.columnFields);
				view.getCommonFieldsForm().setValue(container);
				
				//TODO CHILDRENPANEL
		//TODO PERMISSIONS
				
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				// TODO Auto-generated method stub
				
			}
		});	
	}

	private void transferToClient(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "transfertoclient");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSavePolicySuccess(String policyId){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice guardada com sucesso"), TYPE.TRAY_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("policyid", policyId);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onExecuteDetailedCalculations(){
		InsurancePolicy policy =  view.getPolicyHeaderForm().getValue();
		String policyId = policy == null ? null : policy.id;

		if(policyId != null) {
			broker.executeDetailedCalculations(policyId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					onExecuteDetailedCalculationsSuccess();
					NavigationHistoryManager.getInstance().reload();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onExecuteDetailedCalculationsFailed();
				}
			});
		}else {
			onGetPolicyFailed();
		}
	}
	private void onExecuteDetailedCalculationsSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os cálculos detalhados foram executados com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onExecuteDetailedCalculationsFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível executar os cálculos detalhados"), TYPE.ALERT_NOTIFICATION));
	}

	private void onGetPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a Apólice seleccionada"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("policyid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onValidatePolicy(){
		broker.validatePolicy(view.getPolicyHeaderForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onValidationSuccess();
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				for(ResponseError error : errors){
					onValidationFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
				}
			}
		});
	}
	
	private void onValidationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice foi validada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onValidationFailed(String message){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice falhou a validação :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onRequestCompanyInfo(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "companyinforequest");
		item.setParameter("ownerid", view.getPolicyHeaderForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onRequestClientInfo(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "clientinforequest");
		item.setParameter("ownerid", view.getPolicyHeaderForm().getValue().id);
		NavigationHistoryManager.getInstance().go(item);
	}

}
