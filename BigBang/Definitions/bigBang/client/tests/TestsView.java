package bigBang.client.tests;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.form.SerialExpenseCreationForm;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TestsView extends View implements TestsViewPresenter.Display {

	
	private VerticalPanel wrapper = new VerticalPanel();
	private SerialExpenseCreationForm form;
	public TestsView(){
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		form = new SerialExpenseCreationForm(){

			@Override
			protected void noSubPolicyChangedState() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onSubPolicyValueChanged() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onChangedPolicyNumber() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onClickVerifyPolicyNumber() {
				// TODO Auto-generated method stub
				
			}

			@Override
			protected void onClickMarkAsInvalid() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		wrapper.add(form.getNonScrollableContent());
	}

	@Override
	protected void initializeView() {
		return;
	}


}
