package bigBang.library.client.userInterface;

import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SimpleEditableListEntry <T> extends ListEntry<T> {

	public Image deleteButton;
	public Image editButton;
	public Widget rightWidget;

	public SimpleEditableListEntry(T value) {
		super(value);
		if(deleteButton == null)
			deleteButton = new Image();
		deleteButton.getElement().getStyle().setCursor(Cursor.POINTER);
		deleteButton.setTitle("Apagar");
		if(editButton == null)
			editButton = new Image();
		editButton.getElement().getStyle().setCursor(Cursor.POINTER);
		editButton.setTitle("Editar");
		setEditable(false);
	}
	
	@Override
	public void setRightWidget(Widget w) {
		if(w != deleteButton)
			this.rightWidget = w;
		super.setRightWidget(w);
	}
	
	@Override
	public void setSelected(boolean selected, boolean fireEvents) {
		super.setSelected(selected, fireEvents);
		Resources r = GWT.create(Resources.class);
		if(deleteButton == null)
			deleteButton = new Image();
		deleteButton.setResource(isSelected() ? r.listDeleteIconSmallWhite() : r.listDeleteIconSmallBlack());
		if(editButton == null)
			editButton = new Image();
		editButton.setResource(isSelected() ? r.listEditIconSmallWhite() : r.listEditIconSmallBlack());
	}
	
	public void setEditable(boolean editable){
		if(editable){
			this.setRightWidget(deleteButton);
		}else {
			if(this.rightWidget != null)
				this.setRightWidget(this.rightWidget);
			else
				this.setRightWidget(new SimplePanel());
		}
		this.setLeftWidget(editable ? this.editButton : new SimplePanel());
	}

}
