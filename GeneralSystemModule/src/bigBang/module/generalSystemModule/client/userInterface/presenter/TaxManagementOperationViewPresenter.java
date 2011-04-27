package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.Operation;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.client.userInterface.view.TaxManagementOperationView;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.Tax;
import bigBang.module.generalSystemModule.shared.operation.TaxManagementOperation;

public class TaxManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		void setLines(Line[] lines);
		
		//Buttons
		HasClickHandlers getNewButton();
		HasClickHandlers getSaveButton();
		HasClickHandlers getDeleteButton();
	
		String getCurrentCoverageId();
	
		//Form
		HasValue<Tax> getTaxForm();
		void showForm(boolean show);
		void lockForm(boolean lock);
		
		//List
		void removeTaxFromList(Tax tax);
		void addTaxToList(Tax tax);
		void updateTaxInList(Tax tax);		
		
		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}
	
	private CoveragesServiceAsync service;
	private Display view;
	@SuppressWarnings("unused")
	private EventBus eventBus;
	
	private TaxManagementOperation operation;
	private boolean bound = false;

	public TaxManagementOperationViewPresenter(EventBus eventBus,
			Service coveragesService,
			TaxManagementOperationView view) {
		setEventBus(eventBus);
		setService(coveragesService);
		setView((View) view);
	}

	@Override
	public void setService(Service service) {
		this.service = (CoveragesServiceAsync) service;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void setView(View view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		if(!bound)
			bind();
		bound = true;

		setup();
		
		container.clear();
		container.add(this.view.asWidget());
	}
	
	public void setup(){
		this.service.getLines(new BigBangAsyncCallback<Line[]>() {

			@Override
			public void onSuccess(Line[] result) {
				view.setLines(result);
			}
		});
	}

	@Override
	public void bind() {
		view.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				view.getTaxForm().setValue(null);
				view.lockForm(false);
				view.showForm(true);
			}
		});
		view.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Tax value = view.getTaxForm().getValue();
				if(value.id == null)
					createTax(value);
				else 
					saveTax(value);
			}
		});
		view.getDeleteButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Tax tax = view.getTaxForm().getValue();
				if(tax.id == null)
					view.showForm(false);
				else
					deleteTax(tax);
			}
		});
	}
	
	private void createTax(Tax tax) {
		service.createTax(tax, new BigBangAsyncCallback<Tax>() {

			@Override
			public void onSuccess(Tax result) {
				view.addTaxToList(result);
				view.showForm(false);
			}
		});
	}
	
	private void saveTax(Tax tax) {
		service.saveTax(tax, new BigBangAsyncCallback<Tax>() {

			@Override
			public void onSuccess(Tax result) {
				view.getTaxForm().setValue(result);
				view.showForm(false);
			}
		});
	}

	private void deleteTax(final Tax tax) {
		service.deleteTax(tax.id, new BigBangAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.showForm(false);
				view.removeTaxFromList(tax);
			}
		});
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		this.operation = (TaxManagementOperation) o;
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

	@Override
	public void setOperationPermission(boolean hasPermissionForOperation) {
		setReadOnly(!hasPermissionForOperation);
	}
	
	public void setReadOnly(boolean readOnly) {
		this.view.setReadOnly(readOnly);
	}

}
