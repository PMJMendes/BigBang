package bigBang.module.generalSystemModule.client.userInterface.view;

import org.gwt.mosaic.ui.client.ToolButton;

import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterList;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterListEntry;
import bigBang.module.generalSystemModule.client.userInterface.CostCenterOperationsToolbar;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CostCenterManagementOperationViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CostCenterManagementOperationView extends View implements CostCenterManagementOperationViewPresenter.Display {

	private static final int LIST_WIDTH = 400; //px

	private CostCenterList costCenterList;
	private CostCenterForm costCenterForm;
	private ToolButton newButton;
	private CostCenterOperationsToolbar toolbar;
	
	protected ActionInvokedEventHandler<CostCenterManagementOperationViewPresenter.Action> actionHandler; 
	
	public CostCenterManagementOperationView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		costCenterList = new CostCenterList();
		
		this.newButton = (ToolButton) costCenterList.getNewButton();
		this.newButton.addClickHandler(new ClickHandler() {
			
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

		VerticalPanel previewWrapper = new VerticalPanel();
		previewWrapper.setSize("100%", "100%");
		
		this.toolbar = new CostCenterOperationsToolbar() {
			
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
			
			@Override
			public void onDelete() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<CostCenterManagementOperationViewPresenter.Action>(Action.DELETE));
			}
		};
		
		previewWrapper.add(toolbar);
		previewWrapper.setCellHeight(toolbar, "21px");
		
		costCenterForm = new CostCenterForm();
		costCenterForm.setSize("100%", "100%");
		previewWrapper.add(costCenterForm);
		previewWrapper.setCellHeight(costCenterForm, "100%");
		
		wrapper.add(previewWrapper);
	}

	@Override
	protected void initializeView() {
		return;
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
	public void prepareNewCostCenter(CostCenter costCenter) {
		for(ValueSelectable<CostCenter> s : this.costCenterList){
			if(s.getValue().id == null){
				s.setSelected(true, true);
				return;
			}
		}
		CostCenterListEntry entry = new CostCenterListEntry(costCenter);
		this.costCenterList.add(0, entry);
		this.costCenterList.getScrollable().scrollToTop();
		entry.setSelected(true, false);
	}

	@Override
	public boolean isFormValid() {
		return this.costCenterForm.validate();
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
	public void removeFromList(ValueSelectable<CostCenter> selectable) {
		this.costCenterList.remove(selectable);
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
