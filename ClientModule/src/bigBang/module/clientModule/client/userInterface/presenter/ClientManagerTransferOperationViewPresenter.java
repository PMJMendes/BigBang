package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter;

public class ClientManagerTransferOperationViewPresenter extends MassManagerTransferViewPresenter<Client> {

	protected ClientProcessBroker clientBroker;

	public ClientManagerTransferOperationViewPresenter(Display<Client> view){
		super(view);
		this.clientBroker = ((ClientProcessBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT));
	}

	@Override
	protected void onTransfer(String newManagerId,
			Collection<Client> affectedProcesses) {

		String[] processIds = new String[affectedProcesses.size()];
		
		int i = 0;
		for(Client c : affectedProcesses){
			processIds[i] = c.id;
			i++;
		}
		
		clientBroker.createManagerTransfer(processIds, newManagerId, new ResponseHandler<ManagerTransfer>() {

			@Override
			public void onResponse(ManagerTransfer response) {
				onTransferSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onTransferFailed();
			}
		});
	}

	@Override
	protected void onCancel() {
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void checkUserPermission(ResponseHandler<Boolean> handler) {
		handler.onResponse(true); //TODO
	}

	@Override
	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
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

}
