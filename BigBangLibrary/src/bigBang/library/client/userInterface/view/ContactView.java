package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.shared.ModuleConstants;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ContactView extends View {

	public static class InfoEntry extends View {

		protected ExpandableListBoxFormField typeField;
		protected TextBoxFormField valueField;
		protected ContactInfo info;

		public InfoEntry(){
			VerticalPanel mainWrapper = new VerticalPanel();
			mainWrapper.setWidth("100%");
			mainWrapper.getElement().getStyle().setBorderColor("gray");
			mainWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");

			typeField = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactInfoTypes, "Tipo");
			valueField = new TextBoxFormField("Valor");
			valueField.setFieldWidth("100%");

			mainWrapper.add(typeField);
			mainWrapper.add(valueField);

			initWidget(mainWrapper);
			clear();
		}

		public void setInfo(ContactInfo info){
			if(info == null){
				clear();
			}else{
				typeField.setValue(info.typeId);
				valueField.setValue(info.value);
			}
		}
		
		public ContactInfo getInfo() {
			this.info.typeId = typeField.getValue();
			this.info.value = valueField.getValue();
			return this.info;
		} 
		
		public void clear() {
			typeField.clear();
			valueField.clear();
		}
		
		public void setReadOnly(boolean readOnly) {
			typeField.setReadOnly(readOnly);
			valueField.setReadOnly(readOnly);
			info = new ContactInfo();
		}
	}

	public static class ChildContactListEntry extends ListEntry<Contact> {

		public ChildContactListEntry(Contact value) {
			super(value);
			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			setHeight("25px");
		}

		public <I extends Object> void setInfo(I info) {
			Contact contact = (Contact) info;
			setText(contact.name);
		};

	}
	
	protected TextBoxFormField nameField;
	protected ExpandableListBoxFormField typeField;
	protected VerticalPanel infoWrapper;
	protected Collection<InfoEntry> infoEntries;
	protected List<Contact> childContactsList;
	protected BigBangOperationsToolBar toolbar;
	protected Contact contact;

	public ContactView(){
		nameField = new TextBoxFormField("Nome");
		nameField.setFieldWidth("100%");
		typeField = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactTypes, "Tipo");

		SimplePanel mainWrapper = new SimplePanel();
		mainWrapper.setSize("100%", "100%");
		
		ScrollPanel scrollWrapper = new ScrollPanel();
		scrollWrapper.setSize("100%", "100%");

		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");
		
		toolbar = new BigBangOperationsToolBar() {
			
			@Override
			public void onSaveRequest() {
				onSaveContact(contact);
			}
			
			@Override
			public void onEditRequest() {
				//TODO
			}
			
			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub
				
			}
		};
		toolbar.hideAll();
		toolbar.showItem(BigBangOperationsToolBar.SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.CREATE, true);
		toolbar.addItem(SUB_MENU.CREATE, new MenuItem("Sub Contacto", new Command() {
			
			@Override
			public void execute() {
				onCreateSubContact();
			}
		}));
		
		toolbar.setHeight("21px");
		toolbar.setWidth("100%");
		
		wrapper.add(toolbar);
		
		HorizontalPanel nameWrapper = new HorizontalPanel();
		nameWrapper.add(nameField);
		wrapper.add(nameWrapper);
		wrapper.add(typeField);

		infoEntries = new ArrayList<ContactView.InfoEntry>();
		infoWrapper = new VerticalPanel();
		infoWrapper.setWidth("100%");

		wrapper.add(infoWrapper);
		wrapper.setCellHeight(infoWrapper, "100%");

		childContactsList = new List<Contact>();
		childContactsList.setSize("100%", "150px");
		wrapper.add(childContactsList.getListContent());

		scrollWrapper.add(wrapper);
		mainWrapper.add(scrollWrapper);
		initWidget(mainWrapper);
	}

	public void setEditable(boolean editable) {
		//TODO
	}

	public void setContact(Contact contact){
		this.contact = contact;
		this.nameField.setValue(contact.name);
		for(int i = 0; contact.info != null && i < contact.info.length; i++) {
			InfoEntry entry = new InfoEntry();
			entry.setInfo(contact.info[i]);
			infoEntries.add(entry);
			infoWrapper.add(entry);
		}
		if(contact.subContacts != null){
			for(Contact c : contact.subContacts){
				ChildContactListEntry entry = new ChildContactListEntry(c); 
				childContactsList.add(entry);
			}
		}
	}
	
	public Contact getContact(){
		return this.contact;
	}
	
	public void clear(){
		this.nameField.clear();
		this.typeField.clear();
		this.infoWrapper.clear();
		this.childContactsList.clear();
		this.contact = new Contact();
	}

	public List<Contact> getChildContactsList() {
		return this.childContactsList;
	}
	
	public abstract void onSaveContact(Contact contact);

	public abstract void onCreateSubContact();
}

