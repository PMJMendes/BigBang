package bigBang.module.clientModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.ContactsPreviewList;
import bigBang.library.client.userInterface.DockPanel;
import bigBang.library.client.userInterface.ExpandableButton;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.PreviewPanel;
import bigBang.module.clientModule.shared.ClientProcess;

public class ClientPreviewPanel extends PreviewPanel<ClientProcess> {

	private int RIGHT_PANEL_WIDTH = 200;//PX
	
	private ClientFormView form;
	
	public ClientPreviewPanel(){
		super();
		showIndividualSection(true);
		setIndividualSectionContent(getIndividualSectionContent());
		
		showMultipleSection(true);
	}
	
	public Widget getIndividualSectionContent(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		//RightBAR
		VerticalPanel rightBar = new VerticalPanel();
		rightBar.setSize("100%", "100%");
		wrapper.addEast(rightBar, this.RIGHT_PANEL_WIDTH);
		
		//Contacts
		/*VerticalPanel contactsPanel = new VerticalPanel();
		contactsPanel.setWidth("100%");
		contactsPanel.add(new Label("Contactos"));
		
		/*final PopupPanel contactPopupPanel = new PopupPanel();
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
		
		rightBar.add(new ContactsPreviewList());
		
		
		
		//EXPERIMENTAL START
		
		SimplePanel filler = new SimplePanel();
		filler.setSize("100%", "100%");
		rightBar.add(filler);
		rightBar.setCellHeight(filler, "100%");
		
		//EXPERIMENTAL END
		
		//FORM
		this.form = new ClientFormView();
		this.form.setSize("100%", "100%");

		wrapper.add(this.form);
		
		return wrapper;
	}
	
	@Override
	public void setValue(ClientProcess value, boolean fireEvents) {
		super.setValue(value, fireEvents);
		render();
	}
	
	public void render(){
		//this.form.showProcess(getValue());
	}

}
