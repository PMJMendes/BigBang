package bigBang.module.casualtyModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterController;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class CasualtyOperationsViewPresenter implements ViewPresenter {

	public static interface Display {
		HasWidgets getContainer();
		Widget asWidget();
	}
	
	private Display view;
	private ViewPresenterController controller;
	
	public CasualtyOperationsViewPresenter(Display view){
		setView((UIObject) view);
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.view.asWidget());
		initializeController();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.controller.onParameters(parameterHolder);
	}
	
	public void initializeController(){
		this.controller = new ViewPresenterController(view.getContainer()) {
			
			@Override
			protected void onNavigationHistoryEvent(NavigationHistoryItem historyItem) {
				return;
			}
			
			@Override
			public void onParameters(HasParameters parameters) {
				String display = parameters.peekInStackParameter("display");
				display = display == null ? new String() : display;

				if(display.equalsIgnoreCase("search")){
					present("CASUALTY_SEARCH", parameters, true);
				}else if(display.equalsIgnoreCase("subcasualty")){
					present("SUB_CASUALTY_VIEW", parameters);
				}else if(display.equalsIgnoreCase("history")){
					present("HISTORY", parameters, true);
				}else if(display.equalsIgnoreCase("sendmessage")){
					present("CASUALTY_SEND_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("receivemessage")){
					present("CASUALTY_RECEIVE_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("subcasualtysendmessage")){
					present("SUB_CASUALTY_SEND_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("subcasualtyreceivemessage")){
					present("SUB_CASUALTY_RECEIVE_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("viewmanagertransfer")){
					present("MANAGER_TRANSFER", parameters);
				}else if(display.equalsIgnoreCase("conversation")){
					present("CASUALTY_CONVERSATION", parameters);
				}else if(display.equalsIgnoreCase("subcasualtyconversation")){
					present("SUB_CASUALTY_CONVERSATION", parameters);
				}else if(display.equalsIgnoreCase("assessment")){
					present("ASSESSMENT", parameters);
				}else if(display.equalsIgnoreCase("assessmentsendmessage")){
					present("ASSESSMENT_SEND_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("assessmentreceivemessage")){
					present("ASSESSMENT_RECEIVE_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("assessmentconversation")){
					present("ASSESSMENT_CONVERSATION", parameters);
				}else if(display.equalsIgnoreCase("medicalfile")){
					present("MEDICAL_FILE", parameters);
				}else if(display.equalsIgnoreCase("medicalfilereceivemessage")){
					present("MEDICAL_FILE_RECEIVE_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("medicalfilesendmessage")){
					present("MEDICAL_FILE_SEND_MESSAGE", parameters);
				}else if(display.equalsIgnoreCase("medicalfileconversation")){
					present("MEDICAL_FILE_CONVERSATION", parameters);
				}
				else{
						goToDefault();
				}
			}

			private void goToDefault(){
				NavigationHistoryItem item = navigationManager.getCurrentState();
				item.setStackParameter("display");
				item.pushIntoStackParameter("display", "search");
				navigationManager.go(item);
			}
		};
	}

}
