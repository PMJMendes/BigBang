package bigBang.module.mainModule.client.userInterface.presenter;

import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Session;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.LogoutEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class MainScreenViewPresenter implements ViewPresenter {

	public enum Action{
		SHOW_PREFERENCES,
		HIDE_PREFERENCES,
		BACKOFFICE,
		LOGOUT,

		SHOW_SECTION_TASKS, 
		SHOW_SECTION_GENERAL_SYSTEM, 
		SHOW_SECTION_CLIENT, 
		SHOW_SECTION_QUOTE_REQUEST,
		SHOW_SECTION_INSURANCE_POLICY, 
		SHOW_SECTION_RECEIPT, 
		SHOW_SECTION_RISK_ANALISYS,
		SHOW_SECTION_CASUALTY,
		SHOW_SECTION_EXPENSE
	}

	public enum Section {
		GENERAL_SYSTEM,
		CLIENT,
		RISK_ANALISYS,
		INSURANCE_POLICY,
		RECEIPT,
		QUOTE_REQUEST,
		CASUALTY,
		EXPENSE,
		COMPLAINT,
		PERSONAL
	}

	public interface Display {
		Widget asWidget();
		void showSection(Section section);
		void setUsername(String username);
		void setDomain(String domain);
		void showBackOffice(boolean show);
		void showGenSystem(boolean show);
		HasWidgets getContainer();
		HasWidgets getPreferencesContainer();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void showPreferences(boolean show);
	}

	private Display view;
	private ViewPresenterController preferencesController;
	private ViewPresenterController controller;
	private boolean bound = false;

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
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		controller.onParameters(parameterHolder);
	}

	public void bind() {
		if(bound){
			return;
		}

		view.registerActionHandler(new ActionInvokedEventHandler<MainScreenViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				NavigationHistoryItem item = new NavigationHistoryItem();

				switch(action.getAction()){
				case LOGOUT:
					EventBus.getInstance().fireEvent(new LogoutEvent());
					break;
				case SHOW_PREFERENCES:
					if(preferencesController == null){
						initializePreferencesController();
					}
					MainScreenViewPresenter.this.preferencesController.onParameters(null);
					view.showPreferences(true);
					break;
				case HIDE_PREFERENCES:
					view.showPreferences(false);
					break;
				case BACKOFFICE:
					item.setParameter("section", "backoffice");
					NavigationHistoryManager.getInstance().go(item);
					break;

				case SHOW_SECTION_TASKS:
					item.setParameter("section", "agenda");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_GENERAL_SYSTEM:
					item.setParameter("section", "generalsystem");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_CLIENT:
					item.setParameter("section", "client");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_QUOTE_REQUEST:
					item.setParameter("section", "quoterequest");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_INSURANCE_POLICY:
					item.setParameter("section", "insurancepolicy");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_RECEIPT:
					item.setParameter("section", "receipt");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_RISK_ANALISYS:
//					item.setParameter("section", "riskanalisys");
//					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_CASUALTY:
					item.setParameter("section", "casualty");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case SHOW_SECTION_EXPENSE:
					item.setParameter("section", "expense");
					NavigationHistoryManager.getInstance().go(item);
				break;
				}
			}
		});

		//APPLICATION-WIDE EVENTS

		bound = true;
	}

	public HasWidgets getContainer(){
		return view.getContainer();
	}

	private void initializeController(){
		this.controller = new ViewPresenterController() {

			@Override
			public void onParameters(HasParameters parameters) {
				view.showBackOffice(Session.isRoot());
				view.showGenSystem(!Session.isAgent());

				String section = parameters.getParameter("section");
				section = section == null ? new String() : section;

				if(section.equalsIgnoreCase("agenda")){
					view.showSection(Section.PERSONAL);
				}else if(section.equalsIgnoreCase("generalsystem")){
					view.showSection(Section.GENERAL_SYSTEM);
				}else if(section.equalsIgnoreCase("client")){
					view.showSection(Section.CLIENT);
				}else if(section.equalsIgnoreCase("insurancepolicy")){
					view.showSection(Section.INSURANCE_POLICY);
				}else if(section.equalsIgnoreCase("casualty")){
					view.showSection(Section.CASUALTY);
				}else if(section.equalsIgnoreCase("quoterequest")){
					view.showSection(Section.QUOTE_REQUEST);
				}else if(section.equalsIgnoreCase("expense")){
					view.showSection(Section.EXPENSE);
				}else if(section.equalsIgnoreCase("complaint")){
					view.showSection(Section.COMPLAINT);
				}else if(section.equalsIgnoreCase("receipt")){
					view.showSection(Section.RECEIPT);
				}/*else if(section.equalsIgnoreCase("riskanalisys")){
					view.showSection(Section.RISK_ANALISYS);
				}*/
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

	private void initializePreferencesController(){
		this.preferencesController = new ViewPresenterController(view.getPreferencesContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				present("CHANGE_PASSWORD", parameters);
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
