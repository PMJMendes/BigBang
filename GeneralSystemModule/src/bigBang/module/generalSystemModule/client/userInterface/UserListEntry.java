package bigBang.module.generalSystemModule.client.userInterface;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import bigBang.library.client.userInterface.ListEntry;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

public class UserListEntry extends ListEntry<User> {

	private UserProfile[] userProfiles;
	
	private Label userProfileLabel;
	
	public UserListEntry(User user, UserProfile[] profiles) {
		super(user);
	
		userProfiles = profiles;
		
		HorizontalPanel rightWidgetWrapper = new HorizontalPanel();
		rightWidgetWrapper.setSize("100px", "100%");
		userProfileLabel = new Label();
		userProfileLabel.getElement().getStyle().setFontWeight(FontWeight.NORMAL);		
		rightWidgetWrapper.add(userProfileLabel);
		setRightWidget(rightWidgetWrapper);
		
		setHeight("40px");
		
		setInfo(user);
	}
	
	public void setInfo(User info) {
		if(info.id == null){
			setTitle("Novo Utilizador");
			return;
		}
		
		setTitle(info.name);
		setText(info.username);
		for(int i = 0; i < userProfiles.length; i++){
			if(userProfiles[i].id.equals(info.profile.name)){
				userProfileLabel.setText(userProfiles[i].name);
				break;
			}
		}
	}

}
