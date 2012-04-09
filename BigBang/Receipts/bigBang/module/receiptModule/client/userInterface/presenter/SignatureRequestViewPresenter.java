package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.dataAccess.SignatureRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SignatureRequestViewPresenter implements ViewPresenter{

	public static enum Action{
		CANCEL,
		REPEAT_SIGNATURE_REQUEST,
		RECEIVE_REPLY
	}
	
	protected Display view;
	protected boolean bound = false;
	private SignatureRequestBroker signatureBroker;
	private ReceiptProcessDataBroker receiptBroker;
	private String signatureRequestId;
	
	public SignatureRequestViewPresenter(Display view) {
		setView((UIObject) view);
		signatureBroker = (SignatureRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SIGNATURE_REQUEST);
		receiptBroker = (ReceiptProcessDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
	}
	
	public static interface Display{
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<SignatureRequest> getForm();
		void allowCancel(boolean hasPermission);
		void allowReceiveResponse(boolean hasPermission);
		void allowRepeatRequest(boolean hasPermission);
		void disableToolbar();
		HasEditableValue<Receipt> getOwnerForm();
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
		
		view.registerActionHandler(new ActionInvokedEventHandler<SignatureRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					cancelSignatureRequest();
					break;
				case RECEIVE_REPLY:
					receiveReply();
					break;
				case REPEAT_SIGNATURE_REQUEST:
					repeatReceiveRequest();
					break;
				}
			}
		});
		
		bound = true;
	}

	protected void repeatReceiveRequest() {
		signatureBroker.repeatRequest(view.getForm().getInfo(), new ResponseHandler<SignatureRequest>() {
			
			@Override
			public void onResponse(SignatureRequest response) {
				NavigationHistoryManager.getInstance().reload();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao repetir o pedido de assinatura."), TYPE.ALERT_NOTIFICATION));
			}
		});
		
	}

	protected void receiveReply() {
		SignatureRequest.Response response = new SignatureRequest.Response();
		response.requestId = view.getForm().getInfo().id;
		
		signatureBroker.receiveResponse(response, new ResponseHandler<SignatureRequest>() {
			
			@Override
			public void onResponse(SignatureRequest response) {
				NavigationHistoryManager.getInstance().reload();	
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Erro ao receber a resposta ao pedido de assinatura."), TYPE.ALERT_NOTIFICATION));
			}
		});

	}

	protected void cancelSignatureRequest() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "cancelsignaturerequest");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		signatureRequestId = parameterHolder.getParameter("signaturerequestid");
		
		signatureBroker.getRequest(signatureRequestId, new ResponseHandler<SignatureRequest>() {
			
			@Override
			public void onResponse(SignatureRequest response) {
				view.getForm().setInfo(response);
				view.getForm().setReadOnly(true);
				view.allowCancel(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SignatureRequestProcess.CANCEL_SIGNATURE_REQUEST));
				view.allowReceiveResponse(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SignatureRequestProcess.RECEIVE_REPLY));
				view.allowRepeatRequest(PermissionChecker.hasPermission(response, BigBangConstants.OperationIds.SignatureRequestProcess.REPEAT_SIGNATURE_REQUEST));
			
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
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível mostrar o pedido de assinatura."), TYPE.ALERT_NOTIFICATION));
				view.getForm().setReadOnly(true);
				view.disableToolbar();
			}
		});
		
	}
	
	

}
