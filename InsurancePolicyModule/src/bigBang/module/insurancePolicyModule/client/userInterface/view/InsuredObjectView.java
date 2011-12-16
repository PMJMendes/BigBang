package bigBang.module.insurancePolicyModule.client.userInterface.view;

import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsuredObjectOperationsToolbar;

public class InsuredObjectView extends View {

	protected InsuredObjectForm form;
	
	public InsuredObjectView(){
		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		InsuredObjectOperationsToolbar toolbar = new InsuredObjectOperationsToolbar(){

			@Override
			public void onEditRequest() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSaveRequest() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCancelRequest() {
				// TODO Auto-generated method stub
				
			}};
		
		form = new InsuredObjectForm();
		form.setSize("100%", "100%");
		wrapper.add(form);

		initWidget(wrapper);
	}
	
}
