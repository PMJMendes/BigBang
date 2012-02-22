package bigBang.client.tests;

import bigBang.definitions.shared.InsurancePolicy.CoInsurer;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.CoInsurerSelection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	private VerticalPanel wrapper = new VerticalPanel();
	private CoInsurerSelection form;
//	private VerticalPanel container = new VerticalPanel();
//
//	private PopupPanel popupPanel;
//
//	private ActionInvokedEventHandler<Action> actionHandler;
	
	private boolean bool = true;
	
	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		form = new CoInsurerSelection();
		form.setSize("100%", "100%");
		Button testGet = new Button("Get Value");
		testGet.addClickHandler(new ClickHandler() {


			@Override
			public void onClick(ClickEvent event) {
			
				if(bool){
					form.setMainCoInsuranceAgency("4e61a352-31a1-4a65-91c7-9fb700200fb8");
					bool = false;
				}else{
					bool = true;
					form.setMainCoInsuranceAgency("");
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

		
		CoInsurer[] lol = new CoInsurer[1];
		lol[0] = new CoInsurer();
		
		lol[0].insuranceAgencyId = "";
		lol[0].percent = "45";
		
		
		
		form.setMainCoInsuranceAgency(string);
		form.setValue(lol);

		
		
	}



}
