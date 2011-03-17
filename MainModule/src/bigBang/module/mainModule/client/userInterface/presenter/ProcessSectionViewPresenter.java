package bigBang.module.mainModule.client.userInterface.presenter;

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Operation;
import bigBang.library.shared.Service;
import bigBang.library.shared.event.OperationInvokedEvent;
import bigBang.library.shared.event.OperationInvokedEventHandler;
import bigBang.library.shared.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.userInterface.view.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ProcessSectionViewPresenter implements ViewPresenter {
	
	public interface Display {
		HasValue <Object> getOperationNavigationPanel();
		HasWidgets getOperationViewContainer();
		
		void createOperationNavigationItem(Operation o);
		Widget asWidget();
	}
	
	private Display view;
	private EventBus eventBus;
	
	public ProcessSectionViewPresenter(EventBus eventBus, Service service,
			Display view) {
		this.view = view;
		this.setEventBus(eventBus);
		bind();
	}

	public void setService(Service service) {
		
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	public void bind() {
		this.eventBus.addHandler(OperationInvokedEvent.TYPE, new OperationInvokedEventHandler() {
			public void onOperationInvoked(OperationInvokedEvent event) {
				//view.getOperationNavigationPanel().setValue((Object) event.getOperation());
			}
		});		
		
		/*this.view.getOperationNavigationPanel().addValueChangeHandler(new ValueChangeHandler<Object>() {
			/*
			public void onValueChange(ValueChangeEvent<Object> event) {
				GWT.log(((Operation)event.getValue()).getDescription());
				ViewPresenter presenter = ((Operation) event.getValue()).getPresenter();
				presenter.setEventBus(eventBus);
				presenter.go(view.getOperationViewContainer());
			}
		});*/
	}

	public void createOperationItem(Operation o) {
		this.view.createOperationNavigationItem(o);
	}

	public void setView(View view) {
		// TODO Auto-generated method stub
	}

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

}
