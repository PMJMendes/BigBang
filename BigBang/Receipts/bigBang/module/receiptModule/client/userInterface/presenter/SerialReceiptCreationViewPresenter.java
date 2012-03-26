package bigBang.module.receiptModule.client.userInterface.presenter;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.ReceiptProcessDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.receiptModule.shared.ReceiptPolicyWrapper;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SerialReceiptCreationViewPresenter implements ViewPresenter{

	public enum Action{
		VERIFY_RECEIPT,
		VERIFY_POLICY,
		MARK_RECEIPT,
		SAVE,
		CANCEL, NO_IMAGE, IMAGE
	}

	private Display view;
	private boolean bound = false;
	private InsurancePolicyBroker policyBroker;
	private ReceiptProcessDataBroker receiptBroker;

	public interface Display{

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		HasValue<ReceiptPolicyWrapper> getForm();

		void enableReceiptNumber(boolean enable);

		void clear();

		void lockReceiptView(boolean b);

	}

	public SerialReceiptCreationViewPresenter(Display view){
		receiptBroker = (ReceiptProcessDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT);
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case MARK_RECEIPT:
					onMarkReceipt();
					break;
				case SAVE:
					onSave();
					break;
				case VERIFY_POLICY:
					onVerifyPolicy();
					break;
				case VERIFY_RECEIPT:
					onVerifyReceipt();
					break;
				case IMAGE:
					onHasImage();
					break;
				case NO_IMAGE:
					onNoImage();
					break;
				}

			}

		});

		bound = true;
	}

	protected void onNoImage() {

		view.enableReceiptNumber(true);
		view.lockReceiptView(true);
		view.clear();
	}

	protected void onHasImage() {
		view.clear();
		view.enableReceiptNumber(false);
		view.lockReceiptView(false);
	}

	protected void onVerifyReceipt() {
		// TODO Auto-generated method stub
		
	}

	protected void onVerifyPolicy() {
		// TODO Auto-generated method stub
		
	}

	protected void onSave() {
		// TODO Auto-generated method stub
		
	}

	protected void onMarkReceipt() {
		// TODO Auto-generated method stub
		
	}

	protected void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		//TODO
	}
}
