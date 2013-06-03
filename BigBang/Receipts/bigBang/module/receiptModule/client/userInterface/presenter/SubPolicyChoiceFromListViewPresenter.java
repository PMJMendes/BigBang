package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.SubPolicy;
import bigBang.definitions.shared.SubPolicyStub;
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
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.SubPoliciesList.Entry;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class SubPolicyChoiceFromListViewPresenter implements ViewPresenter {

	private Display view;
	private InsuranceSubPolicyBroker broker;
	private boolean bound = false;
	
	public static enum Action{
		CHOSEN_SUB_POLICY,
		CANCEL, LIST_CHANGED, MARK_RECEIPT
	}
	
	public SubPolicyChoiceFromListViewPresenter(Display view) {
		
		this.broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		setView((UIObject)view);
		
	}
	
	public interface Display{

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void clear();

		HasValueSelectables<SubPolicyStub> getList();

		void fillList(Collection<SubPolicyStub> collection);

		void enableConfirm(boolean b);

		Widget asWidget();

		HasEditableValue<SubPolicy> getForm();

		void enableMarkReceipt(boolean b);

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

	private void bind() {
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case MARK_RECEIPT:
					onMark();
					break;
				case CANCEL:
					onCancel();
					break;
				case CHOSEN_SUB_POLICY:
					getSelectedSubPolicy();
					break;
				case LIST_CHANGED:
					listChanged();
					break;
				}
			}
		});
	}

	protected void listChanged() {
		view.enableConfirm(true);
		Collection<ValueSelectable<SubPolicyStub>> selected = view.getList().getSelected();
		
		if(selected.size() > 0){

			broker.getSubPolicy(((SubPolicyStub)((Entry)selected.toArray()[0]).getValue()).id,  new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy response) {
					view.getForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível mostrar o recibo."), TYPE.ALERT_NOTIFICATION));
				}
			});
		}
		
	}

	protected abstract void onCancel();
	protected abstract void onMark();

	@Override
	public void setParameters(HasParameters parameterHolder) {
		view.clear();
		view.enableConfirm(false);
	}
	
	public abstract void setSubPolicies(Collection<SubPolicyStub> stubs);
	public abstract void getSelectedSubPolicy();
	public void fillList(Collection<SubPolicyStub> collection){
	
		view.fillList(collection);
		
	}

	public void enableMarkReceipt(boolean b) {
			view.enableMarkReceipt(b);
	}
}
