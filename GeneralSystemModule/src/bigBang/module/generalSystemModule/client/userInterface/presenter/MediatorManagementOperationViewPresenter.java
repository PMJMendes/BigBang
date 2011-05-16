package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.ContactManager;
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
import bigBang.library.interfaces.TipifiedListService;
import bigBang.library.shared.TipifiedListItem;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;
import bigBang.module.generalSystemModule.shared.CommissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;
import bigBang.module.generalSystemModule.shared.ModuleConstants;
import bigBang.module.generalSystemModule.shared.operation.MediatorManagementOperation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MediatorManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		//List
		HasValueSelectables<Mediator> getList();
		void clearList();
		void addValuesToList(Mediator[] result);
		void removeMediatorFromList(Mediator c);
		
		//Form
		HasEditableValue<Mediator> getForm();
		HasClickHandlers getSaveButton();
		HasClickHandlers getEditButton();
		HasClickHandlers getDeleteButton();
		boolean isFormValid();
		void lockForm(boolean lock);
		
		//Contacts
		void setContactManager(ContactManager contactManager);
		
		//General
		HasClickHandlers getNewButton();
		HasClickHandlers getRefreshButton();
		
		void prepareNewMediator();
		void removeNewMediatorPreparation();
		void setCommissionProfiles(CommissionProfile[] profiles);

		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}

	private MediatorServiceAsync service;
	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;
	private ContactManager contactManager;
	
	private MediatorManagementOperation operation;
	
	private boolean bound = false;
	
	private Mediator[] mediators;
	private CommissionProfile[] comissionProfiles;

	public MediatorManagementOperationViewPresenter(EventBus eventBus, Service service, View view){
		setEventBus(eventBus);
		setService(service);
		setView(view);
		contactManager = new ContactManager();
	}

	public void setService(Service service) {
		this.service = (MediatorServiceAsync)service;
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
		view.setContactManager(this.contactManager);
		
		this.service.getMediators(new BigBangAsyncCallback<Mediator[]>() {
			public void onSuccess(Mediator[] result) {
				mediators = result;
				setupGo();
			}
		});
		TipifiedListService.Util.getInstance().getListItems(ModuleConstants.ListIDs.CommissionProfiles, new BigBangAsyncCallback<TipifiedListItem[]>() {

			@Override
			public void onSuccess(TipifiedListItem[] result) {
				comissionProfiles = new CommissionProfile[result.length];
				for(int i = 0; i < result.length; i++) {
					comissionProfiles[i] = new CommissionProfile();
					comissionProfiles[i].id = result[i].id;
					comissionProfiles[i].value = result[i]. value;
				}
				setupGo();
			}
		});
	}
	
	private void setupGo(){
		if(this.comissionProfiles == null || this.mediators == null)
			return;
		
		view.clearList();
		view.getForm().setValue(null);
		view.setCommissionProfiles(this.comissionProfiles);
		view.lockForm(true);
		view.addValuesToList(this.mediators);
		
		this.comissionProfiles = null;
		this.mediators = null;
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.getForm().setValue(null);
					contactManager.setOfflineMode(true);
					return;
				}			
				
				contactManager.setOfflineMode(false);
				for(Selectable s : selected) {
					@SuppressWarnings("unchecked")
					ValueSelectable<Mediator> vs = (ValueSelectable<Mediator>) s;
					Mediator value = vs.getValue();
					view.getForm().setValue(value);
					view.getForm().setReadOnly(true);
					view.lockForm(value == null);
					if(value.id != null){
						view.removeNewMediatorPreparation();
						contactManager.setEntityInfo(value.id, new AsyncCallback<Void>() { //TODO
							
							@Override
							public void onSuccess(Void result) {}
							
							@Override
							public void onFailure(Throwable caught) {}
						});
					}
				}
			}
		});
		view.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.prepareNewMediator();
				for(Selectable s : view.getList().getSelected()) {
					@SuppressWarnings("unchecked")
					ValueSelectable<Mediator> vs = (ValueSelectable<Mediator>) s;
					Mediator value = vs.getValue();
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
				Mediator value = view.getForm().getValue();
				value.contacts = contactManager.getContactsArray();
				if(value.id == null)
					createMediator(value);
				else
					saveMediator(value);
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.getForm().getValue().id == null)
					view.removeNewMediatorPreparation();
				else
					deleteMediator(view.getForm().getValue());
			}
		});
		view.getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				setup();
			}
		});
	}
	public void createMediator(Mediator c) {
		service.createMediator(c, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onSuccess(Mediator result) {
				for(ValueSelectable<Mediator> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	public void saveMediator(Mediator c) {
		service.saveMediator(c, new BigBangAsyncCallback<Mediator>() {

			@Override
			public void onSuccess(Mediator result) {
				for(ValueSelectable<Mediator> s : view.getList().getSelected()){
					s.setValue(result);
					view.getForm().setValue(result);
					view.getForm().setReadOnly(true);
					break;
				}
			}
		});
	}
	
	public void deleteMediator(final Mediator c) {
		//TODO alert
		service.deleteMediator(c.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.removeMediatorFromList(c);
				view.getList().clearSelection();
				view.getForm().setValue(null);
			}
		});
	}	

	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public void setOperation(Operation o) {
		this.operation = (MediatorManagementOperation) o;
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
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	private void setReadOnly(boolean result) {
		view.setReadOnly(result);
	}
	
}
