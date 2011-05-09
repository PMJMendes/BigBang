package bigBang.library.client.userInterface;

import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

public class NavigationListEntry<T> extends ListEntry<T> implements NavigationItem {

	protected Image nextImage;
	
	public NavigationListEntry(T value) {
		super(value);
		setRightWidget(nextImage);
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
	
	public void setNavigatable(boolean navigatable) {
		nextImage.setVisible(navigatable);
	}

	@Override
	public void setSelected(boolean selected, boolean fireEvents) {
		super.setSelected(selected, fireEvents);
		if(nextImage == null)
			nextImage = new Image();
		Resources r = GWT.create(Resources.class);
		nextImage.setResource(selected ? r.listNextIconWhite(): r.listNextIconBlack());
	}
}
