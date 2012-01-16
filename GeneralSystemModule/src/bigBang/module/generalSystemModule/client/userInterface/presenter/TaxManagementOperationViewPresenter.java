package bigBang.module.generalSystemModule.client.userInterface.presenter;

import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.Tax;
import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class TaxManagementOperationViewPresenter implements ViewPresenter {
	
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
		
		//GENERAL
		void clear();
		
		//List
		void removeTaxFromList(Tax tax);
		void addTaxToList(Tax tax);
		void updateTaxInList(Tax tax);		
		
		void setReadOnly(boolean readOnly);
		Widget asWidget();
	}
	
	private Display view;
	private boolean bound = false;

	public TaxManagementOperationViewPresenter(Display view) {
		setView((UIObject) view);
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
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		//TODO
	}
	
	public void bind() {
		if(bound){return;}

//		view.getNewButton().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				view.getTaxForm().setValue(null);
//				view.lockForm(false);
//				view.showForm(true);
//			}
//		});
//		view.getSaveButton().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				Tax value = view.getTaxForm().getValue();
//				if(value.id == null){
//					value.coverageId = view.getCurrentCoverageId();
//					createTax(value);
//				}else 
//					saveTax(value);
//			}
//		});
//		view.getDeleteButton().addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				Tax tax = view.getTaxForm().getValue();
//				if(tax.id == null)
//					view.showForm(false);
//				else
//					deleteTax(tax);
//			}
//		});
		
		bound = true;
	}
//
//	private void createTax(Tax tax) {
//		service.createTax(tax, new BigBangAsyncCallback<Tax>() {
//
//			@Override
//			public void onSuccess(Tax result) {
//				view.addTaxToList(result);
//				view.showForm(false);
//			}
//		});
//	}
//	
//	private void saveTax(Tax tax) {
//		service.saveTax(tax, new BigBangAsyncCallback<Tax>() {
//
//			@Override
//			public void onSuccess(Tax result) {
//				view.getTaxForm().setValue(result);
//				view.showForm(false);
//			}
//		});
//	}
//
//	private void deleteTax(final Tax tax) {
//		service.deleteTax(tax.id, new BigBangAsyncCallback<Void>() {
//
//			@Override
//			public void onSuccess(Void result) {
//				view.showForm(false);
//				view.removeTaxFromList(tax);
//			}
//		});
//	}

}
