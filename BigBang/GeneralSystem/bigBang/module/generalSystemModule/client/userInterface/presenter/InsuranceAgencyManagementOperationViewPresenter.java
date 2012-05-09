package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.InsuranceAgencyBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.InsuranceAgency;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
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

public class InsuranceAgencyManagementOperationViewPresenter implements ViewPresenter {

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
		HasValueSelectables<InsuranceAgency> getList();
		void removeFromList(ValueSelectable<InsuranceAgency> selectable);
		HasValueSelectables<Contact> getContactsList();
		HasValueSelectables<Document> getDocumentsList();

		//Form
		HasEditableValue<InsuranceAgency> getForm();
		boolean isFormValid();

		//General
		void registerActionInvokedHandler(ActionInvokedEventHandler<Action> handler);
		void prepareNewInsuranceAgency(InsuranceAgency agency);

		//PERMISSIONS
		void clearAllowedPermissions();
		void allowCreate(boolean allow);
		void allowEdit(boolean allow);
		void allowDelete(boolean allow);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected InsuranceAgencyBroker insuranceAgencyBroker;
	
	public InsuranceAgencyManagementOperationViewPresenter(View view){
		this.insuranceAgencyBroker = ((InsuranceAgencyBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.INSURANCE_AGENCY));
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
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
		String agencyId = parameterHolder.getParameter("companyid");
		agencyId = agencyId == null ? new String() : agencyId;

		if(inInsuranceAgencyCreation()){
			clearNewInsuranceAgency();
		}
		
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCompanies);
		view.allowCreate(hasPermissions);
		view.allowEdit(hasPermissions);
		view.allowDelete(hasPermissions);

		if(agencyId.isEmpty()){
			clearView();
		}else if(agencyId.equalsIgnoreCase("new")){
			setupNewInsuranceAgency();
		}else{
			showInsuranceAgency(agencyId);
		}
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<InsuranceAgency> selected = (ValueSelectable<InsuranceAgency>) event.getFirstSelected();
				InsuranceAgency selectedValue = selected == null ? null : selected.getValue();
				String agencyId = selectedValue == null ? null : selectedValue.id;
				agencyId = agencyId == null ? new String() : agencyId;

				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				if(agencyId.isEmpty()){
					item.removeParameter("companyid");
				}else{
					item.setParameter("companyid", agencyId);
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
					item.setParameter("companyid", "new");
					NavigationHistoryManager.getInstance().go(item);
					break;
				case DELETE:
					deleteInsuranceAgency(view.getForm().getValue());
					break;
				case EDIT:
					view.getForm().setReadOnly(false);
					view.setSaveModeEnabled(true);
					break;
				case SAVE:
					InsuranceAgency info = view.getForm().getInfo();
					view.getForm().setReadOnly(true);
					if(info.id.equalsIgnoreCase("new"))
						createInsuranceAgency(info);
					else
						saveInsuranceAgency(info);
					break;
				case CANCEL_EDIT:
					if(inInsuranceAgencyCreation()){
						deleteInsuranceAgency(view.getForm().getValue());
					}else{
						NavigationHistoryManager.getInstance().reload();
					}
					break;
				case REFRESH:
					onRefresh();
					break;
				default:
					break;
				}
			}
		});
		
		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Contact> selected = (ValueSelectable<Contact>) event.getFirstSelected();
				Contact item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "contactmanagement");
					navItem.setParameter("ownerid", view.getForm().getValue().id);
					navItem.setParameter("contactid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<Document> selected = (ValueSelectable<Document>) event.getFirstSelected();
				Document item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.setParameter("show", "documentmanagement");
					navItem.setParameter("ownerid", item.ownerId);
					navItem.setParameter("documentid", itemId);
					navItem.setParameter("ownertypeid", BigBangConstants.EntityIds.CLIENT);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});
		
		bound = true;
	}

	private void setup(){
		this.insuranceAgencyBroker.requireDataRefresh();
		this.insuranceAgencyBroker.getInsuranceAgencies(new ResponseHandler<InsuranceAgency[]>() {

			@Override
			public void onResponse(InsuranceAgency[] response) {
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetInsuranceAgencyListFailed();
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

	private void setupNewInsuranceAgency(){
		boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCompanies);
		if(hasPermissions){
			InsuranceAgency agency = new InsuranceAgency();
			agency.id = "new";
			agency.name = "Nova Seguradora";
			view.getList().clearSelection();
			view.prepareNewInsuranceAgency(agency);
			view.getForm().setValue(agency);
	
			view.allowDelete(hasPermissions);
			view.allowEdit(hasPermissions);
			view.setSaveModeEnabled(hasPermissions);
			view.getForm().setReadOnly(!hasPermissions);
		}else{
			GWT.log("User does not have the required permissions");
		}
	}

	private void clearNewInsuranceAgency(){
		if(inInsuranceAgencyCreation()){
			for(ValueSelectable<InsuranceAgency> selected : view.getList().getAll()){
				InsuranceAgency agency = selected.getValue();
				if(agency == null || agency.id.equalsIgnoreCase("new")){
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

	private boolean inInsuranceAgencyCreation(){
		for(ValueSelectable<InsuranceAgency> selected : view.getList().getAll()){
			InsuranceAgency agency = selected.getValue();
			if(agency == null || agency.id.equalsIgnoreCase("new")){
				return true;
			}
		}
		return false;
	}

	private void showInsuranceAgency(String agencyId){
		//Selects the user in list
		for(ValueSelectable<InsuranceAgency> entry : view.getList().getAll()){
			InsuranceAgency listInsuranceAgency = entry.getValue();
			if(listInsuranceAgency.id.equalsIgnoreCase(agencyId) && !entry.isSelected()){
				entry.setSelected(true, true);
			}
		}
		//Gets the user to show
		this.insuranceAgencyBroker.getInsuranceAgency(agencyId, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				view.clearAllowedPermissions();

				boolean hasPermissions = PermissionChecker.hasPermission(SessionGeneralSystem.getInstance(), ModuleConstants.OpTypeIDs.ManageCompanies);
				view.allowEdit(hasPermissions);
				view.allowDelete(hasPermissions);

				view.setSaveModeEnabled(false);
				view.getForm().setValue(response);
				view.getForm().setReadOnly(true);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetInsuranceAgencyFailed();
			}
		});
	}

	public void createInsuranceAgency(InsuranceAgency c) {
		c.id = null;
		this.insuranceAgencyBroker.addInsuranceAgency(c, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("companyid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Seguradora criada com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onCreateInsuranceAgencyFailed();
			}
		});
	}

	public void saveInsuranceAgency(InsuranceAgency c) {
		this.insuranceAgencyBroker.updateInsuranceAgency(c, new ResponseHandler<InsuranceAgency>() {

			@Override
			public void onResponse(InsuranceAgency response) {
				NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
				item.setParameter("companyid", response.id);
				NavigationHistoryManager.getInstance().go(item);
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Seguradora guardada com sucesso."), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onSaveInsuranceAgencyFailed();
			}
		});
	}

	public void deleteInsuranceAgency(final InsuranceAgency c) {
		if(c.id.equalsIgnoreCase("new")){
			clearNewInsuranceAgency();
		}else{
			this.insuranceAgencyBroker.removeInsuranceAgency(c.id, new ResponseHandler<InsuranceAgency>() {

				@Override
				public void onResponse(InsuranceAgency response) {
					NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
					item.removeParameter("companyid");
					NavigationHistoryManager.getInstance().go(item);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Seguradora eliminada com sucesso."), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					onDeleteInsuranceAgencyFailed();
				}
			});
		}
	}	

	private void onRefresh(){
		insuranceAgencyBroker.requireDataRefresh();
		NavigationHistoryManager.getInstance().reload();
	}
	
	private void onGetInsuranceAgencyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a seguradora seleccionada"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("companyid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetInsuranceAgencyListFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível obter a lista de seguradoras"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCreateInsuranceAgencyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível criar a seguradora"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onSaveInsuranceAgencyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível guardar as alterações à seguradora"), TYPE.ALERT_NOTIFICATION));
		view.getForm().setReadOnly(false);
	}

	private void onDeleteInsuranceAgencyFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não foi possível eliminar a seguradora"), TYPE.ALERT_NOTIFICATION));
	}

}
