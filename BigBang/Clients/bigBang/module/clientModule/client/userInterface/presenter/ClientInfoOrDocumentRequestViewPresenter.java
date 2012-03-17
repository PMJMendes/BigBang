package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;

public class ClientInfoOrDocumentRequestViewPresenter extends
		InfoOrDocumentRequestViewPresenter<Client> {

	private ClientProcessBroker broker;
	
	public ClientInfoOrDocumentRequestViewPresenter(Display<Client> view) {
		super(view);
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
		super.setParameters(parameterHolder);
	}
	
	@Override
	protected void onSend() {
		InfoOrDocumentRequest request = view.getForm().getInfo();
		broker.createInfoOrDocumentRequest(request, new ResponseHandler<InfoOrDocumentRequest>() {
			
			@Override
			public void onResponse(InfoOrDocumentRequest response) {
				view.getForm().setValue(response);
				onSendRequestSuccess();
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.popFromStackParameter("display");
				NavigationHistoryManager.getInstance().go(item);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});
	}

	@Override
	protected InfoOrDocumentRequest getFormattedRequest(String ownerId,
			String ownerTypeId) {
		InfoOrDocumentRequest request = new InfoOrDocumentRequest();
		request.parentDataObjectId = ownerId;
		request.parentDataTypeId = ownerTypeId;
		return request;
	}

	@Override
	protected void showOwner(String ownerId, String ownerTypeId) {
		broker.getClient(ownerId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getOwnerForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	@Override
	protected void checkOwnerPermissions(String ownerId, String ownerTypeId,
			final ResponseHandler<Boolean> handler) {
		broker.getClient(ownerId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				handler.onResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.ClientProcess.CREATE_INFO_REQUEST));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}
	
	@Override
	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o cliente"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	@Override
	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para criar o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onGenericError() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ocorreu um erro ao apresentar o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
}
