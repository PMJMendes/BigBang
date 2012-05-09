package bigBang.client.tests;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
//	private ExpandableListBoxFormField requestType = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Documento");
//	private ExpandableListBoxFormField requestType2 = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.REQUEST_TYPE, "Tipo de Documento");
	//	private VerticalPanel container = new VerticalPanel();
	//
	//	private PopupPanel popupPanel;
	//
	//	private ActionInvokedEventHandler<Action> actionHandler;

	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		InsurancePolicySearchPanel list = new InsurancePolicySearchPanel();
		list.doSearch();
		list.setSize("100%", "100%");
		wrapper.add(list);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
