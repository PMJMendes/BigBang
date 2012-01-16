package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.HasParameters;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection.DocumentDetailEntry;
import bigBang.library.client.userInterface.view.DocumentSections.FileNoteSection;
import bigBang.library.client.userInterface.view.DocumentSections.GeneralInfoSection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DocumentViewPresenter implements ViewPresenter{

	private Display view;
	private Document doc;
	private boolean bound = false;

	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		NEW_FILE,
		NEW_NOTE, CHANGE_TO_FILE, CHANGE_TO_NOTE, ADD_NEW_DETAIL, REMOVE_FILE
	}

	public interface Display{

		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		public void setDocument(Document doc);
		void createNewFile();
		void createNewNote();
		void addDetail(DocInfo docInfo);
		GeneralInfoSection getGeneralInfo();
		FileNoteSection getFileNote();
		DetailsSection getDetails();
		void setEditable(boolean b);
		Document getInfo();
		DocumentDetailEntry initializeDocumentDetailEntry();

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
				case CANCEL:{
					view.setDocument(view.getInfo());
					view.setEditable(false);
					break;
				}
				case EDIT:{
					view.setEditable(true);
					break;
				}
				case NEW_NOTE:{
					view.createNewNote();
					break;
				}
				case SAVE:{
					break;
				}
				case CHANGE_TO_FILE: {
					view.createNewFile(); 
					break;
				}
				case CHANGE_TO_NOTE:{
					view.createNewNote(); 
					break;
				}
				case  ADD_NEW_DETAIL:{
					addDetail();
					break;
				}


				}

			}


		});
	}

	public void setDocument(Document doc) {

		this.doc = doc;
		view.setDocument(doc);
		DocInfo[] docInfo = doc.parameters;
		
		for(int i = 0; i< docInfo.length; i++){
			
			view.addDetail(docInfo[i]);
		}
		view.addDetail(null);
		view.setEditable(false);
	}
	
	
	public Document getInfo(){
		
		return this.doc;
		
	}
	
	public void addDetail(){
		
		DocumentDetailEntry temp = view.initializeDocumentDetailEntry();
		temp.setHeight("40px");
		view.getDetails().getList().remove(view.getDetails().getList().size()-1);
		temp.setEditable(true);
		view.getDetails().getList().add(temp);
		view.addDetail(null);
		
	}
	

}
