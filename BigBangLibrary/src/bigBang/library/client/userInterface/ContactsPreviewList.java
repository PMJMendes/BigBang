package bigBang.library.client.userInterface;

import bigBang.library.client.ContactManager;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.shared.Contact;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class ContactsPreviewList extends List<Contact> {

	protected class ContactPreviewPanel extends NavigationPanel {
		
		protected Label nameLabel;
		protected Label valueLabel;
		protected ExpandableListBoxFormField type;
		protected ContactManager contactManager;

		public ContactPreviewPanel(ContactManager contactManager){
			this();
			setContactManager(contactManager);
		}
		
		public ContactPreviewPanel(){
			super();
			setSize("100%", "100%");
			navBar.setText("Contacto");
		}
		
		public void setContactManager(ContactManager contactManager) {
			this.contactManager = contactManager;
		}
		
		public void setContact(Contact contact) {
			/*final ContactForm form = new ContactForm();
			form.addAttachHandler(new AttachEvent.Handler() {

				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached())
						setSize(form.getOffsetWidth() + "px", "500px");
				}
			});
			form.setValue(contact);

			if(!hasHomeWidget())
				setHomeWidget(form);
			else
				navigateTo(form);

			form.getSubContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
				
				@Override
				public void onSelectionChanged(SelectionChangedEvent event) {
					for(Selectable s : event.getSelected()){
						@SuppressWarnings("unchecked")
						Contact contact = ((ValueSelectable<Contact>)s).getValue();
						setContact(contact);
						break;						
					}
				}
			});
			
			form.getNewButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					setContact(new Contact());
				}
			});
			
			form.getSaveButton().addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Contact c = form.getValue();
					if(c.id == null || c.id.equals("")){
						contactManager.addContact(c, new BigBangAsyncCallback<Contact>() {

							@Override
							public void onSuccess(Contact result) {
								form.setReadOnly(true);
							}
						});
					}else{
						contactManager.updateContact(c, new BigBangAsyncCallback<Contact>() {

							@Override
							public void onSuccess(Contact result) {
								form.setReadOnly(true);
							}
						});
					}
				}
			});*/
		}
	}
	
	protected Resources resources;
	protected PopupPanel contactPopupPanel;
	protected SelectedStateChangedEventHandler contactSelectedStateChangedHandler;
	protected ContactManager manager;
	protected HandlerRegistration managerHandlerRegistration;
	protected Button newButton;

	public ContactsPreviewList(){
		super();

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		resources = GWT.create(Resources.class);
		ListHeader header = new ListHeader();
		header.setText("Contactos");
		header.setHeight("25px");
		header.setLeftWidget(new Image(resources.contactsIcon()));

		setHeaderWidget(header);

		setSize("100%", "145px");

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		contactPopupPanel = new PopupPanel();
		contactPopupPanel.setAutoHideEnabled(true);
		contactPopupPanel.setSize("350px", "400px");
		
		/*contactSelectedStateChangedHandler = new SelectedStateChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectedStateChanged(final SelectedStateChangedEvent event) {
				if(!event.getSelected()){
					contactPopupPanel.hide();
					((ListEntry<?>) event.getSource()).setSelected(false);
				}else{
					ContactPreviewPanel contactPreviewPanel = new ContactPreviewPanel(ContactsPreviewList.this.manager);
					contactPreviewPanel.setContact(((ValueSelectable<Contact>)event.getSource()).getValue());
					contactPopupPanel.setWidget(contactPreviewPanel);
					contactPopupPanel.setPopupPositionAndShow(new PositionCallback() {

						@Override
						public void setPosition(int offsetWidth, int offsetHeight) {
							contactPopupPanel.setPopupPosition(((UIObject) event.getSource()).getAbsoluteLeft() - offsetWidth,
									((UIObject) event.getSource()).getAbsoluteTop() + 10);
						}
					});
				}
			}
		};
		
		contactPopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				for(ListEntry<?> e : entries){
					if(e.isSelected())
						e.setSelected(false);
				}
			}
		});*/
		
		newButton = new Button("Novo");
		newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				/*bigBang.library.client.userInterface.view.PopupPanel popup = new bigBang.library.client.userInterface.view.PopupPanel();
				ContactPreviewPanel contactPreviewPanel = new ContactPreviewPanel(ContactsPreviewList.this.manager);
				contactPreviewPanel.setContact(new Contact());
				popup.add(contactPreviewPanel);
				popup.setSize("600px", "600px");
				popup.center();*/
			}
		});
		header.setRightWidget(newButton);
	}
	
	public ContactsPreviewList(ContactManager contactManager) {
		this();
		setManager(contactManager);
	}
	
	public void setManager(ContactManager manager) {
		this.manager = manager;
		//if(this.managerHandlerRegistration != null)
		//	this.managerHandlerRegistration = this.manager.
		refresh();
	}
	
	public void refresh(){
		for(Contact c : this.manager.getContacts()){
			addEntryForContact(c);
		}
	}

	protected void addEntryForContact(Contact c){
		ListEntry<Contact> e = new ListEntry<Contact>(c) {
			@Override
			public void setSelected(boolean selected, boolean fireEvents) {
				super.setSelected(selected, fireEvents);
				if(selected) {
					//setLeftWidget(new Image(resources.phoneSmallIconWhite()));
				}else{
					//setLeftWidget(new Image(resources.phoneSmallIconBlack()));
				}
			}
		};
		//e.setLeftWidget(new Image(resources.phoneSmallIconBlack()));
		e.setText(c.name);
		e.setToggleable(true);
		e.addSelectedStateChangedEventHandler(contactSelectedStateChangedHandler);
		e.setHeight("25px");
		add(e);
	}
	
	public void setReadOnly(boolean readOnly) {
		this.newButton.setEnabled(!readOnly);
	}
}
