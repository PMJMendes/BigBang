package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.ExerciseChooser;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyDetailsForm;
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
	private InsurancePolicyDetailsForm detailsForm;

	public InsurancePolicySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");

		ListHeader searchPanelHeader = new ListHeader("Apólices");
		searchPanelWrapper.add(searchPanelHeader);

		searchPanel = new InsurancePolicySearchPanel(){ //TODO FAZ SENTIDO MANTER ESTE SEARCHPANEL?

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

		SplitLayoutPanel formPanel = new SplitLayoutPanel();

		policyForm = new InsurancePolicyForm();
		//TODO ADD INSURANCE, OBJECT AND EXERCISE FORM

		objectsList = new InsuredObjectSearchPanel(null); //TODO METER AQUI O BROKER
		formPanel.addWest(objectsList, 200);
		formPanel.add(policyForm);
		formPanel.setSize("100%", "100%");
		formContainer.add(formPanel);
		formContainer.setCellHeight(formPanel, "100%");

		VerticalPanel rightWrapper = new VerticalPanel();

		rightWrapper.setSize("100%", "100%");

		rightWrapper.add(toolbar);
		rightWrapper.add(formContainer);
		rightWrapper.setCellHeight(formContainer, "100%");
		VerticalPanel detailTableContainer = new VerticalPanel();
		detailTableContainer.setSize("100%", "100%");
		
		exerciseChooser = new ExerciseChooser();
		detailTableContainer.add(exerciseChooser);

		detailsForm = new InsurancePolicyDetailsForm("");
		detailTableContainer.add(detailsForm.getNonScrollableContent());
		
		rightWrapper.add(detailTableContainer);
		rightWrapper.setCellHeight(detailTableContainer, "100%");

		contentWrapper.add(rightWrapper);
		mainWrapper.add(contentWrapper);

	}

	@Override
	protected void initializeView() {}

	@Override
	public void setReadOnly(boolean readOnly){
		policyForm.setReadOnly(readOnly);
		exerciseChooser.setReadOnly(readOnly);
		detailsForm.setReadOnly(readOnly);
	}
	
	@Override
	public void setValue(InsurancePolicy result) {
		policyForm.setValue(result);
		exerciseChooser.setValue(result.exerciseData);
		detailsForm.setValue(result.exerciseData[0]);
		detailsForm.setExerciseDetailSectionName("Detalhes do exercício "+ result.exerciseData[0].label + " para a Apólice");
		detailsForm.fillTable(result.coverages, result.columns, result.exerciseData[0].columnFields);
	}

}
