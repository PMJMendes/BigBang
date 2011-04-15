package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.UserList;
import bigBang.module.generalSystemModule.client.userInterface.UserListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

public class UserManagementOperationView extends View implements UserManagementOperationViewPresenter.Display {
	
	private static final int LIST_WIDTH = 400; //px

	private UserList userList;
	private UserForm userForm;
	
	public UserManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		userList = new UserList();
		userList.setSize("100%", "100%");
		wrapper.addWest(userList, LIST_WIDTH);
		wrapper.setWidgetMinSize(userList, LIST_WIDTH);

		userForm = new UserForm();
		wrapper.add(userForm);

		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<User> getList() {
		return (HasValueSelectables<User>)this.userList;
	}
	
	@Override
	public void clearList(){
		this.userList.clear();
	}
	
	@Override
	public void addValuesToList(User[] result) {
		for(int i = 0; i < result.length; i++)
			this.userList.add(new UserListEntry(result[i]));
	}

	@Override
	public void removeUserFromList(User c) {
		for(ListEntry<User> e : this.userList){
			if(e.getValue() == c || e.getValue().id.equals(c.id)){
				this.userList.remove(e);
				break;
			}
		}
	}

	@Override
	public HasEditableValue<User> getForm() {
		return this.userForm;
	}

	@Override
	public void prepareNewUser() {
		for(ValueSelectable<User> s : this.userList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		UserListEntry entry = new UserListEntry(new User());
		this.userList.add(entry);
		this.userList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}
	
	@Override
	public void removeNewUserPreparation(){
		for(ValueSelectable<User> s : this.userList){
			if(s.getValue().id == null){
				this.removeUserFromList(s.getValue());
				break;
			}
		}
	}

	@Override
	public HasClickHandlers getNewButton() {
		return this.userList.newButton;
	}

	@Override
	public HasClickHandlers getRefreshButton() {
		return this.userList.refreshButton;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return this.userForm.getSaveButton();
	}

	@Override
	public HasClickHandlers getEditButton() {
		return this.userForm.getEditButton();
	}
	
	@Override
	public HasClickHandlers getDeleteButton() {
		return this.userForm.getDeleteButton();
	}

	@Override
	public boolean isFormValid() {
		return this.userForm.validate();
	}

	@Override
	public void setUserProfiles(UserProfile[] profiles) {
		userForm.setUserProfiles(profiles);
	}

	@Override
	public void setCostCenters(CostCenter[] costCenters) {
		userForm.setCostCenters(costCenters);
	}

	@Override
	public void lockForm(boolean lock) {
		this.userForm.setReadOnly(true);
		this.userForm.lock(lock);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.userList.newButton).setEnabled(!readOnly);
		this.userForm.setReadOnly(readOnly);
	}

}
