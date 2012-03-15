package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.interfaces.ExchangeServiceAsync;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ExchangeItemStub;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExchangeItemSelectionViewPresenter implements ViewPresenter,
		HasValue<IncomingMessage> {
	
	HandlerManager manager;
	protected Display view;
	private boolean bound = false;
	ExchangeServiceAsync service;
	
	public static interface Display{

		Widget asWidget();

		void addEmailEntry(ExchangeItemStub email);
		
	}
	
	public ExchangeItemSelectionViewPresenter(Display view){
		setView((UIObject)view);
		manager = new HandlerManager(this);
		service = ExchangeService.Util.getInstance();
	}
	@Override
	public void setView(UIObject view) {
		
		this.view = (Display)view;
	}
	
	public void bind(){
		if(bound ){
			return;
		}
		
		//TODO
		
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
	
		service.getItems(new BigBangAsyncCallback<ExchangeItemStub[]>() {

			@Override
			public void onResponseSuccess(ExchangeItemStub[] result) {
				
				for(int i=0; i<result.length; i++){
					view.addEmailEntry(result[i]);
				}
				
			}
			
			public void onResponseFailure(Throwable caught) {
				super.onResponseFailure(caught);
			};
		
		
		
		});
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<IncomingMessage> handler) {
		return manager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);		
	}

	@Override
	public IncomingMessage getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(IncomingMessage value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(IncomingMessage value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

}
