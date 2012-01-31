package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;

public class ClientInfoOrDocumentRequestViewPresenter extends
		InfoOrDocumentRequestViewPresenter {

	public static interface Display extends InfoOrDocumentRequestViewPresenter.Display {
		HasEditableValue<Client> getOwnerForm();
	}
	
	private ClientProcessBroker broker;
	private String clientId;
	
	public ClientInfoOrDocumentRequestViewPresenter(Display view) {
		super(view);
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clientId = parameterHolder.getParameter("id");
		clientId = clientId == null ? new String() : clientId;
		String requestId = parameterHolder.getParameter("requestid");
		requestId = requestId == null ? new String() : requestId;
		
		if(clientId.isEmpty()){
			clearView();
		}else{
			if(requestId.isEmpty()){
				showNewRequest();
			}else{
				showRequest(requestId);
			}
		}
	}

	private void showRequest(final String requestId){
		broker.getClient(clientId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				//TODO check permissions FJVC
				broker.getClientSubProcess(clientId, requestId, new ResponseHandler<BigBangProcess>() {

					@Override
					public void onResponse(BigBangProcess response) {
						//InfoOrDocumentRequest request = (InfoOrDocumentRequest) response; //TODO
						//view.getForm().setValue(request);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetRequestFailed();
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}
	
	private void showNewRequest(){
		
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
				item.removeParameter("operation");
				NavigationHistoryManager.getInstance().go(item);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onSendRequestFailed();
			}
		});
	}

	@Override
	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o cliente"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onGetRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o pedido de informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
}
