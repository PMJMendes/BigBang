package bigBang.library.client.userInterface.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.FieldValidator;
import bigBang.library.client.FormField;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.userInterface.ListBoxFormField;
import bigBang.library.client.userInterface.MutableSelectionFormFieldFactory;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectMessageAddressesForm extends FormField<MsgAddress[]>{

	protected boolean hasDummyValue = false;
	protected HorizontalPanel wrapper;
	private ListBoxFormField contacts;
	private ListBoxFormField contactsFrom;
	List<Contact> list;
	private TextBoxFormField textField;
	private Button clear, cancel, add;
	private Contact[] currentAddresses;
	private boolean isReadOnly;
	private FormField<String> otherEntityContacts;
	protected Map<String, String> ownerTypes;

	public SelectMessageAddressesForm(String label,FieldValidator<MsgAddress[]> validator){
		this();
		setLabel(label);
		setValidator(validator);
	}

	SelectMessageAddressesForm(FieldValidator<MsgAddress[]> validator){
		this();
		setValidator(validator);
	}

	protected void setLabel(String label) {
		if(label == null || label.equals("")){
			this.label.setText("");

		}else{
			this.label.setText(label);
		}
	}

	public SelectMessageAddressesForm(String string) {
		this();
		setLabel(string);
	}

	public SelectMessageAddressesForm() {
		super();

		VerticalPanel mainWrapper = new VerticalPanel();
		initWidget(mainWrapper);

		mainWrapper.add(this.label);

		contactsFrom = new ListBoxFormField("Contactos de:");
		contacts = new ListBoxFormField("Para:");
		otherEntityContacts = MutableSelectionFormFieldFactory.getFormField(BigBangConstants.EntityIds.OTHER_ENTITY, null);
		list = new ArrayList<Contact>();

		HorizontalPanel textWrapper = new HorizontalPanel();

		textField = new TextBoxFormField(){
			@Override
			public void setReadOnly(boolean readonly) {
				return;
			}
		};
		clear = new Button("Limpar");
		add = new Button("Adicionar");
		cancel = new Button("Cancelar");

		HorizontalPanel contactsAndOtherEntities = new HorizontalPanel();
		
		contactsAndOtherEntities.add(contactsFrom);
		contactsAndOtherEntities.add(otherEntityContacts);
		
		mainWrapper.add(contactsAndOtherEntities);
		textWrapper.add(textField);
		textWrapper.add(contacts);
		textWrapper.add(add);
		textWrapper.add(cancel);
		textWrapper.add(clear);
		mainWrapper.add(textWrapper);

		textWrapper.setSpacing(4);

		contactsFrom.setVisible(false);
		contacts.setVisible(false);
		cancel.setVisible(false);


		textField.setEditable(false);		
		textField.getElement().getStyle().setBackgroundColor("white");
		textField.getElement().getStyle().clearMargin();
		textField.getParent().getElement().getStyle().clearMargin();
		textField.getNativeField().getElement().getStyle().clearMargin();

		add.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchContext(true);
			}
		});

		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				switchContext(false);

			}
		});

		clear.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				textField.clear();
				contacts.setValue(null);
				list.clear();
			}
		});
		
		contactsFrom.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				contactChoiceChanged(event.getValue());
			}
		});

		contacts.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(!(event.getValue() == null) && !event.getValue().isEmpty()){
					for(Contact contact : currentAddresses){
						if(contact.info[0].id.equals(event.getValue())){
							textField.setValue((textField.getValue() != null ? textField.getValue() : "") + contact.info[0].value + ", ");
							list.add(contact);
							switchContext(false);
							break;
						}
					}
				}
			}
		});
		
		otherEntityContacts.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if(event.getValue() != null)
					contactChoiceChanged(event.getValue(), true);
			}
		});
		
		ownerTypes = new HashMap<String, String>();

		otherEntityContacts.setVisible(false);
		contacts.setFieldWidth("400px");
		contactsFrom.setFieldWidth("400px");
	}
	
	private void contactChoiceChanged(String id) {
		contactChoiceChanged(id, false);
	}
	
	protected void contactChoiceChanged(String id, boolean isOtherEntity) {

		if(id == null || id.isEmpty() /*|| BigBangConstants.TypifiedListValues.MEDIATOR_IDS.DIRECT.equalsIgnoreCase(id)*/){
			clearEmails();
			return;
		}
		else if(id.equalsIgnoreCase(BigBangConstants.EntityIds.OTHER_ENTITY)){
			clearEmails();
			otherEntityContacts.setVisible(true);
			return;
		}

		otherEntityContacts.setVisible(isOtherEntity);

		ContactsServiceAsync contactsService = ContactsService.Util.getInstance();

		contactsService.getFlatEmails(id, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onResponseSuccess(Contact[] result) {
				setContacts(result);
			}			

			@Override
			public void onResponseFailure(Throwable caught) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os e-mails"), TYPE.ERROR_NOTIFICATION));
				super.onResponseFailure(caught);
			}

		});

	}


	protected void switchContext(boolean b) {
		contactsFrom.setVisible(b);
		add.setVisible(!b);
		textField.setVisible(!b);
		contacts.setVisible(b);
		cancel.setVisible(b);
		clear.setVisible(!b);		
		contacts.setValue(null);
		otherEntityContacts.setVisible(b && BigBangConstants.EntityIds.OTHER_ENTITY.equalsIgnoreCase(contactsFrom.getValue()));
	}

	@Override
	public void clear() {
		contacts.clearValues();
		list.clear();
	}

	@Override
	protected void setReadOnlyInternal(boolean readonly) {
		clear.setEnabled(!readonly);
		contacts.setReadOnly(readonly);
		isReadOnly = readonly;
	}

	@Override
	public boolean isReadOnly() {
		return isReadOnly;
	}

	@Override
	public void focus() {
		contacts.focus();
	}

	@Override
	public void setLabelWidth(String width) {
		this.label.setWidth(width);		
	}

	public void setContacts(Contact[] addresses){
		contacts.clearValues();
		currentAddresses = addresses;
		for(Contact add : addresses){
			ContactInfo info = add.info[0];
			contacts.addItem(add.name + " <" + info.value + ">", info.id);
		}
	}

	@Override
	public MsgAddress[] getValue() {

		MsgAddress[] toReturn = new MsgAddress[list.size()];

		int i = 0;
		for(Contact cont : list){
			toReturn[i] = new MsgAddress();
			toReturn[i].usage = MsgAddress.Usage.CC;
			toReturn[i].contactInfoId = cont.info[0].id;
			i++;
		}

		return toReturn;
	}


	@Override
	public void setValue(MsgAddress[] value) {

		for(MsgAddress add : value){
			Contact cont = new Contact();
			cont.info = new ContactInfo[1];
			cont.info[0] = new ContactInfo();
			cont.info[0].id = add.contactInfoId;
			cont.info[0].value = add.address;
			list.add(cont);
			textField.setValue((textField.getValue() != null ? textField.getValue() : "") + cont.info[0].value + ", ");
		}		
	}

	public void addOtherEntityEntry(){
		contactsFrom.addItem("Outras Entidades", BigBangConstants.EntityIds.OTHER_ENTITY);
	}
	
	public void clearEmails() {
		contacts.clearValues();
		otherEntityContacts.setValue(null);
	}

	public void addItemContactList(String name, String id, String ownerType) {
		contactsFrom.addItem(name, id);
		ownerTypes.put(id, ownerType);
	}
}
