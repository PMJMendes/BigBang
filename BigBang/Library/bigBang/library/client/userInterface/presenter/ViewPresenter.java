package bigBang.library.client.userInterface.presenter;

import bigBang.library.client.HasParameters;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;

public interface ViewPresenter {	
	
	public void setView(UIObject view);
	
	public void go(final HasWidgets container);
	
	public void setParameters(HasParameters parameterHolder);
	
}
