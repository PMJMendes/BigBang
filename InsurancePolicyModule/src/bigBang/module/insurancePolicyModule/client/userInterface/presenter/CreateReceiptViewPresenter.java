package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
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
import bigBang.library.client.userInterface.view.View;

public class CreateReceiptViewPresenter implements ViewPresenter {

	public static enum Action{
		CREATE_RECEIPT,
		CANCEL_RECEIPT_CREATION
	}

	public static interface Display {
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasEditableValue<Receipt> getForm();
		void setPolicyInfo(InsurancePolicy policy);
		boolean isFormValid();
		Widget asWidget();
		void showReceiptCreationFailedErrorMessage();
	}

	protected Display view;
	protected boolean bound = false;
	protected InsurancePolicyBroker insurancePolicyBroker;
	private String policyId;

	public CreateReceiptViewPresenter(Display view){
		this.insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
		setView((View) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		bind();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String policyId = parameterHolder.getParameter("id");
		this.policyId = policyId == null ? new String() : policyId;
		
		if(policyId.isEmpty()){
			onReceiptCreationFailed();
		}else{
			createReceipt(policyId);
		}
	}
	
	private void bind() {
		if(bound){
			return;
		}
		bound = true;
		view.registerActionHandler(new ActionInvokedEventHandler<CreateReceiptViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CREATE_RECEIPT:
					createReceipt(CreateReceiptViewPresenter.this.policyId);
					break;
				case CANCEL_RECEIPT_CREATION:
					onReceiptCreationCancelled();
					break;
				}
			}
		});
	}

	private void createReceipt(String policyId){
		if(view.isFormValid()) {
			Receipt receipt = view.getForm().getInfo();
			this.insurancePolicyBroker.createReceipt(this.policyId, receipt, new ResponseHandler<Receipt>() {
				
				@Override
				public void onResponse(Receipt response) {
					view.getForm().setValue(response);
					onReceiptCreated();
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					onReceiptCreationFailed();
				}
			});
		}
	}
	
	private void onReceiptCreationCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("operation", "search");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onReceiptCreationFailed(){
		view.showReceiptCreationFailedErrorMessage();
	}
	
	private void onReceiptCreated(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("operation", "search");
		NavigationHistoryManager.getInstance().go(item);
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O recibo foi criado com sucesso."), TYPE.TRAY_NOTIFICATION));
	}

}
