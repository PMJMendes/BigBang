package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceSubPolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.SubPolicy;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter;

public class SubPolicyClientInfoOrDocumentRequestViewPresenter extends
		InfoOrDocumentRequestViewPresenter<SubPolicy> {

	protected InsuranceSubPolicyBroker broker;
	
	public SubPolicyClientInfoOrDocumentRequestViewPresenter(
			bigBang.library.client.userInterface.presenter.InfoOrDocumentRequestViewPresenter.Display<SubPolicy> view) {
		super(view);
		broker = (InsuranceSubPolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_SUB_POLICY);
	}

	@Override
	protected InfoOrDocumentRequest getFormattedRequest(String ownerId,
			String ownerTypeId) {
		InfoOrDocumentRequest request = new InfoOrDocumentRequest();
		request.parentDataObjectId = ownerId;
		request.parentDataTypeId = BigBangConstants.EntityIds.INSURANCE_SUB_POLICY;
		return request;
	}

	@Override
	protected void showOwner(String ownerId, String ownerTypeId) {
		broker.getSubPolicy(ownerId, new ResponseHandler<SubPolicy>() {

			@Override
			public void onResponse(SubPolicy response) {
				view.getOwnerForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	@Override
	protected void checkOwnerPermissions(String ownerId, String ownerTypeId,
			ResponseHandler<Boolean> handler) {
		handler.onResponse(true);
	}

	@Override
	protected void onSend() {
		InfoOrDocumentRequest request = view.getForm().getInfo();
		broker.createInfoOrDocumentRequest(request, new ResponseHandler<InfoOrDocumentRequest>() {

			@Override
			public void onResponse(InfoOrDocumentRequest response) {
				onCreateRequestSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateRequestFailed();
			}
		});
	}

	@Override
	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownerTypeId");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onCreateRequestSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido de Informação foi criado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownerTypeId");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onCreateRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar o pedido de informação"), TYPE.ALERT_NOTIFICATION));
	}
	
	@Override
	protected void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível criar o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownerTypeId");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	@Override
	protected void onUserLacksPermission() {
		return;
	}

	@Override
	protected void onGenericError() {
		return;
	}

}
