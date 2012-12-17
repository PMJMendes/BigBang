package bigBang.module.tasksModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TasksSectionViewPresenter implements ViewPresenter {

	public static enum Action{
		ON_OVERLAY_CLOSED

	}

	public static enum SectionOperation{
		AGENDA,
		MAIL_ORGANIZER,
		EMAIL
	}

	public interface Display {

		HasWidgets getOperationViewContainer();

		Widget asWidget();

		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);

		void registerOperationSelectionHandler(
				ActionInvokedEventHandler<SectionOperation> actionInvokedEventHandler);

		void selectOperation(SectionOperation agenda);

		HasWidgets getOverlayViewContainer();

		void showOverlayViewContainer(boolean b);

	}

	private Display view;
	private ViewPresenterController controller;
	private ViewPresenterController overlayController;
	private boolean bound;

	public TasksSectionViewPresenter(UIObject view){
		this.view = (Display) view;
	}

	private void initializeController(){
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}

			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("personal")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("agenda")){
						view.selectOperation(SectionOperation.AGENDA);
						present("AGENDA", parameters, true);
					}else if(display.equalsIgnoreCase("mailorganizer")){
						view.selectOperation(SectionOperation.MAIL_ORGANIZER);
						present("MAIL_ORGANIZER", parameters, true);
					}else if(display.equalsIgnoreCase("email")){
						view.selectOperation(SectionOperation.EMAIL);
						present("EMAIL", parameters, true);
					}else{
						goToDefault();
					}

				}
				TasksSectionViewPresenter.this.overlayController.onParameters(parameters);
			}

			protected void goToDefault() {
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "agenda");
				navigationManager.go(item);
			}

		};

		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onNavigationHistoryEvent(
					NavigationHistoryItem historyItem) {
				// TODO Auto-generated method stub

			}

		};

	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
		initializeController();
	}

	private void bind() {
		if(bound){
			return;
		}
		this.view.registerActionHandler(new ActionInvokedEventHandler<TasksSectionViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case ON_OVERLAY_CLOSED:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("show");
					NavigationHistoryManager.getInstance().go(item);
					break;
				}
			}			

		});

		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<TasksSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(
					ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "personal");
				item.setStackParameter("display");

				if(action.getAction() == null){
					item.pushIntoStackParameter("display", "agenda");
				}else{
					switch(action.getAction()){
					case AGENDA:
						item.pushIntoStackParameter("display", "agenda");
						break;
					case MAIL_ORGANIZER:
						item.pushIntoStackParameter("display", "mailorganizer");
						break;
					case EMAIL:
						item.pushIntoStackParameter("display", "email");
						break;
					}
					

					NavigationHistoryManager.getInstance().go(item);
				}
			}
		});

		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);		
	}	

}
