package bigBang.library.client.userInterface.view;


import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;


public abstract class DocumentSections{

	public static class GeneralInfoSection extends View{
		
		VerticalPanel wrapper;
		private TextBoxFormField name;
		private ExpandableListBoxFormField docType;
		Document doc = new Document();
		ActionInvokedEventHandler<Action> actionHandler;
		
		
		BigBangOperationsToolBar toolbar;
		MenuItem delete;
		
		public GeneralInfoSection(){

			delete = new MenuItem("Eliminar", new Command() {
				
				@Override
				public void execute() {
					fireAction(Action.DELETE);
					
				}
			});
			
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
				
				@Override
				public void setSaveModeEnabled(boolean enabled) {
					super.setSaveModeEnabled(enabled);
					delete.setEnabled(enabled);
				}

			};
	
			wrapper = new VerticalPanel();
			initWidget(wrapper);
			wrapper.setWidth("100%");
			toolbar.hideAll();
			toolbar.showItem(SUB_MENU.EDIT, true);
			toolbar.addItem(delete);
			delete.getElement().getStyle().setProperty("textAlign", "center");
			delete.setEnabled(false);
			//delete.setVisible(false);
			toolbar.setHeight("21px");
			toolbar.setWidth("100%");
			wrapper.add(toolbar);
			
			setName(new TextBoxFormField("Nome"));
			setDocType(new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo"));
			
			wrapper.add(getName());
			wrapper.add(getDocType());
			
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
			getName().setValue(doc.name);
			getDocType().setValue(doc.docTypeId);
			
		}
		
		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
		}

		public void setEditable(boolean b) {
			getDocType().setReadOnly(!b);
			getName().setReadOnly(!b);
			
		}

		public BigBangOperationsToolBar getToolbar() {
			return toolbar;
			
		}

		public TextBoxFormField getName() {
			return name;
		}

		public void setName(TextBoxFormField name) {
			this.name = name;
		}

		public ExpandableListBoxFormField getDocType() {
			return docType;
		}

		public void setDocType(ExpandableListBoxFormField docType) {
			this.docType = docType;
		}

		public void enableDelete(boolean b) {
			delete.setEnabled(b);
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
		private Button changeToFile;
		private Button changeToNote;
		private boolean isFileBoolean;
		private TextBoxFormField filename;
		private Button removeFile;
		
		private Image mimeImg;
		
		private Label charRemain;
		private Label charRemainLabel;
		
		private HorizontalPanel filenameRemoveButton;
		
		private ActionInvokedEventHandler<Action> actionHandler;
		private boolean hasFile;
		
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
			upload = (new FileUpload());
			getUpload().setName("upload");
			changeToFile = new Button("Substituir por ficheiro");
			changeToFile.addClickHandler(handler);
			changeToNote = new Button("Substituir por nota");
			changeToNote.addClickHandler(handler);
			
			note = new TextAreaFormField("Nota");
			charRemainP = new HorizontalPanel();
			charRemain = new Label("Caracteres Restantes: ");
			charRemainLabel = new Label(""+ MAXCHAR);
			charRemainP.add(charRemain);
			charRemainP.add(charRemainLabel);
			getNote().setMaxCharacters(MAXCHAR, charRemainLabel);
			
			wrapper.setWidth("100%");
			buttonsFileorNote = new HorizontalPanel();
			
			wrapper.add(getNote());
			wrapper.add(charRemainP);
			buttonsFileorNote.add(isFile);
			buttonsFileorNote.add(isText);
			wrapper.add(buttonsFileorNote);
			wrapper.add(getUpload());
			
			removeFile = new Button("Apagar Ficheiro");
			removeFile.addClickHandler(handler);
			
			filename = new TextBoxFormField("Nome do Ficheiro");
			getFilename().setEditable(false);
			mimeImg = new Image();
			
			
			filenameRemoveButton = new HorizontalPanel();
			filenameRemoveButton.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

			filenameRemoveButton.add(mimeImg);
			filenameRemoveButton.add(getFilename());
			filenameRemoveButton.add(removeFile);
			
			
			wrapper.add(filenameRemoveButton);
			
			submitChange = new HorizontalPanel();
			
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
			
			
			removeFile.setVisible(false);
			changeToNote.setVisible(false);
			getFilename().setVisible(false);
			getUpload().setVisible(false);
			changeToFile.setVisible(false);
			getNote().setVisible(false);
			charRemainP.setVisible(false);
			
			isFile.setHeight("90px");
			isFile.setWidth("90px");
			
			isText.setHeight("90px");
			isText.setWidth("90px");
			
			isFile.setVisible(true);
			isText.setVisible(true);
			buttonsFileorNote.setVisible(true);

			
		}
		
		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
		}

		public void createNewFile() {
			
			isFileBoolean = true;
			getFilename().setVisible(true);
			buttonsFileorNote.setVisible(false);
			changeToFile.setVisible(false);
			getNote().setVisible(false);
			getUpload().setVisible(true);
			changeToNote.setVisible(true);
			charRemainP.setVisible(false);
			filenameRemoveButton.setVisible(false);
			
		}

		public void createNewNote() {
			
			isFileBoolean = false;
			buttonsFileorNote.setVisible(false);
			getNote().setVisible(true);
			getUpload().setVisible(false);
			changeToNote.setVisible(false);
			changeToFile.setVisible(true);
			charRemainP.setVisible(true);
			filenameRemoveButton.setVisible(false);
		
		}

		public void setDocumentFile(Document doc) {
			
			this.getFilename().setValue(doc.fileName);
			mimeImg.setResource(getMimeImage(doc.mimeType));
			mimeImg.setVisible(true);
			getUpload().setVisible(false);
			changeToNote.setVisible(false);
			filenameRemoveButton.setVisible(true);
			hasFile = true;
			
		}
		
		public void setDocumentNote(Document doc){
			
			this.getNote().setValue(doc.text);
			
		}

		private ImageResource getMimeImage(String mimeType) {
			
			Resources resources = GWT.create(Resources.class);
			ImageResource mimeImage;
			
			if(mimeType.equalsIgnoreCase("text/plain")){
				mimeImage = resources.txtIcon();
			}
			else if(mimeType.equalsIgnoreCase("application/pdf")){
				mimeImage = resources.pdfIcon();
			}
			else if(mimeType.equalsIgnoreCase("application/excel")
					|| mimeType.equalsIgnoreCase("application/vnd.ms-excel")
					|| mimeType.equalsIgnoreCase("application/x-excel")
					|| mimeType.equalsIgnoreCase("application/x-msexcel")){
				mimeImage = resources.pdfIcon();
			}
			else if(mimeType.equalsIgnoreCase("application/msword")){
				mimeImage = resources.docIcon();
			}
			else if(mimeType.equalsIgnoreCase("image/jpeg")  
					|| mimeType.equalsIgnoreCase("image/bmp") 
					|| mimeType.equalsIgnoreCase("image/png") 
					|| mimeType.equalsIgnoreCase("image/tiff")){
				mimeImage = resources.imageIcon();
			}
			else if(mimeType.equalsIgnoreCase("application/powerpoint")
					|| mimeType.equalsIgnoreCase("application/mspowerpoint")
					|| mimeType.equalsIgnoreCase("application/vnd.ms-powerpoint")
					|| mimeType.equalsIgnoreCase("application/x-mspowerpoint")){
				mimeImage = resources.pptIcon();
			}
			else if(mimeType.equalsIgnoreCase("application/x-compressed")
					|| mimeType.equalsIgnoreCase("application/x-zip-compressed") 
					|| mimeType.equalsIgnoreCase("application/zip") 
					|| mimeType.equalsIgnoreCase("multipart/x-zip") 
					|| mimeType.equalsIgnoreCase("multipart/x-zip")){
				mimeImage = resources.zipIcon();
			}
			else
				mimeImage = resources.fileIcon();
			
			return mimeImage;
		}

		public void setEditable(boolean b) {
			
				isFile.setEnabled(b);
				isText.setEnabled(b);
				
				
			if(!isFileBoolean()){
				getNote().setReadOnly(!b);
				changeToFile.setVisible(b);
				charRemainLabel.setVisible(b);
				charRemain.setVisible(b);
			}
			else{
				removeFile.setVisible(b);
			}
			
			if(!hasFile && isFileBoolean()){
				changeToNote.setVisible(b);
				removeFile.setVisible(false);
			}
			
			if(isFile.isVisible()){
				changeToNote.setVisible(false);
				changeToFile.setVisible(false);
			}

		}

		public void removeFile() {
			
			hasFile = false;
			getFilename().setVisible(false);
			mimeImg.setVisible(false);
			removeFile.setVisible(false);
			getUpload().setVisible(true);
			setEditable(true);
		}

		public TextBoxFormField getFilename() {
			return filename;
		}

		public TextAreaFormField getNote() {
			return note;
		}


		public boolean isFileBoolean() {
			return isFileBoolean;
		}

		public FileUpload getUpload() {
			return upload;
		}
		
	}
	
	public static class DetailsSection extends View{
		
		public class DocumentDetailEntry extends ListEntry<DocInfo>{

			private TextBoxFormField  info;
			private TextBoxFormField infoValue;
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

				setInfo(new TextBoxFormField());
				setInfoValue(new TextBoxFormField());

				getInfo().setValue(docInfo.name);
				getInfoValue().setValue(docInfo.value);
				
				remove = new Button("X");
				remove.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						fireEvent(new DeleteRequestEvent(getValue()));
					}
				});
				this.setLeftWidget(getInfo());
				this.setWidget(getInfoValue());
				this.setRightWidget(remove);
				super.setValue(docInfo);
			}

			public void setEditable(boolean editable){

				if(getInfo() == null){
					this.setVisible(editable);
					return;
				}
				getInfo().setReadOnly(!editable);
				getInfoValue().setReadOnly(!editable);
				remove.setVisible(editable);
			}


			@Override
			public DocInfo getValue() {
				return super.getValue();
			}

			public TextBoxFormField getInfo() {
				return info;
			}

			public void setInfo(TextBoxFormField info) {
				this.info = info;
			}

			public TextBoxFormField getInfoValue() {
				return infoValue;
			}

			public void setInfoValue(TextBoxFormField infoValue) {
				this.infoValue = infoValue;
			}
		}
		
		List<DocInfo> details;
		DocInfo[] docInfo;
		VerticalPanel wrapper;
		private ActionInvokedEventHandler<Action> actionHandler;
		private DeleteRequestEventHandler deleteHandler;
		
		
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
			temp.addHandler(deleteHandler, DeleteRequestEvent.TYPE);
			details.add(temp);

		}

		public void setEditable(boolean b) {

			if(details.size() > 1){
					for(int i = 0; i<details.size()-1; i++){
						
						((DocumentDetailEntry)details.get(i)).getInfo().setReadOnly(!b);
						((DocumentDetailEntry)details.get(i)).getInfoValue().setReadOnly(!b);
						((DocumentDetailEntry)details.get(i)).remove.setVisible(b);
					}
		
					details.get(details.size()-1).setVisible(b);
				}
		}
			
			
		
		 
		public List<DocInfo> getList(){
			
			return details;
			
		}

		public DocumentDetailEntry getNewDocumentDetailEntry() {
			DocInfo emptyd = new DocInfo();
			emptyd.name = "";
			emptyd.value = "";
			DocumentDetailEntry newD = new DocumentDetailEntry(emptyd);
			return newD;
		}

		public void registerDeleteHandler(
				DeleteRequestEventHandler deleteRequestEventHandler) {
			this.deleteHandler = deleteRequestEventHandler;
			
		}
		
	}
	

}
