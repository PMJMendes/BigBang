package bigBang.module.insurancePolicyModule.client.userInterface.view;

import bigBang.library.client.userInterface.view.View;
import bigBang.module.insurancePolicyModule.client.userInterface.presenter.InsurancePolicySectionViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

public class InsurancePolicySectionView extends View implements InsurancePolicySectionViewPresenter.Display {
	
private HasWidgets wrapper;
	
	public InsurancePolicySectionView(){
		SimplePanel wrapper = new SimplePanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		
		this.wrapper = wrapper;
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasWidgets getContainer() {
		return this.wrapper;
	}

}
