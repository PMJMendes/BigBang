package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseSelector;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel.Entry;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectSearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.PolicyNotesFormSection;
import bigBang.module.insurancePolicyModule.client.userInterface.PolicySelectButton;
import bigBang.module.insurancePolicyModule.client.userInterface.form.CoverageExerciseDetailsForm;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsurancePolicyHeaderForm;
import bigBang.module.insurancePolicyModule.client.userInterface.form.InsuredObjectForm;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter.Action;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display{

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	private InsurancePolicySearchPanel searchPanel;
	protected ActionInvokedEventHandler<Action> actionHandler;
	private InsurancePolicyChildrenPanel childrenPanel;
	protected HasWidgets childrenPresentersPanel;
	private InsurancePolicyOperationsToolBar toolbar;
	private InsuredObjectSearchPanel objectsList;
	private InsurancePolicyHeaderForm policyForm;
	private InsuredObjectForm objectForm;
	private ExerciseSelector exerciseChooser;
	private CoverageExerciseDetailsForm detailsForm;
	private PolicyNotesFormSection policyNotesForm;
	private PolicySelectButton policySelectButton;

	public InsurancePolicySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");

		ListHeader searchPanelHeader = new ListHeader("Apólices");
		searchPanelWrapper.add(searchPanelHeader);

		policyForm = new InsurancePolicyHeaderForm();
		objectForm = new InsuredObjectForm();
		
		searchPanel = new InsurancePolicySearchPanel(){ 
			@Override
			public void onResults(Collection<InsurancePolicyStub> results) {
				super.onResults(results);
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.ON_NEW_RESULTS));
			}
		};

		doSearch();
		
		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");

		mainWrapper.addWest(searchPanelWrapper, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		SimplePanel childrenPresentersPanel = new SimplePanel();
		childrenPresentersPanel.setSize("100%", "100%");
		this.childrenPresentersPanel = childrenPresentersPanel;

		childrenPanel = new InsurancePolicyChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		contentWrapper.addEast(childrenPanel, 300);

		toolbar = new InsurancePolicyOperationsToolBar(){

			@Override
			public void onVoidPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.VOID_POLICY));
			}

			@Override
			public void onBrokerageTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.TRANSFER_BROKERAGE));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onValidate() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.VALIDATE));
			}

			@Override
			public void onCreateSubstitutePolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_SUBSTITUTE_POLICY));
			}

			@Override
			public void onRequestInfoFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.REQUEST_CLIENT_INFO));
			}

			@Override
			public void onRequestInfoFromAgency() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.REQUEST_AGENCY_INFO));
			}

			@Override
			public void onIncludeInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.INCLUDE_INSURED_OBJECT));
			}

			@Override
			public void onCreateInsuredObjectFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_INSURED_OBJECT_FROM_CLIENT));
			}

			@Override
			public void onCreateManagerTransfer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.TRANSFER_MANAGER));
			}

			@Override
			public void onExecuteDecailedCalculations() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.EXECUTE_DETAILED_CALCULATIONS));
			}

			@Override
			public void onCreateInfoManagementProcess() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_INFO_MANAGEMENT_PROCESS));
			}

			@Override
			public void onCreateSubPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_SUB_POLICY));
			}

			@Override
			public void onIssueDebitNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.ISSUE_DEBIT_NOTE));
			}

			@Override
			public void onCreateNegotiation() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_NEGOTIATION));
			}

			@Override
			public void onCreateHealthExpense() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_EXPENSE));
			}

			@Override
			public void onCreateRiskAnalysis() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_RISK_ANALISYS));
			}

			@Override
			public void onCreateReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_RECEIPT));
			}

			@Override
			public void onTransferToClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.TRANSFER_TO_CLIENT));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}

		};

		VerticalPanel formContainer = new VerticalPanel();
		formContainer.setSize("100%", "100%");

		HorizontalPanel formPanel = new HorizontalPanel();

		VerticalPanel objectsPolicyContainer = new VerticalPanel();
		objectsPolicyContainer.getElement().getStyle().setProperty("borderRight", "1px solid #688AA2");

		policySelectButton = new PolicySelectButton(new InsurancePolicyStub());
		objectsPolicyContainer.setSize("100%","100%");
		objectsPolicyContainer.add(policySelectButton);
		policySelectButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if (event.getSelected()){
					actionHandler.onActionInvoked(new  ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.ON_POLICY_SELECTED));
				}
			}
		});
		objectsList = new InsuredObjectSearchPanel();
		objectsList.showFilterField(false);
		objectsList.getNewObjectButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.NEW_INSURED_OBJECT));
			}
		});
		objectsPolicyContainer.add(objectsList);
		objectsPolicyContainer.setCellHeight(objectsList, "100%");
		objectsList.setSize("100%", "100%");
		formPanel.add(objectsPolicyContainer);
		objectsPolicyContainer.setCellWidth(objectsList, "200px");
		formPanel.setCellWidth(objectsPolicyContainer, "200px");
		formPanel.setCellHeight(objectsPolicyContainer, "100%");
		formPanel.add(policyForm.getNonScrollableContent());
		formPanel.add(objectForm.getNonScrollableContent());

		objectForm.getNonScrollableContent().setVisible(false);

		formPanel.setCellWidth(policyForm, "100%");
		policyForm.getNonScrollableContent().setSize("100%", "100%");
		objectForm.getNonScrollableContent().setSize("100%", "100%");
		formPanel.setSize("100%", "100%");
		formContainer.add(formPanel);
		formContainer.setCellHeight(formPanel, "100%");

		ScrollPanel scrollContainer = new ScrollPanel();
		scrollContainer.setSize("100%", "100%");
		scrollContainer.getElement().getStyle().setProperty("overflowx","hidden");
		VerticalPanel centerWrapper = new VerticalPanel();

		centerWrapper.setSize("100%", "100%");

		VerticalPanel toolbarAndCenterWrapper = new VerticalPanel();
		toolbarAndCenterWrapper.setSize("100%", "100%");
		toolbarAndCenterWrapper.add(toolbar);

		centerWrapper.add(formContainer);
		centerWrapper.setCellHeight(formContainer, "100%");
		VerticalPanel detailTableContainer = new VerticalPanel();
		detailTableContainer.setSize("100%", "100%");

		exerciseChooser = new ExerciseSelector();
		detailTableContainer.add(exerciseChooser);
		exerciseChooser.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.NEW_EXERCISE));
			}
		});

		detailsForm = new CoverageExerciseDetailsForm("");
		detailTableContainer.add(detailsForm.getNonScrollableContent());
		centerWrapper.add(detailTableContainer);
		centerWrapper.setCellHeight(detailTableContainer, "100%");
		centerWrapper.setCellWidth(detailTableContainer, "100%");

		policyNotesForm = new PolicyNotesFormSection();
		policyNotesForm.setSize("100%", "100%");

		centerWrapper.add(policyNotesForm);

		scrollContainer.add(centerWrapper);
		scrollContainer.getElement().getStyle().setBackgroundColor("whiteSmoke");
		scrollContainer.getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);

		toolbarAndCenterWrapper.add(scrollContainer);
		toolbarAndCenterWrapper.setCellHeight(scrollContainer, "100%");
		contentWrapper.add(toolbarAndCenterWrapper);
		mainWrapper.add(contentWrapper);

	}

	@Override
	protected void initializeView() {}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
	}

	@Override
	public HasValueSelectables<InsurancePolicyStub> getList() {
		return searchPanel;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getPolicyHeaderForm() {
		return policyForm;
	}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectHeaderForm() {
		return objectForm;
	}

	@Override
	public void allowDeleteInsuredObject(boolean allow) {
		toolbar.allowDelete(allow);
	}

	@Override
	public HasEditableValue<FieldContainer> getCommonFieldsForm() {
		return detailsForm;
	}

	@Override
	public HasValue<ExerciseData> getExerciseForm() {
		return exerciseChooser;
	}

	@Override
	public HasValue<String> getPolicyNotesForm() {
		return policyNotesForm;	
	}

	@Override
	public HasValueSelectables<InsuredObjectStub> getInsuredObjectsList() {
		return objectsList;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean allow) {
		toolbar.allowEdit(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		toolbar.allowDelete(allow);
	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		toolbar.allowCreateReceipt(allow);
	}

	@Override
	public void allowCreateInsuredObject(boolean allow) {
		objectsList.allowCreateInsuredObject(allow);
	}

	@Override
	public void allowIncludeInsuredObject(boolean allow) {
		toolbar.allowIncludeInsuredObject(allow);
	}

	@Override
	public void allowCreateNewExercise(boolean allow) {
		exerciseChooser.allowCreateExercise(allow);
	}

	@Override
	public void allowValidatePolicy(boolean allow) {
		toolbar.allowValidate(allow);
	}

	@Override
	public void allowVoidPolicy(boolean allow) {
		toolbar.allowVoidPolicy(allow);
	}

	@Override
	public void allowTransferBrokerage(boolean allow) {
		toolbar.allowTransferBrokerage(allow);
	}

	@Override
	public void allowCreateSubstitutePolicy(boolean allow) {
		toolbar.allowCreateSubstitutepolicy(allow);
	}

	@Override
	public void allowRequestClientInfo(boolean allow) {
		toolbar.allowRequestClientInfo(allow);
	}

	@Override
	public void allowRequestAgencyInfo(boolean allow) {
		toolbar.allowRequestAgencyInfo(allow);
	}

	@Override
	public void allowCreateInsuredObjectFromClient(boolean allow) {
		toolbar.allowCreateInsuredObjectFromClient(allow);
	}

	@Override
	public void allowTransferManager(boolean allow) {
		toolbar.allowTransferManager(allow);
	}

	@Override
	public void allowExecuteDetailedCalculations(boolean allow) {
		toolbar.allowExecuteDetailedalculations(allow);
	}

	@Override
	public void allowCreateInfoManagementProcess(boolean allow) {
		toolbar.allowCreateInfoManagementProcess(allow);
	}

	@Override
	public void allowCreateSubPolicy(boolean allow) {
		toolbar.allowCreateSubPolicy(allow);
	}

	@Override
	public void allowIssueDebitNote(boolean allow) {
		toolbar.allowIssueDebitNote(allow);
	}

	@Override
	public void allowCreateNegotiation(boolean allow) {
		toolbar.allowCreateNegotiation(allow);
	}

	@Override
	public void allowCreateHealthExpense(boolean allow) {
		toolbar.allowCreateHealthExpense(allow);
	}

	@Override
	public void allowCreateRiskAnalisys(boolean allow) {
		toolbar.allowCreateRiskAnalisys(allow);
	}

	@Override
	public void allowTransferToClient(boolean allow) {
		toolbar.allowTransferToClient(allow);
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return childrenPanel.documentsList;
	}

	@Override
	public HasValueSelectables<SubPolicyStub> getSubPoliciesList() {
		return childrenPanel.subPoliciesList;
	}

	@Override
	public HasValueSelectables<ReceiptStub> getReceiptsList() {
		return childrenPanel.receiptsList;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getExpensesList() {
		return childrenPanel.expensesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return childrenPanel.subProcessesList;
	}

	@Override
	public void setAvailableExercises(ExerciseData[] exerciseData) {
		exerciseChooser.setAvailableExercises(exerciseData);
	}

	@Override
	public HasValue<InsurancePolicyStub> getPolicySelector() {
		return policySelectButton;
	}

	@Override
	public HasValue<String> getExerciseSelector() {
		return exerciseChooser.getExerciseSelector();
	}

	@Override
	public void setHeaders(	StructuredFieldContainer.Coverage[] coverages,
			StructuredFieldContainer.ColumnHeader[] columns){
		detailsForm.setHeaders(coverages, columns);
	}

	@Override
	public void setCoveragesExtraFields(StructuredFieldContainer.Coverage[] coverages) {
		detailsForm.setCoveragesExtraFields(coverages);
	}


	@Override
	public void setReadOnly(boolean b) {
		detailsForm.setReadOnly(b);
		exerciseChooser.setReadOnly(b);
		objectForm.setReadOnly(b);
		policyForm.setReadOnly(b);
		policyNotesForm.setReadOnly(b);
		objectsList.setReadOnly(b);
	}

	@Override
	public void setOwner(InsurancePolicy policy) {
		setObjectListOwner(policy == null ? null : policy.id);
		childrenPanel.setPolicy(policy);
	}

	@Override
	public void showObjectForm(boolean b) {
		objectForm.getNonScrollableContent().setVisible(b);
	}

	@Override
	public void showPolicyForm(boolean b) {
		policyForm.getNonScrollableContent().setVisible(b);
	}

	@Override
	public void clearPolicySelection() {
		policySelectButton.setSelected(false, false);
	}

	@Override
	public void setToolbarEditMode(boolean b) {
		toolbar.lockNonSaveOptions(b);
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public void lockToolbar() {
		toolbar.lockAll();
	}

	@Override
	public void setPolicyEntrySelected(boolean b) {
		policySelectButton.setSelected(b, false);
	}

	@Override
	public void setExerciseFieldsHeader(String header){
		detailsForm.setExerciseHeader(header);
	}

	@Override
	public Coverage[] getPresentCoverages() {
		return detailsForm.getPresentCoverages();
	}

	@Override
	public void dealWithObject(InsuredObjectStub response) {
		objectsList.dealWithObject(response);
	}

	@Override
	public HasClickHandlers getObjectDeleteButton(){
		return objectForm.getDeleteButton();
	}

	@Override
	public void setSelectedObject(String id) {
		objectsList.setSelected(id);
	}

	@Override
	public void focusInsuredObjectForm() {
		objectForm.focus();
	}

	@Override
	public void setNotesReadOnly(boolean b) {
		policyNotesForm.setReadOnly(b);
	}

	@Override
	public void clearObjectsList() {
		objectsList.clear();
	}

	@Override
	public void clearPolicyList() {
		searchPanel.clear();
	}

	@Override
	public void doSearch() {
		searchPanel.doSearch();
	}

	@Override
	public void allowManagerChange(boolean b) {
		policyForm.allowManagerChange(b);
	}
	
	@Override
	public void setObjectListOwner(String policyId){
		objectsList.setOwner(policyId);
	}

	@Override
	public void addEntryToList(Entry entry) {
		searchPanel.add(0, entry);
		entry.setSelected(true, false);
	}

	@Override
	public void removeElementFromList(ValueSelectable<InsurancePolicyStub> stub) {
		searchPanel.remove(stub);
	}
}
