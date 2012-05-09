package bigBang.module.clientModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientChildrenPanel;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanelListEntry;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {

	protected final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)

	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	protected ClientChildrenPanel childrenPanel;
	protected ClientProcessToolBar operationsToolbar; 
	protected ToolButton newButton;

	protected ActionInvokedEventHandler<ClientSearchOperationViewPresenter.Action> actionHandler;

	public ClientSearchOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader("Clientes");
		header.showNewButton("Novo");
		this.newButton = header.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.NEW));
			}
		});

		searchPanelWrapper.add(header);

		searchPanel = new ClientSearchPanel();
		searchPanel.setSize("100%", "100%");

		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");
		searchPanelWrapper.setSize("100%", "100%");

		wrapper.addWest(searchPanelWrapper, SEARCH_PANEL_WIDTH);
		wrapper.setWidgetMinSize(searchPanelWrapper, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		this.childrenPanel = new ClientChildrenPanel();
		this.childrenPanel.setSize("100%", "100%");		

		contentWrapper.addEast(this.childrenPanel, 250);

		final VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		operationsToolbar = new ClientProcessToolBar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}

			@Override
			public void onCreatePolicy() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.CREATE_POLICY));
			}

			@Override
			public void onCreateRiskAnalisys() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.CREATE_RISK_ANALISYS));
			}

			@Override
			public void onCreateQuoteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.CREATE_QUOTE_REQUEST));
			}

			@Override
			public void onCreateCasualty() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.CREATE_CASUALTY));
			}

			@Override
			public void onMergeWithClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.MERGE_WITH_CLIENT));
			}

			@Override
			public void onTransferToManager() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.TRANSFER_MANAGER));
			}

			@Override
			public void onRequestInfoOrDocument() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.REQUIRE_INFO_DOCUMENT));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.DELETE));
			}
		};

		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");

		this.form.addValueChangeHandler(new ValueChangeHandler<Client>() {
			@Override
			public void onValueChange(ValueChangeEvent<Client> event) {
				Client client = event.getValue();
				childrenPanel.setClient(client);
			}
		});

		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);

		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
		form.lock(true);
		
		searchPanel.doSearch();
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	public HasSelectables<?> getClientSearchList() {
		return this.searchPanel;
	}

	@Override
	public void selectClient(String clientId) {
		this.searchPanel.clearSelection();
		for(ValueSelectable<ClientStub> s : this.searchPanel) {
			if(s != null && s.getValue().id.equalsIgnoreCase(clientId)){
				s.setSelected(true, false);
				break;
			}
		}
	}

	@Override
	public HasValueSelectables<ClientStub> getList() {
		return this.searchPanel;
	}
	
	@Override
	public void removeFromList(ValueSelectable<ClientStub> selectable) {
		this.searchPanel.remove(selectable);
	}

	@Override
	public HasEditableValue<Client> getForm() {
		return this.form;
	}

	@Override
	public boolean isFormValid() {
		return this.form.validate();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewClient(Client client) {
		for(ListEntry<ClientStub> s : this.searchPanel){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(client);
		this.searchPanel.add(0, entry);
		this.searchPanel.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}

	@Override
	public void scrollFormToTop() {
		this.form.scrollToTop();
	}
	
	/*## PERMISSIONS START ##*/
	
	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void clearAllowedPermissions() {
		this.form.setReadOnly(true);
		this.operationsToolbar.lockAll();
	}
	
	@Override
	public void allowCreate(boolean allow) {
		this.newButton.setEnabled(allow);
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
	public void allowRequestInfoOrDocument(boolean allow) {
		this.operationsToolbar.allowInfoOrDocumentRequest(allow);
	}

	@Override
	public void allowManagerTransfer(boolean allow) {
		this.operationsToolbar.allowManagerTransfer(allow);
	}

	@Override
	public void allowClientMerge(boolean allow) {
		this.operationsToolbar.allowClientMerge(allow);
	}

	@Override
	public void allowCreatePolicy(boolean allow) {
		this.operationsToolbar.allowCreatePolicy(allow);
	}

	@Override
	public void allowCreateRiskAnalysis(boolean allow) {
		this.operationsToolbar.allowCreateRiskAnalysis(allow);
	}

	@Override
	public void allowCreateQuoteRequest(boolean allow) {
		this.operationsToolbar.allowCreateQuoteRequest(allow);
	}

	@Override
	public void allowcreateCasualty(boolean allow) {
		this.operationsToolbar.allowCreateCasualty(allow);
	}
	
	/*## PERMISSIONS END ##*/
	
	/*## CHILDREN LISTS START ##*/

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public HasValueSelectables<InsurancePolicyStub> getPolicyList() {
		return this.childrenPanel.insurancePoliciesList;
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
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return this.childrenPanel.subProcessesList;
	}
	
	@Override
	public HasValueSelectables<QuoteRequestStub> getQuoteRequestList() {
		return this.childrenPanel.quoteRequestsList;
	}

	@Override
	public HasValueSelectables<CasualtyStub> getCasualtyList() {
		return this.childrenPanel.casualtiesList;
	}

	@Override
	public void setForCreation(boolean forCreation) {
		if(forCreation) {
			form.setForCreate();
		}else{
			form.setForEdit();
		}
	}
	
	/*## CHILDREN LISTS END ##*/
	
}
