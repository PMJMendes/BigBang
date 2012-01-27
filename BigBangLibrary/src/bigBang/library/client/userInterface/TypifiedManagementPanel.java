package bigBang.library.client.userInterface;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.TipifiedListItem;

public interface  TypifiedManagementPanel {

	String getListId();

	void setListId(String listId);

	void setEditModeEnabled(boolean enabled);

	void setEditable(boolean editable);

	void setReadOnly(boolean readonly);

	public FilterableList<TipifiedListItem> getList();

	Widget asWidget();
	
	public void setTag(String tag);
	
	public String getTag();

}
