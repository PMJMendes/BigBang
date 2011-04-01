package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.UserList;
import bigBang.module.generalSystemModule.client.userInterface.UserListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class UserManagementOperationView extends View implements UserManagementOperationViewPresenter.Display {

	private final int USER_LIST_WIDTH = 400; //px
	
	private UserList userList;
	private UserForm userForm;
	
	private PopupPanel newUserPopup;
	private UserForm newUserForm;
	
	private Button newUserButton;
	private Button submitNewUserButton;
	
	public UserManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");
		
		userList = new UserList();
		ListHeader header = new ListHeader();
		header.setText("Utilizadores");
		header.showNewButton("Novo");
		header.showRefreshButton();
		userList.setHeaderWidget(header);
		wrapper.addWest(userList, USER_LIST_WIDTH);
		
		newUserButton = new Button("Novo Utilizador");

		userForm = new UserForm(null, null);		
		userForm.addButton(newUserButton);
		
		wrapper.add(userForm);
		
		initWidget(wrapper);
		
		submitNewUserButton = new Button("Submeter");
		newUserForm = new UserForm(null, null);
		newUserPopup = new PopupPanel("Criação de Utilizador");
		newUserPopup.add(newUserForm.getNonScrollableContent());
	}

	@Override
	public void setUsers(User[] users) {
		userList.clear();
		for(int i = 0; i < users.length; i++)
			userList.add(new UserListEntry(users[i]));
	}

	@Override
	public void showDetailsForUser(User u) {
		this.userForm.setUser(u);
	}


	@Override
	public HasClickHandlers getNewUserButton() {
		return this.newUserButton;
	}

	@Override
	public void showNewUserForm() {
		newUserForm.clearInfo();
		newUserForm.showPasswordField(true);
		newUserForm.setReadOnly(false);

		newUserPopup.center();
		
		newUserPopup.setWidth("600px");
	}

	@Override
	public HasClickHandlers getSubmitNewUserButton() {
		return this.submitNewUserButton;
	}

	@Override
	public User getNewUserInfo() {
		return (User) this.newUserForm.getInfo();
	}
	
}
