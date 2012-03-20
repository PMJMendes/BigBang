package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuredObjectDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SubPolicyStub;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
		CREATE_SUB_POLICY, ISSUE_DEBIT_NOTE,
		CREATE_NEGOTIATION, CREATE_HEALTH_EXPENSE,
		CREATE_RISK_ANALISYS, TRANSFER_TO_CLIENT
	}

	public interface Display {
		//Listtype filter text
		HasValueSelectables<InsurancePolicyStub> getList();

		//Form
		HasEditableValue<InsurancePolicy> getForm();
		void scrollFormToTop();
		boolean isFormValid();

		HasValue<String> getInsuredObjectTableFilter();
		HasValue<String> getExerciseTableFilter();
		HasValue<TableSection> getCoverageTable();

		//Permissions
		void clearAllowedPermissions();
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void allowCreateReceipt(boolean allow);
		void allowCreateInsuredObject(boolean allow);
		void allowIncludeInsuredObject(boolean allow);
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
		void allowIssueDebitNote(boolean allow);
		void allowCreateNegotiation(boolean allow);
		void allowCreateHealthExpense(boolean allow);
		void allowCreateRiskAnalisys(boolean allow);
		void allowTransferToClient(boolean allow);

		//children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<InsuredObjectStub> getObjectsList();
		HasValueSelectables<ExerciseStub> getExercisesList();
		HasValueSelectables<SubPolicyStub> getSubPoliciesList();
		HasValueSelectables<ReceiptStub> getReceiptsList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);
		void confirm(String message, ResponseHandler<Boolean> handler);

		Widget asWidget();
	}

	protected Display view;
	protected InsurancePolicyBroker broker;
	protected InsuredObjectDataBroker insuredObjectBroker;
	protected String currentInsuredObjectFilterId, currentExerciseFilterId;
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
		String policyId = parameterHolder.getParameter("policyid");
		checkStatus(policyId);
		policyId = policyId == null ? new String() : policyId;

		setup();

		if(policyId.isEmpty()){
			clearView();
		}else if(this.broker.isTemp(policyId)){
			showScratchPadPolicy(policyId);
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
					item.removeParameter("policyid");
				}else{
					item.setParameter("policyid", selectedPolicyId);
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
							NavigationHistoryManager.getInstance().reload();
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
							item.setParameter("policyid", policyId);
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
					item.pushIntoStackParameter("display", "delete");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_RECEIPT:
					item.pushIntoStackParameter("display", "createreceipt");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_EXERCISE:
					item.pushIntoStackParameter("display", "viewexercise");
					item.setParameter("exerciseid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case INCLUDE_INSURED_OBJECT:
					item.pushIntoStackParameter("display", "includeinsuredobject");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INSURED_OBJECT:
					item.pushIntoStackParameter("display", "viewinsuredobject");
					item.setParameter("objectid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					item.pushIntoStackParameter("display", "createinsuredobjectfromclient");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case CREATE_HEALTH_EXPENSE:
					item.pushIntoStackParameter("display", "createhealthexpense");
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
					item.setParameter("policyid", view.getForm().getValue().id);
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
					item.pushIntoStackParameter("display", "requestagencyinfo");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case REQUEST_CLIENT_INFO:
					item.pushIntoStackParameter("display", "requestclientinfo");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_BROKERAGE:
					item.pushIntoStackParameter("display", "brokeragetransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case TRANSFER_MANAGER:
					item.pushIntoStackParameter("display", "managertransfer");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case VOID_POLICY:
					onVoidPolicy();
					break;

				case TRANSFER_TO_CLIENT:{
					transferToClient();
					break;
				}
				}
			}
		});
		view.getInsuredObjectTableFilter().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onTableFiltersChanged();
			}
		});
		view.getExerciseTableFilter().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onTableFiltersChanged();
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

	private void checkStatus(String nextPolicyId){
		InsurancePolicy policy = view.getForm().getValue();
		if(policy != null && policy.id != null){
			if((nextPolicyId == null || !nextPolicyId.equalsIgnoreCase(policy.id)) && broker.isTemp(policy.id)){
				broker.closePolicyResource(policy.id, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						return;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						return;
					}
				});
			}
		}
	}

	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
		view.getContactsList().clearSelection();
		view.getDocumentsList().clearSelection();
		view.getHistoryList().clearSelection();
		view.getExercisesList().clearSelection();
		view.getObjectsList().clearSelection();
	}

	private void setup(){
		view.getContactsList().clearSelection();
		view.getDocumentsList().clearSelection();
		view.getHistoryList().clearSelection();
		view.getExercisesList().clearSelection();
		view.getObjectsList().clearSelection();
	}

	private void showPolicy(String policyId){
		if(broker.isTemp(policyId)){
			showScratchPadPolicy(policyId);
		}else{

			this.currentExerciseFilterId = null;
			this.currentInsuredObjectFilterId = null;

			for(ValueSelectable<InsurancePolicyStub> entry : view.getList().getAll()){
				InsurancePolicyStub listPolicy = entry.getValue();
				if(listPolicy.id.equalsIgnoreCase(policyId)){
					entry.setSelected(true, false);
				}else if(entry.isSelected()){
					entry.setSelected(false, false);
				}
			}

			this.broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY));
					view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.DELETE_POLICY));
					view.allowCreateExercise(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_EXERCISE));
					view.allowIncludeInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.INCLUDE_INSURED_OBJECT));
					view.allowCreateInsuredObject(broker.isTemp(response.id));
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
					view.allowIssueDebitNote(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_DEBIT_NOTE));
					view.allowCreateNegotiation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_NEGOTIATION));
					view.allowCreateHealthExpense(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE));
					view.allowCreateRiskAnalisys(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RISK_ANALISYS));
					view.allowTransferToClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_TO_CLIENT));


					view.setSaveModeEnabled(false);
					view.getForm().setReadOnly(true);
					view.getForm().setValue(response);

					broker.getPage(response.id, view.getInsuredObjectTableFilter().getValue(), view.getExerciseTableFilter().getValue(), new ResponseHandler<InsurancePolicy.TableSection>() {

						@Override
						public void onResponse(TableSection response) {
							view.getCoverageTable().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetPageFailed();
						}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetPolicyFailed();
				}
			});

		}
	}

	private void showScratchPadPolicy(final String policyId){
		if(broker.isTemp(policyId)){
			broker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.clearAllowedPermissions();

					view.allowEdit(true);
					view.allowDelete(true);
					view.allowCreateExercise(true);
					view.allowCreateInsuredObject(true);

					view.setSaveModeEnabled(true);
					view.getForm().setValue(response);

					view.getForm().setReadOnly(false);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onOpenPolicyResourceFailed();
					broker.closePolicyResource(policyId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							NavigationHistoryManager.getInstance().reload();
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onClosePolicyResourceFailed();
						}
					});
				}
			});
		}else{
			showPolicy(policyId);
		}
	}

	protected void saveWorkState(final ResponseHandler<Void> handler){
		final InsurancePolicy policy = view.getForm().getInfo(); 
		if(broker.isTemp(policy.id)){
			broker.updatePolicy(policy, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					broker.saveCoverageDetailsPage(policy.id, currentInsuredObjectFilterId, currentExerciseFilterId, view.getCoverageTable().getValue(), new ResponseHandler<TableSection>(){

						@Override
						public void onResponse(TableSection response) {
							handler.onResponse(null);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							handler.onError(new String[]{});
						}

					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					handler.onError(new String[]{});
				}
			});
		} else {
			handler.onResponse(null);
		}
	}


	private void savePolicy(final InsurancePolicy policy){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				broker.commitPolicy(policy, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy response) {
						onSavePolicySuccess();
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
				}
			}
		});
	}

	private void onExecuteDetailedCalculations(){
		InsurancePolicy policy =  view.getForm().getValue();
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

	private void onVoidPolicy(){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("show", "voidpolicy");
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showContact(final Contact contact) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("show", "contactmanagement");
				item.setParameter("ownerid", contact.ownerId);
				item.setParameter("ownertypeid", contact.ownerTypeId);
				item.setParameter("contactid", contact.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showDocument(final Document document){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("show", "documentmanagement");
				item.setParameter("ownerid", document.ownerId);
				item.setParameter("ownertypeid", document.ownerTypeId);
				item.setParameter("documentid", document.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showSubProcess(final BigBangProcess process){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				String type = process.dataTypeId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

				if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
					item.pushIntoStackParameter("display", "negotiation");
					item.setParameter("negotiationid", process.dataId);
					NavigationHistoryManager.getInstance().go(item);
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void onTableFiltersChanged(){
		final String policyId = view.getForm().getValue().id;
		final String objectFilterId = view.getInsuredObjectTableFilter().getValue();
		final String exerciseFilterId =  view.getExerciseTableFilter().getValue();

		if(broker.isTemp(policyId)){
			broker.saveCoverageDetailsPage(policyId, currentInsuredObjectFilterId, currentExerciseFilterId, view.getCoverageTable().getValue(), new ResponseHandler<InsurancePolicy.TableSection>() {

				@Override
				public void onResponse(TableSection response) {
					broker.getPage(policyId, objectFilterId, exerciseFilterId, new ResponseHandler<InsurancePolicy.TableSection>() {

						@Override
						public void onResponse(TableSection response) {
							view.getCoverageTable().setValue(response);
							currentInsuredObjectFilterId = objectFilterId;
							currentExerciseFilterId = exerciseFilterId;
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetPageFailed();
						}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onSavePolicyFailed();
				}
			});
		}else{
			broker.getPage(policyId, objectFilterId, exerciseFilterId, new ResponseHandler<InsurancePolicy.TableSection>() {

				@Override
				public void onResponse(TableSection response) {
					view.getCoverageTable().setValue(response);
					currentInsuredObjectFilterId = objectFilterId;
					currentExerciseFilterId = exerciseFilterId;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetPageFailed();
				}
			});
		}
	}

	private void onValidationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice foi validada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onValidationFailed(String message){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice falhou a validação :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
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

	private void showInsuredObject(final InsuredObjectStub object) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "viewinsuredobject");
				item.setParameter("policyid", view.getForm().getValue().id);
				item.setParameter("objectid", object.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showExercise(final ExerciseStub exercise) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "viewexercise");
				item.setParameter("policyid", view.getForm().getValue().id);
				item.setParameter("exerciseid", exercise.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showSubPolicy(final SubPolicyStub subPolicy){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "subpolicy");
				item.setParameter("policyid", view.getForm().getValue().id);
				item.setParameter("subpolicyid", subPolicy.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}
	
	private void showReceipt(final ReceiptStub receipt){
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "receipt");
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "search");
				item.setParameter("receiptid", receipt.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void showHistory(final HistoryItemStub historyItem) {
		saveWorkState(new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.pushIntoStackParameter("display", "history");
				item.setParameter("historyownerid", view.getForm().getValue().id);
				item.setParameter("historyitemid", historyItem.id);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(null);
			}
		});
	}

	private void onGetPageFailed(){
		view.getInsuredObjectTableFilter().setValue(currentInsuredObjectFilterId, false);
		view.getExerciseTableFilter().setValue(currentExerciseFilterId, false);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível apresentar a página na tabela de coberturas"), TYPE.ALERT_NOTIFICATION));
	}

	private void onSavePolicySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice guardada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void transferToClient(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "transfertoclient");
		NavigationHistoryManager.getInstance().go(item);
	}

}
