package bigBang.module.quoteRequestModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
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

public class QuoteRequestMassManagerTransferViewPresenter extends MassManagerTransferViewPresenter<QuoteRequestStub, QuoteRequest> {

	protected QuoteRequestBroker quoteRequestBroker;

	public QuoteRequestMassManagerTransferViewPresenter(Display<QuoteRequestStub, QuoteRequest> view){
		super(view);
		this.quoteRequestBroker = ((QuoteRequestBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.QUOTE_REQUEST));
	}

	@Override
	protected void onTransfer(String newManagerId,
			Collection<QuoteRequestStub> affectedProcesses) {

		String[] processIds = new String[affectedProcesses.size()];

		int i = 0;
		for(QuoteRequestStub c : affectedProcesses){
			processIds[i] = c.id;
			i++;
		}

		quoteRequestBroker.createManagerTransfer(processIds, newManagerId, new ResponseHandler<ManagerTransfer>() {

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
		//		this.view.setOperationFilter(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER);
		this.view.refreshMainList();
		this.view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<QuoteRequestStub> selectable = (ValueSelectable<QuoteRequestStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getSelectedList().clearSelection();
					quoteRequestBroker.getQuoteRequest(selectable.getValue().id, new ResponseHandler<QuoteRequest>() {

						@Override
						public void onResponse(QuoteRequest response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetQuoteRequestFailed();
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
				ValueSelectable<QuoteRequestStub> selectable = (ValueSelectable<QuoteRequestStub>) event.getFirstSelected();
				if(selectable != null) {
					view.getMainList().clearSelection();
					quoteRequestBroker.getQuoteRequest(selectable.getValue().id, new ResponseHandler<QuoteRequest>() {

						@Override
						public void onResponse(QuoteRequest response) {
							view.getSelectedProcessForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onGetQuoteRequestFailed();
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
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.QUOTE_REQUEST, BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER, new ResponseHandler<Boolean>() {

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

	protected void onGetQuoteRequestFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o QuoteRequeste seleccionado"), TYPE.ALERT_NOTIFICATION));
	}

}
