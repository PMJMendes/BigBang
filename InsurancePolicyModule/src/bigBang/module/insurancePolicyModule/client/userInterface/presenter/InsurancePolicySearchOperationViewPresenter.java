package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySearchOperationViewPresenter implements
ViewPresenter {

	public static enum Action {
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE,
		CREATE_RECEIPT,
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
		CREATE_SUB_POLICY, ISSUE_CREDIT_NOTE,
		CREATE_NEGOTIATION, CREATE_HEALTH_EXPENSE,
		CREATE_RISK_ANALISYS
	}

	public interface Display {
		//Listtype filter text
		HasValueSelectables<InsurancePolicyStub> getList();
		void removeFromList(ValueSelectable<InsurancePolicyStub> selectable);
		void selectInsurancePolicy(String policyId);

		//Form
		HasEditableValue<InsurancePolicy> getForm();
		void scrollFormToTop();
		boolean isFormValid();

		HasValue<String> getInsuredObjectTableFilter();
		HasValue<String> getExerciseTableFilter();
		TableSection getCurrentTablePage();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowCreateReceipt(boolean allow);
		void allowCreateInsuredObject(boolean allow);
		void allowCreateExercise(boolean allow);
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
		void allowIssueCreditNote(boolean allow);
		void allowCreateNegotiation(boolean allow);
		void allowCreateHealthExpense(boolean allow);
		void allowCreateRiskAnalisys(boolean allow);

		//children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<InsuredObjectStub> getObjectsList();
		HasValueSelectables<ExerciseStub> getExercisesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected Display view;
	protected InsurancePolicyBroker broker;
	protected InsuredObjectDataBroker insuredObjectBroker;
	protected boolean bound = false;

	public InsurancePolicySearchOperationViewPresenter(Display view){
		this.broker = ((InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY));
		this.insuredObjectBroker = ((InsuredObjectDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.POLICY_INSURED_OBJECT));
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String policyId = parameterHolder.getParameter("id");
		policyId = policyId == null ? new String() : policyId;

		if(policyId.isEmpty()){
			clearView();
		}else{
			showPolicy(policyId);
		}
	}


	public void bind() {
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
					item.removeParameter("id");
				}else{
					item.setParameter("id", selectedPolicyId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		this.view.registerActionInvokedHandler(new ActionInvokedEventHandler<InsurancePolicySearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

				switch(action.getAction()){
				case EDIT:
					broker.openPolicyResource(view.getForm().getValue().id, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							showScratchPadPolicy(response);
						}
						@Override
						public void onError(Collection<ResponseError> errors) {
							onOpenPolicyResourceFailed();
						}
					});
					break;
				case CANCEL_EDIT:
					final String policyId = broker.getFinalMapping(view.getForm().getValue().id);
					broker.closePolicyResource(view.getForm().getValue().id, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.setParameter("id", policyId);
							NavigationHistoryManager.getInstance().go(item);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							broker.discardTemp(view.getForm().getValue().id);
							onClosePolicyResourceFailed();
							NavigationHistoryManager.getInstance().reload();
						}
					});
					break;
				case SAVE:
					InsurancePolicy info = view.getForm().getInfo();
					view.getForm().setReadOnly(true);
					savePolicy(info);
					break;
				case DELETE:
					item.setParameter("operation", "delete");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_RECEIPT:
					item.setParameter("operation", "createreceipt");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_EXERCISE:
					item.setParameter("operation", "createexercise");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INSURED_OBJECT:
					item.setParameter("operation", "createinsuredobject");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					item.setParameter("operation", "createinsuredobjectfromclient");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_HEALTH_EXPENSE:
					item.setParameter("operation", "createhealthexpense");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INFO_MANAGEMENT_PROCESS:
					item.setParameter("operation", "createinfomanagementprocess");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_NEGOTIATION:
					item.setParameter("operation", "createnegotiation");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_RISK_ANALISYS:
					item.setParameter("operation", "createriskanalisys");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_SUB_POLICY:
					item.setParameter("operation", "createsubpolicy");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_SUBSTITUTE_POLICY:
					item.setParameter("operation", "createsubstitutepolicy");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case EXECUTE_DETAILED_CALCULATIONS:
					//TODO
					break;
				case VALIDATE:
					onValidatePolicy();
					break;
				case ISSUE_CREDIT_NOTE:
					item.setParameter("operation", "issuecreditnote");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case REQUEST_AGENCY_INFO:
					item.setParameter("operation", "requestagencyinfo");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case REQUEST_CLIENT_INFO:
					item.setParameter("operation", "requestclientinfo");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_BROKERAGE:
					item.setParameter("operation", "brokeragetransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_MANAGER:
					item.setParameter("operation", "managertransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case VOID_POLICY:
					item.setParameter("operation", "voidpolicy");
					NavigationHistoryManager.getInstance().go(item);
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
		view.getObjectsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				InsuredObjectStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<InsuredObjectStub>)event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showInsuredObject(selectedValue);
				}
			}
		});
		view.getExercisesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ExerciseStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<ExerciseStub>)event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showExercise(selectedValue);
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

	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void showPolicy(String policyId){
		for(ValueSelectable<InsurancePolicyStub> entry : view.getList().getAll()){
			InsurancePolicyStub listPolicy = entry.getValue();
			if(listPolicy.id.equalsIgnoreCase(policyId) && !entry.isSelected()){
				view.selectInsurancePolicy(policyId);
				break;
			}
		}

		this.broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY));
				view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.DELETE_POLICY));
				view.allowCreateExercise(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_EXERCISE));
				view.allowCreateInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_INSURED_OBJECT));
				view.allowCreateReceipt(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT));
				view.allowValidatePolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.VALIDATE_POLICY));
				view.allowVoidPolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.VOID_POLICY));
				view.allowTransferBrokerage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_BROKERAGE));
				view.allowCreateSubstitutePolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUBSTITUTE_POLICY));
				view.allowRequestClientInfo(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_CLIENT_INFO_REQUEST));
				view.allowRequestAgencyInfo(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_COMPANY_INFO_REQUEST));
				view.allowCreateInsuredObjectFromClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_INSURED_OBJECT_FROM_CLIENT));
				view.allowTransferManager(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER));
				view.allowExecuteDetailedCalculations(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.EXECUTE_DETAILED_CALCULATIONS));
				view.allowCreateInfoManagementProcess(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_INFO_MANAGEMENT_PROCESS));
				view.allowCreateSubPolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUB_POLICY));
				view.allowIssueCreditNote(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_CREDIT_NOTE));
				view.allowCreateNegotiation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_NEGOTIATION));
				view.allowCreateHealthExpense(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE));
				view.allowCreateRiskAnalisys(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RISK_ANALISYS));

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetPolicyFailed();
			}
		});
	}

	private void showScratchPadPolicy(InsurancePolicy policy){
		view.clearAllowedPermissions();

		boolean hasPermissions = true; //TODO IMPORTANT FJVC
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);
		view.allowDelete(false);
		view.allowCreateExercise(false);
		view.allowCreateInsuredObject(false);
		view.allowCreateReceipt(false);
		view.allowVoidPolicy(false);
		view.allowTransferBrokerage(false);
		view.allowCreateSubstitutePolicy(false);
		view.allowRequestClientInfo(false);
		view.allowRequestAgencyInfo(false);
		view.allowCreateInsuredObjectFromClient(false);
		view.allowTransferManager(false);
		view.allowExecuteDetailedCalculations(false);
		view.allowCreateInfoManagementProcess(false);
		view.allowCreateSubPolicy(false);
		view.allowIssueCreditNote(false);
		view.allowCreateNegotiation(false);
		view.allowCreateHealthExpense(false);
		view.allowCreateRiskAnalisys(false);

		view.setSaveModeEnabled(true);
		view.getForm().setValue(policy);
		view.getForm().setReadOnly(false);
	}


	private void savePolicy(final InsurancePolicy policy){
		this.broker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(final InsurancePolicy policyResponse) {

				broker.saveCoverageDetailsPage(policy.id, view.getInsuredObjectTableFilter().getValue(), view.getExerciseTableFilter().getValue(), view.getCurrentTablePage(), new ResponseHandler<TableSection>() {

					@Override
					public void onResponse(TableSection response) {
						broker.commitPolicy(policyResponse, new ResponseHandler<InsurancePolicy>() {

							@Override
							public void onResponse(InsurancePolicy response) {
								NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
								item.setParameter("id", response.id);
								NavigationHistoryManager.getInstance().go(item);
								EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onSavePolicyFailed();
							}
						});
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onSavePolicyFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSavePolicyFailed();
			}
		});
	}

	private void onValidatePolicy(){
		broker.validatePolicy(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onValidationSuccess();
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				for(ResponseError error : errors){
					onValidationFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
					break;
				}
			}
		});
	}

	private void showContact(Contact contact) {
		//TODO
	}

	private void showDocument(Document document){
		//TODO
	}

	private void onValidationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice foi validada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onValidationFailed(String message){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice falhou a validação :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
	}

	private void onGetPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a Apólice seleccionada"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("id");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSavePolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações à Apólice"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onOpenPolicyResourceFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Neste momento não é possível editar a Apólice"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void onClosePolicyResourceFailed(){
		GWT.log("Could not close a policy resource : " + NavigationHistoryManager.getInstance().getCurrentState());
	}

	private void showInsuredObject(InsuredObjectStub object) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("operation", "viewinsuredobject");
		item.setParameter("id", view.getForm().getValue().id);
		item.setParameter("objectid", object.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showExercise(ExerciseStub exercise) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("operation", "viewexercise");
		item.setParameter("id", view.getForm().getValue().id);
		item.setParameter("exerciseid", exercise.id);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void showHistory(HistoryItemStub historyItem) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("operation", "history");
		item.setParameter("historyitemid", historyItem.id);
		NavigationHistoryManager.getInstance().go(item);
	}

}
