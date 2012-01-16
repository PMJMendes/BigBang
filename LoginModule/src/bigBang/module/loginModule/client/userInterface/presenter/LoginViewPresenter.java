package bigBang.module.loginModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Session;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.LogoutEventHandler;
import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.client.event.SessionExpiredEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewPresenter implements ViewPresenter {

	public interface Display {
		HasClickHandlers getSubmitButton();

		void setDomains(String[] domains);
		HasValue<String> getDomain();
		HasValue<String> getUsername();
		HasValue<String> getPassword();

		void showErrorMessage(String message);
		void hideErrorMessage();

		public void showLoading(boolean show);

		Widget asWidget();
	}

	private Display view;
	private AuthenticationServiceAsync service;

	public LoginViewPresenter(Display view){
		service = AuthenticationService.Util.getInstance();
		this.setView((UIObject) view);
	} 

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	public void go(final HasWidgets container) {
		checkActiveSession(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(!response){
					checkIntegratedLogin(new ResponseHandler<Boolean>() {

						@Override
						public void onResponse(Boolean response) {
							if(!response){
								bind();
								view.setDomains(BigBangConstants.Session.DOMAINS);
								String domain = Window.Location.getParameter("domain");

								if(domain != null && isKnownDomain(domain)){
									view.getDomain().setValue(domain);
								}else{
									view.getDomain().setValue(BigBangConstants.Session.DOMAINS[BigBangConstants.Session.DEFAULT_DOMAIN_INDEX]);
								}
								container.clear();
								container.add(LoginViewPresenter.this.view.asWidget());
							}
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							onResponse(false);
						}
					});
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(false);
			}
		});
	}
	
	private void checkActiveSession(final ResponseHandler<Boolean> handler){
		service.getCurrentLoginData(new BigBangAsyncCallback<LoginResponse>() {

			@Override
			public void onSuccess(LoginResponse loginResponse) {
				if(loginResponse != null){
					//LOGIN SUCCESS
					Session.setUserId(loginResponse.userId);
					Session.setUsername(loginResponse.userName);
					Session.setDisplayName("TODO");
					Session.setDomain(loginResponse.domain);
					
					onLoginSuccess();
					handler.onResponse(true);
				}else{
					handler.onResponse(false);
				}
			}
		});
	}
	
	private void checkIntegratedLogin(final ResponseHandler<Boolean> handler){
		final String domain = Window.Location.getParameter("domain");
		if(domain == null || !isKnownDomain(domain)){
			handler.onResponse(false);
			return;
		}

		service.login(domain, new AsyncCallback<LoginResponse>() {

			public void onSuccess(LoginResponse loginResponse) {
				if(loginResponse == null){
					//LOGIN FAILED
					view.getDomain().setValue(domain);
					LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar automaticamente.");
					handler.onResponse(false);
				} else {
					//LOGIN SUCCESS
					Session.setUserId(loginResponse.userId);
					Session.setUsername(loginResponse.userName);
					Session.setDisplayName("TODO");
					Session.setDomain(domain);
					
					onLoginSuccess();
				}
			}

			public void onFailure(Throwable caught) {
				onLoginFailed();
			}
		});
	}
	
	private void checkLogin(String username, String password, final String domain){
		view.showLoading(true);
		service.login(username, password, domain, new AsyncCallback<LoginResponse>() {

			public void onSuccess(LoginResponse loginResponse) {
				//LOGIN SUCCESS
				Session.setUserId(loginResponse.userId);
				Session.setUsername(loginResponse.userName);
				Session.setDisplayName("TODO");
				Session.setDomain(domain);

				onLoginSuccess();
			}

			public void onFailure(Throwable caught) {
				onLoginFailed();
			}
		});
		view.getPassword().setValue("");
	}

	private boolean isKnownDomain(String domain){
		String[] domains = BigBangConstants.Session.DOMAINS;
		if(domain != null) {
			for(int i = 0; i < domains.length; i++){
				if(domain.equalsIgnoreCase(domains[i])){
					return true;
				}
			}
		}
		return false;
	}

	public void bind() {
//		this.checkLogin("root", "Premium.", "CrediteEGS", new ResponseHandler<Boolean>() { //TODO IMPORTANT FJVC
//
//			@Override
//			public void onResponse(Boolean response) {
//				GWT.log("login success");
//			}
//
//			@Override
//			public void onError(Collection<ResponseError> errors) {
//				GWT.log("login failure");
//			}
//		});

		view.getSubmitButton().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				checkLogin(view.getUsername().getValue(), view.getPassword().getValue(), view.getDomain().getValue());
			}
		});
		
		//APPLICATION-WIDE
		EventBus.getInstance().addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

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
		EventBus.getInstance().addHandler(SessionExpiredEvent.TYPE, new SessionExpiredEventHandler() {

			@Override
			public void onSessionExpired() {
				LoginViewPresenter.this.onSessionExpired();
			}
		});
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}
	
	private void onLoginSuccess(){
		this.view.showLoading(false);
		GWT.log("Authentication success for " + Session.getUsername());
		EventBus.getInstance().fireEvent(new LoginSuccessEvent());
	}
	
	private void onLoginFailed(){
		GWT.log("Authentication service failure");
		LoginViewPresenter.this.view.showLoading(false);
		LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar. Verifique se as credenciais de acesso inseridas são as correctas");
	}
	
	private void onSessionExpired(){
		GWT.log("The session expired");
		Window.Location.reload();
	}

}
