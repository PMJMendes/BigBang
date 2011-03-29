package bigBang.module.generalSystemModule.client.userInterface.presenter;

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.operation.UserManagementOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UserManagementOperationViewPresenter implements OperationViewPresenter {

	public interface Display {
		Widget asWidget();
		void setUsers(User[] users);
		HasValue<User> getUserList();
		void selectFirstElementInUserList();
		void showDetailsForUser(User u);
		HasClickHandlers getNewUserButton();
		void showNewUserForm();
		HasClickHandlers getSubmitNewUserButton();
		User getNewUserInfo();
	}
	
	private UserServiceAsync service;
	private EventBus eventBus;
	private Display view;
	
	private UserManagementOperation operation;
	private User[] userCache;
	
	public UserManagementOperationViewPresenter(EventBus eventBus, UserServiceAsync service, Display view) {
		this.setEventBus(eventBus);
		this.setService(service);
		this.setView((View)view);
		
		this.fetchUsers();
	}
	
	
	private void fetchUsers() {
		this.service.getUsers(new AsyncCallback<User[]>() {
			
			@Override
			public void onSuccess(User[] result) {
				userCache = result;
				view.setUsers(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not fetch the users.");
			}
		});
	}


	@Override
	public void setService(Service service) {
		this.service = (UserServiceAsync) service;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		
		this.view.selectFirstElementInUserList();
		
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void bind() {
		this.view.getNewUserButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.showNewUserForm();
			}
		});
		this.view.getUserList().addValueChangeHandler(new ValueChangeHandler<User>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<User> event) {
				for(User user : userCache){
					if(user.id.equals(event.getValue())){
						view.showDetailsForUser(user);
						break;
					}
				}
			}
		});
		view.getSubmitNewUserButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				User newUser = view.getNewUserInfo();
				createUser(newUser);
			}
		});
	}
	
	public void createUser(final User user){
		service.addUser(user, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not create user");
			}

			@Override
			public void onSuccess(String result) {
				view.setUsers(userCache);
				view.getUserList().setValue(user);
			}
		});
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}
	
	public void setOperation(Operation o) {
		this.operation = (UserManagementOperation) o;
	}

	public Operation getOperation() {
		return operation;
	}


	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}


	private void setReadOnly(boolean result) {
		// TODO Auto-generated method stub
		
	}

}
