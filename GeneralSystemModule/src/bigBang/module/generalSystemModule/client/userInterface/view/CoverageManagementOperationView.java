package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.definitions.shared.Coverage;
import bigBang.definitions.shared.Line;
import bigBang.definitions.shared.SubLine;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CoverageList;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private LineList lineList;
	private SubLineList subLineList;
	private CoverageList coverageList;
	
	public CoverageManagementOperationView(){
		lineList = new LineList();
		subLineList = new SubLineList(lineList);
		coverageList = new CoverageList(subLineList);

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
		this.coverageList.clearFilters();
		this.lineList.clear();
		this.lineList.clearFilters();
		this.subLineList.clear();
		this.subLineList.clearFilters();
	}

	@Override
	public void refresh() {
		this.subLineList.clear();
		this.coverageList.clear();
		this.lineList.refresh();
	}

	@Override
	public void showSubLinesFor(Line line) {
		subLineList.clear();
		coverageList.clear();
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
