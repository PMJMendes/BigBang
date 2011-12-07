package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.HistoryServiceAsync;
import bigBang.library.interfaces.Service;
import bigBang.library.shared.operation.HistoryOperation;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UndoOperationViewPresenter implements OperationViewPresenter {

	public static enum Action {
		REVERT_OPERATION
	}

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setProcessId(String processId);
		void refreshList();

		//FORM
		HasValue<HistoryItem> getForm();

		//MISC
		void setUndoable(boolean undoable);
		void selectItem(String id);
		
		void showConfirmUndo(ConfirmationCallback callback);
		void showErrors(Collection<ResponseError> errors);
		HandlerRegistration addAttachHandler(AttachEvent.Handler handler);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void clear();
		Widget asWidget();
	}

	@SuppressWarnings("unused")
	private EventBus eventBus;
	private HistoryServiceAsync service;
	private Display view;
	protected HistoryBroker historyBroker;
	protected String[] implementedOperationIds;
	private String processId;

	private HistoryOperation operation;

	private boolean bound = false;

	public UndoOperationViewPresenter(EventBus eventBus, HistoryBroker broker, Display view, String processId) {
		this.processId = processId;
		setEventBus(eventBus);
		setService(service);
		setView((View) view);
		this.historyBroker = broker;
	}

	@Override
	public void setService(Service service) {}

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
		if(!bound)
			bind();
		bound = true;

		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		if(bound)
			return;
		view.setProcessId(this.processId);
		view.setUndoable(false);
		view.registerActionHandler(new ActionInvokedEventHandler<UndoOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case REVERT_OPERATION:
					onRevertOperation();
					break;
				}
			}

			public void onRevertOperation() {
				view.showConfirmUndo(new ConfirmationCallback() {

					@Override
					public void onResult(boolean result) {
						if(result){
							historyBroker.undo(view.getForm().getValue().id, new ResponseHandler<HistoryItem>() {

								@Override
								public void onResponse(HistoryItem response) {
									view.clear();
								}

								@Override
								public void onError(Collection<ResponseError> errors) {
									view.showErrors(errors);
								}
							});
						}
					}
				});
			}
		});
		view.getUndoItemList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getSelected().size() == 0){
					view.getForm().setValue(null);
					view.setUndoable(false);
					return;
				}
				Selectable s = event.getFirstSelected();
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItem> vs = (ValueSelectable<HistoryItem>) s;
				HistoryItemStub item = vs.getValue();
				historyBroker.getItem(item.id, processId, new ResponseHandler<HistoryItem>() {

					@Override
					public void onResponse(HistoryItem response) {
						view.getForm().setValue(response);
						view.setUndoable(response.canUndo);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		});
		view.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					view.refreshList();
				}
			}
		});
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (HistoryOperation) o;
	}

	@Override
	public Operation getOperation() {
		return operation;
	}

	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setTargetEntity(String id) {
		this.view.selectItem(id);
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		// TODO Auto-generated method stub

	}

}
