package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.ClientGroupList;
import bigBang.module.generalSystemModule.client.userInterface.ClientGroupListEntry;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientGroupManagementOperationView extends View implements ClientGroupManagementOperationViewPresenter.Display{

	protected static final int LIST_WIDTH = 400; //PX
	
	protected ClientGroupFormView groupForm;
	protected ClientGroupList groupList;
	protected BigBangOperationsToolBar toolbar;
	protected ActionInvokedEventHandler<ClientGroupManagementOperationViewPresenter.Action> actionHandler;
	
	public ClientGroupManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		groupList = new ClientGroupList();
		groupList.getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.NEW));
			}
		});
		groupList.getRefreshButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});

		groupList.setSize("100%", "100%");
		wrapper.addWest(groupList, LIST_WIDTH);
		wrapper.setWidgetMinSize(groupList, LIST_WIDTH);

		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");

		this.toolbar = new BigBangOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		toolbar.addItem(SUB_MENU.ADMIN, new MenuItem("Apagar", new Command() {

			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		}));

		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		groupForm = new ClientGroupFormView();
		formWrapper.add(groupForm);



		wrapper.add(formWrapper);

		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<ClientGroup> getList() {
		return this.groupList;
	}

	@Override
	public HasEditableValue<ClientGroup> getForm() {
		return this.groupForm;
	}

	@Override
	public boolean isFormValid() {
		return this.groupForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.groupForm.setReadOnly(true);
		toolbar.setSaveModeEnabled(false);
	}

	@Override
	public void clear() {
		this.groupForm.clearInfo();
		this.groupList.clear();
		this.groupList.clearFilters();
	}

	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewClientGroup() {
		for(ValueSelectable<ClientGroup> s : this.groupList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		ClientGroupListEntry entry = new ClientGroupListEntry(new ClientGroup());
		this.groupList.add(entry);
		this.groupList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}

	@Override
	public void removeNewClientGroupPreparation() {
		for(ValueSelectable<ClientGroup> s : this.groupList){
			if(s.getValue().id == null){
				this.groupList.remove(s);
				break;
			}
		}
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}
	
}
