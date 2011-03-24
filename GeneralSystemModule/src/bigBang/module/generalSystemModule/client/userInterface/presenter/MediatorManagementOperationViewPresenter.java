package bigBang.module.generalSystemModule.client.userInterface.presenter;

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

import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.interfaces.MediatorServiceAsync;
import bigBang.module.generalSystemModule.shared.ComissionProfile;
import bigBang.module.generalSystemModule.shared.Mediator;
import bigBang.module.generalSystemModule.shared.operation.MediatorManagementOperation;

public class MediatorManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		//Mediator Listing
		HasValue<Mediator> getMediatorList();
		void setMediatorListValues(Mediator[] values);
		void removeMediatorListValues(Mediator[] values);
		
		//MediatorInfo
		HasClickHandlers getEditMediatorButton();
		HasClickHandlers getSaveMediatorButton();
		void setMediatorFormEditable(boolean editable);
		Mediator getMediatorInfo();
		boolean isMediatorFormValid();
		void showDetailsForMediator(Mediator mediator);
		void setMediatorComissionProfiles(ComissionProfile[] profiles);
		
		//Mediator Creation
		HasClickHandlers getNewMediatorButton();
		HasClickHandlers getSubmitNewMediatorButton();
		void setNewMediatorFormEditable(boolean editable);
		boolean isNewMediatorFormValid();
		Mediator getNewMediatorInfo();
		void showNewMediatorForm(boolean show);
		void setNewMediatorComissionProfiles(ComissionProfile[] profiles);
		
		//Mediator Deletion
		HasClickHandlers getDeleteMediatorButton();
		void showConfirmDelete(ConfirmationCallback callback);
		
		Widget asWidget();
	}

	private MediatorServiceAsync service;
	private EventBus eventBus;
	private Display view;
	
	private MediatorManagementOperation operation;
	
	private Mediator[] mediatorCache;
	private ComissionProfile[] comissionProfiles;
	
	public MediatorManagementOperationViewPresenter(EventBus eventBus, MediatorServiceAsync service, Display view) {
		this.setEventBus(eventBus);
		this.setService(service);
		this.setView((View)view);
	}
	
	
	@Override
	public void setService(Service service) {
		this.service = (MediatorServiceAsync) service;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		
		this.loadData();
		view.setMediatorFormEditable(false);
		
		container.clear();
		container.add(this.view.asWidget());
	}
	
	private void loadData(){
		service.getComissionProfiles(new AsyncCallback<ComissionProfile[]>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not fetch the comission profiles");
			}

			@Override
			public void onSuccess(ComissionProfile[] result) {
				view.setMediatorComissionProfiles(result);
				view.setNewMediatorComissionProfiles(result);
				service.getMediators(new AsyncCallback<Mediator[]>() {
					
					@Override
					public void onSuccess(Mediator[] result) {
						mediatorCache = result;
						view.setMediatorListValues(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Could not fetch the mediator list");
					}
				});
			}
		});
	}

	@Override
	public void bind() {
		view.getMediatorList().addValueChangeHandler(new ValueChangeHandler<Mediator>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Mediator> event) {
				view.showDetailsForMediator(event.getValue());
			}
		});
		view.getEditMediatorButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//TODO PERMISSIONS
				view.setMediatorFormEditable(true);
			}
		});
		view.getSaveMediatorButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.isMediatorFormValid()){
					saveMediator(view.getMediatorInfo());
					view.setMediatorFormEditable(false);
				}
			}
		});
		view.getNewMediatorButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.showNewMediatorForm(true);
				view.setNewMediatorFormEditable(true);
			}
		});
		view.getSubmitNewMediatorButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(view.isNewMediatorFormValid())
					createMediator(view.getNewMediatorInfo());
			}
		});
		view.getDeleteMediatorButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.showConfirmDelete(new ConfirmationCallback() {
					
					@Override
					public void onResult(boolean result) {
						if(result)
							deleteMediator(view.getMediatorInfo());
					}
				});
			}
		});
	}
	
	protected void deleteMediator(final Mediator mediator){
		service.deleteMediator(mediator.id, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not delete the mediator");
			}

			@Override
			public void onSuccess(String result) {
				view.removeMediatorListValues(new Mediator[]{mediator});
			}
		});
	}

	protected void createMediator(Mediator newMediatorInfo) {
		view.showNewMediatorForm(false);
	}


	protected void saveMediator(Mediator mediatorInfo) {
		view.setMediatorFormEditable(false);
	}


	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (MediatorManagementOperation) o;
	}

	@Override
	public Operation getOperation() {
		return this.operation;
	}

	@Override
	public void goCompact(HasWidgets container) {
		// TODO Auto-generated method stub

	}

	@Override
	public String setTargetEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
