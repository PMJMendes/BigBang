package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.RiskAnalysis;
import bigBang.library.client.BigBangPermissionManager;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.ScreenInvokedEvent;
import bigBang.library.client.event.ScreenInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.event.ShowMeRequestEvent;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.Permission;
import bigBang.module.clientModule.client.userInterface.view.CreateInsurancePolicyView;
import bigBang.module.clientModule.interfaces.ClientServiceAsync;
import bigBang.module.clientModule.shared.ModuleConstants;
import bigBang.module.clientModule.shared.operation.ClientSearchOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ClientSearchOperationViewPresenter implements OperationViewPresenter {

	public static enum Action{
		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE,
		SHOW_HISTORY,

		CREATE_POLICY,
		CREATE_RISK_ANALISYS,
		CREATE_QUOTE_REQUEST,
		CREATE_CASUALTY,

		MERGE_WITH_CLIENT,
		TRANSFER_MANAGER,

		REQUIRE_INFO_DOCUMENT
	}

	public interface Display {
		//List
		HasValueSelectables<?> getList();

		//Form
		HasEditableValue<Client> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//Operations
		HasWidgets showPolicyForm(boolean show);

		void showRiskAnalisysForm(boolean show);
		HasEditableValue<RiskAnalysis> getRiskAnalisysForm();
		boolean isRiskAnalisysFormValid();
		void lockRiskAnalisysForm(boolean lock);

		void showQuoteRequestForm(boolean show);
		HasEditableValue<QuoteRequest> getQuoteRequestForm();
		boolean isQuoteRequestFormValid();
		void lockQuoteRequestForm(boolean lock);

		void showCasualtyForm(boolean show);
		HasEditableValue<Casualty> getCasualtyForm();
		boolean isCasualtyFormValid();
		void lockCasualtyForm(boolean lock);

		void showClientMergeForm(boolean show);
		HasEditableValue<Client> getClientMergeForm();
		boolean isClientMergeFormValid();
		void lockClientMergeForm(boolean lock);

		void showManagerTransferForm(boolean show);
		HasEditableValue<String> getManagerTransferForm();
		boolean isManagerTransferFormValid();
		void lockManagerTransferForm(boolean lock);

		void showInfoOrDocumentRequestForm(boolean show);
		HasEditableValue<InfoOrDocumentRequest> getInfoOrDocumentRequestForm();
		boolean isInfoOrDocumentFormValid();
		void lockInfoOrDocumentRequestForm(boolean lock);

		void showDeleteForm(boolean show);
		HasEditableValue<String> getDeleteForm();
		boolean isDeleteFormValid();
		void lockDeleteForm(boolean lock);

		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowUpdate(boolean allow);
		void allowDelete(boolean allow);
		void allowRequestInfoOrDocument(boolean allow);
		void allowManagerTransfer(boolean allow);
		void allowClientMerge(boolean allow);
		void allowCreatePolicy(boolean allow);
		void allowCreateRiskAnalysis(boolean allow);
		void allowCreateQuoteRequest(boolean allow);
		void allowcreateCasualty(boolean allow);

		//History
		HasValueSelectables<HistoryItemStub> getHistoryList();
		void showHistory(Client process, String selectedItemId);
		
		//policies
		HasValueSelectables<InsurancePolicyStub> getPolicyList();
		
		//General
		void clear();
		void selectClient(Client client);
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewClient();
		void removeNewClientPreparation();
		void setSaveModeEnabled(boolean enabled);
		void setReadOnly(boolean readOnly);
		void showMessage(String message);
		void showErrorMessage(String message);
		void showWarningMessage(String message);
		void scrollFormToTop();

		Widget asWidget();
		View getInstance();
	}

	protected Display view;
	private EventBus eventBus;
	private ClientSearchOperation operation;

	protected ClientProcessBroker clientBroker;
	protected int clientDataVersionNumber;
	protected boolean bound = false;

	public ClientSearchOperationViewPresenter(EventBus eventBus, ClientServiceAsync service, View view){
		this.setService((Service) service);
		this.setView(view);
		this.setEventBus(eventBus);
		this.clientBroker = (ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT);
	}

	public void setService(Service service) {}

	public void setEventBus(final EventBus eventBus) {
		if(this.eventBus != null || eventBus == null)
			return;
		this.eventBus = eventBus;
		registerEventHandlers(eventBus);
	}

	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		view.setReadOnly(true);
		container.add(this.view.asWidget());
	}

	//The compact version of the operation view
	public void goCompact(HasWidgets container){
		go(container);
		/*this.bind();
		container.clear();
		container.add((Widget)this.view.getSearchPreviewPanelContainer());*/
	}

	public void bind() {
		if(bound) {return;}
		bound = true;
		view.lockForm(true);
		this.view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				view.setSaveModeEnabled(false);
				view.scrollFormToTop();
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selected = (ValueSelectable<ClientStub>) event.getFirstSelected();
				final ClientStub selectedValue = selected == null ? null : selected.getValue();
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewClientPreparation();
						view.lockForm(true);
						BigBangPermissionManager.Util.getInstance().getProcessPermissions(selectedValue.processId, new ResponseHandler<Permission[]> () {

							@Override
							public void onResponse(Permission[] response) {
								view.clearAllowedPermissions();
								for(int i = 0; i < response.length; i++) {
									Permission p = response[i];
									if(p.instanceId == null){continue;}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CreateClient)){
										view.allowCreate(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.ChangeClientData)){
										view.allowUpdate(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.DELETE_CLIENT)){
										view.allowDelete(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CREATE_MANAGER_TRANSFER)){
										view.allowManagerTransfer(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CREATE_INFO_OR_DOCUMENT_REQUEST)){
										view.allowRequestInfoOrDocument(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.MERGE_CLIENT)){
										view.allowClientMerge(true);
									}
									if(p.id.equalsIgnoreCase(ModuleConstants.OpTypeIDs.CREATE_POLICY)){
										view.allowCreatePolicy(true);
									}
								}
								clientBroker.getClient(selectedValue.id, new ResponseHandler<Client>() {

									@Override
									public void onResponse(Client value) {
										view.getForm().setValue(value);
										view.lockForm(value == null);
									}

									@Override
									public void onError(Collection<ResponseError> errors) {
										view.showErrorMessage("Não foi possível mostrar a informação para o cliente. Por favor tente mais tarde.");
									}
								});
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								view.showErrorMessage("Não foi possível mostrar a informação para o cliente. Por favor tente mais tarde.");
							}

						});
					}
				}
			}
		});
		this.view.registerActionInvokedHandler(new ActionInvokedEventHandler<ClientSearchOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewClient();

					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<Client> vs = (ValueSelectable<Client>) s;
						Client value = vs.getValue();
						view.getForm().setValue(value);
						break;
					}
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					Client info = view.getForm().getInfo();
					if(info.id == null)
						addClient(info);
					else
						updateClient(info);
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getValue().id == null){
						view.removeNewClientPreparation();
					}else{
						view.getForm().revert();
						view.getForm().setReadOnly(true);
						view.setSaveModeEnabled(false);
					}
					break;
				case DELETE:
					deleteClient();
					break;
				case REFRESH:
					clientBroker.requireDataRefresh();
					clientBroker.getClient(view.getForm().getValue().id, new ResponseHandler<Client>() {

						@Override
						public void onResponse(Client response) {
							view.getForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							view.showErrorMessage("Não foi possível actualizar a informação para o cliente. Por favor tente mais tarde.");
						}
					});
					break;
				case CREATE_CASUALTY:
					createCasualty();
					break;
				case CREATE_POLICY:
					createPolicy();
					break;
				case CREATE_QUOTE_REQUEST:
					createQuoteRequest();
					break;
				case CREATE_RISK_ANALISYS:
					createRiskAnalisys();
					break;
				case MERGE_WITH_CLIENT:
					mergeWithClient();
					break;
				case TRANSFER_MANAGER:
					transferManager();
					break;
				case REQUIRE_INFO_DOCUMENT:
					requireInfoOrDocument();
					break;
				}
			}

			protected void createCasualty(){
				view.getCasualtyForm().setValue(null);
				view.showCasualtyForm(true);
				view.lockCasualtyForm(false);
			}

			protected void createPolicy(){
				HasWidgets container = view.showPolicyForm(true);

				CreateInsurancePolicyView createPolicyView = new CreateInsurancePolicyView();
				CreateInsurancePolicyViewPresenter presenter = new CreateInsurancePolicyViewPresenter(null, createPolicyView) {

					@Override
					public void onPolicyCreated() {
						ClientSearchOperationViewPresenter.this.view.showPolicyForm(false);
					}

					@Override
					public void onCreationCancelled() {
						ClientSearchOperationViewPresenter.this.view.showPolicyForm(false);
					}

				};
				presenter.setClient(view.getForm().getValue());
				presenter.go(container);
			}

			protected void createQuoteRequest(){
				view.getQuoteRequestForm().setValue(null);
				view.showQuoteRequestForm(true);
				view.lockQuoteRequestForm(false);
			}

			protected void createRiskAnalisys(){
				view.getRiskAnalisysForm().setValue(null);
				view.showRiskAnalisysForm(true);
				view.lockRiskAnalisysForm(false);
			}

			protected void mergeWithClient(){
				Client receptor = view.getClientMergeForm().getValue();

				if(receptor != null && receptor.id != null){
					clientBroker.mergeWithClient(receptor.id, view.getForm().getValue().id, new ResponseHandler<Client>() {

						@Override
						public void onResponse(Client response) {
							view.getForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							view.showErrorMessage("De momento não foi possível fundir os clientes. Por favor tente mais tarde.");
							GWT.log("Não foi possível fundir os clientes : "+errors.toString());
						}
					});
				}
			}

			protected void transferManager(){
				ClientStub client = view.getForm().getValue();
				String[] clientIds = new String[]{
						client.id
				};
				String[] clientProcessIds = new String[]{
						client.processId
				};
				String managerId = view.getManagerTransferForm().getValue();
				clientBroker.createManagerTransfer(clientProcessIds, clientIds, managerId, new ResponseHandler<ManagerTransfer>() {

					@Override
					public void onResponse(ManagerTransfer response) {
						view.showManagerTransferForm(false);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						view.showErrorMessage("Não foi possível criar a transferência de gestor de cliente. Por favor, tente mais tarde.");
					}
				});
			}

			protected void requireInfoOrDocument(){
				InfoOrDocumentRequest request = view.getInfoOrDocumentRequestForm().getValue();

				clientBroker.createInfoOrDocumentRequest(request, new ResponseHandler<InfoOrDocumentRequest>() {

					@Override
					public void onResponse(InfoOrDocumentRequest response) {
						view.showInfoOrDocumentRequestForm(false);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						view.showErrorMessage("Não foi possível criar o pedido de informação. Por favor tente mais tarde.");
					}
				});
			}

			protected void deleteClient(){
				Client client = view.getForm().getValue();
				if(client == null || client.id == null || client.processId == null) {
					return;
				}
				view.getDeleteForm().commit();
				clientBroker.removeClient(client.id, view.getDeleteForm().getValue(), new ResponseHandler<String>() {

					@Override
					public void onResponse(String response) {
						view.showDeleteForm(false);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						view.showErrorMessage("Não foi possível eliminar o cliente. Por favor, tente mais tarde.");
					}
				});
			}

		});
		this.view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> vs = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				if(vs != null){
					view.showHistory(view.getForm().getValue(), vs.getValue().id);
				}
			}
		});
		this.view.getPolicyList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> vs = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				if(vs != null) {
					eventBus.fireEvent(new ScreenInvokedEvent(ScreenInvokedEvent.OPERATION_TYPE_READ, BigBangConstants.EntityIds.INSURANCE_POLICY, vs.getValue().id));
					view.getPolicyList().clearSelection();
				}
			}
		});
	}

	public void setOperation(final ClientSearchOperation operation) {
		this.operation = operation;
	}

	public void registerEventHandlers(final EventBus eventBus) {
		if(eventBus == null) {setEventBus(eventBus);}
		eventBus.addHandler(ScreenInvokedEvent.TYPE, new ScreenInvokedEventHandler() {
			
			@Override
			public void onScreenInvoked(ScreenInvokedEvent event) {
				if(event.getProcessTypeId().equalsIgnoreCase(BigBangConstants.EntityIds.CLIENT)){
					String clientId = event.getTargetId();
					final String operationId = event.getOperationId();
					
					clientBroker.getClient(clientId, new ResponseHandler<Client>() {
						
						@Override
						public void onResponse(Client response) {
							if(operationId.equalsIgnoreCase(ScreenInvokedEvent.OPERATION_TYPE_READ)){
								view.selectClient(response);
								eventBus.fireEvent(new ShowMeRequestEvent(view));
							}
							
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
						}
					});
				}
			}
		});
	}

	private void showClientProcess(final String processId){
		this.clientBroker.getClient(processId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				throw new RuntimeException("Could not get the client process with id=\"" + processId + "\"");
			}
		});
	}

	public void setOperation(Operation o) {
		this.operation = (ClientSearchOperation)o;
	}

	public Operation getOperation() {
		return this.operation;
	}

	public String setTargetEntity(String id) {
		showClientProcess(id);
		return id;
	}

	@Override
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	private void setReadOnly(boolean result) {
		this.view.setReadOnly(true);
	}

	public void addClient(Client client) {
		clientBroker.addClient(client, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.removeNewClientPreparation();
				view.selectClient(response);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void updateClient(Client client) {
		clientBroker.updateClient(client, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.removeNewClientPreparation();
				view.selectClient(response);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

}
