package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.SessionGeneralSystem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CostCenterManagementOperationViewPresenter implements ViewPresenter {

	public static enum Action{
		NEW,
		REFRESH,
		EDIT,
		SAVE,
		CANCEL_EDIT,
		DELETE
	}

	public interface Display {
		//List
		HasValueSelectables<CostCenter> getList();
		void removeFromList(ValueSelectable<CostCenter> selectable);

		//Form
		HasEditableValue<CostCenter> getForm();
		boolean isFormValid();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewCostCenter(CostCenter costCenter);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	private boolean bound = false;
	private Display view;
	private CostCenterBroker costCenterBroker;

	public CostCenterManagementOperationViewPresenter(View view){
		this.costCenterBroker = (CostCenterBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.COST_CENTER);
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
		setup();
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String costCenterId = parameterHolder.getParameter("costcenterid");
		costCenterId = costCenterId == null ? new String() : costCenterId;

		if(inCostCenterCreation()){
			clearNewCostCenter();
		}
		
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCostCenters);
		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(costCenterId.isEmpty()){
			clearView();
		}else if(costCenterId.equalsIgnoreCase("new")){
			setupNewCostCenter();
		}else{
			showCostCenter(costCenterId);
		}
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<CostCenter> selected = (ValueSelectable<CostCenter>) event.getFirstSelected();
				CostCenter selectedValue = selected == null ? null : selected.getValue();
				String costCenterId = selectedValue == null ? null : selectedValue.id;
				costCenterId = costCenterId == null ? new String() : costCenterId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(costCenterId.isEmpty()){
					item.removeParameter("costcenterid");
				}else{
					item.setParameter("costcenterid", costCenterId);
				}
				NavigationHistoryManager.getInstance().go(item);
			}
		});

		view.registerActionInvokedHandler(new ActionInvokedEventHandler<Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.setParameter("costcenterid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteCostCenter(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					CostCenter info = view.getForm().getInfo();
					view.getForm().setReadOnly(true);
					if(info.id.equalsIgnoreCase("new"))
						createCostCenter(info);
					else
						saveCostCenter(info);
					break;
				case CANCEL_EDIT:
					if(inCostCenterCreation()){
						deleteCostCenter(view.getForm().getValue());
					}else{
						NavigationHistoryManager.getInstance().reload();
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private void setup(){
		this.costCenterBroker.requireDataRefresh();
		this.costCenterBroker.getCostCenters(new ResponseHandler<CostCenter[]>() {

			@Override
			public void onResponse(CostCenter[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetCostCenterListFailed();
			}
		});
	}

	private void clearView(){
		view.setSaveModeEnabled(false);
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.getList().clearSelection();
	}

	private void setupNewCostCenter(){
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCostCenters);
		if(hasPermissions){
			CostCenter costCenter = new CostCenter();
			costCenter.id = "new";
			costCenter.name = "Novo Centro de Custo";
			view.getList().clearSelection();
			view.prepareNewCostCenter(costCenter);
			view.getForm().setValue(costCenter);
	
			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);
			view.setSaveModeEnabled(hasPermissions);
			view.getForm().setReadOnly(!hasPermissions);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewCostCenter(){
		if(inCostCenterCreation()){
			for(ValueSelectable<CostCenter> selected : view.getList().getAll()){
				CostCenter costCenter = selected.getValue();
				if(costCenter == null || costCenter.id.equalsIgnoreCase("new")){
					view.removeFromList(selected);
					break;
				}
			}
			view.clearAllowedPermissions();
			view.getForm().setValue(null);
			view.getForm().setReadOnly(true);
			view.getList().clearSelection();
		}
	}

	private boolean inCostCenterCreation(){
		for(ValueSelectable<CostCenter> selected : view.getList().getAll()){
			CostCenter costCenter = selected.getValue();
			if(costCenter == null || costCenter.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showCostCenter(String costCenterId){
		//Selects the user in list
		for(ValueSelectable<CostCenter> entry : view.getList().getAll()){
			CostCenter listCostCenter = entry.getValue();
			if(listCostCenter.id.equalsIgnoreCase(costCenterId) && !entry.isSelected()){
				entry.setSelected(true, true);
			}
		}
		//Gets the user to show
		this.costCenterBroker.getCostCenter(costCenterId, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCostCenters);
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);
				
				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetCostCenterFailed();
			}
		});
	}

	public void createCostCenter(CostCenter c) {
		c.id = null;
		this.costCenterBroker.addCostCenter(c, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("costcenterid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Centro de Custo criado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateCostCenterFailed();
			}
		});
	}

	public void saveCostCenter(CostCenter c) {
		this.costCenterBroker.updateCostCenter(c, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("costcenterid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Centro de Custo guardado com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveCostCenterFailed();
			}
		});
	}

	public void deleteCostCenter(final CostCenter c) {
		if(c.id.equalsIgnoreCase("new")){
			clearNewCostCenter();
		}else{
			this.costCenterBroker.removeCostCenter(c.id, new ResponseHandler<CostCenter>() {

				@Override
				public void onResponse(CostCenter response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("costcenterid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Centro de Custo eliminado com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteCostCenterFailed();
				}
			});
		}
	}	

	private void onGetCostCenterFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter o Centro de Custo seleccionado"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("costcenterid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetCostCenterListFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de Centros de Custo"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCreateCostCenterFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar o Centro de Custo"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveCostCenterFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações ao Centro de Custo"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onDeleteCostCenterFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar o Centro de Custo"), TYPE.ALERT_NOTIFICATION));
	}

}
