package bigBang.module.loginModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Session;
import bigBang.library.client.event.LoginSuccessEvent;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.interfaces.AuthenticationService;
import bigBang.module.loginModule.interfaces.AuthenticationServiceAsync;
import bigBang.module.loginModule.shared.LoginDomain;
import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
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
	private boolean bound = false;

	private LoginDomain[] domains = new LoginDomain[0];

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
								view.setDomains(new String[0]);
								container.clear();
								container.add(LoginViewPresenter.this.view.asWidget());

								getDomains(new ResponseHandler<String[]>() {
									@Override
									public void onResponse(String[] response) {
										view.setDomains(response);
										String domain = Window.Location.getParameter("domain");

										if(domain != null && isKnownDomain(domain)){
											view.getDomain().setValue(domain);
										}else{
											view.getDomain().setValue(response[0]);
										}
									}

									@Override
									public void onError(Collection<ResponseError> errors) {
										view.showErrorMessage("Não foi possível obter a lista de domínios do Big Bang.");
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
			public void onResponseSuccess(LoginResponse loginResponse) {
				if(loginResponse != null){
					onLoginSuccess(loginResponse);
					handler.onResponse(true);
				}else{
					handler.onResponse(false);
				}
			}
		});
	}
	
	private void checkIntegratedLogin(final ResponseHandler<Boolean> handler) {
		final String domain = Window.Location.getParameter("domain");
		if(domain == null || !isKnownDomain(domain)){
			handler.onResponse(false);
			return;
		}

		service.login(domain, new BigBangAsyncCallback<LoginResponse>() {

			public void onResponseSuccess(LoginResponse loginResponse) {
				if(loginResponse == null){
					//LOGIN FAILED
					view.getDomain().setValue(domain);
					LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar automaticamente.");
					handler.onResponse(false);
				} else {
					onLoginSuccess(loginResponse);
				}
			}

			public void onResponseFailure(Throwable caught) {
				onLoginFailed();
			}
		});
	}

	private void getDomains(final ResponseHandler<String[]> handler) {
		service.getDomains(new BigBangAsyncCallback<LoginDomain[]>() {
			@Override
			public void onResponseSuccess(LoginDomain[] result) {
				domains = result;
				String[] response = new String[result.length];
				for (int i = 0; i < result.length; i++) {
					response[i] = result[i].domainName;
				}
				handler.onResponse(response);
			}
			@Override
			public void onResponseFailure(Throwable caught) {
				handler.onError(new String[] {new String("Could not get the application domains.")});
				super.onResponseFailure(caught);
			}
		});
	}

	private void checkLogin(String username, String password, final String domain){
		view.showLoading(true);
		service.login(username, password, domain, new BigBangAsyncCallback<LoginResponse>() {

			public void onResponseSuccess(LoginResponse loginResponse) {
				onLoginSuccess(loginResponse);
			}

			public void onResponseFailure(Throwable caught) {
				onLoginFailed();
			}
		});
		view.getPassword().setValue("");
	}

	private boolean isKnownDomain(String domain) {
		if(domain != null) {
			for(int i = 0; i < domains.length; i++){
				if(domain.equalsIgnoreCase(domains[i].domainName)){
					return true;
				}
			}
		}
		return false;
	}

	public void bind() {
		if(bound) {return;}
		
		view.getSubmitButton().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				checkLogin(view.getUsername().getValue(), view.getPassword().getValue(), view.getDomain().getValue());
			}
		});
		
		//APPLICATION-WIDE
		
		bound = true;
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}
	
	private void onLoginSuccess(LoginResponse loginResponse){
		Session.setUserId(loginResponse.userId);
		Session.setUsername(loginResponse.userName);
		Session.setDisplayName("TODO");
		Session.setDomain(loginResponse.domain);
		Session.setIsRoot(loginResponse.isSU);
		Session.setIsAgent(loginResponse.isAgent);

		this.view.showLoading(false);
		GWT.log("Authentication success for " + Session.getUsername());
		EventBus.getInstance().fireEvent(new LoginSuccessEvent());
	}
	
	private void onLoginFailed(){
		GWT.log("Authentication service failure");
		LoginViewPresenter.this.view.showLoading(false);
		LoginViewPresenter.this.view.showErrorMessage("Não foi possível autenticar. Verifique se as credenciais de acesso inseridas são as correctas");
	}
	
}
