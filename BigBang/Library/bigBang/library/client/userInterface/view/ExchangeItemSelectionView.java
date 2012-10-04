package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.IncomingMessage.AttachmentUpgrade;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedStateChangedEvent;
import bigBang.library.client.event.CheckedStateChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.ExchangeItemSelectionToolbar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.form.ExchangeItemForm;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter;
import bigBang.library.client.userInterface.presenter.ExchangeItemSelectionViewPresenter.Action;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExchangeItemSelectionView extends View implements ExchangeItemSelectionViewPresenter.Display{

	FilterableList<ExchangeItemStub> emails;
	FilterableList<AttachmentStub> attachments;
	ExchangeItemForm centerForm;
	private ActionInvokedEventHandler<Action> actionHandler;

	private ExchangeItemSelectionToolbar toolbar;

	public static class AttachmentEntry extends ListEntry<AttachmentStub>{

		protected TextBoxFormField docName;
		protected ExpandableListBoxFormField docType;
		protected Image mimeImg;
		protected Label filename;
		protected boolean initialized = false;

		public HasValue<String> getDocName(){
			return docName;
		}

		public HasValue<String> getDocType(){
			return docType;
		}

		public AttachmentEntry(AttachmentStub value) {
			super(value);
			setInfo(value);
			setHeight("90px");
		}

		public void setInfo(AttachmentStub item){
			if(!initialized){
				docName = new TextBoxFormField();
				docName.setFieldWidth("170px");
				docName.setReadOnlyInternal(true);
				docType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.DOCUMENT_TYPE, "");
				docType.showLabel(false);
				docType.setReadOnlyInternal(true);
				mimeImg = new Image();
				filename = getFormatedLabel();
				filename.getElement().getStyle().setFontSize(11, Unit.PX);



				VerticalPanel wrapper = new VerticalPanel();
				this.setWidget(wrapper);

				HorizontalPanel bottom = new HorizontalPanel();
				bottom.add(mimeImg);
				bottom.add(filename);
				wrapper.add(bottom);
				wrapper.add(docName);
				wrapper.add(docType);

				docName.getNativeField().addKeyDownHandler(new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent event) {
						event.stopPropagation();
					}
				});




				addCheckedStateChangedEventHandler(new CheckedStateChangedEventHandler() {

					@Override
					public void onCheckedStateChanged(CheckedStateChangedEvent event) {
						docName.setReadOnlyInternal(!event.getChecked());
						docType.setReadOnlyInternal(!event.getChecked());
						if(!event.getChecked()){
							docName.clear();
							docType.clear();
						}
					}
				});


			}

			mimeImg.setResource(getMimeImage(item.mimeType));
			mimeImg.setSize("18px", "18px");
			filename.setText(item.fileName + " (" + item.size + "kb)");

			setMetaData(new String[]{item.fileName});

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

			mimeImg.getElement().getStyle().setMarginLeft(5, Unit.PX);

			return mimeImage;
		}

	}

	public static class EmailEntry extends ListEntry<ExchangeItemStub>{

		protected Label from;
		protected Label timestamp;
		protected Image hasAttachments;
		protected Label subject;
		protected HTML bodyPreview;
		protected boolean initialized = false;

		public EmailEntry(ExchangeItemStub value) {
			super(value);
			setInfo(value);
			setHeight("70px");
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
				if(item.attachmentCount > 0){
					Resources resources = GWT.create(Resources.class);
					hasAttachments = new Image(resources.icon_attachment());
					top.add(hasAttachments);
				}
				top.setCellWidth(from, "200px");
				top.setCellWidth(timestamp, "150px");

				subject = getFormatedLabel();
				subject.getElement().getStyle().setFontSize(11, Unit.PX);

				bodyPreview = new HTML();
				bodyPreview.getElement().getStyle().setFontSize(10, Unit.PX);
				bodyPreview.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);

				VerticalPanel container = new VerticalPanel();
				this.setWidget(container);
				container.setSize("100%", "100%");


				container.add(top);
				container.add(subject);
				container.add(bodyPreview);
				container.setCellHeight(bodyPreview, "100%");

				initialized = true;
			}
			from.setText(item.from);
			timestamp.setText(item.timestamp);
			subject.setText(item.subject);
			bodyPreview.setWordWrap(true);
			bodyPreview.setHTML(item.bodyPreview);

			setMetaData(new String[]{item.from, item.subject, item.timestamp});
		}
	}


	public ExchangeItemSelectionView(){

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("1220px", "650px");
		//LEFT
		VerticalPanel leftWrapper = new VerticalPanel();
		leftWrapper.setSize("100%", "100%");
		ListHeader header = new ListHeader("Lista de E-mails");
		emails = new FilterableList<ExchangeItemStub>();
		emails.setHeaderWidget(header);
		emails.showFilterField(false);
		leftWrapper.add(emails);
		leftWrapper.setCellHeight(emails, "100%");
		wrapper.addWest(leftWrapper, 330);


		//RIGHT
		SplitLayoutPanel insideWrapper = new SplitLayoutPanel();
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.setSize("100%", "100%");
		ListHeader aHeader = new ListHeader("Lista de Anexos");
		attachments = new FilterableList<AttachmentStub>();
		attachments.setCheckable(true);
		attachments.setHeaderWidget(aHeader);
		attachments.showFilterField(false);
		attachments.setSelectableEntries(false);
		rightWrapper.add(attachments);
		rightWrapper.setCellHeight(attachments, "100%");
		insideWrapper.addEast(rightWrapper, 250);

		//CENTER
		VerticalPanel centerWrapper = new VerticalPanel();
		centerWrapper.setSize("100%", "100%");
		centerForm = new ExchangeItemForm();
		centerForm.setSize("100%", "100%");
		centerWrapper.add(centerForm);
		centerWrapper.setCellHeight(centerForm, "100%");
		centerForm.setReadOnly(true);
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
				fireAction(Action.CANCEL);
			}

			@Override
			public void onConfirmRequest() {
				fireAction(Action.CONFIRM);
			}

		};

		insideVerticalWrapper.add(toolbar);
		insideVerticalWrapper.setSize("100%", "100%");
		insideVerticalWrapper.setCellWidth(toolbar, "100%");
		insideVerticalWrapper.add(insideWrapper);
		insideVerticalWrapper.setCellHeight(insideWrapper, "100%");

		wrapper.add(insideVerticalWrapper);
	}


	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;

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

		emails.clear();
		attachments.clear();
		centerForm.clearInfo();

	}

	@Override
	public HasValueSelectables<ExchangeItemStub> getEmailList() {
		return emails;
	}

	@Override
	public HasValue<ExchangeItem> getForm() {
		return centerForm;
	}

	@Override
	public HasValueSelectables<AttachmentStub> getAttachmentList() {
		return attachments;
	}

	@Override
	public void setAttachments(AttachmentStub[] attachments) {
		this.attachments.clear();
		for(int i = 0; i<attachments.length; i++){
			this.attachments.add(new AttachmentEntry(attachments[i]));
		}
	}


	@Override
	public AttachmentUpgrade[] getChecked() {


		int ammount = attachments.getChecked().size();
		AttachmentUpgrade[] attachs = new AttachmentUpgrade[ammount];
		int counter = 0;

		AttachmentUpgrade temp;

		for(int i = 0; i<attachments.size(); i++){
			if(attachments.get(i).isChecked()){
				temp = new AttachmentUpgrade();
				temp.docTypeId = ((AttachmentEntry)attachments.get(i)).getDocType().getValue();
				temp.name =((AttachmentEntry)attachments.get(i)).getDocName().getValue();
				temp.attachmentId = ((AttachmentEntry)attachments.get(i)).getValue().id;
				attachs[counter] = temp;
				counter++;
			}
		}

		return attachs;
	}


}
