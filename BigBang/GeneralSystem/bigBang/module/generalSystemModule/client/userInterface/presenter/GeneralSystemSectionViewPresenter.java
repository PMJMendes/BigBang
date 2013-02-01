package bigBang.module.generalSystemModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class GeneralSystemSectionViewPresenter implements ViewPresenter {

	public static enum Action {
		ON_OVERLAY_CLOSED,
	}

	public static enum SectionOperation {
		HISTORY,
		USER,
		AGENCY,
		MEDIATOR,
		GROUP,
		TAX,
		COVERAGE,
		COST_CENTER,
		OTHER_ENTITIES
	}

	public static interface Display {
		HasWidgets getOperationViewContainer();
		HasWidgets getOverlayViewContainer();
		void showOverlayViewContainer(boolean show);
		void selectOperation(SectionOperation operation);
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void registerOperationSelectionHandler(ActionInvokedEventHandler<SectionOperation> handler);
		Widget asWidget();
	}

	private Display view;
	private ViewPresenterController controller;
	private ViewPresenterController overlayController;
	private boolean bound;

	public GeneralSystemSectionViewPresenter(Display view) {
		this.setView((UIObject)view);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}

	private void bind() {
		if(bound){
			return;
		}
		this.view.registerActionHandler(new ActionInvokedEventHandler<GeneralSystemSectionViewPresenter.Action>() {

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

		this.view.registerOperationSelectionHandler(new ActionInvokedEventHandler<GeneralSystemSectionViewPresenter.SectionOperation>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<SectionOperation> action) {

				NavigationHistoryItem item = new NavigationHistoryItem();
				item.setParameter("section", "generalsystem");
				item.setStackParameter("display");

				if(action.getAction() == null) {
					item.pushIntoStackParameter("display", "history");
				}else{
					switch(action.getAction()) {
					case HISTORY:
						item.pushIntoStackParameter("display", "history");
						break;
					case COST_CENTER:
						item.pushIntoStackParameter("display", "costcenter");
						break;
					case USER:
						item.pushIntoStackParameter("display", "user");
						break;
					case MEDIATOR:
						item.pushIntoStackParameter("display", "mediator");
						break;
					case AGENCY:
						item.pushIntoStackParameter("display", "insuranceagency");
						break;
					case TAX:
						item.pushIntoStackParameter("display", "tax");
						break;
					case COVERAGE:
						item.pushIntoStackParameter("display", "coverage");
						break;
					case GROUP:
						item.pushIntoStackParameter("display", "clientgroup");
						break;
					case OTHER_ENTITIES:
						item.pushIntoStackParameter("display", "otherentities");
					}
				}

				NavigationHistoryManager.getInstance().go(item);
			}
		});
		
		bound = true;
	}

	private void initializeController(){
		this.controller = new ViewPresenterController(view.getOperationViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				String section = parameters.getParameter("section");
				if(section != null && section.equalsIgnoreCase("generalsystem")){
					String display = parameters.peekInStackParameter("display");
					display = display == null ? "" : display;

					if(display.equalsIgnoreCase("history")){
						view.selectOperation(SectionOperation.HISTORY);
						parameters.setParameter("historyownerid", SessionGeneralSystem.getInstance().id);
						present("HISTORY", parameters, true);
					}else if(display.equalsIgnoreCase("user")){
						view.selectOperation(SectionOperation.USER);
						present("GENERAL_SYSTEM_USER_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("costcenter")){
						view.selectOperation(SectionOperation.COST_CENTER);
						present("GENERAL_SYSTEM_COST_CENTER_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("clientgroup")){
						view.selectOperation(SectionOperation.GROUP);
						present("GENERAL_SYSTEM_CLIENT_GROUP_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("coverage")){
						view.selectOperation(SectionOperation.COVERAGE);
						present("GENERAL_SYSTEM_COVERAGE_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("insuranceagency")){
						view.selectOperation(SectionOperation.AGENCY);
						present("GENERAL_SYSTEM_INSURANCE_AGENCY_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("mediator")){
						view.selectOperation(SectionOperation.MEDIATOR);
						present("GENERAL_SYSTEM_MEDIATOR_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("tax")){
						view.selectOperation(SectionOperation.TAX);
						present("GENERAL_SYSTEM_TAX_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("user")){
						view.selectOperation(SectionOperation.USER);
						present("GENERAL_SYSTEM_USER_MANAGEMENT", parameters, true);
					}else if(display.equalsIgnoreCase("otherentities")){
						view.selectOperation(SectionOperation.OTHER_ENTITIES);
						present("GENERAL_SYSTEM_OTHER_ENTITIES_MANAGEMENT", parameters, true);
					}else{
						goToDefault();
					}
				}
				GeneralSystemSectionViewPresenter.this.overlayController.onParameters(parameters);
			}

			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "history");
				navigationManager.go(item);
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
		this.overlayController = new ViewPresenterController(view.getOverlayViewContainer()) {

			@Override
			public void onParameters(HasParameters parameters) {
				String show = parameters.getParameter("show");
				show = show == null ? new String() : show;

				if(show.isEmpty()){
					view.showOverlayViewContainer(false);

					//OVERLAY VIEWS
				}else if(show.equalsIgnoreCase("contactmanagement")){
					present("CONTACT", parameters);
					view.showOverlayViewContainer(true);
				}else if(show.equalsIgnoreCase("documentmanagement")){
					present("DOCUMENT", parameters);
					view.showOverlayViewContainer(true);
				}
			}

			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
		};
	}

}
