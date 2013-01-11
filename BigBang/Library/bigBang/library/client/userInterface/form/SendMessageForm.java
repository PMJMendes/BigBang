package bigBang.library.client.userInterface.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Kind;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.definitions.shared.TypifiedText;
import bigBang.definitions.shared.User;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBroker;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.TypifiedTextSelector;
import bigBang.library.client.userInterface.autocomplete.AutoCompleteTextListFormField;
import bigBang.library.client.userInterface.presenter.ContactManagementViewPresenter;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.view.ContactManagementView;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.interfaces.DocumentServiceAsync;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendMessageForm extends FormView<Conversation> implements DocumentsBrokerClient{

	protected boolean firstTime = true;
	protected ExpandableListBoxFormField requestType;
	protected RadioButtonFormField expectsResponse;
	protected ListBoxFormField contactsFrom;
	protected TypifiedTextSelector text;
	protected ListBoxFormField to;
	protected NumericTextBoxFormField replyLimit;
	protected AutoCompleteTextListFormField forwardReply;
	protected TextBoxFormField internalCCAddresses;
	protected TextBoxFormField externalCCAddresses;
	private VerticalPanel noteWrapper;
	protected RadioButtonFormField emailOrNote;
	protected RichTextAreaFormField note;
	private Button addContactButton;
	private Button addDocumentButton;
	protected bigBang.library.client.userInterface.List<Document> attachments;
	protected Map<String, String> ownerTypes;
	private HorizontalPanel emailAndAttachmentsWrapper;
	protected ContactManagementViewPresenter contactManagementPanel;
	private PopupPanel popup;
	protected String ownerId;
	private DocumentViewPresenter documentViewPresenter;
	private DocumentsBroker documentBroker;
	protected String ownerTypeId;
	protected String documentOwnerId;
	private String contactChosen;
	private TextBoxFormField subject;
	private ListBoxFormField documentsFrom;

	public SendMessageForm(){

		addSection("Detalhes do Processo de Mensagem");
		subject = new TextBoxFormField("Tópico");
		contactsFrom = new ListBoxFormField("Contactos de:");
		contactsFrom.setFieldWidth("400px");
		to = new ListBoxFormField("Para:");

		documentsFrom = new ListBoxFormField("Documentos de:");
		documentsFrom.setFieldWidth("400px");

		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Mensagem");
		text = new TypifiedTextSelector();	
		to.setFieldWidth("400px");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("70px");

		forwardReply = new AutoCompleteTextListFormField("Utilizadores a envolver no processo");
		internalCCAddresses = new TextBoxFormField("CC Endereços separados por ','");
		externalCCAddresses = new TextBoxFormField("BCC Endereços separados por ','");

		expectsResponse = new RadioButtonFormField("Espera resposta");
		expectsResponse.addOption("YES", "Sim");
		expectsResponse.addOption("NO", "Não");

		expectsResponse.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue().equals("YES")){
					replyLimit.clear();
					replyLimit.setEditable(true);
					replyLimit.setReadOnly(false);
				}else{
					replyLimit.clear();
					replyLimit.setEditable(false);
					replyLimit.setReadOnly(true);
				}
			}
		});

		addContactButton = new Button("Gerir contactos");

		addFormField(requestType, false);
		addFormField(subject, false);
		addFormField(forwardReply, false);
		addFormField(expectsResponse, true);
		addFormField(replyLimit, true);

		addSection("Detalhes da Mensagem");

		addFormField(contactsFrom, true);
		addLineBreak();

		HorizontalPanel toAndButtonWrapper = new HorizontalPanel();
		toAndButtonWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toAndButtonWrapper.add(to);
		toAndButtonWrapper.add(addContactButton);

		addWidget(toAndButtonWrapper);

		VerticalPanel emailWrapper = new VerticalPanel();
		noteWrapper = new VerticalPanel();

		emailOrNote = new RadioButtonFormField("E-mail ou Nota");
		emailOrNote.addOption(Kind.EMAIL.toString(), "E-mail");
		emailOrNote.addOption(Kind.NOTE.toString(), "Nota");

		note = new RichTextAreaFormField("Notas");

		addFormField(emailOrNote);

		registerFormField(internalCCAddresses);
		registerFormField(externalCCAddresses);
		registerFormField(text);
		registerFormField(to);
		registerFormField(note);

		emailAndAttachmentsWrapper = new HorizontalPanel();

		emailWrapper.add(internalCCAddresses);
		emailWrapper.add(externalCCAddresses);
		text.setWidth("670px");
		emailWrapper.add(text);

		attachments = new bigBang.library.client.userInterface.List<Document>();
		ListHeader firstHeader = new ListHeader("Anexos");
		ListHeader header = new ListHeader();
		addDocumentButton = new Button("Criar Documento");
		addDocumentButton.getElement().getStyle().setMarginRight(14, Unit.PX);
		addDocumentButton.setEnabled(false);
		header.setRightWidget(addDocumentButton);
		header.setLeftWidget(documentsFrom);
		attachments.setHeaderWidget(header);
		VerticalPanel attachmentsWrapper = new VerticalPanel();
		attachmentsWrapper.add(firstHeader);
		attachmentsWrapper.add(attachments);
		attachmentsWrapper.setCellHeight(attachments, "510px");
		emailAndAttachmentsWrapper.add(emailWrapper);
		emailAndAttachmentsWrapper.add(attachmentsWrapper);

		noteWrapper.setVisible(false);
		noteWrapper.add(note);

		addWidget(emailAndAttachmentsWrapper);
		addWidget(noteWrapper);

		requestType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(!event.getValue().isEmpty()){
					text.setReadOnly(false);
				}
				else{
					text.setReadOnly(true);
				}
				text.setTypifiedTexts(event.getValue());
			}
		});

		((UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER)).getUsers(new ResponseHandler<User[]>() {

			@Override
			public void onResponse(User[] response) {
				List<String> suggestions = new ArrayList<String>();
				for(int i = 0; i < response.length; i++) {
					suggestions.add(response[i].name);
				}
				forwardReply.setSuggestions(suggestions);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});

		text.setReadOnly(true);

		expectsResponse.setValue("YES");

		ownerTypes = new HashMap<String, String>();

		contactManagementPanel = new ContactManagementViewPresenter(new ContactManagementView());
		popup = new PopupPanel();

		addContactButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HasParameters params = new HasParameters();
				params.setParameter("ownerid", contactsFrom.getValue());
				params.setParameter("ownertypeid", getOwnerType(contactsFrom.getValue()));
				popup = new PopupPanel();
				popup.addAttachHandler(new Handler() {

					@Override
					public void onAttachOrDetach(AttachEvent event) {
						if (!event.isAttached()){
							contactChoiceChanged(contactsFrom.getValue());
						}
					}
				});
				contactManagementPanel.setParameters(params);
				contactManagementPanel.go(popup);
				popup.center();				
			}
		});

		addDocumentButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HasParameters params = new HasParameters();
				params.setParameter("ownerid", documentOwnerId);
				params.setParameter("ownertypeid", getOwnerType(documentOwnerId));
				params.setParameter("documentid", "new");
				popup = new PopupPanel();
				
				documentViewPresenter.setParameters(params);
				documentViewPresenter.go(popup);
				popup.center();				
			}
		});

		documentViewPresenter = new DocumentViewPresenter(new DocumentView()){

			@Override
			protected void onCreateDocument() {
				popup.hidePopup();
				documentOwnerChanged(documentOwnerId);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Documento gravado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}
		};

		contactsFrom.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				contactChoiceChanged(event.getValue());
			}
		});

		documentsFrom.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				documentOwnerChanged(event.getValue());
			}
		});

		documentBroker = (DocumentsBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.DOCUMENT);


		emailOrNote.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() == null){
					emailOrNote.setValue(Kind.EMAIL.toString());
				}
				else{
					boolean isEmail = Kind.EMAIL.toString().equalsIgnoreCase(event.getValue());
					emailAndAttachmentsWrapper.setVisible(isEmail);
					noteWrapper.setVisible(!isEmail);
				}
			}
		});		

		setValidator(new SendMessageFormValidator(this));
	}

	protected void documentOwnerChanged(String value) {
		if(value == null || value.isEmpty() || BigBangConstants.TypifiedListValues.MEDIATOR_IDS.DIRECT.equalsIgnoreCase(value)){
			addDocumentButton.setEnabled(false);
			clearDocuments();
			return;
		}
		addDocumentButton.setEnabled(true);
		documentOwnerId = value;
		clearDocuments();
		DocumentServiceAsync documentsService = DocumentService.Util.getInstance();

		documentsService.getDocuments(value, new AsyncCallback<Document[]>() {

			@Override
			public void onSuccess(Document[] result) {
				for(Document doc : result){						
					addDocument(doc);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				return;
			}
		});
	}

	private void clearDocuments() {
		ArrayList<ListEntry<Document>> checkeds = new ArrayList<ListEntry<Document>>();

		for(ListEntry<Document>  doc: attachments){
			if(doc.isChecked()){
				checkeds.add(doc);
			}
		}
		attachments.clear();

		for(ListEntry<Document>  checked: checkeds){
			attachments.add(checked);
		}

	}

	private void contactChoiceChanged(String id) {
		if(id == null || id.isEmpty() || BigBangConstants.TypifiedListValues.MEDIATOR_IDS.DIRECT.equalsIgnoreCase(id)){
			clearEmails();
			addContactButton.setEnabled(false);
			return;
		}
		ContactsServiceAsync contactsService = ContactsService.Util.getInstance();

		contactsService.getFlatEmails(id, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onResponseSuccess(Contact[] result) {
				setAvailableContacts(result);
				to.setValue(contactChosen);
				contactChosen = null;
				addContactButton.setEnabled(true);
			}			

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os e-mails"), TYPE.ERROR_TRAY_NOTIFICATION));
				super.onResponseFailure(caught);
			}

		});
	}


	@Override
	public Conversation getInfo() {

		//CONVERSATION
		if(value == null){
			value = new Conversation();
		}

		Conversation conversation = value;
		conversation.requestTypeId = requestType.getValue();
		conversation.subject = subject.getValue();
		TypifiedText requestText = text.getValue();

		try{
			conversation.replylimit = replyLimit.getValue().intValue();
		}catch(Exception e){
			conversation.replylimit = null;
		}

		//MESSAGE
		Message msg = new Message();
		msg.conversationId = conversation.id;
		msg.kind = Kind.EMAIL.toString().equalsIgnoreCase(emailOrNote.getValue()) ? Kind.EMAIL : Kind.NOTE;
		msg.subject = requestText.subject;

		if(Kind.EMAIL.equals(msg.kind)){
			List<Message.MsgAddress> addresses = new ArrayList<Message.MsgAddress>();
			List<String> outgoingAttachment = new ArrayList<String>();
			msg.text = requestText.text;
			addresses = getAddresses();
			outgoingAttachment = getAttachments();


			msg.addresses = new Message.MsgAddress[addresses.size()];

			for(int i = 0; i<msg.addresses.length; i++){
				msg.addresses[i] = addresses.get(i);
			}

			msg.outgoingAttachmentIds = new String[outgoingAttachment.size()];

			for(int i = 0; i<msg.outgoingAttachmentIds.length; i++){
				msg.outgoingAttachmentIds[i] = outgoingAttachment.get(i);
			}
		}else{
			msg.text = note.getValue();
		}


		conversation.messages = new Message[1];
		conversation.messages[0] = msg;
		return conversation;
	}

	private List<String> getAttachments() {

		List<String> attachs = new ArrayList<String>();

		for(ListEntry<Document> doc : attachments){
			if(doc.isChecked()){
				attachs.add(doc.getValue().id);
			}
		}

		return attachs;
	}

	private List<MsgAddress> getAddresses() {

		List<MsgAddress> addresses = new ArrayList<MsgAddress>();

		MsgAddress to = new MsgAddress();
		to.contactInfoId = this.to.getValue();
		to.usage = MsgAddress.Usage.TO;

		addresses.add(to);

		MsgAddress me = new MsgAddress();
		me.userId = Session.getUserId();
		me.usage = MsgAddress.Usage.REPLYTO;

		addresses.add(me);

		for(String userId : forwardReply.getValue()){
			MsgAddress newAddress = new MsgAddress();
			newAddress.display = userId;
			newAddress.usage = MsgAddress.Usage.REPLYTO;
			addresses.add(newAddress);
		}

		if(internalCCAddresses.getValue() != null){
			String[] ccs = internalCCAddresses.getValue().split(",");

			for(int i = 0; i<ccs.length; i++){
				MsgAddress newAddress = new MsgAddress();
				newAddress.usage =  MsgAddress.Usage.CC;
				newAddress.address = ccs[i].trim();
				addresses.add(newAddress);
			}
		}
		if(externalCCAddresses.getValue() != null){
			String[] bccs = externalCCAddresses.getValue().split(",");

			for(int i = 0; i<bccs.length; i++){
				MsgAddress newAddress = new MsgAddress();
				newAddress.usage =  MsgAddress.Usage.BCC;
				newAddress.address = bccs[i].trim();
				addresses.add(newAddress);
			}
		}

		return addresses;
	}

	@Override
	public void setInfo(Conversation info) {

		replyLimit.setValue(info.replylimit != null ? info.replylimit.doubleValue() : null);
		requestType.setValue(info.requestTypeId);
		subject.setValue(info.subject);
		TypifiedText textValue = new TypifiedText();
		textValue.subject = info.subject;

		if(info.messages != null && info.messages.length > 0 && info.messages[0] != null){
			Message msg = info.messages[0];

			emailOrNote.setValue(msg.kind.toString());

			if(msg.kind.equals(Kind.EMAIL)){
				text.setValue(textValue);
			}else{
				note.setValue(msg.text);
			}

			List<String> forwardReplies = new ArrayList<String>();

			for(int i = 0; i<msg.addresses.length ; i++){
				switch(msg.addresses[i].usage){
				case BCC:
					externalCCAddresses.setValue(( externalCCAddresses.getValue() == null ?
							"" : externalCCAddresses.getValue() +", " ) + msg.addresses[i].address);
					break;
				case CC:
					internalCCAddresses.setValue(( internalCCAddresses.getValue() == null ? "" : internalCCAddresses.getValue() + ", ") + msg.addresses[i].address);
					break;
				case REPLYTO:
					forwardReplies.add(msg.addresses[i].display);
					break;
				case TO:
					contactsFrom.setValue(msg.addresses[i].ownerId);
					contactChosen = msg.addresses[i].contactInfoId; 
					break;
				}
			}

			forwardReply.setValue(forwardReplies);
		}
		else{
			emailOrNote.setValue(Kind.EMAIL.toString());
		}

		if(requestType.getValue() != null){
			requestType.setReadOnly(true);
		}
		if(subject.getValue() != null){
			subject.setReadOnly(true);
		}

	}

	public void setAvailableContacts(Contact[] contacts) {
		this.to.clearValues();
		if(contacts != null){
			for(int i = 0; i < contacts.length; i++){
				Contact contact = contacts[i];
				ContactInfo info = contact.info[0];
				this.to.addItem(contact.name + " <" + info.value + ">", info.id);
			}
		}
	}



	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if(text != null){
			if(requestType != null && requestType.getValue()!= null && !requestType.getValue().isEmpty()){
				text.setReadOnly(readOnly);
			}else{
				text.setReadOnly(true);
			}
		}
	}


	public HasValue<String> getContactType() {
		return contactsFrom;
	}

	public HasValue<String> getTo(){
		return to;
	}

	@Override
	public void clearInfo() {
		super.clearInfo();
		expectsResponse.setValue("YES");
		emailOrNote.setValue(Kind.EMAIL.toString());
	}

	public ListBoxFormField getContactList() {
		return contactsFrom;
	}


	public void clearEmails() {
		to.clearValues();
	}


	public void lockCreateNewButton(boolean lock) {
		addContactButton.setEnabled(!lock);
	}

	public void addDocuments(Collection<Document> documents) {

		attachments.clear();

		if(documents != null){
			for(Document doc : documents){
				DocumentsList.Entry entry = new DocumentsList.Entry(doc);
				attachments.add(entry);
				attachments.setCheckable(true);
				attachments.setSelectableEntries(false);
				entry.setSelectable(false);
				entry.setCheckable(true);
			}
		}

	}

	public void addDocument(Document doc){
		for(ListEntry<Document>  document: attachments){
			if(document.getValue().id.equalsIgnoreCase(doc.id)){
				return;
			}
		}
		
		DocumentsList.Entry entry = new DocumentsList.Entry(doc);
		attachments.add(entry);
		attachments.setCheckable(true);
		attachments.setSelectableEntries(false);
		entry.setSelectable(false);
		entry.setCheckable(true);
	}

	public void addItemContactList(String name, String id, String ownerType) {
		contactsFrom.addItem(name, id);
		documentsFrom.addItem(name, id);
		ownerTypes.put(id, ownerType);
	}

	public String getOwnerType(String id){
		return ownerTypes.get(id);
	}

	public void setTypeAndOwnerId(String ownerTypeId2, String ownerId2) {
		this.ownerId = ownerId2;
		this.ownerTypeId = ownerTypeId2;
		documentBroker.registerClient(this);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}

	@Override
	public int getDocumentsDataVersionNumber(String ownerId) {
		return 0;
	}

	@Override
	public void setDocumentsDataVersionNumber(String ownerId, int number) {
		return;
	}

	@Override
	public void setDocuments(String ownerId, List<Document> documents) {
		addDocuments(documents);
	}

	@Override
	public void removeDocument(String ownerId, Document document) {
	}

	@Override
	public void addDocument(String ownerId, Document document) {
	}

	@Override
	public void updateDocument(String ownerId, Document document) {
	}
}
