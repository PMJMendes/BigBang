package bigBang.module.generalSystemModule.client.userInterface.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.generalSystemModule.client.userInterface.presenter.CoverageManagementOperationViewPresenter;
import bigBang.module.generalSystemModule.shared.CoverageBranch;
import bigBang.module.generalSystemModule.shared.CoverageCategory;
import bigBang.module.generalSystemModule.shared.CoverageModality;

public class CoverageManagementOperationView extends View implements CoverageManagementOperationViewPresenter.Display {

	private final int LIST_WIDTH = 400; //PX
	
	private List<CoverageCategory> categoryList;
	private List<CoverageModality> modalityList;
	private List<CoverageBranch> branchList;
	private List<CoverageCategory> taxList;
	
	private CoverageCategoryForm coverageCategoryForm;
	private CoverageBranchForm coverageBranchForm;
	private CoverageModalityForm coverageModalityForm;

	private PopupPanel newEntryPopup;	
	
	public CoverageManagementOperationView(){
		HorizontalPanel wrapper = new HorizontalPanel();
		wrapper.setSize("100%", "100%");
		
		/*categoryList = new List<CoverageCategory>();
		ListHeader categoryListHeader = new ListHeader();
		categoryListHeader.showNewButton("Nova", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		categoryListHeader.setText("Categorias");
		categoryList.setHeaderWidget(categoryListHeader);
		
		branchList = new List<CoverageBranch>();
		ListHeader branchListHeader = new ListHeader();
		branchListHeader.setText("Ramos");
		branchListHeader.showNewButton("Novo", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		branchList.setHeaderWidget(branchListHeader);
		
		modalityList = new List<CoverageModality>();
		ListHeader modalityListHeader = new ListHeader();
		modalityListHeader.setText("Modalidades");
		modalityListHeader.showNewButton("Nova", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		modalityList.setHeaderWidget(modalityListHeader);
		
		taxList = new List<CoverageCategory>();
		ListHeader taxListHeader = new ListHeader();
		taxListHeader.setText("Impostos");
		taxListHeader.showNewButton("Novo", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		taxList.setHeaderWidget(taxListHeader);
		
		wrapper.add(categoryList);
		wrapper.setCellWidth(categoryList, "25%");
		wrapper.add(branchList);
		wrapper.setCellWidth(branchList, "25%");
		wrapper.add(modalityList);
		wrapper.setCellWidth(modalityList, "25%");
		wrapper.add(taxList);
		wrapper.setCellWidth(taxList, "25%");
		*/
		initWidget(wrapper);
	}
	
	void setListEntries(CoverageCategory[] categories) {
		
	}
	
}
