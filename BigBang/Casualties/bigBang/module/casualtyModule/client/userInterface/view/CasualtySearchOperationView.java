package bigBang.module.casualtyModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.casualtyModule.client.userInterface.CasualtyChildrenPanel;
import bigBang.module.casualtyModule.client.userInterface.CasualtyProcessToolBar;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanel;
import bigBang.module.casualtyModule.client.userInterface.form.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter;
import bigBang.module.casualtyModule.client.userInterface.presenter.CasualtySearchOperationViewPresenter.Action;

public class CasualtySearchOperationView extends View implements CasualtySearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX
	
	protected CasualtySearchPanel searchPanel;
	protected CasualtyForm form;
	protected CasualtyProcessToolBar operationsToolbar;
	protected CasualtyChildrenPanel childrenPanel;
	
	protected ActionInvokedEventHandler<Action> actionHandler;
	
	public CasualtySearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);		
		mainWrapper.setSize("100%", "100%");
		
		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");
		
		ListHeader header = new ListHeader("Sinistros");
		searchPanelWrapper.add(header);
		
		searchPanel = new CasualtySearchPanel();
		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");
		
		mainWrapper.addWest(searchPanelWrapper, SEARCH_PANEL_WIDTH);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new CasualtyProcessToolBar() {

			@Override
			public void onCreateSubCasualty() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.CREATE_SUB_CASUALTY));
			}

			@Override
			public void onClose() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.CLOSE));

			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.DELETE));

			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.EDIT));

			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.SAVE));

			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.SAVE));

			}
			
			@Override
			public void onTransferManager() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.TRANSFER_MANAGER));
				
			}

			@Override
			public void onInfoOrDocumentRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CasualtySearchOperationViewPresenter.Action>(Action.INFO_DOCUMENT_REQUEST));
				
			}
			
		};
		this.form = new CasualtyForm();

		this.childrenPanel = new CasualtyChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");
		mainWrapper.addEast(this.childrenPanel, 250);

		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);
		mainWrapper.add(formWrapper);
		
		this.form.addValueChangeHandler(new ValueChangeHandler<Casualty>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Casualty> event) {
				childrenPanel.setCasualty(event.getValue());
			}
		});
		
		searchPanel.doSearch();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<CasualtyStub> getList() {
		return this.searchPanel;
	}

	@Override
	public HasEditableValue<Casualty> getForm() {
		return this.form;
	}

	@Override
	public void clearAllowedPermissions() {
		this.operationsToolbar.lockAll();
	}

	@Override
	public void allowEdit(boolean allow) {
		this.operationsToolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.operationsToolbar.allowDelete(allow);
	}

	@Override
	public void allowClose(boolean allow) {
		this.operationsToolbar.allowClose(allow);
	}
	
	@Override
	public void allowTransferManager(boolean allow) {
		this.operationsToolbar.allowManagerTransfer(allow);
	}
	
	@Override
	public void allowCreateSubCasualty(boolean allow) {
		this.operationsToolbar.allowCreateSubCasualty(allow);		
	}

	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return this.childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return this.childrenPanel.documentsList;
	}

	@Override
	public HasValueSelectables<SubCasualtyStub> getSubCasualtyList() {
		return this.childrenPanel.subCasualtyList;
	}
	
	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return this.childrenPanel.subProcessesList;
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void allowInfoOrDocumentRequest(boolean hasPermission) {
		operationsToolbar.allowInfoOrDocumentRequest(hasPermission);
	}

}
