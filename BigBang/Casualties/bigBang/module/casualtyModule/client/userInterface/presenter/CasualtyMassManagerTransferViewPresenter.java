package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.ManagerTransfer;
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

public class CasualtyMassManagerTransferViewPresenter extends
MassManagerTransferViewPresenter<CasualtyStub, Casualty> implements ViewPresenter {

	protected CasualtyDataBroker casualtyBroker;

	public CasualtyMassManagerTransferViewPresenter(
			bigBang.library.client.userInterface.presenter.MassManagerTransferViewPresenter.Display<CasualtyStub, Casualty> view) {
		super(view);
		casualtyBroker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
	}

	@Override
	protected void onTransfer(String newManagerId,
			Collection<CasualtyStub> affectedProcesses) {

		String[] processIds = new String[affectedProcesses.size()];

		if(processIds.length == 0){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Nenhum sinistro seleccionado"), TYPE.ALERT_NOTIFICATION));				
			return;
		}
		
		int i = 0;
		for(CasualtyStub c : affectedProcesses){
			processIds[i] = c.id;
			i++;
		}

		casualtyBroker.createManagerTransfer(processIds, newManagerId, new ResponseHandler<ManagerTransfer>() {

			@Override
			public void onResponse(ManagerTransfer response) {
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
		this.view.setOperationFilter(BigBangConstants.OperationIds.CasualtyProcess.CREATE_MANAGER_TRANSFER);
		this.view.refreshMainList();
		this.view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<CasualtyStub> selectable = (ValueSelectable<CasualtyStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getSelectedList().clearSelection();
					casualtyBroker.getCasualty(selectable.getValue().id, new ResponseHandler<Casualty>() {

						@Override
						public void onResponse(Casualty response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetCasualtyFailed();
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
				ValueSelectable<CasualtyStub> selectable = (ValueSelectable<CasualtyStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getMainList().clearSelection();
					casualtyBroker.getCasualty(selectable.getValue().id, new ResponseHandler<Casualty>() {

						@Override
						public void onResponse(Casualty response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetCasualtyFailed();
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
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.CASUALTY, BigBangConstants.OperationIds.CasualtyProcess.CREATE_MANAGER_TRANSFER, new ResponseHandler<Boolean>() {

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

	protected void onGetCasualtyFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Sinistro seleccionado"), TYPE.ALERT_NOTIFICATION));
	}
}
