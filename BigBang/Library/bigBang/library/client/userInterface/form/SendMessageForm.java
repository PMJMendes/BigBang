package bigBang.library.client.userInterface.form;


import java.util.ArrayList;
import java.util.Arrays;
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
import bigBang.definitions.shared.Message.Attachment;
import bigBang.definitions.shared.Message.Kind;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.definitions.shared.User;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.DocumentsList;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.autocomplete.AutoCompleteTextListFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;
import bigBang.library.interfaces.DocumentService;
import bigBang.library.interfaces.DocumentServiceAsync;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendMessageForm extends FormView<Conversation> implements DocumentsBrokerClient {
	
	// Constants
	private static int TO_ADDRESS_ID = 1;
	private static int CC_ADDRESS_ID = 2;
	private static int BCC_ADDRESS_ID = 3;
	
	protected ExpandableListBoxFormField requestType;
	protected TextBoxFormField subject;
	protected AutoCompleteTextListFormField forwardReply;
	protected RadioButtonFormField expectsResponse;
	protected NumericTextBoxFormField replyLimit;
	protected HorizontalPanel toPanel;
	protected TextBoxFormField toAddresses;
	protected Button addToButton;
	protected HorizontalPanel ccPanel;
	protected TextBoxFormField ccAddresses;
	private Button addCcButton;
	protected HorizontalPanel bccPanel;
	protected TextBoxFormField bccAddresses;
	protected Button addBccButton;
	protected VerticalPanel contactsWrapper;
	protected HorizontalPanel contactsEntityPanel;
	protected ListBoxFormField existingContactsEntity;
	protected ListBoxFormField existingContact;
	protected FormField<String> otherEntityContacts;
	protected RadioButtonFormField emailOrNote;
	protected RichTextAreaFormField note;
	protected VerticalPanel noteWrapper;
	protected VerticalPanel emailWrapper;
	protected TextBoxFormField emailSubject;
	protected RichTextAreaFormField emailBody;
	protected VerticalPanel attachmentsWrapper;
	protected bigBang.library.client.userInterface.List<Document> existingAttachments;
	protected ListHeader existingAttsHeaderTitle;
	protected ListHeader existingAttsEntityPicker;
	protected ListBoxFormField documentsFrom;
	protected Map<String, String> ownerTypes;
	protected VerticalPanel leftPanel;
	protected VerticalPanel rightPanel;
	protected HorizontalPanel fullMailPanel;
	protected Button addDocumentButton;
	protected HorizontalPanel buttonsWrapper;
	protected VerticalPanel addedAttachmentsWrapper;
	protected ListHeader addedAttsHeaderTitle;
	protected bigBang.library.client.userInterface.List<Document> addedAttachments;
	protected Button removeDocumentButton;
	
	Conversation value;
	
	public SendMessageForm(){
		
		// Declares Fields
		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Mensagem");
		subject = new TextBoxFormField("Tópico");
		forwardReply = new AutoCompleteTextListFormField("Utilizadores a envolver no processo");
		expectsResponse = new RadioButtonFormField("Espera resposta");
		expectsResponse.addOption("YES", "Sim");
		expectsResponse.addOption("NO", "Não");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("70px");
		contactsWrapper = new VerticalPanel();
		toPanel = new HorizontalPanel();
		toAddresses = new TextBoxFormField("Destinatários do Email (separados por ';')");
		addToButton = new Button("Adicionar");
		ccPanel = new HorizontalPanel();
		ccAddresses = new TextBoxFormField("CC (separados por ';')");
		addCcButton = new Button("Adicionar");
		bccPanel = new HorizontalPanel();
		bccAddresses = new TextBoxFormField("BCC (separados por ';')");
		addBccButton = new Button("Adicionar");
		contactsEntityPanel = new HorizontalPanel();
		existingContactsEntity = new ListBoxFormField("Contacto associado a:");
		existingContactsEntity.setFieldWidth("400px");
		existingContact = new ListBoxFormField("Contacto Existente:");
		existingContact.setFieldWidth("400px");
		otherEntityContacts = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.OTHER_ENTITY, null);
		otherEntityContacts.setLabelText("Contactos de 'Outras Entidades'");
		emailOrNote = new RadioButtonFormField("E-mail ou Nota");
		emailOrNote.addOption(Kind.EMAIL.toString(), "E-mail");
		emailOrNote.addOption(Kind.NOTE.toString(), "Nota");
		emailOrNote.setValue(Kind.EMAIL.toString());
		noteWrapper = new VerticalPanel();
		note = new RichTextAreaFormField("Notas");
		emailWrapper = new VerticalPanel();
		emailSubject = new TextBoxFormField("Assunto");
		emailBody = new RichTextAreaFormField();
		attachmentsWrapper = new VerticalPanel();
		existingAttachments = new bigBang.library.client.userInterface.List<Document>();
		existingAttsHeaderTitle = new ListHeader("Documentos Associados a Entidades");
		existingAttsEntityPicker = new ListHeader();
		documentsFrom = new ListBoxFormField("Documentos de:");
		documentsFrom.setFieldWidth("400px");
		addedAttachmentsWrapper = new VerticalPanel();
		addedAttsHeaderTitle = new ListHeader("Anexos a Enviar");
		addedAttsHeaderTitle.setWidth("430px");
		addedAttachments = new bigBang.library.client.userInterface.List<Document>();
		leftPanel = new VerticalPanel();
		rightPanel = new VerticalPanel();
		fullMailPanel = new HorizontalPanel();
		addDocumentButton = new Button("Adicionar Anexo");
		buttonsWrapper = new HorizontalPanel();
		removeDocumentButton = new Button("Remover Anexo");
		
		// Defines Handlers
		existingContactsEntity.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				contactEntityChanged(event.getValue());
			}
		});
		
		existingContact.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				contactEmailChanged(event.getValue());
			}
		});
		
		otherEntityContacts.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				contactEntityChanged(event.getValue(), true);
			}
		});
		
		addToButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (existingContact.getValue() != null && existingContact.getValue().length()!=0) {
					addAddress(TO_ADDRESS_ID, existingContact.getValue());
				}
			}
		});
		
		addCcButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (existingContact.getValue() != null && existingContact.getValue().length()!=0) {
					addAddress(CC_ADDRESS_ID, existingContact.getValue());
				}
			}
		});
		
		addBccButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (existingContact.getValue() != null && existingContact.getValue().length()!=0) {
					addAddress(BCC_ADDRESS_ID, existingContact.getValue());
				}
			}
		});
		
		addDocumentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Collection<ValueSelectable<Document>> selectedAtt = existingAttachments.getSelected();
				if (selectedAtt.size()>0) {
					ValueSelectable<Document> docVal = (ValueSelectable) selectedAtt.toArray()[0];
					Document doc = (Document) docVal.getValue();
					DocumentsList.Entry entry = new DocumentsList.Entry(doc);
					addedAttachments.add(entry);
					
					removeDocument(null, doc);
				}
			}
		});
		
		removeDocumentButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Collection<ValueSelectable<Document>> selectedAtt = addedAttachments.getSelected();
				if (selectedAtt.size()>0) {
					ValueSelectable<Document> docVal = (ValueSelectable) selectedAtt.toArray()[0];
					Document doc = (Document) docVal.getValue();
					
					for (int i=0; i<addedAttachments.size(); i++) {
						if(addedAttachments.get(i).getValue().id.equals(doc.id)) {
							addedAttachments.remove(i);
							break;
						}
					}
				}
			}
		});
		
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
		
		documentsFrom.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				getDocsFromOwner(event.getValue());
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
		
		// "Draws" screen
		addSection("Detalhes do Processo de Mensagem");
		addFormField(requestType, false);
		addFormField(subject, false);
		addFormField(forwardReply, false);
		addFormField(expectsResponse, true);
		addFormField(replyLimit, true);

		addSection("Detalhes da Mensagem");
		toPanel.add(toAddresses);
		registerFormField(toAddresses);
		toPanel.add(addToButton);
		contactsWrapper.add(toPanel);
		ccPanel.add(ccAddresses);
		registerFormField(ccAddresses);
		ccPanel.add(addCcButton);
		contactsWrapper.add(ccPanel);
		bccPanel.add(bccAddresses);
		bccPanel.add(addBccButton);
		registerFormField(bccAddresses);
		contactsWrapper.add(bccPanel);
		contactsEntityPanel.add(existingContactsEntity);
		contactsEntityPanel.add(otherEntityContacts);
		contactsWrapper.add(contactsEntityPanel);
		contactsWrapper.add(existingContact);
		
		otherEntityContacts.setVisible(false);
		
		addToButton.setEnabled(false);
		addCcButton.setEnabled(false);
		addBccButton.setEnabled(false);

		leftPanel.add(emailOrNote);

		leftPanel.add(contactsWrapper);

		registerFormField(note);
		noteWrapper.add(note);
		leftPanel.add(noteWrapper);
		noteWrapper.setVisible(false);

		emailSubject.setWidth("100%");
		emailSubject.setFieldWidth("100%");
		registerFormField(emailSubject);
		emailWrapper.add(emailSubject);
		emailBody.setLabelWidth("0px");
		emailBody.setFieldWidth("100%");
		emailBody.getElement().getStyle().setBackgroundColor("#FFFFFF");
		emailWrapper.add(emailBody);
		leftPanel.add(emailWrapper);
		emailWrapper.getElement().getStyle().setPaddingTop(40, Unit.PX);
		// setPadding(40, Unit.PX);
		emailWrapper.setVisible(true);

		fullMailPanel.add(leftPanel);

		existingAttsEntityPicker.setLeftWidget(documentsFrom);
		existingAttachments.setHeaderWidget(existingAttsEntityPicker);
		attachmentsWrapper.add(existingAttsHeaderTitle);
		attachmentsWrapper.add(existingAttachments);
		attachmentsWrapper.setCellHeight(existingAttachments, "350px");
		rightPanel.add(attachmentsWrapper);
		buttonsWrapper.add(addDocumentButton);
		rightPanel.add(buttonsWrapper);
		
		addedAttachmentsWrapper.add(addedAttsHeaderTitle);
		addedAttachmentsWrapper.add(addedAttachments);
		addedAttachmentsWrapper.setCellHeight(addedAttachments, "195px");
		addedAttachmentsWrapper.getElement().getStyle().setPaddingTop(20, Unit.PX);
		rightPanel.add(addedAttachmentsWrapper);
		rightPanel.add(removeDocumentButton);

		rightPanel.getElement().getStyle().setPaddingLeft(40, Unit.PX);

		fullMailPanel.add(rightPanel);

		addWidget(fullMailPanel);

		ownerTypes = new HashMap<String, String>();

		addOtherEntitiesContacts();
		
		setValidator(new SendMessageFormValidator(this));
	}

	
	/**
	 * A "pre-method" to the other, which will probably disappear
	 */
	private void contactEntityChanged(String id) {
		contactEntityChanged(id, false);
	}

	/**
	 * The method called when type of entity whose contacts to use changes
	 */
	protected void contactEntityChanged(String id, boolean isOtherEntity) {

		if(id == null || id.isEmpty()) {
		//	clearEmails();
			otherEntityContacts.setVisible(false);
			return;
		} else if(id.equalsIgnoreCase(BigBangConstants.EntityIds.OTHER_ENTITY)) {
			otherEntityContacts.setVisible(true);
		//	clearEmails();
			return;
		}
		
		otherEntityContacts.setVisible(isOtherEntity);

		// Gets the emails associated with the entity
		ContactsServiceAsync contactsService = ContactsService.Util.getInstance();
		contactsService.getFlatEmails(id, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onResponseSuccess(Contact[] result) {
				setAvailableContacts(result);
			}			

			@Override
			public void onResponseFailure(Throwable caught) {
						EventBus.getInstance().fireEvent(
								new NewNotificationEvent(new Notification("",
										"Não foi possível obter os e-mails"),
										TYPE.ERROR_NOTIFICATION));
						super.onResponseFailure(caught);
			}

		});

	}
	
	/**
	 * Clears the otherEntityContacts' and existingContact's fields
	 */
	public void clearEmails() {
		existingContact.clearValues();
		otherEntityContacts.setValue(null);
	}
	
	/**
	 * Sets the possible (selectable) contacts associated with an entity
	 */
	public void setAvailableContacts(Contact[] contacts) {
		this.existingContact.clearValues();
		if(contacts != null){
			for(int i = 0; i < contacts.length; i++){
				Contact contact = contacts[i];
				ContactInfo info = contact.info[0];
				this.existingContact.addItem(info.value, info.value);
			}
		}
	}
	
	/**
	 * The method called when an email address is selected
	 */
	private void contactEmailChanged(String id) {
		if(id == null || id.isEmpty() || id.equals("-")) {
			addToButton.setEnabled(false);
			addCcButton.setEnabled(false);
			addBccButton.setEnabled(false);
		} else {
			addToButton.setEnabled(true);
			addCcButton.setEnabled(true);
			addBccButton.setEnabled(true);
		}
	}
	
	/**
	 * This method adds the address to the corresponding field
	 */
	private void addAddress(int typeOfAddId, String mail) {
		
		String addedAddresses = "";
		
		if (typeOfAddId == TO_ADDRESS_ID) {
			addedAddresses = toAddresses.getValue();
		} else if (typeOfAddId == CC_ADDRESS_ID) {
			addedAddresses = ccAddresses.getValue();
		} else if (typeOfAddId == BCC_ADDRESS_ID) {
			addedAddresses = bccAddresses.getValue();
		}
		
		if (addedAddresses!=null) {
			addedAddresses = addedAddresses.trim();
			
			if (addedAddresses.length()!=0) {
				if (addedAddresses.substring(addedAddresses.length() - 1) != ";") {
					addedAddresses = addedAddresses + "; ";
				}
			}
			
			addedAddresses = addedAddresses + mail;
		} else {
			addedAddresses = mail;
		}
		
		if (typeOfAddId == TO_ADDRESS_ID) {
			toAddresses.setValue(addedAddresses);
		} else if (typeOfAddId == CC_ADDRESS_ID) {
			ccAddresses.setValue(addedAddresses);
		} else if (typeOfAddId == BCC_ADDRESS_ID) {
			bccAddresses.setValue(addedAddresses);
		}
	}
	
	/**
	 * Adds a value to the selectable entities' contacts
	 */
	private void addOtherEntitiesContacts() {
		existingContactsEntity.addItem("Outras Entidades", BigBangConstants.EntityIds.OTHER_ENTITY);
	}
	
	/**
	 * This method gets the documents from a given owner, which may be attached to the email
	 */
	protected void getDocsFromOwner(String value) {

		if(value == null || value.isEmpty() || BigBangConstants.TypifiedListValues.MEDIATOR_IDS.DIRECT.equalsIgnoreCase(value)){
			addDocumentButton.setEnabled(false);
			clearDocuments();
			return;
		}
		addDocumentButton.setEnabled(true);
		clearDocuments();
		DocumentServiceAsync documentsService = DocumentService.Util.getInstance();

		documentsService.getDocuments(value, new AsyncCallback<Document[]>() {

			@Override
			public void onSuccess(Document[] result) {
				addDocuments(Arrays.asList(result));
			}

			@Override
			public void onFailure(Throwable caught) {
				return;
			}
		});
	}
	
	private void clearDocuments() {
		// TODO Auto-generated method stub
		
	}


	public HasValue<String> getContactType() {
		return existingContactsEntity;
	}
	
	public void addItemContactList(String name, String id, String ownerType) {
		existingContactsEntity.addItem(name, id);
		documentsFrom.addItem(name, id);
		ownerTypes.put(id, ownerType);
	}
	
	public ListBoxFormField getContactList() {
		return existingContactsEntity;
	}
	
	public void setTypeAndOwnerId(String ownerTypeId2, String ownerId2) {
		if(value != null){
			value.parentDataObjectId = ownerId2;
			value.parentDataTypeId = ownerTypeId2;
		}
	}
	
	public HasValue<String> getTo(){
		return existingContact;
	}
	
	public void addDocuments(Collection<Document> documents) {
		for (Document doc : documents) {
			addDocument(doc);
		}
	}

	public void addDocument(Document doc){
		DocumentsList.Entry entry = new DocumentsList.Entry(doc);
		existingAttachments.add(entry);
	}

	public void lockCreateNewButton(boolean lock) {
		// TODO Dummy
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getDocumentsDataVersionNumber(String ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setDocumentsDataVersionNumber(String ownerId, int number) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setDocuments(String ownerId, List<Document> documents) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeDocument(String ownerId, Document document) {
		for (int i=0; i<existingAttachments.size(); i++) {
			if(existingAttachments.get(i).getValue().id.equals(document.id)) {
				existingAttachments.remove(i);
				break;
			}
		}
	}


	@Override
	public void addDocument(String ownerId, Document document) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateDocument(String ownerId, Document document) {
		// TODO Auto-generated method stub
		
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
		conversation.parentDataObjectId = value.parentDataObjectId;
		
		conversation.replylimit = (replyLimit==null || replyLimit.getValue()==null) ? null : replyLimit.getValue().intValue();
		
		//MESSAGE
		Message msg = new Message();
		msg.conversationId = conversation.id;
		msg.kind = Kind.EMAIL.toString().equalsIgnoreCase(emailOrNote.getValue()) ? Kind.EMAIL : Kind.NOTE;
		msg.subject = emailSubject.getValue();
		
		if(Kind.EMAIL.equals(msg.kind)){
			List<Message.MsgAddress> addresses = new ArrayList<Message.MsgAddress>();
			List<String> outgoingAttachment = new ArrayList<String>();
			msg.text = emailBody.getValue();
			addresses = getAddresses();
			outgoingAttachment = getAttachments();

			msg.addresses = new Message.MsgAddress[addresses.size()];

			for(int i = 0; i<msg.addresses.length; i++){
				msg.addresses[i] = addresses.get(i);
			}

			msg.attachments = new Message.Attachment[outgoingAttachment.size()];

			for(int i = 0; i<msg.attachments.length; i++){
				msg.attachments[i] = new Message.Attachment();
				msg.attachments[i].docId = outgoingAttachment.get(i);
				msg.attachments[i].promote = true;
				msg.attachments[i].attachmentId = outgoingAttachment.get(i);
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

		for(ListEntry<Document> doc : addedAttachments){
			attachs.add(doc.getValue().id);
		}
		return attachs;
	}
	
	private List<MsgAddress> getAddresses() {

		List<MsgAddress> addresses = new ArrayList<MsgAddress>();
		
		// Gets the To
		String addressesStr = toAddresses.getValue();
		String [] splitAddrs;
		if (addressesStr!=null && addressesStr.length()>0) {
			splitAddrs = addressesStr.split(";");
			for (int i=0; i<splitAddrs.length; i++) {
				String addr = splitAddrs[i].trim();
				MsgAddress to = new MsgAddress();
				to.usage = MsgAddress.Usage.TO;
				to.address = addr;
				addresses.add(to);
			}
		}
		
		// Gets the CC
		addressesStr = ccAddresses.getValue();
		if (addressesStr!=null && addressesStr.length()>0) {
			splitAddrs = addressesStr.split(";");
			for (int i=0; i<splitAddrs.length; i++) {
				String addr = splitAddrs[i].trim();
				MsgAddress cc = new MsgAddress();
				cc.usage = MsgAddress.Usage.CC;
				cc.address = addr;
				addresses.add(cc);
			}
		}
		
		// Gets the BCC
		addressesStr = bccAddresses.getValue();
		if (addressesStr!=null && addressesStr.length()>0) {
			splitAddrs = addressesStr.split(";");
			for (int i=0; i<splitAddrs.length; i++) {
				String addr = splitAddrs[i].trim();
				MsgAddress bcc = new MsgAddress();
				bcc.usage = MsgAddress.Usage.BCC;
				bcc.address = addr;
				addresses.add(bcc);
			}
		}
		
		// Gets the user's email address (the address is set later)
		MsgAddress me = new MsgAddress();
		me.userId = Session.getUserId();
		me.usage = MsgAddress.Usage.REPLYTO;
		
		addresses.add(me);

		for(String userId : forwardReply.getValue()){
			userId = userId.trim();
			MsgAddress newAddress = new MsgAddress();
			newAddress.display = userId;
			newAddress.usage = MsgAddress.Usage.REPLYTO;
			addresses.add(newAddress);
		}

		return addresses;
	}


	@Override
	public void setInfo(Conversation info) {
		
		if (info!=null) {
			value = info;
			requestType.setValue(info.requestTypeId);
			if (info.requestTypeId!=null) {
				requestType.setEditable(false);
			}
			subject.setValue(info.subject);
			if (info.subject!=null && info.subject.length()>0) {
				subject.setEditable(false);
			}
			// Utilizadores a envolver no processo?
			replyLimit.setValue(info.replylimit != null ? info.replylimit.doubleValue() : null);
			if (info.replylimit != null) {
				expectsResponse.setValue("YES");
			}
			if (info.messages != null && info.messages.length>0) {
				Message msg = info.messages[0];
				if (msg.emailId!=null) { 
					emailOrNote.setValue(Kind.EMAIL.toString());
				}

				if (msg.addresses!=null && msg.addresses.length>0) {
					String to = "";
					String cc = "";
					String bcc = "";
					for (int i=0; i<msg.addresses.length; i++) {
						MsgAddress addr = msg.addresses[i];
						
						if (addr.usage == MsgAddress.Usage.TO) {
							if (!to.equals("")) {
								to = to + "; ";
							}
							to = to + addr.address;
						}
						if (addr.usage == MsgAddress.Usage.CC) {
							if (!cc.equals("")) {
								cc = cc + "; ";
							}
							cc = cc + addr.address;
						}
						if (addr.usage == MsgAddress.Usage.BCC) {
							if (!bcc.equals("")) {
								bcc = bcc + "; ";
							}
							bcc = bcc + addr.address;
						}	
					}
					toAddresses.setValue(to);
					ccAddresses.setValue(cc);
					bccAddresses.setValue(bcc);
				}
				emailSubject.setValue(msg.subject);
				emailBody.setValue(msg.text);
				if (msg.attachments!=null && msg.attachments.length>0) {
					for (int i=0; i<msg.addresses.length; i++) {
						Attachment att = msg.attachments[i];
						Document doc = new Document();
						doc.creationDate = att.date;
						doc.docTypeId = att.docTypeId;
						doc.emailId = att.emailId;
						doc.fileName = att.name;
						doc.fileStorageId = att.storageId;
						doc.ownerId = att.ownerId;
						DocumentsList.Entry entry = new DocumentsList.Entry(doc);
						addedAttachments.add(entry);
					}
				}
			}
		}
	}
}
