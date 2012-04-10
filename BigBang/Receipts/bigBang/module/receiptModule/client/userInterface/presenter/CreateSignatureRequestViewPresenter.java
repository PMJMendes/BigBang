package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class CreateSignatureRequestViewPresenter implements ViewPresenter{

	private ReceiptProcessDataBroker broker;
	private Display view;
	private boolean bound;
	
	public enum Action{
		CREATE_SIGNATURE_REQUEST,
		CANCEL
	}
	
	public interface Display{
		
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<SignatureRequest> getForm();
		
	}
	
	public CreateSignatureRequestViewPresenter(Display view){
		broker = (ReceiptProcessDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		setView((UIObject)view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		view.getForm().setValue(null);
		SignatureRequest request = new SignatureRequest();
		request.receiptId = parameterHolder.getParameter("receiptid");
		view.getForm().setValue(request);
		
	}
	
	public void bind(){
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<CreateSignatureRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CREATE_SIGNATURE_REQUEST:
					onCreateSignatureRequest();
					break;
					
				}
				
			}
		});
	}

	protected void onCreateSignatureRequest() {
		broker.createSignatureRequest(view.getForm().getValue(), new ResponseHandler<Receipt>() {
			
			@Override
			public void onResponse(Receipt response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.removeParameter("show");
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Assinatura criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o pedido de assinatura."), TYPE.ALERT_NOTIFICATION));
				
			}
		});
		
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
