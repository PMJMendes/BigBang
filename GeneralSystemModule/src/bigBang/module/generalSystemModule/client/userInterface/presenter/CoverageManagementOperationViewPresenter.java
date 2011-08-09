package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
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
import bigBang.module.generalSystemModule.shared.operation.CoverageManagementOperation;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class CoverageManagementOperationViewPresenter implements
		OperationViewPresenter {
	
	public interface Display {
		//Lists
		HasValueSelectables<Line> getLineList();
		HasValueSelectables<SubLine> getSubLineList();
		HasValueSelectables<Coverage> getCoverageList();		
		void showSubLinesFor(Line line);
		void showCoveragesFor(SubLine subLine);
		
		//General
		void refresh();
		void setReadOnly(boolean readonly);
		void clear();
		Widget asWidget();
	}

	@SuppressWarnings("unused")
	private EventBus eventBus;
	@SuppressWarnings("unused")
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
		
		view.clear();
		setup();
		
		container.clear();
		container.add(view.asWidget());
	}
	
	public void setup(){
		view.refresh();
	}

	@Override
	public void bind() {
		view.getLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.showSubLinesFor(null);
					return;
				}
					
				for(Selectable s : selected) {
					view.showSubLinesFor(((ValueSelectable<Line>)s).getValue());
					break;
				}
			}
		});
		view.getSubLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.showCoveragesFor(null);
					return;
				}
				
				for(Selectable s : selected) {
					view.showCoveragesFor(((ValueSelectable<SubLine>)s).getValue());
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
		setReadOnly(!result);
	}

	private void setReadOnly(boolean result) {
		view.setReadOnly(result);
	}

}
