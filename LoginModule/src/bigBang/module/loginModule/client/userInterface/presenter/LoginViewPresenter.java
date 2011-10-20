package bigBang.module.loginModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.LogoutEventHandler;
import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.client.event.SessionExpiredEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewPresenter implements ViewPresenter {

	public class LoginData {
		public String username;
		public String password;
		public String domain;
	}
	
	public interface Display {
		HasClickHandlers getSubmitButton();
		HasValue<String> getUsername();
		String getDomain();
		HasValue<String> getPassword();
		void setDomains(String[] domains);
		
		void showErrorMessage(String message);
		void hideErrorMessage();
		void showCredentialsPrompt(ResponseHandler<LoginData> responseHandler);
		void hideCredentialsPrompt();
		void showCredentialsPromptLoginError(boolean show);
		
		Widget asWidget();
		void setSelectedDomain(String domain);
		public void showLoading(boolean show);
	}

	private Display view;
	private EventBus eventBus;
	private HasWidgets tmpContainer;
	
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
		registerEventHandlers(eventBus);
	}
	
	public void setView(View view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		tmpContainer = container;
		checkIntegrated();
//		bind();
//		container.clear();
//		container.add(this.view.asWidget());
	}
	
	private void finishGo(){
		bind();
		view.setDomains(new String[]{"CrediteEGS", "AMartins"});
		final String domain = Window.Location.getParameter("domain");
		if(domain != null)
			view.setSelectedDomain(domain);

		tmpContainer.clear();
		tmpContainer.add(view.asWidget());
		tmpContainer = null;
	}

	public void bind() {
		this.checkLogin("root", "Premium.", "CrediteEGS", new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				GWT.log("login success");
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				GWT.log("login failure");
			}
		});
//		view.getSubmitButton().addClickHandler(new ClickHandler() {//TODO IMPORTANT FJVC
//			
//			public void onClick(ClickEvent event) {
//				checkLogin(view.getUsername().getValue(), view.getPassword().getValue(), view.getDomain(), null);
//			}
//		});
	}
	
	private void checkIntegrated(){
		final String domain = Window.Location.getParameter("domain");
		if(domain == null){
			finishGo();
			return;
		}
					
		service.login(domain, new AsyncCallback<String>() {
			
			public void onSuccess(String username) {
				if(username == null){
					view.setSelectedDomain(domain);
					finishGo();
					LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar automaticamente.");
				} else {		
					eventBus.fireEvent(new LoginSuccessEvent(username, domain));
					GWT.log("Authentication success for " + username);
				}
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("Authentication service failure : " + caught.getMessage());
			}
		});
	}
	
	private void checkLogin(String username, String password, String domain, final ResponseHandler<Boolean> callback){
		view.showLoading(true);
		service.login(username, password, domain, new AsyncCallback<String>() {
			
			public void onSuccess(String username) {
				eventBus.fireEvent(new LoginSuccessEvent(username, view.getDomain()));
				GWT.log("Authentication success for " + username);
				LoginViewPresenter.this.view.showLoading(false);
				if(callback != null)
					callback.onResponse(true);
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("Authentication service failure : " + caught.getMessage());
				LoginViewPresenter.this.view.showLoading(false);
				LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar. Verifique se as credenciais de acesso inseridas são as correctas");
				callback.onError(new String[]{"Login error"});
			}
		});
		view.getPassword().setValue("");
	}

	public void registerEventHandlers(EventBus eventBus) {
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
			
			@Override
			public void onLogout(LogoutEvent event) {
				service.logout(new BigBangAsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						GWT.log("logout");
						Window.Location.reload();
					}
				});
			}
		});
		eventBus.addHandler(SessionExpiredEvent.TYPE, new SessionExpiredEventHandler() {
			
			@Override
			public void onSessionExpired() {
				GWT.log("A sess�o expirou");
				showRenewLoginPrompt();
			}
		});
	}
	
	protected void showRenewLoginPrompt(){
		view.showCredentialsPrompt(new ResponseHandler<LoginData>() {

			@Override
			public void onResponse(LoginData response) {
				checkLogin(response.username, response.password, Window.Location.getParameter("domain"), new ResponseHandler<Boolean>() {

					@Override
					public void onResponse(Boolean response) {
						view.hideCredentialsPrompt();
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						view.showCredentialsPromptLoginError(true);
					}
				});
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				Window.Location.reload();
			}
		});
	}

}
