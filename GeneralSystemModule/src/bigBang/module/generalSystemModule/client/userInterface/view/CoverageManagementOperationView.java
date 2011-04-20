package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CoverageList;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private LineList lineList;
	private SubLineList subLineList;
	private CoverageList coverageList;
	
	private PopupPanel popup;
	
	public CoverageManagementOperationView(){
		lineList = new LineList();
		subLineList = new SubLineList();
		coverageList = new CoverageList();

		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSize("100%", "100%");
		
		wrapper.add(lineList);
		wrapper.add(subLineList);
		wrapper.add(coverageList);
		
		wrapper.setCellWidth(lineList, "33%");
		wrapper.setCellWidth(subLineList, "33%");
		wrapper.setCellWidth(coverageList, "33%");
		
		initWidget(wrapper);
	}
	
	@Override
	public HasValueSelectables<Line> getLineList() {
		return lineList;
	}

	@Override
	public HasValueSelectables<SubLine> getSubLineList() {
		return subLineList;
	}

	@Override
	public HasValueSelectables<Coverage> getCoverageList() {
		return coverageList;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.lineList.setReadOnly(readOnly);
	}

	@Override
	public void clear() {
		this.coverageList.clear();
		this.lineList.clear();
		this.subLineList.clear();
	}

	@Override
	public void refresh() {
		this.lineList.refresh();
	}

	@Override
	public void showSubLinesFor(Line line) {
		subLineList.clear();
		if(line == null)
			return;
		this.subLineList.setParentId(line.id);
		for(int i = 0; i < line.subLines.length; i++) {
			subLineList.add(new SubLineList.Entry(line.subLines[i]));
		}
	}

	@Override
	public void showCoveragesFor(SubLine subLine) {
		coverageList.clear();
		if(subLine == null)
			return;
		this.coverageList.setParentId(subLine.id);
		for(int i = 0; i < subLine.coverages.length; i++) {
			coverageList.add(new CoverageList.Entry(subLine.coverages[i]));
		}
	}

}
