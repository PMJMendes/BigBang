package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.definitions.shared.User;
import bigBang.library.client.userInterface.ListEntry;

public class UserListEntry extends ListEntry<User> {

	private Label userProfileLabel;
	
	public UserListEntry(User user) {
		super(user);
	
		HorizontalPanel rightWidgetWrapper = new HorizontalPanel();
		rightWidgetWrapper.setSize("100px", "100%");
		if(userProfileLabel == null)
			userProfileLabel = new Label();
		userProfileLabel.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
		rightWidgetWrapper.add(userProfileLabel);
		setRightWidget(rightWidgetWrapper);
		
		setHeight("40px");
		
		setInfo(user);
	}
	
	@Override
	public <I extends Object> void setInfo(I infoIn) {
		User info = (User) infoIn;
		if(info.id == null){
			setTitle("Novo Utilizador");
			return;
		}
		
		if(userProfileLabel == null)
			userProfileLabel = new Label();
		
		setTitle(info.name);
		setText(info.username);
		String userProfile = info.profile == null ? new String() : info.profile.name == null ? new String() : info.profile.name;
		userProfileLabel.setText(userProfile);
	};

}
