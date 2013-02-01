package bigBang.module.loginModule.client;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Module;
import bigBang.library.client.Session;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.event.LogoutEventHandler;
import bigBang.library.client.event.SessionExpiredEvent;
import bigBang.library.client.event.SessionExpiredEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.loginModule.client.userInterface.presenter.ChangePasswordViewPresenter;
import bigBang.module.loginModule.client.userInterface.presenter.LoginViewPresenter;
import bigBang.module.loginModule.client.userInterface.view.ChangePasswordView;
import bigBang.module.loginModule.client.userInterface.view.LoginView;
import bigBang.module.loginModule.interfaces.AuthenticationService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class LoginModule implements Module {

	private boolean initialized = false;

	@Override
	public void initialize() {
		bindToEvents();
		registerViewPresenters();
		this.initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}

	private void registerViewPresenters(){
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("LOGIN", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				LoginView view = (LoginView) GWT.create(LoginView.class);
				LoginViewPresenter presenter = new LoginViewPresenter(view);
				return presenter;
			}
		});
		ViewPresenterFactory.getInstance().registerViewPresenterInstantiator("CHANGE_PASSWORD", new ViewPresenterInstantiator() {

			@Override
			public ViewPresenter getInstance() {
				ChangePasswordView view = (ChangePasswordView) GWT.create(ChangePasswordView.class);
				ViewPresenter presenter = new ChangePasswordViewPresenter(view);
				return presenter;
			}
		});
	}

	private void bindToEvents(){
		EventBus.getInstance().addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			@Override
			public void onLogout(LogoutEvent event) {
				if(Session.isValid()){
					AuthenticationService.Util.getInstance().logout(new BigBangAsyncCallback<String>() {

						@Override
						public void onResponseSuccess(String result) {
							String domain = Session.getDomain();
							GWT.log("logout");
							Session.invalidate();
							Window.Location.replace(GWT.getHostPageBaseURL() + "?domain=" + domain);
						}
					});
				}
			}
		});
		EventBus.getInstance().addHandler(SessionExpiredEvent.TYPE, new SessionExpiredEventHandler() {

			@Override
			public void onSessionExpired() {
				GWT.log("Session expired");
				Session.invalidate();
				EventBus.getInstance().fireEvent(new LogoutEvent());
				Window.Location.reload();			}
		});
	}

	@Override
	public DataBroker<?>[] getBrokerImplementations() {
		return null;
	}

	@Override
	public String[] getBrokerDependencies() {
		return null;
	}

}
