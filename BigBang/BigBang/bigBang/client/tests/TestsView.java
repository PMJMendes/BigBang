package bigBang.client.tests;

import bigBang.library.client.userInterface.DatePickerFormField;
import bigBang.library.client.userInterface.NumericTextBoxFormField;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private DatePickerFormField dateField;
	private Button get = new Button("teste");
	private NumericTextBoxFormField num;
	public TestsView(){

		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		dateField = new DatePickerFormField("teste");
		num = new NumericTextBoxFormField("kaka");
		num.setValue(22.55);
		get.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println(dateField.getValue());
				Double teste = num.getValue();
				System.out.println(num.getValue());
			}
		});

		wrapper.add(dateField);
		wrapper.add(num);
		wrapper.add(get);
		
	}

	@Override
	protected void initializeView() {
		return;
	}


}
