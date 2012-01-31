package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.SlidePanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter.Action;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected SlidePanel slideWrapper;
	protected Widget mainContent;
	protected HasWidgets childrenPresentersPanel;
	protected InsurancePolicySearchPanel searchPanel;
	protected InsurancePolicyForm form;
	protected InsurancePolicyOperationsToolBar toolbar;
	protected InsurancePolicyChildrenPanel childrenPanel; 
	protected ActionInvokedEventHandler<Action> actionHandler;

	public InsurancePolicySearchOperationView(){
		slideWrapper = new SlidePanel();
		initWidget(slideWrapper);
		slideWrapper.setSize("100%", "100%");
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainContent = mainWrapper;
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new InsurancePolicySearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		SimplePanel childrenPresentersPanel = new SimplePanel();
		childrenPresentersPanel.setSize("100%", "100%");
		this.childrenPresentersPanel = childrenPresentersPanel;
		
		childrenPanel = new InsurancePolicyChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		contentWrapper.addEast(childrenPanel, 300);
		
		final VerticalPanel toolBarFormContainer = new VerticalPanel();
		toolBarFormContainer.setSize("100%", "100%");
		
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
			public void onCreateInsuredObject() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_INSURED_OBJECT));
			}
			
			@Override
			public void onCreateExercise() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_EXERCISE));
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
			public void onIssueCreditNote() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.ISSUE_CREDIT_NOTE));
			}

			@Override
			public void onCreateNegotiation() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_NEGOTIATION));
			}

			@Override
			public void onCreateHealthExpense() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_HEALTH_EXPENSE));
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

		toolbar.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					toolBarFormContainer.setCellHeight(toolbar, "21px");
				}
			}
		});
		
		SplitLayoutPanel formWrapper = new SplitLayoutPanel();
		formWrapper.setSize("100%", "100%");
				
		form = new InsurancePolicyForm(){

			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
			
		};
		formWrapper.add(form);
		form.setSize("100%", "100%");
		form.setForEdit();

		toolBarFormContainer.add(toolbar);
		toolBarFormContainer.add(formWrapper);
		
		contentWrapper.add(toolBarFormContainer);
		
		mainWrapper.add(contentWrapper);
		
		form.addValueChangeHandler(new ValueChangeHandler<InsurancePolicy>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<InsurancePolicy> event) {
				InsurancePolicy policy = event.getValue();
				if(policy == null){
					childrenPanel.clear();
				}else{
					childrenPanel.setPolicy(policy);
				}
			}
		});
		slideWrapper.add(mainWrapper);
		
		this.searchPanel.doSearch();
	}
	
	@Override
	protected void initializeView() {}
	
	@Override
	public HasValueSelectables<InsurancePolicyStub> getList() {
		return this.searchPanel;
	}
	
	@Override
	public void selectInsurancePolicy(String policyId) {
		for(ValueSelectable<InsurancePolicyStub> selectable : this.searchPanel){
			InsurancePolicyStub policy = selectable.getValue();
			if(policy.id.equalsIgnoreCase(policyId)){
				selectable.setSelected(true, false);
				break;
			}
		}
	}

	@Override
	public void scrollFormToTop() {
		this.form.scrollToTop();
	}

	@Override
	public HasEditableValue<InsurancePolicy> getForm() {
		return this.form;
	}

	@Override
	public boolean isFormValid() {
		return form.validate();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	/*## PERMISSIONS START ##*/
	
	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		this.toolbar.allowCreateReceipt(allow);
	}

	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}
	
	public void allowCreateInsuredObject(boolean allow) {
		this.toolbar.allowCreateInsuredObject(allow);
	}
	
	@Override
	public void allowCreateExercise(boolean allow) {
		this.toolbar.allowCreateExercise(allow);
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
	public void allowIssueCreditNote(boolean allow) {
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
	public void allowValidatePolicy(boolean allow) {
		toolbar.allowValidate(allow);
	}
	
	/*## PERMISSIONS END ##*/

	@Override
	public HasValue<String> getInsuredObjectTableFilter() {
		return this.form.getTable().getInsuredObjectFilterValue();
	}

	@Override
	public HasValue<String> getExerciseTableFilter() {
		return this.form.getTable().getExerciseFilterValue();
	}

	@Override
	public TableSection getCurrentTablePage() {
		return this.form.getTable().getData();
	}

	//Gets lists
	
	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return this.childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return this.childrenPanel.documentsList;
	}

	@Override
	public HasValueSelectables<InsuredObjectStub> getObjectsList() {
		return this.childrenPanel.insuredObjectsList;
	}
	
	@Override
	public HasValueSelectables<ExerciseStub> getExercisesList() {
		return this.childrenPanel.exercisesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void removeFromList(ValueSelectable<InsurancePolicyStub> selectable) {
		this.searchPanel.remove(selectable);
	}

}
