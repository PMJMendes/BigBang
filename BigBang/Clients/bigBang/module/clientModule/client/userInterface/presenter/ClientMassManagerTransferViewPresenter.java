package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter;

public class ClientMassManagerTransferViewPresenter extends MassManagerTransferViewPresenter<ClientStub, Client> {

	protected ClientProcessBroker clientBroker;

	public ClientMassManagerTransferViewPresenter(Display<ClientStub, Client> view){
		super(view);
		this.clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
	}

	@Override
	protected void onTransfer(String newManagerId,
			Collection<ClientStub> affectedProcesses) {

		String[] processIds = new String[affectedProcesses.size()];
		
		int i = 0;
		for(ClientStub c : affectedProcesses){
			processIds[i] = c.id;
			i++;
		}
		
		clientBroker.createManagerTransfer(processIds, newManagerId, new ResponseHandler<ManagerTransfer>() {

			@Override
			public void onResponse(ManagerTransfer response) {
				onTransferSuccess();
				view.refreshMainList();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onTransferFailed();
			}
		});
	}
	
	protected void bind() {
		super.bind();
		this.view.setOperationFilter(BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER);
		this.view.refreshMainList();
		this.view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selectable = (ValueSelectable<ClientStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getSelectedList().clearSelection();
					clientBroker.getClient(selectable.getValue().id, new ResponseHandler<Client>() {

						@Override
						public void onResponse(Client response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetClientFailed();
						}
					});
				}else if(!view.getSelectedList().getSelected().isEmpty()){
					view.getSelectedProcessForm().setValue(null);
				}
			}
		});
		this.view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selectable = (ValueSelectable<ClientStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getMainList().clearSelection();
					clientBroker.getClient(selectable.getValue().id, new ResponseHandler<Client>() {

						@Override
						public void onResponse(Client response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetClientFailed();
						}
					});
				}else if(!view.getMainList().getSelected().isEmpty()){
					view.getSelectedProcessForm().setValue(null);
				}
			}
		});
	}

	@Override
	protected void onCancel() {
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void checkUserPermission(final ResponseHandler<Boolean> handler) {
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.CLIENT, BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER, new ResponseHandler<Boolean>() {
			
			@Override
			public void onResponse(Boolean response) {
				handler.onResponse(response);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onResponse(false);
			}
		});
	}

	@Override
	protected void onUserLacksPermission() {
//		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
	}

	@Override
	protected void onTransferSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Transferência de Gestor for criada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void onTransferFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onGetClientFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Cliente seleccionado"), TYPE.ALERT_NOTIFICATION));
	}

}
