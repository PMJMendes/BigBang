package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
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

public class CasualtyInfoOrDocumentRequestViewPresenter extends InfoOrDocumentRequestViewPresenter<Casualty>{

	private CasualtyDataBroker broker;

	public CasualtyInfoOrDocumentRequestViewPresenter(Display<Casualty> view) {
		super(view);
		broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
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
		broker.getCasualty(ownerId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
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
		broker.getCasualty(ownerId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				handler.onResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.CasualtyProcess.CREATE_INFO_REQUEST));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}

	@Override
	protected void onSend() {
		if(view.getForm().validate()) {
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
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
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
	protected void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o sinistro"), TYPE.ALERT_NOTIFICATION));
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.CASUALTY);
		super.setParameters(parameterHolder);
	}

}
