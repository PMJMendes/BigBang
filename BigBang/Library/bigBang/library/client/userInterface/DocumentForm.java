package bigBang.library.client.userInterface;

import java.util.Iterator;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Document;
import bigBang.library.client.event.ContentChangedEvent;
import bigBang.library.client.event.ContentChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.FileUploadPopup;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class DocumentForm extends FormView<Document>{

	public class FilenameTextBoxFormField extends TextBoxFormField{

		public FilenameTextBoxFormField(String filename){
			super(filename);
			((Widget)super.field).setStylePrimaryName("linkFileText");
		}
	}

	public class DocumentDetailEntry extends ListEntry<DocInfo>{

		private TextBoxFormField  info;
		private TextBoxFormField infoValue;
		private Button remove;
		public DocumentDetailEntry(DocInfo docInfo) {
			super(docInfo);
			this.setHeight("40px");
			this.setSelectable(false);
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
			remove.addClickHandler( new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onRemoveListEntry(DocumentDetailEntry.this);
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
			DocInfo docInfo = new DocInfo();
			docInfo.name = info.getValue();
			docInfo.value = infoValue.getValue();
			return docInfo;
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

	TextBoxFormField name;
	ExpandableListBoxFormField docType;
	VerticalPanel filePanel;
	private HorizontalPanel fileNotePanel;
	private VerticalPanel notePanel;
	HorizontalPanel mimeImageFileName;
	private static final int MAXCHAR = 250;
	private Button fileButton;
	private TextAreaFormField note;
	private VerticalPanel wrapper;
	private Button docuShareFileButton;
	private HorizontalPanel charRemainP;
	private ClickHandler handler;
	protected FilenameTextBoxFormField filename;
	private Button removeFile;
	protected FileUploadPopup uploadDialog;
	private Image mimeImg;
	private Label charRemain;
	private Label charRemainLabel;
	private Label noteLabel;
	private Label fileLabel;
	private SubmitCompleteHandler submitHandler;
	private ValueChangeHandler<DocuShareItem> valueChangeHandler;
	private Button add;
	List<DocInfo> details;
	DocInfo[] docInfo;
	//private DeleteRequestEventHandler deleteHandler;
	private boolean initialized = false;
	protected String fileStorageId;
	private boolean hasFile;
	protected String mimeType;
	protected DocuShareHandle docushareHandle;


	public DocumentForm(){

		addSection("Informação Geral");
		name = new TextBoxFormField("Nome");
		docType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "Tipo");
		name.setWidth("390px");
		name.setFieldWidth("390px");
		addFormField(name);
		addFormField(docType);

		//HANDLERS

		handler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == fileButton)
					onPressedFileButton();
				else if(event.getSource() == docuShareFileButton)
					onPressedDocushareButton();
				else if(event.getSource() == removeFile)
					onPressedRemoveFile();
				else if(event.getSource() == add)
					onAddDetail();
			}
		};

		//FILE/NOTE

		addSection("Ficheiro/Nota");
		filePanel = new VerticalPanel();
		fileNotePanel = new HorizontalPanel();
		notePanel = new VerticalPanel();
		wrapper = new VerticalPanel();
		fileButton = new Button("Upload de Ficheiro");
		fileButton = new Button("Upload de Ficheiro");
		fileButton.setWidth("145px");
		fileButton.addClickHandler(handler);
		docuShareFileButton = new Button("Ficheiro do DocuShare");
		docuShareFileButton.addClickHandler(handler);
		docuShareFileButton.setWidth("145px");
		noteLabel = new Label("Nota");
		note = new TextAreaFormField();

		charRemainP = new HorizontalPanel();
		charRemain = new Label("Caracteres Restantes: ");
		charRemainLabel = new Label(""+ MAXCHAR);
		charRemainP.add(charRemain);
		charRemainP.add(charRemainLabel);
		note.setMaxCharacters(MAXCHAR, charRemainLabel);

		wrapper.setWidth("100%");

		note.setHeight("100%");
		note.getNativeField().setWidth("225px");
		note.getNativeField().setHeight("100px");


		fileNotePanel.add(filePanel);
		fileNotePanel.setCellHorizontalAlignment(filePanel, HasHorizontalAlignment.ALIGN_CENTER);
		fileNotePanel.setCellWidth(filePanel, "40%");
		fileNotePanel.add(notePanel);
		fileNotePanel.setCellHorizontalAlignment(notePanel, HasHorizontalAlignment.ALIGN_CENTER);
		fileNotePanel.setCellWidth(notePanel, "60%");
		fileNotePanel.setCellHeight(notePanel, "100%");
		fileNotePanel.setCellHeight(filePanel, "100%");
		fileNotePanel.setWidth("400px");
		notePanel.setCellWidth(note, "100%");

		notePanel.getElement().getStyle().setProperty("borderLeft", "1px solid gray");		

		fileLabel = new Label("Ficheiro");
		fileLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		fileLabel.setWidth("145px");
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
		removeFile.setVisible(false);

		filename = new FilenameTextBoxFormField("");
		filename.setVisible(false);
		filename.setWidth("120px");
		filename.setEditable(false);
		filename.setFieldWidth("120px");
		filename.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				onDownloadFile();
			}
		});

		mimeImg = new Image();

		mimeImageFileName = new HorizontalPanel();
		mimeImageFileName.add(mimeImg);
		mimeImageFileName.add(filename);

		mimeImageFileName.setCellVerticalAlignment(mimeImg, HasVerticalAlignment.ALIGN_MIDDLE);
		mimeImageFileName.setCellVerticalAlignment(filename, HasVerticalAlignment.ALIGN_MIDDLE);
		filePanel.add(mimeImageFileName);
		filePanel.add(removeFile);

		addWidget(wrapper);

		//DETAILS
		addSection("Detalhes");
		currentSection.getElement().getStyle().setPadding(0, Unit.PX);
		details = new List<DocInfo>();
		details.setSelectableEntries(false);
		details.setSize("100%", "300px");
		add = new Button("Adicionar Detalhe");
		add.addClickHandler(handler);
		addWidget(details);

		//ADDHANDLERS
		note.addHandler(new ContentChangedEventHandler() {

			@Override
			public void onContentChanged() {
				if(note.getValue() != null){
					if(note.getValue().length() > 0){
						fileButton.setEnabled(false);
						docuShareFileButton.setEnabled(false);
					}
				}else{
					fileButton.setEnabled(true);
					docuShareFileButton.setEnabled(true);
				}
			}
		}, ContentChangedEvent.TYPE);


		initialized = true;
	}


	public void addDocumentDetail(DocInfo docInfo){

		DocumentDetailEntry temp = new DocumentDetailEntry(docInfo);
		temp.setHeight("40px");
		details.add(temp);
		details.getScrollable().scrollToBottom();

	}

	protected void onAddDetail() {

		DocInfo emptyd = new DocInfo();
		emptyd.name = "";
		emptyd.value = "";

		details.add(details.size()-1,new DocumentDetailEntry(emptyd));


	}


	protected void onPressedDocushareButton(){

		uploadDialog = new FileUploadPopup.FileUploadPopupDocuShare();
		uploadDialog.setParameters(value.ownerId, value.ownerTypeId);

		valueChangeHandler = new ValueChangeHandler<DocuShareItem>() {

			@Override
			public void onValueChange(ValueChangeEvent<DocuShareItem> event) {
				docushareHandle = new DocuShareHandle();
				docushareHandle.handle = event.getValue().handle;
				docushareHandle.locationHandle = uploadDialog.getDirectoryHandle();
				onDocushareItemChanged(event.getValue());
			}
		};

		((FileUploadPopup.FileUploadPopupDocuShare) uploadDialog).getPanel().addValueChangeHandler(valueChangeHandler);

	}

	protected void onPressedFileButton(){

		uploadDialog = new FileUploadPopup.FileUploadPopupDisk(getKey());

		submitHandler = new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				DocumentForm.this.onSubmitComplete(event.getResults());
			}

		};

		((FileUploadPopup.FileUploadPopupDisk)uploadDialog).getSubmitForm().addSubmitCompleteHandler(submitHandler);

	}

	private String getKey() {
		return getValue() != null ? getValue().fileStorageId : null;
	}

	@Override
	public Document getInfo() {
		Document newDoc = value == null ? new Document() : value;

		newDoc.name = name.getValue();
		newDoc.docTypeId = docType.getValue();
		newDoc.fileName = filename.getValue();
		newDoc.fileStorageId = fileStorageId;
		newDoc.hasFile = hasFile;
		newDoc.mimeType = mimeType;

		Iterator<ListEntry<DocInfo>> iterator = details.iterator();
		ListEntry<DocInfo> temp;

		while(iterator.hasNext()){
			temp = iterator.next();
			if(iterator.hasNext()){
				if(temp.getValue().name == null && temp.getValue().value == null){
					iterator.remove();
				}
			}
		}

		newDoc.parameters = new DocInfo[details.size()-1];
		for(int i = 0; i<newDoc.parameters.length; i++){
			newDoc.parameters[i] = details.get(i).getValue();
		}

		newDoc.source = docushareHandle;
		newDoc.text = note.getValue();

		return newDoc;
	}

	@Override
	public void setInfo(Document info) {

		name.setValue(info.name);
		docType.setValue(info.docTypeId);
		isFile(info.hasFile);
		note.setValue(info.text);
		filename.setValue(info.fileName);
		mimeImg.setResource(getMimeImage(info.mimeType));
		for(int i =0; i<info.parameters.length; i++){
			details.add(new DocumentDetailEntry(info.parameters[i]));
		}
		details.add(new DocumentDetailEntry(null));

		fileStorageId = info.fileStorageId;
		hasFile = info.hasFile;
		mimeType = info.mimeType;
		docushareHandle = info.source;

	}

	public void isFile(boolean b) {

		hasFile = b;
		fileButton.setVisible(!b);
		docuShareFileButton.setVisible(!b);
		removeFile.setVisible(b);
		filename.setVisible(b);
		note.setReadOnly(b);
		mimeImageFileName.setVisible(b);

		if(b){
			notePanel.setVisible(false);
			filename.setWidth("300px");
			filename.setFieldWidth("300px");
		}else{
			if(note.getValue() != null){
				fileButton.setEnabled(false);
				docuShareFileButton.setEnabled(false);
			}
			else{
				fileButton.setEnabled(true);
				docuShareFileButton.setEnabled(true);
			}
			notePanel.setVisible(true);
			filename.setWidth("100px");
			filename.setFieldWidth("100px");
		}

	}


	public ImageResource getMimeImage(String mimeType) {

		Resources resources = GWT.create(Resources.class);
		ImageResource mimeImage;

		if(mimeType == null){
			return resources.fileIcon();
		}

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
			mimeImage = resources.xlsIcon();
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


	@Override 
	public void clearInfo() {
		details.clear();
		super.clearInfo();
	}

	@Override
	public void setReadOnly(boolean readOnly) {

		if(initialized){
			note.setReadOnly(readOnly);
			removeFile.setEnabled(!readOnly);
			docuShareFileButton.setEnabled(!readOnly);
			fileButton.setEnabled(!readOnly);

			for(int i = 0; i < details.size()-1; i++){
				((DocumentDetailEntry) details.get(i)).setEditable(!readOnly);
			}
			details.get(details.size()-1).setVisible(!readOnly);
		}

		super.setReadOnly(readOnly);
	}

	protected abstract void onDownloadFile();

	protected void onPressedRemoveFile(){

		isFile(false);
		fileStorageId = null;
		filename.setValue("");
		mimeType = "";
		docushareHandle = null;

	}

	protected abstract void onSubmitComplete(String results);

	protected abstract void onDocushareItemChanged(DocuShareItem value);

	protected void onRemoveListEntry(DocumentDetailEntry documentDetailEntry) {

		details.remove(documentDetailEntry);
	}


	public String getFileStorageId() {
		return fileStorageId;
	}


	public void setFilename(String string) {
		filename.setValue(string);
	}


	public void setFileStorageId(String string) {
		fileStorageId = string;
	}


	public FileUploadPopup getUploadPopup() {
		return uploadDialog;
	}


	public void setMimeType(String mimeType2) {
		mimeType = mimeType2;

	}


	public DocuShareHandle getDocuShareHandle() {
		return docushareHandle;
	}

}
