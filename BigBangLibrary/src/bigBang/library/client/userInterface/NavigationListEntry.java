package bigBang.library.client.userInterface;

import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

public class NavigationListEntry<T> extends ListEntry<T> implements NavigationItem {

	public NavigationListEntry(T value) {
		super(value);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRepresentedValue(Object o) {
		setValue((T)o);
	}

	@Override
	public Object getRepresentedValue() {
		return getValue();
	}

	@Override
	public void setSelected(boolean selected, boolean fireEvents) {
		super.setSelected(selected, fireEvents);
		Resources r = GWT.create(Resources.class);
		setRightWidget(new Image(selected ? r.listNextIconWhite() : r.listNextIconBlack()));
	}
}
