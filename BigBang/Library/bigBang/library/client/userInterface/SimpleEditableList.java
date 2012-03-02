package bigBang.library.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SimpleEditableList<T> extends FilterableList<T> {

	private TextBox valueTextBox;
	private boolean editModeEnabled;
	
	//UI
	private Button editButton;
	private HorizontalPanel toolBar;

	public SimpleEditableList(String listName){
		super();
		this.setSize("300px", "400px");
		
		this.editButton = new Button();
		this.editButton.setSize("80px", "27px");
		
		VerticalPanel headerWrapper = new VerticalPanel();
		headerWrapper.setSize("100%", "100%");
		
		ListHeader header = new ListHeader();
		header.setText(listName);
		header.setRightWidget(editButton);
		editButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setEditModeEnabled(!isEditModeEnabled());
			}
		});
		
		headerWrapper.add(header);
		
		toolBar = new HorizontalPanel();
		toolBar.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		toolBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolBar.setSpacing(5);
		toolBar.setSize("100%", "30px");

		valueTextBox = new TextBox();
		valueTextBox.setWidth("100%");

		final Button addButton = new Button("Criar");
		addButton.setWidth("80px");

		toolBar.add(valueTextBox);
		toolBar.add(addButton);
		toolBar.setCellWidth(valueTextBox, "100%");
		
		headerWrapper.add(toolBar);
		
		setHeaderWidget(headerWrapper);

		addButton.setEnabled(false);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//addNew();
			}
		});
		valueTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				addButton.setEnabled(valueTextBox.getValue().length() > 0);
			}
		});
		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached())
					getElement().getStyle().setBackgroundColor("white");
			}
		});
		
		setEditModeEnabled(false);
	}
	
	public void setEditModeEnabled(boolean enabled){
		this.editModeEnabled = enabled;
		this.editButton.setText(enabled ? "Cancelar" : "Editar");
		showFilterField(!enabled);
		this.toolBar.setVisible(enabled);
		for(ListEntry<T> e : entries) {
			((SimpleEditableListEntry<T>) e).setEditable(enabled);
		}
	}
	
	private boolean isEditModeEnabled(){
		return this.editModeEnabled;
	}
	
}
