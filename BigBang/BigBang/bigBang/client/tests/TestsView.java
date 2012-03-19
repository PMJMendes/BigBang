package bigBang.client.tests;

import bigBang.definitions.shared.InsurancePolicy.CoInsurer;
import bigBang.library.client.HasParameters;
import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.ViewPresenterInstantiator;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.CoInsurerSelection;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.NegotiationExternalInfoRequestViewPresenter;
import bigBang.module.insurancePolicyModule.client.userInterface.view.NegotiationExternalInfoRequestView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
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
