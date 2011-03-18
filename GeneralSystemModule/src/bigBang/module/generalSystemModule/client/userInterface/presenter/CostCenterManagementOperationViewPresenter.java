package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.ArrayList;

import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;

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

import bigBang.library.shared.EventBus;
import bigBang.library.shared.Operation;
import bigBang.library.shared.Service;
import bigBang.library.shared.userInterface.presenter.OperationViewPresenter;
import bigBang.library.shared.userInterface.view.View;
import bigBang.module.generalSystemModule.client.CostCenterServiceAsync;
import bigBang.module.generalSystemModule.shared.CostCenter;
import bigBang.module.generalSystemModule.shared.CostCenterManagementOperation;
import bigBang.module.generalSystemModule.shared.User;

public class CostCenterManagementOperationViewPresenter implements
OperationViewPresenter {

	public interface Display {
		HasValue<String> getCostCenterList();
		String[] getSelectedMembers();
		void removeMembers(String[] memberIds);
		void showConfirmRemoveMember(ConfirmationCallback callback);

		HasClickHandlers getAddMemberButton();
		HasClickHandlers getremoveMemberButton();

		HasValue<String> getName();
		HasValue<String> getCode();
		void showDetailsForCostCenter(CostCenter costCenter);
		Widget asWidget();
		void setCostCenterEntries(CostCenter[] result);

		void showUsersForMembership(String costCenterId, User[] availableUsers);
		HasClickHandlers getAddMemberSubmitButton();
		String[] getSelectedUsersForMembership();
		void updateCostCenterInfo(CostCenter result);
	}

	private CostCenterServiceAsync service;
	private Display view;
	private EventBus eventBus;

	private CostCenterManagementOperation operation;
	private CostCenter[] costCenterCache;

	public CostCenterManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
	}

	public void setService(Service service) {
		this.service = (CostCenterServiceAsync)service;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setView(View view) {
		this.view = (Display)view;
	}

	public void go(HasWidgets container) {
		bind();
		fetchCostCenterList();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void fetchCostCenterList() {
		try{
			this.service.getCostCenterList(new AsyncCallback<CostCenter[]>() {

				public void onSuccess(CostCenter[] result) {
					costCenterCache = result;
					view.setCostCenterEntries(result);
				}

				public void onFailure(Throwable caught) {
					GWT.log("Error while fetching cost center list : " + caught.getMessage());
				}
			});
		}catch(Exception e){
			GWT.log("Error while fetching cost center list");
		}
	}

	public void bind() {
		this.view.getCostCenterList().addValueChangeHandler(new ValueChangeHandler<String>() {

			public void onValueChange(ValueChangeEvent<String> event) {
				showCostCenterDetails(event.getValue());
			}
		});
		this.view.getAddMemberButton().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				addMember(view.getCostCenterList().getValue());
			}
		});
		this.view.getremoveMemberButton().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeMembers(view.getCostCenterList().getValue(), view.getSelectedMembers());
			}
		});
		this.view.getAddMemberSubmitButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				associateUsersToCostCenter(view.getCostCenterList().getValue(), view.getSelectedUsersForMembership());
			}
		});

	}
	
	public void associateUsersToCostCenter(final String costCenterId, String[] userIds){
		service.addMembers(costCenterId, userIds, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not associate users");
			}

			@Override
			public void onSuccess(String result) {
				updateCostCenter(costCenterId);
			}
			
		});
	}
	
	private void updateCostCenter(String id){
		service.getCostCenter(id, new AsyncCallback<CostCenter>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not update cost center");
			}

			@Override
			public void onSuccess(CostCenter result) {
				for(int i = 0; i < costCenterCache.length; i++) {
					if(costCenterCache[i].id.equals(result.id)){
						costCenterCache[i] = result;
						break;
					}
				}
				view.updateCostCenterInfo(result);
			}
		});
	}

	private void addMember(final String costCenterId){
		service.getAvailableUsersForMembership(costCenterId, new AsyncCallback<User[]>() {

			public void onFailure(Throwable caught) {
				GWT.log("Could not fetch users for membership");
			}

			public void onSuccess(User[] result) {
				view.showUsersForMembership(costCenterId, result);
			}

		});
	}

	private void removeMembers(final String costCenterId, final String[] memberIds){
		view.showConfirmRemoveMember(new ConfirmationCallback() {

			public void onResult(boolean result) {
				if(result) {
					service.removeMember(costCenterId, memberIds, new AsyncCallback<String>() {

						public void onFailure(Throwable caught) {
							GWT.log("Could not remove members");
						}

						public void onSuccess(String result) {
							view.removeMembers(memberIds);
						}

					});
				}
			}
		});
	}

	private void showCostCenterDetails(String costCenterId) {
		for(int i = 0; i < costCenterCache.length; i++) {
			if(costCenterCache[i].id.equals(costCenterId)){
				this.view.showDetailsForCostCenter(costCenterCache[i]);
				break;
			}			
		}
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

}
