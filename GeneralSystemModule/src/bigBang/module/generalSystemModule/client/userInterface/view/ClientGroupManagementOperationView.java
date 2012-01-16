package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.ClientGroup;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.ClientGroupList;
import bigBang.module.generalSystemModule.client.userInterface.ClientGroupListEntry;
import bigBang.module.generalSystemModule.client.userInterface.ClientGroupOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.ClientGroupManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ClientGroupManagementOperationView extends View implements ClientGroupManagementOperationViewPresenter.Display{

	protected static final int LIST_WIDTH = 400; //PX
	
	protected ClientGroupFormView groupForm;
	protected ClientGroupList groupList;
	protected ClientGroupOperationsToolbar toolbar;
	private ToolButton newButton;
	protected ActionInvokedEventHandler<ClientGroupManagementOperationViewPresenter.Action> actionHandler;
	
	public ClientGroupManagementOperationView(){
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		groupList = new ClientGroupList();
		this.newButton = (ToolButton) groupList.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {

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

		this.toolbar = new ClientGroupOperationsToolbar() {

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

			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ClientGroupManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		};
		formWrapper.add(toolbar);
		formWrapper.setCellHeight(toolbar, "21px");

		groupForm = new ClientGroupFormView();
		formWrapper.add(groupForm);

		wrapper.add(formWrapper);
	}
	
	@Override
	protected void initializeView() {
		return;
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
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public void prepareNewClientGroup(ClientGroup group) {
		for(ValueSelectable<ClientGroup> s : this.groupList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		ClientGroupListEntry entry = new ClientGroupListEntry(group);
		this.groupList.add(0, entry);
		this.groupList.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}

	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.toolbar.setSaveModeEnabled(enabled);
	}

	@Override
	public void removeFromList(ValueSelectable<ClientGroup> selectable) {
		this.groupList.remove(selectable);
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

}
