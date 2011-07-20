package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.user.client.ui.SplitLayoutPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ExpenseForm;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter;

public class ExpenseSearchOperationView extends View implements ExpenseSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 300; //PX 
	
	protected ExpenseSearchPanel searchPanel;
	protected ExpenseForm form;
	
	public ExpenseSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new ExpenseSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		form = new ExpenseForm();
		form.setSize("100%", "100%");
		
		mainWrapper.add(form);
		
		initWidget(mainWrapper);
	}
	
}
