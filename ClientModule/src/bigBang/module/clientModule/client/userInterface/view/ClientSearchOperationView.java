package bigBang.module.clientModule.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalisys;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.PopupBar;
import bigBang.library.client.userInterface.SlidePanel;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.ClientProcessToolBar;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSearchOperationViewPresenter.Action;

import com.google.gwt.event.logical.shared.AttachEvent;
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

	protected ActionInvokedEventHandler<ClientSearchOperationViewPresenter.Action> actionHandler;

	public ClientSearchOperationView(){
		mainWrapper = new SlidePanel();
		mainWrapper.setSize("100%", "100%");

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		searchPanel = new ClientSearchPanel();
		searchPanel.setSize("100%", "100%");
		wrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		wrapper.setWidgetMinSize(searchPanel, SEARCH_PANEL_WIDTH);

		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");

		VerticalPanel sideBarWrapper = new VerticalPanel();
		sideBarWrapper.setSize("100%", "100%");

		this.childProcessesBar = new PopupBar();
		this.childProcessesBar.setSize("100%", "100%");

		/*Widget testContent =  new ClientSearchPanel(); //TODO
		testContent.setSize("300px", "300px");
		this.childProcessesBar.addItem(new PopupBar.Item("Apólices", testContent));

		Widget testContent2 =  new ClientSearchPanel();
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
			public void onRequestInfoOrDocument() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientSearchOperationViewPresenter.Action>(Action.REQUIRE_INFO_DOCUMENT));
			}
		};

		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");

		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		formWrapper.add(form);

		contentWrapper.add(formWrapper);

		wrapper.add(contentWrapper);
		form.lock(true);

		mainWrapper.add(wrapper);
		mainContent = wrapper;
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
	public HasEditableValue<RiskAnalisys> getRiskAnalisysForm() {
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
		if(show){
			this.popup = new PopupPanel(true, "Pedido de Informação ou documento");
			InfoOrDocumentRequestView form = new InfoOrDocumentRequestView() {
				
				@Override
				public void onSendButtonPressed() {
					showInfoOrDocumentRequestForm(false);
				}
			};
			form.setSize("660px", "605px");
			this.popup.add(form);
			this.popup.addAttachHandler(new AttachEvent.Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						
						popup.setSize("660px", "605px");
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
		//TODO
	}

	@Override
	public void removeNewClientPreparation() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}

}
