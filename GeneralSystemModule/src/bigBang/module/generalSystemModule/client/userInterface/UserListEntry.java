package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.shared.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.User;

public class UserListEntry extends ListEntry<String> {

	public UserListEntry(User user) {
		super(user.id);
		setTitle(user.name);
		setText(user.username);
		
		HorizontalPanel rightWidgetWrapper = new HorizontalPanel();
		rightWidgetWrapper.setSize("100px", "100%");
		Label label = new Label(user.role.name);
		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
		rightWidgetWrapper.add(label);
		setRightWidget(rightWidgetWrapper);
		
		setHeight("40px");
	}

}
