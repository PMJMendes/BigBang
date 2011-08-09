package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.CostCenter;
import bigBang.definitions.shared.User;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterMemberList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CostCenterManagementOperationView extends View implements CostCenterManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private CostCenterList costCenterList;
	private CostCenterMemberList memberList;
	private CostCenterForm costCenterForm;
	private UserForm userForm;
	private BigBangOperationsToolBar toolbar;
	
	protected ActionInvokedEventHandler<CostCenterManagementOperationViewPresenter.Action> actionHandler; 
	
	public CostCenterManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		wrapper.setSize("100%", "100%");

		costCenterList = new CostCenterList();
		
		costCenterList.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.NEW));
			}
		});
		costCenterList.getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.REFRESH));
			}
		});
		
		costCenterList.setSize("100%", "100%");
		wrapper.addWest(costCenterList, LIST_WIDTH);
		wrapper.setWidgetMinSize(costCenterList, LIST_WIDTH);

		final VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");
		
		this.toolbar = new BigBangOperationsToolBar() {
			
			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.SAVE));
			}
			
			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.EDIT));
			}
			
			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.CANCEL_EDIT));
			}
		};
		toolbar.hideAll();
		toolbar.showItem(SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.ADMIN, true);
		toolbar.addItem(SUB_MENU.ADMIN, new MenuItem("Apagar", new Command() {
			
			@Override
			public void execute() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		}));
		
		
		previewWrapper.add(toolbar);
		previewWrapper.setCellHeight(toolbar, "21px");
		
		costCenterForm = new CostCenterForm();
		Widget costCenterFormContent = costCenterForm.getNonScrollableContent();
		costCenterFormContent.setSize("100%", "100%");
		previewWrapper.add(costCenterFormContent);
		previewWrapper.setCellHeight(costCenterFormContent, "100px");		
		
		Widget sectionHeader = new FormViewSection("Membros").getHeader();
		previewWrapper.add(sectionHeader);
		previewWrapper.setCellHeight(sectionHeader, "22px");
		
		memberList = new CostCenterMemberList(){
			protected void onCellDoubleClicked(bigBang.library.client.userInterface.ListEntry<User> entry) {
				showUserDetails(entry.getValue());
			};
		};
		memberList.setSize("100%", "100%");
		previewWrapper.add(memberList);
		previewWrapper.setCellHeight(memberList, "100%");
		wrapper.add(previewWrapper);

		userForm = new UserForm();
		
		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<CostCenter> getList() {
		return (HasValueSelectables<CostCenter>)this.costCenterList;
	}
	
	@Override
	public HasEditableValue<CostCenter> getForm() {
		return this.costCenterForm;
	}

	@Override
	public void prepareNewCostCenter() {
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		CostCenterListEntry entry = new CostCenterListEntry(new CostCenter());
		this.costCenterList.add(entry);
		this.costCenterList.getScrollable().scrollToBottom();
		entry.setSelected(true, true);
	}
	
	@Override
	public void removeNewCostCenterPreparation(){
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				this.costCenterList.remove(s);
				break;
			}
		}
	}

	@Override
	public boolean isFormValid() {
		return this.costCenterForm.validate();
	}

	@Override
	public void lockForm(boolean lock) {
		this.costCenterForm.lock(lock);
		this.toolbar.setSaveModeEnabled(false);
	}

	public void showUserDetails(User user) {
		PopupPanel popup = new PopupPanel();
		popup.setWidth("400px");
		userForm.clearInfo();
		userForm.setInfo(user);
		userForm.lock(true);
		Widget formContent = userForm.getNonScrollableContent();
		formContent.setHeight("240px");
		popup.add(formContent);
		popup.center();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		((Button)this.costCenterList.getNewButton()).setEnabled(!readOnly);
		this.costCenterForm.setReadOnly(readOnly);
	}
	
	@Override
	public void clear(){
		this.costCenterForm.clearInfo();
		this.costCenterList.clear();
		this.costCenterList.clearFilters();
		this.memberList.clear();
		this.userForm.clearInfo();
	}

	@Override
	public void showUsersForCostCenterWithId(String costCenterId) {
		this.memberList.setCostCenterId(costCenterId);
	}

	@Override
	public void clearMembersList() {
		this.memberList.setCostCenterId(null);
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
