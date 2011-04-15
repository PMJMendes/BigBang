package bigBang.library.client.userInterface;

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
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.NavigationToolbar.NavigationEvent.Navigation;
import bigBang.library.client.userInterface.NavigationToolbar.NavigationEventHandler;
import bigBang.library.client.userInterface.SlidePanel.Direction;
import bigBang.library.client.userInterface.view.ContactForm;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.ContactsServiceAsync;
import bigBang.library.shared.Contact;

public class ContactsPreviewList extends List<Contact> {

	protected class ContactPreviewPanel extends View {
		
		protected Label nameLabel;
		protected Label valueLabel;
		protected ExpandableListBoxFormField type;
		protected NavigationToolbar navToolbar;
		protected SlidePanel slide;
		
		public ContactPreviewPanel(){
			this(true);
		}
		
		public ContactPreviewPanel(boolean slideAble){
			
			VerticalPanel wrapper = new VerticalPanel();
			
			navToolbar = new NavigationToolbar();
			navToolbar.showNext(true);
			navToolbar.addNavigationEventHandler(new NavigationEventHandler() {
				
				@Override
				public void onNavigationEvent(Navigation n) {
					if(n == Navigation.NEXT){
						slideToContact(null);
					}
				}
			});
			wrapper.add(navToolbar);
			ContactForm form = new ContactForm();
			wrapper.add(form);
			wrapper.setCellHeight(form, "100%");
			
			if(slideAble){
				slide = new SlidePanel();
				wrapper.setSize("100%", "100%");
				slide.setSize("350px", "400px");
				slide.add(wrapper);
				initWidget(slide);
			}else{
				wrapper.setSize("350px", "400px");
				initWidget(wrapper);
			}
		}
		
		public void setContact(Contact contact) {
			//nameLabel.setText(contact.name);
			//valueLabel.setText(contact.info[0].value);
		}
		
		private void slideToContact(Contact contact) {
			final ContactPreviewPanel current = this;
			final ContactPreviewPanel newPanel = new ContactPreviewPanel(false);
			newPanel.slide = slide;
			newPanel.setContact(contact);
			newPanel.navToolbar.showPrevious(true);
			newPanel.navToolbar.addNavigationEventHandler(new NavigationEventHandler() {
				
				@Override
				public void onNavigationEvent(Navigation n) {
					if(n == Navigation.PREVIOUS) {
						newPanel.slide.slideInto(current, Direction.RIGHT);
					}
				}
			});
			
			slide.slideInto(newPanel, Direction.LEFT);
		}
	}
	
	Resources resources;
	PopupPanel contactPopupPanel;
	SelectedStateChangedEventHandler contactSelectedStateChangedHandler;

	public ContactsPreviewList(){
		super();
		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		resources = GWT.create(Resources.class);
		ListHeader header = new ListHeader();
		header.setText("Contactos");
		header.setHeight("25px");
		header.setLeftWidget(new Image(resources.contactsIcon()));

		Button newButton = new Button("Novo");
		header.setRightWidget(newButton);

		setHeaderWidget(header);		

		setSize("100%", "145px");

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		contactPopupPanel = new PopupPanel();
		contactPopupPanel.setAutoHideEnabled(true);
		final ContactPreviewPanel preview = new ContactPreviewPanel();
		
		contactPopupPanel.setWidget(preview);
		contactPopupPanel.setSize("350px", "400px");
		
		contactSelectedStateChangedHandler = new SelectedStateChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectedStateChanged(final SelectedStateChangedEvent event) {
				if(!event.getSelected()){
					contactPopupPanel.hide();
					((ListEntry<?>) event.getSource()).setSelected(false);
				}else{
					preview.setContact(((ListEntry<Contact>) event.getSource()).getValue());
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
		
		for(int i = 0; i < 3; i++) {
			addEntryForContact(null);
		}
		ListEntry<Contact> moreEntry = new ListEntry<Contact>(null);
		moreEntry.setHeight("25px");
		moreEntry.setText("ver+");
		add(moreEntry);
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
		add(e);
	}
}
