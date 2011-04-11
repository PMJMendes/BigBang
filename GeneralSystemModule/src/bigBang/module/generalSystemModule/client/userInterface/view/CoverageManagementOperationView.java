package bigBang.module.generalSystemModule.client.userInterface.view;

import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.userInterface.SimpleEditableList;
import bigBang.library.client.userInterface.SimpleEditableListEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;
import bigBang.module.generalSystemModule.shared.CoverageTax;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private SimpleEditableList<CoverageCategory> categoryList;
	private SimpleEditableList<CoverageModality> modalityList;
	private SimpleEditableList<CoverageBranch> branchList;
	private SimpleEditableList<CoverageTax> taxList;

	public CoverageManagementOperationView(){
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSize("100%", "100%");
		
		categoryList = new SimpleEditableList<CoverageCategory>("Categorias");
		modalityList = new SimpleEditableList<CoverageModality>("Modalidades");
		branchList = new SimpleEditableList<CoverageBranch>("Ramos");
		taxList =new SimpleEditableList<CoverageTax>("Impostos");
		
		categoryList.setSize("100%", "100%");
		modalityList.setSize("100%", "100%");
		branchList.setSize("100%", "100%");
		taxList.setSize("100%", "100%");
		
		categoryList.getElement().getStyle().setProperty("borderRight", "1px solid gray");
		modalityList.getElement().getStyle().setProperty("borderRight", "1px solid gray");
		branchList.getElement().getStyle().setProperty("borderRight", "1px solid gray");
		taxList.getElement().getStyle().setProperty("borderRight", "1px solid gray");
		
		wrapper.add(categoryList);
		wrapper.add(branchList);
		wrapper.add(modalityList);
		wrapper.add(taxList);
		
		wrapper.setCellWidth(categoryList, "25%");
		wrapper.setCellWidth(modalityList, "25%");
		wrapper.setCellWidth(branchList, "25%");
		wrapper.setCellWidth(taxList, "25%");
		
		initWidget(wrapper);
	}

	@Override
	public HasValueSelectables<CoverageCategory> getCategoryList() {
		return this.categoryList;
	}

	@Override
	public HasValueSelectables<CoverageBranch> getBranchList() {
		return this.branchList;
	}

	@Override
	public HasValueSelectables<CoverageModality> getModalityList() {
		return this.modalityList;
	}

	@Override
	public HasValueSelectables<CoverageTax> getTaxList() {
		return this.taxList;
	}

	@Override
	public void setCategories(CoverageCategory[] c) {
		for(int i = 0; i < c.length; i++){
			SimpleEditableListEntry<CoverageCategory> e = new SimpleEditableListEntry<CoverageCategory>(c[i]);
			e.setTitle(c[i].name);
			e.setRightWidget(new Label(c[i].branches.length + ""));
			this.categoryList.add(e);
		}
	}

	@Override
	public void setBranches(CoverageBranch[] b) {
		for(int i = 0; i < b.length; i++){
			SimpleEditableListEntry<CoverageBranch> e = new SimpleEditableListEntry<CoverageBranch>(b[i]);
			e.setTitle(b[i].name);
			e.setRightWidget(new Label(b[i].modalities.length + ""));
			this.branchList.add(e);
		}
	}

	@Override
	public void setModalities(CoverageModality[] m) {
		for(int i = 0; i < m.length; i++){
			SimpleEditableListEntry<CoverageModality> e = new SimpleEditableListEntry<CoverageModality>(m[i]);
			e.setTitle(m[i].name);
			e.setRightWidget(new Label(m[i].taxes.length + ""));
			this.modalityList.add(e);
		}
	}

	@Override
	public void setTaxes(CoverageTax[] t) {
		for(int i = 0; i < t.length; i++){
			SimpleEditableListEntry<CoverageTax> e = new SimpleEditableListEntry<CoverageTax>(t[i]);
			e.setTitle(t[i].name);
			e.setRightWidget(new Label(t[i].value + ""));
			this.taxList.add(e);
		}
	}
	
	
}
