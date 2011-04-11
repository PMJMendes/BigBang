package bigBang.library.client.userInterface;

import bigBang.library.client.resources.Resources;
import bigBang.library.shared.TipifiedListItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SimpleEditableListEntry <T> extends ListEntry<T> {

	public Image deleteButton;
	public Widget rightWidget;

	public SimpleEditableListEntry(T value) {
		super(value);
		if(deleteButton == null)
			deleteButton = new Image();
		deleteButton.getElement().getStyle().setCursor(Cursor.POINTER);
		deleteButton.setTitle("Apagar");
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
	}
	
	public void setEditable(boolean editable){
		if(editable){
			this.setRightWidget(deleteButton);
		}else {
			if(this.rightWidget != null)
				this.setRightWidget(this.rightWidget);
		}
	}

}
