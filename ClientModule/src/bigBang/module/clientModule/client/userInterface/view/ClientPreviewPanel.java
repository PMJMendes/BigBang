package bigBang.module.clientModule.client.userInterface.view;

import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.TypifiedListManagementPanel;
import bigBang.library.client.userInterface.view.PreviewPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientPreviewPanel extends View {

	private int RIGHT_PANEL_WIDTH = 200;//PX
	
	private ClientFormView form;
	
	public ClientPreviewPanel(){
		super();
	}
	
	public Widget getIndividualSectionContent(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		//RightBAR
		VerticalPanel rightBar = new VerticalPanel();
		rightBar.setSize("100%", "100%");
		wrapper.addEast(rightBar, this.RIGHT_PANEL_WIDTH);
		
		//Contacts
		VerticalPanel contactsPanel = new VerticalPanel();
		contactsPanel.setWidth("100%");
		ListHeader contactsHeader = new ListHeader();
		contactsHeader.setText("Contactos");
		contactsPanel.add(contactsHeader);
		
		final PopupPanel contactPopupPanel = new PopupPanel();
		contactPopupPanel.setAutoHideEnabled(true);
		
		final FilterableList<String> contactsList = new FilterableList<String>();
		contactsList.setHeight("300px");

		SelectedStateChangedEventHandler contactSelectedStateChangedHandler = new SelectedStateChangedEventHandler() {
			
			@Override
			public void onSelectedStateChanged(final SelectedStateChangedEvent event) {
				if(!event.getSelected()){
					contactPopupPanel.hide();
					((ListEntry<?>) event.getSource()).setSelected(false);
				}else{
					contactPopupPanel.setWidget(contactsList);
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
		
		for(int i = 0; i < 3; i++) {
			final ListEntry<String> e = new ListEntry<String>("Documento"+i);
			e.setHeight("25px");
			e.setTitle("94545678"+i);
			e.addSelectedStateChangedEventHandler(contactSelectedStateChangedHandler);
			e.setToggleable(true);
			contactsPanel.add(e);
			contactPopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
				
				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					e.setSelected(false);
				}
			});
		}
		
		rightBar.add(contactsPanel);
		
		//Documents
		/*VerticalPanel documentsPanel = new VerticalPanel();
		documentsPanel.setWidth("100%");
		documentsPanel.add(new Label("Documentos"));
		for(int i = 0; i < 3; i++) {
			ListEntry<String> e = new ListEntry<String>("Documento"+i);
			e.setText("94545678"+i);
			e.setSelectable(false);
			e.setHeight("20px");
			documentsPanel.add(e);
		}*/
		
		//rightBar.add(new ContactsPreviewList());
		
		
		
		//EXPERIMENTAL START
		
		SimplePanel filler = new SimplePanel();
		filler.setSize("100%", "100%");
		rightBar.add(filler);
		rightBar.setCellHeight(filler, "100%");
		
		//EXPERIMENTAL END
		
		//FORM
		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");

		wrapper.add(form);
		
		
		return wrapper;
	}

}
