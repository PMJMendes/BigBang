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
		subLineList = new SubLineList();
		coverageList = new CoverageList();

		HorizontalPanel wrapper = new HorizontalPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		wrapper.add(lineList);
		wrapper.add(subLineList);
		wrapper.add(coverageList);
		
		wrapper.setCellWidth(lineList, "33%");
		wrapper.setCellWidth(subLineList, "33%");
		wrapper.setCellWidth(coverageList, "33%");
	}
	
	@Override
	protected void initializeView() {
		return;
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
		this.subLineList.setReadOnly(readOnly);
		this.coverageList.setReadOnly(readOnly);
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
	public void setLine(String lineId) {
	
		clear();
		
		coverageList.setId(null);
		coverageList.setSubLineId(null);
		coverageList.setLineId(lineId);
		
		subLineList.setId(null);
		subLineList.setLineId(lineId);
		
		lineList.setId(lineId);
		lineList.setLines(lineId);


	}

	@Override
	public void setSubLine(String lineId, String subLineId) {
		
		clear();
		
		coverageList.setId(null);
		coverageList.setLineId(lineId);
		coverageList.setSubLineId(subLineId);
		
		lineList.setId(lineId);
		
		subLineList.setId(subLineId);
		subLineList.setLineId(lineId);
		subLineList.setSubLines(lineId, subLineId);
		
	}

	@Override
	public void setCoverage(String lineId, String subLineId, String coverageId) {
		
		clear();
		
		lineList.setId(lineId);
		
		subLineList.setLineId(lineId);
		subLineList.setId(subLineId);
		
		coverageList.setId(coverageId);
		coverageList.setLineId(lineId);
		coverageList.setSubLineId(subLineId);
		coverageList.setCoverages(lineId, subLineId, coverageId);
		
	}

}
