package bigBang.library.client.userInterface.form;


import java.util.List;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Message.Kind;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DocumentsBrokerClient;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.RadioButtonFormField;
import bigBang.library.client.userInterface.RichTextAreaFormField;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.autocomplete.AutoCompleteTextListFormField;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.requestfactory.shared.impl.EntityCodex;

public class SendMessageForm extends FormView<Conversation> implements DocumentsBrokerClient {
	
	// Constants
	private static int TO_ADDRESS_ID = 1;
	private static int CC_ADDRESS_ID = 2;
	private static int BCC_ADDRESS_ID = 1;
	
	protected ExpandableListBoxFormField requestType;
	private TextBoxFormField subject;
	protected AutoCompleteTextListFormField forwardReply;
	protected RadioButtonFormField expectsResponse;
	protected NumericTextBoxFormField replyLimit;
	protected TextBoxFormField toAddresses;
	private Button addToButton;
	protected TextBoxFormField ccAddresses;
	private Button addCcButton;
	protected TextBoxFormField bccAddresses;
	private Button addBccButton;
	private HorizontalPanel contactsWrapper;
	protected ListBoxFormField existingContactsEntity;
	protected ListBoxFormField existingContact;
	private FormField<String> otherEntityContacts;
	public RadioButtonFormField emailOrNote;
	protected RichTextAreaFormField note;
	private VerticalPanel noteWrapper;
	private VerticalPanel emailWrapper;
	private TextBoxFormField emailSubject;
	private RichTextAreaFormField emailBody;
	
	public SendMessageForm(){
		
		requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Mensagem");
		requestType.allowEdition(false);
		subject = new TextBoxFormField("Tópico");
		forwardReply = new AutoCompleteTextListFormField("Utilizadores a envolver no processo");
		expectsResponse = new RadioButtonFormField("Espera resposta");
		expectsResponse.addOption("YES", "Sim");
		expectsResponse.addOption("NO", "Não");
		replyLimit = new NumericTextBoxFormField("Prazo de Resposta", false);
		replyLimit.setUnitsLabel("dias");
		replyLimit.setFieldWidth("70px");
		toAddresses = new TextBoxFormField("Destinatários do Email (separados por ';')");
		addToButton = new Button("Adicionar");
		ccAddresses = new TextBoxFormField("CC (separados por ';')");
		addCcButton = new Button("Adicionar");
		bccAddresses = new TextBoxFormField("BCC (separados por ';')");
		addBccButton = new Button("Adicionar");
		existingContactsEntity = new ListBoxFormField("Contacto associado a:");
		existingContactsEntity.setFieldWidth("400px");
		existingContact = new ListBoxFormField("Contacto Existente:");
		existingContact.setFieldWidth("400px");
		otherEntityContacts = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.OTHER_ENTITY, null);
		emailOrNote = new RadioButtonFormField("E-mail ou Nota");
		emailOrNote.addOption(Kind.EMAIL.toString(), "E-mail");
		emailOrNote.addOption(Kind.NOTE.toString(), "Nota");
		noteWrapper = new VerticalPanel();
		note = new RichTextAreaFormField("Notas");
		emailWrapper = new VerticalPanel();
		emailSubject = new TextBoxFormField("Assunto");
		emailBody = new RichTextAreaFormField();

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
		
		addSection("Detalhes do Processo de Mensagem");
		addFormField(requestType, false);
		addFormField(subject, false);
		addFormField(forwardReply, false);
		addFormField(expectsResponse, true);
		addFormField(replyLimit, true);

		addSection("Detalhes da Mensagem");
		contactsWrapper = new HorizontalPanel();
		contactsWrapper.add(toAddresses);
		contactsWrapper.add(addToButton);
		contactsWrapper.add(ccAddresses);
		contactsWrapper.add(addCcButton);
		contactsWrapper.add(bccAddresses);
		contactsWrapper.add(addBccButton);
		contactsWrapper.add(existingContactsEntity);
		contactsWrapper.add(otherEntityContacts);
		contactsWrapper.add(existingContact);
		addWidget(contactsWrapper);
		
		addFormField(emailOrNote);

		registerFormField(note);
		noteWrapper.add(note);
		addWidget(noteWrapper);
		noteWrapper.setVisible(false);
		
		registerFormField(emailSubject);
		emailWrapper.add(emailSubject);
		emailBody.setLabelWidth("0px");
		emailBody.setFieldWidth("100%");
		emailWrapper.add(emailBody);
		addWidget(emailWrapper);
		emailWrapper.setVisible(true);
		
		
		// Handlers
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
			clearEmails();
			return;
		} else if(id.equalsIgnoreCase(BigBangConstants.EntityIds.OTHER_ENTITY)) {
			clearEmails();
			otherEntityContacts.setVisible(true);
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
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os e-mails"), TYPE.ERROR_NOTIFICATION));
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
				this.existingContact.addItem(contact.name + " <" + info.value + ">", info.id);
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
			existingContact.clearValues();
		} else {
			existingContact.setValue(id);
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
		}
		
		if (typeOfAddId == TO_ADDRESS_ID) {
			toAddresses.setValue(addedAddresses);
		} else if (typeOfAddId == CC_ADDRESS_ID) {
			ccAddresses.setValue(addedAddresses);
		} else if (typeOfAddId == BCC_ADDRESS_ID) {
			bccAddresses.setValue(addedAddresses);
		}
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setInfo(Conversation info) {
		// TODO Auto-generated method stub
		
	}
}
