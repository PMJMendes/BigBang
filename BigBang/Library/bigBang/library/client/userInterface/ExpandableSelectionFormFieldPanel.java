package bigBang.library.client.userInterface;

import com.google.gwt.user.client.ui.HasValue;

import bigBang.library.client.userInterface.view.View;

public abstract class ExpandableSelectionFormFieldPanel extends View implements HasValue<String> {

	public abstract void setListId(String listId);

	@Override
	protected void initializeView() {
		return;
	}

	public abstract void setReadOnly(boolean readOnly);
	
	public abstract boolean isReadOnly();

}
