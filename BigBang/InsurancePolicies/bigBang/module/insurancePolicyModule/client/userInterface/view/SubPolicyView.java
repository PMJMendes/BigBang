package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.ComplexFieldContainer.ExerciseData;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.FieldContainer;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.StructuredFieldContainer;
import bigBang.definitions.shared.StructuredFieldContainer.Coverage;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.CoverageExerciseDetailsForm;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseSelector;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectSubPolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.PolicyNotesFormSection;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicyOperationsToolbar;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPolicySelectButton;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.SubPolicyViewPresenter.Action;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubPolicyView extends View implements SubPolicyViewPresenter.Display{

	private ActionInvokedEventHandler<Action> actionHandler;
	private SubPolicyForm form;
	private InsurancePolicyForm policyForm;
	private SubPolicyOperationsToolbar toolbar;
	private SubPolicyChildrenPanel childrenPanel;
	private InsuredObjectSubPolicySearchPanel objectsList;
	private InsuredObjectForm objectForm;
	private ExerciseSelector exerciseChooser;
	private CoverageExerciseDetailsForm detailsForm;
	private SubPolicySelectButton subPolicySelectButton;
	private PolicyNotesFormSection subPolicyNotesForm;
	private PolicyNotesFormSection policyNotesForm;

	public SubPolicyView() {

		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel policyWrapper = new VerticalPanel();
		policyWrapper.setSize("100%", "100%");

		ListHeader policyHeader = new ListHeader("Apólice Principal");
		policyHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.BACK));
			}
		}));
		policyWrapper.add(policyHeader);

		policyForm = new InsurancePolicyForm();
		policyNotesForm = new PolicyNotesFormSection();

		policyWrapper.add(policyForm);
		policyWrapper.add(policyNotesForm);
		policyWrapper.setCellHeight(policyForm, "100%");

		mainWrapper.addWest(policyWrapper, 600);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		SimplePanel childrenPresentersPanel = new SimplePanel();
		childrenPresentersPanel.setSize("100%", "100%");

		childrenPanel = new SubPolicyChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		contentWrapper.addEast(childrenPanel, 300);

		toolbar = new SubPolicyOperationsToolbar(){

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CANCEL_EDIT));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onIncludeInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.INCLUDE_INSURED_OBJECT));
			}

			@Override
			public void onIncludeInsuredObjectFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.INCLUDE_INSURED_OBJECT_FROM_CLIENT));
			}

			@Override
			public void onCreateInsuredObjectFromClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_INSURED_OBJECT_FROM_CLIENT));
			}

			@Override
			public void onExcludeInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.EXCLUDE_INSURED_OBJECT));
			}

			@Override
			public void onPerformCalculations() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.PERFORM_CALCULATIONS));
			}

			@Override
			public void onValidate() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.VALIDATE));
			}

			@Override
			public void onTransferToPolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.TRANSFER_TO_POLICY));
			}

			@Override
			public void onCreateInfoOrDocumentRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_INFO_OR_DOCUMENT_REQUEST));
			}

			@Override
			public void onCreateReceipt() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_RECEIPT));
			}

			@Override
			public void onVoid() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.VOID));
			}

			@Override
			protected void onCreateHealthExpense() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.CREATE_EXPENSE));
			}

		};

		VerticalPanel formContainer = new VerticalPanel();
		formContainer.setSize("100%", "100%");

		HorizontalPanel formPanel = new HorizontalPanel();

		form = new SubPolicyForm();
		objectForm = new InsuredObjectForm();

		VerticalPanel objectsPolicyContainer = new VerticalPanel();

		subPolicySelectButton = new SubPolicySelectButton(new SubPolicyStub());
		objectsPolicyContainer.setSize("100%","100%");
		objectsPolicyContainer.add(subPolicySelectButton);
		subPolicySelectButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if (event.getSelected()){
					actionHandler.onActionInvoked(new  ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.SUB_POLICY_SELECTED));
				}
			}
		});
		objectsList = new InsuredObjectSubPolicySearchPanel();
		objectsList.showFilterField(false);
		objectsList.getNewObjectButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SubPolicyViewPresenter.Action>(Action.NEW_INSURED_OBJECT));
			}
		});
		objectsPolicyContainer.add(objectsList);
		objectsPolicyContainer.setCellHeight(objectsList, "100%");
		objectsList.setSize("100%", "100%");
		formPanel.add(objectsPolicyContainer);
		objectsPolicyContainer.setCellWidth(objectsList, "200px");
		formPanel.setCellWidth(objectsPolicyContainer, "200px");
		formPanel.setCellHeight(objectsPolicyContainer, "100%");
		formPanel.add(form.getNonScrollableContent());
		formPanel.add(objectForm.getNonScrollableContent());

		objectForm.getNonScrollableContent().setVisible(false);

		formPanel.setCellWidth(form, "100%");
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

		detailsForm = new CoverageExerciseDetailsForm("");
		detailTableContainer.add(detailsForm.getNonScrollableContent());
		centerWrapper.add(detailTableContainer);
		centerWrapper.setCellHeight(detailTableContainer, "100%");
		centerWrapper.setCellWidth(detailTableContainer, "100%");

		subPolicyNotesForm = new PolicyNotesFormSection();
		subPolicyNotesForm.setSize("100%", "100%");

		centerWrapper.add(subPolicyNotesForm);

		scrollContainer.add(centerWrapper);
		scrollContainer.getElement().getStyle().setBackgroundColor("whiteSmoke");
		scrollContainer.getElement().getStyle().setOverflowX(Style.Overflow.HIDDEN);

		toolbarAndCenterWrapper.add(scrollContainer);
		toolbarAndCenterWrapper.setCellHeight(scrollContainer, "100%");
		contentWrapper.add(toolbarAndCenterWrapper);
		mainWrapper.add(contentWrapper);

		policyForm.setHeaderFormVisible(false);
		exerciseChooser.allowCreateExercise(false);

		policyForm.setReadOnly(true);
		subPolicyNotesForm.setReadOnly(true);
		policyNotesForm.setReadOnly(true);
		form.setReadOnly(true);
		exerciseChooser.setReadOnly(true);
	}

	@Override
	protected void initializeView() {
		return;		
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		actionHandler = handler;
	}
	public void allowEdit(boolean allow) {
		toolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowIncludeInsuredObject(boolean allow) {
		toolbar.allowIncludeInsuredObject(allow);
	}

	@Override
	public void allowCreateInsuredObject(boolean allow) {
		objectsList.allowCreateInsuredObject(allow);
	}

	@Override
	public void allowIncludeInsuredObjectFromClient(boolean allow) {
		toolbar.allowIncludeInsuredObjectFromClient(allow);
	}

	@Override
	public void allowExcludeInsuredObject(boolean allow) {
		toolbar.allowExcludeInsuredObject(allow);
	}

	@Override
	public void allowPerformCalculations(boolean allow) {
		toolbar.allowPerformCalculations(allow);
	}

	@Override
	public void allowValidate(boolean allow) {
		toolbar.allowValidate(allow);
	}

	@Override
	public void allowTransferToPolicy(boolean allow) {
		toolbar.allowTransferToPolicy(allow);
	}

	@Override
	public void allowCreateInfoOrDocumentRequest(boolean allow) {
		toolbar.allowCreateInfoOrDocumentRequest(allow);
	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		toolbar.allowCreateReceipt(allow);
	}

	@Override
	public void allowVoid(boolean allow) {
		toolbar.allowVoid(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		toolbar.allowDelete(allow);
	}

	@Override
	public void allowCreateInsuredObjectFromClient(boolean allow) {
		toolbar.allowCreateInsuredObjectFromClient(allow);
	}

	@Override
	public HasValue<InsurancePolicy> getPolicyForm() {
		return policyForm;
	}

	@Override
	public HasValue<String> getSubPolicyNotesForm(){
		return subPolicyNotesForm;
	}

	@Override
	public HasValue<String> getPolicyNotesForm(){
		return policyNotesForm;
	}

	@Override
	public void setOwner(String id) {
		objectsList.setOwner(id);
		childrenPanel.contactsList.setOwner(id);
		childrenPanel.documentsList.setOwner(id);
		childrenPanel.expensesList.setOwner(id);
		childrenPanel.historyList.setOwner(id);
		childrenPanel.receiptList.setOwner(id);
		childrenPanel.subProcessesList.setOwner(id);
	}

	@Override
	public void setToolbarEditMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
		toolbar.lockNonSaveOptions(b);		
	}

	@Override
	public HasValue<SubPolicyStub> getSubPolicySelector() {
		return subPolicySelectButton;
	}

	@Override
	public void setHeaders(StructuredFieldContainer.Coverage[] coverages, StructuredFieldContainer.ColumnHeader[] columns) {
		detailsForm.setHeaders(coverages, columns);
	}

	@Override
	public void setReadOnly(boolean b) {
		detailsForm.setReadOnly(b);
		form.setReadOnly(b);
		objectForm.setReadOnly(b);
		subPolicyNotesForm.setReadOnly(b);
		objectsList.setReadOnly(b);
	}

	@Override
	public HasValueSelectables<InsuredObjectStub> getInsuredObjectsList() {
		return objectsList;
	}

	@Override
	public void setCoveragesExtraFields(Coverage[] coverages) {
		detailsForm.setCoveragesExtraFields(coverages);		
	}

	@Override
	public HasEditableValue<FieldContainer> getCommonFieldsForm() {
		return detailsForm;
	}

	@Override
	public void showObjectForm(boolean b) {
		objectForm.getNonScrollableContent().setVisible(b);		
	}

	@Override
	public void showSubPolicyForm(boolean b) {
		form.getNonScrollableContent().setVisible(b);		
	}

	@Override
	public void setSubPolicyEntrySelected(boolean b) {
		subPolicySelectButton.setSelected(false, false);
	}

	@Override
	public HasValue<String> getExerciseSelector() {
		return exerciseChooser.getExerciseSelector();
	}

	@Override
	public HasValue<ExerciseData> getExerciseForm() {
		return exerciseChooser;
	}

	@Override
	public void setExerciseFieldsHeader(String string) {
		detailsForm.setExerciseHeader(string);		
	}

	@Override
	public void setExerciseVisible(boolean b) {
		exerciseChooser.setVisible(b);		
	}

	@Override
	public void setAvailableExercises(ExerciseData[] exercises) {
		exerciseChooser.setAvailableExercises(exercises);
	}

	@Override
	public void dealWithObject(InsuredObject info) {
		objectsList.dealWithObject(info);

	}

	@Override
	public HasEditableValue<SubPolicy> getSubPolicyForm() {
		return form;
	}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectHeaderForm() {
		return objectForm;
	}

	@Override
	public Coverage[] getPresentCoverages() {
		return detailsForm.getPresentCoverages();
	}

	@Override
	public void clearPolicySelection() {
		subPolicySelectButton.setSelected(false, false);

	}

	@Override
	public void setSelectedObject(String id) {
		objectsList.setSelected(id);		
	}

	@Override
	public void dealWithObject(InsuredObjectStub insuredObjectStub) {
		objectsList.dealWithObject(insuredObjectStub);		
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return childrenPanel.contactsList;	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return childrenPanel.documentsList;
	}

	@Override
	public HasValueSelectables<ReceiptStub> getReceiptsList() {
		return childrenPanel.receiptList;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getExpensesList() {
		return childrenPanel.expensesList;
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return childrenPanel.subProcessesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}

	@Override
	public HasClickHandlers getObjectDeleteButton() {
		return objectForm.getDeleteButton();
	}
}
