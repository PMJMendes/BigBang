package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.SubPolicy;
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

public class SubPolicyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		CANCEL_EDIT,
		SAVE
	}
	
	public static interface Display {
		HasEditableValue<SubPolicy> getForm();
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		Widget asWidget();
	}
	
	protected Display view;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected boolean bound = false;
	
	public SubPolicyViewPresenter(Display view) {
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
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
		String subPolicyId = parameterHolder.getParameter("subpolicyid");
		subPolicyId = subPolicyId == null ? new String() : subPolicyId;
		String parentPolicyId = parameterHolder.getParameter("id");
		parentPolicyId = parentPolicyId == null ? new String() : parentPolicyId;
		
		if(parentPolicyId .isEmpty()) {
			onGetParentFailed();
		} else if(subPolicyId.isEmpty()) {
			onGetSubPolicyFailed();
		} else if(subPolicyId.equalsIgnoreCase("new")) {
			showCreateSubPolicy(parentPolicyId);
		} else {
			showSubPolicy(parentPolicyId, subPolicyId);
		}
	}
	
	private void bind(){
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<SubPolicyViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case EDIT:
					view.getForm().setReadOnly(false);
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL_EDIT:
					break;
				}
			}
		});
		
		//TODO
		
		bound = true;
	}
	
	private void clearView(){
		view.getForm().setValue(null);
	}
	
	protected void showCreateSubPolicy(final String parentPolicyId){
		
		this.subPolicyBroker.openSubPolicyResource(null, new ResponseHandler<SubPolicy>() {
			
			@Override
			public void onResponse(SubPolicy subPolicy) {
				subPolicy.mainPolicyId = parentPolicyId;
				subPolicyBroker.getSubPolicyDefinition(subPolicy, new ResponseHandler<SubPolicy>() {

					@Override
					public void onResponse(SubPolicy initializedSubPolicy) {
						view.getForm().setValue(initializedSubPolicy);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onCreateSubPolicyFailed();						
					}
				});
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateSubPolicyFailed();
			}
		});
	}
	
	protected void onSave(){
		SubPolicy subPolicy = view.getForm().getInfo();

		if(subPolicyBroker.isTemp(subPolicy.id)){
			subPolicyBroker.updateSubPolicy(subPolicy, new ResponseHandler<SubPolicy>() {

				@Override
				public void onResponse(SubPolicy updatedSubPolicy) {
					subPolicyBroker.commitSubPolicy(updatedSubPolicy, new ResponseHandler<SubPolicy>() {

						@Override
						public void onResponse(SubPolicy response) {
							onSaveSubPolicySuccess();
							NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
							item.setParameter("subpolicyid", response.id);
							NavigationHistoryManager.getInstance().go(item);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onSaveSubPolicyError();
						}
					});
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onSaveSubPolicyError();
				}
			});
		}else{
			makeTemp(subPolicy, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					onSave();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetSubPolicyFailed();
				}
			});
		}
	}
	
	protected void makeTemp(SubPolicy subPolicy, final ResponseHandler<Void> handler){
		subPolicyBroker.openSubPolicyResource(subPolicy.id, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				handler.onResponse(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onError(errors);
			}
		});
	}
	
	protected void showSubPolicy(String parentPolicyId, String subPolicyId){
		this.subPolicyBroker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetSubPolicyFailed();
			}
		});
	}

	private void onGetParentFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice Principal"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subpolicyid");
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onGetSubPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subpolicyid");
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onCreateSubPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("subpolicyid");
		item.removeParameter("operation");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	private void onSaveSubPolicySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Apólice Adesão foi guardada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	private void onSaveSubPolicyError(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível guardar a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
	}
	
}
