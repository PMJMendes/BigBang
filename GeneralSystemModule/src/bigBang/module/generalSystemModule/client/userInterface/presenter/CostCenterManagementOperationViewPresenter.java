package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CostCenterBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.CostCenter;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.shared.operation.CostCenterManagementOperation;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CostCenterManagementOperationViewPresenter implements
OperationViewPresenter {

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

		//Form
		HasEditableValue<CostCenter> getForm();
		boolean isFormValid();
		void lockForm(boolean lock);

		//Members list
		void showUsersForCostCenterWithId(String costCenterId);
		void clearMembersList();

		//General
		void clear();
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewCostCenter();
		void removeNewCostCenterPreparation();
		void setSaveModeEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;
	protected CostCenterBroker costCenterBroker;

	private CostCenterManagementOperation operation;

	private boolean bound = false;

	public CostCenterManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setView(view);
		this.costCenterBroker = (CostCenterBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.COST_CENTER);
	}

	public void setService(Service service) {}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		bound = true;
		view.clear();

		view.getList().setMultipleSelectability(false);
		view.getForm().setReadOnly(true);
		fetchCostCenterList();

		container.clear();
		container.add(this.view.asWidget());
	}

	private void fetchCostCenterList() {
		//Refreshes The cost centers data (Info automatically propagated to the broker clients)
		this.costCenterBroker.requireDataRefresh();
		this.costCenterBroker.getCostCenters(new ResponseHandler<CostCenter[]>() {

			@Override
			public void onResponse(CostCenter[] response) {}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
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
				if(selectedValue == null){
					view.getForm().setValue(null);
					view.showUsersForCostCenterWithId(null);
					view.lockForm(true);
				}else{
					if(selectedValue.id != null){
						view.removeNewCostCenterPreparation();
						costCenterBroker.getCostCenter(selectedValue.id, new ResponseHandler<CostCenter>() {

							@Override
							public void onResponse(CostCenter value) {
								view.getForm().setValue(value);
								view.showUsersForCostCenterWithId(value.id);
								view.lockForm(value == null);
							}

							@Override
							public void onError(Collection<ResponseError> errors) {}
						});
					}
				}

			}
		});
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<CostCenterManagementOperationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case NEW:
					view.prepareNewCostCenter();
					for(Selectable s : view.getList().getSelected()) {
						@SuppressWarnings("unchecked")
						ValueSelectable<CostCenter> vs = (ValueSelectable<CostCenter>) s;
						CostCenter value = vs.getValue();
						view.getForm().setValue(value);
						view.getForm().setReadOnly(false);
						view.clearMembersList();

						break;
					}
					break;
				case REFRESH:
					fetchCostCenterList();
					break;
				case DELETE:
					if(view.getForm().getValue().id == null)
						view.removeNewCostCenterPreparation();
					else
						deleteCostCenter(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					if(!view.isFormValid())
						return;
					CostCenter info = view.getForm().getInfo();
					if(info.id == null)
						createCostCenter(info);
					else
						saveCostCenter(info);
					break;
				case CANCEL_EDIT:
					if(view.getForm().getValue().id == null){
						view.removeNewCostCenterPreparation();
					}else{
						view.getForm().revert();
						view.getForm().setReadOnly(true);
					}
					break;
				default:
					break;
				}
			}
		});
	}
	public void createCostCenter(CostCenter c) {
		costCenterBroker.addCostCenter(c, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				for(ValueSelectable<CostCenter> s : view.getList().getSelected()){
					s.setValue(response);
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);
					break;
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}
	public void saveCostCenter(CostCenter c) {
		costCenterBroker.updateCostCenter(c, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				for(ValueSelectable<CostCenter> s : view.getList().getSelected()){
					s.setValue(response);
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					view.setSaveModeEnabled(false);
					break;
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}

	public void deleteCostCenter(final CostCenter c) {
		costCenterBroker.removeCostCenter(c.id, new ResponseHandler<CostCenter>() {

			@Override
			public void onResponse(CostCenter response) {
				view.getList().clearSelection();
				view.clearMembersList();
				view.getForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (CostCenterManagementOperation) o;
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

	private void setReadOnly(boolean result) {
		view.setReadOnly(result);
	}

}
