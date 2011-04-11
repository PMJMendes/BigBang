package bigBang.module.generalSystemModule.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Operation;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.OperationViewPresenter;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.interfaces.Service;
import bigBang.module.generalSystemModule.interfaces.CoveragesServiceAsync;
import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;
import bigBang.module.generalSystemModule.shared.CoverageTax;
import bigBang.module.generalSystemModule.shared.operation.CoverageManagementOperation;

public class CoverageManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		//Lists
		HasValueSelectables<CoverageCategory> getCategoryList();
		HasValueSelectables<CoverageBranch> getBranchList();
		HasValueSelectables<CoverageModality> getModalityList();
		HasValueSelectables<CoverageTax> getTaxList();		
		
		void setCategories(CoverageCategory[] c);
		void setBranches(CoverageBranch[] b);
		void setModalities(CoverageModality[] m);
		void setTaxes(CoverageTax[] t);
		
		Widget asWidget();
	}

	private EventBus eventBus;
	private CoveragesServiceAsync service;
	private Display view;
	
	private boolean bound = false;
	
	private CoverageManagementOperation operation;
	
	public CoverageManagementOperationViewPresenter(EventBus eventBus, Service service, Display view) {
		setView((View) view);
		setEventBus(eventBus);
		setService(service);
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
		container.add(view.asWidget());
	}
	
	public void setup(){
		this.service.getCoverages(new BigBangAsyncCallback<CoverageCategory[]>() {

			@Override
			public void onSuccess(CoverageCategory[] categories) {
				view.setCategories(categories);
			}
		});
	}

	@Override
	public void bind() {
		view.getCategoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				java.util.Collection<? extends Selectable> selected = event.getSelected();
				for(Selectable s : selected) {
					view.setBranches(((ValueSelectable<CoverageCategory>) s).getValue().branches);
					break;
				}
			}
		});
		view.getBranchList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				java.util.Collection<? extends Selectable> selected = event.getSelected();
				for(Selectable s : selected) {
					view.setModalities(((ValueSelectable<CoverageBranch>) s).getValue().modalities);
					break;
				}
			}
		});
		view.getModalityList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				java.util.Collection<? extends Selectable> selected = event.getSelected();
				for(Selectable s : selected) {
					view.setTaxes(((ValueSelectable<CoverageModality>) s).getValue().taxes);
					break;
				}
			}
		});
	}

	@Override
	public void registerEventHandlers(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOperation(Operation o) {
		operation = (CoverageManagementOperation) o;
	}

	@Override
	public Operation getOperation() {
		return operation;
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
	public void setOperationPermission(boolean result) {
		this.operation.setPermission(result);
		setReadOnly(result);
	}

	private void setReadOnly(boolean result) {
		// TODO Auto-generated method stub
		
	}

}
