package bigBang.module.mainModule.client.userInterface.presenter;

import Jewel.Web.client.Jewel_Web;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Session;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.userInterface.MenuSection;
import bigBang.library.client.userInterface.presenter.SectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenViewPresenter implements ViewPresenter {
	
	public enum Action{
		SHOW_PREFERENCES,
		LOGOUT
	}
	
	public interface Display {
		Widget asWidget();
		void createMenuSection(SectionViewPresenter sectionPresenter);
		void showSection(MenuSection section) throws Exception;
		void setUsername(String username);
		void setDomain(String domain);
		void showFirstSection();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void showPreferences(boolean show);
	}

	private Display view;

	public MainScreenViewPresenter(View view){
		this.setView(view);
		initializeView();
	}
	
	private void initializeView(){
		view.setUsername(Session.getUsername());
		view.setDomain(Session.getDomain());
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());		
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}

	public void bind() {
		view.registerActionHandler(new ActionInvokedEventHandler<MainScreenViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case LOGOUT:
					EventBus.getInstance().fireEvent(new LogoutEvent());
					break;
				case SHOW_PREFERENCES:
					//view.showPreferences(true); TODO FJVC
					new Jewel_Web().onModuleLoad();
					break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS
	}

}
