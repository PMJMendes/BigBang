package bigBang.module.clientModule.client.userInterface.presenter;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Operation;
import bigBang.library.shared.Service;
import bigBang.library.shared.event.OperationInvokedEvent;
import bigBang.library.shared.event.OperationInvokedEventHandler;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ClientMergeOperationViewPresenter implements OperationViewPresenter {
	
	public interface Display {
		Widget asWidget();

		View getInstance();
	}
	
	private EventBus eventBus;
	private Display view;
	private Operation operation;

	public ClientMergeOperationViewPresenter(EventBus eventBus, Service service, View view) {
		this.setEventBus(eventBus);
		this.setView(view);
	}

	public void setService(Service service) {
		//TODO
	}
	
	public void setView(View view) {
		this.view = (Display) view;
	}

	public void setEventBus(final EventBus eventBus) {
		if(eventBus == null)
			return;

		this.eventBus = eventBus;
		
		registerEventHandlers(eventBus);
	}

	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	public void goCompact(HasWidgets container) {
		go(container);
	}

	public void bind() {
		// TODO Auto-generated method stub

	}

	public void registerEventHandlers(final EventBus eventBus) {
		eventBus.addHandler(OperationInvokedEvent.TYPE,	new OperationInvokedEventHandler() {

			public void onOperationInvoked(OperationInvokedEvent event) {
				if(getOperation() == null || !event.getOperationId().equals(getOperation().getId()))
					return;
				if(event.goToScreen())
					GWT.log("GOTO SCREEN");
				else {
					GWT.log("DO NOT GOTO SCREEN");
					View tempView  = view.getInstance();
					ClientMergeOperationViewPresenter presenter = new ClientMergeOperationViewPresenter(eventBus, null, tempView);
					event.getViewPresenterManager().managePresenter(getOperation().getId(), presenter);
					//presenter.setTarget(event.getTargetId());
				}
			}
		});
	}

	public void setOperation(Operation o) {
		this.operation = o;
	}

	public Operation getOperation() {
		return operation;
	}

	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
