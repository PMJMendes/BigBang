package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.HasParameters;
import bigBang.library.client.dataAccess.TypifiedListBroker;

public interface  TypifiedManagementPanel {
	
	void setParameters(HasParameters parameters);

	String getListId();

	void setListId(String listId);

	void setEditModeEnabled(boolean enabled);

	void allowEdition(boolean editable);

	void setReadOnly(boolean readonly);

	public FilterableList<TipifiedListItem> getList();

	Widget asWidget();
	
	public void setTag(String tag);
	
	public String getTag();

	void setTypifiedDataBroker(TypifiedListBroker broker);

}
