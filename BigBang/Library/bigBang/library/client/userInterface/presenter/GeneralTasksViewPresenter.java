package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.library.client.HasParameters;

public class GeneralTasksViewPresenter implements ViewPresenter {

	protected UIObject view;
	
	@Override
	public void setView(UIObject view) {
		this.view = view;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add((Widget) this.view);
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		return;
	}

}
