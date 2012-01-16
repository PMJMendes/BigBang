package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.Document;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Display;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;

public abstract class DocumentViewPresenter implements ViewPresenter{

	private Display view;
	private Document doc;
	private boolean bound = false;
	
	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		CREATE_NEW_DOCUMENT,
		NEW_FILE,
		NEW_NOTE, CHANGE_TO_FILE, CHANGE_TO_NOTE
	}
	
	public interface Display{
		
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		public void setDocument(Document doc);
		void createNewFile();
		void createNewNote();
		
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}
	
	private void bind() {
		if(bound){
			return;
		}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>(){

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				
				switch(action.getAction()){
				case NEW_FILE: 
					view.createNewFile(); 
					break;
				case CREATE_NEW_DOCUMENT: ; break;
				case CANCEL: break;
				case EDIT: break;
				case NEW_NOTE: view.createNewNote();
				case SAVE: break;
				case CHANGE_TO_FILE: view.createNewFile();
				case CHANGE_TO_NOTE: view.createNewNote();
				
				
				}
				
			}
			
			
		});
	}

	private void setDocument(Document doc) {
		
		this.doc = doc;
		view.setDocument(doc);
		
	}
	
}
