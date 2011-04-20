package bigBang.library.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.ContactForm;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.shared.Contact;
import bigBang.library.shared.ContactInfo;
import bigBang.library.shared.ModuleConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.UIObject;

public class ContactsPreviewList extends List<Contact> {

	protected class ContactPreviewPanel extends NavigationPanel {
		
		protected Label nameLabel;
		protected Label valueLabel;
		protected ExpandableListBoxFormField type;

		public ContactPreviewPanel(){
			super();
			setSize("100%", "100%");
			navBar.setText("Contacto");
		}
		
		public void setContact(Contact contact) {
			ContactForm form = new ContactForm();
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
		}
	}
	
	protected Resources resources;
	protected PopupPanel contactPopupPanel;
	protected SelectedStateChangedEventHandler contactSelectedStateChangedHandler;
	
	protected String entityInstanceId;

	public ContactsPreviewList(){
		this(null, null);
	}
	
	public ContactsPreviewList(String entityTypeId, String entityInstanceId){
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
		
		contactSelectedStateChangedHandler = new SelectedStateChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectedStateChanged(final SelectedStateChangedEvent event) {
				if(!event.getSelected()){
					contactPopupPanel.hide();
					((ListEntry<?>) event.getSource()).setSelected(false);
				}else{
					ContactPreviewPanel contactPreviewPanel = new ContactPreviewPanel();
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
		});
		
		Button newButton = new Button("Novo");
		newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				bigBang.library.client.userInterface.view.PopupPanel popup = new bigBang.library.client.userInterface.view.PopupPanel();
				ContactPreviewPanel contactPreviewPanel = new ContactPreviewPanel();
				contactPreviewPanel.setContact(new Contact());
				popup.add(contactPreviewPanel);
				popup.setSize("600px", "400px");
				popup.center();
			}
		});
		header.setRightWidget(newButton);
		setEntityInfo(entityInstanceId);
	}

	private void addEntryForContact(Contact c){
		ListEntry<Contact> e = new ListEntry<Contact>(c) {
			@Override
			public void setSelected(boolean selected, boolean fireEvents) {
				super.setSelected(selected, fireEvents);
				if(selected) {
					setLeftWidget(new Image(resources.phoneSmallIconWhite()));
				}else{
					setLeftWidget(new Image(resources.phoneSmallIconBlack()));
				}
			}
		};
		e.setLeftWidget(new Image(resources.phoneSmallIconBlack()));
		e.setText("94545678");
		e.setToggleable(true);
		e.addSelectedStateChangedEventHandler(contactSelectedStateChangedHandler);
		e.setHeight("25px");
		c.subContacts = new Contact[1];
		c.subContacts[0] = new Contact();
		add(e);
	}

	public void setEntityInfo(String entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
		if(entityInstanceId != null){
			fetchContacts();
			
			/** TODO
			 * Contact c = new Contact();
			
			c.name = "novo contacto";
			c.info = new ContactInfo[2];
			c.info[0] = new ContactInfo();
			c.info[0].typeId = "";
			c.info[0].value = "telefone";
			
			c.info[1] = new ContactInfo();
			c.info[1].typeId = "";
			c.info[1].value = "email";
			
			ContactsService.Util.getInstance().createContact(, c, new BigBangAsyncCallback<Contact>() {

				@Override
				public void onSuccess(Contact result) {
					GWT.log("success");
				}
			});*/
		}		
	}
	
	protected void fetchContacts(){
		if(this.entityInstanceId == null)
			return;
		ContactsService.Util.getInstance().getContacts(entityInstanceId, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onSuccess(Contact[] result) {
				clear();
				for(int i = 0; i < result.length; i++){
					addEntryForContact(result[i]);
				}
			}
		});
	}

	public Contact[] getContacts() {
		Contact[] result = new Contact[size()];
		int i = 0;
		for(ValueSelectable<Contact> s : entries){
			result[i] = s.getValue();
			i++;
		}
		return result;
	}
}
