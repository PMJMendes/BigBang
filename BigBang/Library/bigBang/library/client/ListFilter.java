package bigBang.library.client;

import com.google.gwt.user.client.ui.Widget;


public interface ListFilter<T> {
	public String getName();
	public String setValue(T value);
		
	public Widget getWidget();
	public void clearValue();
}
