package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.InsuredObjectStub.Change;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
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
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel.Entry;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySearchOperationViewPresenter implements ViewPresenter{

	public static class Services
	{
		public static final InsurancePolicyServiceAsync insurancePolicyService =
				GWT.create(InsurancePolicyService.class);
	}

	public static enum Action {
		ON_POLICY_SELECTED,
		NEW_EXERCISE,
		NEW_INSURED_OBJECT,
		DELETE_INSURED_OBJECT,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE,
		CREATE_RECEIPT,
		INCLUDE_INSURED_OBJECT,
		VOID_POLICY,
		TRANSFER_BROKERAGE,
		CREATE_SUBSTITUTE_POLICY,
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
		ON_NEW_RESULTS, CREATE_SUB_POLICY_RECEIPT, RECEIVE_MESSAGE, SEND_MESSAGE
	}
	public interface Display {
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		HasValueSelectables<InsurancePolicyStub> getList();
		HasEditableValue<InsurancePolicy> getPolicyHeaderForm();
		HasEditableValue<InsuredObject> getInsuredObjectHeaderForm();
		void allowDeleteInsuredObject(boolean allow);
		HasEditableValue<FieldContainer> getCommonFieldsForm();

		HasEditableValue<ComplexFieldContainer.ExerciseData> getExerciseForm();
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
		void allowSendMessage(boolean allow);
		void allowReceiveMessage(boolean allow);

		//children lists
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();
		HasValueSelectables<SubPolicyStub> getSubPoliciesList();
		HasValueSelectables<ReceiptStub> getReceiptsList();
		HasValueSelectables<ExpenseStub> getExpensesList();
		HasValueSelectables<HistoryItemStub> getHistoryList();
		HasValueSelectables<BigBangProcess> getSubProcessesList();
		HasValueSelectables<ConversationStub> getConversationList();


		Widget asWidget();

		void setAvailableExercises(ComplexFieldContainer.ExerciseData[] exerciseData);

		HasValue<InsurancePolicyStub> getPolicySelector();

		void setHeaders(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] columns);

		void setCoveragesExtraFields(StructuredFieldContainer.Coverage[] coverages);

		void setReadOnly(boolean b);

		void showObjectForm(boolean b);

		void showPolicyForm(boolean b);

		void clearPolicySelection();

		void setToolbarEditMode(boolean b);

		void lockToolbar();

		void setPolicyEntrySelected(boolean b);

		void setExerciseFieldsHeader(String header);

		Coverage[] getPresentCoverages();

		void dealWithObject(InsuredObjectStub response);

		HasClickHandlers getObjectDeleteButton();

		void setSelectedObject(String id);

		void focusInsuredObjectForm();

		void setNotesReadOnly(boolean b);

		void clearObjectsList();

		void clearPolicyList();

		void setOwner(InsurancePolicy policy);

		void allowManagerChange(boolean b);

		void setObjectListOwner(String policyId);

		void addEntryToList(Entry entry);

		void removeElementFromList(ValueSelectable<InsurancePolicyStub> stub);

		void doSearch(boolean b);

		void setInvalidObjectEntry(ValueSelectable<InsuredObjectStub> entry,
				boolean validate);

		void setInvalidPolicySelector(boolean b);

		void showExerciseForm(boolean b);

		void allowCreateSubPolicyReceipt(boolean allow);
	}

	private InsurancePolicyBroker broker;
	private Display view;
	private boolean bound;
	private boolean onPolicy = true;
	private String policyId;
	private boolean isEditModeEnabled;
	private ExerciseData[] availableExercises;

	public InsurancePolicySearchOperationViewPresenter(Display view){
		this.broker = (InsurancePolicyBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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
				if(selected != null && selected.getValue().id.equals("new")){
					return;
				}
				removeNewListEntry();
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
					NavigationHistoryManager.getInstance().reload(); //TODO QUANDO SE CANCELA NOVO está um onCancelEdit lá em baixo comentado.
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
				case INCLUDE_INSURED_OBJECT:
					item.pushIntoStackParameter("display", "includeinsuredobject");
					NavigationHistoryManager.getInstance().go(item);
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
					item.setParameter("policyid", policyId);
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
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SEND_MESSAGE:
					onSendMessage();
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
				case ON_POLICY_SELECTED:
					onPolicySelected();
					break;
				case DELETE_INSURED_OBJECT:
					onDeleteInsuredObject();
					break;
				case NEW_EXERCISE:
					onNewExercise();
					break;
				case NEW_INSURED_OBJECT:
					onNewInsuredObject();
					break;
				case ON_NEW_RESULTS:
					onNewResults();
					break;
				case CREATE_SUB_POLICY_RECEIPT:
					onCreateSubPolicyReceipt();
					break;
				}

			}

		});

		view.getExerciseSelector().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onExerciseChanged(event.getValue());
			}
		});

		view.getInsuredObjectsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				validateCurrentObject();
				validateCurrentPolicy();
				if(event.getFirstSelected() != null){
					InsuredObjectStub stub = ((ValueSelectable<InsuredObjectStub>)event.getFirstSelected()).getValue();
					if(stub.change != Change.DELETED){
						onObjectSelected(stub);
					}
					else{
						if(onPolicy){
							view.getInsuredObjectsList().clearSelection();
						}else{
							view.setSelectedObject(view.getInsuredObjectHeaderForm().getValue().id);
						}
					}
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
		view.getObjectDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onDeleteInsuredObject();
			}
		});
		
		view.getConversationList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ConversationStub stub = event.getFirstSelected() == null ? null : ((ValueSelectable<ConversationStub>) event.getFirstSelected()).getValue();
				if(stub != null){
					showConversation(stub.id);
				}
			}
		});

		//APPLICATION-WIDE EVENTS
		this.bound = true;
	}



	protected void showConversation(String id) {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "conversation");
		item.setParameter("conversationid", id);
		NavigationHistoryManager.getInstance().go(item);			
	}


	protected void onCreateSubPolicyReceipt() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "createsubpolicyreceipts");
		NavigationHistoryManager.getInstance().go(item);
	}


	protected void onNewResults() {
		InsurancePolicy pol = view.getPolicyHeaderForm().getInfo();
		if(pol != null && pol.id != null) {
			ensureListedAndSelected(pol);
		}
	}


	private void ensureListedAndSelected(InsurancePolicy pol) {

		boolean found = false;
		for(ValueSelectable<InsurancePolicyStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equals(pol.id)){
				found  = true;
				stub.setSelected(true, false);
			}
			else{
				stub.setSelected(false,false);
			}
		}
		if(!found){
			InsurancePolicySearchPanel.Entry entry = new InsurancePolicySearchPanel.Entry(pol);
			view.addEntryToList(entry);
		}

		if(view.getList().getAll().size() > 0 && !policyId.equals("new")){
			removeNewListEntry();
		}

	}


	protected void onExerciseChanged(String string) {

		if(string == null || string.isEmpty() || string.equals("forced")){
			return;
		}


		if(isEditModeEnabled){
			broker.updateExercise(policyId, view.getExerciseForm().getInfo());
			if(onPolicy){
				broker.saveContextForPolicy(policyId, 
						getCurrentExerciseId(),
						view.getCommonFieldsForm().getInfo());
			}else{
				broker.saveContextForInsuredObject(policyId, 
						view.getInsuredObjectHeaderForm().getValue().id, getCurrentExerciseId(), 
						view.getCommonFieldsForm().getInfo());
			}
		}

		if(onPolicy){
			view.getCommonFieldsForm().setValue(broker.getContextForPolicy(policyId, string));
		}else{
			view.getCommonFieldsForm().setValue(broker.getContextForInsuredObject(policyId, view.getInsuredObjectHeaderForm().getValue().id, string));
		}

		for(int i = 0; i<availableExercises.length; i++){
			if(availableExercises[i].id.equalsIgnoreCase(getCurrentExerciseId())){
				availableExercises[i] = view.getExerciseForm().getInfo();
			}else if(availableExercises[i].id.equalsIgnoreCase(string)){
				view.getExerciseForm().setValue(availableExercises[i]);
			}
		}
	}


	protected void onObjectSelected(InsuredObjectStub object) {

		if(onPolicy){
			view.clearPolicySelection();
		}
		saveInternally();

		fillObject(object.id);

		onPolicy = false;
	}


	protected void onDeleteInsuredObject() {
		view.dealWithObject(broker.removeInsuredObject(policyId, view.getInsuredObjectHeaderForm().getValue().id));
		onPolicySelected();
	}


	protected void onNewInsuredObject() {
		if(onPolicy){
			view.clearPolicySelection();
		}
		saveInternally();

		fillObject(null);
		onPolicy = false;
	}


	protected void onNewExercise() {

		ExerciseData[] newArray = new ExerciseData[availableExercises.length+1];

		newArray[0] = broker.createExercise(policyId);

		for(int i = 1; i<newArray.length; i++){
			newArray[i] = availableExercises[i-1];
		}

		availableExercises = newArray;
		view.allowCreateNewExercise(false);
		fillExercise(availableExercises);
	}


	protected void onPolicySelected() {
		if (onPolicy)
			return;
		saveInternally();
		fillPolicy();
	}

	private String getCurrentExerciseId() {
		ComplexFieldContainer.ExerciseData ex = view.getExerciseForm().getValue();
		return  ex != null ? ex.id : null;
	}

	private void saveInternally() {
		if(!isEditModeEnabled){
			return;
		}
		if(!onPolicy){
			broker.updateInsuredObject(policyId, view.getInsuredObjectHeaderForm().getInfo());
			broker.saveContextForInsuredObject(policyId, 
					view.getInsuredObjectHeaderForm().getValue().id, getCurrentExerciseId(), 
					view.getCommonFieldsForm().getInfo());
			view.dealWithObject(view.getInsuredObjectHeaderForm().getInfo());
		}
		else{
			InsurancePolicy changedPolicy = view.getPolicyHeaderForm().getInfo();
			changedPolicy.notes = view.getPolicyNotesForm().getValue();
			broker.updatePolicyHeader(changedPolicy);
			broker.saveContextForPolicy(policyId, 
					getCurrentExerciseId(),
					view.getCommonFieldsForm().getInfo());
		}
	}


	protected void showExpense(ExpenseStub selectedValue) {
		NavigationHistoryItem item = new NavigationHistoryItem();
		item.setParameter("section", "expense");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		item.setParameter("expenseid", selectedValue.id);
		NavigationHistoryManager.getInstance().go(item);
	}


	protected void showSubProcess(BigBangProcess selectedValue) {
		String type = selectedValue.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MANAGER_TRANSFER)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewmanagertransfer");
			item.setParameter("transferid", selectedValue.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.CONVERSATION)){
			showConversation(selectedValue.dataId);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.NEGOTIATION)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "negotiation");
			item.setParameter("negotiationid", selectedValue.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}
	}


	protected void showHistory(HistoryItemStub selectedValue) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", policyId);
		navItem.setParameter("historyItemId", selectedValue.id);
		NavigationHistoryManager.getInstance().go(navItem);
	}


	protected void showReceipt(ReceiptStub selectedValue) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("section", "receipt");
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display", "search"); 
		navItem.setParameter("receiptid", selectedValue.id);
		NavigationHistoryManager.getInstance().go(navItem);
	}


	protected void showSubPolicy(SubPolicyStub selectedValue) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("subpolicyid", selectedValue.id);
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display","search");
		navItem.pushIntoStackParameter("display","subpolicy");
		NavigationHistoryManager.getInstance().go(navItem);

	}


	protected void showDocument(Document selectedValue) {
		saveInternally();
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "documentmanagement");
		navItem.setParameter("ownerid", selectedValue.ownerId);
		navItem.setParameter("documentid", selectedValue.id);
		navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.INSURANCE_POLICY);
		NavigationHistoryManager.getInstance().go(navItem);
	}


	protected void showContact(Contact selectedValue) {
		saveInternally();
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "contactmanagement");
		navItem.setParameter("ownerid", selectedValue.ownerId);
		navItem.setParameter("contactid", selectedValue.id);
		navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.INSURANCE_POLICY);
		NavigationHistoryManager.getInstance().go(navItem);
	}


	protected void onVoidPolicy() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "voidpolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDelete() {
		broker.removePolicy(policyId, new ResponseHandler<String>() {

			@Override
			public void onResponse(String response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice eliminada com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter(policyId);
				NavigationHistoryManager.getInstance().go(item);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a apólice"), TYPE.ALERT_NOTIFICATION));					
			}
		});
	}

	protected void onEdit() {
		view.setReadOnly(false);
		isEditModeEnabled = true;
		view.setToolbarEditMode(true);
	}

	protected void onSave() {
		saveInternally();
		broker.updateExercise(policyId, view.getExerciseForm().getInfo());
		broker.updateCoverages(view.getPresentCoverages());
		if(view.getPolicyHeaderForm().validate()) {
			broker.persistPolicy(policyId,new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					if(policyId.equalsIgnoreCase("new")){
						removeNewListEntry();
					}
					onSavePolicySuccess(response.id);
					isEditModeEnabled = false;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					for(ResponseError error : errors){
						if ( ResponseError.ErrorLevel.USER.equals(error.code) )
							onValidationFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
						else
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar a apólice"), TYPE.ALERT_NOTIFICATION));
					}
				}
			});
		}else{
			onFormValidationFailed();
		}
	}

	protected void removeNewListEntry() {
		for(ValueSelectable<InsurancePolicyStub> stub : view.getList().getAll()){
			if(stub.getValue().id.equalsIgnoreCase("new")){
				view.removeElementFromList(stub);
				return;
			}
		}
	}

	//
	//	protected void onCancelEdit() {
	//		if(policyId.equalsIgnoreCase("new")){
	//			NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
	//			navig.setParameter("section", "client");
	//			navig.removeParameter("policyid");
	//			navig.removeParameter("sublineid");
	//			NavigationHistoryManager.getInstance().go(navig);
	//			removeNewListEntry();
	//			return;
	//		}
	//		view.setReadOnly(true);
	//		broker.discardEditData(policyId);
	//		revert();
	//		view.setToolbarEditMode(false);
	//		isEditModeEnabled = false;
	//	}
	//
	//
	//
	//	private void revert() {
	//
	//		view.setObjectListOwner(policyId);
	//
	//		if(onPolicy || view.getInsuredObjectHeaderForm().getValue().change == Change.CREATED){
	//			fillPolicy();
	//		}
	//		else{
	//			fillObject(view.getInsuredObjectHeaderForm().getValue().id);
	//		}
	//
	//	}


	private void fillObject(String id) {
		if(id == null){
			InsuredObject newInsuredObject = broker.createInsuredObject(policyId);
			newInsuredObject.unitIdentification = "Nova Unidade de Risco";
			view.getInsuredObjectHeaderForm().setValue(newInsuredObject);
			view.getCommonFieldsForm().setValue(broker.getContextForInsuredObject(policyId, newInsuredObject.id, getCurrentExerciseId()));
			view.showObjectForm(true);
			view.showPolicyForm(false);
			view.setNotesReadOnly(true);
			view.dealWithObject(newInsuredObject);
			if(view.getExerciseSelector().getValue() != null && view.getExerciseForm().getValue() != null){
				view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Unidade de Risco: " + newInsuredObject.unitIdentification);	
			}
			view.focusInsuredObjectForm();
			view.getInsuredObjectHeaderForm().validate();
		}else{

			broker.getInsuredObject(policyId, id, new ResponseHandler<InsuredObject>() {

				@Override
				public void onResponse(InsuredObject response) {
					view.getInsuredObjectHeaderForm().setValue(response);
					view.getCommonFieldsForm().setValue(broker.getContextForInsuredObject(policyId, response.id, getCurrentExerciseId()));
					view.showObjectForm(true);
					view.showPolicyForm(false);
					view.dealWithObject(response);
					view.setNotesReadOnly(true);
					if(view.getExerciseSelector().getValue() != null && view.getExerciseForm().getValue() != null){
						view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Unidade de Risco: " + response.unitIdentification);	
					}
					view.getInsuredObjectHeaderForm().validate();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o objecto"), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
	}


	@Override
	public void setParameters(HasParameters parameterHolder) {

		policyId = parameterHolder.getParameter("policyid");

		clearListSelectionNoFireEvent();

		if(policyId != null){
			view.setInvalidPolicySelector(false);
			if(policyId.equals("new")){
				String subLineId = parameterHolder.getParameter("sublineid");
				String clientId = parameterHolder.getParameter("clientid");
				broker.getEmptyPolicy(subLineId, clientId, new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy response) {
						isEditModeEnabled = true;
						view.setToolbarEditMode(true);
						view.allowEdit(true);
						view.allowManagerChange(true);
						view.getPolicySelector().setValue(response);
						view.setCoveragesExtraFields(response.coverages);
						view.setHeaders(response.coverages, response.columns);
						setExercises(response.exerciseData);
						view.setOwner(null);
						view.clearObjectsList();
						view.getPolicyNotesForm().setValue(response.notes);
						fillPolicy();
						view.setReadOnly(false);		
						view.allowCreateNewExercise(true);
						ensureListedAndSelected(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar uma nova apólice"), TYPE.ALERT_NOTIFICATION));
					}
				});
			}else{
				broker.getPolicy(policyId,new ResponseHandler<InsurancePolicy>() {

					@Override
					public void onResponse(InsurancePolicy response) {
						isEditModeEnabled = false;
						view.setOwner(response);
						view.setToolbarEditMode(false);
						view.allowManagerChange(false);
						view.getPolicySelector().setValue(response);
						view.setCoveragesExtraFields(response.coverages);
						view.setHeaders(response.coverages, response.columns);
						setExercises(response.exerciseData);
						setPermissions(response);
						view.getPolicyNotesForm().setValue(response.notes);
						fillPolicy();
						view.setReadOnly(true);		
						view.allowCreateNewExercise(true);
						ensureListedAndSelected(response);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice"), TYPE.ALERT_NOTIFICATION));
					}
				});	
			}
		}
		else{
			view.lockToolbar();
			view.getCommonFieldsForm().setValue(null);
			view.getExerciseForm().setValue(null);
			view.getExerciseSelector().setValue(null);
			view.setOwner(null);
			view.setAvailableExercises(null);
			view.showExerciseForm(false);
			view.setCoveragesExtraFields(null);
			view.getPolicySelector().setValue(null);
			view.getPolicyNotesForm().setValue(null);
			view.getPolicyHeaderForm().setValue(null);
			view.getInsuredObjectHeaderForm().setValue(null);
			view.clearObjectsList();
			view.clearPolicySelection();
			view.setReadOnly(true);
			view.setPolicyEntrySelected(false);
		}

	}

	private void clearListSelectionNoFireEvent() {

		if(view.getPolicyHeaderForm() != null && view.getPolicyHeaderForm().getValue() != null && view.getPolicyHeaderForm().getValue().id != null){
			for(ValueSelectable<InsurancePolicyStub> stub : view.getList().getSelected()){
				if(!stub.getValue().id.equals(policyId)){
					stub.setSelected(false, false);
					return;
				}

			}
		}
	}


	protected void setExercises(ExerciseData[] exerciseData) {

		if(exerciseData == null){
			view.setAvailableExercises(new ExerciseData[0]);
			view.getExerciseForm().setValue(null);
			view.showExerciseForm(false);
			view.getExerciseSelector().setValue(null);
			return;
		}

		int start = exerciseData[0].isActive ? 0 : 1; //FIRST ONE IS THE NEW ONE 

		availableExercises = new ExerciseData[exerciseData.length - start];

		for(int i = start; i<exerciseData.length; i++)
			availableExercises[i - start] = exerciseData[i];

		fillExercise(availableExercises);
	}


	private void fillExercise(ExerciseData[] exercises) {
		view.setAvailableExercises(exercises);
		view.getExerciseForm().setValue(exercises[0], false);
		view.showExerciseForm(true);
		view.getExerciseSelector().setValue(exercises[0].id, false);
	}


	private void fillPolicy() {
		InsurancePolicy pol = broker.getPolicyHeader(policyId);
		view.getInsuredObjectsList().clearSelection();
		view.getPolicyHeaderForm().setValue(pol);
		view.getCommonFieldsForm().setValue(broker.getContextForPolicy(policyId, getCurrentExerciseId()));
		view.showObjectForm(false);
		view.showPolicyForm(true);
		view.setNotesReadOnly(false);
		view.setPolicyEntrySelected(true);
		if(view.getExerciseForm().getValue() != null){
			view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Apólice");	
		}
		onPolicy = true;
		view.getPolicyHeaderForm().validate();
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
					for(ResponseError error : errors){
						onExecuteDetailedCalculationsFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
					}
				}
			});
		}else {
			onGetPolicyFailed();
		}
	}

	private void onExecuteDetailedCalculationsSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os cálculos detalhados foram executados com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	private void onExecuteDetailedCalculationsFailed(String message){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os cálculos detalhados falharam :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
	}

	private void onGetPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a Apólice seleccionada"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("policyid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onValidatePolicy(){
		broker.validatePolicy(policyId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onValidationSuccess();
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				for(ResponseError error : errors){
					if ( ResponseError.ErrorLevel.USER.equals(error.code) )
						onValidationFailed(error.description.replaceAll("(\r\n|\n)", "<br />"));
					else
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível validar a apólice"), TYPE.ALERT_NOTIFICATION));					
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

	private void onReceiveMessage(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "receivemessage");
		item.setParameter("ownerid", policyId);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onSendMessage(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "sendmessage");
		item.setParameter("ownerid", policyId);
		NavigationHistoryManager.getInstance().go(item);
	}

	private void setPermissions(InsurancePolicy response){

		view.allowCreateHealthExpense(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_HEALTH_EXPENSE));
		view.allowCreateInfoManagementProcess(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_INFO_MANAGEMENT_PROCESS));
		view.allowCreateInsuredObjectFromClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_INSURED_OBJECT_FROM_CLIENT));
		view.allowEdit(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.UPDATE_POLICY));
		view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.DELETE_POLICY));
		view.allowCreateReceipt(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT));
		view.allowIncludeInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.INCLUDE_INSURED_OBJECT));
		view.allowValidatePolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.VALIDATE_POLICY));
		view.allowVoidPolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.VOID_POLICY));
		view.allowTransferBrokerage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_BROKERAGE));
		view.allowCreateSubstitutePolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUBSTITUTE_POLICY));
		//TODO REQUESTS
		view.allowSendMessage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CONVERSATION));
		view.allowReceiveMessage(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CONVERSATION));
		view.allowTransferManager(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER));
		view.allowExecuteDetailedCalculations(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.EXECUTE_DETAILED_CALCULATIONS));
		view.allowCreateSubPolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_SUB_POLICY));
		view.allowIssueDebitNote(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_DEBIT_NOTE));
		view.allowCreateNegotiation(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_NEGOTIATION));
		view.allowCreateRiskAnalisys(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RISK_ANALISYS));
		view.allowTransferToClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_TO_CLIENT));
		view.allowCreateSubPolicyReceipt(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsurancePolicyProcess.CREATE_RECEIPT));

	}

	private void validateCurrentObject(){
		if(!this.onPolicy){
			InsuredObject object = view.getInsuredObjectHeaderForm().getInfo();

			for(ValueSelectable<InsuredObjectStub> entry : view.getInsuredObjectsList().getAll()){
				if(entry.getValue().id.equalsIgnoreCase(object.id)) {
					view.setInvalidObjectEntry(entry, !view.getInsuredObjectHeaderForm().validate() && !(object.change == Change.DELETED));
					break;
				}
			}
		}
	}

	private void validateCurrentPolicy(){
		if(this.onPolicy){
			view.setInvalidPolicySelector(!view.getPolicyHeaderForm().validate());
		}
	}

	private void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}

}
