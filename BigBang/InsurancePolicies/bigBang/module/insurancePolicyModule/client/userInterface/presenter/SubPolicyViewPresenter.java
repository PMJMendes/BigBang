package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

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

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SubPolicyViewPresenter implements ViewPresenter {

	public static enum Action {
		EDIT,
		CANCEL_EDIT,
		SAVE,
		INCLUDE_INSURED_OBJECT,
		CREATE_INSURED_OBJECT,
		INCLUDE_INSURED_OBJECT_FROM_CLIENT,
		CREATE_INSURED_OBJECT_FROM_CLIENT,
		EXCLUDE_INSURED_OBJECT,
		PERFORM_CALCULATIONS,
		VALIDATE,
		TRANSFER_TO_POLICY,
		CREATE_INFO_OR_DOCUMENT_REQUEST,
		CREATE_RECEIPT,
		VOID,
		DELETE
	}

	public static interface Display {
		HasEditableValue<SubPolicy> getForm();
		HasValue<InsurancePolicy> getParentPolicyForm();

		void setSaveModeEnabled(boolean enabled);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		//PERMISSIONS
		void allowEdit(boolean allow);
		void allowIncludeInsuredObject(boolean allow);
		void allowCreateInsuredObject(boolean allow);
		void allowIncludeInsuredObjectFromClient(boolean allow);
		void allowCreateInsuredObjectFromClient(boolean allow);
		void allowExcludeInsuredObject(boolean allow);
		void allowPerformCalculations(boolean allow);
		void allowValidate(boolean allow);
		void allowTransferToPolicy(boolean allow);
		void allowCreateInfoOrDocumentRequest(boolean allow);
		void allowCreateReceipt(boolean allow);
		void allowVoid(boolean allow);
		void allowDelete(boolean allow);
		
		void confirmDelete(ResponseHandler<Boolean> handler);

		Widget asWidget();
	}

	protected Display view;
	protected InsuranceSubPolicyBroker subPolicyBroker;
	protected InsurancePolicyBroker policyBroker;
	protected boolean bound = false;

	public SubPolicyViewPresenter(Display view) {
		this.subPolicyBroker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
		this.policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
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
					onEdit();
					break;
				case SAVE:
					onSave();
					break;
				case CANCEL_EDIT:
					onCancel();
					break;
				case DELETE:
					onDelete();
					break;
				case VOID:
					//TODO
					break;
				case CREATE_INSURED_OBJECT:
					//TODO
					break;
				case CREATE_INSURED_OBJECT_FROM_CLIENT:
					//TODO
					break;
				case INCLUDE_INSURED_OBJECT:
					//TODO
					break;
				case INCLUDE_INSURED_OBJECT_FROM_CLIENT:
					//TODO
					break;
				case EXCLUDE_INSURED_OBJECT:
					//TODO
					break;
				case VALIDATE:
					//TODO
					break;
				case PERFORM_CALCULATIONS:
					//TODO
					break;
				case CREATE_RECEIPT:
					//TODO
					break;
				case TRANSFER_TO_POLICY:
					//TODO
					break;
				case CREATE_INFO_OR_DOCUMENT_REQUEST:
					//TODO
					break;
				}
			}
		});

		bound = true;
	}

	private void clearView(){
		view.getForm().setValue(null);
	}

	protected void showCreateSubPolicy(final String parentPolicyId){
		this.showParentPolicy(parentPolicyId);
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

	protected void showSubPolicy(String parentPolicyId, String subPolicyId){
		this.showParentPolicy(parentPolicyId);
		this.subPolicyBroker.getSubPolicy(subPolicyId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				view.getForm().setReadOnly(true);
				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetSubPolicyFailed();
			}
		});
	}

	protected void onEdit(){
		final SubPolicy subPolicy = view.getForm().getValue();
		if(subPolicy != null) {
			if(!subPolicyBroker.isTemp(subPolicy.id)){
				makeTemp(subPolicy, new ResponseHandler<Void>() {

					@Override
					public void onResponse(Void response) {
						subPolicyBroker.getSubPolicy(subPolicy.id, new ResponseHandler<SubPolicy>() {

							@Override
							public void onResponse(SubPolicy response) {
								//TODO permissions
								view.getForm().setReadOnly(false);
								view.setSaveModeEnabled(true);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {
								onGetSubPolicyFailed();
							}
						});
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetSubPolicyFailed();
					}
				});
			}else{
				view.allowEdit(true);
				view.getForm().setReadOnly(false);
				view.setSaveModeEnabled(true);
			}
		};
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

	protected void onCancel(){
		SubPolicy subPolicy = view.getForm().getValue();

		if(subPolicy != null && subPolicyBroker.isTemp(subPolicy.id)){
			subPolicyBroker.closeSubPolicyResource(subPolicy.id, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					return;
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
		NavigationHistoryManager.getInstance().reload();
	}

	protected void onDelete(){
		view.confirmDelete(new ResponseHandler<Boolean>(){

			@Override
			public void onResponse(Boolean response) {
				if(response) {
					SubPolicy subPolicy = view.getForm().getValue();
					if(subPolicy != null) {
//						subPolicyBroker.removeSubPolicy(subPolicy.id, new ResponseHandler<String>() {
//
//							@Override
//							public void onResponse(String response) {
//								onDeleteSubPolicySuccess();
//							}
//
//							@Override
//							public void onError(Collection<ResponseError> errors) {
//								onDeleteSubPolicyFailed();
//							}
//						});
					}
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(false);
			}
		});
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

	protected void showParentPolicy(String parentPolicyId){
		if(parentPolicyId == null){
			onGetParentFailed();
		}else{
			this.policyBroker.getPolicy(parentPolicyId, new ResponseHandler<InsurancePolicy>() {

				@Override
				public void onResponse(InsurancePolicy response) {
					view.getParentPolicyForm().setValue(response);
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onGetParentFailed();
				}
			});
		}
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

	private void onDeleteSubPolicyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar a Apólice Adesão"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onDeleteSubPolicySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Apólice Adesão foi eliminada com sucesso"), TYPE.TRAY_NOTIFICATION));
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
