package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class ClientMergeViewPresenter implements ViewPresenter {

	public static enum Action {
		MERGE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<Client> getSourceClientForm();
		HasEditableValue<Client> getTargetClientForm();
		HasValueSelectables<ClientStub> getList();

		void confirmMerge(ResponseHandler<Boolean> confirm);
		void registerActionHandler(ActionInvokedEventHandler<Action> actionHandler);
		Widget asWidget();
	}

	private Display view;
	private ClientProcessBroker broker;
	private boolean bound = false;

	public ClientMergeViewPresenter(Display view){
		this.broker = ((ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT));
		setView((UIObject) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String sourceId = parameterHolder.getParameter("clientid");
		sourceId = sourceId == null ? new String() : sourceId;
		String targetId = parameterHolder.getParameter("targetid");
		targetId = targetId == null ? new String() : targetId;

		if(sourceId.isEmpty()){
			onGetSourceClientError();
		}else{
			showSourceClient(sourceId);
		}

		if(targetId.isEmpty()){
			clearTargetClient();
		}else{
			showTargetClient(targetId);
		}
	}

	private void bind() {
		if(bound){return;}

		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<ClientStub> selected = (ValueSelectable<ClientStub>) event.getFirstSelected();
				ClientStub selectedValue = selected == null ? null : selected.getValue();
				String clientId = selectedValue == null ? new String() : selectedValue.id;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(clientId.isEmpty()){
					item.removeParameter("targetid");
				}else{
					item.setParameter("targetid", clientId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});
		
		view.registerActionHandler(new ActionInvokedEventHandler<ClientMergeViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case MERGE:
					doMerge();
					break;
				case CANCEL:
					onMergeOperationCancelled();
					break;
				}
			}
		});

		bound = true;
	}

	private void showSourceClient(String clientId){
		broker.getClient(clientId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getSourceClientForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetSourceClientError();
			}
		});
	} 

	private void showTargetClient(String targetId){
		broker.getClient(targetId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				view.getTargetClientForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetTargetClientError();
			}
		});
	}

	private void clearTargetClient(){
		view.getTargetClientForm().setValue(null);
		view.getList().clearSelection();
	}
	
	private void doMerge(){
		String sourceClientId = view.getSourceClientForm().getValue().id;
		String targetClientId = view.getTargetClientForm().getValue().id;
		
		broker.mergeWithClient(sourceClientId, targetClientId, new ResponseHandler<Client>() {

			@Override
			public void onResponse(Client response) {
				onMergeOperationSuccess(response.id);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onMergeOperationFailed();
			}
		});
	}
	
	private void onGetSourceClientError(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o cliente-origem"), TYPE.ALERT_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("targetid");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		item.removeParameter("clientid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetTargetClientError(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível o cliente seleccionado"), TYPE.ALERT_NOTIFICATION));

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("targetid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onMergeOperationSuccess(String targetId){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("targetid");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		item.setParameter("clientid", targetId);
		NavigationHistoryManager.getInstance().go(item);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O cliente foi fundido com sucesso."), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onMergeOperationCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("targetid");
		item.setStackParameter("display");
		item.pushIntoStackParameter("display", "search");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onMergeOperationFailed(){
		
	}
	
}
