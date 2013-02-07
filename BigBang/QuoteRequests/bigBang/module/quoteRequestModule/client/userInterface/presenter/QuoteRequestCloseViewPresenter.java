package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class QuoteRequestCloseViewPresenter implements ViewPresenter {

	public static enum Action {
		CLOSE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected QuoteRequestBroker broker;
	protected Display view;
	protected boolean bound = false;
	protected String requestId;

	public QuoteRequestCloseViewPresenter(Display view){
		this.broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		requestId = parameterHolder.getParameter("quoterequestid");
		if(requestId != null && !requestId.isEmpty()){
			showClose();
		}else{
			onCloseFailed();
		}
	}

	protected void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<QuoteRequestCloseViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CLOSE:
					onClose();
					break;
				case CANCEL:
					onCancel();
					break;
				}				
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}

	protected void showClose(){
		this.broker.getQuoteRequest(requestId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST)){
					onCloseFailed();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCloseFailed();
			}
		});
	}

	protected void onClose(){
		this.broker.getQuoteRequest(requestId, new ResponseHandler<QuoteRequest>() {

			@Override
			public void onResponse(QuoteRequest response) {
//				if(!PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.QuoteRequestProcess.CLOSE_QUOTE_REQUEST)){
//					onCloseFailed();
//				}else{
//					broker.closeQuoteRequest(response.id, view.getForm().getInfo(), new ResponseHandler<QuoteRequest>() {
//
//						@Override
//						public void onResponse(QuoteRequest response) {
//							onCloseSuccess();
//						}
//
//						@Override
//						public void onError(Collection<ResponseError> errors) {
//							onCloseFailed();
//						}
//					});
//				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCloseFailed();
			}
		});
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar a Consulta de Mercado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCloseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Consulta de Mercado Encerrada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("quoterequestid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}

}
