package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.CoverageList;
import bigBang.module.generalSystemModule.client.userInterface.LineList;
import bigBang.module.generalSystemModule.client.userInterface.SubLineList;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.Coverage;
import bigBang.module.generalSystemModule.shared.Line;
import bigBang.module.generalSystemModule.shared.SubLine;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private LineList lineList;
	private SubLineList subLineList;
	private CoverageList coverageList;
	
	private LineForm lineForm;
	private SubLineForm subLineForm;
	private CoverageForm coverageForm;
	
	public CoverageManagementOperationView(){
		lineList = new LineList();
		subLineList = new SubLineList();
		coverageList = new CoverageList();
		
		lineForm = new LineForm();
		subLineForm = new SubLineForm();
		coverageForm = new CoverageForm();
		
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSize("100%", "100%");
		
		wrapper.add(lineList);
		wrapper.add(subLineList);
		wrapper.add(coverageList);
		
		wrapper.setCellWidth(lineList, "33%");
		wrapper.setCellWidth(subLineList, "33%");
		wrapper.setCellWidth(coverageList, "33%");
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
	public void setLines(Line[] lines) {
		this.lineList.clear();
		for(int i = 0; i < lines.length; i++) {
			ListEntry<Line> e = new ListEntry<Line>(lines[i]);
			e.setTitle(lines[i].name);
			e.setRightWidget(new Label(lines[i].categoryId));
			this.lineList.add(e);
		}
	}

	@Override
	public void setSubLines(SubLine[] subLines) {
		this.subLineList.clear();
		for(int i = 0; i < subLines.length; i++) {
			ListEntry<SubLine> e = new ListEntry<SubLine>(subLines[i]);
			e.setTitle(subLines[i].name);
			e.setRightWidget(new Label(subLines[i].coverages.length + " coberturas"));
			this.subLineList.add(e);
		}
	}

	@Override
	public void setCoverages(Coverage[] coverages) {
		this.coverageList.clear();
		for(int i = 0; i < coverages.length; i++) {
			ListEntry<Coverage> e = new ListEntry<Coverage>(coverages[i]);
			e.setTitle(coverages[i].name);
			this.coverageList.add(e);
		}
	}

	@Override
	public HasValue<Line> getLineform() {
		return lineForm;
	}

	@Override
	public HasValue<SubLine> getSubLineForm() {
		return subLineForm;
	}

	@Override
	public HasValue<Coverage> getCoverageForm() {
		return coverageForm;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		//this.coverageList.getNewButton().setEnabled(readOnly);
		//this.lineList.getNewButton().setEnabled(readOnly);
		//this.subLineList.getNewButton().setEnabled(readOnly);
		this.coverageForm.setReadOnly(readOnly);
		this.lineForm.setReadOnly(readOnly);
		this.subLineForm.setReadOnly(readOnly);
	}

	@Override
	public void clear() {
		this.coverageList.clear();
		this.lineList.clear();
		this.subLineList.clear();
		this.coverageForm.clearInfo();
		this.lineForm.clearInfo();
		this.subLineForm.clearInfo();
	}

}
