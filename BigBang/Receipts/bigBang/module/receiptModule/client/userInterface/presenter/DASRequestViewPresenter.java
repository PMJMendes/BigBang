package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.DASRequestBroker;
import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DASRequest;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DASRequestViewPresenter implements ViewPresenter{

	public static enum Action{
		CANCEL,
		REPEAT_DAS_REQUEST,
		RECEIVE_REPLY,
	}

	protected Display view;
	protected boolean bound = false;
	private DASRequestBroker broker;
	private ReceiptDataBroker receiptBroker;
	private String dasRequestId;

	public DASRequestViewPresenter(Display view){
		setView((UIObject)view);
		broker = (DASRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DAS_REQUEST);
		receiptBroker = (ReceiptDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
	}

	public static interface Display{
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<DASRequest> getForm();
		void allowCancel(boolean hasPermission);
		void allowReceiveResponse(boolean hasPermission);
		void allowRepeatRequest(boolean hasPermission);
		void disableToolbar();
		HasSelectables<ValueSelectable<HistoryItemStub>> getHistoryList();
		HasEditableValue<Receipt> getOwnerForm();
		void applyOwnerToList(String negotiationId);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;

	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	private void bind(){
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<DASRequestViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					cancelDASRequest();
					break;
				case RECEIVE_REPLY:
					receiveReply();
					break;
				case REPEAT_DAS_REQUEST:
					repeatReceiveRequest();
					break;
				}
			}
		});
		
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				HistoryItemStub selectedValue = event.getFirstSelected() == null ? null : ((ValueSelectable<HistoryItemStub>) event.getFirstSelected()).getValue();
				if(selectedValue != null) {
					showHistory(selectedValue);
				}
			}
		});

		bound = true;

	}

	private void showHistory(final HistoryItemStub historyItem) {

		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.pushIntoStackParameter("display", "history");
		item.setParameter("historyownerid", view.getForm().getValue().id);
		item.setParameter("historyitemid", historyItem.id);
		NavigationHistoryManager.getInstance().go(item);

	}

	protected void receiveReply() {
		DASRequest.Response response = new DASRequest.Response();
		response.requestId = view.getForm().getInfo().id;

		broker.receiveResponse(response, new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.popFromStackParameter("display");
				item.removeParameter("dasrequestid");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Resposta recebida com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao receber a resposta à Declaração de Ausência de Sinistro."), TYPE.ALERT_NOTIFICATION));
			}
		});

	}

	protected void cancelDASRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "canceldasrequest");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void repeatReceiveRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "repeatdasrequest");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		dasRequestId = parameterHolder.getParameter("dasrequestid");
		view.applyOwnerToList(dasRequestId);
		broker.getRequest(dasRequestId, new ResponseHandler<DASRequest>() {

			@Override
			public void onResponse(DASRequest response) {
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
				view.allowCancel(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.DASRequestProcess.CANCEL_DAS_REQUEST));
				view.allowReceiveResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.DASRequestProcess.RECEIVE_REPLY));
				view.allowRepeatRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.DASRequestProcess.REPEAT_DAS_REQUEST));

				receiptBroker.getReceipt(response.receiptId, new ResponseHandler<Receipt>() {

					@Override
					public void onResponse(Receipt response) {
						view.getOwnerForm().setValue(response);
						view.getOwnerForm().setReadOnly(true);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o recibo."), TYPE.ALERT_NOTIFICATION));
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o pedido de Declaração de Ausência de Sinistros."), TYPE.ALERT_NOTIFICATION));
				view.getForm().setReadOnly(true);
				view.disableToolbar();
			}
		});

	}



}
