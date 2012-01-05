package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.InsuredObjectStub;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.SlidePanel;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyChildrenPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyOperationsToolBar;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySearchOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InsurancePolicySearchOperationView extends View implements InsurancePolicySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected SlidePanel slideWrapper;
	protected Widget mainContent;
	protected InsurancePolicySearchPanel searchPanel;
	protected InsurancePolicyForm form;
	protected InsurancePolicyOperationsToolBar operationsToolBar;
	protected InsurancePolicyChildrenPanel childrenPanel; 
	protected ActionInvokedEventHandler<Action> actionHandler;

	public InsurancePolicySearchOperationView(){
		slideWrapper = new SlidePanel();
		slideWrapper.setSize("100%", "100%");
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainContent = mainWrapper;
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new InsurancePolicySearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		childrenPanel = new InsurancePolicyChildrenPanel();
		childrenPanel.setSize("100%", "100%");
		contentWrapper.addEast(childrenPanel, 300);
		
		final VerticalPanel toolBarFormContainer = new VerticalPanel();
		toolBarFormContainer.setSize("100%", "100%");
		
		operationsToolBar = new InsurancePolicyOperationsToolBar(){

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
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CREATE_INSURED_OBJECT));
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
			public void onIssueCreditNote() {
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
				actionHandler.onActionInvoked(new ActionInvokedEvent<InsurancePolicySearchOperationViewPresenter.Action>(Action.CANCEL));
			}
			
		};

		operationsToolBar.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					toolBarFormContainer.setCellHeight(operationsToolBar, "21px");
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

		toolBarFormContainer.add(operationsToolBar);
		toolBarFormContainer.add(formWrapper);
		
		contentWrapper.add(toolBarFormContainer);
		
		mainWrapper.add(contentWrapper);
		
		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
		
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
		initWidget(slideWrapper);
	}
	
	@Override
	public HasValueSelectables<?> getList() {
		return this.searchPanel;
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
	public void lockForm(boolean lock) {
		form.setReadOnly(lock);
	}

	@Override
	public void clear() {
		searchPanel.clearSelection();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewPolicy() {
		for(ListEntry<InsurancePolicyStub> s : this.searchPanel){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		InsurancePolicySearchPanel.Entry entry = new InsurancePolicySearchPanel.Entry(new InsurancePolicy());
		this.searchPanel.add(0, entry);
		this.searchPanel.getScrollable().scrollToTop();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewPolicyPreparation() {
		for(ValueSelectable<InsurancePolicyStub> s : this.searchPanel){
			if(s.getValue().id == null){
				this.searchPanel.remove(s);
				break;
			}
		}
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearAllowedPermissions() {
		this.form.setReadOnly(true);
		//this.childrenPanel.setReadOnly(true); todo
		this.operationsToolBar.lockAll();
	}

	@Override
	public HasWidgets showCreateReceiptForm(boolean show) {
		if(show){
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "100%");
	
			ListHeader header = new ListHeader();
			header.setText("Criação de Recibo");
			header.setLeftWidget(new Button("Voltar", new ClickHandler() {
	
				@Override
				public void onClick(ClickEvent event) {
					slideWrapper.slideInto(mainContent, Direction.RIGHT);
				}
			}));
			wrapper.add(header);
	
			SimplePanel viewContainer = new SimplePanel();
			viewContainer.setSize("100%", "100%");
	
			wrapper.add(viewContainer);
			wrapper.setCellHeight(viewContainer, "100%");
	
			slideWrapper.slideInto(
					wrapper, Direction.LEFT);
			return viewContainer;
		}else{
			slideWrapper.slideInto(
					mainContent, Direction.RIGHT);
			return null;
		}
	}

	@Override
	public HasEditableValue<Receipt> getNewReceiptForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNewReceiptFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockNewReceiptForm(boolean lock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void allowCreateReceipt(boolean allow) {
		this.operationsToolBar.allowCreateReceipt(allow);
	}

	@Override
	public void allowUpdate(boolean allow) {
		this.operationsToolBar.setEditionAvailable(allow);
		//this.childrenPanel.setReadOnly(!allow); TODO
	}

	@Override
	public void allowDelete(boolean allow) {
		this.operationsToolBar.allowDelete(allow);
	}
	
	public void allowCreateInsuredObject(boolean allow) {
		this.operationsToolBar.allowCreateInsuredObject(allow);
	}

	@Override
	public String getInsuredObjectTableFilter() {
		return this.form.getTable().getInsuredObjectFilterValue();
	}

	@Override
	public String getExerciseTableFilter() {
		return this.form.getTable().getExerciseFilterValue();
	}

	@Override
	public TableSection getCurrentTablePage() {
		return this.form.getTable().getData();
	}

	@Override
	public HasWidgets showCreateInsuredObjectView(boolean show) {
		if(show){
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "100%");
			
			ListHeader header = new ListHeader("Criação de Unidade de Risco");
			wrapper.add(header);
			header.setLeftWidget(new Button("Voltar", new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					showCreateInsuredObjectView(false);
				}
			}));
			
			SimplePanel container = new SimplePanel();
			container.setSize("100%", "100%");
			wrapper.add(container);			
			wrapper.setCellHeight(container, "100%");
			slideWrapper.slideInto(wrapper, Direction.LEFT);
			return container;
		}else{
			slideWrapper.slideInto(mainContent, Direction.RIGHT);
		}
		return null;
	}
	

	@Override
	public void showHistory(InsurancePolicy policy, String selectedItemId) {
		UndoOperationView historyView = new UndoOperationView();
		UndoOperationViewPresenter presenter = new UndoOperationViewPresenter(null,
				(HistoryBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY),
				historyView, policy.id,
				policy.processId,
				BigBangConstants.EntityIds.INSURANCE_POLICY);
		presenter.setTargetEntity(selectedItemId);
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader();
		header.setText("Histórico da Apólice Nº" + policy.number);
		header.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				slideWrapper.slideInto(mainContent, Direction.RIGHT);
			}
		}));
		wrapper.add(header);

		SimplePanel viewContainer = new SimplePanel();
		viewContainer.setSize("100%", "100%");
		presenter.go(viewContainer);

		wrapper.add(viewContainer);
		wrapper.setCellHeight(viewContainer, "100%");

		slideWrapper.slideInto(wrapper, Direction.LEFT);
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
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}
	
}
