package bigBang.client.tests;

import bigBang.library.client.ViewPresenterFactory;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.CollapsibleFormViewSection;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.FormViewSection;
import bigBang.library.client.userInterface.view.PopupPanel;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.Button;
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
		
		FormView<?> testForm = new FormView<Object>() {

			@Override
			public Object getInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setInfo(Object info) {
				// TODO Auto-generated method stub
				
			}
		};
		testForm.setSize("100%", "100%");
		testForm.addSection("TEST");

		FormViewSection testSection = new CollapsibleFormViewSection("O meu teste");
		Button button = new Button("Botao");
		button.setSize("250px", "250px");
		testSection.addWidget(button);
		testForm.addSection(testSection);
		
		testForm.addSection("Why hello");
		
		wrapper.add(testForm);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
