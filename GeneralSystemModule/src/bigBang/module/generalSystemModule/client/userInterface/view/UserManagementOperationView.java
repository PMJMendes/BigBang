package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.User;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.UserList;
import bigBang.module.generalSystemModule.client.userInterface.UserListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.UserManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserManagementOperationView extends View implements UserManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private UserList userList;
	private UserForm userForm;
	protected BigBangOperationsToolBar toolbar;
	protected ActionInvokedEventHandler<UserManagementOperationViewPresenter.Action> actionHandler;

	public UserManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		userList = new UserList();
		userList.getNewButton().addClickHandler(new ClickHandler() {

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

		this.toolbar = new BigBangOperationsToolBar() {

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
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		toolbar.addItem(SUB_MENU.ADMIN, new MenuItem("Apagar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<UserManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		}));

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		userForm = new UserForm();
		formWrapper.add(userForm);



		wrapper.add(formWrapper);

		initWidget(wrapper);
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
				this.userList.remove(s);
				break;
			}
		}
	}

	@Override
	public boolean isFormValid() {
		return this.userForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.userForm.setReadOnly(true);
		this.userForm.lock(lock);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.userList.getNewButton()).setEnabled(!readOnly);
		this.userForm.setReadOnly(readOnly);
	}

	@Override
	public void clear(){
		this.userForm.clearInfo();
		this.userList.clear();
		this.userList.clearFilters();
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

}
