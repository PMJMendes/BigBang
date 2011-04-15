package bigBang.library.client.userInterface;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;

public class SearchPanelListEntry<T> extends ListEntry<T> {

	private CheckBox checkBox;
	
	public SearchPanelListEntry(T value) {
		super(value);
	}

}
