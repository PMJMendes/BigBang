package bigBang.module.clientModule.client.userInterface.view;

import java.util.Collection;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.PopupBar;
import bigBang.library.client.userInterface.SlidePanel;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientChildrenPanel;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanelListEntry;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter.Action;

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

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {

	protected final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)

	protected SlidePanel mainWrapper;
	protected PopupPanel popup;
	protected Widget mainContent;
	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	protected ClientChildrenPanel childrenPanel;
	protected ClientProcessToolBar operationsToolbar; 
	protected ClientManagerTransferView managerTransferView;
	protected InfoOrDocumentRequestView infoOrDocumentRequestView;
	protected ClientMergeView clientMergeView;
	protected DeleteClientView clientDeleteView;
	protected PopupBar childProcessesBar;
	protected ToolButton newButton;

	protected ActionInvokedEventHandler<ClientSearchOperationViewPresenter.Action> actionHandler;

	public ClientSearchOperationView(){
		mainWrapper = new SlidePanel();
		mainWrapper.setSize("100%", "100%");

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
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

		this.childProcessesBar = new PopupBar();
		this.childProcessesBar.setSize("100%", "100%");

		Widget testContent =  new ClientSearchPanel(); //TODO
		testContent.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Apólices", testContent));

		/*Widget testContent2 =  new ClientSearchPanel();
		testContent2.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Sinistros", testContent2));

		Widget testContent3 =  new ClientSearchPanel();
		testContent3.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("C. Mercado", testContent3));

		Widget testContent4 =  new ClientSearchPanel();
		testContent4.setSize("400px", "400px");
		this.childProcessesBar.addItem(new PopupBar.Item("A. Risco", testContent4));

		Widget testContent5 =  new ClientSearchPanel();
		testContent5.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Reclamação", testContent5));
		 */

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
				getClientMergeForm().setValue(null);
				clientMergeView.getOriginalForm().setValue(form.getValue());
				showClientMergeForm(true);
				lockClientMergeForm(true);
			}

			@Override
			public void onTransferToManager() {
				getManagerTransferForm().setValue(null);
				showManagerTransferForm(true);
				lockManagerTransferForm(false);
			}

			@Override
			public void onRequestInfoOrDocument() {
				getInfoOrDocumentRequestForm().setValue(null);
				showInfoOrDocumentRequestForm(true);
				lockInfoOrDocumentRequestForm(false);
			}

			@Override
			public void onHistory() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.SHOW_HISTORY));
			}

			@Override
			public void onDelete() {
				getDeleteForm().setValue(null);
				showDeleteForm(true);
				lockDeleteForm(false);
			}

			@Override
			public void onRefresh() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.REFRESH));
			}
		};

		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");

		this.form.addValueChangeHandler(new ValueChangeHandler<Client>() {
			@Override
			public void onValueChange(ValueChangeEvent<Client> event) {
				Client client = event.getValue();
				if(client != null){
					if(client.id != null){
						childrenPanel.setClient(event.getValue());
					}else{
						childrenPanel.clear();
					}
				}else{
					childrenPanel.clear();
					
				}
				childrenPanel.setReadOnly(client == null); //TODO FJVC
			}
		});

		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);

		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
		form.lock(true);

		mainWrapper.add(wrapper);
		mainContent = wrapper;

		this.managerTransferView = new ClientManagerTransferView() {

			@Override
			public void onTransferButtonPressed(String managerId) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.TRANSFER_MANAGER));
			}
		};
		this.infoOrDocumentRequestView = new InfoOrDocumentRequestView() {

			@Override
			public void onSendButtonPressed() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.REQUIRE_INFO_DOCUMENT));
			}

			@Override
			public void onBackButtonPressed() {
				showInfoOrDocumentRequestForm(false);
			}
		};
		this.clientMergeView = new ClientMergeView() {

			@Override
			public void onMergeButtonPressed() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.MERGE_WITH_CLIENT));
			}

			@Override
			public void onBackButtonPressed() {
				showClientMergeForm(false);
			}
		};
		this.clientDeleteView = new DeleteClientView() {

			@Override
			public void onDeleteButtonPressed() {
				this.confirmDelete(new ResponseHandler<Boolean>() {

					@Override
					public void onResponse(Boolean response) {
						if(response){
							actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.DELETE));
						}
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		};

		addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					childrenPanel.setClient(form.getValue());
				}
			}
		});

		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
		initWidget(mainWrapper);
	}

	public HasSelectables<?> getClientSearchList() {
		return this.searchPanel;
	}

	@Override
	public void selectClient(Client client) {
		this.searchPanel.clearSelection();
		for(ValueSelectable<ClientStub> s : this.searchPanel) {
			if(s != null && s.getValue().id.equalsIgnoreCase(client.id)){
				s.setSelected(true, false);
				break;
			}
		}
		this.form.setValue(client, true);
		if(this.mainWrapper.getCurrentWidget() != this.mainContent){
			this.mainWrapper.slideInto(mainContent, Direction.LEFT);
		}
	}

	@Override
	public void setReadOnly(boolean readonly) {
		form.lock(readonly);
	}


	public View getInstance() {
		return new ClientSearchOperationView();
	}

	@Override
	public HasValueSelectables<?> getList() {
		return this.searchPanel;
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
	public void lockForm(boolean lock) {
		this.form.lock(lock);
		this.operationsToolbar.setEditionAvailable(!lock);
	}

	@Override
	public void clear() {
		this.form.clearInfo();
	}

	@Override
	public HasWidgets showPolicyForm(boolean show) {
		if(show){
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "100%");

			ListHeader header = new ListHeader();
			header.setText("Criação de Apólice");
			header.setLeftWidget(new Button("Voltar", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					mainWrapper.slideInto(mainContent, Direction.RIGHT);
				}
			}));
			wrapper.add(header);

			SimplePanel viewContainer = new SimplePanel();
			viewContainer.setSize("100%", "100%");

			wrapper.add(viewContainer);
			wrapper.setCellHeight(viewContainer, "100%");

			mainWrapper.slideInto(
					wrapper, Direction.LEFT);
			return viewContainer;
		}else{
			mainWrapper.slideInto(
					mainContent, Direction.RIGHT);
			return null;
		}
	}

	@Override
	public void showRiskAnalisysForm(boolean show) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasEditableValue<RiskAnalysis> getRiskAnalisysForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRiskAnalisysFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockRiskAnalisysForm(boolean lock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showQuoteRequestForm(boolean show) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasEditableValue<QuoteRequest> getQuoteRequestForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isQuoteRequestFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockQuoteRequestForm(boolean lock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showCasualtyForm(boolean show) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasEditableValue<Casualty> getCasualtyForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCasualtyFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockCasualtyForm(boolean lock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showClientMergeForm(boolean show) {
		if(show){
			mainWrapper.slideInto(this.clientMergeView, Direction.LEFT);
		}else{
			mainWrapper.slideInto(mainContent, Direction.RIGHT);
		}
	}

	@Override
	public HasEditableValue<Client> getClientMergeForm() {
		return this.clientMergeView.getReceptorForm();
	}

	@Override
	public boolean isClientMergeFormValid() {
		return true;
	}

	@Override
	public void lockClientMergeForm(boolean lock) {
		this.clientMergeView.getOriginalForm().setReadOnly(lock);
	}

	@Override
	public void showInfoOrDocumentRequestForm(boolean show) {
		if(show){
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "100%");

			ListHeader header = new ListHeader();
			header.setText("Criação de Pedido de Informação ou Documento");
			header.setLeftWidget(new Button("Voltar", new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showInfoOrDocumentRequestForm(false);
				}
			}));
			wrapper.add(header);

			SimplePanel viewContainer = new SimplePanel();
			viewContainer.setSize("100%", "100%");

			this.infoOrDocumentRequestView.setClient(this.form.getValue());
			viewContainer.add(this.infoOrDocumentRequestView);

			wrapper.add(viewContainer);
			wrapper.setCellHeight(viewContainer, "100%");

			mainWrapper.slideInto(
					wrapper, Direction.LEFT);
		}else{
			mainWrapper.slideInto(mainContent, Direction.RIGHT);
		}
	}

	@Override
	public void showDeleteForm(boolean show) {
		if(show){
			this.popup = new PopupPanel(true, "Eliminação de Cliente");
			Widget view = this.clientDeleteView;
			view.setSize("660px", "100px");
			this.popup.add(view);
			this.popup.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						popup.setSize("660px", "100px");
					}
				}
			});
			this.popup.center();
		}else{
			this.popup.hidePopup();
			this.popup.clear();
			this.popup.removeFromParent();
		}
	}

	@Override
	public HasEditableValue<String> getDeleteForm() {
		return this.clientDeleteView.form;
	}

	@Override
	public boolean isDeleteFormValid() {
		return this.clientDeleteView.form.validate();
	}

	@Override
	public void lockDeleteForm(boolean lock) {
		this.clientDeleteView.form.setReadOnly(lock);
	}

	@Override
	public HasEditableValue<InfoOrDocumentRequest> getInfoOrDocumentRequestForm() {
		return this.infoOrDocumentRequestView.form;
	}

	@Override
	public boolean isInfoOrDocumentFormValid() {
		return this.infoOrDocumentRequestView.form.validate();
	}

	@Override
	public void lockInfoOrDocumentRequestForm(boolean lock) {
		this.infoOrDocumentRequestView.form.setReadOnly(lock);
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewClient() {
		for(ListEntry<ClientStub> s : this.searchPanel){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(new Client());
		this.searchPanel.add(0, entry);
		this.searchPanel.getScrollable().scrollToTop();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewClientPreparation() {
		for(ValueSelectable<ClientStub> s : this.searchPanel){
			if(s.getValue().id == null){
				this.searchPanel.remove(s);
				break;
			}
		}
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void showHistory(Client process, String selectedItemId) {
		UndoOperationView historyView = new UndoOperationView();
		UndoOperationViewPresenter presenter = new UndoOperationViewPresenter(null,
				(HistoryBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY),
				historyView, process.id,
				process.processId, BigBangConstants.EntityIds.CLIENT);
		presenter.setTargetEntity(selectedItemId);
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader();
		header.setText("Histórico do Processo Cliente Nº" + process.clientNumber + " (" + process.name + ")");
		header.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainWrapper.slideInto(mainContent, Direction.RIGHT);
			}
		}));
		wrapper.add(header);

		SimplePanel viewContainer = new SimplePanel();
		viewContainer.setSize("100%", "100%");
		presenter.go(viewContainer);

		wrapper.add(viewContainer);
		wrapper.setCellHeight(viewContainer, "100%");

		mainWrapper.slideInto(
				wrapper, Direction.LEFT);
	}

	@Override
	public void showManagerTransferForm(boolean show) {
		if(show){
			this.popup = new PopupPanel(true, "Transferência de Gestor");
			managerTransferView.setSize("660px", "100px");
			this.popup.add(managerTransferView);
			this.popup.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						popup.setSize("660px", "100px");
					}
				}
			});
			this.popup.center();
		}else{
			this.popup.hidePopup();
			this.popup.clear();
			this.popup.removeFromParent();
		}
	}

	@Override
	public HasEditableValue<String> getManagerTransferForm() {
		return this.managerTransferView.form;
	}

	@Override
	public boolean isManagerTransferFormValid() {
		return this.managerTransferView.form.validate();
	}

	@Override
	public void lockManagerTransferForm(boolean lock) {
		this.managerTransferView.form.setReadOnly(lock);
	}

	@Override
	public void clearAllowedPermissions() {
		this.form.setReadOnly(true);
		this.childrenPanel.setReadOnly(true);
		this.operationsToolbar.lockAll();
	}

	@Override
	public void allowCreate(boolean allow) {
		this.newButton.setEnabled(allow);
	}

	@Override
	public void allowUpdate(boolean allow) {
		this.operationsToolbar.setEditionAvailable(allow);
		this.childrenPanel.setReadOnly(!allow);
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
	
	@Override
	public void scrollFormToTop() {
		this.form.scrollToTop();
	}

	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public HasValueSelectables<InsurancePolicyStub> getPolicyList() {
		return this.childrenPanel.insurancePoliciesList;
	}
	
}
