package bigBang.client.tests;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.InsurancePolicy.CoInsurer;
import bigBang.definitions.shared.TypifiedText;
import bigBang.library.client.HasParameters;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.TypifiedTextFormField;
import bigBang.library.client.userInterface.presenter.DocumentViewPresenter;
import bigBang.library.client.userInterface.view.DocumentView;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.clientModule.client.userInterface.presenter.ClientSectionViewPresenter.Action;
import bigBang.module.clientModule.client.userInterface.view.ClientSectionView;
import bigBang.module.insurancePolicyModule.client.userInterface.CoInsurerSelection;

public class TestsView extends View implements TestsViewPresenter.Display {

	private VerticalPanel wrapper = new VerticalPanel();
	private CoInsurerSelection form;
//	private VerticalPanel container = new VerticalPanel();
//
//	private PopupPanel popupPanel;
//
//	private ActionInvokedEventHandler<Action> actionHandler;

	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		form = new CoInsurerSelection();
		form.setSize("100%", "100%");
		Button testGet = new Button("Get Value");
		testGet.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				CoInsurer[] coInsurers = form.getValue();
				System.out.println();
				System.out.println();
				System.out.println();
				for(CoInsurer coInsurer : coInsurers){
					
					System.out.println(coInsurer.insuranceAgencyId);
					System.out.println(coInsurer.percent);
					
				}
				
			}
		});
		wrapper.add(form);
		wrapper.add(testGet);

	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setTypifiedTexts(String tag) {

	}

	@Override
	public HasWidgets getContainer() {
//		return container;
		return null;
	}

	@Override
	public void show() {
//		this.popupPanel = new PopupPanel(){
//			@Override
//			protected void onDetach() {
//				super.onDetach();
//				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.ON_OVERLAY_CLOSED));
//				TestsView.this.popupPanel = null;
//			}
//		};
//		this.popupPanel.add((Widget)this.container);
//		this.popupPanel.center();
	}

	@Override
	public void setMainCoInsuranceAgency(String string) {

		
		CoInsurer[] lol = new CoInsurer[3];
		lol[0] = new CoInsurer();
		lol[1] = new CoInsurer();
		lol[2] = new CoInsurer();
		
		lol[0].insuranceAgencyId = "4e61a352-31a1-4a65-91c7-9fb700200fb8";
		lol[0].percent = "45";
		
		lol[1].insuranceAgencyId = "4e61a352-31a1-4a65-91c7-9fb700200fb8";
		lol[1].percent = "45";
		
		lol[2].insuranceAgencyId = "4e61a352-31a1-4a65-91c7-9fb700200fb8";
		lol[2].percent = "45";
		
		
		form.setMainCoInsuranceAgency(string);
		form.setValue(lol);
		
		
		
	}



}
