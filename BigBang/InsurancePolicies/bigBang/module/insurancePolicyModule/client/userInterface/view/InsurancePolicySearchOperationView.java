package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
import bigBang.definitions.shared.SubPolicyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseChooser;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.CoverageExerciseDetailsForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectSearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter.Action;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display{

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	private InsurancePolicySearchPanel searchPanel;
	protected ActionInvokedEventHandler<Action> actionHandler;
	private InsurancePolicyChildrenPanel childrenPanel;
	protected HasWidgets childrenPresentersPanel;
	private InsurancePolicyOperationsToolBar toolbar;
	private InsuredObjectSearchPanel objectsList;
	private InsurancePolicyForm policyForm;
	private ExerciseChooser exerciseChooser;
	private CoverageExerciseDetailsForm detailsForm;

	public InsurancePolicySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");

		ListHeader searchPanelHeader = new ListHeader("Apólices");
		searchPanelWrapper.add(searchPanelHeader);

		searchPanel = new InsurancePolicySearchPanel(){ //TODO FAZ SENTIDO MANTER ESTE SEARCHPANEL? OU CRIAR UM NOVO?

			@Override
			public void onResults(Collection<InsurancePolicyStub> results) {
				super.onResults(results);
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.ON_NEW_RESULTS));
			}
		};

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
				// TODO Auto-generated method stub

			}

			@Override
			public void onBrokerageTransfer() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDelete() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onValidate() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateSubstitutePolicy() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestInfoFromClient() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRequestInfoFromAgency() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateInsuredObject() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onIncludeInsuredObject() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateExercise() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateInsuredObjectFromClient() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateManagerTransfer() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onExecuteDecailedCalculations() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateInfoManagementProcess() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateSubPolicy() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onIssueDebitNote() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateNegotiation() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateHealthExpense() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateRiskAnalysis() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCreateReceipt() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTransferToClient() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onEditRequest() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSaveRequest() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub

			}

		};

		VerticalPanel formContainer = new VerticalPanel();
		formContainer.setSize("100%", "100%");

		HorizontalPanel formPanel = new HorizontalPanel();

		policyForm = new InsurancePolicyForm();
		//TODO ADD INSURANCE, OBJECT AND EXERCISE FORM

		objectsList = new InsuredObjectSearchPanel(null); //TODO METER AQUI O BROKER
		objectsList.showFilterField(false);
		formPanel.add(objectsList);
		objectsList.setWidth("100%");
		formPanel.setCellWidth(objectsList, "200px");
		formPanel.add(policyForm.getNonScrollableContent());
		formPanel.setCellWidth(policyForm, "100%");
		policyForm.getNonScrollableContent().setSize("100%", "100%");
		formPanel.setSize("100%", "100%");
		formContainer.add(formPanel);
		formContainer.setCellHeight(formPanel, "100%");

		ScrollPanel scrollContainer = new ScrollPanel();
		scrollContainer.setSize("100%", "100%");
		
		VerticalPanel centerWrapper = new VerticalPanel();

		centerWrapper.setSize("100%", "100%");

		VerticalPanel toolbarAndCenterWrapper = new VerticalPanel();
		toolbarAndCenterWrapper.setSize("100%", "100%");
		toolbarAndCenterWrapper.add(toolbar);
		
		centerWrapper.add(formContainer);
		centerWrapper.setCellHeight(formContainer, "100%");
		VerticalPanel detailTableContainer = new VerticalPanel();
		detailTableContainer.setSize("100%", "100%");
		
		exerciseChooser = new ExerciseChooser();
		detailTableContainer.add(exerciseChooser);

		detailsForm = new CoverageExerciseDetailsForm("");
		detailTableContainer.add(detailsForm.getNonScrollableContent());
		
		centerWrapper.add(detailTableContainer);
		centerWrapper.setCellHeight(detailTableContainer, "100%");

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasValueSelectables<InsurancePolicyStub> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasEditableValue<InsurancePolicy> getPolicyHeaderForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasEditableValue<InsuredObject> getInsuredObjectHeaderForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void allowDeleteInsuredObject(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasEditableValue<FieldContainer> getCommonFieldsForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValue<ExerciseData> getExerciseForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValue<String> getExerciseSelector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void allowCreateNewExercise(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasValue<String> getPolicyNotesForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<InsuredObjectStub> getInsuredObjectsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllowedPermissions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowEdit(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowDelete(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateInsuredObject(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowIncludeInsuredObject(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateExercise(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowValidatePolicy(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowVoidPolicy(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowTransferBrokerage(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateSubstitutePolicy(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowRequestClientInfo(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowRequestAgencyInfo(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateInsuredObjectFromClient(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowTransferManager(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowExecuteDetailedCalculations(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateInfoManagementProcess(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateSubPolicy(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowIssueDebitNote(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateNegotiation(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateHealthExpense(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateRiskAnalisys(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowTransferToClient(boolean allow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<SubPolicyStub> getSubPoliciesList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<ReceiptStub> getReceiptsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getExpensesList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		// TODO Auto-generated method stub
		return null;
	}

}
