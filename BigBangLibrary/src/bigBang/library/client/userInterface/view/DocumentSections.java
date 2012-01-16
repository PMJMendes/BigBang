package bigBang.library.client.userInterface.view;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.userInterface.view.ContactView.ContactEntry;
import bigBang.library.client.userInterface.view.DocumentSections.DetailsSection.DocumentDetailEntry;


public abstract class DocumentSections{

	public static class GeneralInfoSection extends View{
		
		VerticalPanel wrapper;
		TextBoxFormField name;
		ExpandableListBoxFormField docType;
		Document doc = new Document();
		ActionInvokedEventHandler<Action> actionHandler;
		
		public GeneralInfoSection(){

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
			wrapper.setWidth("100%");
			toolbar.hideAll();
			toolbar.showItem(SUB_MENU.EDIT, true);
			
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

		public void setEditable(boolean b) {
			docType.setReadOnly(!b);
			name.setReadOnly(!b);
			
		}
		
	}
	
	
	public static class FileNoteSection extends View{

		private static final int MAXCHAR = 250;
		private Button isFile;
		private Button isText;
		private TextAreaFormField note;
		private VerticalPanel wrapper;
		private HorizontalPanel buttonsFileorNote;
		private HorizontalPanel submitChange;
		private HorizontalPanel charRemainP;
		private FileUpload upload;
		private ClickHandler handler;
		private Button submit;
		private Button changeToFile;
		private Button changeToNote;
		
		private TextBoxFormField filename;
		private Button removeFile;
		
		private HorizontalPanel filenameRemoveButton;
		
		private ActionInvokedEventHandler<Action> actionHandler;
		
		public FileNoteSection(){
			
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
					else if(event.getSource() == changeToNote){
						fireAction(Action.CHANGE_TO_NOTE);
					}
					else if(event.getSource() == removeFile){
						fireAction(Action.REMOVE_FILE);
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
			changeToNote.addClickHandler(handler);
			
			note = new TextAreaFormField("Nota");
			charRemainP = new HorizontalPanel();
			Label charRemain = new Label("Caracteres Restantes: ");
			Label charRemainLabel = new Label(""+ MAXCHAR);
			charRemainP.add(charRemain);
			charRemainP.add(charRemainLabel);
			note.setMaxCharacters(MAXCHAR, charRemainLabel);
			
			wrapper.setWidth("100%");
			buttonsFileorNote = new HorizontalPanel();
			
			wrapper.add(note);
			wrapper.add(charRemainP);
			wrapper.add(submit);
			buttonsFileorNote.add(isFile);
			buttonsFileorNote.add(isText);
			wrapper.add(buttonsFileorNote);
			wrapper.add(upload);
			
			removeFile = new Button("Apagar Ficheiro");
			removeFile.addClickHandler(handler);
			
			filename = new TextBoxFormField("Nome do Ficheiro");
			filename.setEditable(false);
			filenameRemoveButton = new HorizontalPanel();
			
			filenameRemoveButton.add(filename);
			filenameRemoveButton.add(removeFile);
			
			
			wrapper.add(filenameRemoveButton);
			
			submitChange = new HorizontalPanel();
			
			submitChange.add(submit);
			submitChange.add(changeToFile);
			submitChange.add(changeToNote);
			wrapper.add(submitChange);
			
			wrapper.add(filenameRemoveButton);
			
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
			charRemainP.setVisible(false);
			
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
			changeToFile.setVisible(false);
			note.setVisible(false);
			submit.setVisible(true);
			submit.setText("Submeter ficheiro");
			upload.setVisible(true);
			changeToNote.setVisible(true);
			submitChange.setVisible(true);
			charRemainP.setVisible(false);
			filenameRemoveButton.setVisible(false);
			
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
			charRemainP.setVisible(true);
			filenameRemoveButton.setVisible(false);
			
		
		}

		public void setDocument(Document doc) {

			if(doc.fileStorageId != null){
				createNewFile();
				this.filename.setValue(doc.fileName);
				upload.setVisible(false);
				submitChange.setVisible(false);
				filenameRemoveButton.setVisible(true);
				
			}
			else{
				createNewNote();
			}
			
			
		}

		public void setEditable(boolean b) {
			
			removeFile.setVisible(b);
			
		}
		
	}
	
	public static class DetailsSection extends View{
		
		public class DocumentDetailEntry extends ListEntry<DocInfo>{

			protected TextBoxFormField  info;
			protected TextBoxFormField infoValue;
			private Button remove;
			public DocumentDetailEntry(DocInfo docInfo) {
				super(docInfo);
			}

			@Override
			public void setValue(DocInfo docInfo) {


				if(docInfo == null){
					Button add = new Button("Adicionar Detalhe");
					add.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							fireAction(Action.ADD_NEW_DETAIL);
						}
					});
					add.setWidth("180px");
					this.setLeftWidget(add);
					super.setValue(docInfo);
					return;	

				}

				info = new TextBoxFormField();
				infoValue = new TextBoxFormField();

				info.setValue(docInfo.name);
				infoValue.setValue(docInfo.value);
				
				remove = new Button("X");
				remove.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						fireEvent(new DeleteRequestEvent(getValue()));
					}
				});
				this.setLeftWidget(info);
				this.setWidget(infoValue);
				this.setRightWidget(remove);
				super.setValue(docInfo);
			}

			public void setEditable(boolean editable){

				if(info == null){
					this.setVisible(editable);
					return;
				}
				info.setReadOnly(!editable);
				infoValue.setReadOnly(!editable);
				remove.setVisible(editable);
			}


			@Override
			public DocInfo getValue() {
				return super.getValue();
			}
		}
		
		List<DocInfo> details;
		DocInfo[] docInfo;
		VerticalPanel wrapper;
		private ActionInvokedEventHandler<Action> actionHandler;
		
		
		public DetailsSection(){
			
			wrapper = new VerticalPanel();
			initWidget(wrapper);
			wrapper.setWidth("100%");
			details = new List<DocInfo>();
			wrapper.add(details.getScrollable());
			
		}
		
		@Override
		protected void initializeView() {
			return;
		}
		
		public void initHandler(ActionInvokedEventHandler<Action> actionHandler){
			
			this.actionHandler = actionHandler;
			
		}
		
		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
		}
		
		
		public void addDocumentDetail(DocInfo docInfo){


			DocumentDetailEntry temp = new DocumentDetailEntry(docInfo);
			temp.setHeight("40px");
			//temp.addHandler(deleteHandler, DeleteRequestEvent.TYPE);
			details.add(temp);

		}

		public void setEditable(boolean b) {
			
			for(int i = 0; i<details.size()-1; i++){
				
				((DocumentDetailEntry)details.get(i)).info.setReadOnly(!b);
				((DocumentDetailEntry)details.get(i)).infoValue.setReadOnly(!b);
				((DocumentDetailEntry)details.get(i)).remove.setVisible(b);
			}

			details.get(details.size()-1).setVisible(b);
			
			
		}
		 
		public List<DocInfo> getList(){
			
			return details;
			
		}

		public DocumentDetailEntry getNewDocumentDetailEntry() {
			DocInfo emptyd = new DocInfo();
			emptyd.name = "";
			emptyd.value = "";
			return new DocumentDetailEntry(emptyd);
		}
		
	}
	

}
