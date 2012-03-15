package bigBang.library.client.userInterface.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.ExchangeItemSelectionToolbar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.TextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

public class ExchangeItemSelectionView extends View implements ExchangeItemSelectionViewPresenter.Display{

	FilterableList<ExchangeItemStub> emails;
	FilterableList<AttachmentStub> attachments;
	
	protected TextBoxFormField chosenFrom;
	protected DatePickerFormField chosenTimestamp;
	protected TextBoxFormField chosenSubject;
	protected TextAreaFormField body;
	private ExchangeItemSelectionToolbar toolbar;
	
	public static class AttachmentEntry extends ListEntry<AttachmentStub>{
		
		protected TextBoxFormField docName;
		protected ExpandableListBoxFormField docType;
		protected Image mimeImg;
		protected Label filename;
		protected Label size;
		protected boolean initialized = false;
		
		public AttachmentEntry(AttachmentStub value) {
			super(value);
			setInfo(value);
			setHeight("65px");
		}

		public void setInfo(AttachmentStub item){
			if(!initialized){
				docName = new TextBoxFormField("Nome do documento");
				docType = new ExpandableListBoxFormField("Tipo de documento");
				mimeImg = new Image();
				filename = getFormatedLabel();
				size = getFormatedLabel();
				
				VerticalPanel wrapper = new VerticalPanel();
				
				HorizontalPanel top = new HorizontalPanel();
				top.add(docName);
				top.add(docType);
				
				HorizontalPanel bottom = new HorizontalPanel();
				bottom.add(mimeImg);
				bottom.add(filename);
				bottom.add(size);
				
				wrapper.add(top);
				wrapper.add(bottom);
			}
			
			mimeImg.setResource(getMimeImage(item.mimeType));
			filename.setText(item.fileName);
			size.setText(item.size+ " kb");
			
			
		}
		
		public ImageResource getMimeImage(String mimeType) {

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
			
			mimeImg.getElement().getStyle().setMarginLeft(5, Unit.PX);
			
			return mimeImage;
		}

	}
	
	public static class EmailEntry extends ListEntry<ExchangeItemStub>{

		protected Label from;
		protected Label timestamp;
		protected Label subject;
		protected Label bodyPreview;
		protected boolean initialized = false;
		
		public EmailEntry(ExchangeItemStub value) {
			super(value);
			setInfo(value);
			setHeight("65px");
		}
		
		public void setInfo(ExchangeItemStub item){
			if(!initialized){
				from = getFormatedLabel();
				from.setWordWrap(false);
				timestamp = getFormatedLabel();
				timestamp.setWordWrap(false);
				HorizontalPanel top = new HorizontalPanel();
				top.add(from);
				top.add(timestamp);
				top.setCellWidth(from, "80%");
				top.setCellWidth(timestamp, "20%");
				
				subject = getFormatedLabel();
				subject.getElement().getStyle().setFontSize(11, Unit.PX);
				
				bodyPreview = getFormatedLabel();
				bodyPreview.getElement().getStyle().setFontSize(10, Unit.PX);
				bodyPreview.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
				
				VerticalPanel container = new VerticalPanel();
				container.setSize("100%", "100%");
				
				container.add(top);
				container.add(subject);
				container.add(bodyPreview);
				
				initialized = true;
			}
			from.setText("De: "+item.from);
			timestamp.setText(item.timestamp);
			subject.setText(item.subject);
			bodyPreview.setText(item.bodyPreview);
			
		}
	}
	
	
	public ExchangeItemSelectionView(){

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("900px", "650px");
		
		//LEFT
		VerticalPanel leftWrapper = new VerticalPanel();
		leftWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader("Lista de E-mails");
		emails = new FilterableList<ExchangeItemStub>();
		emails.setHeaderWidget(header);
		emails.showFilterField(false);
		leftWrapper.add(emails);
		leftWrapper.setCellHeight(emails, "100%");
		wrapper.addWest(leftWrapper, 200);
		
		
		//RIGHT
		SplitLayoutPanel insideWrapper = new SplitLayoutPanel();
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.setSize("100%", "100%");
		ListHeader aHeader = new ListHeader("Lista de Anexos");
		attachments = new FilterableList<AttachmentStub>();
		attachments.setCheckable(true);
		attachments.setHeaderWidget(aHeader);
		attachments.showFilterField(false);
		rightWrapper.add(attachments);
		rightWrapper.setCellHeight(attachments, "100%");
		insideWrapper.addEast(rightWrapper, 200);
		
		//CENTER
		VerticalPanel centerWrapper = new VerticalPanel();
		chosenFrom = new TextBoxFormField("De");
		chosenFrom.setReadOnly(true);
		chosenSubject = new TextBoxFormField("Assunto");
		chosenSubject.setReadOnly(true);
		chosenTimestamp = new DatePickerFormField("Data");
		chosenTimestamp.setReadOnly(true);
		body = new TextAreaFormField("Corpo da Mensagem");
		body.setReadOnly(true);
		centerWrapper.add(chosenFrom);
		centerWrapper.add(chosenTimestamp);
		centerWrapper.add(chosenSubject);
		centerWrapper.add(body);
		insideWrapper.add(centerWrapper);
		insideWrapper.setSize("100%", "100%");
		VerticalPanel insideVerticalWrapper = new VerticalPanel();
		insideVerticalWrapper.setSize("100%", "100%");

		ListHeader centerHeader = new ListHeader("E-mail");
		
		insideVerticalWrapper.add(centerHeader);
		//TOOLBAR
		toolbar = new ExchangeItemSelectionToolbar(){

			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onConfirmRequest() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		insideVerticalWrapper.add(toolbar);
		insideVerticalWrapper.setSize("100%", "100%");
		insideVerticalWrapper.setCellWidth(toolbar, "100%");
		insideVerticalWrapper.add(insideWrapper);
		insideVerticalWrapper.setCellHeight(insideWrapper, "100%");
		
		wrapper.add(insideVerticalWrapper);
	}
	
	@Override
	public void addEmailEntry(ExchangeItemStub email){
		EmailEntry entry = new EmailEntry(email);
		emails.add(entry);
	}
	
	@Override
	protected void initializeView() {
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
