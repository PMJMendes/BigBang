package bigBang.module.insurancePolicyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicyStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class InsurancePolicyMassManagerTransferViewPresenter extends MassManagerTransferViewPresenter<InsurancePolicyStub, InsurancePolicy> implements
ViewPresenter {

	protected InsurancePolicyBroker policyBroker;

	public InsurancePolicyMassManagerTransferViewPresenter(
			bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter.Display<InsurancePolicyStub, InsurancePolicy> view) {
		super(view);
		policyBroker = (InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY);
	}

	@Override
	protected void onTransfer(String newManagerId,
			Collection<InsurancePolicyStub> affectedProcesses) {
		String[] processIds = new String[affectedProcesses.size()];

		if(processIds.length == 0){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Nenhuma apólice seleccionada"), TYPE.ALERT_NOTIFICATION));				
			return;
		}
		
		int i = 0;
		for(InsurancePolicyStub p : affectedProcesses){
			processIds[i] = p.id;
			i++;
		}

		policyBroker.createManagerTransfer(processIds, newManagerId, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onTransferSuccess();
				view.refreshMainList();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onTransferFailed();
			}
		});
	}

	protected void bind() {
		super.bind();
		this.view.setOperationFilter(BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER);
		this.view.refreshMainList();
		this.view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selectable = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getSelectedList().clearSelection();
					policyBroker.getPolicy(selectable.getValue().id, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetPolicyFailed();
						}
					});
				}else if(!view.getSelectedList().getSelected().isEmpty()){
					view.getSelectedProcessForm().setValue(null);
				}
			}
		});
		this.view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsurancePolicyStub> selectable = (ValueSelectable<InsurancePolicyStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getMainList().clearSelection();
					policyBroker.getPolicy(selectable.getValue().id, new ResponseHandler<InsurancePolicy>() {

						@Override
						public void onResponse(InsurancePolicy response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetPolicyFailed();
						}
					});
				}else if(!view.getMainList().getSelected().isEmpty()){
					view.getSelectedProcessForm().setValue(null);
				}
			}
		});
	}

	@Override
	protected void onCancel() {
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void checkUserPermission(final ResponseHandler<Boolean> handler) {
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.INSURANCE_POLICY, BigBangConstants.OperationIds.InsurancePolicyProcess.TRANSFER_MANAGER, new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				handler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				handler.onResponse(false);
			}
		});
	}

	@Override
	protected void onUserLacksPermission() {
//		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
	}

	@Override
	protected void onTransferSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Transferência de Gestor for criada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	@Override
	protected void onTransferFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onGetPolicyFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Apólice seleccionada"), TYPE.ALERT_NOTIFICATION));
	}

}
