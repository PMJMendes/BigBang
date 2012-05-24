package bigBang.library.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.view.FormView;



public class ContactForm extends FormView<Contact> {

	public class ContactEntry extends ListEntry<ContactInfo>{

		protected ExpandableListBoxFormField type;
		protected TextBoxFormField infoValue;
		private Button remove;
		public ContactEntry(ContactInfo contactinfo) {
			super(contactinfo);
		}

		@Override 
		public void setValue(ContactInfo contactinfo) {

			if(contactinfo == null){
				Button add = new Button("Adicionar Detalhe");
				add.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						ContactForm.this.addNewDetail();

					}
				});
				add.setWidth("180px");
				this.setLeftWidget(add);
				super.setValue(contactinfo);
				return;	

			}

			type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_DETAILS_TYPE, "");
			type.setFieldWidth("100px");
			infoValue = new TextBoxFormField();
			infoValue.setFieldWidth("205px");
			infoValue.setWidth("205px");

			type.setValue(contactinfo.typeId);
			infoValue.setValue(contactinfo.value);

			remove = new Button("X");
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					onDeleteRequest(event.getSource());
				}
			});
			this.setLeftWidget(type);
			this.setWidget(infoValue);
			this.setRightWidget(remove);
			super.setValue(contactinfo);
		}


		public void setEditable(boolean editable){

			if(type == null){
				this.setVisible(editable);
				return;
			}
			type.setReadOnly(!editable);
			infoValue.setReadOnly(!editable);
			remove.setVisible(editable);
		}


		@Override
		public ContactInfo getValue() {

			return value;

		}
	}

	private TextBoxFormField name;
	private ExpandableListBoxFormField type;
	private AddressFormField address;
	private List<ContactInfo> contactIL;

	public ContactForm() {

		addSection("Informação Geral");

		name = new TextBoxFormField("Nome"); 
		name.setFieldWidth("200px");
		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_TYPE, "Tipo");
		type.setFieldWidth("155px");
		addFormField(name);
		addFormField(type);
		addSection("Morada");
		address = new AddressFormField();
		address.setWidth("400px");
		addFormField(address);
		addSection("Detalhes");
		currentSection.setSize("100%", "200px");
		contactIL = new List<ContactInfo>();
		contactIL.setSelectableEntries(false);
		contactIL.setSize("100%", "100%");
		addWidget(contactIL);

	}

	@Override
	public Contact getInfo() {

		Contact newContact = value != null ? value : new Contact();

		newContact.address = address.getValue();
		newContact.name = name.getValue();
		newContact.typeId = type.getValue();

		for(int i = 0; i<contactIL.size()-1; i++){

			if(((ContactEntry) contactIL.get(i)).type.getValue() == null && ((ContactEntry) contactIL.get(i)).infoValue.getValue() == null){
				contactIL.remove(i);
				i--;
			}
		}

		newContact.info = new ContactInfo[contactIL.size()-1];
		for(int i = 0; i<contactIL.size()-1; i++){

			newContact.info[i] = new ContactInfo();
			newContact.info[i].typeId = ((ContactEntry) contactIL.get(i)).type.getValue();
			newContact.info[i].value = ((ContactEntry) contactIL.get(i)).infoValue.getValue();
		}	

		return newContact;

	}

	@Override
	public void setInfo(Contact contact) {

		contactIL.clear();

		if(contact == null){
			this.name.setValue("");
			this.type.setValue("");
			this.address.setValue(null);
			addContactInfo(null);
			return;
		}

		this.name.setValue(contact.name);
		this.type.setValue(contact.typeId);
		this.address.setValue(contact.address);

		for(int i = 0; i<contact.info.length; i++){
			addContactInfo(contact.info[i]);
		}
		addContactInfo(null);

	}


	public void addContactInfo(ContactInfo contactinfo){

		ContactEntry temp = new ContactEntry(contactinfo);
		if(contactinfo == null){
			temp.setValue(null);
		}
		temp.setHeight("40px");
		contactIL.add(temp);
		contactIL.getScrollable().scrollToBottom();

	}

	public void setEditable(boolean editable) {

		this.name.setReadOnly(!editable);
		this.type.setReadOnly(!editable);
		this.type.setReadOnly(!editable);
		this.address.setReadOnly(!editable);
		int i;
		ContactEntry temp;
		for(i=0; i<contactIL.size(); i++){

			temp = (ContactEntry) contactIL.get(i);
			temp.setEditable(editable);

		}		
	}

	public List<ContactInfo> getContactInfoList() {
		return contactIL;
	}

	public ContactEntry initializeContactEntry() {
		return new ContactEntry(new ContactInfo());
	}


	public void addNewDetail() {

		ContactEntry temp = initializeContactEntry();
		temp.setHeight("40px");
		getContactInfoList().remove(getContactInfoList().size()-1);
		temp.setEditable(true);
		addContactInfo(temp.getValue());
		addContactInfo(null);

	}

	public void onDeleteRequest(Object object) {

		List<ContactInfo> list = getContactInfoList();

		for(ValueSelectable<ContactInfo> cont: list){

			if(cont.getValue() == object) {
				list.remove(cont);
				break;
			}
		}
	}
}
