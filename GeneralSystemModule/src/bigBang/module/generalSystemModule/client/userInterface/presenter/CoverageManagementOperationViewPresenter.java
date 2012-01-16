package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CoverageManagementOperationViewPresenter implements ViewPresenter {
	
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

	private Display view;
	private boolean bound = false;
	
	public CoverageManagementOperationViewPresenter(Display view) {
		setView((View) view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}
	
	public void bind() {
		if(bound){return;}
		
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
		
		bound = true;
	}

}
