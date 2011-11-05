package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.Receipt;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class CreateReceiptViewPresenter implements ViewPresenter {

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
	}

	protected EventBus eventBus;
	protected Display view;
	protected boolean bound = false;
	protected InsurancePolicyBroker insurancePolicyBroker;

	public CreateReceiptViewPresenter(EventBus eventBus, Display view){
		setEventBus(eventBus);
		setView((View) view);
		this.insurancePolicyBroker = (InsurancePolicyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
	}

	@Override
	public void setService(Service service) {
		return;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		bind();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound){
			return;
		}
		bound = true;
		view.registerActionHandler(new ActionInvokedEventHandler<CreateReceiptViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CREATE_RECEIPT:
					createReceipt();
					break;
				case CANCEL_RECEIPT_CREATION:
					onCreationCancelled();
					break;
				}
			}
		});
	}

	public void setPolicy(InsurancePolicy policy){
		view.setPolicyInfo(policy);
	}

	public void createReceipt(){
		if(view.isFormValid()) {
			view.getForm().commit();
			Receipt receipt = view.getForm().getValue();
			this.insurancePolicyBroker.createReceipt(receipt.policyId, receipt, new ResponseHandler<Receipt>() {
				
				@Override
				public void onResponse(Receipt response) {
					onReceiptCreated();
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
				}
			});
		}
	}
	
	public abstract void onReceiptCreated();

	public abstract void onCreationCancelled();

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}



}
