package bigBang.client.tests;

import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	private VerticalPanel wrapper = new VerticalPanel();
	//	private VerticalPanel container = new VerticalPanel();
	//
	//	private PopupPanel popupPanel;
	//
	//	private ActionInvokedEventHandler<Action> actionHandler;

	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		PopupPanel popup = new PopupPanel();
		ViewPresenter presenter = ViewPresenterFactory.getInstance().getViewPresenter("INSURANCE_POLICY_NEGOTIATION_RESPONSE");
//		ViewPresenter presenter = ViewPresenterFactory.getInstance().getViewPresenter("CLIENT_SEARCH");
	
//		HasParameters param = new HasParameters();
//		param.setParameter("clientid", "22f5cc61-e9f8-4141-801e-9fb70020135f");
		
	//	presenter.setParameters(param);
		
		presenter.go(popup);
		popup.center();
	}

	@Override
	protected void initializeView() {
		return;
	}


}
