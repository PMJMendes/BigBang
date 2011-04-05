package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.interfaces.CostCenterService;
import bigBang.module.generalSystemModule.interfaces.UserServiceAsync;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.User;
import bigBang.module.generalSystemModule.shared.UserProfile;
import bigBang.module.generalSystemModule.shared.operation.UserManagementOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UserManagementOperationViewPresenter implements
OperationViewPresenter {

	public interface Display {
		//List
		HasValueSelectables<User> getList();
		void clearList();
		void addValuesToList(User[] result, UserProfile[] profiles);
		void removeUserFromList(User c);

		//Form
		HasEditableValue<User> getForm();
		HasClickHandlers getSaveButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		void setUserProfiles(UserProfile[] profiles);
		void setCostCenters(CostCenter[] costCenters);
		boolean isFormValid();
		void lockForm(boolean lock);

		//General
		HasClickHandlers getNewButton();
		HasClickHandlers getRefreshButton();

		void prepareNewUser();
		void removeNewUserPreparation();

		Widget asWidget();
	}

	private UserServiceAsync service;
	private Display view;
	private EventBus eventBus;
	
	private UserProfile[] profiles;
	private CostCenter[] costCenters;
	private User[] users;

	private UserManagementOperation operation;

	private boolean bound = false;

	public UserManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
	}

	public void setService(Service service) {
		this.service = (UserServiceAsync)service;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;

		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		setup();

		container.clear();
		container.add(this.view.asWidget());
	}

	private void setup(){
		users = null;
		costCenters = null;
		profiles = null;
		
		this.service.getUsers(new BigBangAsyncCallback<User[]>() {
			public void onSuccess(User[] result) {
				users = result;
				if(users != null && costCenters!= null && profiles != null)
					doSetup();
			}
		});
		this.service.getUserProfiles(new BigBangAsyncCallback<UserProfile[]>() {

			@Override
			public void onSuccess(UserProfile[] result) {
				profiles = result;
				if(users != null && costCenters!= null && profiles != null)
					doSetup();
			}
		});
		CostCenterService.Util.getInstance().getCostCenterList(new BigBangAsyncCallback<CostCenter[]>() {

			@Override
			public void onSuccess(CostCenter[] result) {
				costCenters = result;
				if(users != null && costCenters!= null && profiles != null)
					doSetup();
			}
		});
	}

	private void doSetup(){
		view.setCostCenters(costCenters);
		view.setUserProfiles(profiles);
		
		view.clearList();
		view.getForm().setValue(null);
		view.addValuesToList(users, profiles);
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				GWT.log("selection changed");

				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.getForm().setValue(null);
					return;
				}			

				for(Selectable s : selected) {
					@SuppressWarnings("unchecked")
					ValueSelectable<User> vs = (ValueSelectable<User>) s;
					User value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(true);
					if(value.id != null){
						view.removeNewUserPreparation();
					}
				}
			}
		});
		view.getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.prepareNewUser();
				for(Selectable s : view.getList().getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<User> vs = (ValueSelectable<User>) s;
					User value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(false);
					break;
				}
			}
		});
		view.getEditButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//TODO checkPermission
				view.getForm().setReadOnly(false);
			}
		});
		view.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(!view.isFormValid())
					return;
				User value = view.getForm().getValue();
				if(value.id == null)
					createUser(value);
				else
					saveUser(value);
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(view.getForm().getValue().id == null)
					view.removeNewUserPreparation();
				else
					deleteUser(view.getForm().getValue());
			}
		});
		view.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				setup();
			}
		});
	}
	public void createUser(User c) {
		service.addUser(c, new BigBangAsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				for(ValueSelectable<User> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	public void saveUser(User c) {
		service.saveUser(c, new BigBangAsyncCallback<User>() {

			@Override
			public void onSuccess(User result) {
				for(ValueSelectable<User> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}

	public void deleteUser(final User c) {
		//TODO alert
		service.deleteUser(c.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.removeUserFromList(c);
				view.getList().clearSelection();
				view.getForm().setValue(null);
			}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (UserManagementOperation) o;
	}

	public Operation getOperation() {
		return operation;
	}

	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOperationPermission(boolean permission) {
		this.operation.setPermission(permission);
		this.setReadOnly(permission);
	}

	private void setReadOnly(boolean permission) {
		// TODO Auto-generated method stub

	}

}
