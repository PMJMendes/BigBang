package bigBang.library.client.userInterface.view;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;

public class ContactView2 extends View implements ContactViewPresenter.Display{

	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		CREATE_CHILD_CONTACT,
		SHOW_CHILD_CONTACTS
	}
	
	public class ContactEntry extends ListEntry<ContactInfo>{

		private ExpandableListBoxFormField type;
		private TextBoxFormField infoValue;
		private Button remove;

		public ContactEntry(ContactInfo contactinfo) {
			super(contactinfo);
		}

		@Override
		public void setValue(ContactInfo contactinfo) {

			if(contactinfo == null){
				Button add = new Button("Adicionar Detalhe");
				add.setWidth("180px");
				this.setLeftWidget(add);
				super.setValue(contactinfo);
				return;	
				
			}
			
			type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_DETAILS_TYPE, "");
			infoValue = new TextBoxFormField();

			type.setValue(contactinfo.typeId);
			infoValue.setValue(contactinfo.value);

			remove = new Button("X");
			this.setLeftWidget(type);
			this.setWidget(infoValue);
			this.setRightWidget(remove);
			super.setValue(contactinfo);
		}


		@Override
		public ContactInfo getValue() {

			return super.getValue();
		}
	}


	//	public static class SubContactListEntry extends ListEntry<Contact> {
	//
	//		public SubContactListEntry(Contact value) {
	//			super(value);
	//			setText("sub contacto");
	//			setHeight("30px");
	//		}
	//	}

	//	private SubContactListEntry subContacts;
	private VerticalPanel wrapper;
	private Contact contacto;
	private BigBangOperationsToolBar toolbar;
	private TextBoxFormField name;
	private ExpandableListBoxFormField type;
	private AddressFormField address;
	private List<ContactInfo> contactIL = new List<ContactInfo>();
	private ActionInvokedEventHandler<Action> actionHandler;
	private ListEntry<Void> childContactsButton;

	public ContactView2(){

		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setWidth("100%");
		//TOOLBAR
		toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE);
			}

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT);
			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL);
			}
		};
		toolbar.hideAll();
		toolbar.showItem(BigBangOperationsToolBar.SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.CREATE, true);
		toolbar.addItem(SUB_MENU.CREATE, new MenuItem("Sub Contacto", new Command() {

			@Override
			public void execute() {
				fireAction(Action.CREATE_CHILD_CONTACT);
			}
		}));

		toolbar.setHeight("21px");
		toolbar.setWidth("100%");

		name = new TextBoxFormField("Nome");
		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_TYPE, "Tipo");
		address = new AddressFormField();

		childContactsButton = new NavigationListEntry<Void>(null);
		childContactsButton.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		childContactsButton.setTitle("Sub-Contactos");
		childContactsButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if(event.getSelected()) {
					fireAction(Action.SHOW_CHILD_CONTACTS);
				}
			}
		});
		childContactsButton.setHeight("40px");


		HorizontalPanel horz = new HorizontalPanel();
		horz.add(type);
		horz.add(name);
		wrapper.add(toolbar);
		wrapper.add(horz);
		wrapper.add(address);
		ListHeader conts = new ListHeader("Detalhes");
		wrapper.add(conts);
		wrapper.add(contactIL.getListContent());
		wrapper.add(childContactsButton);

		//END TOOLBAR



	}

	@Override
	public void setContact(Contact contact) {

		this.name.setValue(contact.name);
		this.type.setValue(contact.typeId);
		this.address.setValue(contact.address);


	}
	@Override
	public void addContactInfo(ContactInfo contactinfo){
		
		
		ContactEntry temp = new ContactEntry(contactinfo);
		temp.setHeight("40px");
		contactIL.add(temp);

	}


	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}


}
