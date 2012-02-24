package bigBang.module.generalSystemModule.client.userInterface.presenter;

import java.util.Collection;

import sun.security.action.GetLongAction;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CoverageManagementOperationViewPresenter implements ViewPresenter {

	private String lineId;
	private String subLineId;
	private String coverageId;

	public interface Display {
		//Lists
		HasValueSelectables<Line> getLineList();
		HasValueSelectables<SubLine> getSubLineList();
		HasValueSelectables<Coverage> getCoverageList();		

		//General
		void refresh();
		void setReadOnly(boolean readonly);
		void clear();
		Widget asWidget();
		void setLine(String lineId);
		void setSubLine(String subLineId, String lineId);
		void setCoverage(String lineId, String subLineId, String coverageId);
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

		view.clear();

		lineId = parameterHolder.getParameter("lineid");
		subLineId = parameterHolder.getParameter("sublineid");
		coverageId = parameterHolder.getParameter("coverageid");


		if(coverageId != null){

			view.setCoverage(lineId, subLineId, coverageId);

		}else if(subLineId != null){
			view.setSubLine(subLineId, lineId);
		}
		else
			view.setLine(lineId);
	}

	public void bind() {
		if(bound){return;}

		view.getLineList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.getLineList().clearSelection();
					return;
				}

				for(Selectable s : selected) {
					ValueSelectable<Line> vs = (ValueSelectable<Line>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineId", vs.getValue().id);
					navig.removeParameter("sublineid");
					navig.removeParameter("coverageid");
					NavigationHistoryManager.getInstance().go(navig);
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
					view.getSubLineList().clearSelection();
				}

				for(Selectable s : selected) {
					ValueSelectable<SubLine> vs = (ValueSelectable<SubLine>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineId", vs.getValue().lineId);
					navig.setParameter("sublineid", vs.getValue().id);
					navig.removeParameter("coverageid");
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}
			}
		});
		view.getCoverageList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				Collection<? extends Selectable> selected = event.getSelected();
				if(selected.size() == 0){
					view.getCoverageList().clearSelection();
				}
				
				for(Selectable s : selected) {
					ValueSelectable<Coverage> vs = (ValueSelectable<Coverage>)s;
					NavigationHistoryItem navig = NavigationHistoryManager.getInstance().getCurrentState();
					navig.setParameter("lineId", lineId);
					navig.setParameter("sublineid", vs.getValue().subLineId);
					navig.setParameter("coverageid", vs.getValue().id);
					NavigationHistoryManager.getInstance().go(navig);
					break;
				}
				
			}
		});

		bound = true;

	}
}
