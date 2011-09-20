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
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.PopupBar;
import bigBang.library.client.userInterface.SlidePanel;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.presenter.UndoOperationViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanelListEntry;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter.Action;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationView extends View implements ClientSearchOperationViewPresenter.Display {

	protected final int SEARCH_PANEL_WIDTH = 400; //minimum and starting width (px)
	//protected final int SEARCH_PREVIEW_PANEL_WIDTH = 400;

	protected SlidePanel mainWrapper;
	protected PopupPanel popup;
	protected Widget mainContent;
	protected ClientSearchPanel searchPanel;
	protected ClientFormView form;
	protected ContactsPreviewList contactsList;
	protected ClientProcessToolBar operationsToolbar; 
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

		VerticalPanel sideBarWrapper = new VerticalPanel();
		sideBarWrapper.setSize("100%", "100%");

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

		sideBarWrapper.add(childProcessesBar);
		sideBarWrapper.setCellWidth(childProcessesBar, "100%");

		this.contactsList = new ContactsPreviewList();
		this.contactsList.setSize("100%", "100%");

		sideBarWrapper.add(contactsList);
		sideBarWrapper.setCellHeight(this.contactsList, "100%");

		contentWrapper.addEast(sideBarWrapper, 250);

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
			public void onHistory() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.SHOW_HISTORY));
			}

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.DELETE));
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
						contactsList.setContactProcessAndOperationAndOwner(client.processId, ModuleConstants.OpTypeIDs.ChangeClientData, client.id);
					}else{
						contactsList.clearAll();
					}
				}else{
					contactsList.clearAll();
				}
				contactsList.setReadOnly(client == null); //TODO FJVC
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

		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
		initWidget(mainWrapper);
	}

	public HasSelectables<?> getClientSearchList() {
		return this.searchPanel;
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
	public void showPolicyForm(boolean show) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasEditableValue<InsurancePolicy> getPolicyForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPolicyFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockPolicyForm(boolean lock) {
		// TODO Auto-generated method stub

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
			mainWrapper.slideInto(
					new ClientMergeView() {

						@Override
						public void onMergeButtonPressed() {

							this.confirmMerge(new ResponseHandler<Boolean>() {

								@Override
								public void onResponse(Boolean response) {
									if(response){
										//TODO Merge
										onBackButtonPressed();
									}
								}

								@Override
								public void onError(Collection<ResponseError> errors) {}
							});
						}

						@Override
						public void onBackButtonPressed() {
							mainWrapper.slideInto(mainContent, Direction.RIGHT);
						}
					}, Direction.LEFT);
		}else{
			mainWrapper.slideInto(mainContent, Direction.RIGHT);
		}
	}

	@Override
	public HasEditableValue<Casualty> getClientMergeForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClientMergeFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockClientMergeForm(boolean lock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showInfoOrDocumentRequestForm(boolean show) {
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		ListHeader header = new ListHeader();
		header.setText("Pedido de Informação ou Documento");
		header.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mainWrapper.slideInto(mainContent, Direction.RIGHT);
			}
		}));
		wrapper.add(header);

		SimplePanel viewContainer = new SimplePanel();
		viewContainer.setSize("100%", "100%");
		InfoOrDocumentRequestView requestView = new InfoOrDocumentRequestView() {

			@Override
			public void onSendButtonPressed() {
				onBackButtonPressed();
			}

			@Override
			public void onBackButtonPressed() {
				mainWrapper.slideInto(mainContent, Direction.RIGHT);
			}

		};
		requestView.setClient(this.form.getValue());
		viewContainer.add(requestView);

		wrapper.add(viewContainer);
		wrapper.setCellHeight(viewContainer, "100%");

		mainWrapper.slideInto(
				wrapper, Direction.LEFT);
	}

	@Override
	public void showDeleteForm(boolean show) {
		if(show){
			this.popup = new PopupPanel(true, "Eliminação de Cliente");
			DeleteClientView view = new DeleteClientView() {

				@Override
				public void onDeleteButtonPressed() {
					this.confirmDelete(new ResponseHandler<Boolean>() {

						@Override
						public void onResponse(Boolean response) {
							if(response){
								showDeleteForm(false);
							}
						}

						@Override
						public void onError(Collection<ResponseError> errors) {}
					});

				}
			};
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDeleteFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockDeleteForm(boolean lock) {
		// TODO Auto-generated method stub

	}

	@Override
	public HasEditableValue<Casualty> getInfoOrDocumentRequestForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInfoOrDocumentFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockInfoOrDocumentRequestForm(boolean lock) {
		// TODO Auto-generated method stub

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
		ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(new ValueWrapper<ClientStub>(new Client()));
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
	public void showHistory(Client process) {
		UndoOperationView historyView = new UndoOperationView();
		UndoOperationViewPresenter presenter = new UndoOperationViewPresenter(null,
				(HistoryBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.HISTORY),
				historyView,
				process.processId);
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
			ClientManagerTransferView view = new ClientManagerTransferView() {

				@Override
				public void onTransferButtonPressed() {
					showManagerTransferForm(false); //TODO
				}
			};
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
	public HasEditableValue<String> getManagerTransferForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isManagerTransferFormValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void lockManagerTransferForm(boolean lock) {
		// TODO Auto-generated method stub

	}

}
