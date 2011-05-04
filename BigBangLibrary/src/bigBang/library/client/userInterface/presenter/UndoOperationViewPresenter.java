package bigBang.library.client.userInterface.presenter;

import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.view.UndoOperationView;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.library.interfaces.UndoServiceAsync;
import bigBang.library.shared.ProcessUndoItem;
import bigBang.library.shared.operation.UndoOperation;

public class UndoOperationViewPresenter implements OperationViewPresenter {

	public interface Display {
		//LIST
		HasValueSelectables<ProcessUndoItem> getUndoItemList();
		void setUndoItems(ProcessUndoItem[] items);
		void removeUndoItem(ProcessUndoItem item);
		void addItem(ProcessUndoItem item);
		
		//FORM
		HasValue<ProcessUndoItem> getForm();
		
		//BUTTONS
		HasClickHandlers getUndoButton();
		
		//MISC
		void showConfirmUndo(ConfirmationCallback callback);
		
		Widget asWidget();
	}
	
	private EventBus eventBus;
	private UndoServiceAsync service;
	private Display view;
	
	private String processId;
	
	private UndoOperation operation;
	
	private boolean bound = false;
	
	public UndoOperationViewPresenter(EventBus eventBus, Service service, Display view, String processId) {
		this.processId = processId;
		setEventBus(eventBus);
		setService(service);
		setView((View) view);
	}

	@Override
	public void setService(Service service) {
		this.service = (UndoServiceAsync) service;
	}

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
		
		setup();
		
		container.clear();
		container.add(this.view.asWidget());
	}
	
	private void setup(){
		service.getProcessUndoItems(processId, new BigBangAsyncCallback<ProcessUndoItem[]>() {

			@Override
			public void onSuccess(ProcessUndoItem[] result) {
				view.setUndoItems(result);
			}
		});
	}

	@Override
	public void bind() {
		view.getUndoItemList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(event.getSelected().size() == 0)
					view.getForm().setValue(null);
				for(Selectable s : event.getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<ProcessUndoItem> vs = (ValueSelectable<ProcessUndoItem>) s;
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

	private void undo(final ProcessUndoItem item) {
		service.undo(item.id, new BigBangAsyncCallback<ProcessUndoItem>() {

			@Override
			public void onSuccess(ProcessUndoItem result) {
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
		this.operation = (UndoOperation) o;
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
