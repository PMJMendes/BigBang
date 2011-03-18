package bigBang.module.loginModule.client.userInterface.presenter;

import bigBang.library.client.EventBus;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewPresenter implements ViewPresenter {

	public interface Display {
		HasClickHandlers getSubmitButton();
		HasValue<String> getUsername();
		HasValue<String> getPassword();
		Widget asWidget();
	}

	private Display view;
	private EventBus eventBus;
	
	private AuthenticationServiceAsync service;

	public LoginViewPresenter(EventBus eventBus, Display view){
		this.setView(view);
		this.setEventBus(eventBus);
	} 

	public void setView(Display view) {
		this.view = view;
	}

	public void setService(Service service) {
		this.service = (AuthenticationServiceAsync) service;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	public void bind() {
		view.getSubmitButton().addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				checkLogin();
			}
		});
	}
	
	private void checkLogin(){		
		service.login(view.getUsername().getValue(), view.getPassword().getValue(), new AsyncCallback<String>() {
			
			public void onSuccess(String username) {
				eventBus.fireEvent(new LoginSuccessEvent(username));
				GWT.log("Authentication success for " + username);
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("Authentication service failure : " + caught.getMessage());
			}
		});
		
	}

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub
		
	}

}
