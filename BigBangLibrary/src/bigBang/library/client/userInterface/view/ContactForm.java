package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.FormField;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.shared.Address;
import bigBang.library.shared.Contact;
import bigBang.library.shared.ContactInfo;
import bigBang.library.shared.ModuleConstants;

public class ContactForm extends FormView<Contact> {

	public static class SubContactListEntry extends ListEntry<Contact> {

		public SubContactListEntry(Contact value) {
			super(value);
			setText("sub contacto");
			setHeight("30px");
		}
	}
	
	public static class ContactInfoListEntry extends ListEntry<ContactInfo> {

		public ExpandableListBoxFormField typeField;
		public TextBoxFormField valueField;
		
		public ContactInfoListEntry(ContactInfo value) {
			super(value);
			VerticalPanel wrapper = new VerticalPanel();
			wrapper.setSize("100%", "500px");
			
			typeField = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactInfoTypes, "Tipo");
			valueField = new TextBoxFormField("Valor");
			
			wrapper.add(typeField);
			wrapper.add(valueField);
		}
		
	}
	
	protected static class Tuple <T1, T2> {
		public T1 type;
		public T2 value;
	}
	
	protected Button saveButton, deleteButton;
	
	protected TextBoxFormField name;
	protected AddressFormField address;
	protected HasClickHandlers newButton;
	
	protected ArrayList<Tuple<FormField<?>, FormField<?>>> infoFields;
	
	protected List<Contact> subContactsList;
	protected List<ContactInfo> contactInfoList;
	
	protected TextBoxFormField infoValue;
	protected ExpandableListBoxFormField infoType;
	
	protected Contact contact;
	
	public ContactForm(){
		infoFields = new ArrayList<Tuple<FormField<?>, FormField<?>>>();
		
		addSection("Informação do Contacto");
		name = new TextBoxFormField("Nome");
		addFormField(name);
		
		addSection("Detalhes");
		//((CellPanel) this.currentSection.getContentWrapper()).setSpacing(0);
		//((UIObject) this.currentSection.getContentWrapper()).getElement().getStyle().setMargin(0, Unit.PX);
		this.contactInfoList = new List<ContactInfo>();
		//this.contactInfoList.setSize("100%", "100%");
		//addWidget(this.contactInfoList.getListContent());
		
		infoType = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactInfoTypes, "Tipo");
		infoValue = new TextBoxFormField("Valor");
		
		addFormField(infoType);
		addFormField(infoValue);
		
		address = new AddressFormField();
		name.setFieldWidth("200px");
		
		addSection("Morada");
		final FormViewSection addressSection = currentSection;
		
		final Button removeAddressButton = new Button("Remover morada");
		final Button newAddressButton = new Button("Nova morada");
		
		removeAddressButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addressSection.clear();
				addressSection.addWidget(newAddressButton);
			}
		});
		
		newAddressButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addressSection.clear();
				addressSection.addFormField(address);
				addressSection.addWidget(removeAddressButton);
			}
		});
		
		addressSection.addWidget(newAddressButton);
		
		addSection("Contactos associados");
		
		subContactsList = new List<Contact> ();
		subContactsList.setSize("100%", "200px");
		subContactsList.setMultipleSelectability(false);
		
		ListHeader header = new ListHeader();
		this.newButton = new Button("Criar novo");
		((UIObject) newButton).setWidth("100px");
		header.setLeftWidget((Widget) this.newButton);
		
		VerticalPanel subContactListWrapper = new VerticalPanel();
		subContactListWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		subContactListWrapper.setSize("100%", "100%");
		
		subContactListWrapper.add(header);
		subContactListWrapper.add(subContactsList.getListContent());
		subContactListWrapper.setCellHeight(subContactsList.getListContent(), "100%");
		subContactsList.getListContent().setHeight("150px");
		HasWidgets listWrapper = currentSection.getContentWrapper();
		((VerticalPanel) listWrapper).setSpacing(0);
		((UIObject) listWrapper).getElement().getStyle().setPadding(0, Unit.PX);
		currentSection.setContent(subContactListWrapper);
		currentSection.setStyleName("emptyContainer");
		//currentSection.setHeight("200px");
		
		addWidget(subContactListWrapper);
		
		saveButton = new Button("Guardar");
		deleteButton = new Button("Apagar");
		
		addButton(saveButton);
		addButton(deleteButton);
		
		this.contact = new Contact();
		
		setReadOnly(false);
	}

	public List<Contact> getSubContactsList() {
		return this.subContactsList;
	}
	
	public HasClickHandlers getNewButton(){
		return this.newButton;
	}
	
	@Override
	public Contact getInfo() {
		this.contact.name = this.name.getValue();
		this.contact.address = this.address.getValue();
		this.contact.info = new ContactInfo[1];
		ContactInfo i = new ContactInfo();
		i.typeId = this.infoType.getValue();
		i.value = this.infoValue.getValue();
		this.contact.info[0] = i;
		return this.contact;
	}

	@Override
	public void setInfo(Contact info) {
		this.infoFields.clear();
		
		name.setTitle(info.name == null ? "" : info.name);
		address.setValue(info.address == null ? new Address() : info.address);

		Arrays.sort(info.info, new Comparator<ContactInfo>() {

			@Override
			public int compare(ContactInfo arg0, ContactInfo arg1) {
				return arg0.typeId.compareTo(arg1.typeId);
			}
		});
		
		this.contactInfoList.clear();
		for(int i = 0; i < info.info.length; i++) {
			ContactInfoListEntry e = new ContactInfoListEntry(info.info[i]);
			this.contactInfoList.add(e);
		}
		
		this.subContactsList.clear();
		for(int i = 0; i < info.subContacts.length; i++)
			this.subContactsList.add(new SubContactListEntry(info.subContacts[i]));
	}

	public HasClickHandlers getSaveButton() {
		return this.saveButton;
	}
	
	public HasClickHandlers getDeleteButton() {
		return this.deleteButton;
	}
	
}
