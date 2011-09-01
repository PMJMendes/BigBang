package bigBang.library.client.userInterface.presenter;

import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.HistoryBroker;
import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.interfaces.HistoryServiceAsync;
import bigBang.library.shared.operation.HistoryOperation;

public class UndoOperationViewPresenter implements OperationViewPresenter {

	public interface Display {
		//LIST
		HasValueSelectables<HistoryItemStub> getUndoItemList();
		void setProcessId(String processId);
		void removeUndoItem(HistoryItemStub item);
		void addItem(HistoryItemStub item);
		
		//FORM
		HasValue<HistoryItem> getForm();
		
		//BUTTONS
		HasClickHandlers getUndoButton();
		
		//MISC
		void showConfirmUndo(ConfirmationCallback callback);
		
		Widget asWidget();
	}
	
	@SuppressWarnings("unused")
	private EventBus eventBus;
	private HistoryServiceAsync service;
	private Display view;
	protected HistoryBroker historyBroker;
	
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
		
		fetchHistoryItems();
		
		container.clear();
		container.add(this.view.asWidget());
	}
	
	protected void fetchHistoryItems(){
		this.historyBroker.requireDataRefresh();
		/*this.historyBroker.getItems(this.processId, new ResponseHandler<HistoryItemStub[]>() {
			
			@Override
			public void onResponse(HistoryItemStub[] response) {}
			
			@Override
			public void onError(Collection<ResponseError> errors) {}
		});*/
	}

	@Override
	public void bind() {
		if(bound)
			return;
		view.setProcessId(this.processId);
		view.getUndoItemList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getSelected().size() == 0)
					view.getForm().setValue(null);
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<HistoryItem> vs = (ValueSelectable<HistoryItem>) s;
					view.getForm().setValue(vs.getValue());
				}
			}
		});
		view.getUndoButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.showConfirmUndo(new ConfirmationCallback() {
					
					@Override
					public void onResult(boolean result) {
						if(result){
							undo(view.getForm().getValue());
						}
					}
				});
			}
		});
	}

	private void undo(final HistoryItemStub item) {
		service.undo(item.id, new BigBangAsyncCallback<HistoryItemStub>() {

			@Override
			public void onSuccess(HistoryItemStub result) {
				view.addItem(result);
				view.removeUndoItem(item);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		// TODO Auto-generated method stub

	}

}
