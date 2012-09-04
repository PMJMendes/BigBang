package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;

public class CreateQuoteRequestInfoOrDocumentRequestViewPresenter extends
InfoOrDocumentRequestViewPresenter<QuoteRequest> {

	private QuoteRequestBroker broker;

	public CreateQuoteRequestInfoOrDocumentRequestViewPresenter(Display<QuoteRequest> view) {
		super(view);
		this.broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		parameterHolder.setParameter("ownertypeid", BigBangConstants.EntityIds.QUOTE_REQUEST);
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
		broker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
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
		broker.getQuoteRequest(ownerId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				handler.onResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_INFO_OR_DOCUMENT_REQUEST));
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
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Cosulta de Mercado"), TYPE.ALERT_NOTIFICATION));
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
