package bigBang.library.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;


public abstract class DocumentSections{

	public static class DocumentTopSection extends View{
		
		VerticalPanel wrapper;
		TextBoxFormField name;
		ExpandableListBoxFormField docType;
		Document doc = new Document();
		ActionInvokedEventHandler<Action> actionHandler;
		
		public DocumentTopSection(){

			BigBangOperationsToolBar toolbar;
			toolbar = new BigBangOperationsToolBar(){

				@Override
				public void onEditRequest() {
					fireAction(Action.EDIT);

				}

				@Override
				public void onSaveRequest() {
					fireAction(Action.SAVE);

				}

				@Override
				public void onCancelRequest() {
					fireAction(Action.CANCEL);

				}

			};
			
			
			wrapper = new VerticalPanel();
			initWidget(wrapper);
			toolbar.hideAll();
			toolbar.showItem(SUB_MENU.EDIT, true);
			toolbar.showItem(SUB_MENU.CREATE, true);
			toolbar.addItem(SUB_MENU.CREATE, new MenuItem("Documento", new Command() {

				@Override
				public void execute() {
					fireAction(Action.CREATE_NEW_DOCUMENT);
				}
			}));
			toolbar.setHeight("21px");
			toolbar.setWidth("100%");
			wrapper.add(toolbar);
			
			name = new TextBoxFormField("Nome");
			docType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo");
			
			wrapper.add(name);
			wrapper.add(docType);
			
		}
		
		@Override
		protected void initializeView() {
			return;
		}
		
		public void initHandler(ActionInvokedEventHandler<Action> actionHandler){
			
			this.actionHandler = actionHandler;
			
		}
		
		public void setDocument(Document doc){
			
			this.doc = doc;
			
			name.setValue(doc.name);
			docType.setValue(doc.docTypeId);
			
		}
		
		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
		}
		
	}
	
	
	public static class DocumentMiddleSection extends View{

		private Button isFile;
		private Button isText;
		private TextAreaFormField note;
		private VerticalPanel wrapper;
		private HorizontalPanel buttonsFileorNote;
		private HorizontalPanel submitChange;
		private FileUpload upload;
		private ClickHandler handler;
		private Button submit;
		private Button changeToFile;
		private Button changeToNote;
		private ActionInvokedEventHandler<Action> actionHandler;
		
		public DocumentMiddleSection(){
			
			wrapper = new VerticalPanel();
			initWidget(wrapper);
		
			handler = new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					if(event.getSource() == isFile){
						fireAction(Action.NEW_FILE);
					}
					else if(event.getSource() == isText){
						fireAction(Action.NEW_NOTE);
					}
					else if(event.getSource() == changeToFile){
						fireAction(Action.CHANGE_TO_FILE);
					}
				}

			};
			isFile = new Button("Novo Ficheiro");
			isFile.addClickHandler(handler);
			isText = new Button("Nova Nota");
			isText.addClickHandler(handler);
			upload = new FileUpload();
			submit = new Button("Submeter");
			changeToFile = new Button("Substituir por ficheiro");
			changeToFile.addClickHandler(handler);
			changeToNote = new Button("Substituir por nota");
			
			note = new TextAreaFormField("Nota");
			note.setMaxCharacters(250);
			wrapper.setWidth("100%");
			buttonsFileorNote = new HorizontalPanel();
			
			wrapper.add(note);
			wrapper.add(submit);
			buttonsFileorNote.add(isFile);
			buttonsFileorNote.add(isText);
			wrapper.add(buttonsFileorNote);
			
			submitChange = new HorizontalPanel();
			
			submitChange.add(submit);
			submitChange.add(changeToFile);
			submitChange.add(upload);
			submitChange.add(changeToNote);
			
			wrapper.add(submitChange);
		}
		
		@Override
		protected void initializeView() {
			return;
		}
		
		public void initHandler(ActionInvokedEventHandler<Action> actionHandler){
			
			this.actionHandler = actionHandler;
			
		}
		
		public void generateNewDocument(){
			
			
			upload.setVisible(false);
			changeToFile.setVisible(false);
			note.setVisible(false);
			submit.setVisible(false);
			submitChange.setVisible(false);
			
			isFile.setHeight("80px");
			isFile.setWidth("80px");
			
			isText.setHeight("80px");
			isText.setWidth("80px");
			

				
			
		}
		
		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
		}

		public void createNewFile() {
			
			
			buttonsFileorNote.setVisible(false);
			note.setVisible(false);
			submit.setVisible(true);
			submit.setText("Submeter ficheiro");
			upload.setVisible(true);
			changeToNote.setVisible(true);
			submitChange.setVisible(true);
			
		}

		public void createNewNote() {
			
			
			buttonsFileorNote.setVisible(false);
			note.setVisible(true);
			submit.setVisible(true);
			submit.setText("Submeter nota");
			upload.setVisible(false);
			changeToNote.setVisible(false);
			changeToFile.setVisible(true);
			submitChange.setVisible(true);

			
		
		}
		
	}
	
	
	

}
