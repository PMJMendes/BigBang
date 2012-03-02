package bigBang.module.riskAnalisysModule.client.userInterface.presenter;

import bigBang.library.client.HasParameters;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class RiskAnalisysSearchOperationViewPresenter implements ViewPresenter {

	public interface Display {
		Widget asWidget();
	}

	protected Display view;
	protected boolean bound;

	public RiskAnalisysSearchOperationViewPresenter(Display view) {
		setView((UIObject)view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		//TODO
	}
	
	public void bind() {
		if(bound)
			return;
		
		//APPLICATION-WIDE EVENTS
		bound = true;
	}

}
