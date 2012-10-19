package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.User;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.UserList;
import bigBang.module.generalSystemModule.client.userInterface.UserListEntry;
import bigBang.module.generalSystemModule.client.userInterface.UserOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.form.UserForm;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserManagementOperationView extends View implements UserManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private UserList userList;
	private UserForm userForm;
	protected UserOperationsToolbar toolbar;
	protected ToolButton newButton;
	protected ActionInvokedEventHandler<UserManagementOperationViewPresenter.Action> actionHandler;

	public UserManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		userList = new UserList();
		this.newButton = (ToolButton) userList.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.NEW));
			}
		});
		userList.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});

		userList.setSize("100%", "100%");
		wrapper.addWest(userList, LIST_WIDTH);
		wrapper.setWidgetMinSize(userList, LIST_WIDTH);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new UserOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}
			
			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		};

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		userForm = new UserForm();
		formWrapper.add(userForm);
		wrapper.add(formWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasValueSelectables<User> getList() {
		return (HasValueSelectables<User>)this.userList;
	}

	@Override
	public HasEditableValue<User> getForm() {
		return this.userForm;
	}
	
	@Override
	public void showFormPassword(boolean show) {
		this.userForm.showPasswordField(show);
	}

	@Override
	public void prepareNewUser(User newUser) {
		for(ValueSelectable<User> s : this.userList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		UserListEntry entry = new UserListEntry(newUser);
		this.userList.add(0, entry);
		this.userList.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}
	
	@Override
	public void removeFromList(ValueSelectable<User> selectable) {
		this.userList.remove(selectable);
	}

	@Override
	public boolean isFormValid() {
		return this.userForm.validate();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void clearAllowedPermissions() {
		this.toolbar.lockAll();
	}

	@Override
	public void allowCreate(boolean allow) {
		this.newButton.setEnabled(allow);
	}
	
	@Override
	public void allowEdit(boolean allow) {
		this.toolbar.setEditionAvailable(allow);
	}
	
	@Override
	public void allowDelete(boolean allow) {
		this.toolbar.allowDelete(allow);
	}

	@Override
	public void addUserToList(ListEntry<User> response) {
		userList.add(0, response);
	}

}
