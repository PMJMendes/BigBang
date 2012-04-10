package bigBang.module.casualtyModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CasualtySearchOperationViewPresenter implements ViewPresenter {

	public interface Display {
		
		
		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	
	public CasualtySearchOperationViewPresenter(View view){
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		// TODO Auto-generated method stub
		
	}
	
	public void bind() {
		if(bound)
			return;
		bound = true;
	}
}
