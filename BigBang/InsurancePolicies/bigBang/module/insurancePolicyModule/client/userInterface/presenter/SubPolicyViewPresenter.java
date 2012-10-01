package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ComplexFieldContainer;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.InsuredObjectStub.Change;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.StructuredFieldContainer.ColumnHeader;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.definitions.shared.SubPolicy;
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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubPolicyViewPresenter implements ViewPresenter{

	public static enum Action {
		EDIT, 
		CANCEL_EDIT, 
		SAVE, 
		INCLUDE_INSURED_OBJECT, 
		INCLUDE_INSURED_OBJECT_FROM_CLIENT, 
		CREATE_INSURED_OBJECT_FROM_CLIENT, 
		EXCLUDE_INSURED_OBJECT, 
		PERFORM_CALCULATIONS, 
		VALIDATE, 
		TRANSFER_TO_POLICY, 
		CREATE_INFO_OR_DOCUMENT_REQUEST, 
		CREATE_RECEIPT, 
		VOID, 
		DELETE, 
		CREATE_EXPENSE,
		BACK, 
		SUB_POLICY_SELECTED, 
		NEW_INSURED_OBJECT
	}

	public static interface Display {

		Widget asWidget();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void allowIncludeInsuredObject(boolean allow);

		void allowCreateInsuredObject(boolean allow);

		void allowIncludeInsuredObjectFromClient(boolean allow);

		void allowCreateInsuredObjectFromClient(boolean allow);

		void allowExcludeInsuredObject(boolean allow);

		void allowPerformCalculations(boolean allow);

		void allowValidate(boolean allow);

		void allowTransferToPolicy(boolean allow);

		void allowCreateInfoOrDocumentRequest(boolean allow);

		void allowCreateReceipt(boolean allow);

		void allowVoid(boolean allow);

		void allowDelete(boolean allow);

		HasValue<InsurancePolicy> getPolicyForm();

		HasEditableValue<SubPolicy> getSubPolicyForm();

		HasValue<String> getSubPolicyNotesForm();

		void setToolbarEditMode(boolean b);

		HasValue<SubPolicyStub> getSubPolicySelector();

		void setHeaders(Coverage[] coverages, ColumnHeader[] columns);

		void setReadOnly(boolean b);

		HasValueSelectables<InsuredObjectStub> getInsuredObjectsList();

		void setCoveragesExtraFields(Coverage[] coverages);

		HasEditableValue<FieldContainer> getCommonFieldsForm();

		void showObjectForm(boolean b);

		void showSubPolicyForm(boolean b);

		void setSubPolicyEntrySelected(boolean b);

		HasValue<String> getExerciseSelector();

		HasValue<ExerciseData> getExerciseForm();

		void setExerciseFieldsHeader(String string);

		void setExerciseVisible(boolean b);

		void setAvailableExercises(ExerciseData[] exercises);

		HasEditableValue<InsuredObject> getInsuredObjectHeaderForm();

		void dealWithObject(InsuredObjectStub insuredObjectStub);

		Coverage[] getPresentCoverages();

		void clearPolicySelection();

		void setSelectedObject(String id);

		HasValueSelectables<Contact> getContactsList();

		HasValueSelectables<Document> getDocumentsList();

		HasValueSelectables<ReceiptStub> getReceiptsList();

		HasValueSelectables<ExpenseStub> getExpensesList();

		HasValueSelectables<BigBangProcess> getSubProcessesList();

		HasValueSelectables<HistoryItemStub> getHistoryList();

		HasClickHandlers getObjectDeleteButton();

		void dealWithObject(InsuredObject info);

		void setSubPolicyNotesReadOnly(boolean b);

		void focusInsuredObjectForm();

		void clearObjectList();

		void allowCreateContact(boolean hasPermission);

		void allowCreateDocument(boolean hasPermission);

		void lockToolbar();

		void setOwner(SubPolicy subPol);

	}

	protected Display view;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected InsurancePolicyBroker policyBroker;
	protected boolean bound = false;
	private String subPolicyId;
	private String policyId;
	protected boolean isEditModeEnabled;
	private boolean onPolicy;
	private ExerciseData[] availableExercises;

	public SubPolicyViewPresenter(Display view){
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager
				.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.policyBroker = (InsurancePolicyBroker) DataBrokerManager
				.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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

		subPolicyId = parameterHolder.getParameter("subpolicyid");
		policyId = parameterHolder.getParameter("policyId");

		if(subPolicyId != null){
			if(subPolicyId.equals("new")){
				subPolicyBroker.getEmptySubPolicy(policyId, new ResponseHandler<SubPolicy>() {
					@Override
					public void onResponse(SubPolicy response) {

						isEditModeEnabled = false;
						onPolicy = true;
						view.setToolbarEditMode(true);
						view.getSubPolicySelector().setValue(response);
						view.setHeaders(response.coverages, response.columns);
						setExercises(response.exerciseData);
						view.setOwner(null);
						view.setCoveragesExtraFields(response.coverages);
						view.getSubPolicyNotesForm().setValue(response.notes);
						setPermissions(response);
						fillPolicy();
						fillSubPolicy();
						view.clearObjectList();
						view.setReadOnly(false);
						isEditModeEnabled = true;
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						subPolicyGetError();
					}
				});
			}
			else{
				subPolicyBroker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

					@Override
					public void onResponse(SubPolicy response) {
						policyId = response.mainPolicyId;
						isEditModeEnabled = false;
						onPolicy = true;
						view.setOwner(response);
						view.setToolbarEditMode(false);
						view.getSubPolicySelector().setValue(response);
						view.setHeaders(response.coverages, response.columns);
						setExercises(response.exerciseData);
						setPermissions(response);
						view.setCoveragesExtraFields(response.coverages);
						view.getSubPolicyNotesForm().setValue(response.notes);
						fillPolicy();
						fillSubPolicy();
						view.setReadOnly(true);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						subPolicyGetError();
					}
				});
			}
		}
		else{
			subPolicyGetError();
			view.lockToolbar();
		}

	}
	protected void fillSubPolicy() {

		SubPolicy pol = subPolicyBroker.getSubPolicyHeader(subPolicyId);
		view.getInsuredObjectsList().clearSelection();
		view.getSubPolicyForm().setValue(pol);
		view.getCommonFieldsForm().setValue(subPolicyBroker.getContextForSubPolicy(subPolicyId, getCurrentExerciseId()));
		view.showObjectForm(false);
		view.showSubPolicyForm(true);
		view.setSubPolicyEntrySelected(true);
		view.setSubPolicyNotesReadOnly(true);
		if(view.getExerciseForm().getValue() != null){
			view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Apólice");	
		}
		onPolicy = true;
	}

	private String getCurrentExerciseId() {
		ComplexFieldContainer.ExerciseData ex = view.getExerciseForm().getValue();
		return  ex != null ? ex.id : null;
	}

	protected void fillPolicy() {

		policyBroker.getPolicy(policyId, new ResponseHandler<InsurancePolicy>() {

			@Override
			public void onResponse(InsurancePolicy response) {
				view.getPolicyForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				policyGetError();
				view.lockToolbar();
			}
		});

	}

	protected void setPermissions(SubPolicy response) {

		view.allowCreateInfoOrDocumentRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_INFO_OR_DOCUMENT_REQUEST));
		view.allowCreateInsuredObjectFromClient(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.INCLUDE_OBJECT_FROM_CLIENT));
		view.allowCreateInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY));
		view.allowCreateReceipt(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.CREATE_RECEIPT));
		view.allowDelete(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.DELETE_SUB_POLICY));
		view.allowExcludeInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EXCLUDE_OBJECT));
		//view.allowIncludeInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.INCLUDE_OBJECT_FROM_CLIENT));
		//		view.allowIncludeInsuredObject(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.INCLUDE_OBJECT_FROM_CLIENT));
		view.allowPerformCalculations(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.PERFORM_CALCULATIONS));
		view.allowTransferToPolicy(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.TRANSFER_TO_POLICY));
		view.allowValidate(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VALIDATE));
		view.allowVoid(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.VOID));
		view.allowCreateContact(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY));
		view.allowCreateDocument(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.InsuranceSubPolicyProcess.EDIT_SUB_POLICY));
	}

	protected void setExercises(ExerciseData[] exerciseData) {

		if(exerciseData == null){
			view.setExerciseVisible(false);
			view.getExerciseForm().setValue(null);
			view.getExerciseSelector().setValue(null);
			return;
		}

		view.setExerciseVisible(true);

		int start = exerciseData[0].isActive ? 0 : 1; //FIRST ONE IS THE NEW ONE 

		availableExercises = new ExerciseData[exerciseData.length - start];

		for(int i = start; i<exerciseData.length; i++)
			availableExercises[i - start] = exerciseData[i];

		fillExercise(availableExercises);		
	}

	private void fillExercise(ExerciseData[] exercises) {
		view.setAvailableExercises(exercises);
		view.getExerciseForm().setValue(exercises[0], false);
		view.getExerciseSelector().setValue(exercises[0].id, false);		
	}

	private void policyGetError() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice mãe"), TYPE.ALERT_NOTIFICATION));		
	}

	private void subPolicyGetError() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a apólice adesão"), TYPE.ALERT_NOTIFICATION));		
	}

	private void bind() {
		if (bound) {
			return;
		}
		view.registerActionHandler(new ActionInvokedEventHandler<SubPolicyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();

				switch(action.getAction()){
				case BACK:
					onBack();
					break;
				case CANCEL_EDIT:
					onCancelEdit();
					break;
				case CREATE_EXPENSE:
					onCreateExpense();
					break;
				case CREATE_INFO_OR_DOCUMENT_REQUEST:
					onCreateInfoOrDocumentRequest();
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					//TODO
					break;
				case CREATE_RECEIPT:
					item.pushIntoStackParameter("display", "subpolicycreatereceipt");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					onDelete();
					break;
				case EDIT:
					onEdit();
					break;
				case EXCLUDE_INSURED_OBJECT:
					//TODO
					break;
				case INCLUDE_INSURED_OBJECT:
					//TODO
					break;
				case INCLUDE_INSURED_OBJECT_FROM_CLIENT:
					//TODO					
					break;
				case NEW_INSURED_OBJECT:
					onNewInsuredObject();
					break;
				case PERFORM_CALCULATIONS:
					onPerformCalculations();
					break;
				case SAVE:
					onSave();
					break;
				case SUB_POLICY_SELECTED:
					onSubPolicySelected();
					break;
				case TRANSFER_TO_POLICY:
					onTransferToPolicy();
					break;
				case VALIDATE:
					onValidate();
					break;
				case VOID:
					onVoid();
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

		//APPLICATION-WIDE EVENTS
		this.bound = true;

	}

	protected void onDeleteInsuredObject() {
		view.dealWithObject(subPolicyBroker.removeInsuredObject(subPolicyId, view.getInsuredObjectHeaderForm().getValue().id));
		onSubPolicySelected();		
	}

	protected void showHistory(HistoryItemStub selectedValue) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.pushIntoStackParameter("display", "history");
		navItem.setParameter("historyownerid", subPolicyId);
		navItem.setParameter("historyItemId", selectedValue.id);
		NavigationHistoryManager.getInstance().go(navItem);

	}

	protected void showSubProcess(BigBangProcess selectedValue) {
		String type = selectedValue.dataTypeId;

		if(type.equalsIgnoreCase(BigBangConstants.EntityIds.MANAGER_TRANSFER)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewmanagertransfer");
			item.setParameter("transferid", selectedValue.dataId);
			NavigationHistoryManager.getInstance().go(item);
		}else if(type.equalsIgnoreCase(BigBangConstants.EntityIds.INFO_REQUEST)){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.pushIntoStackParameter("display", "viewinforequest");
			item.setParameter("requestid", selectedValue.dataId);
			NavigationHistoryManager.getInstance().go(item);
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

	protected void showReceipt(ReceiptStub selectedValue) {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("section", "receipt");
		navItem.setStackParameter("display");
		navItem.pushIntoStackParameter("display", "search"); 
		navItem.setParameter("receiptid", selectedValue.id);
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

	protected void onObjectSelected(InsuredObjectStub stub) {
		if(onPolicy){
			view.clearPolicySelection();
		}
		saveInternally();

		fillObject(stub.id);

		onPolicy = false;		
	}

	protected void onExerciseChanged(String string) {
		if(string == null || string.isEmpty() || string.equals("forced")){
			return;
		}


		if(isEditModeEnabled){
			if(onPolicy){
				subPolicyBroker.saveContextForSubPolicy(subPolicyId, 
						getCurrentExerciseId(),
						view.getCommonFieldsForm().getInfo());
			}else{
				subPolicyBroker.saveContextForInsuredObject(subPolicyId, 
						view.getInsuredObjectHeaderForm().getValue().id, getCurrentExerciseId(), 
						view.getCommonFieldsForm().getInfo());
			}
		}

		if(onPolicy){
			view.getCommonFieldsForm().setValue(subPolicyBroker.getContextForSubPolicy(subPolicyId, string));
		}else{
			view.getCommonFieldsForm().setValue(subPolicyBroker.getContextForInsuredObject(subPolicyId, view.getInsuredObjectHeaderForm().getValue().id, string));
		}

		for(int i = 0; i<availableExercises.length; i++){
			if(availableExercises[i].id.equalsIgnoreCase(getCurrentExerciseId())){
				availableExercises[i] = view.getExerciseForm().getValue();
			}else if(availableExercises[i].id.equalsIgnoreCase(string)){
				view.getExerciseForm().setValue(availableExercises[i]);
			}
		}		
	}

	protected void onVoid() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "voidsubpolicy");
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void onValidate() {
		subPolicyBroker.validateSubPolicy(subPolicyId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Adesão validada com sucesso"), TYPE.TRAY_NOTIFICATION));
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

	protected void onValidationFailed(String message) {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A apólice falhou a validação :<br><br>" + message), TYPE.ALERT_NOTIFICATION));
	}

	protected void onTransferToPolicy() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "subpolicytransfertopolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSubPolicySelected() {
		if(onPolicy)
			return;
		saveInternally();
		fillSubPolicy();
	}

	private void saveInternally() {
		if(!isEditModeEnabled){
			return;
		}
		if(!onPolicy){
			subPolicyBroker.updateInsuredObject(subPolicyId, view.getInsuredObjectHeaderForm().getInfo());
			subPolicyBroker.saveContextForInsuredObject(subPolicyId, 
					view.getInsuredObjectHeaderForm().getValue().id, getCurrentExerciseId(), 
					view.getCommonFieldsForm().getInfo());
			view.dealWithObject(view.getInsuredObjectHeaderForm().getInfo());
		}
		else{
			subPolicyBroker.updateSubPolicyHeader(view.getSubPolicyForm().getInfo());
			subPolicyBroker.saveContextForSubPolicy(subPolicyId, 
					getCurrentExerciseId(),
					view.getCommonFieldsForm().getInfo());
		}		
	}

	protected void onSave() {
		saveInternally();
		subPolicyBroker.updateCoverages(view.getPresentCoverages());
		subPolicyBroker.persistSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				onSavePolicySuccess(response.id);
				isEditModeEnabled = false;
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar a apólice"), TYPE.ALERT_NOTIFICATION));					
			}
		});
	}

	protected void onSavePolicySuccess(String id) {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Apólice guardada com sucesso"), TYPE.TRAY_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("subpolicyid", id);
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void onPerformCalculations() {
		SubPolicy policy =  view.getSubPolicyForm().getValue();
		String policyId = policy == null ? null : policy.id;

		if(policyId != null) {
			subPolicyBroker.executeDetailedCalculations(policyId, new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					onExecuteDetailedCalculationsSuccess();
					NavigationHistoryManager.getInstance().reload();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onExecuteDetailedCalculationsFailed();
				}
			});
		}else {
			subPolicyGetError();
		}
	}

	protected void onExecuteDetailedCalculationsFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Os cálculos detalhados foram executados com sucesso"), TYPE.TRAY_NOTIFICATION));

	}

	protected void onExecuteDetailedCalculationsSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível executar os cálculos detalhados"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onNewInsuredObject() {
		if(onPolicy){
			view.clearPolicySelection();
		}
		saveInternally();

		fillObject(null);
		onPolicy = false;
	}

	private void fillObject(String id) {
		if(id == null){
			InsuredObject newInsuredObject = subPolicyBroker.createInsuredObject(subPolicyId);
			newInsuredObject.unitIdentification = "Nova Unidade de Risco";
			view.getInsuredObjectHeaderForm().setValue(newInsuredObject);
			view.getCommonFieldsForm().setValue(subPolicyBroker.getContextForInsuredObject(subPolicyId, newInsuredObject.id, getCurrentExerciseId()));
			view.showObjectForm(true);
			view.showSubPolicyForm(false);
			view.setSubPolicyNotesReadOnly(true);
			view.dealWithObject(newInsuredObject);
			if(view.getExerciseSelector().getValue() != null && view.getExerciseForm().getValue() != null){
				view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Unidade de Risco");	
			}
			view.focusInsuredObjectForm();
			return;
		}

		subPolicyBroker.getInsuredObject(subPolicyId, id, new ResponseHandler<InsuredObject>() {

			@Override
			public void onResponse(InsuredObject response) {
				view.getInsuredObjectHeaderForm().setValue(response);
				view.getCommonFieldsForm().setValue(subPolicyBroker.getContextForInsuredObject(subPolicyId, response.id, getCurrentExerciseId()));
				view.showObjectForm(true);
				view.showSubPolicyForm(false);
				view.dealWithObject(response);
				view.setSubPolicyNotesReadOnly(true);
				if(view.getExerciseSelector().getValue() != null && view.getExerciseForm().getValue() != null){
					view.setExerciseFieldsHeader("Detalhes do exercício " + view.getExerciseForm().getValue().label +" para a Unidade de Risco");	
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o objecto"), TYPE.ALERT_NOTIFICATION));					
			}
		});
	}

	protected void onEdit() {
		view.setReadOnly(false);
		isEditModeEnabled = true;
		view.setToolbarEditMode(true);
	}

	protected void onDelete() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.setParameter("show", "deletesubpolicy");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateInfoOrDocumentRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.pushIntoStackParameter("display", "subpolicyinforequest");
		item.setParameter("ownerid", view.getSubPolicyForm().getValue().id);
		item.setParameter("ownertypeid",
				BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateExpense() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.pushIntoStackParameter("display", "createexpensesubpolicy");
		item.setParameter("objectid", "new");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCancelEdit() {
		view.setReadOnly(true);
		subPolicyBroker.discardEditData(subPolicyId);
		revert();
		view.setToolbarEditMode(false);
		isEditModeEnabled = false;
	}

	private void revert() {

		if(onPolicy || view.getInsuredObjectHeaderForm().getValue().change == Change.CREATED){
			fillPolicy();
		}
		else{
			fillObject(view.getInsuredObjectHeaderForm().getValue().id);
		}
	}

	protected void onBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance()
				.getCurrentState();
		item.removeParameter("subpolicyid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}


}
