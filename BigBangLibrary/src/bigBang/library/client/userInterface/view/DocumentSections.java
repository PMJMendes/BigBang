package bigBang.library.client.userInterface.view;


import org.gwt.mosaic.ui.client.MessageBox;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.client.FormField;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter.Action;
import bigBang.library.client.userInterface.view.FileUploadPopup.FileUploadPopupDisk;
import bigBang.library.client.userInterface.view.FileUploadPopup.FileUploadPopupDocuShare;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.AutoHorizontalAlignmentConstant;


public abstract class DocumentSections{

	public static class DocumentOperationsToolBar extends BigBangOperationsToolBar{

		private MenuItem delete;

		public DocumentOperationsToolBar(){
			super();
			delete = new MenuItem("Eliminar", new Command() {

				@Override
				public void execute() {
					onDeleteRequest();
				}

			});

			this.addItem(SUB_MENU.ADMIN, delete);
			this.hideAll();
			this.showItem(SUB_MENU.EDIT, true);
			this.adminSubMenu.setVisible(true);
			this.showItem(SUB_MENU.ADMIN, true);
			this.setHeight("21px");
			this.setWidth("100%");
			this.adminSubMenu.getElement().getStyle().setZIndex(12000);

		}

		@Override
		public void onEditRequest() {

		}

		@Override
		public void onSaveRequest() {
		}

		@Override
		public void onCancelRequest() {
		}

		public void onDeleteRequest(){

		}
	}

	public static class GeneralInfoSection extends View{

		VerticalPanel wrapper;
		private TextBoxFormField name;
		private ExpandableListBoxFormField docType;
		Document doc = new Document();
		ActionInvokedEventHandler<Action> actionHandler;
		DocumentOperationsToolBar toolbar;


		public GeneralInfoSection(){


			toolbar = new DocumentOperationsToolBar(){

				@Override
				public void onEditRequest() {
					fireAction(Action.EDIT);
				}

				@Override
				public void onSaveRequest(){
					fireAction(Action.SAVE);
				}
				@Override
				public void onCancelRequest() {
					fireAction(Action.CANCEL);
				}

				@Override
				public void onDeleteRequest() {
					MessageBox.confirm("Eliminar Modelo", "Tem certeza que pretende eliminar o documento seleccionado?", new MessageBox.ConfirmationCallback() {

						@Override
						public void onResult(boolean result) {
							if(result){
								fireAction(Action.DELETE);
							}
						}
					});
				}

			};

			wrapper = new VerticalPanel();
			initWidget(wrapper);
			wrapper.setWidth("100%");
			toolbar.showItem(SUB_MENU.EDIT, true);
			toolbar.setHeight("21px");
			toolbar.setWidth("100%");
			wrapper.add(toolbar);

			setName(new TextBoxFormField("Nome"));
			setDocType(new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo"));
			
			name.setWidth("100%");
			name.setFieldWidth("100%");
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
			this.setEditable(false);

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
	}


	public static class FileNoteSection extends View{

		private VerticalPanel filePanel = new VerticalPanel();
		private HorizontalPanel fileNotePanel = new HorizontalPanel();
		private VerticalPanel notePanel = new VerticalPanel();
		private Label fileLabel = new Label(" Ficheiro");
		private Label noteLabel = new Label(" Nota");
		HorizontalPanel mimeImageFileName;
		private static final int MAXCHAR = 250;
		private Button fileButton;
		private TextAreaFormField note;
		private VerticalPanel wrapper;
		private Button docuShareFileButton;
		private HorizontalPanel charRemainP;
		private ClickHandler handler;
		private FilenameTextBoxFormField filename;
		private Button removeFile;
		private FileUploadPopup uploadDialog;

		public FileUploadPopup getUploadDialog() {
			return uploadDialog;
		}

		private String fileUploadFilename = null;
		private String fileStorageId = null;

		private Image mimeImg;

		private Label charRemain;
		private Label charRemainLabel;

		private ActionInvokedEventHandler<Action> actionHandler;
		private boolean hasFile;

		public class FilenameTextBoxFormField extends TextBoxFormField{

			public FilenameTextBoxFormField(String filename){
				super(filename);
				((Widget)super.field).setStylePrimaryName("linkFileText");
			}
		}


		public FileNoteSection(){

			wrapper = new VerticalPanel();
			initWidget(wrapper);

			handler = new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if(event.getSource() == fileButton){
						fireAction(Action.NEW_FILE_FROM_DISK);
					}
					else if(event.getSource() == removeFile){
						fireAction(Action.REMOVE_FILE);
					}
					else if(event.getSource() == docuShareFileButton){
						fireAction(Action.NEW_FILE_FROM_DOCUSHARE);
					}
				}

			};

			fileButton = new Button("Upload de Ficheiro");
			fileButton.getElement().getStyle().setMarginLeft(5, Unit.PX);
			fileButton.addClickHandler(handler);
			docuShareFileButton = new Button("Ficheiro do DocuShare");
			docuShareFileButton.addClickHandler(handler);
			docuShareFileButton.getElement().getStyle().setMarginLeft(5, Unit.PX);

			noteLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
			note = new TextAreaFormField();
			note.getElement().getStyle().setMarginLeft(5, Unit.PX);
			
			
			charRemainP = new HorizontalPanel();
			charRemain = new Label("Caracteres Restantes: ");
			charRemain.getElement().getStyle().setMarginLeft(5, Unit.PX);
			charRemainLabel = new Label(""+ MAXCHAR);
			charRemainP.add(charRemain);
			charRemainP.add(charRemainLabel);
			getNote().setMaxCharacters(MAXCHAR, charRemainLabel);

			wrapper.setWidth("100%");
			
			note.setHeight("100%");
			note.getNativeField().setWidth("100%");
			note.getNativeField().setHeight("300px");
			note.setWidth("100%");
			
			fileNotePanel.add(filePanel);
			fileNotePanel.setCellHorizontalAlignment(filePanel, HasHorizontalAlignment.ALIGN_CENTER);
			fileNotePanel.setCellWidth(filePanel, "50%");
			fileNotePanel.add(notePanel);
			fileNotePanel.setCellHorizontalAlignment(notePanel, HasHorizontalAlignment.ALIGN_CENTER);
			fileNotePanel.setCellWidth(notePanel, "50%");
			fileNotePanel.setCellHeight(notePanel, "100%");
			fileNotePanel.setCellHeight(filePanel, "100%");
			fileNotePanel.setWidth("100%");
			filePanel.setWidth("100%");
			notePanel.setCellWidth(note, "100%");
			notePanel.setWidth("100%");


			fileLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
			filePanel.add(fileLabel);
			filePanel.add(fileButton);
			filePanel.setCellVerticalAlignment(fileButton, HasVerticalAlignment.ALIGN_MIDDLE);
			filePanel.add(docuShareFileButton);
			filePanel.setCellVerticalAlignment(docuShareFileButton, HasVerticalAlignment.ALIGN_MIDDLE);

			notePanel.add(noteLabel);
			notePanel.add(note);
			notePanel.add(charRemainP);

			wrapper.add(fileNotePanel);

			removeFile = new Button("Remover/Substituir");
			removeFile.addClickHandler(handler);
			removeFile.getElement().getStyle().setMarginLeft(5, Unit.PX);

			filename = new FilenameTextBoxFormField("Nome do Ficheiro");
			getFilename().setEditable(false);
			filename.setFieldWidth("");
			filename.addMouseUpHandler(new MouseUpHandler() {

				@Override
				public void onMouseUp(MouseUpEvent event) {
					fireAction(Action.DOWNLOAD_FILE);
				}
			});

			mimeImg = new Image();
			
			

			mimeImageFileName = new HorizontalPanel();
			mimeImageFileName.add(mimeImg);
			mimeImageFileName.add(filename);
			filePanel.add(mimeImageFileName);
			filePanel.add(removeFile);
			

		}

		@Override
		protected void initializeView() {
			return;
		}

		public void initHandler(ActionInvokedEventHandler<Action> actionHandler){

			this.actionHandler = actionHandler;

		}

		public Image getMimeImage(){
			return mimeImg;
		}

		protected void fireAction(Action action){
			if(this.actionHandler != null) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
			}
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

			mimeImg.setVisible(true);

			return mimeImage;
		}

		public void setEditable(boolean editable) {
		
			note.setReadOnly(!editable);
			fileButton.setEnabled(editable);
			docuShareFileButton.setEnabled(editable);
			removeFile.setEnabled(editable);

		}

		public void removeFile() {

			hasFile = false;
			getFilename().setVisible(false);
			removeFile.setVisible(false);

		}

		public void enableRemoveFile(boolean b){
			removeFile.setVisible(b);
		}

		public TextBoxFormField getFilename() {
			return filename;
		}

		public TextAreaFormField getNote() {
			return note;
		}

		public String getFileUploadFilename() {
			if(uploadDialog != null){
				fileUploadFilename = uploadDialog.getUploadPopup().getFilename();
			}
			return fileUploadFilename;
		}

		public void setFileUploadFilename(String fileUploadFilename) {
			this.fileUploadFilename = fileUploadFilename;
		}

		public String getFileStorageId() {
			if(uploadDialog != null){
				fileStorageId = uploadDialog.getUploadPopup().getFileStorageId();
			}
			return fileStorageId;
		}

		public void setFileStorageId(String fileStorageId) {
			this.fileStorageId = fileStorageId;
		}


		public void setUploadDialog(FileUploadPopupDisk fileUploadPopupDisk) {
			uploadDialog = fileUploadPopupDisk;

		}


		public void setUploadDialog(FileUploadPopupDocuShare fileUploadPopupDocuShare) {
			uploadDialog = fileUploadPopupDocuShare;

		}

		public void setDocument(Document doc) {
			
			if(doc.hasFile){
				hasFile(true);
				filename.setValue(doc.fileName);
				fileButton.setVisible(false);
				docuShareFileButton.setVisible(false);
				mimeImg.setResource(getMimeImage(doc.mimeType));
				mimeImageFileName.setVisible(true);
				removeFile.setVisible(true);
				hideNote();
			}
			else{
				hasFile(false);
				filename.setVisible(false);
				removeFile.setVisible(false);
				fileButton.setVisible(true);
				docuShareFileButton.setVisible(true);
				mimeImageFileName.setVisible(false);
				notePanel.setVisible(true);
			}
			
		}

		public void hasFile(boolean b) {
		
			hasFile = b;
			
		}
		
		private void hideNote(){
			
			notePanel.setVisible(false);
			
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

				this.info.setFieldWidth("");
				this.infoValue.setFieldWidth("100%");
				this.remove.setWidth("20px");
				super.setValue(docInfo);
				this.setLeftWidget(getInfo());
				this.setWidget(getInfoValue());
				this.setRightWidget(remove);
			}

			public void setEditable(boolean editable){

				if(getInfo() == null){
					this.setVisible(editable);
					return;
				}
				getInfo().setReadOnly(!editable);
				getInfoValue().setReadOnly(!editable);
				remove.setVisible(editable);
				add.setVisible(editable);
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
		private ActionInvokedEventHandler<Action> actionHandler;
		private DeleteRequestEventHandler deleteHandler;
		private Button add;


		public DetailsSection(){

			details = new List<DocInfo>();
			Widget widget = details.getScrollable();
			initWidget(widget);

			widget.setSize("100%", "100%");
			add = new Button("Adicionar Detalhe");
			add.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					fireAction(Action.ADD_NEW_DETAIL);
				}
			});
			add.setWidth("180px");

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
			}
			if(details.size() >= 1){
				details.get(details.size()-1).setVisible(b);
			}
			add.setVisible(b);
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
